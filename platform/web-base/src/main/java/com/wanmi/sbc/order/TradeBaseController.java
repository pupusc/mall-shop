package com.wanmi.sbc.order;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.request.PaymentRecordRequest;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.annotation.MultiSubmitWithToken;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.constants.WebBaseErrorCode;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerListForOrderCommitRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdAndStoreIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaListByConditionRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelByCustomerIdAndStoreIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.CommissionUnhookType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerSimVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.distribute.DistributionService;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleIsInProgressRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuySendDateRuleRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoVerifyRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleInProcessResponse;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleIsInProgressResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionCacheQueryProvider;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordListRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityFreeDeliveryByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingViewQueryByIdsRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsValidRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelBySkuRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.response.distribution.MultistageSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordListResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsValidResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import com.wanmi.sbc.marketing.bean.enums.GiftType;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.enums.RecruitApplyType;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingBuyoutPriceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingHalfPriceSecondPieceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingPointBuyLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.order.api.provider.groupon.GrouponProvider;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.GrouponInstanceQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeItemQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.api.request.groupon.GrouponOrderValidRequest;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderListRequest;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetGoodsMarketingRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnCountByConditionRequest;
import com.wanmi.sbc.order.api.request.trade.FindProviderTradeRequest;
import com.wanmi.sbc.order.api.request.trade.GrouponInstanceByGrouponNoRequest;
import com.wanmi.sbc.order.api.request.trade.LatestDeliverDateRequest;
import com.wanmi.sbc.order.api.request.trade.MergeGoodsInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradeAddReceivableRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCancelRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeConfirmReceiveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCountCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemByCustomerIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemConfirmSettlementRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotCycleBuyGiftRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotGiftRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotMarkupRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListByParentIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageQueryRequest;
import com.wanmi.sbc.order.api.request.trade.TradeParamsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeQueryPurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyCycleBuyRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyStoreRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyTradeMarketingRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordResponse;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderListResponse;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.trade.FindProviderTradeResponse;
import com.wanmi.sbc.order.api.response.trade.TradeDeliverRecordResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetFreightResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.api.response.trade.VerifyGoodsResponse;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.CycleBuySendDateRuleDTO;
import com.wanmi.sbc.order.bean.dto.ReceivableAddDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.AuditState;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.IsAccountStatus;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.vo.CycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import com.wanmi.sbc.order.bean.vo.GrouponTradeVO;
import com.wanmi.sbc.order.bean.vo.InvoiceVO;
import com.wanmi.sbc.order.bean.vo.ShippingItemVO;
import com.wanmi.sbc.order.bean.vo.TradeCommissionVO;
import com.wanmi.sbc.order.bean.vo.TradeCommitResultVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeCycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeDeliverVO;
import com.wanmi.sbc.order.bean.vo.TradeDistributeItemCommissionVO;
import com.wanmi.sbc.order.bean.vo.TradeDistributeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeGrouponCommitFormVO;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.TradeItemMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import com.wanmi.sbc.order.bean.vo.TradeStateVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.mapper.CustmerMapper;
import com.wanmi.sbc.order.mapper.GoodsInfoMapper;
import com.wanmi.sbc.order.mapper.TradeGoodsInfoPageMapper;
import com.wanmi.sbc.order.mapper.TradeItemMapper;
import com.wanmi.sbc.order.request.GrouponBuyRequest;
import com.wanmi.sbc.order.request.ImmediateBuyRequest;
import com.wanmi.sbc.order.request.StoreBagsBuyRequest;
import com.wanmi.sbc.order.request.SuitBuyRequest;
import com.wanmi.sbc.order.request.TradeItemConfirmModifyGoodsNumRequest;
import com.wanmi.sbc.order.request.TradeItemConfirmRequest;
import com.wanmi.sbc.order.request.TradeItemRequest;
import com.wanmi.sbc.order.response.DeliverCalendarResponse;
import com.wanmi.sbc.order.response.OrderTodoResp;
import com.wanmi.sbc.order.response.TradeConfirmResponse;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.DeliveryQueryProvider;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressVerifyRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @menu 订单公共接口类
 * @tag feature_d_cps3
 * @status undone
 */
@Api(tags = "TradeBaseController", description = "订单公共服务API")
@RestController
@RequestMapping("/trade")
@Slf4j
@Validated
public class TradeBaseController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private TradeItemProvider tradeItemProvider;

    @Autowired
    private TradeItemQueryProvider tradeItemQueryProvider;

    @Resource
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private MarketingPluginQueryProvider marketingPluginQueryProvider;

    @Resource
    private DeliveryQueryProvider deliveryQueryProvider;

    @Resource
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Resource
    private AuditQueryProvider auditQueryProvider;

    @Resource
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Resource
    private CouponCodeQueryProvider couponCodeQueryProvider;

    @Resource
    private DistributorGoodsInfoQueryProvider distributorGoodsInfoQueryProvider;

    @Resource
    private DistributionCacheService distributionCacheService;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DistributionRecordQueryProvider distributionRecordQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Autowired
    private GrouponProvider grouponProvider;

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private GrouponInstanceQueryProvider grouponInstanceQueryProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;

    @Autowired
    private AppointmentRecordQueryProvider appointmentRecordQueryProvider;
    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private GoodsTobeEvaluateQueryProvider goodsTobeEvaluateQueryProvider;

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;
    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private TradeItemMapper tradeItemMapper;

    @Autowired
    private TradeGoodsInfoPageMapper tradeGoodsInfoPageMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private CustmerMapper custmerMapper;

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private DistributionCacheQueryProvider distributionCacheQueryProvider;

    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private ExternalProvider externalProvider;

    /**
     * @description 商城配合知识顾问
     * @menu 查询订单详情
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查询订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> details(@PathVariable String tid) {
        TradeGetByIdResponse tradeGetByIdResponse =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).needLmOrder(Boolean.TRUE).build()).getContext();
        if (tradeGetByIdResponse == null
                || tradeGetByIdResponse.getTradeVO() == null) {
            return BaseResponse.success(null);
        }
        TradeVO detail = tradeGetByIdResponse.getTradeVO();

        //周期购订单 重新设置商品数量 购买的数量/期数
        if (detail.getCycleBuyFlag()) {
            detail.getTradeItems().forEach(tradeItemVO -> {
                tradeItemVO.setNum(tradeItemVO.getNum() / tradeItemVO.getCycleNum());
            });
        }

        checkUnauthorized(tid, tradeGetByIdResponse.getTradeVO());
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        final boolean flag = config.getStatus() == 1;
        int days = JSONObject.parseObject(config.getContext()).getInteger("day");
        TradeStateVO tradeState = detail.getTradeState();
        boolean canReturnFlag =
                tradeState.getFlowState() == FlowState.COMPLETED || (tradeState.getPayState() == PayState.PAID
                        && tradeState.getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED && tradeState.getFlowState
                        () != FlowState.VOID);
        canReturnFlag = isCanReturnTime(flag, days, tradeState, canReturnFlag);
        //开店礼包不支持退货退款
        canReturnFlag = canReturnFlag && DefaultFlag.NO == detail.getStoreBagsFlag();
        detail.setCanReturnFlag(canReturnFlag);
        if (Objects.nonNull(detail.getIsBookingSaleGoods()) && detail.getBookingType() == BookingType.EARNEST_MONEY) {
            BigDecimal price = detail.getTradePrice().getTailPrice().subtract(detail.getTradePrice().getDeliveryPrice());
            CouponCodeListForUseByCustomerIdRequest requ = CouponCodeListForUseByCustomerIdRequest.builder()
                    .customerId(commonUtil.getOperatorId())
                    .tradeItems(tradeItemMapper.tradeItemVOsToTradeItemInfoDTOs(detail.getTradeItems())).price(price).build();
            detail.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(requ).getContext()
                    .getCouponCodeList());
        }

        //未完全支付的定金预售订单改变会作废
        this.fillTradeBookingTimeOut(detail);
        if (PayState.NOT_PAID == detail.getTradeState().getPayState()
                || PayState.PAID_EARNEST == detail.getTradeState().getPayState() ) {
            detail.getTradeItems().stream()
                    .forEach(t -> t.setVirtualCoupons(Collections.EMPTY_LIST));
            detail.getGifts().stream()
                    .forEach(t -> t.setVirtualCoupons(Collections.EMPTY_LIST));
        }
        return BaseResponse.success(detail);
    }


    /**
     * B店主客户订单详情
     */
    @ApiOperation(value = "B店主客户订单详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/distribute/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeVO> distributeDetails(@PathVariable String tid) {
        TradeGetByIdResponse tradeGetByIdResponse =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext();
        TradeVO detail = tradeGetByIdResponse.getTradeVO();
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        final boolean flag = config.getStatus() == 1;
        int days = JSONObject.parseObject(config.getContext()).getInteger("day");
        TradeStateVO tradeState = detail.getTradeState();
        boolean canReturnFlag =
                tradeState.getFlowState() == FlowState.COMPLETED || (tradeState.getPayState() == PayState.PAID
                        && tradeState.getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED && tradeState.getFlowState
                        () != FlowState.VOID);
        canReturnFlag = isCanReturnTime(flag, days, tradeState, canReturnFlag);
        //开店礼包不支持退货退款
        canReturnFlag = canReturnFlag && DefaultFlag.NO == detail.getStoreBagsFlag();
        detail.setCanReturnFlag(canReturnFlag);
        detail.getConsignee().setPhone(detail.getConsignee().getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})",
                "$1****$2"));
        detail.getConsignee().setDetailAddress(detail.getConsignee().getDetailAddress().replace(detail.getConsignee()
                .getAddress(), "********"));

        //查询商品的入账状态
        DistributionRecordListRequest distributionRecordListRequest = DistributionRecordListRequest
                .builder()
                .tradeId(detail.getId())
                .build();
        BaseResponse<DistributionRecordListResponse> response = distributionRecordQueryProvider.list
                (distributionRecordListRequest);
        if (response != null && response.getContext() != null && CollectionUtils.isNotEmpty(response.getContext()
                .getDistributionRecordVOList())) {
            List<DistributionRecordVO> distributionRecordVOList = response.getContext().getDistributionRecordVOList();
            detail.getTradeItems().stream().forEach(tradeItemVO -> {
                distributionRecordVOList.stream()
                        .filter(distributionRecordVO -> distributionRecordVO.getGoodsInfoId().equals(tradeItemVO
                                .getSkuId()))
                        .forEach(distributionRecordVO -> {
                            if (distributionRecordVO.getDeleteFlag().equals(DeleteFlag.YES)) {
                                tradeItemVO.setIsAccountStatus(IsAccountStatus.FAIL);
                            } else if (distributionRecordVO.getCommissionState().equals(CommissionReceived.RECEIVED)) {
                                tradeItemVO.setIsAccountStatus(IsAccountStatus.YES);
                            } else {
                                tradeItemVO.setIsAccountStatus(IsAccountStatus.NO);
                            }
                        });
            });
        }


        //处理所有的分销奖励
//        detail.getTradeItems().forEach(tradeItemVO -> {
//            tradeItemVO.setDistributionCommission(tradeItemVO.getDistributionCommission().multiply(new BigDecimal
// (tradeItemVO.getNum())));
//        });
        return BaseResponse.success(detail);
    }

    /**
     * 校验商品是否可以立即购买
     */
    @ApiOperation(value = "校验商品是否可以立即购买")
    @RequestMapping(value = "/checkGoods", method = RequestMethod.PUT)
    public BaseResponse checkGoods(@RequestBody @Valid TradeItemConfirmRequest confirmRequest) {
        String customerId = commonUtil.getOperatorId();
        List<TradeItemDTO> tradeItems = confirmRequest.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum())
                        .isAppointmentSaleGoods(o.getIsAppointmentSaleGoods()).appointmentSaleId(o.getAppointmentSaleId())
                        .isBookingSaleGoods(o.getIsBookingSaleGoods()).bookingSaleId(o.getBookingSaleId())
                        .build()
        ).collect(Collectors.toList());
        List<String> skuIds =
                confirmRequest.getTradeItems().stream().map(TradeItemRequest::getSkuId).collect(Collectors.toList());
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        //商品验证
        verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Lists.newArrayList(),
                tradeGoodsInfoPageMapper.goodsInfoResponseToTradeGoodsInfoPageDTO(response), null, false));
        verifyQueryProvider.verifyStore(new VerifyStoreRequest(response.getGoodsInfos().stream().map
                (GoodsInfoVO::getStoreId).collect(Collectors.toList())));

        Map<String, GoodsInfoVO> goodsInfoVOMap = response.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItems.stream().forEach(tradeItemDTO -> {
            tradeItemDTO.setBuyPoint(goodsInfoVOMap.get(tradeItemDTO.getSkuId()).getBuyPoint());
        });
        // 校验商品限售信息
        TradeItemGroupVO tradeItemGroupVOS = new TradeItemGroupVO();
        tradeItemGroupVOS.setTradeItems(tradeItemMapper.tradeItemDTOsToTradeItemVos(tradeItems));
        this.validateRestrictedGoods(tradeItemGroupVOS, customer);
        IteratorUtils.zip(response.getGoodsInfos(), tradeItemGroupVOS.getTradeItems(),
                (a, b) ->
                        a.getGoodsInfoId().equals(b.getSkuId())
                ,
                (c, d) -> {
                    d.setBuyPoint(c.getBuyPoint());
                });
        // 预约活动校验是否有资格
        this.validateAppointmentQualification(Collections.singletonList(tradeItemGroupVOS));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 从采购单中确认订单商品
     */
    @ApiOperation(value = "从采购单中确认订单商品")
    @RequestMapping(value = "/confirm", method = RequestMethod.PUT)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse confirm(@RequestBody @Valid TradeItemConfirmRequest confirmRequest) {

        String customerId = commonUtil.getOperatorId();

        List<String> skuIds =
                confirmRequest.getTradeItems().stream().map(TradeItemRequest::getSkuId).collect(Collectors.toList());

        List<TradeItemDTO> tradeItems = confirmRequest.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum())
                        .isAppointmentSaleGoods(o.getIsAppointmentSaleGoods()).appointmentSaleId(o.getAppointmentSaleId())
                        .isBookingSaleGoods(o.getIsBookingSaleGoods()).bookingSaleId(o.getBookingSaleId())
                        .build()
        ).collect(Collectors.toList());

        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        DistributeChannel distributeChannel = commonUtil.getDistributeChannel();
        ChannelType channelType = distributeChannel.getChannelType();
        TradeItemConfirmSettlementRequest request = new TradeItemConfirmSettlementRequest();
        request.setCustomerId(customerId);
        request.setSkuIds(skuIds);
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        request.setOpenFlag(openFlag);
        request.setChannelType(channelType);
        request.setDistributeChannel(distributeChannel);
        request.setTradeItems(tradeItems);
        request.setTradeMarketingList(confirmRequest.getTradeMarketingList());
        request.setForceConfirm(confirmRequest.isForceConfirm());
        request.setTerminalToken(commonUtil.getTerminalToken());
        request.setAreaId(confirmRequest.getAreaId());
        return tradeItemProvider.confirmSettlement(request);
        //验证采购单

