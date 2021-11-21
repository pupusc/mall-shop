package com.wanmi.sbc.goods;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.constants.WebBaseErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelWithDefaultByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelWithDefaultByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.distribute.DistributionService;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.bean.dto.goods.EsGoodsInfoDTO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleAndBookingSaleRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsDetailProperBySkuIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsDetailSimpleRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdAndSkuIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsViewByPointsGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCacheInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoSmallProgramCodeRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleAndBookingSaleResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsDetailProperResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsDetailSimpleResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdAndSkuIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByPointsGoodsIdResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByGoodsAndSkuResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.DistributorGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedPurchaseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.request.GrouponGoodsViewByIdResponse;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityingByGoodsInfoIdsRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptStatus;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptType;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.order.api.provider.groupon.GrouponProvider;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseProvider;
import com.wanmi.sbc.order.api.request.groupon.GrouponDetailQueryRequest;
import com.wanmi.sbc.order.api.request.groupon.GrouponDetailWithGoodsRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFillBuyCountRequest;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailQueryResponse;
import com.wanmi.sbc.order.api.response.groupon.GrouponDetailWithGoodsResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseFillBuyCountResponse;
import com.wanmi.sbc.order.bean.vo.GrouponDetailVO;
import com.wanmi.sbc.order.bean.vo.GrouponDetailWithGoodsVO;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.request.ShareMiniProgramRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.topic.service.AtmosphereService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @menu 商城配合知识顾问
 * @tag feature_d_cps3
 * @status undone
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "GoodsBaseController", description = "S2B web公用-商品信息API")
@RefreshScope
@Slf4j
public class GoodsBaseController {

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private PurchaseProvider purchaseProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private DistributorGoodsInfoQueryProvider distributorGoodsInfoQueryProvider;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private GrouponProvider grouponProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    //商品不存在
    private static final String GOODSNOTEXIST = "商品不存在";

    public static final String CYCLE_BUY = "周期购";

    @Autowired
    private CustomerProvider customerProvider;
    @Value("${know.ordinary.good.ids}")
    private String goodIds;

    @Value("${search.unshow.goodsIds}")
    private String searchUnShowGoodsIds;

    @Autowired
    private AtmosphereService atmosphereService;
    /**
     * @description 商品分页(ES级)
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "商品分页")
    @RequestMapping(value = "/spus", method = RequestMethod.POST)
    public BaseResponse<EsGoodsResponse> goodslist(@RequestBody EsGoodsInfoQueryRequest queryRequest) {
        CustomerVO customerVO = commonUtil.getCustomer();
        if (customerVO == null || StringUtils.isEmpty(customerVO.getFanDengUserNo())) {
            queryRequest.setCpsSpecial(0);
        } else {
            CounselorDto counselorDto = customerProvider.isCounselor(Integer.valueOf(customerVO.getFanDengUserNo())).getContext();
            if (Objects.isNull(counselorDto)) {
                queryRequest.setCpsSpecial(0);
            }
        }

        return BaseResponse.success(list(queryRequest, commonUtil.getCustomer()));
    }

    /**
     * 未登录时,查询商品分页(ES级)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "未登录时,查询商品分页")
    @RequestMapping(value = "/spuListFront", method = RequestMethod.POST)
    public BaseResponse<EsGoodsResponse> spuListFront(@RequestBody EsGoodsInfoQueryRequest queryRequest) {
        queryRequest.setCpsSpecial(0);
        EsGoodsResponse response = list(queryRequest, null);
        if (CollectionUtils.isNotEmpty(response.getEsGoods().getContent())) {
            Map<String, List<EsGoodsInfoDTO>> buyCountMap =
                    queryRequest.getEsGoodsInfoDTOList().stream()
                            .collect(Collectors.groupingBy(EsGoodsInfoDTO::getGoodsInfoId));

            if (MapUtils.isEmpty(buyCountMap)) {
                return BaseResponse.success(response);
            }
            response.getEsGoods().getContent().forEach(goods -> {
                goods.getGoodsInfos().stream()
                        .filter(goodsInfo -> Objects.nonNull(goodsInfo) && buyCountMap.containsKey(goodsInfo
                                .getGoodsInfoId()))
                        .forEach(goodsInfo -> goodsInfo.setBuyCount(
                                buyCountMap.get(goodsInfo.getGoodsInfoId()).get(0).getGoodsNum()));
            });
        }
        return BaseResponse.success(response);
    }

    /**
     * Spu商品详情，放开删除的sku商品和下架的sku商品，前端判断多规格自动切换
     * @description 查询Spu商品详情
     * @menu 商品
     * @param skuId
     * @status done
     */
    @ApiOperation(value = "查询Spu商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/spu/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdResponse> detail(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, commonUtil.getCustomer(), Boolean.TRUE));
    }

