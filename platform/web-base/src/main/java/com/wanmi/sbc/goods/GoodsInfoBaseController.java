package com.wanmi.sbc.goods;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.*;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDelFlagGetRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailWithNotDeleteByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetWithNotDeleteByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.quicklogin.ThirdLoginRelationResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.distribute.DistributionService;
import com.wanmi.sbc.distribute.response.ShopInfoResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsDistributorGoodsListQueryRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.bean.dto.goods.EsGoodsInfoDTO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoSiteQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.prop.GoodsPropQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleInProgressAllGoodsInfoIdsRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoPageByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.ProviderGoodsStockSyncRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListAllByCateIdRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListIndexByCateIdRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleInProgressAllGoodsInfoIdsResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsIdsResp;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoListByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoPageByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListAllByCateIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListIndexByCateIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsDetailFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.bean.dto.GoodsInfoDetailByGoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseProvider;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFillBuyCountRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseQueryRequest;
import com.wanmi.sbc.order.api.response.purchase.PurchaseFillBuyCountResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseQueryResponse;
import com.wanmi.sbc.order.bean.vo.PurchaseVO;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品Controller
 * Created by daiyitian on 17/4/12.
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "GoodsInfoBaseController", description = "S2B web公用-商品信息API")
public class GoodsInfoBaseController {

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private GoodsInfoSiteQueryProvider goodsInfoSiteQueryProvider;

    @Autowired
    private PurchaseProvider purchaseProvider;

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsPropQueryProvider goodsPropQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private DistributorGoodsInfoQueryProvider distributorGoodsInfoQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    public static final String CYCLE_BUY = "周期购";

