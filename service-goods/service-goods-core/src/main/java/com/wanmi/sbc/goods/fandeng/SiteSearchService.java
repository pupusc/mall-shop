package com.wanmi.sbc.goods.fandeng;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.GoodsChannelTypeEnum;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.fandeng.model.SyncBookPkgMetaReq;
import com.wanmi.sbc.goods.fandeng.model.SyncBookPkgReqVO;
import com.wanmi.sbc.goods.fandeng.model.SyncBookResMetaReq;
import com.wanmi.sbc.goods.fandeng.model.SyncBookResReqVO;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.mq.ProducerService;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.repository.GoodsPropRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 主站搜索
 * @date 2022-03-15 22:24:00
 */
@RefreshScope
@Slf4j
@Service
public class SiteSearchService {
    @Value("${site.search.goods.jump.url}")
    private String goodsJumpUrl;

    @Value("${site.search.goods.heart.url:null}")
    private String heartJumpUrl;

    @Value("${site.search.pkg.jump.url}")
    private String packageJumpUrl;

    @Value("${site.search.goods.sync.cate}")
    private Integer goodsSyncCate;

    @Value("${site.search.pkg.sync.type}")
    private Integer packageSyncType;

    @Value("${site.search.goods.sync.url}")
    private String syncBookResUrl;

    @Value("${site.search.pkg.sync.url}")
    private String syncBookPkgUrl;

    @Autowired
    private FddsOpenPlatformService fddsOpenPlatformService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsImageRepository goodsImageRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private GoodsPropRepository goodsPropRepository;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private BookListModelRepository bookListModelRepository;

    @Autowired
    private BookListGoodsPublishService bookListGoodsPublishService;

    @Autowired
    private ProducerService producerService;


    public void siteSearchBookResNotify(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("同步的书籍id为空");
            return;
        }