    /**
     * Spu商品详情，放开删除的sku商品和下架的sku商品，前端判断多规格自动切换
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "购物车弹框查询Spu商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/spu/shopcart/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdResponse> detailForShopCart(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, commonUtil.getCustomer(), Boolean.FALSE));
    }

    /**
     * 积分Spu商品详情
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "查询积分Spu商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "pointsGoodsId", value = "pointsGoodsId",
            required = true)
    @RequestMapping(value = "/points/spu/{pointsGoodsId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByPointsGoodsIdResponse> pointsGoodsDetail(@PathVariable String pointsGoodsId) {
        GoodsViewByPointsGoodsIdRequest request = new GoodsViewByPointsGoodsIdRequest();
        request.setPointsGoodsId(pointsGoodsId);
        request.setShowLabelFlag(true);
        request.setShowSiteLabelFlag(true);
        return BaseResponse.success(goodsQueryProvider.getViewByPointsGoodsId(request).getContext());
    }


    /**
     * 查询商品图文信息和属性
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "查询商品图文信息和属性")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId",
            required = true)
    @RequestMapping(value = "/goodsDetailProper/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsDetailProperResponse> goodsDetailProper(@PathVariable String skuId) {
        GoodsDetailProperBySkuIdRequest request = new GoodsDetailProperBySkuIdRequest();
        request.setSkuId(skuId);
        return BaseResponse.success(goodsQueryProvider.getGoodsDetail(request).getContext());
    }


    /**
     * 查询商品页面展示简易信息
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "查询商品页面展示简易信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId",
            required = true)
    @RequestMapping(value = "/goodsDetailSimple/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsDetailSimpleResponse> goodsDetailSimple(@PathVariable String skuId) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build
                ()).getContext();

        if (Objects.isNull(goodsInfo) || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        GoodsDetailSimpleRequest request = new GoodsDetailSimpleRequest();
        request.setGoodsId(goodsInfo.getGoodsId());
        GoodsDetailSimpleResponse response = goodsQueryProvider.getGoodsDetailSimple(request).getContext();
        return BaseResponse.success(response);
    }


    /**
     * 小C-店铺精选-进入商品详情
     *
     * @return 返回分页结果
     */
    @ApiOperation(value = "小C-店铺精选-进入商品详情")
    @RequestMapping(value = "/shop/goods-detail/{distributorId}/{goodsId}/{goodsInfoId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdAndSkuIdsResponse> shopGoodsDetail(@PathVariable String distributorId,
                                                                        @PathVariable String goodsId, @PathVariable
                                                                                String goodsInfoId) {
        DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest request = new
                DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest(distributorId, goodsInfoId, goodsId);
        BaseResponse<DistributorGoodsInfoListByCustomerIdAndGoodsIdResponse> baseResponse =
                distributorGoodsInfoQueryProvider.listByCustomerIdAndGoodsId(request);
        List<DistributorGoodsInfoVO> list = baseResponse.getContext().getDistributorGoodsInfoVOList();
        if (CollectionUtils.isEmpty(list)) {
            return BaseResponse.info(CommonErrorCode.SUCCESSFUL, GOODSNOTEXIST);
        }
        List<String> skuIds = list.stream().map(DistributorGoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        return BaseResponse.success(goodsDetailBaseInfo(request.getGoodsInfoId(), skuIds, distributorId));
    }

    @ApiOperation(value = "未登录时,购物车弹框查询查询Spu商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/unLogin/spu/shopcart/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdResponse> unLoginDetailForShopCart(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, null, Boolean.FALSE));
    }

    @ApiOperation(value = "未登录时,查询Spu商品详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/unLogin/spu/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsViewByIdResponse> unLoginDetail(@PathVariable String skuId) {
        return BaseResponse.success(detail(skuId, null, Boolean.TRUE));
    }


    /**
     * 查询SPU列表
     *
     * @param queryRequest 查询列表
     * @param customer     会员
     * @return spu商品封装数据
     */
    private EsGoodsResponse list(EsGoodsInfoQueryRequest queryRequest, CustomerVO customer) {
        if (queryRequest.getIsFix()) {
            queryRequest.setGoodsIds(Arrays.asList(goodIds.split(",")));
        }
        if (queryRequest.getCpsSpecial() != null) {
            queryRequest.setSortFlag(11); //添加排序
        }
        if (Objects.nonNull(queryRequest.getMarketingId())) {
            MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
            marketingGetByIdRequest.setMarketingId(queryRequest.getMarketingId());
            queryRequest.setGoodsInfoIds(
                    marketingQueryProvider.getByIdForCustomer(marketingGetByIdRequest).getContext()
                            .getMarketingForEndVO().getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId)
                            .collect(Collectors.toList()));
        }
        //只看分享赚商品信息
        if (Objects.nonNull(queryRequest.getDistributionGoodsAudit()) && DistributionGoodsAudit.CHECKED.toValue() ==
                queryRequest.getDistributionGoodsAudit()) {
            queryRequest.setDistributionGoodsStatus(NumberUtils.INTEGER_ZERO);
        }
        //获取会员和等级
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        //B2b模式下，可以按会员/客户价格排序，否则按市场价排序
        if (Objects.nonNull(customer) && osUtil.isB2b()) {
            CustomerLevelWithDefaultByCustomerIdResponse response = customerLevelQueryProvider
                    .getCustomerLevelWithDefaultByCustomerId(
                            CustomerLevelWithDefaultByCustomerIdRequest.builder().customerId(customer.getCustomerId()
                            ).build
                                    ()).getContext();
            queryRequest.setCustomerLevelId(response.getLevelId());
            queryRequest.setCustomerLevelDiscount(response.getLevelDiscount());
        } else {
            String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
            queryRequest.setContractStartDate(now);
            queryRequest.setContractEndDate(now);
            queryRequest.setCustomerLevelId(0L);
            queryRequest.setCustomerLevelDiscount(BigDecimal.ONE);
        }
        if(CYCLE_BUY.equals(queryRequest.getKeywords())) {
            queryRequest.setGoodsType(GoodsType.CYCLE_BUY);
            queryRequest.setKeywords(null);
        }
        if (!StringUtils.isBlank(searchUnShowGoodsIds)) {
            log.info("---->>> 搜索过滤的 goodsIdi is {}", searchUnShowGoodsIds);
            String[] searchUnShowGoodsIdAttr = searchUnShowGoodsIds.split(",");
            queryRequest.setUnGoodsIds(Arrays.asList(searchUnShowGoodsIdAttr));
        }
        EsGoodsResponse response = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext();
        //如果是linkedmall商品，实时查库存
        List<GoodsInfoNestVO> goodsInfoNestList = response.getEsGoods().getContent().stream()
                .flatMap(v -> v.getGoodsInfos().stream()).collect(Collectors.toList());
        response.getEsGoods().getContent().stream().flatMap(v -> v.getGoodsInfos().stream()).collect(Collectors.toList());
        List<Long> itemIds = goodsInfoNestList.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (GoodsInfoNestVO goodsInfo : goodsInfoNestList) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList()
                                .stream()
                                .filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId()))
                                .findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            goodsInfo.setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(skuStock > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        if (Objects.nonNull(response.getEsGoods()) && CollectionUtils.isNotEmpty(response.getEsGoods().getContent())) {
            //非商品抵扣方式下，清理商品积分
            systemPointsConfigService.clearBuyPoinsForEsSpu(response.getEsGoods().getContent());
            List<GoodsInfoVO> goodsInfoList = response.getEsGoods().getContent().stream().map(EsGoodsVO::getGoodsInfos)
                    .flatMap(Collection::stream).map(goods -> {
                        GoodsInfoVO goodsInfoVO = KsBeanUtil.convert(goods, GoodsInfoVO.class);
                        goodsInfoVO.setVendibility(goods.getVendibilityStatus());
                        Integer enterPriseAuditStatus = goods.getEnterPriseAuditStatus();
                        if (Objects.nonNull(enterPriseAuditStatus)) {
                            goodsInfoVO.setEnterPriseAuditState(EnterpriseAuditState.CHECKED.toValue() == enterPriseAuditStatus ?
                                    EnterpriseAuditState.CHECKED : null);
                        }
                        return goodsInfoVO;
                    })
                    .collect(Collectors.toList());
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoList);
            //只看分享赚商品信息
            if (Objects.nonNull(queryRequest.getDistributionGoodsAudit()) && DistributionGoodsAudit.CHECKED.toValue()
                    == queryRequest.getDistributionGoodsAudit()) {
                goodsInfoList = goodsInfoList.stream().filter(goodsInfoVO -> DistributionGoodsAudit.CHECKED.equals
                        (goodsInfoVO.getDistributionGoodsAudit())).collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(goodsInfoList)) {
                return new EsGoodsResponse();
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
            }
            //填充
            if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
                PurchaseFillBuyCountRequest request = new PurchaseFillBuyCountRequest();
                request.setCustomerId(customer.getCustomerId());
                request.setGoodsInfoList(goodsInfoList);
                request.setInviteeId(commonUtil.getPurchaseInviteeId());
                PurchaseFillBuyCountResponse purchaseFillBuyCountResponse = purchaseProvider.fillBuyCount(request)
                        .getContext();
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
            Map<String, List<GoodsInfoVO>> voMap = goodsInfoList.stream().collect(Collectors.groupingBy
                    (GoodsInfoVO::getGoodsId));
            //等级价格
            List<String> skuIds = new ArrayList<>();
            for (EsGoodsVO esGoodsVO : response.getEsGoods().getContent()) {
                skuIds.addAll(esGoodsVO.getGoodsInfos().stream()
                        .map(GoodsInfoNestVO::getGoodsInfoId).collect(Collectors.toList()));
            }
            List<GoodsLevelPriceVO> goodsLevelPrices = this.getGoodsLevelPrices(skuIds, customer);

            List<String> goodsIds = response.getEsGoods().getContent().stream().map(EsGoodsVO::getId).collect(Collectors.toList());

            List<GoodsVO> goodsVOList = goodsQueryProvider.listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsIds).build()).getContext().getGoodsVOList();


            response.getEsGoods().getContent().forEach(esGoods -> {
                List<GoodsInfoVO> goodsInfoVOList = voMap.get(esGoods.getId());
                List<GoodsInfoNestVO> goodsInfoNests = esGoods.getGoodsInfos();
                List<GoodsInfoNestVO> resultGoodsInfos = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
                    goodsInfoVOList.forEach(goodsInfoVO -> {
                        goodsInfoVO.setGoodsFavorableCommentNum(esGoods.getGoodsFavorableCommentNum());
                        goodsInfoVO.setGoodsSalesNum(esGoods.getGoodsSalesNum());
                        goodsInfoVO.setGoodsCollectNum(esGoods.getGoodsCollectNum());
                        goodsInfoVO.setGoodsEvaluateNum(esGoods.getGoodsEvaluateNum());
                        GoodsInfoNestVO goodsInfoNest = KsBeanUtil.convert(goodsInfoVO, GoodsInfoNestVO.class);
                        goodsInfoNest.setVendibilityStatus(goodsInfoVO.getVendibility());
                        //知识顾问商品不显示优惠
                        if (esGoods.getCpsSpecial() != null && esGoods.getCpsSpecial() == 1) {
                            goodsInfoNest.setSalePrice(goodsInfoNest.getMarketPrice());
                        }
                        resultGoodsInfos.add(goodsInfoNest);
                    });
                    //设置企业商品的审核状态
                    resultGoodsInfos.forEach(goodsInfoNest -> {
                        Optional<GoodsInfoNestVO> optionalGoodsInfoNest =
                                goodsInfoNests.stream().filter((g) -> g.getGoodsInfoId().equals(goodsInfoNest.getGoodsInfoId())).findFirst();
                        if (optionalGoodsInfoNest.isPresent()) {
                            goodsInfoNest.setEnterPriseAuditStatus(optionalGoodsInfoNest.get().getEnterPriseAuditStatus());
                        }
                        //判断当前用户对应企业购商品等级企业价
                        if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                            Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                                    .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(goodsInfoNest.getGoodsInfoId()))
                                    .findFirst();
                            goodsInfoNest.setEnterPrisePrice(first.isPresent() ? first.get().getPrice() : goodsInfoNest.getEnterPrisePrice());
                        }
                    });
                    esGoods.setGoodsInfos(resultGoodsInfos);
                }

                if (CollectionUtils.isNotEmpty(goodsVOList)) {
                    goodsVOList.forEach(goodsVO -> {
                        if (Objects.equals(esGoods.getId(),goodsVO.getGoodsId()) && Objects.nonNull(goodsVO.getGoodsSubtitle())) {
                            esGoods.setGoodsSubtitle(goodsVO.getGoodsSubtitle());
                        }
                    });
                }

            });
            // 填充预约、预售信息
            List<String> goodInfoIdList = goodsInfoList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
                response.setAppointmentSaleVOList(appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList
                        (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getAppointmentSaleVOList());
                response.setBookingSaleVOList(bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList
                        (BookingSaleInProgressRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext().getBookingSaleVOList());
            }
        }
        return response;
    }


    /**
     * SPU商品详情
     *
     * @param skuId    商品skuId
     * @param customer 会员
     * @return SPU商品详情
     * fullMarketing
     */
    private GoodsViewByIdResponse detail(String skuId, CustomerVO customer, Boolean fullMarketing) {

        String customerId = null;
        if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
            customerId = customer.getCustomerId();
        }
        GoodsViewByIdResponse response = goodsDetailBaseInfoNew(skuId, customerId);
        if (response.getGoods().getCpsSpecial() != null && response.getGoods().getCpsSpecial() == 1) {
            if (customer == null || StringUtils.isEmpty(customer.getFanDengUserNo())) {
                throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
            }
            CounselorDto counselorDto = customerProvider.isCounselor(Integer.valueOf(customer.getFanDengUserNo())).getContext();
            if (Objects.isNull(counselorDto)) {
                throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
            }
        }
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos().stream()
                .filter(g -> {
                    if (Objects.isNull(g.getProviderId())) {
                        if (g.getAddedFlag() == AddedFlag.YES.toValue()) {
                            return Boolean.TRUE;
                        }
                    } else if (Constants.yes.equals(g.getVendibility()) && g.getAddedFlag() == AddedFlag.YES.toValue()) {
                        return Boolean.TRUE;
                    }
                    return Boolean.FALSE;
                })
                .collect(Collectors.toList());
        //当前sku不在可售之内，则不存在
        if (goodsInfoVOList.stream().noneMatch(i -> i.getGoodsInfoId().equals(skuId))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        List<GoodsLevelPriceVO> goodsLevelPrices = this.getGoodsLevelPrices(response.getGoodsInfos().stream()
                .map(GoodsInfoVO::getGoodsInfoId)
                .collect(Collectors.toList()), customer);
        final GoodsVO goodsVO = response.getGoods();
        List<GoodsInfoVO> goodsInfos = response.getGoodsInfos().stream().map(goodsinfo -> {
            BigDecimal marketPrice = goodsinfo.getMarketPrice();
            marketPrice = marketPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
            goodsinfo.setMarketPrice(marketPrice);
            //知识顾问专属商品没有其他优惠
            if (goodsVO.getCpsSpecial() != null && goodsVO.getCpsSpecial() == 1) {
                goodsinfo.setSalePrice(marketPrice);
                goodsinfo.setPaidCardPrice(marketPrice);
            }
            if (Objects.nonNull(goodsinfo.getAppointmentSaleVO()) && CollectionUtils.isNotEmpty(goodsinfo.getAppointmentSaleVO().getAppointmentSaleGoods())) {
                List<AppointmentSaleGoodsVO> appointmentSaleGoods = goodsinfo.getAppointmentSaleVO().getAppointmentSaleGoods().stream().map(good -> {
                    BigDecimal price = good.getPrice();
                    price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
                    good.setPrice(price);
                    return good;
                }).collect(Collectors.toList());
                goodsinfo.getAppointmentSaleVO().setAppointmentSaleGoods(appointmentSaleGoods);
            }
            return goodsinfo;
        }).collect(Collectors.toList());
        response.setGoodsInfos(goodsInfos);

        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            response.setFullMarketing(fullMarketing);
            response = detailGoodsInfoVOListNew(response, goodsInfoVOList, customer);
            response.getGoodsInfos().forEach(goodsInfo -> {
                //判断当前用户对应企业购商品等级企业价
                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                            .findFirst();
                    goodsInfo.setEnterPrisePrice(first.isPresent() ? first.get().getPrice() : goodsInfo.getEnterPrisePrice());
                }
            });
        } else {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        //sku氛围
        List<String> skuIds = goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(skuIds)){
            List<AtmosphereDTO> atmos =atmosphereService.getAtmosphere(skuIds);
            if(CollectionUtils.isNotEmpty(atmos)){
                response.getGoodsInfos().forEach(g->{
                    if(atmos.stream().anyMatch(p->p.getSkuId().equals(g.getGoodsInfoId()))){
                        AtmosphereDTO atmosphereDTO = atmos.stream().filter(p->p.getSkuId().equals(g.getGoodsInfoId())).findFirst().get();
                        g.setImageUrl(atmosphereDTO.getImageUrl());
                        g.setAtmosType(atmosphereDTO.getAtmosType());
                        g.setElementFour(atmosphereDTO.getElementFour());
                        g.setElementThree(atmosphereDTO.getElementThree());
                        g.setElementTwo(atmosphereDTO.getElementTwo());
                        g.setElementOne(atmosphereDTO.getElementOne());
                    }
                });
            }
        }
        return response;
    }

    private List<GoodsLevelPriceVO> getGoodsLevelPrices(List<String> skuIds, CustomerVO customer) {
        List<GoodsLevelPriceVO> goodsLevelPriceList = new ArrayList<>();
        if (Objects.nonNull(customer) && CollectionUtils.isNotEmpty(skuIds)) {
            //等级价格
            GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest = new GoodsLevelPriceBySkuIdsRequest();
            goodsLevelPriceBySkuIdsRequest.setSkuIds(skuIds);
            goodsLevelPriceBySkuIdsRequest.setType(PriceType.ENTERPRISE_SKU);
            goodsLevelPriceList = goodsLevelPriceQueryProvider
                    .listBySkuIds(goodsLevelPriceBySkuIdsRequest).getContext().getGoodsLevelPriceList();
            if (CollectionUtils.isNotEmpty(goodsLevelPriceList)) {
                return goodsLevelPriceList.stream()
                        .filter(goodsLevelPrice -> goodsLevelPrice.getLevelId().equals(customer.getCustomerLevelId()))
                        .collect(Collectors.toList());
            }
        }
        return goodsLevelPriceList;
    }

    /**
     * SPU商品详情  优化
     *
     * @param response
     * @param goodsInfoVOList
     * @param customer
     * @return
     */
    private GoodsViewByIdResponse detailGoodsInfoVOListNew(GoodsViewByIdResponse response, List<GoodsInfoVO>
            goodsInfoVOList, CustomerVO customer) {
        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoVOList);

            //计算营销价格
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class));
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }

            // 注意这边是取反，非全量营销时isFlashSaleMarketing=true
            if (Objects.nonNull(response.getFullMarketing()) && Objects.equals(response.getFullMarketing(), Boolean.FALSE)) {
                // 排除秒杀
                filterRequest.setIsFlashSaleMarketing(Boolean.TRUE);
            }
            filterRequest.setIsIndependent(Boolean.TRUE);
            response.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext()
                    .getGoodsInfoVOList());
            response.getGoodsInfos().stream().forEach(
                    goodsInfoVO -> {
                        //知识顾问专属商品没有其他优惠
                        if (response.getGoods().getCpsSpecial() != null && response.getGoods().getCpsSpecial() == 1) {
                            goodsInfoVO.setPaidCardPrice(goodsInfoVO.getMarketPrice());
                            goodsInfoVO.setPaidCardIcon(null);
                        }
                    }

            );
        }
        return response;
    }


    /**
     * SPU商品详情
     *
     * @param response
     * @param goodsInfoVOList
     * @param customer
     * @return
     */
    private GoodsViewByIdResponse detailGoodsInfoVOList(GoodsViewByIdResponse response, List<GoodsInfoVO>
            goodsInfoVOList, CustomerVO customer) {
        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            //根据开关重新设置分销商品标识
            distributionService.checkDistributionSwitch(goodsInfoVOList);
            String customerId = null;
            if (Objects.nonNull(customer) && StringUtils.isNotBlank(customer.getCustomerId())) {
                customerId = customer.getCustomerId();
            }
            //预约，预售商品与其他营销活动互斥（在后面调用营销插件时，判断是否走插件中的逻辑）
            List<String> goodInfoIdList =
                    goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(goodInfoIdList)) {
                AppointmentSaleAndBookingSaleResponse allRes = appointmentSaleQueryProvider.mergeAppointmentSaleAndBookingSale(
                        AppointmentSaleAndBookingSaleRequest.builder().goodsInfoIdList(goodInfoIdList).build()).getContext();
                if (Objects.nonNull(allRes) &&
                        (CollectionUtils.isNotEmpty(allRes.getAppointmentSaleVOList()) || CollectionUtils.isNotEmpty(allRes.getBookingSaleVOList()))) {
                    goodsInfoVOList.forEach(goodsInfoVO -> {
                        allRes.getAppointmentSaleVOList().forEach(appointmentSaleVO -> {
                            if (appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoId().equals(goodsInfoVO.getGoodsInfoId())) {
                                goodsInfoVO.setAppointmentSaleVO(appointmentSaleVO);
                            }
                        });
                        allRes.getBookingSaleVOList().forEach(bookingSaleVO -> {
                            if (bookingSaleVO.getBookingSaleGoods().equals(goodsInfoVO.getGoodsInfoId())) {
                                goodsInfoVO.setBookingSaleVO(bookingSaleVO);
                            }
                        });
                    });
                }
            }

            //计算区间价
            GoodsIntervalPriceByGoodsAndSkuResponse priceResponse =
                    goodsIntervalPriceService.getGoodsAndSku(goodsInfoVOList,
                            Collections.singletonList(response.getGoods()), customerId);
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();

            //计算营销价格
            MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
            filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class));
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }

            // 注意这边是取反，非全量营销时isFlashSaleMarketing=true
            if (Objects.nonNull(response.getFullMarketing()) && Objects.equals(response.getFullMarketing(),
                    Boolean.FALSE)) {
                // 排除秒杀
                filterRequest.setIsFlashSaleMarketing(Boolean.TRUE);
            }
            response.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext()
                    .getGoodsInfoVOList());
            //限售加入限售信息
            if (Objects.nonNull(customer)) {
                response.setGoodsInfos(this.setRestrictedNum(response.getGoodsInfos(), customer));
            }
            response.setGoodsInfos(this.fillActivityInfo(response.getGoodsInfos()));
        }
        return response;
    }

    /**
     * SPU商品详情-基础信息（不包括区间价、营销信息） 优化
     *
     * @param skuId 商品skuId
     * @return SPU商品详情
     */
    private GoodsViewByIdResponse goodsDetailBaseInfoNew(String skuId, String customerId) {
        GoodsCacheInfoByIdRequest request = new GoodsCacheInfoByIdRequest();
        request.setCustomerId(customerId);
        request.setGoodsInfoId(skuId);
        request.setShowLabelFlag(true);
        request.setShowSiteLabelFlag(true);
        GoodsViewByIdResponse response = goodsQueryProvider.getCacheViewById(request).getContext();
        List<GoodsInfoVO> goodsInfo = response.getGoodsInfos().stream().filter(
                item -> StringUtils.equals(item.getGoodsInfoId(), skuId))
                .collect(Collectors.toList());
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        if (DefaultFlag.NO.equals(openFlag)
                || DefaultFlag.NO.equals(distributionCacheService.queryStoreOpenFlag(String.valueOf(response.getGoods().getStoreId())))
                || CollectionUtils.isEmpty(goodsInfo)
                || !DistributionGoodsAudit.CHECKED.equals(goodsInfo.get(0).getDistributionGoodsAudit())) {
            response.setDistributionGoods(Boolean.FALSE);
        } else {
            response.setDistributionGoods(Boolean.TRUE);
        }
        return response;
    }

    /**
     * SPU商品详情-基础信息（不包括区间价、营销信息）
     *
     * @param skuId 商品skuId
     * @return SPU商品详情
     */
    private GoodsViewByIdResponse goodsDetailBaseInfo(String skuId) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build
                ()).getContext();

        if (Objects.isNull(goodsInfo) || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        GoodsViewByIdRequest request = new GoodsViewByIdRequest();
        request.setGoodsId(goodsInfo.getGoodsId());
        request.setShowLabelFlag(true);
        request.setShowSiteLabelFlag(true);
        GoodsViewByIdResponse response = goodsQueryProvider.getViewById(request).getContext();
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        if (DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(distributionCacheService.queryStoreOpenFlag
                (String.valueOf(goodsInfo.getStoreId()))) || !DistributionGoodsAudit.CHECKED.equals(goodsInfo
                .getDistributionGoodsAudit())) {
            response.setDistributionGoods(Boolean.FALSE);
        } else {
            response.setDistributionGoods(Boolean.TRUE);
        }
        return response;
    }

    /**
     * 店铺精选页进入-商品详情页
     *
     * @param skuId
     * @param skuIds
     * @return
     */
    private GoodsViewByIdAndSkuIdsResponse goodsDetailBaseInfo(String skuId, List<String> skuIds,
                                                               String distributorId) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(skuId).build
                ()).getContext();

        if (Objects.isNull(goodsInfo)
                || Objects.equals(DeleteFlag.YES, goodsInfo.getDelFlag())
                || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus())) || DefaultFlag.NO.equals
                (distributionCacheService.queryStoreOpenFlag(String.valueOf(goodsInfo.getStoreId())))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        if (AddedFlag.NO.toValue() == goodsInfo.getAddedFlag()) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_ADDEDFLAG);
        }

        if (!DistributionGoodsAudit.CHECKED.equals(goodsInfo.getDistributionGoodsAudit())) {
            skuIds = skuIds.stream().filter(goodsInfoId -> !goodsInfoId.equals(skuId)).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(skuIds)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        GoodsViewByIdAndSkuIdsRequest request = new GoodsViewByIdAndSkuIdsRequest();
        request.setGoodsId(goodsInfo.getGoodsId());
        request.setSkuIds(skuIds);
        GoodsViewByIdAndSkuIdsResponse goodsViewByIdAndSkuIdsResponse = goodsQueryProvider.getViewByIdAndSkuIds
                (request).getContext();
        if (CollectionUtils.isEmpty(goodsViewByIdAndSkuIdsResponse.getGoodsInfos())) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        if (StringUtils.isNotBlank(distributorId)) {
            BaseResponse<DistributorLevelByCustomerIdResponse> baseResponse =
                    distributionService.getByCustomerId(distributorId);
            DistributorLevelVO distributorLevelVO = Objects.isNull(baseResponse) ? null :
                    baseResponse.getContext().getDistributorLevelVO();
            goodsViewByIdAndSkuIdsResponse.getGoodsInfos().forEach(goodsInfoVO -> {
                if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate()) && DistributionGoodsAudit.CHECKED == goodsInfoVO.getDistributionGoodsAudit()) {
                    goodsInfoVO.setDistributionCommission(distributionService.calDistributionCommission(goodsInfoVO.getDistributionCommission(), distributorLevelVO.getCommissionRate()));
                }
            });
        }
        return goodsViewByIdAndSkuIdsResponse;
    }


    /**
     * 获取某个商品的小程序码
     *
     * @return
     */
    @ApiOperation(value = "获取某个商品的小程序码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "skuId", value = "skuId", required = true)
    @RequestMapping(value = "/getSkuQrCode", method = RequestMethod.POST)
    public BaseResponse<String> getSkuQrCode(@RequestBody ShareMiniProgramRequest request) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(
                GoodsInfoByIdRequest.builder().goodsInfoId(request.getSkuId()).build()).getContext();
        if (Objects.isNull(goodsInfo)
                || Objects.equals(DeleteFlag.YES, goodsInfo.getDelFlag())
                || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        if (AddedFlag.NO.toValue() == goodsInfo.getAddedFlag()) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_ADDEDFLAG);
        }

        // 新增判断条件，未登录情况下取数据库小程序码的oss地址，如果当前用户已登录，重新生成
        if (Objects.isNull(request.getShareUserId())) {
            if (StringUtils.isNotBlank(goodsInfo.getSmallProgramCode())) {
                return BaseResponse.success(goodsInfo.getSmallProgramCode());
            }
            //没有，重新生成
            MiniProgramQrCodeRequest miniProgramQrCodeRequest = new MiniProgramQrCodeRequest();
            miniProgramQrCodeRequest.setPage("pages/sharepage/sharepage");
            miniProgramQrCodeRequest.setScene(request.getSkuId());
            String codeUrl = wechatAuthProvider.getWxaCodeUnlimit(miniProgramQrCodeRequest).getContext().toString();
            //更新字段
            if (StringUtils.isNotBlank(codeUrl)) {

                GoodsInfoSmallProgramCodeRequest goodsInfoSmallProgramCodeRequest =
                        new GoodsInfoSmallProgramCodeRequest();
                goodsInfoSmallProgramCodeRequest.setGoodsInfoId(request.getSkuId());
                goodsInfoSmallProgramCodeRequest.setCodeUrl(codeUrl);
                BaseResponse response = goodsInfoProvider.updateSkuSmallProgram(goodsInfoSmallProgramCodeRequest);
                if (response.getCode().equals(BaseResponse.SUCCESSFUL().getCode())) {
                    return BaseResponse.success(codeUrl);
                }

            }
            return BaseResponse.success(codeUrl);
        } else {
            request.setShareId(commonUtil.getShareId(request.getShareUserId()));
            return wechatAuthProvider.getMiniProgramQrCodeWithShareUserId(request);
        }

    }


    /**
     * 拼团-进入商品详情
     */
    @ApiOperation(value = "拼团-进入商品详情")
    @RequestMapping(value = "/groupon/goods-detail/spu/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GrouponGoodsViewByIdResponse> grouponGoodsDetailLogin(@PathVariable String skuId) {
        //拼团业务信息
        GrouponDetailQueryRequest grouponDetailQueryRequest = GrouponDetailQueryRequest.builder().
                optType(GrouponDetailOptType.GROUPON_GOODS_DETAIL)
                .leader(Boolean.TRUE).goodsInfoId(skuId).build();

        return grouponGoodsDetail(grouponDetailQueryRequest);
    }


    /**
     * 拼团-进入商品详情-未登录
     */
    @ApiOperation(value = "拼团-进入商品详情")
    @RequestMapping(value = "/unLogin/groupon/goods-detail/spu/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GrouponGoodsViewByIdResponse> grouponGoodsDetailUnLogin(
            @PathVariable String skuId) {
        //拼团业务信息
        GrouponDetailQueryRequest grouponDetailQueryRequest = GrouponDetailQueryRequest.builder().optType
                (GrouponDetailOptType
                        .GROUPON_GOODS_DETAIL)
                .leader(Boolean.TRUE).goodsInfoId(skuId).build();
        return grouponGoodsDetail(grouponDetailQueryRequest);
    }

    /**
     * 拼团-进入拼团详情页-未登录
     */
    @ApiOperation(value = "拼团-进入拼团详情")
    @RequestMapping(value = "/unLogin/groupon/groupon-detail/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<GrouponGoodsViewByIdResponse> grouponDetailByGrouponNoUnLogin(
            @PathVariable String grouponNo) {
        //拼团业务信息
        GrouponDetailQueryRequest grouponDetailWithGoodsRequest = GrouponDetailQueryRequest.builder().optType
                (GrouponDetailOptType.GROUPON_JOIN).grouponNo(grouponNo).build();
        return grouponGoodsDetailByGrouponNo(grouponDetailWithGoodsRequest);
    }

    /**
     * 拼团-进入拼团详情页-登录
     */
    @ApiOperation(value = "拼团-进入拼团详情")
    @RequestMapping(value = "/groupon/groupon-detail/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<GrouponGoodsViewByIdResponse> grouponDetailByGrouponNo(
            @PathVariable String grouponNo) {
        //拼团业务信息
        GrouponDetailQueryRequest grouponDetailWithGoodsRequest = GrouponDetailQueryRequest.builder().optType
                (GrouponDetailOptType.GROUPON_JOIN).grouponNo(grouponNo).build();
        return grouponGoodsDetailByGrouponNo(grouponDetailWithGoodsRequest);
    }

    /**
     * 1.根据商品获取团信息
     * 2.查询商品信息
     * 3.根据拼团活动设置商品信息
     *
     * @param grouponDetailQueryRequest
     * @return
     */
    private BaseResponse<GrouponGoodsViewByIdResponse> grouponGoodsDetail(GrouponDetailQueryRequest
                                                                                  grouponDetailQueryRequest) {
        // 用户信息
        CustomerVO customer = Objects.nonNull(commonUtil.getOperatorId()) ? commonUtil.getCustomer() : null;
        //spu
        GoodsViewByIdResponse response = goodsDetailBaseInfo(grouponDetailQueryRequest.getGoodsInfoId());

        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos().stream()
                .filter(g -> AddedFlag.YES.toValue() == g.getAddedFlag())
                .collect(Collectors.toList());
        // 查询skus信息
        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            response = detailGoodsInfoVOList(response, response.getGoodsInfos(), customer);
        }

        //拼团信息
        grouponDetailQueryRequest.setGoodsId(response.getGoods().getGoodsId());
        grouponDetailQueryRequest.setCustomerId(Objects.isNull(customer) ? null : customer.getCustomerId());
        GrouponDetailQueryResponse grouponDetailQueryResponse = grouponProvider
                .getGrouponDetail(grouponDetailQueryRequest)
                .getContext();
        //sku-spu-skus
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponDetailQueryResponse.getGrouponDetail()
                .getGrouponGoodsInfos();
        List<String> skuIds = grouponGoodsInfoVOList.stream().map(GrouponGoodsInfoVO::getGoodsInfoId).collect
                (Collectors.toList());

        //过滤拼团商品
        goodsInfoVOList = goodsInfoVOList.stream()
                .filter(g -> skuIds.contains(g.getGoodsInfoId()))
                .collect(Collectors.toList());

        IteratorUtils.zip(goodsInfoVOList, response.getGoodsInfos(),
                (a, b) -> a.getGoodsInfoId().equals(b.getGoodsInfoId()),
                (c, d) -> {
                    c.setCouponLabels(d.getCouponLabels());
                }
        );

        response.setGoodsInfos(goodsInfoVOList);
        //商品是否存在
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        //以上为商品信息
        return BaseResponse.success(wrapeGrouponGoodsViewByIdResponse(response, grouponDetailQueryResponse));
    }

    /**
     * 1.根据团编号获取团信息
     * 2.查询商品信息
     * 3.根据拼团活动设置商品信息
     *
     * @param grouponDetailQueryRequest
     * @return
     */
    private BaseResponse<GrouponGoodsViewByIdResponse> grouponGoodsDetailByGrouponNo(GrouponDetailQueryRequest
                                                                                             grouponDetailQueryRequest) {
        // 用户信息
        CustomerVO customer = Objects.nonNull(commonUtil.getOperatorId()) ? commonUtil.getCustomer() : null;
        //拼团信息
        grouponDetailQueryRequest.setCustomerId(Objects.isNull(customer) ? null : customer.getCustomerId());
        GrouponDetailQueryResponse grouponDetailQueryResponse = grouponProvider
                .getGrouponDetail(grouponDetailQueryRequest)
                .getContext();
        //根据活动商品筛选sku
        List<GrouponGoodsInfoVO> grouponGoodsInfoVOList = grouponDetailQueryResponse.getGrouponDetail()
                .getGrouponGoodsInfos();
        List<String> skuIds = grouponGoodsInfoVOList.stream().map(GrouponGoodsInfoVO::getGoodsInfoId).collect
                (Collectors.toList());

        if (CollectionUtils.isEmpty(skuIds)) {
            throw new SbcRuntimeException(WebBaseErrorCode.NOT_EXSIT_GROUP_GOODS);
        }
        //团详情页面根据groupon反查sku
        GoodsViewByIdResponse response = goodsDetailBaseInfo(skuIds.get(0));
        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos().stream()
                .filter(g -> AddedFlag.YES.toValue() == g.getAddedFlag())
                .collect(Collectors.toList());
        // 查询skus信息
        if (CollectionUtils.isNotEmpty(goodsInfoVOList)) {
            long startTime1 = System.currentTimeMillis();
            System.out.println("detailGoodsInfoVOList执行代码块/方法");
            response = detailGoodsInfoVOList(response, goodsInfoVOList, customer);
        }
        //判断活动是否结束
        if (!GrouponDetailOptStatus.ACTIVITY_END.equals(grouponDetailQueryResponse.getGrouponDetail()
                .getGrouponDetailOptStatus())) {
            //过滤分销商品
            goodsInfoVOList = response.getGoodsInfos().stream().filter(g -> !DistributionGoodsAudit.CHECKED.equals(g
                    .getDistributionGoodsAudit())).collect(Collectors.toList());
            //过滤拼团商品
            goodsInfoVOList = goodsInfoVOList.stream()
                    .filter(g -> skuIds.contains(g.getGoodsInfoId())).filter(g -> !DistributionGoodsAudit.CHECKED.equals
                            (g.getDistributionGoodsAudit()))
                    .collect(Collectors.toList());
            response.setGoodsInfos(goodsInfoVOList);

        }
        //商品是否存在
        if (CollectionUtils.isEmpty(goodsInfoVOList)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        //以上为商品信息
        return BaseResponse.success(wrapeGrouponGoodsViewByIdResponse(response, grouponDetailQueryResponse));
    }

    /**
     * 商品信息处理拼团信息
     * 起订限定量
     *
     * @param response
     * @param grouponDetailQueryResponse
     * @return
     */
    private GrouponGoodsViewByIdResponse wrapeGrouponGoodsViewByIdResponse(GoodsViewByIdResponse response,
                                                                           GrouponDetailQueryResponse
                                                                                   grouponDetailQueryResponse) {
        GrouponDetailVO grouponDetail = grouponDetailQueryResponse.getGrouponDetail();
        // 用户信息
        CustomerVO customer = Objects.nonNull(commonUtil.getOperatorId()) ? commonUtil.getCustomer() : null;

        //商品处理拼团信息
        GrouponDetailWithGoodsRequest grouponDetailWithGoodsRequest = new GrouponDetailWithGoodsRequest();
        grouponDetailWithGoodsRequest.setGrouponActivity(grouponDetail
                .getGrouponActivity());
        grouponDetailWithGoodsRequest.setGoodsInfoList(response.getGoodsInfos());
        grouponDetailWithGoodsRequest.setGrouponGoodsInfoVOList(grouponDetail
                .getGrouponGoodsInfos());
        grouponDetailWithGoodsRequest.setCustomerId(Objects.isNull(customer) ? null : customer.getCustomerId());
        GrouponDetailWithGoodsResponse grouponDetailWithGoodsResponse = grouponProvider
                .getGrouponDetailWithGoodsInfos(grouponDetailWithGoodsRequest)
                .getContext();
        //返回拼团信息
        GrouponGoodsViewByIdResponse grouponGoodsViewByIdResponse = KsBeanUtil.convert(response,
                GrouponGoodsViewByIdResponse.class);
        //精简返回页面的数据
        GrouponDetailWithGoodsVO grouponDetailWithGoodsVO = KsBeanUtil.convert(grouponDetail, GrouponDetailWithGoodsVO
                .class);

        //参团商品sku优先显示为团长开团的sku
        if (Objects.nonNull(grouponDetail.getTradeInGroupon())) {
            grouponDetailWithGoodsVO.setGoodInfoId(grouponDetail.getTradeInGroupon().getTradeGroupon().getGoodInfoId());
            grouponDetailWithGoodsVO.setGroupCustomerId(grouponDetail.getTradeInGroupon().getBuyer().getId());
        }
        grouponGoodsViewByIdResponse.setGrouponDetails(grouponDetailWithGoodsVO);
        grouponGoodsViewByIdResponse.setGoodsInfos(grouponDetailWithGoodsResponse.getGoodsInfoVOList());
        grouponGoodsViewByIdResponse.setGrouponInstanceList(grouponDetail.getGrouponInstanceList());


        return grouponGoodsViewByIdResponse;
    }

    /**
     * 填充预售信息
     *
     * @param goodsInfoVOS
     * @return
     */
    private List<GoodsInfoVO> fillActivityInfo(List<GoodsInfoVO> goodsInfoVOS) {
        List<String> goodsInfoIds = goodsInfoVOS.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        List<AppointmentSaleVO> appointmentSaleVOList =
                appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(
                        (AppointmentSaleInProgressRequest.builder().goodsInfoIdList(goodsInfoIds).build())).getContext().getAppointmentSaleVOList();
        if (CollectionUtils.isNotEmpty(appointmentSaleVOList)) {
            Map<String, List<AppointmentSaleVO>> appointmentMap =
                    appointmentSaleVOList.stream().collect(Collectors.groupingBy(a -> a.getAppointmentSaleGood().getGoodsInfoId()));
            goodsInfoVOS.forEach(g -> {
                if (appointmentMap.containsKey(g.getGoodsInfoId())) {
                    BigDecimal appointmentPrice =
                            appointmentMap.get(g.getGoodsInfoId()).get(0).getAppointmentSaleGood().getPrice();
                    g.setAppointmentPrice(appointmentPrice);
                    g.setAppointmentSaleVO(appointmentMap.get(g.getGoodsInfoId()).get(0));
                }
            });
        }
        List<BookingSaleVO> bookingSaleVOList = bookingSaleQueryProvider.inProgressBookingSaleInfoByGoodsInfoIdList
                (BookingSaleInProgressRequest.builder().goodsInfoIdList(goodsInfoIds).build()).getContext().getBookingSaleVOList();
        if (CollectionUtils.isNotEmpty(bookingSaleVOList)) {
            Map<String, List<BookingSaleVO>> bookingMap =
                    bookingSaleVOList.stream().collect(Collectors.groupingBy(a -> a.getBookingSaleGoods().getGoodsInfoId()));
            goodsInfoVOS.forEach(g -> {
                if (bookingMap.containsKey(g.getGoodsInfoId())) {
                    BigDecimal bookingPrice =
                            bookingMap.get(g.getGoodsInfoId()).get(0).getBookingSaleGoods().getBookingPrice();
                    g.setBookingPrice(bookingPrice);
                    g.setBookingSaleVO(bookingMap.get(g.getGoodsInfoId()).get(0));
                }
            });
        }
        return goodsInfoVOS;
    }


    /**
     * 设置限售数据
     *
     * @param goodsInfoVOS
     * @param customerVO
     * @return
     */
    private List<GoodsInfoVO> setRestrictedNum(List<GoodsInfoVO> goodsInfoVOS, CustomerVO customerVO) {
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(goodsInfoVOS)) {
            List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS = new ArrayList<>();
            goodsInfoVOS.stream().forEach(g -> {
                GoodsRestrictedValidateVO rvv = new GoodsRestrictedValidateVO();
                rvv.setNum(g.getBuyCount());
                rvv.setSkuId(g.getGoodsInfoId());
                goodsRestrictedValidateVOS.add(rvv);
            });
            GoodsRestrictedSalePurchaseResponse response = goodsRestrictedSaleQueryProvider.validatePurchaseRestricted(
                    GoodsRestrictedBatchValidateRequest.builder()
                            .goodsRestrictedValidateVOS(goodsRestrictedValidateVOS)
                            .customerVO(customerVO)
                            .build()).getContext();
            if (Objects.nonNull(response) && org.apache.commons.collections4.CollectionUtils.isNotEmpty(response.getGoodsRestrictedPurchaseVOS())) {
                List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = response.getGoodsRestrictedPurchaseVOS();
                Map<String, GoodsRestrictedPurchaseVO> purchaseMap =
                        goodsRestrictedPurchaseVOS.stream().collect((Collectors.toMap(GoodsRestrictedPurchaseVO::getGoodsInfoId, g -> g)));
                goodsInfoVOS.stream().forEach(g -> {
                    GoodsRestrictedPurchaseVO goodsRestrictedPurchaseVO = purchaseMap.get(g.getGoodsInfoId());
                    if (Objects.nonNull(goodsRestrictedPurchaseVO)) {
                        if (DefaultFlag.YES.equals(goodsRestrictedPurchaseVO.getDefaultFlag())) {
                            g.setMaxCount(goodsRestrictedPurchaseVO.getRestrictedNum());
                            g.setCount(goodsRestrictedPurchaseVO.getStartSaleNum());
                        } else {
                            g.setGoodsStatus(GoodsStatus.INVALID);
                        }
                    }
                });
            }
            return goodsInfoVOS;
        }
        return goodsInfoVOS;
    }


    /**
     * 查询这个sku是否为正在进行中拼团
     *
     * @return
     */
    @ApiOperation(value = "查询这个sku是否为正在进行中拼团")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsInfoId", value = "goodsInfoId",
            required = true)
    @RequestMapping(value = "/groupOn/sku/{goodsInfoId}", method = RequestMethod.GET)
    public BaseResponse<GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse> checkGroupOnFlag(@PathVariable String goodsInfoId) {

        GrouponGoodsInfoVO grouponGoodsInfo = grouponActivityQueryProvider.listActivityingByGoodsInfoIds(
                new GrouponActivityingByGoodsInfoIdsRequest(Arrays.asList(goodsInfoId))).getContext().getGrouponGoodsInfoMap().get(goodsInfoId);

        return BaseResponse.success(new GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse(grouponGoodsInfo));
    }
}
