package com.wanmi.sbc.goods.fandeng;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.GoodsChannelTypeEnum;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Value("${site.search.goods.heart.url}")
    private String heartJumpUrl;

    @Value("${site.search.pkg.jump.url}")
    private String packageJumpUrl;

    @Value("${site.search.pkg.sync.type}")
    private Integer packageSyncType;

    private String syncBookResUrl = "/search/v100/syncPaperBookData";
    private String syncBookPkgUrl = "/search/v100/syncBookListData";

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
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private BookListModelRepository bookListModelRepository;

    @Autowired
    private BookListGoodsPublishRepository bookListGoodsPublishRepository;

    @Autowired
    private BookListGoodsPublishService bookListGoodsPublishService;

    public void syncBookResData(@Valid SyncBookResReqVO reqVO) {
        String json = JSON.toJSONString(reqVO);
        log.info("--->>>同步书籍信息到站内搜索系统, data = {}", json);
        fddsOpenPlatformService.doRequest(syncBookResUrl, json);
    }

    public void syncBookPkgData(@Valid SyncBookPkgReqVO reqVO) {
        String json = JSON.toJSONString(reqVO);
        log.info("--->>>同步书单信息到站内搜索系统, data = {}", json);
        fddsOpenPlatformService.doRequest(syncBookPkgUrl, json);
    }

    /**
     * 1.商品上下架：主动上下架、定时上下架
     */

    /**
     * 新增书籍
     */
    public void createBook(Goods goods) {
        sendBookResDatas(Arrays.asList(goods.getGoodsId()));
    }

    private void sendBookResDatas(List<String> goodsIds) {
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
            resMeta.setSmallIcon(goods.getGoodsImg());
            resMeta.setGoodsName(goods.getGoodsName());
            resMeta.setTitle(goods.getGoodsName());
            resMeta.setSubtitle(goods.getGoodsSubtitle());
            //resMeta.setRankAndDec(null);
            resMeta.setJumpUrl(goodsJumpUrl);

            resMeta.setLabels(Lists.newArrayList());
            resMeta.setContent(null);
            resMeta.setSortFactor(0D);
            resMeta.setPublishStatus(getBookPublishStatus(goods));
            //下架商品不再填充信息
            if (!Integer.valueOf(1).equals(resMeta.getPublishStatus())) {
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
            }

            //查询SKU列表
            GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
            infoQueryRequest.setGoodsId(goods.getGoodsId());
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(goodsInfos)) {
                GoodsInfo sku = goodsInfos.get(0);
                resMeta.setPromotePrice(Objects.nonNull(sku.getSalePrice()) ? sku.getSalePrice().doubleValue() : null);
                resMeta.setMemberPrice(Objects.nonNull(sku.getSalePrice()) ? sku.getSalePrice().doubleValue() : null);
                resMeta.setSellPrice(Objects.nonNull(sku.getSalePrice()) ? sku.getSalePrice().doubleValue() : null);
                resMeta.setReservePrice(Objects.nonNull(sku.getMarketPrice()) ? sku.getMarketPrice().doubleValue() : null);
            }
        }

        syncBookResData(resReqVO);
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

        Optional<GoodsCate> goodsCate = goodsCateRepository.findById(goods.getCateId());
        //不是书籍分类
        if (!goodsCate.isPresent() || !Integer.valueOf(1).equals(goodsCate.get().getBookFlag())) {
            return 0;
        }
        return 1;
    }


    /**
     * 更新书籍
     */
    public void updateBook(Goods newGoods) {
        sendBookResDatas(Arrays.asList(newGoods.getGoodsId()));
    }

    /**
     * 删除书籍
     */
    public void deleteBook(List<String> goodsIds) {
        sendBookResDatas(goodsIds);
    }

    /**
     * 更新上下架
     * 如果商品下架，删除主站搜索
     */
    public void updateBookShelf(List<String> goodsIds) {
        sendBookResDatas(goodsIds);
    }

    /**
     * 更新商品分类
     * 如果变成非书籍分类，删除主站搜索
     */
    public void updateBookCate(List<String> goodsIds) {
        sendBookResDatas(goodsIds);
    }

    /**
     * 更新商品销售状态
     */
    public void updateBooKVendibility(List<String> goodsIds) {

    }

    /**
     * 新增书单
     */
    public void createBookPkg(BookListModelDTO bookListModelParam) {
        sendBookPkgData(bookListModelParam.getId());
    }

    /**
     * 更新书单
     */
    public void updateBookPkg(BookListModelProviderRequest bookListModelRequest) {
        sendBookPkgData(bookListModelRequest.getId());
    }

    /**
     * 删除书单
     */
    public void deleteBookPkg(Integer bookPkgId) {
        sendBookPkgData(bookPkgId);
    }

    /**
     * 更新书单发布
     */
    public void updateBookPkgPublish(Integer bookPkgId) {
        sendBookPkgData(bookPkgId);
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
        pkgMeta.setJumpUrl(packageJumpUrl);
        pkgMeta.setContent(pkgDto.getDesc());
        pkgMeta.setPayCount(null);
        pkgMeta.setPublishStatus(getBookPkgPublishStatus(pkgDto));
        pkgMeta.setResPublishStart(null);
        pkgMeta.setResPublishEnd(null);

        SyncBookPkgReqVO reqVO = new SyncBookPkgReqVO();
        reqVO.setBookPackages(Arrays.asList(pkgMeta));
        syncBookPkgData(reqVO);
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