        SiteSearchNotifyModel model = new SiteSearchNotifyModel();
        model.setType("BOOK_RES");
        model.setIds(ids);
        producerService.siteSearchDataNotify(model);
    }

    public void siteSearchBookPkgNotify(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("同步的书单id为空");
            return;
        }
        SiteSearchNotifyModel model = new SiteSearchNotifyModel();
        model.setType("BOOK_PKG");
        model.setIds(ids.stream().map(item -> item.toString()).collect(Collectors.toList()));
        producerService.siteSearchDataNotify(model);
    }

    public void siteSearchDataConsumer(SiteSearchNotifyModel model) {
        if (Objects.isNull(model)) {
            log.info("站内搜索同步消息内容为空");
        }

        if ("BOOK_RES".equals(model.getType())) {
            sendBookResData(model.getIds());
            return;
        }

        if ("BOOK_PKG".equals(model.getType()) && CollectionUtils.isNotEmpty(model.getIds())) {
            for (String id : model.getIds()) {
                sendBookPkgData(Integer.valueOf(id));
            }
            return;
        }
    }

    private void syncBookResData(@Valid SyncBookResReqVO reqVO) {
        String json = JSON.toJSONString(reqVO);
        log.info("--->>>同步书籍信息到站内搜索系统, data = {}", json);
        fddsOpenPlatformService.doRequest(syncBookResUrl, json);
    }

    private void syncBookPkgData(@Valid SyncBookPkgReqVO reqVO) {
        String json = JSON.toJSONString(reqVO);
        log.info("--->>>同步书单信息到站内搜索系统, data = {}", json);
        fddsOpenPlatformService.doRequest(syncBookPkgUrl, json);
    }

    private void sendBookResData(List<String> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            log.error("商品goodsIds参数为空");
            return;
        }

        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(goodsIds);
        if (CollectionUtils.isEmpty(goodsList)) {
            log.error("商品数据没有查询到, goodsIds = {}", JSON.toJSONString(goodsIds));
            return;
        }

        SyncBookResReqVO resReqVO = new SyncBookResReqVO();
        resReqVO.setPaperBooks(Lists.newArrayList());

        for (Goods goods : goodsList) {
            SyncBookResMetaReq resMeta = new SyncBookResMetaReq();
            resReqVO.getPaperBooks().add(resMeta);

            resMeta.setBookId(goods.getGoodsId());
            resMeta.setIcon(goods.getGoodsImg());
            resMeta.setSmallIcon(null);
            resMeta.setGoodsName(goods.getGoodsName());
            resMeta.setTitle(goods.getGoodsName());
            resMeta.setSubtitle(goods.getGoodsSubtitle());
            //resMeta.setRankAndDec(null);
            resMeta.setJumpUrl(goodsJumpUrl + goods.getGoodsId());

            resMeta.setLabels(Lists.newArrayList());
            resMeta.setContent(null);
            resMeta.setSortFactor(0D);
            resMeta.setPublishStatus(getBookPublishStatus(goods));
            //下架商品不再填充信息
            if (!Integer.valueOf(1).equals(resMeta.getPublishStatus())) {
                log.info("同步书籍单本为下架状态，goodsId = {}, goodsInfo = {}", goods.getGoodsId(), JSON.toJSONString(goods));
                continue;
            }

            if (Boolean.TRUE.equals(goods.getAddedTimingFlag())) {
                resMeta.setPublishStatus(1);
                resMeta.setResPublishStart(goods.getAddedTimingTime());
                resMeta.setResPublishEnd(goods.getAddedTimingTime().plusYears(10L));
            }

            //查询商品图片
            //List<GoodsImage> goodsImages = goodsImageRepository.findByGoodsId(goods.getGoodsId());

            //查询出版信息
            List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId());
            Map<Long, List<GoodsPropDetailRel>> idRel = goodsPropDetailRels.stream().collect(Collectors.groupingBy(GoodsPropDetailRel::getPropId));
            List<GoodsProp> props = goodsPropRepository.findAllByPropIdIn(new ArrayList<>(idRel.keySet()));
            if(CollectionUtils.isNotEmpty(props)){
                for (GoodsProp prop : props) {
                    List<GoodsPropDetailRel> goodsPropDetailRelVos = idRel.get(prop.getPropId());
                    for (GoodsPropDetailRel goodsPropDetailRelVo : goodsPropDetailRelVos) {
                        goodsPropDetailRelVo.setPropName(prop.getPropName());
                        goodsPropDetailRelVo.setPropType(prop.getPropType());
                    }
                }
            }
            for (GoodsPropDetailRel rel : goodsPropDetailRels) {
                if ("作者".equals(rel.getPropName())) {
                    resMeta.setAuthorName(rel.getPropValue());
                }
                if ("出版社".equals(rel.getPropName())) {
                    resMeta.setPublishHouse(rel.getPropValue());
                }
                if ("评分".equals(rel.getPropName())) {
                    resMeta.setHeartPick(Double.valueOf(rel.getPropValue()));
                    resMeta.setHeartJumpUrl(heartJumpUrl);
                }
                if ("定价".equals(rel.getPropName())) {
                    resMeta.setReservePrice(Double.valueOf(rel.getPropValue()));
                }
            }
            //查询SKU列表
            GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
            infoQueryRequest.setGoodsId(goods.getGoodsId());
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(goodsInfos) && Objects.nonNull(goodsInfos.get(0).getMarketPrice())) {
                GoodsInfo sku = goodsInfos.get(0);
                resMeta.setSellPrice(sku.getMarketPrice().doubleValue());
                resMeta.setPromotePrice(sku.getMarketPrice().doubleValue());
                resMeta.setMemberPrice(sku.getMarketPrice().multiply(new BigDecimal(0.96)).doubleValue());
            }
        }

        syncBookResData(resReqVO);
    }

    private void sendBookPkgData(Integer pkgId) {
        if (Objects.isNull(pkgId)) {
            log.error("书单id参数为空");
            return;
        }

        BookListModelDTO pkgDto = bookListModelRepository.findById(pkgId).get();
        if (Objects.isNull(pkgDto)) {
            log.error("书单信息没有找到, id = {}", pkgId);
            return;
        }

        List<BookListGoodsPublishDTO> publishList = bookListGoodsPublishService.list(null, pkgId, CategoryEnum.BOOK_LIST_MODEL.getCode(), null, "xxoo");

        SyncBookPkgMetaReq pkgMeta = new SyncBookPkgMetaReq();
        pkgMeta.setPackageId(pkgDto.getId().toString());
        pkgMeta.setTitle(pkgDto.getName());
        pkgMeta.setSubTitle(null);
        pkgMeta.setCoverImage(pkgDto.getHeadImgUrl()); //headSquareImgUrl
        pkgMeta.setBookCount(CollectionUtils.isEmpty(publishList) ? null : publishList.size());
        pkgMeta.setJumpUrl(packageJumpUrl + pkgDto.getId());
        pkgMeta.setContent(pkgDto.getDesc());
        pkgMeta.setPayCount(null);
        pkgMeta.setPublishStatus(getBookPkgPublishStatus(pkgDto));
        pkgMeta.setResPublishStart(null);
        pkgMeta.setResPublishEnd(null);
        //下架商品不再填充信息
        if (!Integer.valueOf(1).equals(pkgMeta.getPublishStatus())) {
            log.info("同步书籍书单为下架状态，goodsId = {}, goodsInfo = {}", pkgDto.getId(), JSON.toJSONString(pkgDto));
        }

        SyncBookPkgReqVO reqVO = new SyncBookPkgReqVO();
        reqVO.setBookPackages(Arrays.asList(pkgMeta));
        syncBookPkgData(reqVO);
    }

    /**
     * 1、下架状态
     * 2、指定分类
     * 3、删除标志
     * 4、渠道主站
     * 5、审核状态
     */
    private Integer getBookPublishStatus(Goods goods) {
        //已删除
        if (DeleteFlag.YES.equals(goods.getDelFlag())) {
            return 0;
        }
        //审核状态
        if (!CheckStatus.CHECKED.equals(goods.getAuditStatus())) {
            return 0;
        }
        //上架状态
        if (!Integer.valueOf(1).equals(goods.getAddedFlag()) && !Boolean.TRUE.equals(goods.getAddedTimingFlag())) {
            return 0;
        }

        //是否是主站商品
        if (StringUtils.isBlank(goods.getGoodsChannelType())) {
            return 0;
        }
        String[] channelArr = goods.getGoodsChannelType().split(",");
        if (!Arrays.stream(channelArr).filter(item -> GoodsChannelTypeEnum.FDDS_DELIVER.getCode().equals(item)).findFirst().isPresent()) {
            return 0;
        }

        if (goodsSyncCate > 0 && !goodsSyncCate.equals(goods.getCateId())) {
            return 0;
        }

        Optional<GoodsCate> goodsCate = goodsCateRepository.findById(goods.getCateId());
        //不是书籍分类
        if (!goodsCate.isPresent() || !Integer.valueOf(1).equals(goodsCate.get().getBookFlag())) {
            return 0;
        }

        return 1;
    }

    /**
     * 1、下架状态
     * 2、指定分类
     * 3、删除标志
     * 4、渠道主站
     */
    private Integer getBookPkgPublishStatus(BookListModelDTO pkgDto) {
        if (DeleteFlagEnum.DELETE.getCode().equals(pkgDto.getDelFlag())) {
            return 0;
        }
        if (!Integer.valueOf(2).equals(pkgDto.getPublishState())) {
            return 0;
        }
        if (!packageSyncType.equals(pkgDto.getBusinessType())) {
            return 0;
        }
        return 1;
    }
}