    /**
     * 未登录时,查询商品分页(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "未登录时,查询商品分页")
    @RequestMapping(value = "/skuListFront", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> skuListFront(@RequestBody EsGoodsInfoQueryRequest queryRequest) {

        EsGoodsInfoResponse response = list(queryRequest, null);
        if (CollectionUtils.isNotEmpty(queryRequest.getEsGoodsInfoDTOList()) &&
                CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            Map<String, List<EsGoodsInfoDTO>> buyCountMap =
                    queryRequest.getEsGoodsInfoDTOList().stream()
                            .collect(Collectors.groupingBy(EsGoodsInfoDTO::getGoodsInfoId));

            response.getEsGoodsInfoPage().getContent().stream()
                    .filter(esGoodsInfo -> Objects.nonNull(esGoodsInfo.getGoodsInfo())
                            && buyCountMap.containsKey(esGoodsInfo.getGoodsInfo().getGoodsInfoId()))
                    .forEach(esGoodsInfo -> {
                        GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
                        goodsInfo.setBuyCount(
                                buyCountMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId()).get(0).getGoodsNum());
                    });
        }
        return BaseResponse.success(response);
    }

    /**
     * 商品分页(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "查询商品分页")
    @RequestMapping(value = "/skus", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> list(@RequestBody EsGoodsInfoQueryRequest queryRequest) {
        return BaseResponse.success(list(queryRequest, commonUtil.getCustomer()));
    }


    @ApiOperation(value = "根据分销员-会员ID查询店铺精选小店名称")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "distributorId", value = "分销员-会员id", required =
            true)
    @RequestMapping(value = "/shop-info/{distributorId}", method = RequestMethod.GET)
    public BaseResponse<ShopInfoResponse> getShopInfo(@PathVariable String distributorId) {
        ShopInfoResponse response = new ShopInfoResponse();
        //验证会员是否存在
        Boolean delFlag =
                customerQueryProvider.getCustomerDelFlag(new CustomerDelFlagGetRequest(distributorId)).getContext().getDelFlag();
        if (Boolean.TRUE.equals(delFlag)) {
            throw new SbcRuntimeException("K-080301");
        }
        BaseResponse<CustomerDetailGetWithNotDeleteByCustomerIdResponse> baseResponse = customerDetailQueryProvider
                .getCustomerDetailWithNotDeleteByCustomerId(new CustomerDetailWithNotDeleteByCustomerIdRequest
                        (distributorId));
        CustomerDetailGetWithNotDeleteByCustomerIdResponse customerDetailGetWithNotDeleteByCustomerIdResponse =
                baseResponse.getContext();
        if (Objects.nonNull(customerDetailGetWithNotDeleteByCustomerIdResponse)) {
            String customerName = customerDetailGetWithNotDeleteByCustomerIdResponse.getCustomerName();
            String shopName = distributionCacheService.getShopName();
            response.setShopName(customerName + "的" + shopName);

        }
        ThirdLoginRelationResponse thirdLoginRelationResponse = thirdLoginRelationQueryProvider
                .listThirdLoginRelationByCustomer
                        (ThirdLoginRelationByCustomerRequest.builder()
                                .customerId(distributorId)
                                .thirdLoginType(ThirdLoginType.WECHAT)
                                .build()).getContext();
        if (Objects.nonNull(thirdLoginRelationResponse) && Objects.nonNull(thirdLoginRelationResponse
                .getThirdLoginRelation())) {
            response.setHeadImg(thirdLoginRelationResponse.getThirdLoginRelation().getHeadimgurl());
        }
        return BaseResponse.success(response);
    }

    /**
     * 小C-店铺精选页(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "小C-店铺精选页")
    @RequestMapping(value = "/shop/sku-list-to-c", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> shopSkuListToC(@RequestBody EsGoodsInfoQueryRequest queryRequest) {

        MicroServicePage<DistributorGoodsInfoVO> microServicePage = pageDistributorGoodsInfoPageByCustomerId
                (queryRequest, queryRequest.getCustomerId());
        List<DistributorGoodsInfoVO> distributorGoodsInfoVOList = microServicePage.getContent();
        if (CollectionUtils.isEmpty(distributorGoodsInfoVOList)) {
            return BaseResponse.success(new EsGoodsInfoResponse());
        }
        List<String> goodsIdList = distributorGoodsInfoVOList.stream().map(DistributorGoodsInfoVO::getGoodsInfoId)
                .collect(Collectors.toList());
        Map<String, String> map = distributorGoodsInfoVOList.stream().collect(Collectors.toMap
                (DistributorGoodsInfoVO::getGoodsInfoId, DistributorGoodsInfoVO::getId));
        queryRequest.setGoodsInfoIds(goodsIdList);
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodsIdList.size());
        queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        queryRequest = wrapEsGoodsInfoQueryRequest(queryRequest);
        queryRequest.setCustomerLevelId(null);
        queryRequest.setCustomerLevelDiscount(null);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        EsGoodsInfoResponse response =
                esGoodsInfoElasticQueryProvider.distributorGoodsListByCustomerId(queryRequest).getContext();
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = response.getEsGoodsInfoPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getGoodsInfo().getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getGoodsInfo().getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0"
                    , null)).getContext();
        }
        if (stocks != null) {
            for (EsGoodsInfoVO esGoodsInfo : response.getEsGoodsInfoPage().getContent()) {
                if (ThirdPlatformType.LINKED_MALL.equals(esGoodsInfo.getGoodsInfo().getThirdPlatformType())) {
                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                                .filter(v -> String.valueOf(spuStock.getItemId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSkuId()))
                                .findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            esGoodsInfo.getGoodsInfo().setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(esGoodsInfo.getGoodsInfo().getGoodsStatus())) {
                                esGoodsInfo.getGoodsInfo().setGoodsStatus(skuStock > 0 ? GoodsStatus.OK :
                                        GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
            for (GoodsVO goodsVO : response.getGoodsList()) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsVO.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional =
                            stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goodsVO.getThirdPlatformSpuId())).findFirst();
                    if (optional.isPresent()) {
                        Long spuStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        goodsVO.setStock(spuStock);
                    }
                }
            }
        }
        //供应商库存同步
        if (CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            providerSkuStockSync(response);
        }
        response = filterDistributionGoods(queryRequest, response, map);
        if (Objects.nonNull(response.getEsGoodsInfoPage()) && CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage
                ().getContent())) {
            response.setEsGoodsInfoPage(new MicroServicePage<>(response.getEsGoodsInfoPage().getContent(), PageRequest.of
                    (microServicePage.getNumber(), microServicePage.getSize()), microServicePage
                    .getTotalElements()));
        }
        return BaseResponse.success(response);
    }

    /**
     * 分销员-我的店铺-店铺精选页(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "分销员-我的店铺-店铺精选页")
    @RequestMapping(value = "/shop/sku-list", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> shopSkuList(@RequestBody EsGoodsInfoQueryRequest queryRequest) {


        MicroServicePage<DistributorGoodsInfoVO> microServicePage = pageDistributorGoodsInfoPageByCustomerId
                (queryRequest, commonUtil.getOperator().getUserId());
        List<DistributorGoodsInfoVO> distributorGoodsInfoVOList = microServicePage.getContent();
        if (CollectionUtils.isEmpty(distributorGoodsInfoVOList)) {
            return BaseResponse.success(new EsGoodsInfoResponse());
        }
        List<String> goodsIdList = distributorGoodsInfoVOList.stream().map(DistributorGoodsInfoVO::getGoodsInfoId)
                .collect(Collectors.toList());
        Map<String, String> map = distributorGoodsInfoVOList.stream().collect(Collectors.toMap
                (DistributorGoodsInfoVO::getGoodsInfoId, DistributorGoodsInfoVO::getId));
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodsIdList.size());
        queryRequest.setGoodsInfoIds(goodsIdList);
        queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        queryRequest = wrapEsGoodsInfoQueryRequest(queryRequest);
        queryRequest.setCustomerLevelId(null);
        queryRequest.setCustomerLevelDiscount(null);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        EsGoodsInfoResponse response =
                esGoodsInfoElasticQueryProvider.distributorGoodsListByCustomerId(queryRequest).getContext();
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = response.getEsGoodsInfoPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getGoodsInfo().getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getGoodsInfo().getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0"
                    , null)).getContext();
        }
        if (stocks != null) {
            for (EsGoodsInfoVO esGoodsInfo : response.getEsGoodsInfoPage().getContent()) {
                if (ThirdPlatformType.LINKED_MALL.equals(esGoodsInfo.getGoodsInfo().getThirdPlatformType())) {

                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                                .filter(v -> String.valueOf(spuStock.getItemId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSkuId()))
                                .findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            esGoodsInfo.getGoodsInfo().setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(esGoodsInfo.getGoodsInfo().getGoodsStatus())) {
                                esGoodsInfo.getGoodsInfo().setGoodsStatus(skuStock > 0 ? GoodsStatus.OK :
                                        GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
            for (GoodsVO goodsVO : response.getGoodsList()) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsVO.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional =
                            stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goodsVO.getThirdPlatformSpuId())).findFirst();
                    if (optional.isPresent()) {
                        Long spuStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        goodsVO.setStock(spuStock);
                    }
                }
            }
        }
        //供应商库存同步
        if (CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            providerSkuStockSync(response);
        }
        response = filterDistributionGoods(queryRequest, response, map);

        if (Objects.nonNull(response.getEsGoodsInfoPage()) && CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage
                ().getContent())) {
            response.setEsGoodsInfoPage(new MicroServicePage<>(response.getEsGoodsInfoPage().getContent(), PageRequest.of
                    (microServicePage.getNumber(), microServicePage.getSize()), microServicePage
                    .getTotalElements()));
        }
        return BaseResponse.success(response);
    }


    /**
     * 分销员-我的店铺-选品功能(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "分销员-我的店铺-选品功能")
    @RequestMapping(value = "/shop/add-distributor-goods", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> addDistributorGoods(@RequestBody EsGoodsInfoQueryRequest queryRequest) {

        DistributorGoodsInfoListByCustomerIdRequest request = new DistributorGoodsInfoListByCustomerIdRequest();
        request.setCustomerId(commonUtil.getUserInfo().getUserId());

        BaseResponse<DistributorGoodsInfoListByCustomerIdResponse> baseResponse =
                distributorGoodsInfoQueryProvider.listByCustomerId(request);
        List<DistributorGoodsInfoVO> distributorGoodsInfoVOList = baseResponse.getContext()
                .getDistributorGoodsInfoVOList();
        List<String> goodsIdList = distributorGoodsInfoVOList.stream().map(DistributorGoodsInfoVO::getGoodsInfoId)
                .collect(Collectors.toList());
        Map<String, String> map = distributorGoodsInfoVOList.stream().collect(Collectors.toMap
                (DistributorGoodsInfoVO::getGoodsInfoId, DistributorGoodsInfoVO::getId));
        queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        queryRequest.setDistributionGoodsStatus(NumberUtils.INTEGER_ZERO);
        queryRequest = wrapEsGoodsInfoQueryRequest(queryRequest);
        queryRequest.setCustomerLevelId(null);
        queryRequest.setCustomerLevelDiscount(null);
        queryRequest.setDistributionGoodsInfoIds(goodsIdList);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        EsDistributorGoodsListQueryRequest esDistributorGoodsListQueryRequest =
                new EsDistributorGoodsListQueryRequest();
        esDistributorGoodsListQueryRequest.setRequest(queryRequest);
        esDistributorGoodsListQueryRequest.setGoodsIdList(goodsIdList);
        EsGoodsInfoResponse response =
                esGoodsInfoElasticQueryProvider.distributorGoodsList(esDistributorGoodsListQueryRequest).getContext();
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = response.getEsGoodsInfoPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getGoodsInfo().getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getGoodsInfo().getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0"
                    , null)).getContext();
        }
        if (stocks != null) {
            for (EsGoodsInfoVO esGoodsInfo : response.getEsGoodsInfoPage().getContent()) {
                if (ThirdPlatformType.LINKED_MALL.equals(esGoodsInfo.getGoodsInfo().getThirdPlatformType())) {

                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock =
                                spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSkuId())).findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            esGoodsInfo.getGoodsInfo().setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(esGoodsInfo.getGoodsInfo().getGoodsStatus())) {
                                esGoodsInfo.getGoodsInfo().setGoodsStatus(skuStock > 0 ? GoodsStatus.OK :
                                        GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        //供应商库存同步
        if (CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            providerSkuStockSync(response);
        }
        response = filterDistributionGoods(queryRequest, response, map);
        //查询优惠券
        if(CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())){
            response = this.distributionGetCoupon(response, commonUtil.getCustomer());
        }
        return BaseResponse.success(response);
    }


    /**
     * 未登录时，分页
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "未登录时，查询商品分页")
    @RequestMapping(value = "/unLogin/skus", method = RequestMethod.POST)
    public BaseResponse<EsGoodsInfoResponse> unLoginList(@RequestBody EsGoodsInfoQueryRequest queryRequest) {
        return BaseResponse.success(list(queryRequest, null));
    }

    /**
     * 根据商品分类id查询索引的商品属性列表
     *
     * @param cateId 商品分类id
     * @return 索引的属性列表
     */
    @ApiOperation(value = "根据商品分类id查询索引的商品属性列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId", value = "商品分类id", required = true)
    @RequestMapping(value = "/props/{cateId}", method = RequestMethod.GET)
    public BaseResponse<List<GoodsPropVO>> listByCateId(@PathVariable Long cateId) {
        BaseResponse<GoodsPropListIndexByCateIdResponse> baseResponse = goodsPropQueryProvider.listIndexByCateId(new
                GoodsPropListIndexByCateIdRequest(cateId));
        GoodsPropListIndexByCateIdResponse goodsPropListIndexByCateIdResponse = baseResponse.getContext();
        if (Objects.isNull(goodsPropListIndexByCateIdResponse)) {
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(goodsPropListIndexByCateIdResponse.getGoodsPropVOList());
    }

    /**
     * 根据商品分类id查询商品属性列表
     *
     * @param cateId 商品分类id
     * @return 所有属性列表
     */
    @ApiOperation(value = "根据商品分类id查询商品属性列表")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "cateId", value = "商品分类id", required = true)
    @RequestMapping(value = "/props/all/{cateId}", method = RequestMethod.GET)
    public BaseResponse<List<GoodsPropVO>> propsList(@PathVariable Long cateId) {
        BaseResponse<GoodsPropListAllByCateIdResponse> baseResponse = goodsPropQueryProvider.listAllByCateId(new
                GoodsPropListAllByCateIdRequest(cateId));
        GoodsPropListAllByCateIdResponse goodsPropListAllByCateIdResponse = baseResponse.getContext();
        if (Objects.isNull(goodsPropListAllByCateIdResponse)) {
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(goodsPropListAllByCateIdResponse.getGoodsPropVOList());
    }

    /**
     * 商品详情
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "查询商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/sku/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsInfoDetailByGoodsInfoResponse> detail(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, commonUtil.getCustomer()));
    }


    @ApiOperation(value = "未登录时查询商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/unLogin/sku/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsInfoDetailByGoodsInfoResponse> unLoginDetail(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, null));
    }

    /**
     * 根据skuIds获取商品信息
     *
     * @param skuIds
     * @return
     */
    @ApiOperation(value = "根据skuIds获取商品信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<GoodsInfoViewByIdsResponse> queryGoodsListBySkuIds(@RequestBody List<String> skuIds) {
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        return goodsInfoQueryProvider.listViewByIds(goodsInfoRequest);
    }

    @ApiOperation(value = "奖励中心热销商品(销量前10的分销商品)")
    @PostMapping(value = "/hot/goodsInfo")
    public BaseResponse<EsGoodsInfoResponse> hotGoods(@RequestBody EsGoodsInfoQueryRequest queryRequest) {

        EsGoodsInfoResponse response = this.esGoodsInfoResult(null);

        if (CollectionUtils.isNotEmpty(queryRequest.getEsGoodsInfoDTOList()) &&
                CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            Map<String, List<EsGoodsInfoDTO>> buyCountMap =
                    queryRequest.getEsGoodsInfoDTOList().stream()
                            .collect(Collectors.groupingBy(EsGoodsInfoDTO::getGoodsInfoId));

            response.getEsGoodsInfoPage().getContent().stream()
                    .filter(esGoodsInfo -> Objects.nonNull(esGoodsInfo.getGoodsInfo())
                            && buyCountMap.containsKey(esGoodsInfo.getGoodsInfo().getGoodsInfoId()))
                    .forEach(esGoodsInfo -> {
                        GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
                        goodsInfo.setBuyCount(
                                buyCountMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId()).get(0).getGoodsNum());
                    });
        }

        return BaseResponse.success(response);
    }


