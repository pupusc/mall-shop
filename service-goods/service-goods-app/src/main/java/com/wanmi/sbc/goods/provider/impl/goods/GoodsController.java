package com.wanmi.sbc.goods.provider.impl.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdFigureBO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.CompanySourceType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreBycompanySourceType;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.goodsstock.GuanYiSyncGoodsStockRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.linkedmall.SyncItemRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsAddAllResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsAddResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsCheckResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsModifyAllResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsSynResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsDelResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallInitResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.SyncItemResponse;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.dto.*;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsTagVo;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.collect.RedisTagsConstant;
import com.wanmi.sbc.goods.fandeng.SiteSearchService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.request.GoodsPriceSyncQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.info.request.GoodsSaveRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsInfoStockService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.info.service.GoodsStockService;
import com.wanmi.sbc.goods.info.service.LinkedMallGoodsService;
import com.wanmi.sbc.goods.info.service.S2bGoodsService;
import com.wanmi.sbc.goods.mq.ProducerService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.service.StandardImportService;
import com.wanmi.sbc.goods.storecate.service.StoreCateService;
import com.wanmi.sbc.goods.tag.model.Tag;
import com.wanmi.sbc.goods.tag.service.TagService;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import com.wanmi.sbc.goods.thirdgoodscate.repository.ThirdGoodsCateRepository;
import com.wanmi.sbc.goods.thirdgoodscate.service.ThirdGoodsCateService;
import com.wanmi.sbc.linkedmall.api.provider.cate.LinkedMallCateQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.goods.LinkedMallGoodsQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.cate.CateChainByGoodsIdRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.LinkedMallGoodsPageRequest;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * com.wanmi.sbc.goods.provider.impl.goods.GoodsController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:20
 */
@RestController
@Validated
@Slf4j
public class GoodsController implements GoodsProvider {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StandardImportService standardImportService;

    @Autowired
    private S2bGoodsService s2bGoodsService;

    @Autowired
    private GoodsAresService goodsAresService;

    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    @Autowired
    private LinkedMallGoodsService linkedMallGoodsService;

    @Autowired
    private LinkedMallGoodsQueryProvider linkedMallGoodsQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreCateService storeCateService;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private ThirdGoodsCateService thirdGoodsCateService;

    @Autowired
    private LinkedMallCateQueryProvider linkedMallCateQueryProvider;

    @Autowired
    private ThirdGoodsCateRepository thirdGoodsCateRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private TagService tagService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private BookListGoodsPublishService bookListGoodsPublishService;

    @Autowired
    private SiteSearchService siteSearchService;

    public BaseResponse<List<GoodsTagVo>> tags(){
        List<Tag> tags = tagService.findAllTag();
        List<GoodsTagVo> vos = new ArrayList<>();
        for (Tag tag : tags) {
            GoodsTagVo goodsTagVo = new GoodsTagVo();
            BeanUtils.copyProperties(tag, goodsTagVo);
            vos.add(goodsTagVo);
        }
        return BaseResponse.success(vos);
    }

