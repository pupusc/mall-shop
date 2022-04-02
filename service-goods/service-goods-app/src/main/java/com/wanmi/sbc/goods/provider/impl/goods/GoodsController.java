package com.wanmi.sbc.goods.provider.impl.goods;

import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.CompanySourceType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreBycompanySourceType;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.goodsstock.GuanYiSyncGoodsStockRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.api.request.linkedmall.SyncItemRequest;
import com.wanmi.sbc.goods.api.response.goods.*;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsDelResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallInitResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.SyncItemResponse;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPriceChangeDTO;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemDelDTO;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemModificationDTO;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsTagVo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.request.GoodsCostPriceChangeQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsPriceSyncQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.info.request.GoodsSaveRequest;
import com.wanmi.sbc.goods.info.service.*;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
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

    @Override
    public BaseResponse<Map<String,String>> decryLastStock(Map<String, Long> datas){
        goodsInfoStockService.decryLastStock(datas);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> guanYiSyncGoodsStock(GuanYiSyncGoodsStockRequest guanYiSyncGoodsStockRequest){
        return BaseResponse.success(goodsStockService.batchUpdateStock(guanYiSyncGoodsStockRequest.getGoodsIdList(), guanYiSyncGoodsStockRequest.getStartTime(), guanYiSyncGoodsStockRequest.getMaxTmpId(), guanYiSyncGoodsStockRequest.getPageSize()));
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
}