    @ApiOperation(value = "奖励中心热销商品(销量前10的分销商品)")
    @GetMapping(value = "/login/hot/goodsInfo")
    public BaseResponse<EsGoodsInfoResponse> loginHotGoods() {

        EsGoodsInfoResponse response = this.esGoodsInfoResult(commonUtil.getCustomer());

        Page<EsGoodsInfoVO> esGoodsInfoPage = response.getEsGoodsInfoPage();
        List<EsGoodsInfoVO> esGoodsInfoList = esGoodsInfoPage.getContent();
        List<String> goodsInfoIdList = Optional.of(esGoodsInfoList)
                .orElse(Collections.emptyList()).stream()
                .map(esGoodsInfo ->
                        esGoodsInfo.getGoodsInfo().getGoodsInfoId()
                ).collect(Collectors.toList());
        String customerId = commonUtil.getOperatorId();

        if (StringUtils.isBlank(customerId)) {
            return BaseResponse.success(response);
        }

        //查询热销商品采购单
        PurchaseQueryRequest purchaseQueryRequest = PurchaseQueryRequest.builder()
                .customerId(customerId)
                .goodsInfoIds(goodsInfoIdList)
                .inviteeId(commonUtil.getPurchaseInviteeId())
                .build();
        BaseResponse<PurchaseQueryResponse> baseResponse = purchaseQueryProvider.query(purchaseQueryRequest);
        PurchaseQueryResponse purchaseQueryResponse = baseResponse.getContext();
        List<PurchaseVO> purchaseList = purchaseQueryResponse.getPurchaseList();

        if (CollectionUtils.isNotEmpty(purchaseList)) {
            Map<String, Long> goodsNumMap = this.goodsNumMap(purchaseList);
            List<EsGoodsInfoVO> goodsInfoList = this.getGoodsInfoList(esGoodsInfoList, goodsNumMap);
            MicroServicePage<EsGoodsInfoVO> goodsInfoPage = new MicroServicePage<>(goodsInfoList, esGoodsInfoPage.getPageable(), esGoodsInfoPage.getTotalPages());
            response.setEsGoodsInfoPage(goodsInfoPage);
            return BaseResponse.success(response);
        }

        return BaseResponse.success(response);
    }