//        PurchaseQueryRequest purchaseQueryRequest = new PurchaseQueryRequest();
//        purchaseQueryRequest.setCustomerId(customerId);
//        purchaseQueryRequest.setGoodsInfoIds(skuIds);
//        purchaseQueryRequest.setInviteeId(commonUtil.getPurchaseInviteeId());
//        PurchaseQueryResponse purchaseQueryResponse = purchaseQueryProvider.query(purchaseQueryRequest).getContext();
//        List<PurchaseVO> exsistSku = purchaseQueryResponse.getPurchaseList();
//
//        if (CollectionUtils.isEmpty(exsistSku)) {
//            throw new SbcRuntimeException("K-050205");
//        }
//        List<String> existIds = exsistSku.stream().map(PurchaseVO::getGoodsInfoId).collect(Collectors.toList());
//        if (skuIds.stream().anyMatch(skuId -> !existIds.contains(skuId))) {
//            throw new SbcRuntimeException("K-050205");
//        }
//        //验证用户
//        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
//                (customerId)).getContext();
//        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
//        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfos();
//        //根据开关重新设置分销商品标识
//        distributionService.checkDistributionSwitch(goodsInfoVOList);
//        //社交分销业务
//        Purchase4DistributionRequest purchase4DistributionRequest = new Purchase4DistributionRequest();
//        purchase4DistributionRequest.setGoodsInfos(response.getGoodsInfos());
//        purchase4DistributionRequest.setGoodsIntervalPrices(response.getGoodsIntervalPrices());
//        purchase4DistributionRequest.setCustomer(customer);
//        purchase4DistributionRequest.setDistributeChannel(commonUtil.getDistributeChannel());
//        Purchase4DistributionResponse purchase4DistributionResponse = purchaseQueryProvider.distribution
//                (purchase4DistributionRequest).getContext();
//        response.setGoodsInfos(purchase4DistributionResponse.getGoodsInfos());
//        response.setGoodsIntervalPrices(purchase4DistributionResponse.getGoodsIntervalPrices());
//        //验证分销商品状态
//        validShopGoods(purchase4DistributionResponse.getGoodsInfos());
//
//        //商品验证
//        verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
//                KsBeanUtil.convert(response, TradeGoodsInfoPageDTO.class), null, false));
//        verifyQueryProvider.verifyStore(new VerifyStoreRequest(response.getGoodsInfos().stream().map
//                (GoodsInfoVO::getStoreId).collect(Collectors.toList())));
//        //营销活动校验
//        List<TradeMarketingDTO> tradeMarketingList =
//                verifyQueryProvider.verifyTradeMarketing(new VerifyTradeMarketingRequest(confirmRequest.getTradeMarketingList
//                        (), Collections.emptyList(), tradeItems, customerId, confirmRequest.isForceConfirm())).getContext().getTradeMarketingList();
//        // 校验商品限售信息
//        TradeItemGroupVO tradeItemGroupVOS = new TradeItemGroupVO();
//        tradeItemGroupVOS.setTradeItems(KsBeanUtil.convert(tradeItems, TradeItemVO.class));
//        this.validateRestrictedGoods(tradeItemGroupVOS, commonUtil.getCustomer());
//        // 预约活动校验是否有资格
//        this.validateAppointmentQualification(Collections.singletonList(tradeItemGroupVOS));
//
//        //校验预售活动资格，初始化价格
//        List<TradeItemDTO> tradeItemDTOs = fillActivityPrice(tradeItems, goodsInfoVOList);
//
//        return tradeItemProvider.snapshot(TradeItemSnapshotRequest.builder().customerId(customerId).pointGoodsFlag(true)
//                .tradeItems(tradeItemDTOs)
//                .tradeMarketingList(tradeMarketingList)
//                .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
//                .terminalToken(commonUtil.getTerminalToken())
//                .build());
    }


    /**
     * 校验活动初始化价格
     *
     * @param tradeItems
     * @return
     */
    private List<TradeItemDTO> fillActivityPrice(List<TradeItemDTO> tradeItems, List<GoodsInfoVO> goodsInfoVOList, CustomerVO customer, StoreVO storeVO) {
        Map<String, BigDecimal> skuMap =
                goodsInfoVOList.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId,
                        GoodsInfoVO::getMarketPrice));
        return tradeItems.stream().map(item -> {
            if (item.getIsAppointmentSaleGoods()) {
                AppointmentSaleVO appointmentSaleVO =
                        appointmentSaleQueryProvider.getAppointmentSaleRelaInfo(RushToAppointmentSaleGoodsRequest.builder().appointmentSaleId(item.getAppointmentSaleId()).
                                skuId(item.getSkuId()).build()).getContext().getAppointmentSaleVO();
                if (Objects.isNull(appointmentSaleVO)) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (appointmentSaleVO.getSnapUpEndTime().isAfter(LocalDateTime.now()) && appointmentSaleVO.getSnapUpStartTime().isBefore(LocalDateTime.now())) {
                    item.setPrice(Objects.isNull(appointmentSaleVO.getAppointmentSaleGood().getPrice()) ?
                            appointmentSaleVO.getAppointmentSaleGood().getGoodsInfoVO().getMarketPrice()
                            : appointmentSaleVO.getAppointmentSaleGood().getPrice());
                }
                return item;
            }
            if (item.getIsBookingSaleGoods()) {
                BookingSaleIsInProgressResponse bookingResponse =
                        bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest.builder().goodsInfoId(item.getSkuId()).build()).getContext();
                if (Objects.isNull(bookingResponse) || Objects.isNull(bookingResponse.getBookingSaleVO())) {
                    throw new SbcRuntimeException("K-600010");
                }
                BookingSaleVO bookingSaleVO = bookingResponse.getBookingSaleVO();
                if (!bookingSaleVO.getId().equals(item.getBookingSaleId())) {
                    throw new SbcRuntimeException("K-000009");
                }
                if (bookingSaleVO.getPauseFlag() == 1) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
                    if (bookingSaleVO.getHandSelEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getHandSelStartTime().isAfter(LocalDateTime.now())) {
                        throw new SbcRuntimeException("K-170003");
                    }
                    item.setPrice(skuMap.get(bookingSaleVO.getBookingSaleGoods().getGoodsInfoId()));
                    item.setBookingType(BookingType.EARNEST_MONEY);
                    BigDecimal handSelPrice = bookingSaleVO.getBookingSaleGoods().getHandSelPrice();
                    BigDecimal inflationPrice = bookingSaleVO.getBookingSaleGoods().getInflationPrice();
                    item.setEarnestPrice(handSelPrice.multiply(BigDecimal.valueOf(item.getNum())));
                    if (Objects.nonNull(inflationPrice)) {
                        item.setSwellPrice(inflationPrice.multiply(BigDecimal.valueOf(item.getNum())));
                    } else {
                        item.setSwellPrice(item.getEarnestPrice());
                    }
                    item.setHandSelStartTime(bookingSaleVO.getHandSelStartTime());
                    item.setHandSelEndTime(bookingSaleVO.getHandSelEndTime());
                    item.setTailStartTime(bookingSaleVO.getTailStartTime());
                    item.setTailEndTime(bookingSaleVO.getTailEndTime());
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
                    if (bookingSaleVO.getBookingEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getBookingStartTime().isAfter(LocalDateTime.now())) {
                        throw new SbcRuntimeException("K-170003");
                    }
                    item.setBookingType(BookingType.FULL_MONEY);
                    item.setPrice(Objects.isNull(bookingSaleVO.getBookingSaleGoods().getBookingPrice()) ?
                            skuMap.get(bookingSaleVO.getBookingSaleGoods().getGoodsInfoId())
                            : bookingSaleVO.getBookingSaleGoods().getBookingPrice());
                }

                if (bookingSaleVO.getJoinLevel().equals("-3")) {
                    //企业会员
                    CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
                    customerGetByIdRequest.setCustomerId(customer.getCustomerId());
                    CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
                    if (!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                        throw new SbcRuntimeException("K-170009");
                    }
                } else if (bookingSaleVO.getJoinLevel().equals("-2")) {
                    //付费会员
                    List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                            .list(PaidCardCustomerRelListRequest.builder()
                                    .delFlag(DeleteFlag.NO)
                                    .endTimeBegin(LocalDateTime.now())
                                    .customerId(customer.getCustomerId()).build())
                            .getContext().getPaidCardCustomerRelVOList();
                    if (CollectionUtils.isEmpty(relVOList)) {
                        throw new SbcRuntimeException("K-170009");
                    }
                } else if (!bookingSaleVO.getJoinLevel().equals("-1")) {
                    //店铺内客户
                    //第三方商家
                    if (BoolFlag.YES.equals(storeVO.getCompanyType())) {
                        StoreCustomerRelaListByConditionRequest listByConditionRequest = new StoreCustomerRelaListByConditionRequest();
                        listByConditionRequest.setCustomerId(commonUtil.getOperatorId());
                        listByConditionRequest.setStoreId(bookingSaleVO.getStoreId());
                        List<StoreCustomerRelaVO> relaVOList =
                                storeCustomerQueryProvider.listByCondition(listByConditionRequest).getContext().getRelaVOList();
                        if (Objects.nonNull(relaVOList) && relaVOList.size() > 0) {
                            if (!bookingSaleVO.getJoinLevel().equals("0") && !Arrays.asList(bookingSaleVO.getJoinLevel().split(",")).contains(relaVOList.get(0).getStoreLevelId().toString())) {
                                throw new SbcRuntimeException("K-160003");
                            }
                        } else {
                            throw new SbcRuntimeException("K-160004");
                        }
                    } else {
                        if (!bookingSaleVO.getJoinLevel().equals("0") && !Arrays.asList(bookingSaleVO.getJoinLevel().split(",")).contains(Objects.toString(customer.getCustomerLevelId()))) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    }
                }
                return item;
            }
            return item;
        }).collect(Collectors.toList());
    }


    /**
     * 订单满赠订单快照刷新
     */
    @ApiOperation(value = "订单满赠订单快照刷新")
    @RequestMapping(value = "/full-gift/confirm", method = RequestMethod.PUT)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse fullGiftConfirm(@RequestBody @Valid TradeItemSnapshotGiftRequest request) {
        String customerId = commonUtil.getOperatorId();
        List<TradeItemDTO> tradeItems = request.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum()).storeId(o.getStoreId()).build()
        ).collect(Collectors.toList());
        TradeMarketingDTO tradeMarketingDTO = null;
        if(Objects.nonNull(request.getTradeMarketingDTO())){
            //营销活动校验
            List<TradeMarketingDTO> tradeMarketingList = verifyQueryProvider.verifyTradeMarketing(
                    new VerifyTradeMarketingRequest(Collections.singletonList(request.getTradeMarketingDTO()),
                            Collections.emptyList(), tradeItems, customerId, false)).getContext().getTradeMarketingList();
            tradeMarketingDTO = tradeMarketingList.get(0);
        }
        return tradeItemProvider.snapshotGift(TradeItemSnapshotGiftRequest.builder()
                .terminalToken(commonUtil.getTerminalToken())
                .tradeItems(tradeItems)
                .tradeMarketingDTO(tradeMarketingDTO).build());
    }

    /**
     * 订单快照换购刷新
     */
    @ApiOperation(value = "订单快照换购刷新")
    @RequestMapping(value = "/markup/confirm", method = RequestMethod.PUT)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse markupConfirm(@RequestBody @Valid TradeItemSnapshotMarkupRequest request) {
        String customerId = commonUtil.getOperatorId();
        List<TradeItemDTO> tradeItems = request.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum()).storeId(o.getStoreId()).build()
        ).collect(Collectors.toList());
        //营销活动校验
        List<TradeMarketingDTO> tradeMarketingList = verifyQueryProvider.verifyTradeMarketing(
                new VerifyTradeMarketingRequest(request.getTradeMarketingDTO(),
                        Collections.emptyList(), tradeItems, customerId, false)).getContext().getTradeMarketingList();
        return tradeItemProvider.snapshotMarkup(TradeItemSnapshotMarkupRequest.builder()
                .terminalToken(commonUtil.getTerminalToken())
                .tradeItems(tradeItems)
                .tradeMarketingDTO(tradeMarketingList).build());
    }

    private void validShopGoods(List<GoodsInfoVO> goodsInfoVOS) {
        goodsInfoVOS.stream().forEach(goodsInfo -> {
            if (goodsInfo.getGoodsStatus() == GoodsStatus.INVALID) {
                throw new SbcRuntimeException("K-050117");
            }
        });
    }

    /**
     * 开店礼包购买
     */
    @ApiOperation(value = "开店礼包购买")
    @RequestMapping(value = "/store-bags-buy", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse storeBagsBuy(@RequestBody @Valid StoreBagsBuyRequest request) {
        CustomerVO customer = commonUtil.getCustomer();

        List<TradeItemDTO> tradeItems = new ArrayList<>();
        tradeItems.add(TradeItemDTO.builder()
                .skuId(request.getGoodsInfoId())
                .num(1L).build());

        // 1.获取商品信息
        List<String> skuIds = Arrays.asList(request.getGoodsInfoId());
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        GoodsInfoVO goodsInfo = response.getGoodsInfos().get(0);
        //普通商品
        goodsInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
        //开店礼包不限制起订量、限定量
        goodsInfo.setCount(null);
        goodsInfo.setMaxCount(null);
        goodsInfo.setBuyPoint(NumberUtils.LONG_ZERO);
        //商品验证
        BaseResponse<VerifyGoodsResponse> verifyGoods = verifyQueryProvider.verifyGoods(
                new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
                        KsBeanUtil.convert(response, TradeGoodsInfoPageDTO.class),
                        goodsInfo.getStoreId(), true));

        // 2.填充商品信息
        tradeItems = KsBeanUtil.convertList(verifyGoods.getContext().getTradeItems(), TradeItemDTO.class);
        tradeItems.get(0).setPrice(goodsInfo.getMarketPrice());

        // 3.设置订单快照
        return tradeItemProvider.snapshot(
                TradeItemSnapshotRequest.builder()
                        .customerId(customer.getCustomerId())
                        .storeBagsFlag(DefaultFlag.YES)
                        .tradeItems(tradeItems)
                        .tradeMarketingList(new ArrayList<>())
                        .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                        .terminalToken(commonUtil.getTerminalToken())
                        .purchaseBuy(Boolean.FALSE)
                        .build());
    }

    /**
     * 组合购购买
     */
    @ApiOperation(value = "组合购购买")
    @RequestMapping(value = "/suit-buy", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse suitBuy(@RequestBody @Valid SuitBuyRequest request) {
        MarketingSuitsValidRequest marketingRequest = new MarketingSuitsValidRequest();
        marketingRequest.setMarketingId(request.getMarketingId());
        marketingRequest.setBaseStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        marketingRequest.setUserId(commonUtil.getOperatorId());
        // 验证并获取活动信息
        MarketingSuitsValidResponse marketingRespons =
                marketingSuitsQueryProvider.validSuitOrderBeforeCommit(marketingRequest).getContext();
        // 订单快照
        List<TradeItemDTO> tradeItems = new ArrayList<>();
        marketingRespons.getMarketingSuitsSkuVOList().forEach(marketingSuitsSkuVO -> {
            tradeItems.add(TradeItemDTO.builder()
                    .skuId(marketingSuitsSkuVO.getSkuId())
                    .num(marketingSuitsSkuVO.getNum()).build());
        });
        CustomerVO customer = commonUtil.getCustomer();
        List<String> skuIds =
                marketingRespons.getMarketingSuitsSkuVOList().stream().map(marketingSuitsSkuVO -> marketingSuitsSkuVO.getSkuId()).collect(Collectors.toList());
        // 营销活动信息
        List<TradeMarketingDTO> tradeMarketingList = new ArrayList<>();
        tradeMarketingList.add(TradeMarketingDTO.builder().marketingId(request.getMarketingId()).skuIds(skuIds).giftSkuIds(new ArrayList<>()).build());
        TradeGetGoodsResponse response =
                tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(skuIds).build()).getContext();
        // 填充、生成快照
        return tradeItemProvider.snapshot(
                TradeItemSnapshotRequest.builder()
                        .customerId(customer.getCustomerId())
                        .terminalToken(commonUtil.getTerminalToken())
                        .tradeItems(tradeItems)
                        .tradeMarketingList(tradeMarketingList)
                        .suitMarketingFlag(Boolean.TRUE)
                        .suitScene(marketingRespons.getMarketingSuitsVO().getSuitScene())
                        .purchaseBuy(Boolean.FALSE)
                        .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class)).build());

    }


    /**
     * 拼团购买
     */
    @ApiOperation(value = "拼团购买")
    @RequestMapping(value = "/groupon-buy", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse grouponBuy(@RequestBody @Valid GrouponBuyRequest request) {

        List<TradeItemDTO> tradeItems = new ArrayList<>();
        tradeItems.add(TradeItemDTO.builder()
                .skuId(request.getGoodsInfoId())
                .num(request.getBuyCount()).build());

        CustomerVO customer = commonUtil.getCustomer();
        List<String> skuIds = Arrays.asList(request.getGoodsInfoId());
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);

        //周期购商品
        GoodsInfoVO goodsInfoVO = response.getGoodsInfos().get(0);
        CycleBuyInfoDTO cycleBuyInfoDTO = this.fillCycleBuyInfoToSnapshot(goodsInfoVO, request.getDeliveryCycle(),
                request.getSendDateRule(), request.getDeliveryPlan());

        // 填充、生成快照
        return tradeItemProvider.snapshot(
                TradeItemSnapshotRequest.builder()
                        .customerId(customer.getCustomerId())
                        .openGroupon(request.getOpenGroupon())
                        .grouponNo(request.getGrouponNo())
                        .tradeItems(tradeItems)
                        .tradeMarketingList(new ArrayList<>())
                        .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                        .terminalToken(commonUtil.getTerminalToken())
                        .purchaseBuy(Boolean.FALSE)
                        .cycleBuyInfoDTO(cycleBuyInfoDTO)
                        .build());

    }

    /**
     * 立即购买
     */
    @ApiOperation(value = "立即购买")
    @RequestMapping(value = "/immediate-buy", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse immediateBuy(@RequestBody @Valid ImmediateBuyRequest confirmRequest) throws ExecutionException, InterruptedException {
        CustomerVO customer = commonUtil.getCustomer();
        //校验商品的限购信息
        this.validateRestrictedGoodsForReserveLisr(confirmRequest.getTradeItemRequests(), customer);

        List<TradeItemDTO> tradeItems = tradeItemMapper.tradeItemRequestsToTradeItemDTOs(confirmRequest.getTradeItemRequests());

        TradeItemGroupVO tradeItemGroupVOS = new TradeItemGroupVO();
        tradeItemGroupVOS.setTradeItems(tradeItemMapper.tradeItemDTOsToTradeItemVos(tradeItems));

        // 1.获取商品信息
        List<String> skuIds = tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        List<GoodsInfoVO> goodsInfos = response.getGoodsInfos();
        Long storeId = goodsInfos.get(0).getStoreId();  //一般立即购买，多个SKU都在同一个店铺下
        Map<String, GoodsInfoVO> skuMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId,
                g -> g));
        List<GoodsInfoVO> valueList = new ArrayList<GoodsInfoVO>(skuMap.values());
        IteratorUtils.zip(valueList, tradeItemGroupVOS.getTradeItems(),
                (a, b) ->
                        a.getGoodsInfoId().equals(b.getSkuId())
                ,
                (c, d) -> {
                    d.setBuyPoint(c.getBuyPoint());
                });
        StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext().getStoreVO();

        // 优化-异步调用。zc_2021/03/25
        CompletableFuture<CycleBuyInfoDTO> cycleBuyInfoDTOCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //周期购
            TradeItemRequest tradeItemRequest = confirmRequest.getTradeItemRequests().get(NumberUtils.INTEGER_ZERO);
            GoodsInfoVO goodsInfoVO = goodsInfos.get(NumberUtils.INTEGER_ZERO);
            return this.fillCycleBuyInfoToSnapshot(goodsInfoVO, tradeItemRequest.getDeliveryCycle(),
                    tradeItemRequest.getSendDateRule(), tradeItemRequest.getDeliveryPlan());
        });

        // 预约活动校验是否有资格
        this.validateAppointmentQualification(Collections.singletonList(tradeItemGroupVOS));
        //校验预售活动资格，初始化价格
        tradeItems = fillActivityPrice(tradeItems, goodsInfos, customer, storeVO);

        // 2.填充商品区间价
        tradeItems = tradeItemMapper.tradeItemVOsToTradeItemDTOs(verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Lists.newArrayList(),
                tradeGoodsInfoPageMapper.goodsInfoResponseToTradeGoodsInfoPageDTO(response), storeId, true)).getContext().getTradeItems());

        List<TradeMarketingDTO> tradeMarketingList = new ArrayList<>();

        if (DefaultFlag.YES.equals(confirmRequest.getStoreBagsFlag())) {
            // 3.礼包商品，重置商品价格
            tradeItems.stream().filter(i -> skuMap.containsKey(i.getSkuId())).forEach(i -> i.setPrice(skuMap.get(i.getSkuId()).getMarketPrice()));
        } else {
            PurchaseGetGoodsMarketingRequest marketingRequest = new PurchaseGetGoodsMarketingRequest();
            marketingRequest.setGoodsInfos(response.getGoodsInfos());
            marketingRequest.setCustomer(customer);
            Map<String, List<MarketingViewVO>> marketingResponse = purchaseQueryProvider.getGoodsMarketing(marketingRequest).getContext().getMap();
            //优化- boolean isIepCustomerFlag = isIepCustomer();重复调用commonUtil.getCustomer()。zc_2021/03/24
            EnterpriseCheckState customerState = customer.getEnterpriseCheckState();
            boolean isIepCustomerFlag = commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING) && !Objects.isNull(customerState) && customerState == EnterpriseCheckState.CHECKED;
            //优化。zc_2021/03/24
            DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
            DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(String.valueOf(storeId));
            //企业会员判断
            Map<String, Long> buyCountMap = new HashMap<>();

            // 积分换购活动只允许存在一件商品
            long count = tradeItems.stream().filter(i -> i.getNum() > 1).count();
            if(marketingResponse.size() > 1 || count > 0){
                marketingResponse.forEach((k, v) -> {
                    v.removeIf(marketing -> MarketingSubType.POINT_BUY.equals(marketing.getSubType()));
                });
            }
            tradeItems.stream().filter(i -> (Objects.isNull(i.getIsAppointmentSaleGoods()) || Boolean.FALSE.equals(i.getIsAppointmentSaleGoods()))
                            && (Objects.isNull(i.getIsBookingSaleGoods()) || Boolean.FALSE.equals(i.getIsBookingSaleGoods()))
                            && skuMap.containsKey(i.getSkuId())).forEach(i -> {
                        GoodsInfoVO sku = skuMap.get(i.getSkuId());
                        if (sku.getDistributionGoodsAudit() != DistributionGoodsAudit.CHECKED
                                || DefaultFlag.NO.equals(openFlag)
                                || DefaultFlag.NO.equals(storeOpenFlag)) {
                            // 4.1.非礼包、非分销商品，设置默认营销
                            i.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                            // 5.企业价回设,过滤不满足的营销活动
                            if (isIepCustomerFlag && isEnjoyIepGoodsInfo(sku.getEnterPriseAuditState())) {
                                buyCountMap.put(i.getSkuId(), i.getNum());
                            }
                            TradeMarketingDTO tradeMarketing = chooseDefaultMarketing(i, marketingResponse.get(i.getSkuId()));
                            if (tradeMarketing != null) {
                                tradeMarketingList.add(tradeMarketing);
                                i.setMarketingIds(Collections.singletonList(tradeMarketing.getMarketingId()));
                                i.setMarketingLevelIds(Collections.singletonList(tradeMarketing.getMarketingLevelId()));
                            }
                        } else {
                            // 4.2.非礼包、分销商品，重置商品价格
                            i.setPrice(sku.getMarketPrice());
                        }
                    });
            if (buyCountMap.size() > 0) {
                //企业价
                EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
                enterprisePriceGetRequest.setGoodsInfoIds(skuIds);
                enterprisePriceGetRequest.setCustomerId(customer.getCustomerId());
                enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
                enterprisePriceGetRequest.setListFlag(false);
                enterprisePriceGetRequest.setOrderFlag(true);
                EnterprisePriceResponse context = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();
                Map<String, BigDecimal> priceMap = context.getPriceMap();
                tradeItems.forEach(i -> {
                    BigDecimal bigDecimal = priceMap.get(i.getSkuId());
                    if (Objects.nonNull(bigDecimal)) {
                        i.setPrice(bigDecimal);
                    }
                });
            }
        }
        if (CollectionUtils.isNotEmpty(tradeMarketingList)) {
            Iterator<TradeMarketingDTO> it = tradeMarketingList.iterator();
            Long currentPoint = -1L;
            while (it.hasNext()){
                TradeMarketingDTO tradeMarketingDTO = it.next();
                if(tradeMarketingDTO.getMarketingSubType() != null && tradeMarketingDTO.getMarketingSubType() == 10){
                    //检查用户积分是否足够
                    if(currentPoint == -1){
                        String fanDengUserNo = commonUtil.getCustomer().getFanDengUserNo();
                        currentPoint = externalProvider.getByUserNoPoint(FanDengPointRequest.builder().userNo(fanDengUserNo).build()).getContext().getCurrentPoint();
                    }
                    if(currentPoint == null || currentPoint < tradeMarketingDTO.getPointNeed()){
                        it.remove();
                    }
                }
            }
        }
        // 结算页要显示定价
        for (TradeItemDTO tradeItem : tradeItems) {
            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
            if(priceByGoodsId.getContext() != null){
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
            }
        }
        return tradeItemProvider.snapshot(
                TradeItemSnapshotRequest.builder()
                        .customerId(customer.getCustomerId())
                        .tradeItems(tradeItems)
                        .tradeMarketingList(tradeMarketingList)
                        .skuList(goodsInfoMapper.goodsInfoVOsToGoodsInfoDTOs(response.getGoodsInfos()))
                        .terminalToken(commonUtil.getTerminalToken())
                        .purchaseBuy(Boolean.FALSE)
                        .cycleBuyInfoDTO(cycleBuyInfoDTOCompletableFuture.get())
                        .build());
    }


    /**
     * 选择商品默认的营销，以及它的level
     */
    private TradeMarketingDTO chooseDefaultMarketing(TradeItemDTO tradeItem, List<MarketingViewVO> marketings) {

        BigDecimal total = tradeItem.getPrice().multiply(new BigDecimal(tradeItem.getNum()));
        Long num = tradeItem.getNum();

        TradeMarketingDTO tradeMarketing = new TradeMarketingDTO();
        tradeMarketing.setSkuIds(Collections.singletonList(tradeItem.getSkuId()));
        tradeMarketing.setGiftSkuIds(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(marketings)) {
            // 积分换购优先级最高
            for (MarketingViewVO marketing : marketings) {
                // 积分换购
                if(marketing.getSubType() == MarketingSubType.POINT_BUY){
                    String skuId = tradeItem.getSkuId();
                    List<MarketingPointBuyLevelVO> pointBuyLevelList = marketing.getPointBuyLevelList();
                    if(CollectionUtils.isNotEmpty(pointBuyLevelList)){
                        for (MarketingPointBuyLevelVO marketingPointBuyLevelVO : pointBuyLevelList) {
                            if(marketingPointBuyLevelVO.getSkuId().equals(skuId)){
                                tradeMarketing.setMarketingLevelId(pointBuyLevelList.get(0).getId());
                                tradeMarketing.setMarketingId(pointBuyLevelList.get(0).getMarketingId());
                                tradeMarketing.setMarketingSubType(marketing.getSubType().toValue());
                                tradeMarketing.setPointNeed(pointBuyLevelList.get(0).getPointNeed());
                                return tradeMarketing;
                            }
                        }
                    }
                }
            }
            for (MarketingViewVO marketing : marketings) {
                // 满金额减
                if (marketing.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                    List<MarketingFullReductionLevelVO> levels = marketing.getFullReductionLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullReductionLevelVO::getFullAmount).reversed());
                        for (MarketingFullReductionLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满数量减
                if (marketing.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT) {
                    List<MarketingFullReductionLevelVO> levels = marketing.getFullReductionLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullReductionLevelVO::getFullCount).reversed());
                        for (MarketingFullReductionLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满金额折
                if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_AMOUNT) {
                    List<MarketingFullDiscountLevelVO> levels = marketing.getFullDiscountLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullDiscountLevelVO::getFullAmount).reversed());
                        for (MarketingFullDiscountLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getDiscountLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }
                // 满数量折
                if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_COUNT) {
                    List<MarketingFullDiscountLevelVO> levels = marketing.getFullDiscountLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullDiscountLevelVO::getFullCount).reversed());
                        for (MarketingFullDiscountLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getDiscountLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满金额赠
                if (marketing.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                    List<MarketingFullGiftLevelVO> levels = marketing.getFullGiftLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullAmount).reversed());
                        for (MarketingFullGiftLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getGiftLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                List<String> giftIds =
                                        level.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList());
                                if (GiftType.ONE.equals(level.getGiftType())) {
                                    giftIds = Collections.singletonList(giftIds.get(0));
                                }
                                tradeMarketing.setGiftSkuIds(giftIds);
                                return tradeMarketing;
                            }
                        }
                    }
                }
                // 满数量赠
                if (marketing.getSubType() == MarketingSubType.GIFT_FULL_COUNT) {
                    List<MarketingFullGiftLevelVO> levels = marketing.getFullGiftLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullCount).reversed());
                        for (MarketingFullGiftLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getGiftLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                List<String> giftIds =
                                        level.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList());
                                if (GiftType.ONE.equals(level.getGiftType())) {
                                    giftIds = Collections.singletonList(giftIds.get(0));
                                }
                                tradeMarketing.setGiftSkuIds(giftIds);
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 打包一口价
                if (marketing.getSubType() == MarketingSubType.BUYOUT_PRICE) {
                    List<MarketingBuyoutPriceLevelVO> levels = marketing.getBuyoutPriceLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingBuyoutPriceLevelVO::getChoiceCount).reversed());
                        for (MarketingBuyoutPriceLevelVO level : levels) {
                            if (level.getChoiceCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 第二件半价
                if (marketing.getSubType() == MarketingSubType.HALF_PRICE_SECOND_PIECE) {
                    List<MarketingHalfPriceSecondPieceLevelVO> levels = marketing.getHalfPriceSecondPieceLevel();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingHalfPriceSecondPieceLevelVO::getNumber).reversed());
                        for (MarketingHalfPriceSecondPieceLevelVO level : levels) {
                            if (level.getNumber().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 提交订单，用于生成订单操作
     */
    @ApiOperation(value = "提交订单，用于生成订单操作")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @MultiSubmitWithToken
    @GlobalTransactional
    public BaseResponse<List<TradeCommitResultVO>> commit(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        //校验是否需要完善地址信息

        CustomerDeliveryAddressByIdResponse address = null;
        customerDeliveryAddressQueryProvider.getById(new CustomerDeliveryAddressByIdRequest(tradeCommitRequest.getConsigneeId())).getContext();
        if (address != null) {
            PlatformAddressVerifyRequest platformAddressVerifyRequest = new PlatformAddressVerifyRequest();
            if (Objects.nonNull(address.getProvinceId())) {
                platformAddressVerifyRequest.setProvinceId(String.valueOf(address.getProvinceId()));
            }
            if (Objects.nonNull(address.getCityId())) {
                platformAddressVerifyRequest.setCityId(String.valueOf(address.getCityId()));
            }
            if (Objects.nonNull(address.getAreaId())) {
                platformAddressVerifyRequest.setAreaId(String.valueOf(address.getAreaId()));
            }
            if (Objects.nonNull(address.getStreetId())) {
                platformAddressVerifyRequest.setStreetId(String.valueOf(address.getStreetId()));
            }
            if (Boolean.TRUE.equals(platformAddressQueryProvider.verifyAddress(platformAddressVerifyRequest).getContext())) {
                throw new SbcRuntimeException("K-220001");
            }
        }
        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());
        rLock.lock();
        List<TradeCommitResultVO> successResults;
        try {
            Operator operator = commonUtil.getOperator();
            tradeCommitRequest.setOperator(operator);

            List<TradeItemGroupVO> tradeItemGroups =
                    tradeItemQueryProvider.listByTerminalToken(TradeItemByCustomerIdRequest
                            .builder().terminalToken(commonUtil.getTerminalToken()).build()).getContext().getTradeItemGroupList();

            DefaultFlag storeBagsFlag = tradeItemGroups.get(0).getStoreBagsFlag();
            // 邀请人不是分销员时，清空inviteeId
            DistributeChannel distributeChannel = commonUtil.getDistributeChannel();
            DistributionSettingVO distributionSettingVO = distributionCacheService.queryDistributionSetting();
            DefaultFlag openFlag = distributionSettingVO.getOpenFlag();
            Boolean checkInviteeIdIsDistributor = Boolean.FALSE;
            if (Objects.nonNull(distributeChannel) && StringUtils.isNotEmpty(distributeChannel.getInviteeId())) {
                checkInviteeIdIsDistributor = distributionService.checkIsDistributor(openFlag, distributeChannel.getInviteeId());
            }
            DefaultFlag queryShopOpenFlag = distributionSettingVO.getShopOpenFlag();
            if (DefaultFlag.NO.equals(storeBagsFlag) && !distributionService.checkInviteeIdEnable(distributeChannel, checkInviteeIdIsDistributor, queryShopOpenFlag)) {
                // 非开店礼包情况下，判断小店状态不可用
                throw new SbcRuntimeException(WebBaseErrorCode.NOT_BUY_SHOP_GIFT);
            }

            // 邀请人不是分销员时，清空inviteeId[不用缓存的原因是，如果用户先扫描分销的二维码进来，这里取出来的邀请人就是分销员，而不是真正的邀请人]
            DistributionCustomerVO distributionCustomerVO = distributionCustomerQueryProvider.getByCustomerId(DistributionCustomerByCustomerIdRequest.builder().customerId(commonUtil.getOperatorId()).build()).getContext().getDistributionCustomerVO();
            if (Objects.nonNull(distributionCustomerVO) && StringUtils.isNotBlank(distributionCustomerVO.getInviteCustomerIds())) {
                //目前一个用户只会有一个邀请人
                String[] invites = distributionCustomerVO.getInviteCustomerIds().split(",");
                if (invites.length > 0 && !distributionService.isDistributor(invites[0])) {
                    distributeChannel.setInviteeId(null);
                }
            }
            // 设置下单用户，是否分销员
            if (distributionService.checkIsDistributor(openFlag, operator.getUserId())) {
                tradeCommitRequest.setIsDistributor(DefaultFlag.YES);
            }
            tradeCommitRequest.setDistributeChannel(distributeChannel);
            tradeCommitRequest.setShopName(distributionSettingVO.getShopName());

            // 设置分销设置开关
            tradeCommitRequest.setOpenFlag(openFlag);
            tradeCommitRequest.getStoreCommitInfoList().forEach(item ->
                    item.setStoreOpenFlag(distributionCacheService.queryStoreOpenFlag(item.getStoreId().toString()))
            );

            // 验证小店商品
            validShopGoods(tradeItemGroups, tradeCommitRequest.getDistributeChannel());
            // 校验商品限售信息
            // CustomerVO customer = commonUtil.getCustomer();
//            tradeItemGroups.forEach(tradeItemGroupVO -> this.validateRestrictedGoods(tradeItemGroupVO,
//                    customer));

            // 预约活动校验是否有资格
            //this.validateAppointmentQualification(tradeItemGroups);

            // 增加预售活动过期校验
            //this.validateBookingQualification(tradeItemGroups, tradeCommitRequest);

            //校验是商品是否参加了预约，预售活动
            //this.containAppointmentSaleAndBookingSale(tradeItemGroups, customer);

            tradeCommitRequest.setTerminalToken(commonUtil.getTerminalToken());

            //tradeCommitRequest.setCustomer(customer);

            successResults = tradeProvider.commit(tradeCommitRequest).getContext().getTradeCommitResults();

            //标记预售定金标识，前端可判断跳转至订单列表页
            if (CollectionUtils.isNotEmpty(tradeItemGroups)
                    && CollectionUtils.isNotEmpty(tradeItemGroups.get(0).getTradeItems())
                    && Boolean.TRUE.equals(tradeItemGroups.get(0).getTradeItems().get(0).getIsBookingSaleGoods())) {
                successResults.get(0).setIsBookingSaleGoods(Boolean.TRUE);
            }

            //如果是秒杀商品订单更新会员已抢购该商品数量
            if (Objects.nonNull(tradeCommitRequest.getIsFlashSaleGoods()) && tradeCommitRequest.getIsFlashSaleGoods()) {
                TradeVO trade =
                        tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(successResults.get(0).getTid()).build()).getContext().getTradeVO();
                String havePanicBuyingKey =
                        RedisKeyConstant.FLASH_SALE_GOODS_HAVE_PANIC_BUYING + operator.getUserId() + trade.getTradeItems().get(0).getFlashSaleGoodsId();
                redisService.setString(havePanicBuyingKey,
                        Integer.valueOf(StringUtils.isNotBlank(redisService.getString(havePanicBuyingKey)) ?
                                redisService.getString(havePanicBuyingKey) : "0") + trade.getTradeItems().get(0).getNum() + "");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rLock.unlock();
        }
        return BaseResponse.success(successResults);
    }


    /**
     * 提交订单，用于尾款支付操作
     */
    @ApiOperation(value = "提交订单，用于尾款支付操作")
    @RequestMapping(value = "/commit/final", method = RequestMethod.POST)
    @MultiSubmit
    @GlobalTransactional
    public BaseResponse<List<TradeCommitResultVO>> commitTail(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        if (StringUtils.isEmpty(tradeCommitRequest.getTid())) {
            throw new SbcRuntimeException("K-000009");
        }
        if (StringUtils.isEmpty(tradeCommitRequest.getTailNoticeMobile())) {
            throw new SbcRuntimeException("K-000009");
        }
        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());
        rLock.lock();
        List<TradeCommitResultVO> successResults;
        try {
            TradeVO trade =
                    tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tradeCommitRequest.getTid()).build()).getContext().getTradeVO();
            if (Objects.isNull(trade) || Objects.isNull(trade.getIsBookingSaleGoods()) || !trade.getIsBookingSaleGoods()
                    || trade.getBookingType() != BookingType.EARNEST_MONEY) {
                throw new SbcRuntimeException("K-020007");
            }
            if (LocalDateTime.now().isBefore(trade.getTradeState().getTailStartTime())) {
                throw new SbcRuntimeException("K-170010");
            }
            if (trade.getTradeState().getFlowState() == FlowState.AUDIT || trade.getTradeState().getPayState() != PayState.PAID_EARNEST) {
                return BaseResponse.SUCCESSFUL();
            }
            Operator operator = commonUtil.getOperator();
            tradeCommitRequest.setOperator(operator);
            tradeCommitRequest.setTerminalToken(commonUtil.getTerminalToken());
            //生成尾预售款订单快照
            generateSnapshot(tradeCommitRequest, trade);
            List<TradeItemGroupVO> tradeItemGroups =
                    tradeItemQueryProvider.listByTerminalToken(TradeItemByCustomerIdRequest
                            .builder().customerId(commonUtil.getOperatorId()).terminalToken(commonUtil.getTerminalToken()).build()).getContext().getTradeItemGroupList();

            DefaultFlag storeBagsFlag = tradeItemGroups.get(0).getStoreBagsFlag();
            if (DefaultFlag.NO.equals(storeBagsFlag) && !distributionService.checkInviteeIdEnable()) {
                // 非开店礼包情况下，判断小店状态不可用
                throw new SbcRuntimeException("K-080301");
            }

            // 邀请人不是分销员时，清空inviteeId
            DistributeChannel distributeChannel = commonUtil.getDistributeChannel();
            if (StringUtils.isNotEmpty(distributeChannel.getInviteeId()) &&
                    !distributionService.isDistributor(distributeChannel.getInviteeId())) {
                distributeChannel.setInviteeId(null);
            }
            // 设置下单用户，是否分销员
            if (distributionService.isDistributor(operator.getUserId())) {
                tradeCommitRequest.setIsDistributor(DefaultFlag.YES);
            }
            tradeCommitRequest.setDistributeChannel(distributeChannel);
            tradeCommitRequest.setShopName(distributionCacheService.getShopName());

            // 设置分销设置开关
            tradeCommitRequest.setOpenFlag(distributionCacheService.queryOpenFlag());
            tradeCommitRequest.getStoreCommitInfoList().forEach(item ->
                    item.setStoreOpenFlag(distributionCacheService.queryStoreOpenFlag(item.getStoreId().toString()))
            );
            successResults = tradeProvider.commitTail(tradeCommitRequest).getContext().getTradeCommitResults();
        } catch (Exception e) {
            throw e;
        } finally {
            rLock.unlock();
        }
        return BaseResponse.success(successResults);
    }


    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author zhangxiaodong
     * @Description 生成尾预售款订单快照
     * @Date 2020/06/08
     * @Param [request]
     **/
    @GlobalTransactional
    public BaseResponse generateSnapshot(TradeCommitRequest request, TradeVO tradeVO) {

        if (StringUtils.isEmpty(request.getTailNoticeMobile())) {
            throw new SbcRuntimeException("K-000009");
        }
        String customerId = request.getOperator().getUserId();
        //设置尾款订单快照数据信息
        TradeItemRequest tradeItemRequest = new TradeItemRequest();
        TradeItemVO tradeItemVO = tradeVO.getTradeItems().get(0);
        tradeItemRequest.setNum(tradeItemVO.getNum());
        tradeItemRequest.setSkuId(tradeItemVO.getSkuId());
        tradeItemRequest.setIsFlashSaleGoods(false);
        tradeItemRequest.setBookingSaleId(tradeItemVO.getBookingSaleId());
        tradeItemRequest.setIsBookingSaleGoods(true);

        List<TradeItemRequest> tradeItemConfirmRequests = new ArrayList<>();
        tradeItemConfirmRequests.add(tradeItemRequest);

        List<TradeMarketingDTO> tradeMarketingList = new ArrayList<>();
        TradeItemConfirmRequest confirmRequest = new TradeItemConfirmRequest();
        confirmRequest.setTradeItems(tradeItemConfirmRequests);
        confirmRequest.setTradeMarketingList(tradeMarketingList);
        confirmRequest.setForceConfirm(false);
        List<TradeItemDTO> tradeItems = confirmRequest.getTradeItems().stream().map(
                o -> TradeItemDTO.builder().skuId(o.getSkuId()).num(o.getNum()).price(o.getPrice())
                        .isFlashSaleGoods(o.getIsFlashSaleGoods()).flashSaleGoodsId(o.getFlashSaleGoodsId())
                        .isBookingSaleGoods(o.getIsAppointmentSaleGoods()).bookingSaleId(o.getAppointmentSaleId()).build()
        ).collect(Collectors.toList());
        List<String> skuIds =
                confirmRequest.getTradeItems().stream().map(TradeItemRequest::getSkuId).collect(Collectors.toList());
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        //商品验证
//        verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
//                KsBeanUtil.convert(response, TradeGoodsInfoPageDTO.class), null, false));
        verifyQueryProvider.verifyStore(new VerifyStoreRequest(response.getGoodsInfos().stream().map
                (GoodsInfoVO::getStoreId).collect(Collectors.toList())));
        //营销活动校验
        verifyQueryProvider.verifyTradeMarketing(new VerifyTradeMarketingRequest(confirmRequest.getTradeMarketingList
                (), Collections.emptyList(), tradeItems, customerId, confirmRequest.isForceConfirm()));
        return tradeItemProvider.snapshot(TradeItemSnapshotRequest.builder().customerId(customerId).tradeItems
                (tradeItems)
                .tradeMarketingList(confirmRequest.getTradeMarketingList())
                .skuList(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                .snapshotType(Constants.BOOKING_SALE_TYPE)
                .terminalToken(request.getTerminalToken()).build());
    }


    @ApiOperation(value = "展示订单基本信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/show/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeCommitResultVO> commitResp(@PathVariable String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        checkUnauthorized(tid, trade);
        BigDecimal payPrice = trade.getTradePrice().getTotalPrice();
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() &&
                trade.getBookingType() == BookingType.EARNEST_MONEY &&
                trade.getTradeState().getPayState() == PayState.NOT_PAID) {
            payPrice = trade.getTradePrice().getEarnestPrice();
        }
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                && trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
            payPrice = trade.getTradePrice().getTailPrice();
        }
        return BaseResponse.success(new TradeCommitResultVO(tid, trade.getParentId(), trade.getTradeState(),
                trade.getPaymentOrder(),
                payPrice, trade.getTradePrice().getTotalPrice(),
                trade.getOrderTimeOut(), null,
                trade.getSupplier().getStoreName(),
                trade.getSupplier().getIsSelf()));
    }

    private List<GoodsLevelPriceVO> getGoodsLevelPrices(List<String> skuIds, CustomerVO customer) {
        List<GoodsLevelPriceVO> goodsLevelPriceList = new ArrayList<>();
        if (Objects.nonNull(customer)
                && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())
                && CollectionUtils.isNotEmpty(skuIds)) {
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
     * 用于确认订单后，创建订单前的获取订单商品信息
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取订单商品信息")
    @RequestMapping(value = "/purchase", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<TradeConfirmResponse> getPurchaseItems() {

        TradeConfirmResponse confirmResponse = new TradeConfirmResponse();
        String customerId = commonUtil.getOperatorId();
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        List<TradeItemGroupVO> tradeItemGroups = tradeItemQueryProvider.listByTerminalToken(TradeItemByCustomerIdRequest
                .builder().terminalToken(commonUtil.getTerminalToken()).build()).getContext().getTradeItemGroupList();

        List<TradeConfirmItemVO> items = new ArrayList<>();
        List<String> skuIds = tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream())
                .map(TradeItemVO::getSkuId).collect(Collectors.toList());
        //获取订单商品详情,包含区间价，会员级别价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);
        Map<String, Integer> cpsMap = skuResp.getGoodses().stream().filter(good -> good.getCpsSpecial() != null).collect(Collectors.toMap(GoodsVO::getGoodsId, GoodsVO::getCpsSpecial));
        // 营销活动赠品
        List<String> giftIds =
                tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
                        .filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds())).flatMap(r -> r.getGiftSkuIds()
                        .stream()).distinct().collect(Collectors.toList());
        giftIds.addAll(tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
                .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds())).flatMap(r -> r.getMarkupSkuIds()
                        .stream()).distinct().collect(Collectors.toList()));
        Map<Long, StoreVO> storeMap = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
                (tradeItemGroups.stream().map(g -> g
                        .getSupplier().getStoreId())
                        .collect(Collectors.toList()))).getContext().getStoreVOList().stream().collect(Collectors
                .toMap(StoreVO::getStoreId,
                        s -> s));
        TradeGetGoodsResponse giftResp;
        giftResp = tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(giftIds).build()).getContext();
        final TradeGetGoodsResponse giftTemp = giftResp;
        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE,
                s.getSuitMarketingFlag()));
        Integer suitScene = null;
        if(suitMarketingFlag && tradeItemGroups.stream().anyMatch(s -> s.getSuitScene()!=null)){
            suitScene = tradeItemGroups.stream().filter(s -> s.getSuitScene()!=null).findFirst().get().getSuitScene();
        }
        //拼团标记
        boolean grouponFlag = tradeItemGroups.stream().anyMatch(s -> s.getGrouponForm() != null && s.getGrouponForm().getOpenGroupon() != null);
        confirmResponse.setSuitMarketingFlag(suitMarketingFlag);
        confirmResponse.setSuitScene(suitScene);
        // 如果为PC商城下单or组合购商品，将分销商品变为普通商品
        if (ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType()) || suitMarketingFlag || grouponFlag) {
            tradeItemGroups.forEach(tradeItemGroup ->
                    tradeItemGroup.getTradeItems().forEach(tradeItem -> {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                        if (suitMarketingFlag) {
                            tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
                        }
                    })
            );
            skuResp.getGoodsInfos().forEach(item -> {
                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                if (suitMarketingFlag) {
                    item.setBuyPoint(NumberUtils.LONG_ZERO);
                }
            });
        }

        //企业会员判断
        boolean isIepCustomerFlag = isIepCustomer(customer);
        boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();

        Map<String, Long> buyCountMap = new HashMap<>();
        Map<String, BigDecimal> enterprisePriceMap = new HashMap<>();
        List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();
        if (isIepCustomerFlag) {
            tradeItemGroups.forEach(e -> {
                List<TradeItemVO> tradeItems = e.getTradeItems();
                tradeItems.forEach(item -> {
                    buyCountMap.put(item.getSkuId(), item.getNum());
                });
            });
            //企业价
            EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
            enterprisePriceGetRequest.setGoodsInfoIds(skuIds);
            enterprisePriceGetRequest.setCustomerId(customer.getCustomerId());
            enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
            enterprisePriceGetRequest.setListFlag(false);
            enterprisePriceGetRequest.setOrderFlag(true);

            EnterprisePriceResponse context = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();
            Map<String, BigDecimal> priceMap = context.getPriceMap();
            if (priceMap != null) {
                enterprisePriceMap.putAll(priceMap);
            }

            //会员等级价
            goodsLevelPrices.addAll(this.getGoodsLevelPrices(skuIds, customer));
        }

        //分销开关
        boolean distributionPcMall = DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())
                && !ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType());
        //商品验证并填充商品价格
        List<Long> flashSaleGoodsIds = new ArrayList<>();
        tradeItemGroups.forEach(
                g -> {
                    //周期购使用单品运费
                    DefaultFlag freightTemplateType = Objects.nonNull(g.getCycleBuyInfo())
                            ? DefaultFlag.YES
                            : storeMap.get(g.getSupplier().getStoreId()).getFreightTemplateType();
                    g.getSupplier().setFreightTemplateType(freightTemplateType);
                    List<TradeItemVO> tradeItems = g.getTradeItems();
                    List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(tradeItems, TradeItemDTO.class);
                    // 分销商品、开店礼包商品、拼团商品、企业购商品，不验证起限定量
                    skuResp.getGoodsInfos().forEach(item -> {
                        //企业购商品
                        boolean isIep = isIepCustomerFlag && isEnjoyIepGoodsInfo(item.getEnterPriseAuditState());
                        if (DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                || DefaultFlag.YES.equals(g.getStoreBagsFlag())
                                || Objects.nonNull(g.getGrouponForm()) || isIep) {
                            item.setCount(null);
                            item.setMaxCount(null);
                        }
                    });
                    //商品验证并填充商品价格
                    List<TradeItemVO> tradeItemVOList =
                            verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItemDTOList, Collections
                                    .emptyList(),
                                    KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
                                    g.getSupplier().getStoreId(), true)).getContext().getTradeItems();
                    tradeItemVOList.stream().forEach(
                            tradeItemVO -> {
                                tradeItemVO.setCpsSpecial(cpsMap.get(tradeItemVO.getSpuId()));
                            }
                    );
                    //企业购商品价格回设
                    if (isIepCustomerFlag) {
                        tradeItemVOList.forEach(tradeItemVO -> {
                            if (isEnjoyIepGoodsInfo(tradeItemVO.getEnterPriseAuditState())
                                    && (!isGoodsPoint || (isGoodsPoint && (tradeItemVO.getBuyPoint() == null || tradeItemVO.getBuyPoint() == 0)))) {
                                tradeItemVO.setPrice(tradeItemVO.getEnterPrisePrice());

                                BigDecimal price = enterprisePriceMap.get(tradeItemVO.getSkuId());
                                if (price != null) {
                                    tradeItemVO.setPrice(price);
                                }
                                tradeItemVO.setSplitPrice(tradeItemVO.getEnterPrisePrice().multiply(new BigDecimal(tradeItemVO.getNum())));
                                tradeItemVO.setLevelPrice(tradeItemVO.getEnterPrisePrice());
                                //判断当前用户对应企业购商品等级企业价
                                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(tradeItemVO.getSkuId()))
                                            .findFirst();
                                    tradeItemVO.setPrice(first.isPresent() ? first.get().getPrice() : tradeItemVO.getEnterPrisePrice());
                                }
                            }
                        });
                    }
                    //抢购商品价格回设
                    if (StringUtils.isNotBlank(g.getSnapshotType()) && (g.getSnapshotType().equals(Constants.FLASH_SALE_GOODS_ORDER_TYPE))) {
                        tradeItemVOList.forEach(tradeItemVO -> {
                            g.getTradeItems().forEach(tradeItem -> {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                }
                            });
                        });
                        if (CollectionUtils.isNotEmpty(tradeItemVOList)) {
                            flashSaleGoodsIds.add(tradeItemVOList.get(0).getFlashSaleGoodsId());
                        }
                    }
                    tradeItemVOList.forEach(tradeItemVO -> {

                        g.getTradeItems().forEach(tradeItem -> {
                            g.getTradeMarketingList().forEach(tradeItemMarketingVO -> {
                                if (tradeItemMarketingVO.getSkuIds().contains(tradeItemVO.getSkuId())
                                        && !tradeItemVO.getMarketingIds().contains(tradeItemMarketingVO.getMarketingId())) {
                                    tradeItemVO.getMarketingIds().add(tradeItemMarketingVO.getMarketingId());
                                    tradeItemVO.getMarketingLevelIds().add(tradeItemMarketingVO.getMarketingLevelId());
                                }
                            });
                            if ((Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                    tradeItemVO.setIsAppointmentSaleGoods(tradeItem.getIsAppointmentSaleGoods());
                                    tradeItemVO.setAppointmentSaleId(tradeItem.getAppointmentSaleId());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.FULL_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.EARNEST_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getOriginalPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                    tradeItemVO.setHandSelStartTime(tradeItem.getHandSelStartTime());
                                    tradeItemVO.setHandSelEndTime(tradeItem.getHandSelEndTime());
                                    tradeItemVO.setTailStartTime(tradeItem.getTailStartTime());
                                    tradeItemVO.setTailEndTime(tradeItem.getTailEndTime());
                                    tradeItemVO.setEarnestPrice(tradeItem.getEarnestPrice());
                                    tradeItemVO.setSwellPrice(tradeItem.getSwellPrice());
                                }
                            }
                        });
                    });
                    g.setTradeItems(tradeItemVOList);
                    // 分销商品、开店礼包商品，重新设回市场价
                    if (distributionPcMall) {
                        g.getTradeItems().stream().filter(tradeItemVO -> DefaultFlag.YES.equals(g.getStoreBagsFlag())
                                || (Objects.isNull(tradeItemVO.getBuyPoint()) || tradeItemVO.getBuyPoint() == 0)).forEach(item -> {
                            DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId()
                                    .toString());
                            if ((Objects.isNull(item.getIsFlashSaleGoods()) ||
                                    (Objects.nonNull(item.getIsFlashSaleGoods()) && !item.getIsFlashSaleGoods())) &&
                                    (Objects.isNull(item.getIsAppointmentSaleGoods()) || !item.getIsAppointmentSaleGoods()) &&
                                    !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods() && item.getBookingType() == BookingType.FULL_MONEY) &&
                                    DefaultFlag.YES.equals(storeOpenFlag) && (
                                    DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                            || DefaultFlag.YES.equals(g.getStoreBagsFlag()))) {
                                if (null == item.getOriginalPrice()) {
                                    item.setOriginalPrice(BigDecimal.ZERO);
                                }
                                item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                                item.setPrice(item.getOriginalPrice());
                                item.setLevelPrice(item.getOriginalPrice());
                                if (DefaultFlag.YES.equals(g.getStoreBagsFlag())) {
                                    item.setBuyPoint(NumberUtils.LONG_ZERO);
                                }
                            } else {
                                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                            }
                        });
                    }
                    confirmResponse.setStoreBagsFlag(g.getStoreBagsFlag());
                }
        );

        //秒杀包邮
        if (CollectionUtils.isNotEmpty(flashSaleGoodsIds)) {
            FlashSaleGoodsListRequest flashSaleGoodsListRequest = new FlashSaleGoodsListRequest();
            flashSaleGoodsListRequest.setIdList(flashSaleGoodsIds);
            List<FlashSaleGoodsVO> flashSaleGoodsVOList = flashSaleGoodsQueryProvider.list(flashSaleGoodsListRequest).getContext().getFlashSaleGoodsVOList();
            if (CollectionUtils.isNotEmpty(flashSaleGoodsVOList)) {
                Boolean flashFreeDelivery = Boolean.FALSE;
                for (FlashSaleGoodsVO flashSaleGoodsVO : flashSaleGoodsVOList) {
                    flashFreeDelivery = flashSaleGoodsVO.getPostage().equals(NumberUtils.INTEGER_ONE);
                }
                confirmResponse.setFlashFreeDelivery(flashFreeDelivery);
            }
        }

        List<TradeItemMarketingVO> tradeMarketingList = tradeItemGroups.stream()
                .flatMap(tradeItemGroupVO -> tradeItemGroupVO.getTradeMarketingList().stream())
                .collect(Collectors.toList());
        // 加价购商品信息填充
        List<Long> marketingIds =
                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                        .map(TradeItemMarketingVO::getMarketingId).distinct().collect(Collectors.toList());

        MarkupListRequest markupListRequest = MarkupListRequest.builder().marketingId(marketingIds).build();
        List<MarkupLevelVO> levelList = markupQueryProvider.getMarkupList(markupListRequest).getContext().getLevelList();

        //赠品信息填充
        List<String> giftItemIds =
                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds()))
                        .flatMap(r -> r.getGiftSkuIds().stream()).distinct().collect(Collectors.toList());
        List<TradeItemDTO> gifts =
                giftItemIds.stream().map(i -> TradeItemDTO.builder().price(BigDecimal.ZERO)
                        .skuId(i)
                        .build()).collect(Collectors.toList());

        List<TradeItemVO> giftVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(gifts
                , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
                .getContext().getTradeItems();
        gifts = KsBeanUtil.convert(giftVoList, TradeItemDTO.class);
        List<TradeItemDTO> markupItem = new ArrayList<>();
        for (TradeItemGroupVO g : tradeItemGroups) {
            Map<String, List<MarkupLevelVO>> listMap = levelList.stream().collect(Collectors.groupingBy(l -> "" + l.getMarkupId() + l.getId()));
            // 通过 加价购id 阶梯id 加购商品sku定位 换购价格
            List<TradeItemDTO> finalMarkupItem = markupItem;
            g.getTradeMarketingList().parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                    .forEach(m -> {
                        List<MarkupLevelVO> markupLevelVOS = listMap.get("" + m.getMarketingId() + m.getMarketingLevelId());
                        if (CollectionUtils.isNotEmpty(markupLevelVOS)) {
                            m.getMarkupSkuIds().stream().forEach(sku -> {
                                markupLevelVOS.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
                                        .filter(detailVO -> sku.equals(detailVO.getGoodsInfoId()))
                                        .forEach(detail -> {
                                            finalMarkupItem.add(TradeItemDTO.builder()
                                                    .price(detail.getMarkupPrice())
                                                    .skuId(detail.getGoodsInfoId())
                                                    .num(NumberUtils.LONG_ONE)
                                                    .isMarkupGoods(Boolean.TRUE)
                                                    .build());
                                        });
                            });

                        }
                    });

            List<TradeItemVO> markupVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(markupItem
                    , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
                    .getContext().getTradeItems();
            // 换购商品阶梯价不受其他影响
            markupVoList.forEach(m -> {
                m.setSplitPrice(m.getPrice());
                m.setLevelPrice(m.getPrice());
                m.setOriginalPrice(m.getPrice());
            });
            markupItem = KsBeanUtil.convert(markupVoList, TradeItemDTO.class);
            items.add(tradeQueryProvider.queryPurchaseInfo(TradeQueryPurchaseInfoRequest.builder()
                    .tradeItemGroupDTO(KsBeanUtil.convert(g, TradeItemGroupDTO.class))
                    .tradeItemList(gifts).markupItemList(markupItem).build()).getContext().getTradeConfirmItemVO());
        }
        this.setGoodsStatus(items);

        // 验证小店商品
        this.validShopGoods(tradeItemGroups, commonUtil.getDistributeChannel());
        //设置购买总积分
        confirmResponse.setTotalBuyPoint(items.stream().flatMap(i -> i.getTradeItems().stream())
                .filter(i -> Objects.isNull(i.getIsMarkupGoods()) || !i.getIsMarkupGoods()).mapToLong(v -> Objects.isNull(v.getBuyPoint()) ? 0 : v.getBuyPoint() * v.getNum()).sum());

        confirmResponse.setTradeConfirmItems(items);

        BigDecimal totalCommission = dealDistribution(tradeItemGroups, confirmResponse);

        // 设置小店名称、返利总价
        confirmResponse.setShopName(distributionCacheService.getShopName());

        confirmResponse.setTotalCommission(totalCommission);

        // 设置邀请人名字
        if (StringUtils.isNotEmpty(commonUtil.getDistributeChannel().getInviteeId())) {
            DistributionCustomerByCustomerIdRequest request = new DistributionCustomerByCustomerIdRequest();
            request.setCustomerId(commonUtil.getDistributeChannel().getInviteeId());
            DistributionCustomerVO distributionCustomer = distributionCustomerQueryProvider.getByCustomerId(request)
                    .getContext().getDistributionCustomerVO();
            if (distributionCustomer != null) {
                confirmResponse.setInviteeName(distributionCustomer.getCustomerName());
            }
        }

        // 校验拼团信息
        validGrouponOrder(confirmResponse, tradeItemGroups, customerId);
        // 校验组合购活动信息
        dealSuitOrder(confirmResponse, tradeItemGroups, customerId);

        // 填充立即购买的满系营销信息
        fillMarketingInfo(confirmResponse, tradeItemGroups);

        //填充周期购信息
        fillCycleBuyInfoToConfirmResponse(confirmResponse, tradeItemGroups);

        // 根据确认订单计算出的信息，查询出使用优惠券页需要的优惠券列表数据
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        List<TradeItemInfoDTO> tradeDtos = items.stream().flatMap(confirmItem ->
                confirmItem.getTradeItems().stream().map(tradeItem -> {
                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
                    dto.setBrandId(tradeItem.getBrand());
                    dto.setCateId(tradeItem.getCateId());
                    dto.setSpuId(tradeItem.getSpuId());
                    dto.setSkuId(tradeItem.getSkuId());
                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
                    dto.setPrice(tradeItem.getSplitPrice());
                    dto.setDistributionGoodsAudit(tradeItem.getDistributionGoodsAudit());
                    if (DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(
                            distributionCacheService.queryStoreOpenFlag(String.valueOf(tradeItem.getStoreId())))) {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    }
                    return dto;
                })
        ).collect(Collectors.toList());

        // 加价购填充
        markupForGoodsInfo(confirmResponse);

        CouponCodeListForUseByCustomerIdRequest requ = CouponCodeListForUseByCustomerIdRequest.builder()
                .customerId(customerId)
                .tradeItems(tradeDtos).price(confirmResponse.getTotalPrice()).build();

        confirmResponse.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(requ).getContext()
                .getCouponCodeList());

        return BaseResponse.success(confirmResponse);
    }

    /**
     * 添加加价购商品
     *
     * @param confirmResponse
     */
    private void markupForGoodsInfo(TradeConfirmResponse confirmResponse) {
        // 是否有加价购
        List<TradeConfirmMarketingVO> tradeConfirmMarketingVOS = confirmResponse.getTradeConfirmItems().stream()
                .filter(i -> CollectionUtils.isNotEmpty(i.getTradeConfirmMarketingList()))
                .flatMap(i -> i.getTradeConfirmMarketingList().stream())
                .filter(m -> m.getMarketingType() == MarketingType.MARKUP.toValue())
                .collect(Collectors.toList());
        // 页面不需要展示加价购营销信息,排除
        confirmResponse.getTradeConfirmItems().stream()
                .filter(i -> CollectionUtils.isNotEmpty(i.getTradeConfirmMarketingList()))
                .forEach(i -> {
                    List<TradeConfirmMarketingVO> markupList = i.getTradeConfirmMarketingList().stream()
                            .filter(m -> m.getMarketingType() == MarketingType.MARKUP.toValue())
                            .collect(Collectors.toList());
                    i.getTradeConfirmMarketingList().removeAll(markupList);
                });
        if (CollectionUtils.isNotEmpty(tradeConfirmMarketingVOS)) {
            return;
        }

        // 秒杀,拼团,预售商品 不显示加价购信息
        TradeItemVO item = confirmResponse.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO).getTradeItems().get(NumberUtils.INTEGER_ZERO);
        if (Objects.nonNull(confirmResponse.getOpenGroupon()) || item.getIsBookingSaleGoods()
                || item.getIsFlashSaleGoods()) {
            return;
        }
        // 获取用户在店铺里的等级
        CustomerVO customer = commonUtil.getCustomer();
        List<Long> storeIds =
                confirmResponse.getTradeConfirmItems().stream().map(i -> i.getSupplier().getStoreId()).collect(Collectors.toList());
        final HashMap<Long, CommonLevelVO> commonLevelVOMap = marketingPluginQueryProvider.getCustomerLevelsByStoreIds(
                MarketingPluginGetCustomerLevelsByStoreIdsRequest.builder().storeIds(storeIds)
                        .customer(KsBeanUtil.convert(customer, CustomerDTO.class)).build()).getContext()
                .getCommonLevelVOMap();
        List<PaidCardCustomerRelVO> relVOList = new ArrayList<>();
        if (Objects.nonNull(customer)) {
            relVOList = paidCardCustomerRelQueryProvider
                    .list(PaidCardCustomerRelListRequest.builder()
                            .delFlag(DeleteFlag.NO)
                            .endTimeBegin(LocalDateTime.now())
                            .customerId(customer.getCustomerId()).build())
                    .getContext().getPaidCardCustomerRelVOList();
        }
        // 查询加价购阶梯详情
        List<String> ids = confirmResponse.getTradeConfirmItems().stream().flatMap(i -> i.getTradeItems().stream())
                .map(t -> t.getSkuId()).collect(Collectors.toList());
        MarkupLevelBySkuRequest markupLevelBySkuRequest = MarkupLevelBySkuRequest.builder()
                .skuIds(ids).levelAmount(confirmResponse.getTotalPrice()).marketingJoinLevelList(new ArrayList<>()).build();


        // 付费会员
        if (CollectionUtils.isNotEmpty(relVOList)) {
            markupLevelBySkuRequest.getMarketingJoinLevelList().add(MarketingJoinLevel.PAID_CARD_CUSTOMER);
        }
        // 企业会员
        if (EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
            markupLevelBySkuRequest.getMarketingJoinLevelList().add(MarketingJoinLevel.ENTERPRISE_CUSTOMER);
        }
        markupLevelBySkuRequest.setLevelMap(commonLevelVOMap);
        MarkupLevelBySkuResponse levelBySkuResponse = markupQueryProvider.getMarkupListBySku(markupLevelBySkuRequest).getContext();
        List<MarkupLevelVO> levelList = levelBySkuResponse.getLevelList();
        if (CollectionUtils.isEmpty(levelList)) {
            return;
        }
        // 返回对象填充值
        Map<String, GoodsInfoVO> goodsInfoVOMap = levelBySkuResponse.getGoodsInfoVOList().stream().collect(Collectors.toMap(g -> g.getGoodsInfoId(), g -> g));
        Map<Long, List<String>> skuMap = levelList.stream().collect(Collectors.toMap(l -> l.getId(), l -> l.getSkuIds()));
        List<MarkupLevelDetailVO> markupLevelDetailVOS = levelList.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
                .map(d -> {
                    d.setGoodsInfo(goodsInfoVOMap.get(d.getGoodsInfoId()));
                    d.setSkuIds(skuMap.get(d.getMarkupLevelId()));
                    return d;
                }).collect(Collectors.toList());
        //
        List<String> skuIds = confirmResponse.getTradeConfirmItems().stream()
                .flatMap(i -> i.getTradeItems().stream())
                .map(i -> i.getSkuId()).collect(Collectors.toList());
        List<MarkupLevelDetailVO> markupLevelDetailVOList = markupLevelDetailVOS.stream().filter(d -> !skuIds.contains(d.getGoodsInfoId()))
                .collect(Collectors.toList());
        confirmResponse.setMarkupLevel(markupLevelDetailVOList);

    }

    private BigDecimal dealDistribution(List<TradeItemGroupVO> tradeItemGroupVOS, TradeConfirmResponse tradeConfirmResponse) {
        BigDecimal totalCommission = BigDecimal.ZERO;
        Operator operator = commonUtil.getOperator();
        if (distributionService.isDistributor(operator.getUserId())) {
            tradeConfirmResponse.setIsDistributor(DefaultFlag.YES);
        }
        if (CollectionUtils.isNotEmpty(tradeItemGroupVOS)) {
            for (TradeItemGroupVO tradeItemGroupVO : tradeItemGroupVOS) {
                if (DefaultFlag.NO.equals(tradeItemGroupVO.getStoreBagsFlag())) {
                    DistributeChannel channel = commonUtil.getDistributeChannel();
                    tradeItemGroupVO.getTradeItems().forEach(item -> {
                        DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId().toString());
                        if (DistributionGoodsAudit.CHECKED == item.getDistributionGoodsAudit()
                                && DefaultFlag.YES.equals(storeOpenFlag)
                                && ChannelType.PC_MALL != channel.getChannelType()) {
                            item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                            item.setPrice(item.getOriginalPrice());
                            item.setLevelPrice(item.getOriginalPrice());
                            // 初步计算分销佣金
                            item.setDistributionCommission(item.getSplitPrice().multiply(item.getCommissionRate()));
                        } else {
                            item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                        }
                    });

                    List<TradeItemVO> distributionTradeItems = tradeItemGroupVO.getTradeItems().stream()
                            .filter(item -> DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())).collect(Collectors.toList());

                    // 2.设置分销相关字段
                    if (distributionTradeItems.size() != 0) {

                        MultistageSettingGetResponse multistageSetting =
                                distributionCacheQueryProvider.getMultistageSetting().getContext();

                        // 2.1.查询佣金受益人列表
                        DistributionCustomerListForOrderCommitRequest request =
                                new DistributionCustomerListForOrderCommitRequest();
                        request.setBuyerId(commonUtil.getOperatorId());
                        request.setCommissionPriorityType(
                                com.wanmi.sbc.customer.bean.enums.CommissionPriorityType.fromValue(multistageSetting.getCommissionPriorityType().toValue())
                        );
                        request.setCommissionUnhookType(
                                CommissionUnhookType.fromValue(multistageSetting.getCommissionUnhookType().toValue())
                        );
                        request.setDistributorLevels(multistageSetting.getDistributorLevels());
                        request.setInviteeId(channel.getInviteeId());
                        request.setIsDistributor(tradeConfirmResponse.getIsDistributor());

                        List<DistributionCustomerSimVO> inviteeCustomers = distributionCustomerQueryProvider
                                .listDistributorsForOrderCommit(request).getContext().getDistributorList();

                        List<TradeDistributeItemVO> distributeItems = new ArrayList<>();

                        // 商品分销佣金map(记录每个分销商品基础分销佣金)
                        Map<String, BigDecimal> skuBaseCommissionMap = new HashMap<>();
                        distributionTradeItems.forEach(item ->
                                skuBaseCommissionMap.put(item.getSkuId(), item.getDistributionCommission())
                        );

                        // 2.2.根据受益人列表设置分销相关字段
                        if (CollectionUtils.isNotEmpty(inviteeCustomers)) {

                            for (int idx = 0; idx < inviteeCustomers.size(); idx++) {

                                DistributionCustomerSimVO customer = inviteeCustomers.get(idx);

                                DistributorLevelVO level = multistageSetting.getDistributorLevels().stream()
                                        .filter(l -> l.getDistributorLevelId().equals(customer.getDistributorLevelId())).findFirst().get();

                                if (idx == 0) {
                                    // 2.2.1.设置返利人信息
                                    distributionTradeItems.forEach(item -> {
                                        // 设置trade.tradeItems
                                        item.setDistributionCommission(
                                                DistributionCommissionUtils.calDistributionCommission(
                                                        item.getDistributionCommission(), level.getCommissionRate())
                                        );
                                        item.setCommissionRate(item.getCommissionRate().multiply(level.getCommissionRate()));

                                        // 设置trade.distributeItems
                                        TradeDistributeItemVO distributeItem = new TradeDistributeItemVO();
                                        distributeItem.setGoodsInfoId(item.getSkuId());
                                        distributeItem.setNum(item.getNum());
                                        distributeItem.setActualPaidPrice(item.getSplitPrice());
                                        distributeItem.setCommissionRate(item.getCommissionRate());
                                        distributeItem.setCommission(item.getDistributionCommission());
                                        distributeItems.add(distributeItem);
                                    });

                                    // 设置trade.[inviteeId,distributorId,distributorName,commission]
                                    tradeItemGroupVO.setCommission(
                                            distributeItems.stream().map(TradeDistributeItemVO::getCommission)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    );
                                    // 累加返利人佣金至总佣金
                                    totalCommission = totalCommission.add(tradeItemGroupVO.getCommission());

                                } else {
                                    // 2.2.2.设置提成人信息
                                    BigDecimal percentageTotal = BigDecimal.ZERO;
                                    for (int i = 0; i < distributeItems.size(); i++) {
                                        // 设置trade.distributeItems.commissions
                                        if (level != null && level.getPercentageRate() != null) {
                                            TradeDistributeItemVO item = distributeItems.get(i);
                                            TradeDistributeItemCommissionVO itemCommission = new TradeDistributeItemCommissionVO();
                                            itemCommission.setCustomerId(customer.getCustomerId());
                                            itemCommission.setDistributorId(customer.getDistributionId());
                                            itemCommission.setCommission(
                                                    skuBaseCommissionMap.get(item.getGoodsInfoId()).multiply(
                                                            level.getPercentageRate()).setScale(2, BigDecimal.ROUND_DOWN));
                                            if (CollectionUtils.isEmpty(item.getCommissions())) {
                                                item.setCommissions(new ArrayList<>());
                                            }
                                            item.getCommissions().add(itemCommission);
                                            percentageTotal = percentageTotal.add(itemCommission.getCommission());
                                        }
                                    }

                                    // 设置trade.commissions
                                    TradeCommissionVO tradeCommission = new TradeCommissionVO();
                                    tradeCommission.setCustomerId(customer.getCustomerId());
                                    tradeCommission.setCommission(percentageTotal);
                                    tradeCommission.setDistributorId(customer.getDistributionId());
                                    tradeCommission.setCustomerName(customer.getCustomerName());
                                    tradeItemGroupVO.getCommissions().add(tradeCommission);

                                    // 累加提成人佣金至总佣金
                                    totalCommission = totalCommission.add(tradeCommission.getCommission());
                                }

                            }
                        }
                    }
                }
            }
        }
        return totalCommission;
    }


    /**
     * 用于确认订单后，创建订单前的获取订单商品信息
     */
    @ApiOperation(value = "用于立即购买，确认订单中更新商品购买数量")
    @RequestMapping(value = "/modifyGoodsNumForConfirm", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse<TradeConfirmResponse> modifyGoodsNumForConfirm(@RequestBody @Valid TradeItemConfirmModifyGoodsNumRequest request) {
        TradeConfirmResponse confirmResponse = new TradeConfirmResponse();
        String customerId = commonUtil.getOperatorId();
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(
                new CustomerGetByIdRequest(customerId)).getContext();
        GoodsInfoResponse skuResp = getGoodsResponse(Collections.singletonList(request.getGoodsInfoId()), customer);
        if (CollectionUtils.isEmpty(skuResp.getGoodsInfos())) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        //商品验证
        List<TradeItemDTO> itemDTOS = Collections.singletonList(TradeItemDTO.builder().skuId(request.getGoodsInfoId())
                .num(request.getBuyCount()).build());
        List<TradeItemVO> itemVOList = verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(itemDTOS, Collections.emptyList(),
                KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class), skuResp.getGoodsInfos().get(0).getStoreId(), true)).getContext().getTradeItems();

        //更新快照
        com.wanmi.sbc.order.api.request.trade.TradeItemModifyGoodsNumRequest numRequest = com.wanmi.sbc.order.api.request.trade.TradeItemModifyGoodsNumRequest.builder().customerId(customerId).terminalToken(commonUtil.getTerminalToken())
                .num(request.getBuyCount()).skuId(request.getGoodsInfoId()).skuList(KsBeanUtil.convertList(skuResp.getGoodsInfos(), GoodsInfoDTO.class)).build();
        List<TradeItemGroupVO> tradeItemGroups = tradeItemProvider.modifyGoodsNum(numRequest).getContext().getTradeItemGroupList();

        List<TradeConfirmItemVO> items = new ArrayList<>();
        Map<Long, StoreVO> storeMap = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
                (tradeItemGroups.stream().map(g -> g
                        .getSupplier().getStoreId())
                        .collect(Collectors.toList()))).getContext().getStoreVOList().stream().collect(Collectors
                .toMap(StoreVO::getStoreId,
                        s -> s));
        // 如果为PC商城下单，将分销商品变为普通商品
        if (ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType())) {
            tradeItemGroups.forEach(tradeItemGroup ->
                    tradeItemGroup.getTradeItems().forEach(tradeItem -> {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    })
            );
            skuResp.getGoodsInfos().forEach(item -> item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS));
        }

        //企业会员判断
        boolean isIepCustomerFlag = isIepCustomer(customer);
        tradeItemGroups.forEach(
                g -> {
                    g.getSupplier().setFreightTemplateType(storeMap.get(g.getSupplier().getStoreId())
                            .getFreightTemplateType());
                    // 分销商品、开店礼包商品、拼团商品、企业购商品，不验证起限定量
                    skuResp.getGoodsInfos().forEach(item -> {
                        //企业购商品
                        boolean isIep = isIepCustomerFlag && isEnjoyIepGoodsInfo(item.getEnterPriseAuditState());
                        if (DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                || DefaultFlag.YES.equals(g.getStoreBagsFlag())
                                || Objects.nonNull(g.getGrouponForm()) || isIep) {
                            item.setCount(null);
                            item.setMaxCount(null);
                        }
                    });

                    //企业购商品价格回设
                    if (isIepCustomerFlag) {
                        itemVOList.forEach(tradeItemVO -> {
                            if (isEnjoyIepGoodsInfo(tradeItemVO.getEnterPriseAuditState())) {
                                tradeItemVO.setPrice(tradeItemVO.getEnterPrisePrice());
                                tradeItemVO.setSplitPrice(tradeItemVO.getEnterPrisePrice().multiply(new BigDecimal(tradeItemVO.getNum())));
                                tradeItemVO.setLevelPrice(tradeItemVO.getEnterPrisePrice());
                            }
                        });
                    }

                    //抢购商品价格回设
                    if (StringUtils.isNotBlank(g.getSnapshotType()) && g.getSnapshotType().equals(Constants.FLASH_SALE_GOODS_ORDER_TYPE)) {
                        itemVOList.forEach(tradeItemVO -> {
                            g.getTradeItems().forEach(tradeItem -> {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                }
                            });
                        });
                    }
                    itemVOList.forEach(tradeItemVO -> {
                        g.getTradeItems().forEach(tradeItem -> {
                            if ((Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getPrice());
                                    tradeItemVO.setIsAppointmentSaleGoods(tradeItem.getIsAppointmentSaleGoods());
                                    tradeItemVO.setAppointmentSaleId(tradeItem.getAppointmentSaleId());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.FULL_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getOriginalPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                }
                            }
                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.EARNEST_MONEY) {
                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
                                    tradeItemVO.setPrice(tradeItem.getOriginalPrice());
                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
                                    tradeItemVO.setHandSelStartTime(tradeItem.getHandSelStartTime());
                                    tradeItemVO.setHandSelEndTime(tradeItem.getHandSelEndTime());
                                    tradeItemVO.setTailStartTime(tradeItem.getTailStartTime());
                                    tradeItemVO.setTailEndTime(tradeItem.getTailEndTime());
                                    tradeItemVO.setEarnestPrice(tradeItem.getEarnestPrice());
                                    tradeItemVO.setSwellPrice(tradeItem.getSwellPrice());
                                }
                            }
                        });
                    });
                    g.setTradeItems(itemVOList);
                    // 分销商品、开店礼包商品，重新设回市场价
                    if (DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())
                            && !ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType())) {
                        g.getTradeItems().forEach(item -> {
                            DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId()
                                    .toString());
                            if ((Objects.isNull(item.getIsFlashSaleGoods()) ||
                                    (Objects.nonNull(item.getIsFlashSaleGoods()) && !item.getIsFlashSaleGoods())) &&
                                    (Objects.isNull(item.getIsAppointmentSaleGoods()) || !item.getIsAppointmentSaleGoods()) &&
                                    !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods() && item.getBookingType() == BookingType.FULL_MONEY) &&
                                    DefaultFlag.YES.equals(storeOpenFlag) && (
                                    DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                                            || DefaultFlag.YES.equals(g.getStoreBagsFlag()))) {
                                item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                                item.setPrice(item.getOriginalPrice());
                                item.setLevelPrice(item.getOriginalPrice());
                            } else {
                                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                            }
                        });
                    }

                    confirmResponse.setStoreBagsFlag(g.getStoreBagsFlag());
                    items.add(tradeQueryProvider.queryPurchaseInfo(TradeQueryPurchaseInfoRequest.builder()
                            .tradeItemGroupDTO(KsBeanUtil.convert(g, TradeItemGroupDTO.class))
                            .tradeItemList(Collections.emptyList()).build()).getContext().getTradeConfirmItemVO());
                }
        );

        // 验证小店商品
        this.validShopGoods(tradeItemGroups, commonUtil.getDistributeChannel());
        //设置购买总积分
        confirmResponse.setTotalBuyPoint(items.stream().flatMap(i -> i.getTradeItems().stream()).mapToLong(v -> Objects.isNull(v.getBuyPoint()) ? 0 : v.getBuyPoint() * v.getNum()).sum());

        confirmResponse.setTradeConfirmItems(items);

        // 设置小店名称、返利总价
        confirmResponse.setShopName(distributionCacheService.getShopName());
        BigDecimal totalCommission = items.stream().flatMap(i -> i.getTradeItems().stream())
                .filter(i -> DistributionGoodsAudit.CHECKED.equals(i.getDistributionGoodsAudit()))
                .filter(i -> i.getDistributionCommission() != null)
                .map(i -> i.getDistributionCommission().multiply(new BigDecimal(i.getNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        confirmResponse.setTotalCommission(totalCommission);

        // 设置邀请人名字
        if (StringUtils.isNotEmpty(commonUtil.getDistributeChannel().getInviteeId())) {
            DistributionCustomerByCustomerIdRequest idRequest = new DistributionCustomerByCustomerIdRequest();
            idRequest.setCustomerId(commonUtil.getDistributeChannel().getInviteeId());
            DistributionCustomerVO distributionCustomer = distributionCustomerQueryProvider.getByCustomerId(idRequest)
                    .getContext().getDistributionCustomerVO();
            if (distributionCustomer != null) {
                confirmResponse.setInviteeName(distributionCustomer.getCustomerName());
            }
        }

        // 校验拼团信息
        validGrouponOrder(confirmResponse, tradeItemGroups, customerId);
        // 根据确认订单计算出的信息，查询出使用优惠券页需要的优惠券列表数据
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        List<TradeItemInfoDTO> tradeDtos = items.stream().flatMap(confirmItem ->
                confirmItem.getTradeItems().stream().map(tradeItem -> {
                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
                    dto.setBrandId(tradeItem.getBrand());
                    dto.setCateId(tradeItem.getCateId());
                    dto.setSpuId(tradeItem.getSpuId());
                    dto.setSkuId(tradeItem.getSkuId());
                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
                    dto.setPrice(tradeItem.getSplitPrice());
                    dto.setDistributionGoodsAudit(tradeItem.getDistributionGoodsAudit());
                    if (DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(
                            distributionCacheService.queryStoreOpenFlag(String.valueOf(tradeItem.getStoreId())))) {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    }
                    return dto;
                })
        ).collect(Collectors.toList());

        CouponCodeListForUseByCustomerIdRequest requ = CouponCodeListForUseByCustomerIdRequest.builder()
                .customerId(customerId)
                .tradeItems(tradeDtos).build();
        confirmResponse.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(requ).getContext()
                .getCouponCodeList());

        return BaseResponse.success(confirmResponse);
    }


    /*
     *  组合购活动信息校验\处理tradeItem
     * @param response
     * @param tradeItemGroups
     * @param customerId
     */
    private void dealSuitOrder(
            TradeConfirmResponse response, List<TradeItemGroupVO> tradeItemGroups, String customerId) {
        TradeItemGroupVO tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        // 组合购标记
        if (Objects.equals(tradeItemGroup.getSuitMarketingFlag(), Boolean.TRUE) && CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList())) {
            // 获取并校验组合购活动信息
            MarketingSuitsValidRequest marketingSuitsValidRequest = new MarketingSuitsValidRequest();
            marketingSuitsValidRequest.setMarketingId(tradeItemGroup.getTradeMarketingList().get(NumberUtils.INTEGER_ZERO).getMarketingId());
            marketingSuitsValidRequest.setBaseStoreId(Constants.BOSS_DEFAULT_STORE_ID);
            marketingSuitsValidRequest.setUserId(commonUtil.getOperatorId());
            BaseResponse<MarketingSuitsValidResponse> marketingSuits = marketingSuitsQueryProvider.validSuitOrderBeforeCommit(marketingSuitsValidRequest);
            List<MarketingSuitsSkuVO> marketingSuitsSkuVOList = marketingSuits.getContext().getMarketingSuitsSkuVOList();

            confirmItem.getTradeItems().stream().filter(i -> !Boolean.TRUE.equals(i.getIsMarkupGoods())).forEach(tradeItemVO -> {
                MarketingSuitsSkuVO suitsSku = marketingSuitsSkuVOList.stream().filter(sku -> Objects.equals(sku.getSkuId(), tradeItemVO.getSkuId())
                ).findFirst().orElse(new MarketingSuitsSkuVO());
                //设置组合购商品价格
                BigDecimal discountPrice = suitsSku.getDiscountPrice();
                BigDecimal splitPrice = discountPrice.multiply(new BigDecimal(suitsSku.getNum()));
                tradeItemVO.setSplitPrice(splitPrice);
                tradeItemVO.setPrice(discountPrice);
                tradeItemVO.setLevelPrice(discountPrice);
                tradeItemVO.setBuyPoint(NumberUtils.LONG_ZERO);
            });

            BigDecimal goodsPrice = confirmItem.getTradeItems().stream().map(TradeItemVO::getSplitPrice).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            ;
            confirmItem.getTradePrice().setGoodsPrice(goodsPrice);
            confirmItem.getTradePrice().setTotalPrice(goodsPrice);
            response.setTotalBuyPoint(NumberUtils.LONG_ZERO);
        }
    }

    private void validGrouponOrder(
            TradeConfirmResponse response, List<TradeItemGroupVO> tradeItemGroups, String customerId) {
        TradeItemGroupVO tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        TradeItemVO item = tradeItemGroup.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        TradeItemVO resItem = confirmItem.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        if (Objects.nonNull(tradeItemGroup.getGrouponForm())) {

            TradeGrouponCommitFormVO grouponForm = tradeItemGroup.getGrouponForm();

            if (!DistributionGoodsAudit.COMMON_GOODS.equals(item.getDistributionGoodsAudit())) {
                log.error("拼团单，不能下分销商品");
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            // 1.校验拼团商品
            GrouponGoodsInfoVO grouponGoodsInfo = grouponProvider.validGrouponOrderBeforeCommit(
                    GrouponOrderValidRequest.builder()
                            .buyCount(item.getNum().intValue()).customerId(customerId).goodsId(item.getSpuId())
                            .goodsInfoId(item.getSkuId())
                            .grouponNo(grouponForm.getGrouponNo())
                            .openGroupon(grouponForm.getOpenGroupon())
                            .build()).getContext().getGrouponGoodsInfo();

            if (Objects.isNull(grouponGoodsInfo)) {
                log.error("拼团单下的不是拼团商品");
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            // 2.设置拼团活动信息
            boolean freeDelivery = grouponActivityQueryProvider.getFreeDeliveryById(
                    new GrouponActivityFreeDeliveryByIdRequest(grouponGoodsInfo.getGrouponActivityId())).getContext().isFreeDelivery();
            response.setOpenGroupon(grouponForm.getOpenGroupon());
            response.setGrouponFreeDelivery(freeDelivery);

            // 3.设成拼团价
            BigDecimal grouponPrice = grouponGoodsInfo.getGrouponPrice();
            BigDecimal splitPrice = grouponPrice.multiply(new BigDecimal(item.getNum()));
            resItem.setSplitPrice(splitPrice);
            resItem.setPrice(grouponPrice);
            resItem.setLevelPrice(grouponPrice);
            resItem.setBuyPoint(NumberUtils.LONG_ZERO);
            TradePriceVO tradePrice = confirmItem.getTradePrice();
            tradePrice.setGoodsPrice(splitPrice);
            tradePrice.setTotalPrice(splitPrice);
            tradePrice.setBuyPoints(NumberUtils.LONG_ZERO);
            response.setTotalBuyPoint(NumberUtils.LONG_ZERO);
        }
    }

    /*
     * 仅用于立即购买后的确认订单页面，满系活动信息封装
     * @param response
     */
    private void fillMarketingInfo(TradeConfirmResponse response, List<TradeItemGroupVO> tradeItemGroups) {
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        TradeItemGroupVO tradeItemGroupVO = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        List<TradeItemVO> items = confirmItem.getTradeItems();
        List<Long> marketingIds = items.stream().filter(v -> CollectionUtils.isNotEmpty(v.getMarketingIds()))
                .flatMap(v -> v.getMarketingIds().stream()).collect(Collectors.toList());
        List<Long> marketingLevelIds = items.stream().filter(v -> CollectionUtils.isNotEmpty(v.getMarketingLevelIds()))
                .flatMap(v -> v.getMarketingLevelIds().stream()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(marketingIds) || CollectionUtils.isEmpty(marketingLevelIds)) {
            return;
        }
        Map<Long, TradeItemMarketingVO> marketingVOMap = tradeItemGroupVO.getTradeMarketingList()
                .stream().collect(Collectors.toMap(v -> v.getMarketingId(), v -> v));
        List<TradeConfirmMarketingVO> tradeConfirmMarketingList = new ArrayList<>();

        //批量获取满系营销以及规则信息
        MarketingViewQueryByIdsRequest idsRequest = new MarketingViewQueryByIdsRequest();
        idsRequest.setMarketingIds(marketingIds);
        idsRequest.setLevelFlag(true);
        List<MarketingViewVO> marketingViewVOS = marketingQueryProvider.queryViewByIds(idsRequest).getContext().getMarketingViewList();
        if (CollectionUtils.isNotEmpty(marketingViewVOS)) {
            Map<Long, MarketingViewVO> viewMap = marketingViewVOS.stream().collect(Collectors.toMap(MarketingViewVO::getMarketingId, m -> m));
            DecimalFormat fmt = new DecimalFormat("#.##");
            items.stream().filter(i -> CollectionUtils.isNotEmpty(i.getMarketingIds())).forEach(i -> {

                for (Long marketingId : i.getMarketingIds()) {
                    MarketingViewVO marketingViewVO = viewMap.get(marketingId);
                    Long levelId = marketingVOMap.get(marketingId).getMarketingLevelId();
                    if (marketingViewVO != null) {
                        TradeConfirmMarketingVO confirmMarketingVO = new TradeConfirmMarketingVO();
                        confirmMarketingVO.setMarketingId(marketingViewVO.getMarketingId());
                        confirmMarketingVO.setSkuIds(Collections.singletonList(i.getSkuId()));
                        confirmMarketingVO.setMarketingLevelId(levelId);
                        confirmMarketingVO.setMarketingType(marketingViewVO.getMarketingType().toValue());
                        String desc = "该营销不存在";
                        if (MarketingType.REDUCTION.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullReductionLevelVO levelVO = marketingViewVO.getFullReductionLevelList().stream()
                                    .filter(l -> l.getReductionLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s减%s", fmt.format(levelVO.getFullAmount()), fmt.format(levelVO.getReduction()));
                                } else {
                                    desc = String.format("您已满足满%s件减%s", levelVO.getFullCount(), fmt.format(levelVO.getReduction()));
                                }
                            }
                        } else if (MarketingType.DISCOUNT.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullDiscountLevelVO levelVO = marketingViewVO.getFullDiscountLevelList().stream()
                                    .filter(l -> l.getDiscountLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s享%s折", fmt.format(levelVO.getFullAmount()), fmt.format(levelVO.getDiscount().multiply(new BigDecimal(10))));
                                } else {
                                    desc = String.format("您已满足满%s件享%s折", levelVO.getFullCount(), fmt.format(levelVO.getDiscount().multiply(new BigDecimal(10))));
                                }
                            }
                        } else if (MarketingType.GIFT.equals(marketingViewVO.getMarketingType())) {
                            MarketingFullGiftLevelVO levelVO = marketingViewVO.getFullGiftLevelList().stream()
                                    .filter(l -> l.getGiftLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (MarketingSubType.GIFT_FULL_AMOUNT.equals(marketingViewVO.getSubType())) {
                                    desc = String.format("您已满足满%s获赠品", fmt.format(levelVO.getFullAmount()));
                                } else {
                                    desc = String.format("您已满足满%s件获赠品", levelVO.getFullCount());
                                }
                                confirmMarketingVO.setGiftSkuIds(levelVO.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList()));
                            }
                        } else if (MarketingType.BUYOUT_PRICE.equals(marketingViewVO.getMarketingType())) {
                            MarketingBuyoutPriceLevelVO levelVO = marketingViewVO.getBuyoutPriceLevelList().stream()
                                    .filter(l -> l.getReductionLevelId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                desc = String.format("您已满足%s件%s元", levelVO.getChoiceCount(), fmt.format(levelVO.getFullAmount()));
                            }
                        } else if (MarketingType.HALF_PRICE_SECOND_PIECE.equals(marketingViewVO.getMarketingType())) {
                            MarketingHalfPriceSecondPieceLevelVO levelVO = marketingViewVO.getHalfPriceSecondPieceLevel().stream()
                                    .filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                if (BigDecimal.ZERO.compareTo(levelVO.getDiscount()) == 0) {
                                    desc = String.format("您已满足买%s送1", levelVO.getNumber() - 1);
                                } else {
                                    desc = String.format("您已满足第%s件%s折", levelVO.getNumber(), fmt.format(levelVO.getDiscount()));
                                }
                                confirmMarketingVO.setHalfPriceSecondPieceLevel(levelVO);
                            }
                        } else if (MarketingType.MARKUP.equals(marketingViewVO.getMarketingType())) {
                            MarkupLevelVO levelVO = marketingViewVO.getMarkupLevelList().stream()
                                    .filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if (levelVO != null) {
                                desc = String.format("您已满足%s加价购", levelVO.getLevelAmount());
                                confirmMarketingVO.setMarkupLevelVO(levelVO);
                            }
                        } else if(MarketingType.POINT_BUY.equals(marketingViewVO.getMarketingType())) {
                            List<MarketingPointBuyLevelVO> pointBuyLevelList = marketingViewVO.getPointBuyLevelList();
                            List<TradeItemVO> tradeItems = tradeItemGroups.get(0).getTradeItems();
                            boolean got = false;
                            for (MarketingPointBuyLevelVO levelVO : pointBuyLevelList) {
                                if(got) break;
                                for (TradeItemVO tradeItem : tradeItems) {
                                    if(levelVO.getSkuId().equals(tradeItem.getSkuId())){
                                        desc = String.format("您已满足%s积分+%s元换购", levelVO.getPointNeed(), levelVO.getMoney());
                                        confirmMarketingVO.setPointNeed(levelVO.getPointNeed());
                                        confirmMarketingVO.setMoney(levelVO.getMoney());
                                        got = true;
                                        break;
                                    }
                                }
                            }
                            /*MarketingPointBuyLevelVO levelVO = marketingViewVO.getPointBuyLevelList().stream().filter(l -> l.getId().equals(levelId)).findFirst().orElse(null);
                            if(levelVO != null){
                                desc = String.format("您已满足%s积分+%s元换购", levelVO.getPointNeed(), levelVO.getMoney());
                                confirmMarketingVO.setPointNeed(levelVO.getPointNeed());
                                confirmMarketingVO.setMoney(levelVO.getMoney());
                            }*/
                        }
                        if (!MarketingType.SUITS.equals(marketingViewVO.getMarketingType())) {
                            confirmMarketingVO.setMarketingDesc(desc);
                        }
                        tradeConfirmMarketingList.add(confirmMarketingVO);
                    }
                }
            });
        }

        confirmItem.setTradeConfirmMarketingList(tradeConfirmMarketingList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TradeConfirmMarketingVO::getMarketingId))), ArrayList::new)));
    }

    /**
     * 填充商品状态
     *
     * @param items
     */
    public void setGoodsStatus(List<TradeConfirmItemVO> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            items.forEach(item -> {
                List<TradeItemVO> gifts = item.getGifts();
                if (CollectionUtils.isNotEmpty(gifts)) {
                    Map<String, GoodsStatus> statusMap = new HashMap<>();
                    List<String> skuIds = gifts.stream().map(TradeItemVO::getSkuId).collect(Collectors.toList());
                    List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(skuIds).build())
                            .getContext().getGoodsInfos();
                    if (CollectionUtils.isNotEmpty(goodsInfos)) {
                        statusMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g.getGoodsStatus(), (k1, k2) -> k1));
                        for (TradeItemVO gift : gifts) {
                            gift.setGoodsStatus(statusMap.get(gift.getSkuId()));
                        }
                    }
                }
            });
        }
    }

    /**
     * 根据参数查询某订单的运费
     * @param tradeParams
     * @return
     */
    @ApiOperation(value = "根据参数查询某订单的运费")
    @RequestMapping(value = "/getFreight", method = RequestMethod.POST)
    public BaseResponse<List<TradeGetFreightResponse>> getFreight(@RequestBody List<TradeParamsRequest> tradeParams) {
        List<TradeGetFreightResponse> list = new ArrayList<>();
        for (TradeParamsRequest tradeParam : tradeParams) {
            BaseResponse<TradeGetFreightResponse> freight = tradeQueryProvider.getFreight(tradeParam);
            if(CommonErrorCode.SUCCESSFUL.equals(freight.getCode()) && freight.getContext() == null) {
                return BaseResponse.info(CommonErrorCode.FAILED, "所选地区不支持配送");
            }
            list.add(freight.getContext());
        }
        return BaseResponse.success(list);
    }

    /**
     * 我的拼购分页查询订单
     */
    @ApiOperation(value = "我的拼购分页查询订单")
    @RequestMapping(value = "/page/groupons", method = RequestMethod.POST)
    public BaseResponse<Page<GrouponTradeVO>> grouponPage(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .buyerId(commonUtil.getOperatorId())
                .grouponFlag(Boolean.TRUE)
                .isBoss(false)
                .build();
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());
        Page<TradeVO> tradePage = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        List<GrouponTradeVO> tradeReponses = new ArrayList<>();
        tradePage.getContent().forEach(info -> {
            GrouponTradeVO tradeReponse = KsBeanUtil.convert(info, GrouponTradeVO.class);
            //待成团-获取团实例
            if (GrouponOrderStatus.WAIT.equals(tradeReponse.getTradeGroupon().getGrouponOrderStatus())
                    && PayState.PAID.equals(tradeReponse.getTradeState().getPayState())
            ) {
                GrouponInstanceByGrouponNoRequest request = GrouponInstanceByGrouponNoRequest.builder().grouponNo(info
                        .getTradeGroupon().getGrouponNo()).build();
                tradeReponse.setGrouponInstance(grouponInstanceQueryProvider.detailByGrouponNo(request).getContext
                        ().getGrouponInstance());
            }
            tradeReponses.add(tradeReponse);
        });

        return BaseResponse.success(new PageImpl<>(tradeReponses, tradeQueryRequest.getPageable(),
                tradePage.getTotalElements()));
    }

    /**
     * 分页查询订单
     */
    @ApiOperation(value = "分页查询订单")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> page(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder()
                        .flowState(paramRequest.getFlowState())
                        .payState(paramRequest.getPayState())
                        .deliverStatus(paramRequest.getDeliverStatus())
                        .build())
                .buyerId(commonUtil.getOperatorId())
                .inviteeId(paramRequest.getInviteeId())
                .channelType(paramRequest.getChannelType())
                .beginTime(paramRequest.getCreatedFrom())
                .endTime(paramRequest.getCreatedTo())
                .keyworks(paramRequest.getKeywords())
                .isBoss(false)
                .build();
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();

        Page<TradeVO> tradePage = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        final boolean flag = config.getStatus() == 1;
        int days = JSONObject.parseObject(config.getContext()).getInteger("day");
        List<TradeVO> tradeReponses = new ArrayList<>();
        tradePage.getContent().forEach(info -> {
            TradeVO tradeReponse = new TradeVO();
            BeanUtils.copyProperties(info, tradeReponse);
            TradeStateVO tradeState = tradeReponse.getTradeState();
            boolean canReturnFlag =
                    tradeState.getFlowState() == FlowState.COMPLETED || (tradeState.getPayState() == PayState.PAID
                            && tradeState.getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED && tradeState
                            .getFlowState() != FlowState.VOID);
            canReturnFlag = isCanReturnTime(flag, days, tradeState, canReturnFlag);
            //开店礼包不支持退货退款
            canReturnFlag = canReturnFlag && DefaultFlag.NO == tradeReponse.getStoreBagsFlag();
            tradeReponse.setCanReturnFlag(canReturnFlag);

            if (Objects.nonNull(tradeReponse.getIsBookingSaleGoods()) && tradeReponse.getIsBookingSaleGoods() &&
                    tradeReponse.getBookingType() == BookingType.EARNEST_MONEY &&
                    tradeReponse.getTradeState().getPayState() == PayState.NOT_PAID) {
                tradeReponse.getTradePrice().setTotalPrice(tradeReponse.getTradePrice().getEarnestPrice());
            }
            if (Objects.nonNull(tradeReponse.getIsBookingSaleGoods()) && tradeReponse.getIsBookingSaleGoods()
                    && tradeReponse.getBookingType() == BookingType.EARNEST_MONEY && tradeReponse.getTradeState().getPayState() == PayState.PAID_EARNEST) {
                tradeReponse.getTradePrice().setTotalPrice(tradeReponse.getTradePrice().getTailPrice());
            }

            FindProviderTradeResponse findProviderTradeResponse = providerTradeProvider.findByParentIdList(FindProviderTradeRequest.builder().parentId(Arrays.asList(info.getId())).build()).getContext();
            tradeReponse.setTradeVOList(findProviderTradeResponse.getTradeVOList());
            tradeReponses.add(tradeReponse);
        });
        return BaseResponse.success(new PageImpl<>(tradeReponses, tradeQueryRequest.getPageable(),
                tradePage.getTotalElements()));
    }


    /**
     * 分页查询客户订单
     */
    @ApiOperation(value = "分页查询客户订单")
    @RequestMapping(value = "/customer/page", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> customerOrderPage(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder()
                        .flowState(paramRequest.getFlowState())
                        .payState(paramRequest.getPayState())
                        .deliverStatus(paramRequest.getDeliverStatus())
                        .build())
                .inviteeId(commonUtil.getOperatorId())
                .channelType(paramRequest.getChannelType())
                .beginTime(paramRequest.getCreatedFrom())
                .endTime(paramRequest.getCreatedTo())
                .keyworks(paramRequest.getKeywords())
                .customerOrderListAllType(paramRequest.isCustomerOrderListAllType())
                .isBoss(false)
                .build();
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());

        //设定状态条件逻辑,需筛选出已支付下已审核与部分发货订单
        tradeQueryRequest.setPayedAndAudit();

        Page<TradeVO> tradePage = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        final boolean flag = config.getStatus() == 1;
        int days = JSONObject.parseObject(config.getContext()).getInteger("day");
        List<TradeVO> tradeReponses = new ArrayList<>();
        tradePage.getContent().forEach(info -> {
            TradeVO tradeReponse = new TradeVO();
            BeanUtils.copyProperties(info, tradeReponse);
            TradeStateVO tradeState = tradeReponse.getTradeState();
            boolean canReturnFlag =
                    (tradeState.getPayState() == PayState.PAID || tradeState.getFlowState() == FlowState.COMPLETED
                            && tradeState.getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED && tradeState
                            .getFlowState() != FlowState.VOID);
            canReturnFlag = isCanReturnTime(flag, days, tradeState, canReturnFlag);
            //开店礼包不支持退货退款
            canReturnFlag = canReturnFlag && DefaultFlag.NO == tradeReponse.getStoreBagsFlag();
            tradeReponse.setCanReturnFlag(canReturnFlag);
            //待发货状态下排除未支付、待确认订单
            if (!((tradeState.getPayState() == PayState.NOT_PAID
                    || tradeState.getPayState() == PayState.UNCONFIRMED) && (tradeState.getDeliverStatus() ==
                    DeliverStatus.NOT_YET_SHIPPED || DeliverStatus.SHIPPED == tradeState.getDeliverStatus()))) {
                tradeReponses.add(tradeReponse);
            }
        });
        return BaseResponse.success(new PageImpl<>(tradeReponses, tradeQueryRequest.getPageable(),
                tradePage.getTotalElements()));
    }


    /**
     * 查询订单商品清单
     */
    @ApiOperation(value = "查询订单商品清单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/goods/{tid}", method = RequestMethod.GET)
    public BaseResponse<List<TradeItemVO>> tradeItems(@PathVariable String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        checkUnauthorized(tid, trade);
        return BaseResponse.success(trade.getTradeItems());
    }

    /**
     * 查询订单发货清单
     */
    @ApiOperation(value = "查询订单发货清单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/deliverRecord/{tid}/{type}", method = RequestMethod.GET)
    public BaseResponse<TradeDeliverRecordResponse> tradeDeliverRecord(@PathVariable String tid, @PathVariable String
            type) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).needLmOrder(Boolean.TRUE).build()).getContext().getTradeVO();
        //订单列表做验证,客户订单列表无需验证
        if ("0".equals(type)) {
            checkUnauthorized(tid, trade);
        }

        TradeDeliverRecordResponse tradeDeliverRecord = TradeDeliverRecordResponse.builder()
                .status(trade.getTradeState().getFlowState().getStateId())
                .tradeDeliver(trade.getTradeDelivers())
                .yzTid(trade.getYzTid())
                .cycleBuyFlag(trade.getCycleBuyFlag())
                .build();
        return BaseResponse.success(tradeDeliverRecord);
    }

    /**
     * 获取订单发票信息
     */
    @ApiOperation(value = "获取订单发票信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "type", value = "主客订单TYPE", required =
                    true)
    })

    @RequestMapping(value = "/invoice/{tid}/{type}", method = RequestMethod.GET)
    public BaseResponse<InvoiceVO> invoice(@PathVariable String tid, @PathVariable String type) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        if ("0".equals(type)) {
            checkUnauthorized(tid, trade);
        }
        InvoiceVO invoice = trade.getInvoice();
        //若无发票收货地址，则默认为订单收货地址
        if (invoice.getAddress() == null) {
            InvoiceVO.builder()
                    .address(trade.getConsignee().getDetailAddress())
                    .contacts(trade.getConsignee().getName())
                    .phone(trade.getConsignee().getPhone())
                    .build();
        }
        return BaseResponse.success(invoice);
    }

    /**
     * 查询订单附件信息，只做展示使用
     */
    @ApiOperation(value = "查询订单附件信息，只做展示使用")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/encloses/{tid}", method = RequestMethod.GET)
    public BaseResponse<String> encloses(@PathVariable String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        checkUnauthorized(tid, trade);
        return BaseResponse.success(trade.getEncloses());
    }


    /**
     * @description 查询订单付款记录
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "查询订单付款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/payOrder/{tid}", method = RequestMethod.GET)
    public BaseResponse<FindPayOrderResponse> payOrder(@PathVariable String tid) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        //这里注释掉是为了客户订单
        //checkUnauthorized(tid, trade);

        FindPayOrderResponse payOrderResponse = null;
        try {

            BaseResponse<FindPayOrderResponse> response =
                    payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(trade.getId()).build());

            payOrderResponse = response.getContext();


        } catch (SbcRuntimeException e) {
            if ("K-070001".equals(e.getErrorCode())) {
                payOrderResponse = new FindPayOrderResponse();
                payOrderResponse.setPayType(PayType.fromValue(Integer.valueOf(trade.getPayInfo().getPayTypeId())));
                payOrderResponse.setTotalPrice(trade.getTradePrice().getTotalPrice());
            }
        }
        if (Objects.nonNull(trade.getTradeGroupon())) {
            payOrderResponse.setGrouponNo(trade.getTradeGroupon().getGrouponNo());
        }
        payOrderResponse.setPayOrderPrice(trade.getTradePrice().getTotalPayCash());
        payOrderResponse.setStoreName(trade.getSupplier().getStoreName());
        payOrderResponse.setIsSelf(trade.getSupplier().getIsSelf());
        payOrderResponse.setBuyPoints(trade.getTradePrice().getBuyPoints());
        payOrderResponse.setBuyKnowledge(trade.getTradePrice().getBuyKnowledge());
        // 订单流程状态
        payOrderResponse.setFlowState(trade.getTradeState().getFlowState());
        return BaseResponse.success(payOrderResponse);
    }

    /**
     * 根据父订单查询子订单付款记录
     */
    @ApiOperation(value = "查询订单付款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "parentTid", value = "父订单ID", required = true)
    @RequestMapping(value = "/payOrders/{parentTid}", method = RequestMethod.GET)
    public BaseResponse<FindPayOrderListResponse> payOrders(@PathVariable String parentTid) {
        return payOrderQueryProvider.findPayOrderList(new FindPayOrderListRequest(parentTid));
    }

    /**
     * 查询尾款订单付款记录
     */
    @ApiOperation(value = "查询尾款订单付款记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tailOrderNo", value = "父订单ID", required = true)
    @RequestMapping(value = "/payOrderByTailOrderNo/{parentTid}", method = RequestMethod.GET)
    public BaseResponse<FindPayOrderResponse> payOrderByTailOrderNo(@PathVariable String parentTid) {

        List<TradeVO> tradeVOList = tradeQueryProvider.getListByParentId(TradeListByParentIdRequest.builder().parentTid(parentTid).build()).getContext().getTradeVOList();
        TradeVO trade = null;
        FindPayOrderResponse payOrderResponse = null;
        if (CollectionUtils.isNotEmpty(tradeVOList)) {
            trade = tradeVOList.get(0);
            try {
                BaseResponse<FindPayOrderResponse> response =
                        payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(trade.getTailOrderNo()).build());
                payOrderResponse = response.getContext();
            } catch (SbcRuntimeException e) {
                if ("K-070001".equals(e.getErrorCode())) {
                    payOrderResponse = new FindPayOrderResponse();
                    payOrderResponse.setPayType(PayType.fromValue(Integer.valueOf(trade.getPayInfo().getPayTypeId())));
                    payOrderResponse.setTotalPrice(trade.getTradePrice().getTotalPrice());
                }
            }
            payOrderResponse.setStoreName(trade.getSupplier().getStoreName());
            payOrderResponse.setIsSelf(trade.getSupplier().getIsSelf());
            // 订单流程状态
            payOrderResponse.setFlowState(trade.getTradeState().getFlowState());

        }
        return BaseResponse.success(payOrderResponse);
    }

    /**
     * 根据订单号与物流单号查询发货信息
     */
    @ApiOperation(value = "根据订单号与物流单号查询发货信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "tid", value = "订单ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "deliverId", value = "发货单号", required = true)
    })
    @RequestMapping(value = "/shipments/{tid}/{deliverId}/{type}", method = RequestMethod.GET)
    public BaseResponse<TradeDeliverVO> shippItemsByLogisticsNo(@PathVariable String tid, @PathVariable String
            deliverId, @PathVariable String type) {
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        if ("0".equals(type)) {
            checkUnauthorized(tid, trade);
        }
        TradeDeliverVO deliver = trade.getTradeDelivers().stream().filter(
                d -> deliverId.equals(d.getDeliverId())
        ).findFirst().orElseGet(null);
        log.info("deliver=========>:{}", deliver);
        if (deliver != null) {
            List<ShippingItemVO> vos = new ArrayList<>();
            log.info("tradeItems=========>:{}", trade.getTradeItems());
            trade.getTradeItems().forEach(tradeItemVO -> {
                Optional<ShippingItemVO> vo =
                        deliver.getShippingItems().stream().filter(d -> d.getSkuId().equals(tradeItemVO.getSkuId()))
                                .peek(x -> {
                                    x.setPrice(tradeItemVO.getPrice());
                                    x.setBuyPoint(tradeItemVO.getBuyPoint());
                                    x.setPoints(tradeItemVO.getPoints());
                                }).findFirst();
                vo.ifPresent(vos::add);
            });
            deliver.setShippingItems(vos);
        }
        return BaseResponse.success(deliver);
    }

    /**
     * 根据快递公司及快递单号查询物流详情
     */
    @ApiOperation(value = "根据快递公司及快递单号查询物流详情")
    @RequestMapping(value = "/deliveryInfos", method = RequestMethod.POST)
    public ResponseEntity<List<Map<Object, Object>>> logistics(@RequestBody DeliveryQueryRequest queryRequest) {
        List<Map<Object, Object>> result = new ArrayList<>();
//        List<Map<Object, Object>> result = new ArrayList<>();
//
//        CompositeResponse<ConfigRopResponse> response = sdkClient.buildClientRequest().post(ConfigRopResponse.class,
//                "logistics.config", "1.0.0");
//        //如果快递设置为启用
//        if (Objects.nonNull(response.getSuccessResponse()) && DefaultFlag.YES.toValue() == response
//                .getSuccessResponse().getStatus()) {
//            result = deliveryQueryProvider.queryExpressInfoUrl(queryRequest).getContext().getOrderList();
//        }
        //获取快递100配置信息
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.KUAIDI100.toValue());
        LogisticsRopResponse response = systemConfigQueryProvider.findKuaiDiConfig(request).getContext();
        //已启用
        if (response.getStatus() == DefaultFlag.YES.toValue()) {
            result = deliveryQueryProvider.queryExpressInfoUrl(queryRequest).getContext().getOrderList();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增线下付款单
     */
    @ApiOperation(value = "新增线下付款单")
    @RequestMapping(value = "/pay/offline", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse createPayOrder(@RequestBody @Valid PaymentRecordRequest paymentRecordRequest) {
        Operator operator = commonUtil.getOperator();
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(paymentRecordRequest.getTid()).build())
                        .getContext().getTradeVO();

        if (!trade.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            return BaseResponse.error("非法越权操作");
        }

        if (trade.getTradeState().getFlowState() == FlowState.INIT || trade.getTradeState().getFlowState() ==
                FlowState.VOID) {
            throw new SbcRuntimeException("K-050206");
        }
        ReceivableAddDTO receivableAddDTO = ReceivableAddDTO.builder()
                .accountId(paymentRecordRequest.getAccountId())
                .payOrderId(trade.getPayOrderId())
                .createTime(paymentRecordRequest.getCreateTime())
                .comment(paymentRecordRequest.getRemark())
                .encloses(paymentRecordRequest.getEncloses())
                .build();

        TradeAddReceivableRequest tradeAddReceivableRequest =
                TradeAddReceivableRequest.builder()
                        .receivableAddDTO(receivableAddDTO)
                        .platform(operator.getPlatform())
                        .operator(operator)
                        .build();
        return tradeProvider.addReceivable(tradeAddReceivableRequest);
    }

    /**
     * 取消订单
     */
    @ApiOperation(value = "取消订单")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/cancel/{tid}", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse cancel(@PathVariable(value = "tid") String tid) {
        Operator operator = commonUtil.getOperator();
        TradeCancelRequest tradeCancelRequest = TradeCancelRequest.builder()
                .tid(tid).operator(operator).build();

        tradeProvider.cancel(tradeCancelRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 确认收货
     *
     * @param tid 订单号
     */
    @ApiOperation(value = "确认收货")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/receive/{tid}", method = RequestMethod.GET)
    public BaseResponse confirm(@PathVariable String tid) {
        Operator operator = commonUtil.getOperator();
        TradeVO trade =
                tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        checkUnauthorized(tid, trade);

        TradeConfirmReceiveRequest tradeConfirmReceiveRequest = TradeConfirmReceiveRequest.builder()
                .operator(operator).tid(tid).build();

        tradeProvider.confirmReceive(tradeConfirmReceiveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 0元订单默认支付
     *
     * @param tid
     * @return
     */
    @ApiOperation(value = "0元订单默认支付")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单ID", required = true)
    @RequestMapping(value = "/default/pay/{tid}", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<Boolean> defaultPay(@PathVariable String tid) {

        TradeDefaultPayRequest tradeDefaultPayRequest = TradeDefaultPayRequest
                .builder()
                .payWay(PayWay.UNIONPAY)
                .tid(tid)
                .build();

        return BaseResponse.success(tradeProvider.defaultPay(tradeDefaultPayRequest).getContext().getPayResult());
    }

    /**
     * 获取订单商品详情,包含区间价，会员级别价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
        //性能优化，原来从order服务绕道，现在直接从goods服务直行
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
//        TradeGetGoodsResponse response =
//                tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(skuIds).build()).getContext();

        //计算区间价
        GoodsIntervalPriceByCustomerIdResponse priceResponse = goodsIntervalPriceService.getGoodsIntervalPriceVOList
                (response.getGoodsInfos(), customer.getCustomerId());
        response.setGoodsInfos(priceResponse.getGoodsInfoVOList());
        //获取客户的等级
        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                    .goodsInfos(goodsInfoMapper.goodsInfoVOsToGoodsInfoDTOs(response.getGoodsInfos()))
                    .customerDTO(custmerMapper.customerVOToCustomerDTO(customer)).build())
                    .getContext().getGoodsInfoVOList();
            response.setGoodsInfos(goodsInfoVOList);
        }
        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .goodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList())
                .build();
    }

    private void checkUnauthorized(@PathVariable String tid, TradeVO detail) {
        if (!detail.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
    }


    /**
     * 1.订单未完成 （订单已支付扒拉了巴拉  显示退货退款按钮-与后台开关设置无关）
     * 2.订单已完成，在截止时间内，且退货开关开启时，前台显示 申请入口（完成时记录订单可退申请的截止时间，如果完成时开关关闭 时间记录完成当时的时间）
     *
     * @param flag
     * @param days
     * @param tradeState
     * @param canReturnFlag
     * @return
     */
    private boolean isCanReturnTime(boolean flag, int days, TradeStateVO tradeState, boolean canReturnFlag) {
        if (canReturnFlag && tradeState.getFlowState() == FlowState.COMPLETED) {
            if (flag) {
                if (Objects.nonNull(tradeState.getFinalTime())) {
                    //是否可退根据订单完成时配置为准
                    flag = tradeState.getFinalTime().isAfter(LocalDateTime.now());
                } else if (Objects.nonNull(tradeState.getEndTime())) {
                    //容错-历史数据
                    //判断是否在可退时间范围内
                    LocalDateTime endTime = tradeState.getEndTime();
                    return endTime.plusDays(days).isAfter(LocalDateTime.now());
                }
            } else {
                return false;
            }
            return flag;
        }
        return canReturnFlag;
    }

    /**
     * 验证小店商品，开店礼包
     */
    private void validShopGoods(List<TradeItemGroupVO> tradeItemGroups, DistributeChannel channel) {

        DefaultFlag storeBagsFlag = tradeItemGroups.get(0).getStoreBagsFlag();
        if (DefaultFlag.NO.equals(storeBagsFlag)) {
            if (channel.getChannelType() == ChannelType.SHOP) {
                // 1.验证商品是否是小店商品
                List<String> skuIds = tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream())
                        .map(item -> item.getSkuId()).collect(Collectors.toList());
                DistributorGoodsInfoVerifyRequest verifyRequest = new DistributorGoodsInfoVerifyRequest();
                verifyRequest.setDistributorId(channel.getInviteeId());
                verifyRequest.setGoodsInfoIds(skuIds);
                List<String> invalidIds = distributorGoodsInfoQueryProvider
                        .verifyDistributorGoodsInfo(verifyRequest).getContext().getInvalidIds();
                if (CollectionUtils.isNotEmpty(invalidIds)) {
                    throw new SbcRuntimeException("K-080302");
                }

                // 2.验证商品对应商家的分销开关有没有关闭
                tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream().map(item -> {
                    item.setStoreId(i.getSupplier().getStoreId());
                    return item;
                })).forEach(item -> {
                    if (DefaultFlag.NO.equals(
                            distributionCacheService.queryStoreOpenFlag(String.valueOf(item.getStoreId())))) {
                        throw new SbcRuntimeException("K-080302");
                    }
                });
            }
        } else {
            // 开店礼包商品校验
            RecruitApplyType applyType = distributionCacheService.queryDistributionSetting().getApplyType();
            if (RecruitApplyType.REGISTER.equals(applyType)) {
                throw new SbcRuntimeException("K-080302");
            }
            TradeItemVO tradeItem = tradeItemGroups.get(0).getTradeItems().get(0);
            List<String> goodsInfoIds = distributionCacheService.queryStoreBags()
                    .stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            if (!goodsInfoIds.contains(tradeItem.getSkuId())) {
                throw new SbcRuntimeException("K-080302");
            }
        }
    }

    /**
     * 增加预售活动过期校验
     *
     * @param tradeItemGroups
     * @param tradeCommitRequest
     */
    private void validateBookingQualification(List<TradeItemGroupVO> tradeItemGroups, TradeCommitRequest tradeCommitRequest) {
        tradeItemGroups.get(0).getTradeItems().forEach(item -> {
            if (item.getIsBookingSaleGoods()) {
                if (StringUtils.isEmpty(tradeCommitRequest.getTailNoticeMobile()) && item.getBookingType() == BookingType.EARNEST_MONEY) {
                    throw new SbcRuntimeException("K-000009");
                }
                BookingSaleIsInProgressResponse bookingResponse = bookingSaleQueryProvider.isInProgress(BookingSaleIsInProgressRequest.builder().goodsInfoId(item.getSkuId()).build()).getContext();
                if (Objects.isNull(bookingResponse) || Objects.isNull(bookingResponse.getBookingSaleVO())) {
                    throw new SbcRuntimeException("K-600010");
                }
                BookingSaleVO bookingSaleVO = bookingResponse.getBookingSaleVO();
                if (!bookingSaleVO.getId().equals(item.getBookingSaleId())) {
                    throw new SbcRuntimeException("K-000009");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ONE) &&
                        (bookingSaleVO.getHandSelEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getHandSelStartTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-170003");
                }
                if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ZERO) &&
                        (bookingSaleVO.getBookingEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getBookingStartTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-170003");
                }
                tradeCommitRequest.setIsBookingSaleGoods(Boolean.TRUE);
            }
        });
    }

    /**
     * 校验是下单商品是否参加了预约，预售活动，参加了则提示重新下单(积分价商品优先预约、预售活动)
     * 应用场景：普通商品在提交订单之前成为了预约、预售商品，此时给与前端提示
     *
     * @param tradeItemGroups
     */
    private void containAppointmentSaleAndBookingSale(List<TradeItemGroupVO> tradeItemGroups, CustomerVO customer) {

        Boolean suitMarketingFlag =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getSuitMarketingFlag()) && tradeItemGroupVO.getSuitMarketingFlag().equals(Boolean.TRUE));
        Boolean isGrouponOrder =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon()));
        if (suitMarketingFlag || isGrouponOrder) {
            return;
        }
        List<String> skuIds = new ArrayList<>();
        tradeItemGroups.forEach(tradeItemGroup -> {
            skuIds.addAll(tradeItemGroup.getTradeItems().stream().map(TradeItemVO::getSkuId).collect(Collectors.toList()));
        });
        GoodsInfoResponse response = getGoodsResponse(skuIds, customer);
        List<String> needValidSkuIds = new ArrayList<>();
        tradeItemGroups.forEach(tradeItemGroup -> needValidSkuIds.addAll(tradeItemGroup.getTradeItems()
                .stream()
                .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && !i.getIsAppointmentSaleGoods()
                        && Objects.nonNull(i.getIsBookingSaleGoods()) && !i.getIsBookingSaleGoods())
                .map(TradeItemVO::getSkuId).collect(Collectors.toList())));
        //积分价商品不需要校验
        needValidSkuIds.removeAll(response.getGoodsInfos().stream()
                .filter(goodsInfoVO -> Objects.nonNull(goodsInfoVO.getBuyPoint()) && goodsInfoVO.getBuyPoint() > 0)
                .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(needValidSkuIds)) {
            return;
        }
        tradeItemGroups.forEach(tradeItemGroupVO -> {
            IteratorUtils.zip(response.getGoodsInfos(), tradeItemGroupVO.getTradeItems(),
                    (a, b) ->
                            a.getGoodsInfoId().equals(b.getSkuId())
                    ,
                    (c, d) -> {
                        d.setBuyPoint(c.getBuyPoint());
                    });
        });
        // 预约活动校验是否有资格
        this.validateAppointmentQualification(tradeItemGroups);
        appointmentSaleQueryProvider.containAppointmentSaleAndBookingSale(AppointmentSaleInProgressRequest.builder().goodsInfoIdList(needValidSkuIds).build());
    }

    /**
     * 预约活动校验是否有资格
     *
     * @param tradeItemGroups
     */
    private void validateAppointmentQualification(List<TradeItemGroupVO> tradeItemGroups) {
        Boolean suitMarketingFlag =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getSuitMarketingFlag()) && tradeItemGroupVO.getSuitMarketingFlag().equals(Boolean.TRUE));
        Boolean isGrouponOrder =
                tradeItemGroups.stream().anyMatch(tradeItemGroupVO -> Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon()));
        if (suitMarketingFlag || isGrouponOrder) {
            return;
        }
        List<String> appointmentSaleSkuIds = new ArrayList<>();
        List<String> allSkuIds = new ArrayList<>();

        tradeItemGroups.forEach(tradeItemGroup -> {
            appointmentSaleSkuIds.addAll(tradeItemGroup.getTradeItems().stream()
                    .filter(i -> Objects.nonNull(i.getIsAppointmentSaleGoods()) && i.getIsAppointmentSaleGoods())
                    .map(TradeItemVO::getSkuId).collect(Collectors.toList()));
            allSkuIds.addAll(tradeItemGroup.getTradeItems().stream()
                    .filter(i -> Objects.isNull(i.getBuyPoint()) || i.getBuyPoint() == 0)
                    .map(TradeItemVO::getSkuId).collect(Collectors.toList()));
        });
        if (CollectionUtils.isEmpty(allSkuIds)) {
            return;
        }
        AppointmentSaleInProcessResponse response =
                appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(
                        AppointmentSaleInProgressRequest.builder().goodsInfoIdList(allSkuIds)
                                .build()).getContext();
        int purchaseNum = appointmentSaleSkuIds.size();
        int actualNum = 0;
        if (Objects.nonNull(response) || CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            actualNum = response.getAppointmentSaleVOList().size();
        }

        //包含预约中商品, 校验不通过
        if (actualNum > purchaseNum) {
            throw new SbcRuntimeException("K-600017");
        }

        //预约活动失效
        if (purchaseNum > actualNum) {
            throw new SbcRuntimeException("K-600009");
        }

        if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getAppointmentSaleVOList())) {
            response.getAppointmentSaleVOList().forEach(a -> {
                if (!(a.getSnapUpStartTime().isBefore(LocalDateTime.now()) && a.getSnapUpEndTime().isAfter(LocalDateTime.now()))) {
                    throw new SbcRuntimeException("K-600009");
                }
                if (a.getAppointmentType().equals(NumberUtils.INTEGER_ONE)) {
                    if (a.getJoinLevel().equals("-3")) {
                        //企业会员
                        CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
                        customerGetByIdRequest.setCustomerId(commonUtil.getCustomer().getCustomerId());
                        CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
                        if (!EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    } else if (a.getJoinLevel().equals("-2")) {
                        //付费会员
                        List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
                                .list(PaidCardCustomerRelListRequest.builder()
                                        .delFlag(DeleteFlag.NO)
                                        .endTimeBegin(LocalDateTime.now())
                                        .customerId(commonUtil.getCustomer().getCustomerId()).build())
                                .getContext().getPaidCardCustomerRelVOList();
                        if (CollectionUtils.isEmpty(relVOList)) {
                            throw new SbcRuntimeException("K-160003");
                        }
                    } else if (!a.getJoinLevel().equals("-1")) {
                        //获取会员在该店铺的等级，自营店铺取平台等级；第三方店铺取店铺等级
                        CustomerLevelByCustomerIdAndStoreIdResponse levelResponse = customerLevelQueryProvider
                                .getCustomerLevelByCustomerIdAndStoreId(CustomerLevelByCustomerIdAndStoreIdRequest.builder().customerId(commonUtil.getOperatorId()).storeId(a.getStoreId()).build())
                                .getContext();
                        if (Objects.nonNull(levelResponse) && Objects.nonNull(levelResponse.getLevelId())) {
                            if (!a.getJoinLevel().equals("0") && !Arrays.asList(a.getJoinLevel().split(",")).contains(levelResponse.getLevelId().toString())) {
                                throw new SbcRuntimeException("K-160003");
                            }
                        } else {
                            throw new SbcRuntimeException("K-160004");
                        }
                    }
                } else {
                    AppointmentRecordResponse recordResponse =
                            appointmentRecordQueryProvider.getAppointmentInfo(AppointmentRecordQueryRequest.builder().
                                    buyerId(commonUtil.getCustomer().getCustomerId())
                                    .goodsInfoId(a.getAppointmentSaleGood().getGoodsInfoId()).appointmentSaleId(a.getId()).build()).getContext();
                    if (Objects.isNull(recordResponse) || Objects.isNull(recordResponse.getAppointmentRecordVO())) {
                        throw new SbcRuntimeException("K-180001");
                    }
                }
            });
        }
    }

    /**
     * 校验商品限售信息
     *
     * @param tradeItemGroupVO
     * @param customerVO
     */
    private void validateRestrictedGoods(TradeItemGroupVO tradeItemGroupVO, CustomerVO customerVO) {
        //组装请求的数据
        List<TradeItemVO> tradeItemVOS = tradeItemGroupVO.getTradeItems();
        List<GoodsRestrictedValidateVO> list = tradeItemMapper.tradeItemVOsToGoodsRestrictedValidateVOs(tradeItemVOS);
        //组合商品不考虑限售限制
        if (Objects.nonNull(tradeItemGroupVO.getSuitMarketingFlag()) && tradeItemGroupVO.getSuitMarketingFlag()) {
            return;
        }
        Boolean openGroup = false;
        if (Objects.nonNull(tradeItemGroupVO.getGrouponForm()) && Objects.nonNull(tradeItemGroupVO.getGrouponForm().getOpenGroupon())) {
            openGroup = tradeItemGroupVO.getGrouponForm().getOpenGroupon();
        }
        Boolean storeBagsFlag = false;
        if (DefaultFlag.YES.equals(tradeItemGroupVO.getStoreBagsFlag())) {
            storeBagsFlag = true;
        }

        goodsRestrictedSaleQueryProvider.validateOrderRestricted(GoodsRestrictedBatchValidateRequest.builder()
                .goodsRestrictedValidateVOS(list)
                .snapshotType(tradeItemGroupVO.getSnapshotType())
                .customerVO(customerVO)
                .openGroupon(openGroup)
                .storeBagsFlag(storeBagsFlag)
                .build());
    }

    /**
     * 校验商品限售信息 ——— 批发下单使用
     *
     * @param tradeItemRequests
     * @param customerVO
     */
    private void validateRestrictedGoodsForReserveLisr(List<TradeItemRequest> tradeItemRequests,
                                                       CustomerVO customerVO) {
        //组装请求的数据
        goodsRestrictedSaleQueryProvider.validateOrderRestricted(GoodsRestrictedBatchValidateRequest.builder()
                .goodsRestrictedValidateVOS(tradeItemMapper.tradeItemRequestsToGoodsRestrictedValidateVOs(tradeItemRequests))
                .customerVO(customerVO)
                .openGroupon(false)
                .build());
    }

    /**
     * 判断当前用户是否企业会员
     *
     * @return
     */
    private boolean isIepCustomer(CustomerVO customer) {
        EnterpriseCheckState customerState;
        if (Objects.isNull(customer)) {
            customerState = commonUtil.getCustomer().getEnterpriseCheckState();
        } else {
            customerState = customer.getEnterpriseCheckState();
        }
        return commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)
                && !Objects.isNull(customerState)
                && customerState == EnterpriseCheckState.CHECKED;
    }

    /**
     * 判断商品是否企业购商品
     *
     * @param enterpriseAuditState
     * @return
     */
    private boolean isEnjoyIepGoodsInfo(EnterpriseAuditState enterpriseAuditState) {
        return !Objects.isNull(enterpriseAuditState)
                && enterpriseAuditState == EnterpriseAuditState.CHECKED;
    }

    /**
     * @Description: 订单各状态（待支付、待发货...）下的统计
     * @Date: 2020/7/16 11:23
     */
    @ApiOperation(value = "订单todo")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "inviteeId", value = "inviteeId", required = true)
    @GetMapping(value = "/todo/{inviteeId}")
    public BaseResponse<OrderTodoResp> TardeTodo(@PathVariable String inviteeId) {
        OrderTodoResp resp = new OrderTodoResp();
        TradeQueryDTO queryRequest = new TradeQueryDTO();
        queryRequest.setBuyerId(commonUtil.getOperatorId());
        if (StringUtils.isNotBlank(inviteeId) && !"null".equals(inviteeId)) {
            queryRequest.setInviteeId(inviteeId);
        }
        TradeStateDTO tradeState = new TradeStateDTO();

        // 都未发货
        tradeState.setFlowState(FlowState.AUDIT);
        TradeConfigGetByTypeRequest configGetByTypeRequest = new TradeConfigGetByTypeRequest();
        configGetByTypeRequest.setConfigType(ConfigType.ORDER_SETTING_PAYMENT_ORDER);
        TradeConfigGetByTypeResponse context = auditQueryProvider.getTradeConfigByType(configGetByTypeRequest).getContext();
        //不限付款顺序时，未付款同时展示在待付款、待发货下
        //限制先款后货时，未付款只展示在待付款下
        //0 不限； 1先款后货
        if (NumberUtils.INTEGER_ONE.equals(context.getStatus())) {
            tradeState.setPayState(PayState.PAID);
        }
        queryRequest.setTradeState(tradeState);
        Long noDeliveredCount = tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount();
        // 部分发货
        tradeState.setFlowState(FlowState.DELIVERED_PART);
        queryRequest.setTradeState(tradeState);
        Long deliveredPartCount = tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount();
        resp.setWaitDeliver(noDeliveredCount + deliveredPartCount);

        //设置待付款订单
        tradeState.setFlowState(null);
        tradeState.setPayState(PayState.NOT_PAID);
        queryRequest.setTradeState(tradeState);
        resp.setWaitPay(tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount());

        //设置待收货订单
        tradeState.setPayState(null);
        tradeState.setFlowState(FlowState.DELIVERED);
        queryRequest.setTradeState(tradeState);
        resp.setWaitReceiving(tradeQueryProvider.countCriteria(TradeCountCriteriaRequest.builder()
                .whereCriteria(queryRequest.getWhereCriteria()).tradePageDTO(queryRequest).build()).getContext().getCount());


        ReturnCountByConditionRequest returnQueryRequest = new ReturnCountByConditionRequest();
        returnQueryRequest.setBuyerId(commonUtil.getOperatorId());
        // 待审核
        returnQueryRequest.setReturnFlowState(ReturnFlowState.INIT);
        Long waitAudit = returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount();
        // 待收货
        returnQueryRequest.setReturnFlowState(ReturnFlowState.DELIVERED);
        Long waitReceiving = returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount();
        // 待退款
        returnQueryRequest.setReturnFlowState(ReturnFlowState.RECEIVED);
        Long waitRefund = returnOrderQueryProvider.countByCondition(returnQueryRequest).getContext().getCount();

        resp.setRefund(waitAudit + waitReceiving + waitRefund);

        // 商品待评价
        Long goodsEvaluate =
                goodsTobeEvaluateQueryProvider.getGoodsTobeEvaluateNum(GoodsTobeEvaluateQueryRequest.builder()
                        .customerId(commonUtil.getOperatorId()).build()).getContext();
/*        // 店铺服务待评价
        Long storeEvaluate =
                storeTobeEvaluateQueryProvider.getStoreTobeEvaluateNum(StoreTobeEvaluateQueryRequest.builder()
                .customerId(commonUtil.getOperatorId()).build()).getContext();*/

//        resp.setWaitEvaluate(goodsEvaluate + storeEvaluate);
        resp.setWaitEvaluate(goodsEvaluate);
        return BaseResponse.success(resp);
    }


    /**
     * 分页查询订单(优化版)
     */
    @ApiOperation(value = "分页查询订单")
    @RequestMapping(value = "/pageOptimize", method = RequestMethod.POST)
    public BaseResponse<Page<TradeVO>> pageOptimize(@RequestBody TradePageQueryRequest paramRequest) {
        TradeQueryDTO tradeQueryRequest = TradeQueryDTO.builder()
                .tradeState(TradeStateDTO.builder()
                        .flowState(paramRequest.getFlowState())
                        .payState(paramRequest.getPayState())
                        .deliverStatus(paramRequest.getDeliverStatus())
                        .build())
                .buyerId(commonUtil.getOperatorId())
                .inviteeId(paramRequest.getInviteeId())
                .channelType(paramRequest.getChannelType())
                .beginTime(paramRequest.getCreatedFrom())
                .endTime(paramRequest.getCreatedTo())
                .keyworks(paramRequest.getKeywords())
                .isBoss(false)
                .build();
        tradeQueryRequest.setTag(paramRequest.getTag());
        tradeQueryRequest.setPageNum(paramRequest.getPageNum());
        tradeQueryRequest.setPageSize(paramRequest.getPageSize());
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();

        //不限付款顺序时，未付款同时展示在待付款、待发货下
        //限制先款后货时，未付款只展示在待付款下
        //0 不限； 1先款后货
        if (Objects.isNull(paramRequest.getPayState()) && paramRequest.getFlowState() == FlowState.AUDIT) {
            TradeConfigGetByTypeRequest configGetByTypeRequest = new TradeConfigGetByTypeRequest();
            configGetByTypeRequest.setConfigType(ConfigType.ORDER_SETTING_PAYMENT_ORDER);
            TradeConfigGetByTypeResponse context = auditQueryProvider.getTradeConfigByType(configGetByTypeRequest).getContext();
            if (NumberUtils.INTEGER_ONE.equals(context.getStatus())) {
                tradeQueryRequest.getTradeState().setPayState(PayState.PAID);
            }
        }
        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteriaOptimize(TradePageCriteriaRequest.builder()
                .tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();
        List<TradeVO> tradeVOList = tradePage.getContent();
        //扭转预售商品支付尾款状态为已作废
        tradeVOList.forEach(this::fillTradeBookingTimeOut);
        tradeVOList.forEach(this::fillLatestDeliverTime);
        tradeVOList.stream()
                .filter(t -> PayState.NOT_PAID == t.getTradeState().getPayState()
                        || PayState.PAID_EARNEST == t.getTradeState().getPayState())
                .forEach(tradeVO -> {
                    tradeVO.getTradeItems().stream()
                            .forEach(t -> t.setVirtualCoupons(Collections.EMPTY_LIST));
                    tradeVO.getGifts().stream()
                            .forEach(t -> t.setVirtualCoupons(Collections.EMPTY_LIST));
                });
        tradePage.setContent(tradeVOList);
        return BaseResponse.success(tradePage);
    }

    /**
     * 订单周期购赠品订单快照刷新
     */
    @ApiOperation(value = "订单周期购赠品订单快照刷新")
    @RequestMapping(value = "/cycle-buy/gift/confirm", method = RequestMethod.PUT)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse cycleBuyGiftConfirm(@RequestBody @Valid TradeItemSnapshotCycleBuyGiftRequest request) {

        //校验周期购活动
        VerifyCycleBuyRequest verifyCycleBuyRequest = VerifyCycleBuyRequest.builder()
                .goodsId(request.getGoodsId()).gifts(request.getCycleBuyInfo().getCycleBuyGifts()).build();
        verifyQueryProvider.verifyTradeCycleBuy(verifyCycleBuyRequest);
        request.setTerminalToken(commonUtil.getTerminalToken());

        return tradeItemProvider.snapshotCycleBuyGift(request);
    }

    @ApiOperation(value = "查询周期购订单发货日历")
    @GetMapping(value = "/deliver-calendar/{tid}")
    public BaseResponse<DeliverCalendarResponse> deliverCalendar(@PathVariable String tid) {
        TradeVO tradeVO = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(tid).build()).getContext().getTradeVO();
        //权限校验
        if (!tradeVO.getBuyer().getId().equals(commonUtil.getOperatorId())) {
            return BaseResponse.success(new DeliverCalendarResponse());
        }

        // 未审核/已作废/先款后货未支付
        TradeStateVO tradeState = tradeVO.getTradeState();
        boolean showFlag = !AuditState.CHECKED.equals(tradeState.getAuditState()) || FlowState.VOID.equals(tradeState.getFlowState())
                || PaymentOrder.PAY_FIRST.equals(tradeVO.getPaymentOrder()) && !PayState.PAID.equals(tradeState.getPayState());
        // 非周期购订单或订单状态不符合不展示发货日历
        if (Objects.isNull(tradeVO) || Objects.isNull(tradeVO.getTradeCycleBuyInfo()) || showFlag) {
            return BaseResponse.success(new DeliverCalendarResponse());
        }

        //商家主导只展示已配送的数据
        List<DeliverCalendarVO> deliverCalendar = tradeVO.getTradeCycleBuyInfo().getDeliverCalendar();
        if (DeliveryPlan.BUSINESS.equals(tradeVO.getTradeCycleBuyInfo().getDeliveryPlan())) {
            deliverCalendar = deliverCalendar.stream().filter(c -> CycleDeliverStatus.SHIPPED.equals(c.getCycleDeliverStatus())).collect(Collectors.toList());
        }

        Map<String, List<DeliverCalendarVO>> map = deliverCalendar.stream().collect(Collectors.groupingBy(vo -> vo.getDeliverDate().format(DateTimeFormatter.ofPattern("yyyy年M月")), LinkedHashMap::new, Collectors.toList()));

        return BaseResponse.success(new DeliverCalendarResponse(map));
    }


    /**
     * 未完全支付的定金预售订单状态填充为已作废状态
     * <p>
     * （主要订单真实作废比较延迟，计时过后仍然处于待支付尾款情况，前端由订单状态判断来控制支付尾款按钮的展示）
     *
     * @param detail 订单
     */
    private void fillTradeBookingTimeOut(TradeVO detail) {
        //未完全支付的定金预售订单
        if (Boolean.TRUE.equals(detail.getIsBookingSaleGoods())
                && BookingType.EARNEST_MONEY.equals(detail.getBookingType())
                && Objects.nonNull(detail.getTradeState())
                && (!PayState.PAID.equals(detail.getTradeState().getPayState()))) {
            //尾款时间 < 今天
            if (Objects.nonNull(detail.getTradeState().getTailEndTime())
                    && detail.getTradeState().getTailEndTime().isBefore(LocalDateTime.now())) {
                detail.getTradeState().setFlowState(FlowState.VOID);//作废
            }
        }
    }

    /**
     * 组装周期购信息
     *
     * @return
     */
    private CycleBuyInfoDTO fillCycleBuyInfoToSnapshot(GoodsInfoVO goodsInfoVO, DeliveryCycle deliveryCycle, String sendDateRule, DeliveryPlan deliveryPlan) {
        CycleBuyInfoDTO cycleBuyInfoDTO = new CycleBuyInfoDTO();

        if (Objects.nonNull(goodsInfoVO.getGoodsType()) && GoodsType.CYCLE_BUY.ordinal() == goodsInfoVO.getGoodsType()) {

            //验证周期购
            if (Objects.isNull(deliveryPlan)) {
                deliveryPlan = DeliveryPlan.BUSINESS;
            }
            VerifyCycleBuyRequest verifyCycleBuyRequest = VerifyCycleBuyRequest.builder()
                    .goodsId(goodsInfoVO.getGoodsId())
                    .deliveryPlan(deliveryPlan)
                    .sendDateRule(sendDateRule)
                    .deliveryCycle(deliveryCycle).build();
            verifyQueryProvider.verifyTradeCycleBuy(verifyCycleBuyRequest);

            CycleBuyVO cycleBuyVO = cycleBuyQueryProvider
                    .getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsInfoVO.getGoodsId()).build())
                    .getContext().getCycleBuyVO();

            if (Objects.nonNull(cycleBuyVO)) {
                List<String> ids = cycleBuyVO.getCycleBuyGiftVOList().stream().map(CycleBuyGiftVO::getGoodsInfoId).collect(Collectors.toList());
                //赠送方式为可选一种时，默认选择第一件赠品
                if (GiftGiveMethod.CHOICE.equals(cycleBuyVO.getGiftGiveMethod()) && CollectionUtils.isNotEmpty(ids)) {
                    cycleBuyInfoDTO.setCycleBuyGifts(Lists.newArrayList(ids.get(NumberUtils.INTEGER_ZERO)));
                } else {
                    cycleBuyInfoDTO.setCycleBuyGifts(ids);
                }
            }

            //商家主导配送取后台配置数据
            if (DeliveryPlan.BUSINESS.equals(deliveryPlan)) {
                sendDateRule = cycleBuyVO.getSendDateRule().get(NumberUtils.INTEGER_ZERO);
                deliveryCycle = cycleBuyVO.getDeliveryCycle();
            }
            //查询发货日期规则描述
            CycleBuySendDateRuleRequest ruleRequest = CycleBuySendDateRuleRequest.builder()
                    .deliveryCycle(deliveryCycle)
                    .rules(Lists.newArrayList(sendDateRule)).build();
            CycleBuySendDateRuleVO ruleVO = cycleBuyQueryProvider.getSendDateRuleList(ruleRequest).getContext()
                    .getCycleBuySendDateRuleVOList().get(NumberUtils.INTEGER_ZERO);
            cycleBuyInfoDTO.setDeliveryCycle(deliveryCycle);
            cycleBuyInfoDTO.setCycleNum(goodsInfoVO.getCycleNum());
            cycleBuyInfoDTO.setCycleBuySendDateRule(KsBeanUtil.convert(ruleVO, CycleBuySendDateRuleDTO.class));
            cycleBuyInfoDTO.setDeliveryPlan(deliveryPlan);
            return cycleBuyInfoDTO;
        }

        return null;
    }

    /**
     * 仅用于立即购买后的确认订单页面，组装周期购信息
     *
     * @param response        请求响应
     * @param tradeItemGroups 快照
     */
    private void fillCycleBuyInfoToConfirmResponse(TradeConfirmResponse response, List<TradeItemGroupVO> tradeItemGroups) {

        //周期购订单中只有一个下单商品
        TradeConfirmItemVO confirmItem = response.getTradeConfirmItems().get(NumberUtils.INTEGER_ZERO);
        TradeItemVO tradeItemVO = confirmItem.getTradeItems().get(NumberUtils.INTEGER_ZERO);

        //快照中的周期购信息
        TradeItemGroupVO tradeItemGroupVO = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        CycleBuyInfoVO cycleBuyInfoVO = tradeItemGroupVO.getCycleBuyInfo();

        if (Objects.nonNull(tradeItemVO.getGoodsType()) && GoodsType.CYCLE_BUY.equals(tradeItemVO.getGoodsType())) {
            CycleBuyVO cycleBuyVO = cycleBuyQueryProvider
                    .getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(tradeItemVO.getSpuId()).build())
                    .getContext().getCycleBuyVO();
            if (Objects.nonNull(cycleBuyVO)) {
                //周期购订单
                response.setCycleBuyFlag(Boolean.TRUE);

                //取快照中的配送和发货数据
                CycleBuySendDateRuleVO ruleVO = cycleBuyInfoVO.getCycleBuySendDateRule();
                DeliveryCycle deliveryCycle = cycleBuyInfoVO.getDeliveryCycle();

                //计算最近一次的发货时间
                LatestDeliverDateRequest latestDeliverDateRequest = LatestDeliverDateRequest.builder()
                        .date(LocalDate.now()).deliveryCycle(deliveryCycle).rule(ruleVO.getSendDateRule()).build();
                LocalDate latestDeliverTime = tradeQueryProvider.getLatestDeliverDate(latestDeliverDateRequest).getContext().getDate();

                CycleBuyInfoVO vo = new CycleBuyInfoVO();
                vo.setDeliveryCycle(deliveryCycle);
                vo.setCycleBuySendDateRule(ruleVO);
                vo.setCycleNum(cycleBuyInfoVO.getCycleNum());
                vo.setNextDeliverTime(latestDeliverTime);
                vo.setCycleBuyGifts(cycleBuyInfoVO.getCycleBuyGifts());
                vo.setDeliveryPlan(cycleBuyInfoVO.getDeliveryPlan());
                vo.setGiftGiveMethod(cycleBuyVO.getGiftGiveMethod());
                confirmItem.setCycleBuyInfo(vo);

                //赠品
                TradeGetGoodsResponse giftResp = tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(cycleBuyInfoVO.getCycleBuyGifts()).build()).getContext();
                Map<String, Long> giftsMap = cycleBuyVO.getCycleBuyGiftVOList().stream().collect(Collectors.toMap(CycleBuyGiftVO::getGoodsInfoId, CycleBuyGiftVO::getFreeQuantity));
                List<TradeItemDTO> gifts =
                        cycleBuyInfoVO.getCycleBuyGifts().stream().map(i -> TradeItemDTO.builder().price(BigDecimal.ZERO)
                                .num(giftsMap.get(i))
                                .skuId(i)
                                .build()).collect(Collectors.toList());
                List<TradeItemVO> giftVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(gifts
                        , KsBeanUtil.convert(giftResp, TradeGoodsListDTO.class)))
                        .getContext().getTradeItems();
                confirmItem.getGifts().addAll(giftVoList);
            }
        }
    }

    /**
     * 填充最近发货时间
     *
     * @param tradeVO
     */
    public void fillLatestDeliverTime(TradeVO tradeVO) {
        if (tradeVO.getCycleBuyFlag() && Objects.nonNull(tradeVO.getTradeCycleBuyInfo())) {
            TradeCycleBuyInfoVO tradeCycleBuyInfo = tradeVO.getTradeCycleBuyInfo();
            List<DeliverCalendarVO> deliverCalendarVOList = tradeCycleBuyInfo.getDeliverCalendar().stream()
                    .filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus() == CycleDeliverStatus.NOT_SHIPPED
                            || deliverCalendarVO.getCycleDeliverStatus() == CycleDeliverStatus.PUSHED)
                    .collect(Collectors.toList());
            //下期发货信息
            if (CollectionUtils.isNotEmpty(deliverCalendarVOList)) {
                DeliverCalendarVO deliverCalendarVO = deliverCalendarVOList.get(NumberUtils.INTEGER_ZERO);
                //获取下一期发货的日期
                String week = deliverCalendarVO.getDeliverDate().getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.CHINESE);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
                String localTime = df.format(deliverCalendarVO.getDeliverDate());
                tradeCycleBuyInfo.setLocalTime(localTime.concat("(周").concat(week).concat(")"));
                //下一期期数
                //过滤掉只有赠品的发货记录
                List<TradeDeliverVO> tradeDelivers = tradeVO.getTradeDelivers().stream().
                        filter(deliver -> CollectionUtils.isNotEmpty(deliver.getShippingItems()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tradeDelivers)) {
                    TradeDeliverVO tradeDeliverVO = tradeDelivers.get(NumberUtils.INTEGER_ZERO);
                    if (Objects.nonNull(tradeDeliverVO.getCycleNum())) {
                        tradeCycleBuyInfo.setNumberPeriods(tradeDeliverVO.getCycleNum() + NumberUtils.INTEGER_ONE);
                    }
                } else {
                    tradeCycleBuyInfo.setNumberPeriods(NumberUtils.INTEGER_ONE);
                }
            }
        }
    }


}
