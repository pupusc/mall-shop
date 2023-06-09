package com.wanmi.sbc.goods.fandeng;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListTypeEnum;
import com.wanmi.sbc.goods.api.enums.GoodsChannelTypeEnum;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import com.wanmi.sbc.goods.blacklist.repository.GoodsBlackListRepository;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.fandeng.model.SyncBookPkgMetaReq;
import com.wanmi.sbc.goods.fandeng.model.SyncBookPkgReqVO;
import com.wanmi.sbc.goods.fandeng.model.SyncBookResMetaLabelReq;
import com.wanmi.sbc.goods.fandeng.model.SyncBookResMetaReq;
import com.wanmi.sbc.goods.fandeng.model.SyncBookResReqVO;
import com.wanmi.sbc.goods.images.GoodsImage;
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
import com.wanmi.sbc.goods.tag.repository.TagRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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

    @Value("${site.search.goods.heart.url:#{null}}")
    private String heartJumpUrl;

    @Value("${site.search.pkg.jump.url}")
    private String packageJumpUrl;

    @Value("${site.search.goods.sync.cate:#{null}}")
    private List<Integer> goodsSyncCate;

    @Value("${site.search.pkg.sync.type}")
    private List<Integer> packageSyncType;

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

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private GoodsBlackListRepository goodsBlackListRepository;


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

        try {
            //适当延时
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
            resMeta.setJumpUrl(String.format(goodsJumpUrl, goods.getGoodsId()));

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
            //填充商品价格
            fillBookResPrice(resMeta, goods);

            //查询标签信息
            List<SyncBookResMetaLabelReq> labelReqs = tagRepository.findByGoods(goods.getGoodsId())
                    .stream().filter(item -> DeleteFlag.NO.equals(item.getDelFlag())).map(item -> {
                SyncBookResMetaLabelReq labelReq = new SyncBookResMetaLabelReq();
                labelReq.setType(3);
                labelReq.setName(item.getTagName());
                return labelReq;
            }).collect(Collectors.toList());
            resMeta.setLabels(labelReqs);
            resMeta.setExtendLabels(null);
        }

        syncBookResData(resReqVO);
    }

    private void fillBookResPrice(SyncBookResMetaReq resMeta, Goods goods) {
        if (Objects.isNull(resMeta) || Objects.isNull(goods)) {
            return;
        }
        //查询SKU列表
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsInfos) && Objects.nonNull(goodsInfos.get(0).getMarketPrice())) {
            GoodsInfo sku = goodsInfos.get(0);
            resMeta.setSellPrice(priceFormat(sku.getMarketPrice()));
            resMeta.setPromotePrice(priceFormat(sku.getMarketPrice()));
            resMeta.setMemberPrice(priceFormat(sku.getMarketPrice().multiply(new BigDecimal(0.96))));
        }

        //价格黑名单
        List<GoodsBlackListDTO> blackList = goodsBlackListRepository.selectBackListByBusiness(
                goods.getGoodsId(),
                GoodsBlackListTypeEnum.SPU_ID.getCode(),
                GoodsBlackListCategoryEnum.UN_SHOW_VIP_PRICE.getCode());
        if (CollectionUtils.isNotEmpty(blackList)) {
            resMeta.setPromotePrice(resMeta.getSellPrice());
            resMeta.setMemberPrice(resMeta.getSellPrice());
        }
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

        Map<String, GoodsInfo> skuId2ModelAllMap = new HashMap<>();
        Map<String, Goods> spuId2ModelAllMap = new HashMap<>();
        Map<String, GoodsImage> spuId2GoodsImageMap = new HashMap<>();
        if (publishList.size() > 0){
            List<String> spuIds = publishList.stream().map(BookListGoodsPublishDTO::getSpuId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(spuIds)){
                log.error("书单信息对应的spu信息为null, id = {}", pkgId);
            } else {
                List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsIdIn(spuIds);
                if (!CollectionUtils.isEmpty(goodsInfoList)) {
                    Map<String, GoodsInfo> skuId2ModelMap = goodsInfoList.stream()
                            .filter(e -> Objects.equals(e.getAddedFlag(), AddedFlag.YES.toValue()) && Objects.equals(e.getDelFlag(), DeleteFlag.NO))
                            .collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));
                    skuId2ModelAllMap.putAll(skuId2ModelMap);
                }
                List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(spuIds);
                if (!CollectionUtils.isEmpty(goodsList)) {
                    Map<String, Goods> spuId2ModelMap = goodsList.stream()
                            .filter(e -> Objects.equals(e.getAddedFlag(), AddedFlag.YES.toValue()) && Objects.equals(e.getDelFlag(), DeleteFlag.NO))
                            .collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (k1, k2) -> k1));
                    spuId2ModelAllMap.putAll(spuId2ModelMap);
                }

                List<GoodsImage> spuImages = goodsImageRepository.findByGoodsIds(spuIds);
                if (!org.springframework.util.CollectionUtils.isEmpty(spuImages)){
                    Map<String, GoodsImage> tempSpuId2GoodsMap = spuImages.stream()
                            .filter(ex -> Objects.equals(ex.getSort(), 0))
                            .collect(Collectors.toMap(GoodsImage::getGoodsId, Function.identity(), (k1,k2) -> k1));
                    spuId2GoodsImageMap.putAll(tempSpuId2GoodsMap);
                }
            }

        }
        
        SyncBookPkgMetaReq pkgMeta = new SyncBookPkgMetaReq();
        pkgMeta.setPackageId(pkgDto.getId().toString());
        pkgMeta.setTitle(pkgDto.getName());
        pkgMeta.setSubTitle(null);
        pkgMeta.setCoverImage(pkgDto.getHeadImgUrl()); //headSquareImgUrl
        pkgMeta.setBookCount(CollectionUtils.isEmpty(publishList) ? null : publishList.size());
        pkgMeta.setJumpUrl(String.format(packageJumpUrl, pkgDto.getId()));
        pkgMeta.setContent(pkgDto.getDesc());
        pkgMeta.setPayCount(null);
        pkgMeta.setPublishStatus(getBookPkgPublishStatus(pkgDto));
        pkgMeta.setResPublishStart(null);
        pkgMeta.setResPublishEnd(null);
        //下架商品不再填充信息
        if (!Integer.valueOf(1).equals(pkgMeta.getPublishStatus())) {
            log.info("同步书籍书单为下架状态，goodsId = {}, goodsInfo = {}", pkgDto.getId(), JSON.toJSONString(pkgDto));
        }

        List<SyncBookPkgMetaReq.Item> itemList = new ArrayList<>();
        for (BookListGoodsPublishDTO bookListGoodsPublishDTO : publishList) {
            SyncBookPkgMetaReq.Item item = new SyncBookPkgMetaReq.Item();
            item.setTid(bookListGoodsPublishDTO.getSpuId());
            Goods goods = spuId2ModelAllMap.get(bookListGoodsPublishDTO.getSpuId());
            item.setTitle(goods == null ? "" : goods.getGoodsName());
            GoodsInfo goodsInfo = skuId2ModelAllMap.get(bookListGoodsPublishDTO.getSkuId());
            String goodsInfoImgUrl = goodsInfo == null ? "" : goodsInfo.getGoodsInfoImg();
            if (StringUtils.isBlank(goodsInfoImgUrl)) {
                GoodsImage goodsImage = spuId2GoodsImageMap.get(bookListGoodsPublishDTO.getSpuId());
                goodsInfoImgUrl = goodsImage == null ? "" : goodsImage.getArtworkUrl();
            }

            item.setCoverImageUrl(goodsInfoImgUrl);
//            GoodsInfo goodsInfo = skuId2ModelAllMap.get(bookListGoodsPublishDTO.getSkuId());
//            item.setCoverImageUrl(goodsInfo == null ? "" : goodsInfo.getGoodsInfoImg());
//            item.setHasShow((goods == null || goodsInfo == null) ? 1 : 0);
            if (goods == null || goodsInfo == null) {
                continue;
            }
            itemList.add(item);
        }
        pkgMeta.setItems(itemList);


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
        if (!Arrays.stream(channelArr).filter(item -> GoodsChannelTypeEnum.MALL_H5.getCode().equals(item)).findFirst().isPresent()) {
            return 0;
        }

        if (Objects.nonNull(goodsSyncCate) && !goodsSyncCate.contains(goods.getCateId())) {
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
        if (Objects.nonNull(packageSyncType) && !packageSyncType.contains(pkgDto.getBusinessType())) {
            return 0;
        }
        return 1;
    }

    /**
     * 金额格式化
     */
    private Double priceFormat(BigDecimal price) {
        if (Objects.isNull(price)) {
            return null;
        }
        return price.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