    /**
     * 初始化EsGoodsInfo
     *
     * @param esGoodsInfoList
     * @param goodsNumMap
     * @return
     */
    private List<EsGoodsInfoVO> getGoodsInfoList(List<EsGoodsInfoVO> esGoodsInfoList, Map<String, Long> goodsNumMap) {

        return esGoodsInfoList.stream().peek(esGoodsInfo -> {
            GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
            String goodsInfoId = goodsInfo.getGoodsInfoId();
            Long goodsNum = goodsNumMap.get(goodsInfoId);
            goodsNum = Objects.nonNull(goodsNum) ? goodsNum : 0L;
            goodsInfo.setBuyCount(goodsNum);
        }).collect(Collectors.toList());
    }

    /**
     * key:goodInfoId,value:goodsNum
     *
     * @param purchaseList
     * @return
     */
    private Map<String, Long> goodsNumMap(List<PurchaseVO> purchaseList) {
        return purchaseList.stream()
                .collect(Collectors.toMap(PurchaseVO::getGoodsInfoId, PurchaseVO::getGoodsNum));
    }


    private EsGoodsInfoResponse esGoodsInfoResult(CustomerVO customer) {
        DistributorGoodsIdsResp resp = distributorGoodsInfoQueryProvider.salesNumDescLimit10().getContext();
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
        queryRequest.setDistributionGoodsStatus(NumberUtils.INTEGER_ZERO);
        wrapEsGoodsInfoQueryRequest(queryRequest);
        queryRequest.setCustomerLevelId(null);
        queryRequest.setCustomerLevelDiscount(null);
        queryRequest.setPageSize(10);
        queryRequest.setSortFlag(4);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));

        EsDistributorGoodsListQueryRequest request = new EsDistributorGoodsListQueryRequest();
        request.setRequest(queryRequest);
        request.setGoodsIdList(Collections.emptyList());

        EsGoodsInfoResponse esGoodsInfoResponse = esGoodsInfoElasticQueryProvider.distributorGoodsList(request).getContext();
        //查询优惠券
        esGoodsInfoResponse = this.distributionGetCoupon(esGoodsInfoResponse, customer);
        return esGoodsInfoResponse;
    }


    /**
     * 查询优惠券
     * @param esGoodsInfoResponse
     * @param customer
     * @return
     */
    public EsGoodsInfoResponse distributionGetCoupon(EsGoodsInfoResponse esGoodsInfoResponse, CustomerVO customer){
        //查询优惠券
        List<EsGoodsInfoVO> esGoodsInfoList = esGoodsInfoResponse.getEsGoodsInfoPage().getContent();
        List<GoodsInfoDTO> goodsInfoVOS = esGoodsInfoList.stream().map(e -> KsBeanUtil.convert(e.getGoodsInfo(), GoodsInfoDTO.class)).collect(Collectors.toList());
        MarketingPluginGoodsListFilterRequest request = new MarketingPluginGoodsListFilterRequest();
        request.setGoodsInfos(goodsInfoVOS);
        request.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> goodsInfoVOList = marketingPluginProvider.distributionGoodsListFilter(request).getContext().getGoodsInfoVOList();
        //重新赋值于Page内部对象
        Map<String, GoodsInfoNestVO> voMap = goodsInfoVOList.stream()
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> KsBeanUtil.convert(g,
                        GoodsInfoNestVO.class), (s, a) -> s));
        esGoodsInfoResponse.getEsGoodsInfoPage().getContent().stream().forEach(e -> {
            GoodsInfoNestVO goodsInfo = e.getGoodsInfo();
            GoodsInfoNestVO goodsInfoNest = voMap.get(goodsInfo.getGoodsInfoId());
            goodsInfo.setCouponLabels(goodsInfoNest.getCouponLabels());
            e.setGoodsInfo(goodsInfo);
        });
        return esGoodsInfoResponse;
    }

    /**
     * es商品列表
     *
     * @param queryRequest 条件
     * @param customer     会员
     * @return es商品封装数据
     */
    private EsGoodsInfoResponse list(EsGoodsInfoQueryRequest queryRequest, CustomerVO customer) {
        if (Objects.nonNull(queryRequest.getMarketingId())) {
            MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
            marketingGetByIdRequest.setMarketingId(queryRequest.getMarketingId());
            queryRequest.setGoodsInfoIds(
                    marketingQueryProvider.getByIdForCustomer(marketingGetByIdRequest).getContext()
                            .getMarketingForEndVO().getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId)
                            .collect(Collectors.toList()));
            //非PC端分销商品状态
            if (!Objects.equals(ChannelType.PC_MALL, commonUtil.getDistributeChannel().getChannelType()) && DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())) {
                queryRequest.setExcludeDistributionGoods(Boolean.TRUE);
            }
        }
        //只看分享赚商品信息
        if (Objects.nonNull(queryRequest.getDistributionGoodsAudit()) && DistributionGoodsAudit.CHECKED.toValue() == queryRequest.getDistributionGoodsAudit()) {
            queryRequest.setDistributionGoodsStatus(NumberUtils.INTEGER_ZERO);
        }

        queryRequest = wrapEsGoodsInfoQueryRequest(queryRequest);
        EsGoodsInfoResponse response = esGoodsInfoElasticQueryProvider.page(queryRequest).getContext();
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = response.getEsGoodsInfoPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getGoodsInfo().getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getGoodsInfo().getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0"
                    , null)).getContext();
        }
        if (stocks != null) {
            for (EsGoodsInfoVO esGoodsInfo : response.getEsGoodsInfoPage().getContent()) {
                if (ThirdPlatformType.LINKED_MALL.equals(esGoodsInfo.getGoodsInfo().getThirdPlatformType())) {

                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock =
                                spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(esGoodsInfo.getGoodsInfo().getThirdPlatformSkuId())).findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            esGoodsInfo.getGoodsInfo().setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(esGoodsInfo.getGoodsInfo().getGoodsStatus())) {
                                esGoodsInfo.getGoodsInfo().setGoodsStatus(skuStock > 0 ? GoodsStatus.OK :
                                        GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        //供应商库存同步
        if (CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            providerSkuStockSync(response);
        }
        //非商品积分模式下清零
        if (CollectionUtils.isNotEmpty(response.getEsGoodsInfoPage().getContent())) {
            systemPointsConfigService.clearBuyPoinsForEsSku(response.getEsGoodsInfoPage().getContent());
        }
        response = calIntervalPriceAndMarketingPrice(response, customer, queryRequest);

        if (!Objects.isNull(customer)) {
            //填充购买数量
            List<String> goodsInfoIds = response.getEsGoodsInfoPage().getContent().stream().map(i -> i.getGoodsInfo()
                    .getGoodsInfoId()).collect(Collectors.toList());
            List<GoodsLevelPriceVO> goodsLevelPrices = this.getGoodsLevelPrices(goodsInfoIds, customer);
            Map<String, Long> skuNumMap = purchaseQueryProvider.query(PurchaseQueryRequest.builder().goodsInfoIds(goodsInfoIds)
                    .customerId(customer.getCustomerId())
                    .inviteeId(commonUtil.getPurchaseInviteeId())
                    .marketingId(queryRequest.getMarketingId())
                    .build()).getContext().getPurchaseList().stream().collect(Collectors
                    .toMap(PurchaseVO::getGoodsInfoId, PurchaseVO::getGoodsNum));
            response.getEsGoodsInfoPage().getContent().forEach(i -> {
                Long num = skuNumMap.get(i.getGoodsInfo().getGoodsInfoId());
                if (!Objects.isNull(num)) {
                    i.getGoodsInfo().setBuyCount(num);
                } else {
                    i.getGoodsInfo().setBuyCount(NumberUtils.LONG_ZERO);
                }
                //判断当前用户对应企业购商品等级企业价
                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(i.getGoodsInfo().getGoodsInfoId()))
                            .findFirst();
                    i.getGoodsInfo().setEnterPrisePrice(first.isPresent() ? first.get().getPrice() : i.getGoodsInfo().getEnterPrisePrice());
                }
            });
        }
        // 预约抢购中商品/预售中的商品

        List<String> goodInfoIdList =
                response.getEsGoodsInfoPage().getContent().stream().map(EsGoodsInfoVO::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
            BookingSaleInProgressAllGoodsInfoIdsResponse allGoodsInfoIdsResponse =
                    bookingSaleQueryProvider.inProgressAllByGoodsInfoIds(BookingSaleInProgressAllGoodsInfoIdsRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext();
            response.setAppointmentSaleVOList(allGoodsInfoIdsResponse.getAppointmentSaleSimplifyVOList());
            response.setBookingSaleVOList(allGoodsInfoIdsResponse.getBookingSaleSimplifyVOList());
        }

        if(commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
            IepSettingVO iepSettingInfo = commonUtil.getIepSettingInfo();
            response.setIepFlag(Boolean.TRUE);
            response.setEnterprisePriceName(iepSettingInfo.getEnterprisePriceName());
        }

        List<String> goodsInfos= response.getEsGoodsInfoPage().getContent().stream().map(EsGoodsInfoVO::getGoodsId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodsInfos)) {
            List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsInfos).build()).getContext().getGoodsVOList();
            response.getEsGoodsInfoPage().getContent().forEach(esGoodsInfoVO -> {
                goodsVOList.forEach(goodsVO -> {
                    if (Objects.equals(esGoodsInfoVO.getGoodsId(),goodsVO.getGoodsId()) && Objects.nonNull(goodsVO.getGoodsSubtitle())) {
                        esGoodsInfoVO.setGoodsSubtitle(goodsVO.getGoodsSubtitle());
                    }
                });
            });
        }


        return response;
    }

    private List<GoodsLevelPriceVO> getGoodsLevelPrices(List<String> skuIds, CustomerVO customer) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return Collections.emptyList();
        }
        //等级价格
        GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest = new GoodsLevelPriceBySkuIdsRequest();
        goodsLevelPriceBySkuIdsRequest.setSkuIds(skuIds);
        goodsLevelPriceBySkuIdsRequest.setType(PriceType.ENTERPRISE_SKU);
        List<GoodsLevelPriceVO> goodsLevelPriceList = goodsLevelPriceQueryProvider
                .listBySkuIds(goodsLevelPriceBySkuIdsRequest).getContext().getGoodsLevelPriceList();
        if (CollectionUtils.isNotEmpty(goodsLevelPriceList)) {
            goodsLevelPriceList = goodsLevelPriceList.stream()
                    .filter(goodsLevelPrice -> goodsLevelPrice.getLevelId().equals(customer.getCustomerLevelId()))
                    .collect(Collectors.toList());
        }
        return goodsLevelPriceList;
    }

    /**
     * 包装EsGoodsInfoQueryRequest搜索对象
     *
     * @param queryRequest
     * @return
     */
    private EsGoodsInfoQueryRequest wrapEsGoodsInfoQueryRequest(EsGoodsInfoQueryRequest queryRequest) {
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        queryRequest.setContractStartDate(now);
        queryRequest.setContractEndDate(now);
        queryRequest.setCustomerLevelId(0L);
        queryRequest.setCustomerLevelDiscount(BigDecimal.ONE);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        if(CYCLE_BUY.equals(queryRequest.getKeywords())) {
            queryRequest.setGoodsType(GoodsType.CYCLE_BUY);
            queryRequest.setKeywords(null);
        }
        return queryRequest;
    }

    /**
     * 小B-店铺管理页，选品页、编辑，小C-店铺精选页，分销商品统一验证接口
     *
     * @param queryRequest
     * @param response
     * @return
     */
    private EsGoodsInfoResponse filterDistributionGoods(EsGoodsInfoQueryRequest queryRequest, EsGoodsInfoResponse
            response, Map<String, String> map) {
        List<EsGoodsInfoVO> esGoodsInfoList = response.getEsGoodsInfoPage().getContent();
        if (CollectionUtils.isNotEmpty(esGoodsInfoList)) {


            List<GoodsInfoVO> goodsInfoList = esGoodsInfoList.stream().filter(e -> Objects.nonNull(e.getGoodsInfo()))
                    .map(e -> KsBeanUtil.convert(e.getGoodsInfo(), GoodsInfoVO.class))
                    .collect(Collectors.toList());
            queryRequest.setDistributionGoodsAudit(DistributionGoodsAudit.CHECKED.toValue());
            if (CollectionUtils.isEmpty(goodsInfoList)) {
                return new EsGoodsInfoResponse();
            }

            String customerId = commonUtil.getOperatorId();
            BaseResponse<DistributorLevelByCustomerIdResponse> baseResponse = null;
            if (StringUtils.isNotBlank(customerId)) {
                baseResponse = distributionService.getByCustomerId(customerId);
            }
            final BaseResponse<DistributorLevelByCustomerIdResponse> resultBaseResponse = baseResponse;
            //重新赋值于Page内部对象
            Map<String, GoodsInfoNestVO> voMap = goodsInfoList.stream()
                    .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> KsBeanUtil.convert(g, GoodsInfoNestVO
                            .class), (s, a) -> s));
            response.getEsGoodsInfoPage().getContent().forEach(esGoodsInfo -> {
                GoodsInfoNestVO goodsInfoNest = esGoodsInfo.getGoodsInfo();
                if (Objects.nonNull(goodsInfoNest)) {
                    goodsInfoNest = voMap.get(goodsInfoNest.getGoodsInfoId());
                    if (Objects.nonNull(goodsInfoNest)) {
                        goodsInfoNest.setDistributionGoodsInfoId(map.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId()));
                        DistributorLevelVO distributorLevelVO = Objects.isNull(resultBaseResponse) ? null :
                                resultBaseResponse.getContext().getDistributorLevelVO();
                        if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate()) && DistributionGoodsAudit.CHECKED == goodsInfoNest.getDistributionGoodsAudit()) {
                            goodsInfoNest.setDistributionCommission(distributionService.calDistributionCommission(goodsInfoNest.getDistributionCommission(), distributorLevelVO.getCommissionRate()));
                        }
                        esGoodsInfo.setGoodsInfo(goodsInfoNest);
                    }
                }
            });
        }
        return response;
    }

    /**
     * 分页查询分销员商品列表
     *
     * @param queryRequest
     * @param customerId
     * @return
     */
    private MicroServicePage<DistributorGoodsInfoVO> pageDistributorGoodsInfoPageByCustomerId(EsGoodsInfoQueryRequest
                                                                                                      queryRequest,
                                                                                              String customerId) {
        DistributorGoodsInfoPageByCustomerIdRequest distributorGoodsInfoPageByCustomerIdRequest = new
                DistributorGoodsInfoPageByCustomerIdRequest();
        distributorGoodsInfoPageByCustomerIdRequest.setCustomerId(customerId);
        distributorGoodsInfoPageByCustomerIdRequest.setPageNum(queryRequest.getPageNum());
        distributorGoodsInfoPageByCustomerIdRequest.setPageSize(queryRequest.getPageSize());
        BaseResponse<DistributorGoodsInfoPageByCustomerIdResponse> baseResponse = distributorGoodsInfoQueryProvider
                .pageByCustomerId(distributorGoodsInfoPageByCustomerIdRequest);
        MicroServicePage<DistributorGoodsInfoVO> microServicePage = baseResponse.getContext().getMicroServicePage();
        return microServicePage;
    }

    /**
     * 过滤符合条件的分销商品数据
     *
     * @param goodsInfoList
     * @param queryRequest
     * @return
     */
    private List<GoodsInfoVO> filterDistributionGoods(List<GoodsInfoVO> goodsInfoList, EsGoodsInfoQueryRequest
            queryRequest) {
        //根据开关重新设置分销商品标识
        distributionService.checkDistributionSwitch(goodsInfoList);
        //只看分享赚商品信息
        if (Objects.nonNull(queryRequest.getDistributionGoodsAudit()) && DistributionGoodsAudit.CHECKED.toValue() ==
                queryRequest.getDistributionGoodsAudit()) {
            goodsInfoList = goodsInfoList.stream().filter(goodsInfoVO -> DistributionGoodsAudit.CHECKED.equals
                    (goodsInfoVO.getDistributionGoodsAudit())).collect(Collectors.toList());
        }
        return goodsInfoList;
    }

    /**
     * 计算区间价、营销价
     *
     * @param response
     * @param customer
     * @return
     */
    private EsGoodsInfoResponse calIntervalPriceAndMarketingPrice(EsGoodsInfoResponse response, CustomerVO customer,
                                                                  EsGoodsInfoQueryRequest queryRequest) {
        List<EsGoodsInfoVO> esGoodsInfoList = response.getEsGoodsInfoPage().getContent();
        if (CollectionUtils.isNotEmpty(esGoodsInfoList)) {
            List<GoodsInfoVO> goodsInfoList = esGoodsInfoList.stream().filter(e -> Objects.nonNull(e.getGoodsInfo()))
                    .map(e -> {
                        GoodsInfoVO goodsInfoVO = KsBeanUtil.convert(e.getGoodsInfo(), GoodsInfoVO.class);
                        Integer enterPriseAuditStatus = e.getGoodsInfo().getEnterPriseAuditStatus();
                        if (Objects.nonNull(enterPriseAuditStatus)) {
                            goodsInfoVO.setEnterPriseAuditState(EnterpriseAuditState.CHECKED.toValue() == enterPriseAuditStatus ?
                                    EnterpriseAuditState.CHECKED : null);
                        }
                        return goodsInfoVO;
                    })
                    .collect(Collectors.toList());

            goodsInfoList = filterDistributionGoods(goodsInfoList, queryRequest);
            if (CollectionUtils.isEmpty(goodsInfoList)) {
                return new EsGoodsInfoResponse();
            }

            //计算区间价
            GoodsIntervalPriceByCustomerIdRequest priceRequest = new GoodsIntervalPriceByCustomerIdRequest();
            priceRequest.setGoodsInfoDTOList(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class));
            if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
                priceRequest.setCustomerId(customer.getCustomerId());
            }
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceProvider.putByCustomerId(priceRequest).getContext();
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            //等级价格
            if (Objects.nonNull(customer)) {
                List<String> skuIds = goodsInfoList.stream()
                        .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest = new GoodsLevelPriceBySkuIdsRequest();
                goodsLevelPriceBySkuIdsRequest.setSkuIds(skuIds);
                goodsLevelPriceBySkuIdsRequest.setType(EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState()) ? PriceType.ENTERPRISE_SKU : PriceType.SKU);
                List<GoodsLevelPriceVO> goodsLevelPriceList = goodsLevelPriceQueryProvider
                        .listBySkuIds(goodsLevelPriceBySkuIdsRequest).getContext().getGoodsLevelPriceList();
                if (CollectionUtils.isNotEmpty(goodsLevelPriceList)) {
                    goodsLevelPriceList = goodsLevelPriceList.stream()
                            .filter(goodsLevelPrice -> goodsLevelPrice.getLevelId().equals(customer.getCustomerLevelId()))
                            .collect(Collectors.toList());
                }
                response.setGoodsLevelPrices(goodsLevelPriceList);
            }
            goodsInfoList = priceResponse.getGoodsInfoVOList();
            //计算营销价格
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class));
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }
            filterRequest.setMoFangFlag(queryRequest.getMoFangFlag());
            GoodsInfoListByGoodsInfoResponse filterResponse = marketingPluginProvider.goodsListFilter(filterRequest)
                    .getContext();
            if (Objects.nonNull(filterResponse) && CollectionUtils.isNotEmpty(filterResponse.getGoodsInfoVOList())) {
                goodsInfoList = filterResponse.getGoodsInfoVOList();
                goodsInfoList.forEach(goodsInfoVO -> {
                    if(CollectionUtils.isNotEmpty(goodsInfoVO.getIntervalPriceList()))
                    response.getGoodsIntervalPrices().addAll(goodsInfoVO.getIntervalPriceList());
                });
            }

            //填充
            if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
                PurchaseFillBuyCountRequest purchaseFillBuyCountRequest = new PurchaseFillBuyCountRequest();
                purchaseFillBuyCountRequest.setGoodsInfoList(goodsInfoList);
                purchaseFillBuyCountRequest.setCustomerId(customer.getCustomerId());
                PurchaseFillBuyCountResponse purchaseFillBuyCountResponse = purchaseProvider.fillBuyCount
                        (purchaseFillBuyCountRequest).getContext();
                goodsInfoList = purchaseFillBuyCountResponse.getGoodsInfoList();
            }

            goodsInfoList = goodsInfoList.stream().map(goodsInfoVO -> {
                EnterpriseAuditState enterpriseAuditState = goodsInfoVO.getEnterPriseAuditState();
                if (EnterpriseAuditState.CHECKED == enterpriseAuditState) {
                    goodsInfoVO.setGrouponLabel(null);
                }
                return goodsInfoVO;
            }).collect(Collectors.toList());

            //重新赋值于Page内部对象
            Map<String, GoodsInfoNestVO> voMap = goodsInfoList.stream()
                    .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> KsBeanUtil.convert(g,
                            GoodsInfoNestVO.class), (s, a) -> s));
            response.getEsGoodsInfoPage().getContent().forEach(esGoodsInfo -> {
                GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();

                GoodsInfoNestVO goodsInfoNest = voMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId());
                if (Objects.nonNull(goodsInfoNest)) {
                    goodsInfoNest.setGoodsEvaluateNum(goodsInfo.getGoodsEvaluateNum());
                    goodsInfoNest.setGoodsCollectNum(goodsInfo.getGoodsCollectNum());
                    goodsInfoNest.setGoodsFavorableCommentNum(goodsInfo.getGoodsFavorableCommentNum());
                    goodsInfoNest.setGoodsFeedbackRate(goodsInfo.getGoodsFeedbackRate());
                    goodsInfoNest.setGoodsSalesNum(goodsInfo.getGoodsSalesNum());
                    goodsInfoNest.setEnterPriseAuditStatus(goodsInfo.getEnterPriseAuditStatus());
                    goodsInfoNest.setSpecText(goodsInfo.getSpecText());
                    esGoodsInfo.setGoodsInfo(goodsInfoNest);
                }
            });
        }
        return response;
    }

    /**
     * 商品详情
     *
     * @param skuId    商品skuId
     * @param customer 会员
     * @return 商品详情
     */
    private GoodsInfoDetailByGoodsInfoResponse detail(String skuId, CustomerVO customer) {
        //获取会员和等级
        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        goodsInfoRequest.setGoodsInfoId(skuId);
        goodsInfoRequest.setShowLabelFlag(true);
        goodsInfoRequest.setShowSiteLabelFlag(true);
        GoodsInfoDetailByGoodsInfoResponse response = goodsInfoSiteQueryProvider.getByGoodsInfo(goodsInfoRequest)
                .getContext();
        //计算区间价
        GoodsIntervalPriceByCustomerIdRequest goodsIntervalPriceByCustomerIdRequest = new
                GoodsIntervalPriceByCustomerIdRequest();
        goodsIntervalPriceByCustomerIdRequest.setGoodsInfoDTOList(KsBeanUtil.convert(Arrays.asList(response
                .getGoodsInfo()), GoodsInfoDTO.class));
        if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
            goodsIntervalPriceByCustomerIdRequest.setCustomerId(customer.getCustomerId());
        }
        GoodsIntervalPriceByCustomerIdResponse goodsIntervalPriceByCustomerIdResponse = goodsIntervalPriceProvider
                .putByCustomerId(goodsIntervalPriceByCustomerIdRequest).getContext();
        response.setGoodsIntervalPrices(goodsIntervalPriceByCustomerIdResponse.getGoodsIntervalPriceVOList());
        if (CollectionUtils.isNotEmpty(goodsIntervalPriceByCustomerIdResponse.getGoodsInfoVOList())) {
            response.setGoodsInfo(goodsIntervalPriceByCustomerIdResponse.getGoodsInfoVOList().get(0));
        }

        //计算营销价格
        MarketingPluginGoodsDetailFilterRequest filterRequest = new MarketingPluginGoodsDetailFilterRequest();
        filterRequest.setGoodsInfoDetailByGoodsInfoDTO(KsBeanUtil.convert(response, GoodsInfoDetailByGoodsInfoDTO
                .class));
        if (Objects.nonNull(customer)) {
            filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        }
        response = KsBeanUtil.convert(marketingPluginProvider.goodsDetailFilter(filterRequest).getContext(),
                GoodsInfoDetailByGoodsInfoResponse.class);

        if (Objects.nonNull(response.getGoods().getStoreId())) {
            StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(response.getGoods()
                    .getStoreId())).getContext().getStoreVO();
            response.setStoreLogo(store.getStoreLogo());
            response.setStoreName(store.getStoreName());
            response.setCompanyType(store.getCompanyType());
            response.setStoreId(store.getStoreId());
        }
        return response;
    }

    /**
     * 根据商品skuId批量查询商品sku列表
     *
     * @param request 根据批量商品skuId查询结构 {@link GoodsInfoListByIdsRequest}
     * @return 商品sku列表 {@link GoodsInfoListByIdsResponse}
     */
    @ApiOperation(value = "根据商品skuId批量查询商品sku列表")
    @RequestMapping(value = "/info/list-by-ids", method = RequestMethod.POST)
    BaseResponse<GoodsInfoListByIdsResponse> listByIds(@RequestBody @Valid GoodsInfoListByIdsRequest request) {
        return goodsInfoQueryProvider.listByIds(request);
    }

    private void providerSkuStockSync(EsGoodsInfoResponse response) {
        //供应商库存同步
        List<GoodsInfoNestVO> goodsInfoNests =
                response.getEsGoodsInfoPage().getContent().stream().map(EsGoodsInfoVO::getGoodsInfo)
                        .collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOList = KsBeanUtil.convert(goodsInfoNests, GoodsInfoDTO.class);
        Map<String, GoodsInfoVO> goodsInfoVOMap =
                goodsInfoProvider.providerGoodsStockSync(new ProviderGoodsStockSyncRequest
                        (goodsInfoDTOList)).getContext().getGoodsInfoList().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        for (EsGoodsInfoVO esGoodsInfo : response.getEsGoodsInfoPage().getContent()) {
            esGoodsInfo.getGoodsInfo().setStock(goodsInfoVOMap.get(esGoodsInfo.getGoodsInfo().getGoodsInfoId()).getStock());
        }
    }
}
