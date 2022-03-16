package com.wanmi.sbc.goods.fandeng;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Value("${site.search.heart.jump.url}")
    private String heartJumpUrl;

    @Value("${site.search.goods.jump.url}")
    private String goodsJumpUrl;

    @Value("${site.search.package.jump.url}")
    private String packageJumpUrl;

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

    @Resource
    private BookListModelRepository bookListModelRepository;

    private String syncBookResUrl = "/search/v100/syncPaperBookData";
    private String syncBookPkgUrl = "/search/v100/syncBookListData";

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
        Optional<GoodsCate> goodsCate = goodsCateRepository.findById(goods.getCateId());
        //不是书籍分类
        if (!goodsCate.isPresent() || !Integer.valueOf(1).equals(goodsCate.get().getBookFlag())) {
            return;
        }
        //不是上架状态
        if (!Boolean.TRUE.equals(goods.getAddedTimingFlag()) && !Integer.valueOf(1).equals(goods.getAddedFlag())) {
            return;
        }
        //审核状态
        if (!CheckStatus.CHECKED.equals(goods.getAuditStatus())) {
            return;
        }
        //是否是主站商品
        // TODO: 2022/3/16 过滤主站商品才推送
        sendBookResData(goods);
    }

    private void sendBookResData(Goods goods) {
        sendBookResData(Arrays.asList(goods));
    }

    private void sendBookResData(List<Goods> goodsList) {
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
            //resMeta.setContent();
            resMeta.setSortFactor(0D);
            resMeta.setPublishStatus(goods.getAddedFlag());
            if (Boolean.TRUE.equals(goods.getAddedTimingFlag())) {
                resMeta.setPublishStatus(1);
                resMeta.setResPublishStart(goods.getAddedTimingTime());
                resMeta.setResPublishEnd(goods.getAddedTimingTime().plusYears(10L));
            }

            Optional<GoodsCate> goodsCate = goodsCateRepository.findById(goods.getCateId());
            //不是书籍分类
            if (!goodsCate.isPresent() || !Integer.valueOf(1).equals(goodsCate.get().getBookFlag())) {
                resMeta.setPublishStatus(0);
                continue;
            }

            //下架商品不再填充信息
            if (!Integer.valueOf(1).equals(resMeta.getPublishStatus())) {
                continue;
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
     * 更新书籍
     */
    public void updateBook(Goods newGoods) {
        Goods oldGoods = goodsRepository.findById(newGoods.getGoodsId()).orElse(null);
        if (Objects.isNull(oldGoods)) {
            log.error("书籍信息查询不存在, goodsId = {}", newGoods.getGoodsId());
            return;
        }
        sendBookResData(oldGoods);
    }

    /**
     * 删除书籍
     */
    public void deleteBook(List<String> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return;
        }

        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(goodsIds);
        sendBookResData(goodsList);
    }

    /**
     * 更新上下架
     * 如果商品下架，删除主站搜索
     */
    public void updateBookShelf(List<String> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return;
        }

        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(goodsIds);
        sendBookResData(goodsList);
    }

    /**
     * 更新商品分类
     * 如果变成非书籍分类，删除主站搜索
     */
    public void updateBookCate(List<String> goodsIds) {
        if (CollectionUtils.isEmpty(goodsIds)) {
            return;
        }

        List<Goods> goodsList = goodsRepository.findAllByGoodsIdIn(goodsIds);
        sendBookResData(goodsList);
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

    private void sendBookPkgData(Integer id) {
        if (Objects.isNull(id)) {
            log.error("书单id参数为空");
            return;
        }

        BookListModelDTO pkgDto = bookListModelRepository.findById(id).get();
        if (Objects.isNull(pkgDto)) {
            log.error("书单信息没有找到, id = {}", id);
            return;
        }

        SyncBookPkgMetaReq pkgMeta = new SyncBookPkgMetaReq();
        pkgMeta.setPackageId(pkgDto.getId().toString());
        pkgMeta.setTitle(pkgDto.getName());
        pkgMeta.setSubTitle(null);
        pkgMeta.setCoverImage(pkgDto.getHeadImgUrl()); //headSquareImgUrl
        pkgMeta.setBookCount();
        pkgMeta.setJumpUrl(packageJumpUrl);
        pkgMeta.setContent(pkgDto.getDesc());
        pkgMeta.setPayCount(null);
        pkgMeta.setPublishStatus(getPublishStatus(pkgDto));
                
                
        if (Objects.equals(bookListModelDTO.getDelFlag(), DeleteFlagEnum.DELETE.getCode())) {
            throw new SbcRuntimeException(String.format("bookListModel id: %s is delete", id));
        }
        return bookListModelDTO;
    }

    /**
     * 1、下架状态
     * 2、指定分类
     * 3、删除标志
     * 4、渠道主站
     */
    private Integer getPublishStatus(BookListModelDTO pkgDto) {
        if (DeleteFlagEnum.DELETE.getCode().equals(pkgDto.getDelFlag())) {
            return 0;
        }
        if (!Integer.valueOf(2).equals(pkgDto.getPublishState())) {
            return 0;
        }
    }

    /**
     * 更新书单
     */
    public void updateBookPkg(BookListModelProviderRequest bookListModelRequest) {

    }

    /**
     * 删除书单
     */
    public void deleteBookPkg(Integer bookPkgId) {

    }

    /**
     * 更新书单发布
     */
    public void updateBookPkgPublish(Integer bookPkgId) {

    }
}
