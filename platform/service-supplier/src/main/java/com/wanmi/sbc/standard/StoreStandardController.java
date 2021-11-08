package com.wanmi.sbc.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoModifyAddedStatusRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardPageRequest;
import com.wanmi.sbc.elastic.api.response.standard.EsStandardPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsStandardGoodsPageVO;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardImportProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateChildCateIdsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsSynRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByGoodsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.standard.*;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListByGoodsResponse;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogSaveProvider;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogSynRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品库服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreStandardController", description = "商品库服务 API")
@RestController
@RequestMapping("/standard")
public class StoreStandardController {

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private StandardGoodsQueryProvider standardGoodsQueryProvider;

    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private StandardImportProvider standardImportProvider;

    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private OperateDataLogSaveProvider operateDataLogSaveProvider;

    @Autowired
    private StandardSkuQueryProvider standardSkuQueryProvider;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private EsStandardQueryProvider esStandardQueryProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private ClassifyProvider classifyProvider;

    /**
     * 获取商品库详情信息
     *
     * @param goodsId 商品库编号 {@link String}
     * @return 商品库详情
     */
//    @ApiOperation(value = "获取商品库详情信息")
//    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsId",
//            value = "商品库商品Id", required = true)
//    @RequestMapping(value = "/spu/{goodsId}", method = RequestMethod.GET)
//    public BaseResponse<StandardGoodsByIdResponse> info(@PathVariable String goodsId) {
//        StandardGoodsByIdRequest standardGoodsByIdRequest = new StandardGoodsByIdRequest();
//        standardGoodsByIdRequest.setGoodsId(goodsId);
//        return standardGoodsQueryProvider.getById(standardGoodsByIdRequest);
//    }