    /**
     * 新增商品
     * @param request {@link GoodsAddRequest}
     * @return 新增结果 {@link GoodsAddResponse}
     */
    @Override
    public BaseResponse<GoodsAddResponse> add(@RequestBody @Valid GoodsAddRequest request) {
        GoodsSaveRequest goodsSaveRequest = KsBeanUtil.convert(request, GoodsSaveRequest.class);
        List<String> goodsChannelTypeSet = request.getGoods().getGoodsChannelTypeSet();
        if (CollectionUtils.isEmpty(goodsChannelTypeSet)) {
            throw new SbcRuntimeException("K-0400001");
        }
        goodsSaveRequest.getGoods().setGoodsChannelType(String.join(",", goodsChannelTypeSet));
        String result = goodsService.add(goodsSaveRequest);

        GoodsAddResponse response = new GoodsAddResponse();
        response.setResult(result);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<LinkedMallInitResponse> initLinkedMallGoods() {
        List<String> standardIds = new ArrayList<>();
        int pageNum = 1;
        boolean isExit = true;
        //linkedmall供应商店铺
        StoreVO storeVO = storeQueryProvider.getBycompanySourceType(new StoreBycompanySourceType(CompanySourceType.LINKED_MALL)).getContext();
        //店铺默认分类
        List<Long> storeCates = storeCateService.query(storeVO.getStoreId()).stream()
                .filter(v -> DefaultFlag.YES.equals(v.getIsDefault()))
                .map(v -> v.getStoreCateId())
                .collect(Collectors.toList());
        log.info("开始同步linkedmall商品");
        while (isExit) {
            List<QueryBizItemListResponse.Item> items =
                    linkedMallGoodsQueryProvider.getLinkedMallGoodsPage(new LinkedMallGoodsPageRequest(pageNum, 20)).getContext().getContent();
            if (items != null && items.size() > 0) {
                List<QueryItemDetailResponse.Item> goodsDetailBatch =
                        linkedMallGoodsQueryProvider.getGoodsDetailBatch(new GoodsDetailQueryRequest(items.stream()
                                .map(v -> v.getItemId()).collect(Collectors.toList()))).getContext();
                for (QueryItemDetailResponse.Item item : goodsDetailBatch) {
                    standardIds.addAll(linkedMallGoodsService.initLinkedMallGoods(item, storeVO, storeCates).getStandardIds());
                }
                pageNum++;
            } else {
                isExit = false;
            }
        }
        log.info("linkedmall商品初始化完成");
        //刷新ES
        if (CollectionUtils.isNotEmpty(standardIds)) {
            producerService.initStandardByStandardIds(standardIds);
        }
        LinkedMallInitResponse initResponse = new LinkedMallInitResponse();
        initResponse.setStandardIds(standardIds);
        return BaseResponse.success(initResponse);
    }

    /**
     * 删除linkedmall商品，linkedmall删除接口回调失败，手动删除
     * @return
     */
    @Override
    public BaseResponse<List<String>> delLinkedMallGoods() {
        Map<String, List<String>> newLinkedMallGoods = new HashMap<>();
        int pageNum = 1;
        boolean nextPage = true;
        while (nextPage) {
            List<QueryBizItemListResponse.Item> items =
                    linkedMallGoodsQueryProvider.getLinkedMallGoodsPage(new LinkedMallGoodsPageRequest(pageNum, 20)).getContext().getContent();
            if (items != null && items.size() > 0) {
                newLinkedMallGoods.putAll(items.stream()
                        .collect(Collectors.toMap(v -> v.getItemId().toString(),
                                v -> v.getSkuList().stream().map(v1 -> v1.getSkuId().toString()).collect(Collectors.toList()))));
                pageNum++;
            } else {
                nextPage = false;
            }
        }
        if (newLinkedMallGoods.size() > 0) {
            return BaseResponse.success(linkedMallGoodsService.delLinkedMallGoods(newLinkedMallGoods));
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<SyncItemResponse> addLinkedMallGoods(@Valid SyncItemRequest request) {
        SyncItemResponse response = new SyncItemResponse();
        StoreVO storeVO = storeQueryProvider.getBycompanySourceType(new StoreBycompanySourceType(CompanySourceType.LINKED_MALL)).getContext();
        List<Long> storeDefaultCates = storeCateService.query(storeVO.getStoreId()).stream()
                .filter(v -> DefaultFlag.YES.equals(v.getIsDefault()))
                .map(v -> v.getStoreCateId())
                .collect(Collectors.toList());
        List<SyncItemRequest.LinkedMallItem> itemListEntity = request.getItemListEntity();
        for (SyncItemRequest.LinkedMallItem linkedMallItem : itemListEntity) {
            List<GetCategoryChainResponse.Category> categoryChain = linkedMallCateQueryProvider.getCategoryChainByGoodsId(new CateChainByGoodsIdRequest(linkedMallItem.getItemId())).getContext().getCategoryChain();
            //封装三方平台类目实体类
            List<ThirdGoodsCate> thirdGoodsCates = new ArrayList<>();
            for (int i = 0; i < categoryChain.size(); i++) {
                ThirdGoodsCate thirdGoodsCate = new ThirdGoodsCate();
                thirdGoodsCate.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                thirdGoodsCate.setDelFlag(DeleteFlag.NO);
                thirdGoodsCate.setCreateTime(LocalDateTime.now());
                thirdGoodsCate.setUpdateTime(LocalDateTime.now());
                thirdGoodsCate.setCateGrade(i + 1);
                thirdGoodsCate.setCateId(categoryChain.get(i).getCategoryId());
                thirdGoodsCate.setCateName(categoryChain.get(i).getName());
                thirdGoodsCate.setCatePath(catePath(i + 1, categoryChain));
                if (i > 0) {
                    thirdGoodsCate.setCateParentId(categoryChain.get(i - 1).getCategoryId());
                } else {
                    thirdGoodsCate.setCateParentId(0L);
                }
                thirdGoodsCates.add(thirdGoodsCate);
            }
            //防止批量新增同一类目下的商品，重复添加类目
            RLock cateLock = redissonClient.getFairLock(thirdGoodsCates.get(0).getCateId().toString());
            cateLock.lock();
            try {
                //查询当前新增商品的类目是否存在
                List<Long> oldCateIds = thirdGoodsCateService.getByCateIds(ThirdPlatformType.LINKED_MALL, thirdGoodsCates.stream().map(v -> v.getCateId()).collect(Collectors.toList()))
                        .stream()
                        .map(v -> v.getCateId())
                        .collect(Collectors.toList());
                List<ThirdGoodsCate> newCates = thirdGoodsCates.stream().filter(v -> !oldCateIds.contains(v.getCateId())).collect(Collectors.toList());
                //新增类目
                if (newCates != null && newCates.size() > 0) {
                    thirdGoodsCateRepository.saveAll(newCates);
                }
            }catch (Exception e){
                throw  e;
            }finally {
                cateLock.unlock();
            }
            RLock goodsLock = redissonClient.getFairLock(linkedMallItem.getItemId().toString());
            goodsLock.lock();
            try {

                List<String> standardIds = linkedMallGoodsService.addLinkedMallGoods(linkedMallItem.getCategoryId(), linkedMallItem.getItemId(), storeVO, storeDefaultCates);
                response.setStandardIds(standardIds);
            } catch (Exception e) {
                throw e;
            } finally {
                goodsLock.unlock();
            }

        }

        return BaseResponse.success(response);
    }

    private String catePath(int grade,List<GetCategoryChainResponse.Category> categoryChain) {
        String path = "0";
        if (grade == 1) {
            return path;
        } else {

            for (int i = 0; i < grade - 1; i++) {
                path += "|" + categoryChain.get(i).getCategoryId().toString();
            }
            return path;
        }
    }

    @Override
    public BaseResponse<LinkedMallGoodsModifyResponse> linkedmallModify(@Valid LinkedMallGoodsModifyRequest request) {
        List<String> esGoodsInfoIds = new ArrayList<>();
        List<String> standardIds = new ArrayList<>();
        for (LinkedMallItemModificationDTO linkedMallItemModificationDTO : request.getLinkedMallItemModificationDTOS()) {
            RLock fairLock = redissonClient.getFairLock(linkedMallItemModificationDTO.getItemId().toString());
            fairLock.lock();
            try {
                LinkedMallGoodsModifyResponse response = linkedMallGoodsService.updateLinkedmallGoods(linkedMallItemModificationDTO);
                esGoodsInfoIds.addAll(response.getGoodsInfoIds());
                standardIds.addAll(response.getStandardIds());
            } catch (Exception e) {
                throw e;
            } finally {
                fairLock.unlock();
            }
        }
        LinkedMallGoodsModifyResponse response = new LinkedMallGoodsModifyResponse();
        response.setGoodsInfoIds(esGoodsInfoIds);
        response.setStandardIds(standardIds);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<LinkedMallGoodsDelResponse> linkedmallDel(@Valid LinkedMallGoodsDelRequest request) {
        List<String> esGoodsInfoIds = new ArrayList<>();
        List<String> standardIds = new ArrayList<>();
        List<String> delStandardIds = new ArrayList<>();
        for (LinkedMallItemDelDTO linkedMallItemDelDTO : request.getLinkedMallItemDelDTOS()) {
            RLock fairLock = redissonClient.getFairLock(linkedMallItemDelDTO.getItemId().toString());
            fairLock.lock();
            try {
                LinkedMallGoodsDelResponse response = linkedMallGoodsService.linkedMallGoodsDel(linkedMallItemDelDTO);
                esGoodsInfoIds.addAll(response.getGoodsInfoIds());
                standardIds.addAll(response.getStandardIds());
                delStandardIds.addAll(response.getDelStandardIds());
            } catch (Exception e) {
                throw e;
            } finally {
                fairLock.unlock();
            }

        }
        LinkedMallGoodsDelResponse response = new LinkedMallGoodsDelResponse();
        response.setGoodsInfoIds(esGoodsInfoIds);
        response.setStandardIds(standardIds);
        response.setDelStandardIds(delStandardIds);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse vendibilityThirdGoods(@Valid ThirdGoodsVendibilityRequest request) {
        goodsService.vendibilityLinkedmallGoods(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改商品
     *
     * @param request {@link GoodsModifyRequest}
     * @return 修改结果 {@link GoodsModifyResponse}
     */
    @Override
    @Transactional
    public BaseResponse<GoodsModifyResponse> modify(@RequestBody @Valid GoodsModifyRequest request) {
        GoodsSaveRequest goodsSaveRequest = KsBeanUtil.convert(request, GoodsSaveRequest.class);
        List<String> goodsChannelTypeSet = request.getGoods().getGoodsChannelTypeSet();
        if (CollectionUtils.isEmpty(goodsChannelTypeSet)) {
            throw new SbcRuntimeException("K-0400001");
        }
        goodsSaveRequest.getGoods().setGoodsChannelType(String.join(",", goodsChannelTypeSet));
        goodsSaveRequest.getGoods().setAddedTimingTime(request.getGoods().getAddedTimingTime());
        Goods goods = goodsService.getGoodsById(goodsSaveRequest.getGoods().getGoodsId());
        Map<String, Object> map;
        GoodsModifyResponse response = new GoodsModifyResponse();
        if (0 == goods.getGoodsSource() && StringUtils.isEmpty(goods.getProviderGoodsId())) {
            // 供应商编辑商品
            map = goodsService.edit(goodsSaveRequest);
            //商品编辑后，同步商品库
            StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByGoodsId(goods.getGoodsId());
            if(Objects.nonNull(standardGoodsRel)) {
                standardImportService.synProviderGoods(goods.getGoodsId(), standardGoodsRel.getStandardId());
                response.setStandardIds(Collections.singletonList(standardGoodsRel.getStandardId()));
            } else {
                // 平台禁售供应商商品库商品后，商品库关系被删除com.wanmi.sbc.goods.info.service.S2bGoodsService.dealStandardGoods
                // 此时如果商品为审核通过状态，则同步到商品库
                Goods modifiedGoods = (Goods)map.get("oldGoods");
                if (Objects.equals(CheckStatus.CHECKED, modifiedGoods.getAuditStatus())){
                    // 同步到商品库
                    GoodsRequest synRequest = new GoodsRequest();
                    synRequest.setGoodsIds(Arrays.asList(modifiedGoods.getGoodsId()));
                    response.setStandardIds(standardImportService.importStandard(synRequest));
                }
            }
//            //更改商家代销商品的可售性
//            Boolean isDealGoodsVendibility = StringUtil.cast(map.get("isDealGoodsVendibility"), Boolean.class);
//            if(isDealGoodsVendibility != null && isDealGoodsVendibility){
//                goodsService.dealGoodsVendibility(ProviderGoodsNotSellRequest.builder()
//                        .checkFlag(Boolean.TRUE).stockFlag(Boolean.TRUE)
//                        .goodsIds(Lists.newArrayList(goods.getGoodsId())).build());
//            }
        } else {
            //同步代码
            /*商家商品编辑
            if(goodsSaveRequest.getGoods().getAddedFlag() == AddedFlag.YES.toValue()){
                // 如果是上架，要判断所属供应商商品是否是上架状态
                if (StringUtils.isNotEmpty(goods.getProviderGoodsId())) {
                    Goods providerGoods = goodsService.getGoodsById(goods.getProviderGoodsId());
                    if (providerGoods.getAddedFlag() == AddedFlag.NO.toValue()) {
                        // 所属供应商商品是下架，商家商品也修改为下架
                        goodsSaveRequest.getGoods().setAddedFlag(AddedFlag.NO.toValue());
                    }
                }
            }*/
            map = goodsService.edit(goodsSaveRequest);
        }

        //获取商品所在的书单，如果存在，则重新加载书单
        try {
            List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList = bookListGoodsPublishService.list(
                    null, null, CategoryEnum.BOOK_LIST_MODEL.getCode(), goodsSaveRequest.getGoods().getGoodsId(), "xxoo");
            if (CollectionUtils.isNotEmpty(bookListGoodsPublishDTOList)) {
                List<Integer> bookListModelIdList = bookListGoodsPublishDTOList.stream().map(BookListGoodsPublishDTO::getBookListId).collect(Collectors.toList());
                siteSearchService.siteSearchBookPkgNotify(bookListModelIdList);
            }
        } catch (Exception ex) {
            log.error("GoodsController modify exception", ex);
        }

//        //ares埋点-商品-后台修改商品sku
//        goodsAresService.dispatchFunction("editGoodsSku",
//                new Object[]{map.get("newGoodsInfo"), map.get("delInfoIds"), map.get("oldGoodsInfos"), map.get("storeGoodsInfoIds")});

        response.setReturnMap(map);
        return BaseResponse.success(response);
    }

    /**
     * 为商品设置ISBN
     */
    @Override
    public BaseResponse<List<Object[]>> setExtPropForGoods(List<Object[]> props) {
        List list = goodsService.setExtPropForGoods(props);
        return BaseResponse.success(list);
    }

    /**
     * 新增商品定价
     *
     * @param request {@link GoodsAddPriceRequest}
     */
    @Override

    public BaseResponse addPrice(@RequestBody @Valid GoodsAddPriceRequest request) {
        GoodsSaveRequest goodsSaveRequest = KsBeanUtil.convert(request, GoodsSaveRequest.class);
        goodsService.savePrice(goodsSaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增商品基本信息、基价
     *
     * @param request {@link GoodsAddAllRequest}
     * @return 商品编号 {@link GoodsAddAllResponse}
     */
    @Override

    public BaseResponse<GoodsAddAllResponse> addAll(@RequestBody @Valid GoodsAddAllRequest request) {
        GoodsSaveRequest goodsSaveRequest = KsBeanUtil.convert(request, GoodsSaveRequest.class);

        if (CollectionUtils.isNotEmpty(request.getGoods().getGoodsChannelTypeSet())) {
            goodsSaveRequest.getGoods().setGoodsChannelType(String.join(",", request.getGoods().getGoodsChannelTypeSet()));
        }
        String goodsId = goodsService.addAll(goodsSaveRequest);

        GoodsAddAllResponse response = new GoodsAddAllResponse();
        response.setGoodsId(goodsId);
        return BaseResponse.success(response);
    }

    /**
     * 修改商品基本信息、基价
     *
     * @param request {@link GoodsModifyAllRequest}
     * @return 修改结果 {@link GoodsModifyAllResponse}
     */
    @Override
    public BaseResponse<GoodsModifyAllResponse> modifyAll(@RequestBody @Valid GoodsModifyAllRequest request) {
        GoodsSaveRequest goodsSaveRequest = KsBeanUtil.convert(request, GoodsSaveRequest.class);
        Map<String, Object> map = goodsService.editAll(goodsSaveRequest);
        //更改商家代销商品的可售性
//        Boolean isDealGoodsVendibility = StringUtil.cast(map.get("isDealGoodsVendibility"), Boolean.class);
//        if(isDealGoodsVendibility != null && isDealGoodsVendibility){
//            goodsService.dealGoodsVendibility(ProviderGoodsNotSellRequest.builder().checkFlag(Boolean.TRUE).stockFlag(Boolean.TRUE)
//                    .goodsIds(Lists.newArrayList(request.getGoods().getGoodsId())).build());
//        }
        //ares埋点-商品-后台修改商品sku
        goodsAresService.dispatchFunction("editGoodsSku",
                new Object[]{map.get("newGoodsInfo"), map.get("delInfoIds"), map.get("oldGoodsInfos")});

        GoodsModifyAllResponse response = new GoodsModifyAllResponse();
        response.setResultMap(map);
        return BaseResponse.success(response);
    }

    /**
     * 修改商品基本信息、基价
     *
     * @param request {@link GoodsDeleteByIdsRequest}
     */
    @Override
    public BaseResponse deleteByIds(@RequestBody @Valid GoodsDeleteByIdsRequest request) {
        List<String> goodsIdList = request.getGoodsIds();
        goodsService.delete(goodsIdList, request.getStoreId(),request.getUpdatePerson());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 供应商删除商品
     *
     * @param request {@link GoodsDeleteByIdsRequest}
     */
    @Override
    public BaseResponse deleteProviderGoodsByIds(@RequestBody @Valid GoodsDeleteByIdsRequest request) {

        goodsService.deleteProvider(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品上下架状态
     *
     * @param request {@link GoodsModifyAddedStatusRequest}
     */
    @Override

    public BaseResponse modifyAddedStatus(@RequestBody @Valid GoodsModifyAddedStatusRequest request) {
        Integer addedFlag = request.getAddedFlag();
        List<String> goodsIdList = request.getGoodsIds();
        goodsService.updateAddedStatus(addedFlag, goodsIdList);
        //获取商品所在的书单，如果存在，则重新加载书单
        try {
            List<BookListGoodsPublishDTO> allBookListGoodsPublishList = new ArrayList<>();
            for (String s : goodsIdList) {
                List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList = bookListGoodsPublishService.list(
                        null, null, CategoryEnum.BOOK_LIST_MODEL.getCode(), s, "xxoo");
                allBookListGoodsPublishList.addAll(bookListGoodsPublishDTOList);
            }

            if (CollectionUtils.isNotEmpty(allBookListGoodsPublishList)) {
                List<Integer> bookListModelIdList = allBookListGoodsPublishList.stream().map(BookListGoodsPublishDTO::getBookListId).collect(Collectors.toList());
                siteSearchService.siteSearchBookPkgNotify(bookListModelIdList);
            }
        } catch (Exception ex) {
            log.error("GoodsController modifyAddedStatus exception", ex);
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品上下架状态
     *
     * @param request {@link GoodsModifyAddedStatusRequest}
     */
    @Override

    public BaseResponse providerModifyAddedStatus(@RequestBody @Valid GoodsModifyAddedStatusRequest request) {
        Integer addedFlag = request.getAddedFlag();
        List<String> goodsIdList = request.getGoodsIds();
        goodsService.providerUpdateAddedStatus(addedFlag, goodsIdList);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品分类
     *
     * @param request {@link GoodsModifyCateRequest}
     */
    @Override

    public BaseResponse modifyCate(@RequestBody @Valid GoodsModifyCateRequest request) {
        List<String> goodsIds = request.getGoodsIds();
        List<Long> storeCateIds = request.getStoreCateIds();
        goodsService.updateCate(goodsIds, storeCateIds);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品商家名称
     *
     * @param request {@link GoodsModifySupplierNameRequest}
     */
    @Override

    public BaseResponse modifySupplierName(@RequestBody @Valid GoodsModifySupplierNameRequest request) {
        String supplierName = request.getSupplierName();
        Long companyInfoId = request.getCompanyInfoId();
        goodsService.updateSupplierName(supplierName, companyInfoId);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品运费模板
     *
     * @param request {@link GoodsModifyFreightTempRequest}
     */
    @Override

    public BaseResponse modifyFreightTemp(@RequestBody @Valid GoodsModifyFreightTempRequest request) {
        Long freightTempId = request.getFreightTempId();
        List<String> goodsIds = request.getGoodsIds();
        goodsService.updateFreight(freightTempId, goodsIds);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 商品审核
     *
     * @param request {@link GoodsCheckRequest}
     */
    @Override
    public BaseResponse<GoodsCheckResponse> checkGoods(@RequestBody @Valid GoodsCheckRequest request) {
        com.wanmi.sbc.goods.info.request.GoodsCheckRequest goodsCheckRequest =
                KsBeanUtil.convert(request, com.wanmi.sbc.goods.info.request.GoodsCheckRequest.class);
        return BaseResponse.success(s2bGoodsService.check(goodsCheckRequest));
    }

    @Override
    public BaseResponse updateGoodsCollectNum(@RequestBody @Valid GoodsModifyCollectNumRequest
                                                      goodsModifyCollectNumRequest) {
        goodsService.updateGoodsCollectNum(goodsModifyCollectNumRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateGoodsSalesNum(@RequestBody @Valid GoodsModifySalesNumRequest goodsModifySalesNumRequest) {
        goodsService.updateGoodsSalesNum(goodsModifySalesNumRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateGoodsFavorableCommentNum(@RequestBody @Valid GoodsModifyEvaluateNumRequest request) {
        goodsService.updateGoodsFavorableCommentNum(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyShamSalesNum(@RequestBody @Valid GoodsModifyShamSalesNumRequest request) {
        goodsService.updateShamSalesNum(request.getGoodsId(), request.getShamSalesNum());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifySortNo(@RequestBody @Valid GoodsModifySortNoRequest request){
        goodsService.updateSortNo(request.getGoodsId(), request.getSortNo());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<GoodsSynResponse> synGoods(@RequestBody @Valid GoodsSynRequest request) {
        List<String> goodsIds = new ArrayList<>();
        for (String standardgoodsId : request.getGoodsIds()) {
            String goodsId = standardImportService.syn(standardgoodsId, request.getStoreId());
            goodsIds.add(goodsId);
        }
        return BaseResponse.success(new GoodsSynResponse(goodsIds));
    }

    /**
     * 同步库存，将redis的库存进行同步扣除
     */
    @Override
    public BaseResponse syncStock(){
        goodsStockService.syncStock();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新代销商品供应商店铺状态
     */
    @Override
    public BaseResponse updateProviderStatus(@RequestBody @Valid GoodsProviderStatusRequest request) {
        goodsService.updateProviderStatus(request.getProviderStatus(), request.getStoreIds());
        return BaseResponse.SUCCESSFUL();
    }

//    @Override
//    public BaseResponse<Map<String, Map<String, Integer>>> syncERPStock(GoodsInfoListByIdRequest goodsInfoListByIdRequest) {
//        Map<String, Map<String, Integer>> resultMap = goodsStockService.syncERPGoodsStock(
//                goodsInfoListByIdRequest.getGoodsInfoNo(),
//                goodsInfoListByIdRequest.getPageNum(),
//                goodsInfoListByIdRequest.getPageSize());
//        return BaseResponse.success(resultMap);
//    }

//    @Override
//    public BaseResponse<Map<String, Map<String, Integer>>> partialUpdateStock(String erpGoodInfoNo, String lastSyncTime, String pageNum, String pageSize){
//        Map<String, Map<String, Integer>> resultMap = goodsStockService.partialUpdateStock(erpGoodInfoNo, lastSyncTime, pageNum, pageSize);
//        return BaseResponse.success(resultMap);
//    }

//    @Override
//    public BaseResponse<Map<String,String>> decryLastStock(Map<String, Long> datas){
//        goodsInfoStockService.decryLastStock(datas);
//        return BaseResponse.SUCCESSFUL();
//    }

//    @Override
//    public BaseResponse decryFreezeStock(List<GoodsInfoMinusStockDTO> releaseFrozenStockList){
//        goodsInfoStockService.decryFreezeStock(releaseFrozenStockList);
//        return BaseResponse.SUCCESSFUL();
//    }




    @Override
    public BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> guanYiSyncGoodsStock(GuanYiSyncGoodsStockRequest guanYiSyncGoodsStockRequest){
        return BaseResponse.success(goodsStockService.batchUpdateStock(guanYiSyncGoodsStockRequest.getGoodsIdList(), guanYiSyncGoodsStockRequest.isHasSaveRedis(), null));
    }



    @Override
    public BaseResponse<List<GoodsInfoStockSyncProviderResponse>> syncGoodsStock(GoodsInfoListByIdRequest goodsInfoListByIdRequest) {
        return BaseResponse.success(goodsStockService.syncGoodsStock(goodsInfoListByIdRequest.getPageNum(),
                goodsInfoListByIdRequest.getPageSize()));
    }

    @Override
    public BaseResponse<MicroServicePage<GoodsInfoPriceChangeDTO>> syncGoodsPrice(GoodsPriceSyncRequest goodsPriceSyncRequest) {
        MicroServicePage<GoodsInfoPriceChangeDTO> result = goodsInfoService.syncGoodsPrice(KsBeanUtil.convert(goodsPriceSyncRequest, GoodsPriceSyncQueryRequest.class));
        return BaseResponse.success(result);

    }

    /**
     * 同步库存和成本价
     * @param goodsIdList
     * @return
     */
    @Override
    @Deprecated
    public BaseResponse syncGoodsStockAndCostPrice(List<String> goodsIdList){
        //更新库存
        goodsStockService.bookuuSyncGoodsStock(goodsIdList);
        //同步成本价
        goodsInfoService.bookuuSyncGoodsPrice(goodsIdList);
        return BaseResponse.SUCCESSFUL();
    }



//    @Override
//    public BaseResponse<MicroServicePage<GoodsInfoPriceChangeDTO>> syncGoodsInfoCostPrice(GoodsPriceSyncRequest goodsPriceSyncRequest) {
//        return BaseResponse.success(goodsInfoService.syncGoodsCostPrice(KsBeanUtil.convert(goodsPriceSyncRequest, GoodsCostPriceChangeQueryRequest.class)));
//    }


    @Override
    public BaseResponse updateGoodsErpGoodsNo(Collection<GoodsUpdateProviderRequest> erpGoodsColl){
        List<String> goodsIdList = goodsService.updateGoodsErpGoodsNo(erpGoodsColl);
        return BaseResponse.success(goodsIdList);
    }


    @Override
    public BaseResponse updateGoodsByCondition(List<GoodsDataUpdateRequest> goodsDetas) {
        goodsService.updateGoodsByCondition(goodsDetas);
        return BaseResponse.SUCCESSFUL();
    }

    @Deprecated
    @Override
    public BaseResponse getGoodsDetialById(String spuId, String skuId) {


        Map map=new HashMap<>();
       // Map map = goodsService.getGoodsDetialById(spuId, skuId, RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID);
        //填充会员价
        List detailList=(List)map.get("bookDetail");
        Map medioMap =(Map) detailList.get(0);
        List<JSONObject> medioRecomd =(List<JSONObject>) medioMap.get("medioRecomd");
        if(null==medioRecomd ||medioRecomd.size()==0 ){
            //媒体推荐没有
            return BaseResponse.success(map);
        }
        List<String> skuIdList=new ArrayList<>();
        //返回值
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureBOS=new ArrayList<>();
        //循环每个推荐人取出skuId
        for(int i=0;i<medioRecomd.size();i++){
            MetaBookRcmmdFigureBO metaBookRcmmdFigureBO =JSONObject.toJavaObject(medioRecomd.get(i),MetaBookRcmmdFigureBO.class) ;
            List<MetaBookRcmmdFigureBO.RecomentBookVo> recomentBookBoList =metaBookRcmmdFigureBO.getRecomentBookBoList();
            if(null !=recomentBookBoList&& recomentBookBoList.size()!=0){
                //循环其推荐列表
                List<String> sku = recomentBookBoList.stream().map(r -> r.getGoodsInfoId()).collect(Collectors.toList());
                skuIdList.addAll(sku);
            }
        }
        if(null==skuIdList ||skuIdList.size()==0 ){
            //媒体推荐人没有商品信息
            return BaseResponse.success(map);
        }

        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIdList);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
        //用户信息
        String c = "{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
        CustomerGetByIdResponse customer = JSON.parseObject(c, CustomerGetByIdResponse.class);
        //价格信息
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
        Map<String, GoodsInfoVO> goodsPriceMap = goodsInfoVOList
                .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));


        //循环每个推荐人，回填会员价
        for(int j=0;j<medioRecomd.size();j++){
            MetaBookRcmmdFigureBO metaBookRcmmdFigureBO =JSONObject.toJavaObject(medioRecomd.get(j),MetaBookRcmmdFigureBO.class) ;
            List<MetaBookRcmmdFigureBO.RecomentBookVo> recomentBookBoList =metaBookRcmmdFigureBO.getRecomentBookBoList();
            if(null !=recomentBookBoList&& recomentBookBoList.size()!=0){
                //循环其推荐列表
               for(MetaBookRcmmdFigureBO.RecomentBookVo recomentBookVo :recomentBookBoList){
                    if(null != goodsPriceMap && null != goodsPriceMap.get(recomentBookVo.getGoodsInfoId())){
                       // recomentBookVo.setSalePrice(goodsPriceMap.get(recomentBookVo.getGoodsInfoId()).getSalePrice());
                        BeanUtils.copyProperties(goodsPriceMap.get(recomentBookVo.getGoodsInfoId()),recomentBookVo);
                    }
                }
            }
            metaBookRcmmdFigureBOS.add(metaBookRcmmdFigureBO);
        }
        medioMap.put("medioRecomd",metaBookRcmmdFigureBOS);
        detailList.set(0,medioMap);
        map.put("bookDetail",detailList);
        //榜单
        Map rankMap = goodsService.getGoodsDetailRankById(spuId, skuId, RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID);
        map.put("rank",rankMap);
        return BaseResponse.success(map);
    }

    //迁移到mobile里面了
    @Deprecated
    @Override
    public BaseResponse getGoodsDetialById1(String spuId, String skuId) {

        Map map=new HashMap<>();
        String old_json=new String();
        old_json = goodsService.getGoodsDetialById(spuId, skuId, RedisTagsConstant.ELASTIC_SAVE_BOOKS_DETAIL_SPU_ID);
        map=JSONObject.parseObject(old_json,Map.class);

        List<String> skuIdList=new ArrayList<>();
        List<Map> maplist=new ArrayList<>();
        List<MetaBookRcmmdFigureBO> metaBookRcmmdFigureBOS=new ArrayList<>();
        //循环每个推荐人取出skuId,并放入skuIdList
//        List<String> skuIdByGoodsDetailTableOne = goodsService.getSkuIdByGoodsDetailTableOne(map, skuIdList);
//        List<String> skuIdByGoodsDetailOtherBook = goodsService.getSkuIdByGoodsDetailOtherBook(map, skuIdList);
        skuIdList = goodsService.getSkuIdByGoodsDetail(old_json);
        List<String> collect = skuIdList.stream().distinct().collect(Collectors.toList());

        if(null==skuIdList ||skuIdList.size()==0 ){
            //没有商品信息需要回填
            return BaseResponse.success(map);
        }

        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(collect);
        List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext().getGoodsInfos();
        //用户信息
        String c = "{\"checkState\":\"CHECKED\",\"createTime\":\"2023-02-03T15:07:27\",\"customerAccount\":\"15618961858\",\"customerDetail\":{\"contactName\":\"书友_izw9\",\"contactPhone\":\"15618961858\",\"createTime\":\"2023-02-03T15:07:27\",\"customerDetailId\":\"2c9a00d184efa38001861619fbd60235\",\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerName\":\"书友_izw9\",\"customerStatus\":\"ENABLE\",\"delFlag\":\"NO\",\"employeeId\":\"2c9a00027f1f3e36017f202dfce40002\",\"isDistributor\":\"NO\",\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"},\"customerId\":\"2c9a00d184efa38001861619fbd60234\",\"customerLevelId\":3,\"customerPassword\":\"a8568f6a11ca32de1429db6450278bfd\",\"customerSaltVal\":\"64f88c8c7b53457f55671acc856bf60b7ffffe79ba037b8753c005d1265444ad\",\"customerType\":\"PLATFORM\",\"delFlag\":\"NO\",\"enterpriseCheckState\":\"INIT\",\"fanDengUserNo\":\"600395394\",\"growthValue\":0,\"loginErrorCount\":0,\"loginIp\":\"192.168.56.108\",\"loginTime\":\"2023-02-17T10:37:58\",\"payErrorTime\":0,\"pointsAvailable\":0,\"pointsUsed\":0,\"safeLevel\":20,\"storeCustomerRelaListByAll\":[],\"updatePerson\":\"2c90e863786d2a4c01786dd80bc0000a\",\"updateTime\":\"2023-02-11T11:18:23\"}\n";
        CustomerGetByIdResponse customer = JSON.parseObject(c, CustomerGetByIdResponse.class);
        //价格信息
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();
        Map<String, GoodsInfoVO> goodsPriceMap = goodsInfoVOList
                .stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));

        if(null==goodsPriceMap ||goodsPriceMap.size()==0 ){
            //没有商品信息
            return BaseResponse.success(map);
        }

//        if(null!=skuIdByGoodsDetailTableOne && skuIdByGoodsDetailTableOne.size()!=0){
//            //推荐人有商品需要回填信息
//            List detailList=goodsService.fillGoodsDetailTableOne(map,goodsPriceMap);
//            map.put("bookDetail",detailList);
//        }
//        if(null!=skuIdByGoodsDetailOtherBook && skuIdByGoodsDetailOtherBook.size()!=0){
//            //其他书籍有商品需要回填信息
//            List otherBookList = goodsService.fillGoodsDetailOtherBook(map, goodsPriceMap);
//            map.put("otherBook",otherBookList);
//
//        }

            //table2有商品需要回填信息
            map= goodsService.fillGoodsDetail(old_json,goodsPriceMap);

        //榜单
        Map rankMap = goodsService.getGoodsDetailRankById(spuId, skuId, RedisTagsConstant.ELASTIC_SAVE_GOODS_TAGS_SPU_ID);
        map.put("rank",rankMap);
        return BaseResponse.success(map);
    }
}