    /**
     * 查询商品
     *
     * @param pageRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "查询商品")
    @RequestMapping(value = "/spus", method = RequestMethod.POST)
    public BaseResponse<EsStandardPageResponse> list(@RequestBody EsStandardPageRequest pageRequest) {

        Long storeId = commonUtil.getStoreId();

        //自动填充当前商家签约的所有分类
        ContractCateListByConditionRequest cateQueryRequest = new ContractCateListByConditionRequest();
        cateQueryRequest.setStoreId(storeId);
        List<Long> cateIds = contractCateQueryProvider.listByCondition(cateQueryRequest).getContext()
                .getContractCateList().stream().map(ContractCateVO::getGoodsCate).map(GoodsCateVO::getCateId).collect
                        (Collectors.toList());
        if (Objects.nonNull(pageRequest.getCateId())) {
            //获取已签约的子分类
            GoodsCateChildCateIdsByIdRequest goodsCateChildCateIdsByIdRequest = new GoodsCateChildCateIdsByIdRequest();
            goodsCateChildCateIdsByIdRequest.setCateId(pageRequest.getCateId());
            List<Long> tempCateIds =
                    goodsCateQueryProvider.getChildCateIdById(goodsCateChildCateIdsByIdRequest).getContext().getChildCateIdList();
            tempCateIds.add(pageRequest.getCateId());
            cateIds = tempCateIds.stream().filter(cateIds::contains).collect(Collectors.toList());
            //不能列出所有子分类，所以置空
            pageRequest.setCateId(null);
        }
        if (CollectionUtils.isEmpty(cateIds)) {
            return BaseResponse.success(new EsStandardPageResponse());
        }
        pageRequest.setCateIds(cateIds);

        //如果为空，自动填充当前商家签约的所有分类品牌
        if (Objects.isNull(pageRequest.getBrandId())) {
            ContractBrandListRequest request = new ContractBrandListRequest();
            request.setStoreId(storeId);
            pageRequest.setOrNullBrandIds(contractBrandQueryProvider.list(request).getContext().getContractBrandVOList()
                    .stream().filter(contractBrand -> Objects.nonNull(contractBrand.getGoodsBrand()))
                    .map(ContractBrandVO::getGoodsBrand).map(GoodsBrandVO::getBrandId).collect(Collectors.toList()));
        }

        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        pageRequest.setStoreId(storeId);
        BaseResponse<EsStandardPageResponse> response = esStandardQueryProvider.page(pageRequest);
        //列出已被导入的商品库ID
        EsStandardPageResponse context = response.getContext();
        if (CollectionUtils.isNotEmpty(context.getStandardGoodsPage().getContent())) {
            StandardGoodsGetUsedStandardRequest standardRequest = new StandardGoodsGetUsedStandardRequest();
            standardRequest.setStandardIds(context.getStandardGoodsPage().getContent().stream().map(EsStandardGoodsPageVO::getGoodsId).collect(Collectors.toList()));
            standardRequest.setStoreIds(Collections.singletonList(storeId));
            context.setUsedStandard(standardGoodsQueryProvider.getUsedStandard(standardRequest).getContext().getStandardIds());
//            standardRequest.setNeedSynchronize(BoolFlag.YES);
//            //列出需要同步的商品库ID
//            context.setNeedSynStandard(standardGoodsQueryProvider.getNeedSynStandard(standardRequest).getContext()
//            .getStandardIds());
        }
        return response;
    }

    /**
     * 导入商品
     *
     * @param request 导入参数
     * @return 成功结果
     */
    @ApiOperation(value = "导入商品")
    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    public BaseResponse importGoods(@RequestBody StandardImportGoodsRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        if (companyInfo == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.setCompanyType(companyInfo.getCompanyType());
        request.setStoreId(commonUtil.getStoreId());
        request.setSupplierName(companyInfo.getSupplierName());

        BaseResponse<StandardImportGoodsResponse> baseResponse = standardImportProvider.importGoods(request);
        StandardImportGoodsResponse response = baseResponse.getContext();
        List<String> skuIds = Optional.ofNullable(response)
                .map(StandardImportGoodsResponse::getSkuIdList)
                .orElse(null);

        //刷新标品库ES
        esStandardProvider.init(EsStandardInitRequest.builder().goodsIds(request.getGoodsIds()).build());
        //加入ES
        if (CollectionUtils.isNotEmpty(skuIds)) {
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(skuIds).build());
        }

        if (1 == request.getGoodsIds().size()) {
            List<String> standardIds = new ArrayList<String>();
            standardIds.add(request.getGoodsIds().get(0));
            List<Long> stores = new ArrayList<Long>();

            stores.add(request.getStoreId());
            StandardGoodsListUsedGoodsIdRequest standardGoodsListUsedGoodsIdRequest =
                    new StandardGoodsListUsedGoodsIdRequest();
            standardGoodsListUsedGoodsIdRequest.setStandardIds(standardIds);
            standardGoodsListUsedGoodsIdRequest.setStoreIds(stores);
            BaseResponse<StandardGoodsListUsedGoodsIdResponse> baseResponse1 =
                    standardGoodsQueryProvider.listUsedGoodsId(standardGoodsListUsedGoodsIdRequest);
            StandardGoodsListUsedGoodsIdResponse standardGoodsListUsedGoodsIdResponse = baseResponse1.getContext();
            if (Objects.nonNull(standardGoodsListUsedGoodsIdResponse)) {
                standardGoodsListUsedGoodsIdResponse.getGoodsIds().forEach(goodsId -> {
                            GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
                            goodsByIdRequest.setGoodsId(goodsId);
                            BaseResponse<GoodsByIdResponse> baseResponse2 =
                                    goodsQueryProvider.getById(goodsByIdRequest);
                            GoodsByIdResponse goodsByIdResponse = baseResponse2.getContext();
                            if (Objects.nonNull(goodsByIdResponse)) {
                                operateLogMQUtil.convertAndSend("商品", "商品库导入",
                                        "商品库导入:" + goodsByIdResponse.getGoodsName());
                            }

                        }
                );
            }
        } else {
            operateLogMQUtil.convertAndSend("商品", "商品库批量导入", "商品库批量导入");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量同步商品
     */
    @ApiOperation(value = "批量同步商品")
    @RequestMapping(value = "/spu/syn", method = RequestMethod.POST)
    public BaseResponse onSale(@RequestBody GoodsSynRequest request) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(request.getGoodsIds())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        request.setAddedFlag(AddedFlag.NO.toValue());

        //查询上架商品中是否包含供应商商品(下架状态的)，包含则返回
        GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
        goodsListByIdsRequest.setGoodsIds(request.getGoodsIds());
        request.setStoreId(commonUtil.getStoreId());
        List<String> goodsIds = goodsProvider.synGoods(request).getContext().getGoodsIds();
        //更新ES
        esGoodsInfoElasticProvider.updateAddedStatus(EsGoodsInfoModifyAddedStatusRequest.builder().
                addedFlag(AddedFlag.NO.toValue()).goodsIds(goodsIds).goodsInfoIds(null).build());

        //ares埋点-商品-后台批量修改商品spu的所有sku上下架状态
        goodsAresProvider.dispatchFunction(new DispatcherFunctionRequest("editGoodsSpuUp",
                new Object[]{AddedFlag.NO.toValue(), request.getGoodsIds()}));

        if (CollectionUtils.isNotEmpty(goodsIds)) {
            if (1 == goodsIds.size()) {
                GoodsByIdRequest goodsByIdRequest = new GoodsByIdRequest();
                goodsByIdRequest.setGoodsId(goodsIds.get(0));
                GoodsByIdResponse response = goodsQueryProvider.getById(goodsByIdRequest).getContext();
                operateLogMQUtil.convertAndSend("商品", "同步",
                        "同步：SPU编码" + response.getGoodsNo());
            } else {
                operateLogMQUtil.convertAndSend("商品", "批量同步", "批量同步");
            }
            for (String goodsId : goodsIds) {
                GoodsViewByIdRequest goodsViewByIdRequest = new GoodsViewByIdRequest();
                goodsViewByIdRequest.setGoodsId(goodsId);
                GoodsViewByIdResponse newData = goodsQueryProvider.getViewById(goodsViewByIdRequest).getContext();
                //同步日志
                operateDataLogSaveProvider.synDataLog(OperateDataLogSynRequest.builder().supplierGoodsId(goodsId).providerGoodsId(newData.getGoods().getProviderGoodsId()).build());
            }
        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取商品库SKU详情信息
     *
     * @param standardInfoId 商品库SKU编号
     * @return 商品详情
     */
    @RequestMapping(value = "/sku/{standardInfoId}", method = RequestMethod.GET)
    public BaseResponse<StandardSkuByIdResponse> skuInfo(@PathVariable String standardInfoId) {
        return standardSkuQueryProvider.getById(
                StandardSkuByIdRequest.builder().standardInfoId(standardInfoId).build()
        );
    }


    /**
     * 获取商品库详情信息
     *
     * @param goodsId 商品库编号 {@link String}
     * @return 商品库详情
     */
    @ApiOperation(value = "获取商品库详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsId",
            value = "商品库商品Id", required = true)
    @RequestMapping(value = "/spu/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<StandardGoodsByIdResponse> spuInfo(@PathVariable String goodsId) {
        StandardGoodsByIdRequest standardGoodsByIdRequest = new StandardGoodsByIdRequest();
        standardGoodsByIdRequest.setGoodsId(goodsId);
        BaseResponse<StandardGoodsByIdResponse> response = standardGoodsQueryProvider.getById(standardGoodsByIdRequest);
        //获取商品店铺分类
        if (osUtil.isS2b()) {
            goodsId = response.getContext().getGoods().getProviderGoodsId();
            if (goodsId != null) {
                Map<String, List<Integer>> storeCateIdMap = classifyProvider.searchGroupedClassifyIdByGoodsId(Collections.singletonList(goodsId)).getContext();
                if(storeCateIdMap != null){
                    response.getContext().getGoods().setStoreCateIds(storeCateIdMap.get(goodsId).stream().map(Integer::longValue).collect(Collectors.toList()));
                }
            }
        }
        return response;
    }

}
