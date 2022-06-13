package com.wanmi.sbc.order.trade.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Lists;
import com.soybean.mall.order.config.OrderConfigProperties;
import com.soybean.mall.order.miniapp.service.WxOrderService;
import com.soybean.mall.order.prize.service.OrderCouponService;
import com.soybean.mall.wx.mini.order.bean.request.WxOrderDetailRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxVideoOrderDetailResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordProvider;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.InvoiceType;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.PaidCardConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.CompanySourceType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.PointsOrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.enums.node.DistributionType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.common.exception.NotSupportDeliveryException;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.email.CustomerEmailQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardSaveProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerListForOrderCommitRequest;
import com.wanmi.sbc.customer.api.request.email.NoDeleteCustomerEmailListByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengKnowledgeLockRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointDeductRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointLockRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.invoice.CustomerInvoiceByIdAndDelFlagRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdAndStoreIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.fandeng.FanDengConsumeResponse;
import com.wanmi.sbc.customer.api.response.invoice.CustomerInvoiceByIdAndDelFlagResponse;
import com.wanmi.sbc.customer.api.response.store.ListNoDeleteStoreByIdsResponse;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CommissionPriorityType;
import com.wanmi.sbc.customer.bean.enums.CommissionUnhookType;
import com.wanmi.sbc.customer.bean.enums.CompanyType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerEmailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerSimVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.goods.api.provider.bookingsale.BookingSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsProvider;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.restrictedrecord.RestrictedRecordSaveProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsCountRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleGoodsCountRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoBatchPlusStockRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsMinusStockRequest;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordBatchAddRequest;
import com.wanmi.sbc.goods.api.response.bookingsale.BookingSaleByIdResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.ConditionType;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.ProvideType;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsExpressVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsFreeVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateStoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.RestrictedRecordSimpVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionCacheQueryProvider;
import com.wanmi.sbc.marketing.api.provider.gift.FullGiftQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordProvider;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingTradePluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeBatchModifyRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeReturnByIdRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftDetailListByMarketingIdAndLevelIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordIncrBuyNumRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsValidRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingTradeBatchWrapperRequest;
import com.wanmi.sbc.marketing.api.response.distribution.MultistageSettingGetResponse;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftDetailListByMarketingIdAndLevelIdResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsValidResponse;
import com.wanmi.sbc.marketing.bean.constant.CouponErrorCode;
import com.wanmi.sbc.marketing.bean.dto.CouponCodeBatchModifyDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingWrapperDTO;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.wanmi.sbc.order.api.enums.OrderTagEnum;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultQueryRequest;
import com.wanmi.sbc.order.api.request.trade.AutoUpdateInvoiceRequest;
import com.wanmi.sbc.order.api.request.trade.PointsCouponTradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.PointsTradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeAccountRecordRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradePayOnlineCallBackRequest;
import com.wanmi.sbc.order.api.request.trade.TradePurchaseRequest;
import com.wanmi.sbc.order.api.request.trade.TradeUpdateListTradeRequest;
import com.wanmi.sbc.order.api.request.trade.TradeUpdateRequest;
import com.wanmi.sbc.order.api.response.trade.TradeAccountRecordResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.GeneralInvoiceDTO;
import com.wanmi.sbc.order.bean.dto.SpecialInvoiceDTO;
import com.wanmi.sbc.order.bean.dto.StoreCommitInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeUpdateDTO;
import com.wanmi.sbc.order.bean.enums.AuditState;
import com.wanmi.sbc.order.bean.enums.BackRestrictedType;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import com.wanmi.sbc.order.bean.enums.OrderSmsTemplate;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import com.wanmi.sbc.order.bean.enums.PayCallBackResultStatus;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.TradeGoodsListVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.common.GoodsStockService;
import com.wanmi.sbc.order.common.OperationLogMq;
import com.wanmi.sbc.order.common.SystemPointsConfigService;
import com.wanmi.sbc.order.customer.service.CustomerCommonService;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import com.wanmi.sbc.order.exceptionoftradepoints.service.ExceptionOfTradePointsService;
import com.wanmi.sbc.order.groupon.service.GrouponOrderService;
import com.wanmi.sbc.order.logistics.model.root.LogisticsLog;
import com.wanmi.sbc.order.logistics.service.LogisticsLogService;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import com.wanmi.sbc.order.orderinvoice.request.OrderInvoiceSaveRequest;
import com.wanmi.sbc.order.orderinvoice.service.OrderInvoiceService;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.service.PayCallBackResultService;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.payorder.repository.PayOrderRepository;
import com.wanmi.sbc.order.payorder.request.PayOrderGenerateRequest;
import com.wanmi.sbc.order.payorder.response.PayOrderResponse;
import com.wanmi.sbc.order.payorder.service.PayOrderService;
import com.wanmi.sbc.order.purchase.PurchaseService;
import com.wanmi.sbc.order.purchase.request.PurchaseRequest;
import com.wanmi.sbc.order.receivables.model.root.Receivable;
import com.wanmi.sbc.order.receivables.repository.ReceivableRepository;
import com.wanmi.sbc.order.receivables.request.ReceivableAddRequest;
import com.wanmi.sbc.order.receivables.service.ReceivableService;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.repository.ReturnOrderRepository;
import com.wanmi.sbc.order.sensorsdata.SensorsDataService;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallTradeResult;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.fsm.TradeFSMService;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.Discounts;
import com.wanmi.sbc.order.trade.model.entity.GrouponTradeValid;
import com.wanmi.sbc.order.trade.model.entity.PayCallBackOnlineBatch;
import com.wanmi.sbc.order.trade.model.entity.PayInfo;
import com.wanmi.sbc.order.trade.model.entity.PointsTradeCommitResult;
import com.wanmi.sbc.order.trade.model.entity.TradeCommitResult;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.TradeGrouponCommitForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradePointsCouponItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.GeneralInvoice;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.Seller;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.entity.value.SpecialInvoice;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.mapper.TradeMapper;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeCommission;
import com.wanmi.sbc.order.trade.model.root.TradeConfirmItem;
import com.wanmi.sbc.order.trade.model.root.TradeDistributeItem;
import com.wanmi.sbc.order.trade.model.root.TradeDistributeItemCommission;
import com.wanmi.sbc.order.trade.model.root.TradeGroup;
import com.wanmi.sbc.order.trade.model.root.TradeGroupon;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.reponse.TradeFreightResponse;
import com.wanmi.sbc.order.trade.reponse.TradeItemPrice;
import com.wanmi.sbc.order.trade.reponse.TradeRemedyDetails;
import com.wanmi.sbc.order.trade.repository.GrouponInstanceRepository;
import com.wanmi.sbc.order.trade.repository.TradeGroupRepository;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import com.wanmi.sbc.order.trade.request.TradeCreateRequest;
import com.wanmi.sbc.order.trade.request.TradeDeliverRequest;
import com.wanmi.sbc.order.trade.request.TradeParams;
import com.wanmi.sbc.order.trade.request.TradePriceChangeRequest;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeRemedyRequest;
import com.wanmi.sbc.order.trade.request.TradeWrapperListRequest;
import com.wanmi.sbc.order.util.SmsSendUtil;
import com.wanmi.sbc.pay.api.provider.AliPayProvider;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.AliPayRefundRequest;
import com.wanmi.sbc.pay.api.request.ChannelItemByGatewayRequest;
import com.wanmi.sbc.pay.api.request.ChannelItemByIdRequest;
import com.wanmi.sbc.pay.api.request.ChannelItemSaveRequest;
import com.wanmi.sbc.pay.api.request.GatewayConfigByGatewayRequest;
import com.wanmi.sbc.pay.api.request.PayTradeRecordRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderOrParentCodeRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordCountByOrderOrParentCodeRequest;
import com.wanmi.sbc.pay.api.request.WxPayOrderDetailRequest;
import com.wanmi.sbc.pay.api.request.WxPayRefundInfoRequest;
import com.wanmi.sbc.pay.api.response.AliPayRefundResponse;
import com.wanmi.sbc.pay.api.response.PayChannelItemListResponse;
import com.wanmi.sbc.pay.api.response.PayChannelItemResponse;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.api.response.PayTradeRecordCountResponse;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.pay.api.response.WxPayOrderDetailReponse;
import com.wanmi.sbc.pay.api.response.WxPayRefundResponse;
import com.wanmi.sbc.pay.api.response.WxPayResultResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayUtil;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.EmailConfigProvider;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.EmailConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.enums.EmailStatus;
import com.wanmi.sbc.setting.bean.enums.PointsUsageFlag;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 订单service
 * Created by jinwei on 27/3/2017.
 */
@Service
@Slf4j
@RefreshScope
public class TradeService {

    /**
     * 注入消费记录生产者service
     */
    @Autowired
    public OrderProducerService orderProducerService;
    @Autowired
    public PointsGoodsSaveProvider pointsGoodsSaveProvider;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TradeFSMService tradeFSMService;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GoodsInfoProvider goodsInfoProvider;
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private OrderInvoiceService orderInvoiceService;
    @Autowired
    private ReturnOrderRepository returnOrderRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private ReceivableRepository receivableRepository;
    @Autowired
    private PayQueryProvider payQueryProvider;
    @Autowired
    private OsUtil osUtil;
    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;
    @Autowired
    private TradeItemService tradeItemService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AuditQueryProvider auditQueryProvider;
    @Autowired
    private AccountRecordProvider accountRecordProvider;
    @Autowired
    private ReceivableService receivableService;
    @Autowired
    private MarketingTradePluginProvider marketingTradePluginProvider;

    @Autowired
    private FullGiftQueryProvider fullGiftQueryProvider;
    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;
    @Autowired
    private CustomerCommonService customerCommonService;

    @Autowired
    private CouponCodeProvider couponCodeProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;
    @Autowired
    private TradeGroupRepository tradeGroupRepository;
    @Autowired
    private TradeGroupService tradeGroupService;
    @Autowired
    private EmailConfigProvider emailConfigProvider;
    @Autowired
    private CustomerEmailQueryProvider customerEmailQueryProvider;
    @Autowired
    private SystemPointsConfigService systemPointsConfigService;
    @Autowired
    private TradeEmailService tradeEmailService;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private OperationLogMq operationLogMq;
    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private SmsSendUtil smsSendUtil;
    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;
    @Autowired
    private DistributionCacheQueryProvider distributionCacheQueryProvider;
    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;
    @Autowired
    private GrouponInstanceRepository grouponInstanceRepository;
    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private GrouponRecordProvider grouponRecordProvider;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;


    @Autowired
    private LogisticsLogService logisticsLogService;

    @Autowired
    private RestrictedRecordSaveProvider restrictedRecordSaveProvider;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private PayCallBackResultService payCallBackResultService;


    @Autowired
    private GoodsStockService goodsStockService;

    @Autowired
    private LinkedMallTradeService linkedMallTradeService;

    @Autowired
    private AliPayProvider aliPayProvider;
    @Autowired
    private MarketingSuitsQueryProvider marketingSuitsQueryProvider;

    @Autowired
    private BookingSaleGoodsProvider bookingSaleGoodsProvider;

    @Autowired
    private AppointmentSaleGoodsProvider appointmentSaleGoodsProvider;

    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private BookingSaleQueryProvider bookingSaleQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private TradeCustomerService tradeCustomerService;

    @Autowired
    private TradeMarketingService tradeMarketingService;

    @Autowired
    private TradeGoodsService tradeGoodsService;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private CycleBuyDeliverTimeService cycleBuyDeliverTimeService;

    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;


    @Autowired
    private PaidCardSaveProvider paidCardSaveProvider;

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;

    @Autowired
    private BinderAwareChannelResolver resolver;

    @Autowired
    private ExternalProvider externalProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private ExceptionOfTradePointsService exceptionOfTradePointsService;

    @Autowired
    private CustomerProvider customerProvider;
    @Autowired
    private SensorsDataService sensorsDataService;

    @Value("${whiteOrder:1234}")
    private String whiteOrder;


    public static final String FMT_TIME_1 = "yyyy-MM-dd HH:mm:ss";

    @Value("${wx.create.order.send.message.templateId}")
    private String createOrderSendMsgTemplateId;

    @Value("${wx.create.order.send.message.link.url}")
    private String createOrderSendMsgLinkUrl;

    @Autowired
    private WxOrderService wxOrderService;
    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private OrderConfigProperties orderConfigProperties;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param trade
     */
    public void addTrade(Trade trade) {
        Trade tradeNew = trade;
//        tradeNew.setUpdateTime(LocalDateTime.now());
        logger.info("TradeService addTrade param:{}", JSONObject.toJSONString(tradeNew));
        tradeRepository.save(tradeNew);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param trade
     */
    public void updateTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    /**
     * 删除文档
     *
     * @param tid
     */
    public void deleteTrade(String tid) {
        tradeRepository.deleteById(tid);
    }


    /**
     * 订单分页
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public Page<Trade> page(Criteria whereCriteria, TradeQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), Trade.class), request
                .getPageable(), totalSize);
    }

    /**
     * 统计数量
     *
     * @param whereCriteria
     * @param request
     * @return
     */
    public long countNum(Criteria whereCriteria, TradeQueryRequest request) {
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        long totalSize = mongoTemplate.count(query, Trade.class);
        return totalSize;
    }


    /**
     * 根据流程状态时间查询订单
     *
     * @param endDate   endDate
     * @param flowState flowState
     * @return List<Trade>
     */
    public List<Trade> queryTradeByDate(LocalDateTime endDate, FlowState flowState, int PageNum, int pageSize) {
        Criteria criteria = new Criteria();

        criteria.andOperator(Criteria.where("tradeState.flowState").is(flowState.toValue())
                , Criteria.where("tradeState.deliverTime").lt(endDate)
                , Criteria.where("orderType").is(OrderType.NORMAL_ORDER.getOrderTypeId())
        );

        return mongoTemplate.find(
                new Query(criteria).skip(PageNum * pageSize).limit(pageSize)
                , Trade.class);
    }

    /**
     * 查询客户首笔完成的交易号
     *
     * @param customreId
     * @return
     */
    public String queryFirstCompleteTrade(String customreId) {
        Criteria criteria = new Criteria();

        criteria.andOperator(Criteria.where("tradeState.flowState").is(FlowState.COMPLETED.toValue()),
                Criteria.where("buyer.id").is(customreId));
        Query query = new Query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "tradeState.endTime"))).limit(1);

        List<Trade> tradeList = mongoTemplate.find((query), Trade.class);
        if (CollectionUtils.isNotEmpty(tradeList)) {
            return tradeList.get(0).getId();
        }

        return StringUtils.EMPTY;
    }

    /**
     * 根据流程状态时间查询总条数
     *
     * @param endDate
     * @param flowState
     * @return
     */
    public long countTradeByDate(LocalDateTime endDate, FlowState flowState) {
        Criteria criteria = new Criteria();

        criteria.andOperator(Criteria.where("tradeState.flowState").is(flowState.toValue())
                , Criteria.where("tradeState.deliverTime").lt(endDate)
                , Criteria.where("orderType").is(OrderType.NORMAL_ORDER.getOrderTypeId())
        );
        return mongoTemplate.count(new Query(criteria), Trade.class);
    }


    /**
     * C端下单 TODO 作废啦
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> commit(TradeCommitRequest tradeCommitRequest) {

        // 验证用户
        CustomerSimplifyOrderCommitVO customer =
                verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        tradeCommitRequest.setCustomer(customer);

        if (CollectionUtils.isEmpty(tradeCommitRequest.getGoodsChannelTypeSet())) {
            throw new SbcRuntimeException("K-050215");
        }

        Operator operator = tradeCommitRequest.getOperator();
        List<TradeItemGroup> tradeItemGroups = tradeItemService.find(tradeCommitRequest.getTerminalToken());
        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE,
                s.getSuitMarketingFlag()));

        // 预售商品
        boolean isBookingSaleGoods =
                tradeItemGroups.stream().flatMap(s -> s.getTradeItems().stream()).anyMatch(s -> Objects.equals(Boolean.TRUE,
                        s.getIsBookingSaleGoods()));
        // 预约
        boolean isAppointmentSaleGoods =
                tradeItemGroups.stream().flatMap(s -> s.getTradeItems().stream()).anyMatch(s -> Objects.equals(Boolean.TRUE,
                        s.getIsAppointmentSaleGoods()));

        Boolean isCommonGoods = ChannelType.PC_MALL.equals(tradeCommitRequest.getDistributeChannel().getChannelType())
                || suitMarketingFlag || isBookingSaleGoods || isAppointmentSaleGoods;
        // 如果为PC商城下单，将分销商品变为普通商品
        if (isCommonGoods) {
            tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).forEach(tradeItem -> {
                tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
            });
        }

        // 拼团订单--验证
        TradeGrouponCommitForm grouponForm = tradeItemGroups.get(NumberUtils.INTEGER_ZERO).getGrouponForm();
        if (Objects.nonNull(grouponForm)) {
            validGroupon(tradeCommitRequest, tradeItemGroups);
        }


        // 1.查询快照中的购物清单
        // list转map,方便获取
        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));

        List<StoreVO> storeVOList = tradeCacheService.queryStoreList(new ArrayList<>(tradeItemGroupsMap.keySet()));

        Map<Long, CommonLevelVO> storeLevelMap = tradeCustomerService.listCustomerLevelMapByCustomerIdAndIds(
                new ArrayList<>(tradeItemGroupsMap.keySet()), customer.getCustomerId());


        // 1.验证失效的营销信息(目前包括失效的赠品、满系活动、优惠券)
        verifyService.verifyInvalidMarketings(tradeCommitRequest, tradeItemGroups, storeLevelMap);
        // 校验组合购活动信息
        // 2.按店铺包装多个订单信息、订单组信息
        TradeWrapperListRequest tradeWrapperListRequest = new TradeWrapperListRequest();
        tradeWrapperListRequest.setStoreLevelMap(storeLevelMap);
        tradeWrapperListRequest.setStoreVOList(storeVOList);
        tradeWrapperListRequest.setTradeCommitRequest(tradeCommitRequest);
        tradeWrapperListRequest.setTradeItemGroups(tradeItemGroups);
        List<Trade> trades = this.wrapperTradeList(tradeWrapperListRequest);
        TradeGroup tradeGroup = tradeGroupService.wrapperTradeGroup(trades, tradeCommitRequest, grouponForm);
        if (suitMarketingFlag) {
            trades.parallelStream().flatMap(trade -> trade.getTradeItems().stream()).forEach(tradeItem -> {
                tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
            });
        }
        // 处理积分抵扣
        dealPoints(trades, tradeCommitRequest);
        // 预售补充尾款价格
        dealTailPrice(trades, tradeCommitRequest);
        //打包价格
        dealGoodsPackDetail(trades, tradeCommitRequest, customer);
        // 3.批量提交订单
        List<TradeCommitResult> successResults;
        if (tradeGroup != null) {
            successResults = this.createBatchWithGroup(trades, tradeGroup, operator);
        } else {
            successResults = this.createBatch(trades, null, operator);
        }

        try {
            // 4.订单提交成功，删除关联的采购单商品
//            trades.forEach(
//                    trade -> {
//                        List<String> tradeSkuIds =
//                                trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
//                        deletePurchaseOrder(customer.getCustomerId(), tradeSkuIds,
//                                tradeCommitRequest.getDistributeChannel());
//                    }
//            );
            // 5.订单提交成功，删除订单商品快照
            tradeItemService.remove(tradeCommitRequest.getTerminalToken());
            // 6.订单提交成功，增加限售记录
            this.insertRestrictedRecord(trades);
        } catch (Exception e) {
            log.error("Delete the trade sku list snapshot or the purchase order exception," +
                            "trades={}," +
                            "customer={}",
                    JSONObject.toJSONString(trades),
                    customer,
                    e
            );
        }
        return successResults;
    }


    protected void dealTailPrice(List<Trade> trades, TradeCommitRequest tradeCommitRequest) {
        SystemPointsConfigQueryResponse pointsConfig = systemPointsConfigService.querySettingCache();
        final BigDecimal pointWorth = BigDecimal.valueOf(pointsConfig.getPointsWorth());
        trades.stream().filter(t -> Objects.nonNull(t.getIsBookingSaleGoods()) && t.getIsBookingSaleGoods() &&
                Objects.nonNull(t.getBookingType()) && t.getBookingType() == BookingType.EARNEST_MONEY).forEach(t -> {
            TradePrice tradePrice = t.getTradePrice();

            BigDecimal deliveryPrice = Objects.nonNull(tradePrice.getDeliveryPrice())
                    ? tradePrice.getDeliveryPrice() : BigDecimal.ZERO;
            BigDecimal tailPrice = BigDecimal.ZERO;
            if (Objects.nonNull(tradePrice.getTailPrice())) {
                BigDecimal couponPrice = Objects.isNull(tradePrice.getCouponPrice()) ? BigDecimal.ZERO : tradePrice.getCouponPrice();
                //可抵扣的尾款金额
                tailPrice = tradePrice.getTailPrice().subtract(deliveryPrice);
                tradePrice.setCouponPrice(tailPrice.compareTo(couponPrice) == 1 ? couponPrice : tailPrice);
                tailPrice = tailPrice.subtract(Objects.isNull(tradeCommitRequest.getPoints()) ? BigDecimal.ZERO : BigDecimal.valueOf(tradeCommitRequest.getPoints()).divide(pointWorth, 2, BigDecimal.ROUND_HALF_UP));
                tailPrice = tailPrice.subtract(couponPrice);


            } else {
                //膨胀金大于tradeItem的总价，则尾款为0
                tailPrice = tradePrice.getGoodsPrice().subtract(
                        Objects.isNull(tradePrice.getSwellPrice()) ? tradePrice.getEarnestPrice() :
                                tradePrice.getSwellPrice());
            }
            tradePrice.setTailPrice(tailPrice.compareTo(BigDecimal.ZERO) == -1 ? deliveryPrice : tailPrice.add(deliveryPrice));
            tradePrice.setTotalPrice(tradePrice.getEarnestPrice().add(tradePrice.getTailPrice()));
            t.getTradeItems().forEach(i -> {
                i.setSplitPrice(tradePrice.getTotalPrice().subtract(deliveryPrice));
                i.setTailPrice(tradePrice.getTailPrice());
            });
        });
    }


    /**
     * C端尾款下单
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> commitTail(TradeCommitRequest tradeCommitRequest) {

        Trade trade = tradeService.detail(tradeCommitRequest.getTid());
        //校验订单尾款超时未支付，订单作废
        if (LocalDateTime.now().isAfter(trade.getTradeState().getTailEndTime())) {
            log.info("订单尾款超时未支付，发送订单作废MQ，订单号：{}", trade.getId());
            orderProducerService.cancelOrder(trade.getId(), 0L);
            throw new SbcRuntimeException("K-050102");
        }
        if (CollectionUtils.isEmpty(tradeCommitRequest.getGoodsChannelTypeSet())) {
            throw new SbcRuntimeException("K-050215");
        }
        // 验证用户
        CustomerSimplifyOrderCommitVO customer =
                verifyService.simplifyById(tradeCommitRequest.getOperator().getUserId());
        tradeCommitRequest.setCustomer(customer);

        Operator operator = tradeCommitRequest.getOperator();
        List<TradeItemGroup> tradeItemGroups = tradeItemService.find(tradeCommitRequest.getTerminalToken());
        // 组合购标记
        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE,
                s.getSuitMarketingFlag()));

        // 如果为PC商城下单，将分销商品变为普通商品
        if (ChannelType.PC_MALL.equals(tradeCommitRequest.getDistributeChannel().getChannelType()) || suitMarketingFlag) {
            tradeItemGroups.forEach(tradeItemGroup ->
                    tradeItemGroup.getTradeItems().forEach(tradeItem -> {
                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    })
            );
        }
        // 拼团订单--验证
        TradeGrouponCommitForm grouponForm = tradeItemGroups.get(NumberUtils.INTEGER_ZERO).getGrouponForm();
        if (Objects.nonNull(grouponForm)) {
            validGroupon(tradeCommitRequest, tradeItemGroups);
        }
        // 1.查询快照中的购物清单
        // list转map,方便获取
        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));

        List<StoreVO> storeVOList = tradeCacheService.queryStoreList(new ArrayList<>(tradeItemGroupsMap.keySet()));

        Map<Long, StoreVO> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeVOList)) {
            storeMap.putAll(storeVOList.stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s)));
        }

        Map<Long, CommonLevelVO> storeLevelMap = tradeCustomerService.listCustomerLevelMapByCustomerIdAndIds(
                new ArrayList<>(tradeItemGroupsMap.keySet()), customer.getCustomerId());

        // 1.验证失效的营销信息(目前包括失效的赠品、满系活动、优惠券)
        verifyService.verifyInvalidMarketings(tradeCommitRequest, tradeItemGroups, storeLevelMap);
        // 2.尾款订单组装
        wrapperTradeTail(trade, operator, tradeItemGroups, tradeCommitRequest);
        List<Trade> trades = Collections.singletonList(trade);
        // 校验组合购活动信息
        TradeGroup tradeGroup = tradeGroupService.wrapperTradeGroup(trades, tradeCommitRequest, grouponForm);
        // 处理积分抵扣
        dealPoints(trades, tradeCommitRequest);

        dealGoodsPackDetail(trades, tradeCommitRequest, customer);

        // 3.批量提交订单
        List<TradeCommitResult> successResults = new ArrayList<>();
        if (tradeGroup != null) {
            // 生成订单组信息
            if (StringUtils.isEmpty(tradeGroup.getId())) {
                tradeGroup.setId(UUIDUtil.getUUID());
            }
            tradeGroupService.addTradeGroup(tradeGroup);
            trade.setGroupId(tradeGroup.getId());
        }

        // 修改优惠券状态
        //平台优惠券
        List<CouponCodeBatchModifyDTO> dtoList = new ArrayList<>();
        if (tradeGroup != null) {
            TradeCouponVO tradeCoupon = tradeGroup.getCommonCoupon();
            dtoList.add(CouponCodeBatchModifyDTO.builder()
                    .couponCodeId(tradeCoupon.getCouponCodeId())
                    .orderCode(null)
                    .customerId(operator.getUserId())
                    .useStatus(DefaultFlag.YES).build());
        }
        //店铺优惠券
        if (trade.getTradeCoupon() != null) {
            TradeCouponVO tradeCoupon = trade.getTradeCoupon();
            dtoList.add(CouponCodeBatchModifyDTO.builder()
                    .couponCodeId(tradeCoupon.getCouponCodeId())
                    .orderCode(trade.getId())
                    .customerId(trade.getBuyer().getId())
                    .useStatus(DefaultFlag.YES).build());
        }
        if (dtoList.size() > 0) {
            couponCodeProvider.batchModify(CouponCodeBatchModifyRequest.builder().modifyDTOList(dtoList).build());
        }
        // 预售补充尾款价格
        dealTailPrice(trades, tradeCommitRequest);
        TradePrice tradePrice = trade.getTradePrice();
        tradePrice.setCanBackEarnestPrice(tradePrice.getEarnestPrice());
        tradePrice.setCanBackTailPrice(tradePrice.getTailPrice());
        //尾款订单入库
        update(trade, operator);
        successResults.add(new TradeCommitResult(trade.getId(),
                trade.getParentId(), trade.getTradeState(),
                trade.getPaymentOrder(), tradePrice.getTailPrice(),
                trade.getOrderTimeOut(), trade.getSupplier().getStoreName(), trade.getSupplier().getIsSelf()));

        if (Objects.nonNull(trade.getTradePrice()) &&
                Objects.nonNull(trade.getTradePrice().getPoints()) && trade.getTradePrice().getPoints() > 0) {
            // 增加客户积分明细 扣除积分
            customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                    .customerId(trade.getBuyer().getId())
                    .type(OperateType.DEDUCT)
                    .serviceType(PointsServiceType.ORDER_DEDUCTION)
                    .points(trade.getTradePrice().getPoints())
                    .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                    .build());
        }

        try {
            // 4.订单提交成功，删除订单商品快照
            tradeItemService.remove(customer.getCustomerId());
        } catch (Exception e) {
            log.error("Delete the trade sku list snapshot or the purchase order exception," +
                            "trades={}," +
                            "customer={}",
                    JSONObject.toJSONString(trades),
                    customer,
                    e
            );
        }
        return successResults;
    }


    /**
     * 尾款订单
     *
     * @return
     */
    public Trade wrapperTradeTail(Trade trade, Operator operator, List<TradeItemGroup> tradeItemGroups,
                                  TradeCommitRequest tradeCommitRequest) {
        // 查询快照中的购物清单
        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));
        TradeItemGroup tradeItemGroup = tradeItemGroupsMap.get(trade.getSupplier().getStoreId());
        StoreCommitInfoDTO storeCommitInfoDTO = tradeCommitRequest.getStoreCommitInfoList().get(0);
        List<String> skuIds =
                tradeItemGroups.stream().flatMap(g -> g.getTradeItems().stream()).map(TradeItem::getSkuId).collect(Collectors.toList());
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);


        CycleBuyInfoDTO cycleBuyInfoDTO=null;
        if (trade.getCycleBuyFlag()){
            cycleBuyInfoDTO=KsBeanUtil.convert(trade.getTradeCycleBuyInfo(),CycleBuyInfoDTO.class);
            List<String> giftIds= trade.getGifts().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(giftIds)){
                cycleBuyInfoDTO.setCycleBuyGifts(giftIds);
            }

        }

        //【公共方法】修改订单信息验证, 将修改的信息包装成新订单
        return this.validateAndWrapperTrade(trade,
                TradeParams.builder()
                        .backendFlag(false) //表示后端操作
                        .commitFlag(false) //表示修改订单
                        .marketingList(tradeItemGroup.getTradeMarketingList())
                        .tradePrice(trade.getTradePrice())
                        .tradeItems(trade.getTradeItems())
                        .oldGifts(Collections.emptyList()) //修改订单,设置旧赠品
                        .oldTradeItems(trade.getTradeItems()) //修改订单,设置旧商品
                        .storeLevel(null) //修改订单,客户,商家,代理人都无法修改,所以设置为null
                        .couponCodeId(storeCommitInfoDTO.getCouponCodeId())
                        .customer(tradeCommitRequest.getCustomer())
                        .supplier(null)
                        .seller(null)
                        .consigneeId(null)
                        .detailAddress(null)
                        .consigneeUpdateTime(null)
                        .consignee(null)
                        .invoice(null)
                        .invoiceConsignee(null)
                        .deliverWay(null)
                        .payType(null)
                        .buyerRemark(null)
                        .sellerRemark(null)
                        .encloses(null)
                        .ip(operator.getIp())
                        .forceCommit(false)
                        .isBookingSaleGoodsTail(true)
                        .distributeChannel(new DistributeChannel())
                        .goodsInfoViewByIdsResponse(goodsInfoViewByIdsResponse)
                        .cycleBuyInfo(cycleBuyInfoDTO)
                        .build());
    }


    /**
     * 移动端积分商品下单
     */
    @Transactional
    @GlobalTransactional
    public PointsTradeCommitResult pointsCommit(PointsTradeCommitRequest commitRequest) {
        // 1.验证用户
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(commitRequest.getOperator().getUserId());
        commitRequest.setCustomer(customer);

        // 2.包装积分订单信息
        Trade trade = this.wrapperPointsTrade(commitRequest);

        //填充linkedMall类型
        if (Objects.isNull(trade.getThirdPlatformType())) {
            if (trade.getTradeItems().stream().anyMatch(i -> ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType()))) {
                //验证linkedMall是否开启
                if ((!linkedMallTradeService.isOpen())) {
                    throw new SbcRuntimeException("K-050117");
                }
                trade.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
            }
        }

        // 3.提交积分订单
        return this.createPointsTrade(trade,commitRequest);
    }

    /**
     * 拼团订单--验证
     */
    private void validGroupon(TradeCommitRequest request, List<TradeItemGroup> tradeItemGroups) {

        if (tradeItemGroups.size() != NumberUtils.INTEGER_ONE) {
            // 拼团订单只能有一个订单
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        TradeItemGroup tradeItemGroup = tradeItemGroups.get(NumberUtils.INTEGER_ZERO);
        if (tradeItemGroup.getTradeItems().size() != NumberUtils.INTEGER_ONE) {
            // 拼团订单只能有一个商品
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        TradeItem tradeItem = tradeItemGroup.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        if (CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList())
                || DefaultFlag.YES.equals(tradeItemGroup.getStoreBagsFlag())
                || DistributionGoodsAudit.CHECKED.equals(tradeItem.getDistributionGoodsAudit())) {
            // 拼团单不应该具有营销活动、非开店礼包、不是分销商品
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 验证拼团主体信息
        TradeGrouponCommitForm grouponForm = tradeItemGroup.getGrouponForm();
        GrouponTradeValid validInfo = GrouponTradeValid.builder()
                .buyCount(tradeItem.getNum().intValue())
                .customerId(request.getCustomer().getCustomerId())
                .goodsId(tradeItem.getSpuId())
                .goodsInfoId(tradeItem.getSkuId())
                .grouponNo(grouponForm.getGrouponNo())
                .openGroupon(grouponForm.getOpenGroupon())
                .build();
        GrouponGoodsInfoVO grouponGoodsInfo = grouponOrderService.validGrouponOrderBeforeCommit(validInfo);

        grouponForm.setGrouponActivityId(grouponGoodsInfo.getGrouponActivityId());
        grouponForm.setLimitSellingNum(grouponGoodsInfo.getLimitSellingNum());
        grouponForm.setGrouponPrice(grouponGoodsInfo.getGrouponPrice());
    }

    /**
     * 拼团订单--处理
     */
    private void dealGroupon(Trade trade, TradeParams tradeParams) {
        TradeGrouponCommitForm grouponForm = tradeParams.getGrouponForm();
        // 1.将价格设回拼团价
        trade.getTradeItems().forEach(item -> {
            item.setSplitPrice(grouponForm.getGrouponPrice().multiply(new BigDecimal(item.getNum())));
            item.setPrice(grouponForm.getGrouponPrice());
            item.setLevelPrice(grouponForm.getGrouponPrice());
            item.setBuyPoint(NumberUtils.LONG_ZERO);
        });

        TradeItem tradeItem = trade.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        GrouponActivityVO grouponActivity = grouponActivityQueryProvider.getById(
                new GrouponActivityByIdRequest(grouponForm.getGrouponActivityId())).getContext().getGrouponActivity();
        grouponForm.setFreeDelivery(grouponActivity.isFreeDelivery());

        // 2.设置订单拼团信息
        TradeGroupon tradeGroupon = TradeGroupon.builder()
                .grouponNo(grouponForm.getGrouponNo())
                .grouponActivityId(grouponActivity.getGrouponActivityId())
                .goodInfoId(tradeItem.getSkuId())
                .goodId(tradeItem.getSpuId())
                .returnNum(NumberUtils.INTEGER_ZERO)
                .returnPrice(BigDecimal.ZERO)
                .grouponOrderStatus(GrouponOrderStatus.WAIT)
                .leader(grouponForm.getOpenGroupon())
                .payState(PayState.NOT_PAID).build();
        trade.setGrouponFlag(Boolean.TRUE);
        trade.setTradeGroupon(tradeGroupon);
        trade.setOrderTimeOut(LocalDateTime.now().plusMinutes(5L));

        // 3.如果是开团，设置团实例
        if (grouponForm.getOpenGroupon()) {
            // 设置团实例
            String grouponNo = generatorService.generateGrouponNo();
            GrouponInstance grouponInstance = GrouponInstance.builder()
                    .id(grouponNo)
                    .grouponNo(grouponNo)
                    .grouponActivityId(grouponActivity.getGrouponActivityId())
                    .grouponNum(grouponActivity.getGrouponNum())
                    .joinNum(NumberUtils.INTEGER_ZERO)
                    .customerId(tradeParams.getCustomer().getCustomerId())
                    .grouponStatus(GrouponOrderStatus.UNPAY)
                    .build();
            // 修改拼团信息中的团号
            tradeGroupon.setGrouponNo(grouponInstance.getGrouponNo());
            grouponInstanceRepository.save(grouponInstance);
        }

        // 4.如果活动为包邮，设置运费为0
        TradePrice tradePrice = trade.getTradePrice();
        if (grouponActivity.isFreeDelivery()) {
            tradePrice.setDeliveryPrice(BigDecimal.ZERO);
        }

        // 5.增加拼团活动单品的购买量
        GrouponRecordIncrBuyNumRequest request = GrouponRecordIncrBuyNumRequest.builder()
                .buyNum(tradeItem.getNum().intValue())
                .customerId(tradeParams.getCustomer().getCustomerId())
                .goodsId(tradeItem.getSpuId())
                .goodsInfoId(tradeItem.getSkuId())
                .grouponActivityId(grouponActivity.getGrouponActivityId())
                .limitSellingNum(grouponForm.getLimitSellingNum()).build();
        grouponRecordProvider.incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(request);

    }


    /**
     * 从购物车中删除商品信息
     */
    protected void deletePurchaseOrder(String customerId, List<String> skuIds, DistributeChannel distributeChannel) {
        PurchaseRequest request = PurchaseRequest.builder()
                .customerId(customerId)
                .goodsInfoIds(skuIds).inviteeId(getPurchaseInviteeId(distributeChannel))
                .build();
        purchaseService.delete(request);
    }

    /**
     * 获取购物车归属
     * 当且仅当为店铺精选时，需要根据InviteeId区分购物车
     */
    public String getPurchaseInviteeId(DistributeChannel distributeChannel) {

        if (null != distributeChannel && Objects.equals(distributeChannel.getChannelType(), ChannelType.SHOP)) {
            return distributeChannel.getInviteeId();
        }
        return Constants.PURCHASE_DEFAULT;
    }

    /**
     * 将用户下单信息 根据不同店铺 包装成 多个订单 [前端客户下单]
     * 1.校验营销活动
     * 2.校验商品是否可以下单
     * 3.填充订单商品,订单赠品,订单营销信息...
     *
     * @return
     */
    public List<Trade> wrapperTradeList(TradeWrapperListRequest tradeWrapperListRequest) {
        TradeCommitRequest tradeCommitRequest = tradeWrapperListRequest.getTradeCommitRequest();
        List<TradeItemGroup> tradeItemGroups = tradeWrapperListRequest.getTradeItemGroups();
        Map<Long, CommonLevelVO> storeLevelMap = tradeWrapperListRequest.getStoreLevelMap();
        CustomerSimplifyOrderCommitVO customer = tradeCommitRequest.getCustomer();
        List<Trade> trades = new ArrayList<>();
        // 1.查询快照中的购物清单
        // list转map,方便获取
        Map<Long, TradeItemGroup> tradeItemGroupsMap = tradeItemGroups.stream().collect(
                Collectors.toMap(g -> g.getSupplier().getStoreId(), Function.identity()));

        List<StoreVO> storeVOList = tradeWrapperListRequest.getStoreVOList();
        Map<Long, StoreVO> storeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeVOList)) {
            storeMap.putAll(storeVOList.stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s)));
        }

        List<String> skuIds =
                tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).map(TradeItem::getSkuId).collect(Collectors.toList());

        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoViewByIdsResponse.getGoodsInfos();

        // 2.遍历各个店铺下单信息
        for (StoreCommitInfoDTO storeCommitInfoParam : tradeCommitRequest.getStoreCommitInfoList()) {
            TradeItemGroup group = tradeItemGroupsMap.get(storeCommitInfoParam.getStoreId());

//                    填充分销商品审核状态
            group.getTradeItems().forEach(item -> {
                GoodsInfoVO goodsInfoVO =
                        goodsInfoVOList.stream().filter(g -> g.getGoodsInfoId().equals(item.getSkuId())).findFirst().orElse(null);
                if (Objects.nonNull(goodsInfoVO) && Objects.nonNull(goodsInfoVO.getDistributionGoodsAudit()) && Objects.isNull(item.getDistributionGoodsAudit())) {
                    item.setDistributionGoodsAudit(goodsInfoVO.getDistributionGoodsAudit());
                }
                item.setProviderName(group.getSupplier().getStoreName());
            });
            // 2.1.组装发票信息(缺少联系人,联系方式), 统一入参, 方便调用公共方法
            Invoice invoice = Invoice.builder()
                    .generalInvoice(KsBeanUtil.convert(storeCommitInfoParam.getGeneralInvoice(), GeneralInvoice.class))
                    .specialInvoice(KsBeanUtil.convert(storeCommitInfoParam.getSpecialInvoice(), SpecialInvoice.class))
                    .address(storeCommitInfoParam.getInvoiceAddressDetail())
                    .addressId(storeCommitInfoParam.getInvoiceAddressId())
                    .email(storeCommitInfoParam.getInvoiceEmail())
                    .projectId(storeCommitInfoParam.getInvoiceProjectId())
                    .projectName(storeCommitInfoParam.getInvoiceProjectName())
                    .projectUpdateTime(storeCommitInfoParam.getInvoiceProjectUpdateTime())
                    .type(storeCommitInfoParam.getInvoiceType())
                    .sperator(storeCommitInfoParam.isSpecialInvoiceAddress())
                    .updateTime(storeCommitInfoParam.getInvoiceAddressUpdateTime())
                    .taxNo(setInvoiceTaxNo(storeCommitInfoParam.getInvoiceType(), storeCommitInfoParam.getGeneralInvoice(), storeCommitInfoParam.getSpecialInvoice()))
                    .build();
            if (storeMap.get(group.getSupplier().getStoreId()) == null) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            //周期购使用单品运费
            DefaultFlag freightTemplateType = Objects.nonNull(group.getCycleBuyInfo())
                    ? DefaultFlag.YES
                    : storeMap.get(group.getSupplier().getStoreId()).getFreightTemplateType();
            group.getSupplier().setFreightTemplateType(freightTemplateType);

            // 2.2.【公共方法】下单信息验证, 将信息包装成订单
            trades.add(this.validateAndWrapperTrade(new Trade(),
                    TradeParams.builder()
                            .backendFlag(false) //表示前端操作
                            .commitFlag(true) //表示下单
                            .marketingList(group.getTradeMarketingList())
                            .directChargeMobile(tradeCommitRequest.getDirectChargeMobile())
                            .emallSessionId(tradeCommitRequest.getEmallSessionId())
                            .couponCodeId(storeCommitInfoParam.getCouponCodeId())
                            .tradePrice(new TradePrice())
                            .tradeItems(group.getTradeItems())
                            .oldGifts(Collections.emptyList())//下单,非修改订单
                            .oldTradeItems(Collections.emptyList())//下单,非修改订单
                            .storeLevel(storeLevelMap.get(group.getSupplier().getStoreId()))
                            .customer(customer)
                            .supplier(group.getSupplier())
                            .seller(null) //客户下单
                            .consigneeId(tradeCommitRequest.getConsigneeId())
                            .detailAddress(tradeCommitRequest.getConsigneeAddress())
                            .consigneeUpdateTime(tradeCommitRequest.getConsigneeUpdateTime())
                            .consignee(null) //客户下单,不可填写临时收货地址
                            .invoice(invoice)
                            .invoiceConsignee(null) //客户下单,不可填写发票临时收货地址
                            .deliverWay(storeCommitInfoParam.getDeliverWay())
                            .payType(storeCommitInfoParam.getPayType())
                            .buyerRemark(storeCommitInfoParam.getBuyerRemark())
                            .sellerRemark(null) //客户下单,无卖家备注
                            .encloses(storeCommitInfoParam.getEncloses())
                            .ip(tradeCommitRequest.getOperator().getIp())
                            .platform(Platform.CUSTOMER)
                            .forceCommit(tradeCommitRequest.isForceCommit())
                            .orderSource(tradeCommitRequest.getOrderSource())
                            .distributeChannel(tradeCommitRequest.getDistributeChannel())
                            .storeBagsFlag(group.getStoreBagsFlag())
                            .shopName(tradeCommitRequest.getShopName())
                            .isDistributor(tradeCommitRequest.getIsDistributor())
                            .storeOpenFlag(storeCommitInfoParam.getStoreOpenFlag())
                            .openFlag(tradeCommitRequest.getOpenFlag())
                            .grouponForm(group.getGrouponForm())
                            .shareUserId(customer.getCustomerId().equals(tradeCommitRequest.getShareUserId())
                                    ? null : tradeCommitRequest.getShareUserId())
                            .isFlashSaleGoods(tradeCommitRequest.getIsFlashSaleGoods())
                            .suitMarketingFlag(group.getSuitMarketingFlag())
                            .suitScene(group.getSuitScene())
                            .isBookingSaleGoods(tradeCommitRequest.getIsBookingSaleGoods())
                            .tailNoticeMobile(tradeCommitRequest.getTailNoticeMobile())
                            .goodsInfoViewByIdsResponse(goodsInfoViewByIdsResponse)
                            .cycleBuyInfo(group.getCycleBuyInfo())
                            .promoteUserId(tradeCommitRequest.getPromoteUserId() == null ? "" : tradeCommitRequest.getPromoteUserId().toString())
                            .source(tradeCommitRequest.getSource() == null ? "" : tradeCommitRequest.getSource().toString())
                            .miniProgramScene(tradeCommitRequest.getMiniProgramScene())
                            .build()));
        }

//        tradeCommitRequest.getStoreCommitInfoList().forEach(
//                i -> {
//                    TradeItemGroup group = tradeItemGroupsMap.get(i.getStoreId());
//
////                    填充分销商品审核状态
//                    group.getTradeItems().forEach(item -> {
//                        GoodsInfoVO goodsInfoVO =
//                                goodsInfoVOList.stream().filter(g -> g.getGoodsInfoId().equals(item.getSkuId())).findFirst().orElse(null);
//                        if (Objects.nonNull(goodsInfoVO) && Objects.nonNull(goodsInfoVO.getDistributionGoodsAudit()) && Objects.isNull(item.getDistributionGoodsAudit())) {
//                            item.setDistributionGoodsAudit(goodsInfoVO.getDistributionGoodsAudit());
//                        }
//                        item.setProviderName(group.getSupplier().getStoreName());
//                    });
//                    // 2.1.组装发票信息(缺少联系人,联系方式), 统一入参, 方便调用公共方法
//                    Invoice invoice = Invoice.builder()
//                            .generalInvoice(KsBeanUtil.convert(i.getGeneralInvoice(), GeneralInvoice.class))
//                            .specialInvoice(KsBeanUtil.convert(i.getSpecialInvoice(), SpecialInvoice.class))
//                            .address(i.getInvoiceAddressDetail())
//                            .addressId(i.getInvoiceAddressId())
//                            .email(i.getInvoiceEmail())
//                            .projectId(i.getInvoiceProjectId())
//                            .projectName(i.getInvoiceProjectName())
//                            .projectUpdateTime(i.getInvoiceProjectUpdateTime())
//                            .type(i.getInvoiceType())
//                            .sperator(i.isSpecialInvoiceAddress())
//                            .updateTime(i.getInvoiceAddressUpdateTime())
//                            .taxNo(setInvoiceTaxNo(i.getInvoiceType(), i.getGeneralInvoice(), i.getSpecialInvoice()))
//                            .build();
//                    if (storeMap.get(group.getSupplier().getStoreId()) == null) {
//                        throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//                    }
//                    //周期购使用单品运费
//                    DefaultFlag freightTemplateType = Objects.nonNull(group.getCycleBuyInfo())
//                            ? DefaultFlag.YES
//                            : storeMap.get(group.getSupplier().getStoreId()).getFreightTemplateType();
//                    group.getSupplier().setFreightTemplateType(freightTemplateType);
//
//                    // 2.2.【公共方法】下单信息验证, 将信息包装成订单
//                    trades.add(this.validateAndWrapperTrade(new Trade(),
//                            TradeParams.builder()
//                                    .backendFlag(false) //表示前端操作
//                                    .commitFlag(true) //表示下单
//                                    .marketingList(group.getTradeMarketingList())
//                                    .directChargeMobile(tradeCommitRequest.getDirectChargeMobile())
//                                    .emallSessionId(tradeCommitRequest.getEmallSessionId())
//                                    .couponCodeId(i.getCouponCodeId())
//                                    .tradePrice(new TradePrice())
//                                    .tradeItems(group.getTradeItems())
//                                    .oldGifts(Collections.emptyList())//下单,非修改订单
//                                    .oldTradeItems(Collections.emptyList())//下单,非修改订单
//                                    .storeLevel(storeLevelMap.get(group.getSupplier().getStoreId()))
//                                    .customer(customer)
//                                    .supplier(group.getSupplier())
//                                    .seller(null) //客户下单
//                                    .consigneeId(tradeCommitRequest.getConsigneeId())
//                                    .detailAddress(tradeCommitRequest.getConsigneeAddress())
//                                    .consigneeUpdateTime(tradeCommitRequest.getConsigneeUpdateTime())
//                                    .consignee(null) //客户下单,不可填写临时收货地址
//                                    .invoice(invoice)
//                                    .invoiceConsignee(null) //客户下单,不可填写发票临时收货地址
//                                    .deliverWay(i.getDeliverWay())
//                                    .payType(i.getPayType())
//                                    .buyerRemark(i.getBuyerRemark())
//                                    .sellerRemark(null) //客户下单,无卖家备注
//                                    .encloses(i.getEncloses())
//                                    .ip(tradeCommitRequest.getOperator().getIp())
//                                    .platform(Platform.CUSTOMER)
//                                    .forceCommit(tradeCommitRequest.isForceCommit())
//                                    .orderSource(tradeCommitRequest.getOrderSource())
//                                    .distributeChannel(tradeCommitRequest.getDistributeChannel())
//                                    .storeBagsFlag(group.getStoreBagsFlag())
//                                    .shopName(tradeCommitRequest.getShopName())
//                                    .isDistributor(tradeCommitRequest.getIsDistributor())
//                                    .storeOpenFlag(i.getStoreOpenFlag())
//                                    .openFlag(tradeCommitRequest.getOpenFlag())
//                                    .grouponForm(group.getGrouponForm())
//                                    .shareUserId(customer.getCustomerId().equals(tradeCommitRequest.getShareUserId())
//                                            ? null : tradeCommitRequest.getShareUserId())
//                                    .isFlashSaleGoods(tradeCommitRequest.getIsFlashSaleGoods())
//                                    .suitMarketingFlag(group.getSuitMarketingFlag())
//                                    .suitScene(group.getSuitScene())
//                                    .isBookingSaleGoods(tradeCommitRequest.getIsBookingSaleGoods())
//                                    .tailNoticeMobile(tradeCommitRequest.getTailNoticeMobile())
//                                    .goodsInfoViewByIdsResponse(goodsInfoViewByIdsResponse)
//                                    .cycleBuyInfo(group.getCycleBuyInfo())
//                                    .promoteUserId(tradeCommitRequest.getPromoteUserId())
//                                    .source(tradeCommitRequest.getSource())
//                                    .build()));
//                }
//        );
        return trades;
    }

    public Trade wrapperPointsTrade(PointsTradeCommitRequest commitRequest) {
        Trade trade = new Trade();
        // 设置订单基本信息(购买人,商家,收货地址,配送方式,支付方式,备注,订单商品,订单总价...)
        Optional<CommonLevelVO> commonLevelVO;
        boolean flag = true;
        commonLevelVO =
                Optional.of(fromCustomerLevel(customerLevelQueryProvider.getDefaultCustomerLevel().getContext()));
        trade.setBuyer(Buyer.fromCustomer(commitRequest.getCustomer(), commonLevelVO, flag));
        trade.setSupplier(KsBeanUtil.convert(commitRequest.getPointsTradeItemGroup().getSupplier(), Supplier.class));
        TradeItem tradeItem = KsBeanUtil.convert(commitRequest.getPointsTradeItemGroup().getTradeItem(),
                TradeItem.class);
        tradeItem.setOid(generatorService.generateOid());
        if (StringUtils.isBlank(tradeItem.getAdminId())) {
            tradeItem.setAdminId(String.format("%d", trade.getSupplier().getSupplierId()));
        }

        trade.setId(generatorService.generateTid());
        trade.setPlatform(Platform.CUSTOMER);
        trade.setOrderSource(OrderSource.WECHAT);
        trade.setOrderType(OrderType.POINTS_ORDER);
        trade.setPointsOrderType(PointsOrderType.POINTS_GOODS);
        trade.setConsignee(wrapperConsignee(commitRequest.getConsigneeId(), commitRequest.getConsigneeAddress(),
                commitRequest.getConsigneeUpdateTime(), null));
        trade.setDeliverWay(DeliverWay.EXPRESS);
        trade.setPayInfo(PayInfo.builder()
                .payTypeId(String.format("%d", PayType.ONLINE.toValue()))
                .payTypeName(PayType.ONLINE.name())
                .desc(PayType.ONLINE.getDesc())
                .build());
        trade.setBuyerRemark(commitRequest.getBuyerRemark());
        trade.setRequestIp(commitRequest.getOperator().getIp());
        trade.setTradeItems(Collections.singletonList(tradeItem));
        Long currentPoint = externalProvider.getByUserNoPoint(
                FanDengPointRequest.builder().userNo(commitRequest.getCustomer().getFanDengUserNo()).build()).getContext().getCurrentPoint();
        if (currentPoint.compareTo(commitRequest.getPointsTradeItemGroup().getTradeItem()
                .getPoints() * commitRequest.getPointsTradeItemGroup().getTradeItem().getNum()) == -1) {
            throw new SbcRuntimeException("K-120004");
        }
        trade.setTradePrice(TradePrice.builder().points(commitRequest.getPointsTradeItemGroup().getTradeItem()
                .getPoints() * commitRequest.getPointsTradeItemGroup().getTradeItem().getNum()).build());

        return trade;
    }

    /**
     * 设置纳税人识别号
     *
     * @param invoiceType    发票类型 0：普通发票 1：增值税发票 -1：无
     * @param generalInvoice 普票信息
     * @param specialInvoice 增票信息
     * @return
     */
    private String setInvoiceTaxNo(
            Integer invoiceType, GeneralInvoiceDTO generalInvoice, SpecialInvoiceDTO specialInvoice) {
        String taxNo = "";
        //不需要发票
        if (!InvoiceType.NORMAL.equals(invoiceType) && !InvoiceType.SPECIAL.equals(invoiceType)) {
            return taxNo;
        }
        //增票
        if (InvoiceType.SPECIAL.equals(invoiceType)) {
            taxNo = Objects.nonNull(specialInvoice) ? specialInvoice.getIdentification() : "";
        } else {
            taxNo = Objects.nonNull(generalInvoice) ? generalInvoice.getIdentification() : "";
        }
        return taxNo;
    }

    /**
     * 分摊积分信息
     * @param trades
     * @param tradeCommitRequest
     */
    public void dealPoints(List<Trade> trades, TradeCommitRequest tradeCommitRequest) {
        SystemPointsConfigQueryResponse pointsConfig = systemPointsConfigService.querySettingCache();
        final BigDecimal pointWorth = BigDecimal.valueOf(pointsConfig.getPointsWorth());
        /*
         * 商品积分设置
         */
        if (EnableStatus.ENABLE.equals(pointsConfig.getStatus())
                && PointsUsageFlag.GOODS.equals(pointsConfig.getPointsUsageFlag())) {
            //合计商品
            Long sumPoints = trades.stream().flatMap(trade -> trade.getTradeItems().stream())
                    .filter(k -> Objects.nonNull(k.getBuyPoint()))
                    .mapToLong(k -> k.getBuyPoint() * k.getNum()).sum();
            if (sumPoints <= 0) {
                return;
            }
            tradeCommitRequest.setPoints(sumPoints);

            // 如果使用积分 校验可使用积分
            verifyService.verifyPoints(trades, tradeCommitRequest, pointsConfig);
            //积分均摊，积分合计，不需要平滩价
            trades.forEach(trade -> {
                //积分均摊
                trade.getTradeItems().stream()
                        .filter(tradeItem -> Objects.nonNull(tradeItem.getBuyPoint()))
                        .forEach(tradeItem -> {
                            tradeItem.setPoints(tradeItem.getBuyPoint() * tradeItem.getNum());
                            tradeItem.setPointsPrice(BigDecimal.valueOf(tradeItem.getPoints()).divide(pointWorth, 2,
                                    BigDecimal.ROUND_HALF_UP));
                        });
                // 计算积分抵扣额(pointsPrice、points)，并追加积分抵扣金额
                TradePrice tradePrice = trade.getTradePrice();
                Long points = tradeItemService.calcSkusTotalPoints(trade.getTradeItems());
                tradePrice.setPoints(points);
                tradePrice.setBuyPoints(points);
            });
        } else {
            /*
             * 订单积分设置
             */
            //将buyPoint置零
            trades.stream().flatMap(trade -> trade.getTradeItems().stream()).forEach(tradeItem -> tradeItem.setBuyPoint(0L));

            if (tradeCommitRequest.getPoints() == null || tradeCommitRequest.getPoints() <= 0) {
                return;
            }
            //商城积分体系未开启
            if (!EnableStatus.ENABLE.equals(pointsConfig.getStatus())) {
                throw new SbcRuntimeException("K-000018");
            }
            // 如果使用积分 校验可使用积分
            verifyService.verifyPoints(trades, tradeCommitRequest, pointsConfig);

            List<TradeItem> items =
                    trades.stream().flatMap(trade -> trade.getTradeItems().stream()).collect(Collectors.toList());

            // 设置关联商品的积分均摊
            BigDecimal pointsTotalPrice = BigDecimal.valueOf(tradeCommitRequest.getPoints()).divide(pointWorth, 2,
                    BigDecimal.ROUND_HALF_UP);
            tradeItemService.calcPoints(items, pointsTotalPrice, tradeCommitRequest.getPoints(), pointWorth);

            // 设置关联商品的均摊价
            BigDecimal total = tradeItemService.calcSkusTotalPrice(items);
            tradeItemService.calcSplitPrice(items, total.subtract(pointsTotalPrice), total);

            Map<Long, List<TradeItem>> itemsMap = items.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
            itemsMap.keySet().forEach(storeId -> {
                // 找到店铺对应订单的价格信息
                Trade trade = trades.stream()
                        .filter(t -> t.getSupplier().getStoreId().equals(storeId)).findFirst().orElse(null);
                TradePrice tradePrice = trade.getTradePrice();

                // 计算积分抵扣额(pointsPrice、points)，并追加至订单优惠金额、积分抵扣金额
                BigDecimal pointsPrice = tradeItemService.calcSkusTotalPointsPrice(itemsMap.get(storeId));
                Long points = tradeItemService.calcSkusTotalPoints(itemsMap.get(storeId));
                tradePrice.setPointsPrice(pointsPrice);
                tradePrice.setPoints(points);
                tradePrice.setPointWorth(pointsConfig.getPointsWorth());
                // 重设订单总价
                tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(pointsPrice));
            });
        }
    }

    /**
     * 分摊知豆
     * @param trades
     * @param tradeCommitRequest
     */
    public void dealKnowledge(List<Trade> trades, TradeCommitRequest tradeCommitRequest) {

        if (tradeCommitRequest.getKnowledge() == null || tradeCommitRequest.getKnowledge() <= 0) {
            return;
        }
        //将buyKnowledge置零
        trades.stream().flatMap(trade -> trade.getTradeItems().stream()).forEach(tradeItem -> tradeItem.setBuyKnowledge(0L));

        if (StringUtils.isEmpty(tradeCommitRequest.getCustomer().getFanDengUserNo())) {
            throw new SbcRuntimeException("K-000018");
        }
        Integer fanDengUserId = Integer.valueOf(tradeCommitRequest.getCustomer().getFanDengUserNo());
        CounselorDto counselorDto = customerProvider.isCounselor(fanDengUserId).getContext();
        if (counselorDto == null || counselorDto.getProfit() < tradeCommitRequest.getKnowledge()) {
            throw new SbcRuntimeException("K-000018");
        }
        final BigDecimal knowledgeWorth = new BigDecimal(100);
        Trade tradeKnow = trades.get(0);
        //锁定知豆
        FanDengKnowledgeLockRequest knowledgeLockRequest = FanDengKnowledgeLockRequest.builder()
                .desc("提交订单锁定(订单号:" + tradeKnow.getId() + ")")
                .beans(tradeCommitRequest.getKnowledge())
                .userNo(tradeCommitRequest.getCustomer().getFanDengUserNo())
                .sourceId(tradeKnow.getId())
                .build();
        String deductCode = externalProvider.knowledgeLock(knowledgeLockRequest).getContext().getDeductionCode();
        tradeKnow.setDeductCode(deductCode);
        List<TradeItem> items =
                trades.stream().flatMap(trade -> trade.getTradeItems().stream()).collect(Collectors.toList());

        // 设置关联商品的积分均摊
        BigDecimal knowledgeTotalPrice = BigDecimal.valueOf(tradeCommitRequest.getKnowledge()).divide(knowledgeWorth, 2,
                BigDecimal.ROUND_HALF_UP);
        tradeItemService.calcKnowledge(items, knowledgeTotalPrice, tradeCommitRequest.getKnowledge(), knowledgeWorth);

        // 设置关联商品的均摊价
        BigDecimal total = tradeItemService.calcSkusTotalPrice(items);
        tradeItemService.calcSplitPrice(items, total.subtract(knowledgeTotalPrice), total);

        Map<Long, List<TradeItem>> itemsMap = items.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
        itemsMap.keySet().forEach(storeId -> {
            // 找到店铺对应订单的价格信息
            Trade trade = trades.stream()
                    .filter(t -> t.getSupplier().getStoreId().equals(storeId)).findFirst().orElse(null);
            TradePrice tradePrice = trade.getTradePrice();

            // 计算知豆抵扣额(pointsPrice、points)，并追加至订单优惠金额、知豆抵扣金额
            BigDecimal knowledgePrice = tradeItemService.calcSkusTotalKnowledgePrice(itemsMap.get(storeId));
            Long knowledge = tradeItemService.calcSkusTotalKnowledge(itemsMap.get(storeId));
            tradePrice.setKnowledgePrice(knowledgePrice);
            tradePrice.setKnowledge(knowledge);
            tradePrice.setKnowledgeWorth(100L);
            // 重设订单总价
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(knowledgePrice));
        });

    }


    /**
     * 处理打包商品
     */
    public void dealGoodsPackDetail(List<Trade> trades, TradeCommitRequest tradeCommitRequest, CustomerSimplifyOrderCommitVO customer) {
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        Trade trade = trades.get(0);
        //判断打包商品
        //获取商品的打包信息，
        List<String> mainGoodsIdList = trade.getTradeItems().stream().map(TradeItem::getSpuId).collect(Collectors.toList());
        BaseResponse<List<GoodsPackDetailResponse>> packResponse = goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(mainGoodsIdList));
        List<GoodsPackDetailResponse> goodsPackDetailList = packResponse.getContext();

        List<String> childGoodsInfoIdList = new ArrayList<>();

        Map<String, List<GoodsPackDetailResponse>> packId2GoodsPackDetailMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goodsPackDetailList)) {
            for (GoodsPackDetailResponse goodsPackDetailParam : goodsPackDetailList) {
                List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.computeIfAbsent(goodsPackDetailParam.getPackId(), k -> new ArrayList<>());
                goodsPackDetailListTmp.add(goodsPackDetailParam);
                childGoodsInfoIdList.add(goodsPackDetailParam.getGoodsInfoId());
            }
        }

//        GoodsInfoListByConditionRequest goodsInfoListByConditionRequest = new GoodsInfoListByConditionRequest();
//        goodsInfoListByConditionRequest.setGoodsInfoIds(childGoodsInfoIdList);
//        BaseResponse<GoodsInfoListByConditionResponse> goodsInfoListByConditionResponseBaseResponse =
//                goodsInfoQueryProvider.listByCondition(goodsInfoListByConditionRequest);
//        GoodsInfoListByConditionResponse context = goodsInfoListByConditionResponseBaseResponse.getContext();
//        Map<String, GoodsInfoVO> skuId2GoodsInfoMap
//                = context.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));

        List<TradeItem> tradeItemListTmp = new ArrayList<>();
        for (TradeItem tradeItemParam : trade.getTradeItems()) {
            List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.get(tradeItemParam.getSpuId());
            if (CollectionUtils.isNotEmpty(goodsPackDetailListTmp)) {
                //拆分
                BigDecimal splitPrice = tradeItemParam.getSplitPrice() == null ? BigDecimal.ZERO : tradeItemParam.getSplitPrice();
                Long points = tradeItemParam.getPoints() == null ? 0L : tradeItemParam.getPoints();
                Long knowledge = tradeItemParam.getKnowledge() == null ? 0L : tradeItemParam.getKnowledge();
                BigDecimal rateAll = new BigDecimal("100");

                //优先计算 子商品的价格，最后计算主要商品的价格；
                BigDecimal surplusSplitPrice = splitPrice;
                BigDecimal surplusPoint = new BigDecimal(points + "");
                BigDecimal surplusKnowledge = new BigDecimal(knowledge + "");

                //获取子商品信息
                List<TradeItemDTO> TradeItemDTOList = new ArrayList<>();
                for (GoodsPackDetailResponse goodsPackDetailTmp : goodsPackDetailListTmp) {
                    if (!Objects.equals(goodsPackDetailTmp.getGoodsId(), goodsPackDetailTmp.getPackId())) {
                        TradeItemDTOList.add(TradeItemDTO.builder().skuId(goodsPackDetailTmp.getGoodsInfoId()).num(Long.valueOf(goodsPackDetailTmp.getCount())).build());
                    }
                }

                TradePurchaseRequest tradePurchaseRequest = new TradePurchaseRequest();
                tradePurchaseRequest.setCustomer(customer);
                tradePurchaseRequest.setTradeItems(TradeItemDTOList);
                tradePurchaseRequest.setGoodsChannelTypeSet(tradeCommitRequest.getGoodsChannelTypeSet());

                List<TradeItemGroup> tradeItemGroups = this.getTradeItemList(tradePurchaseRequest);
                List<TradeItem> tradeItems = tradeItemGroups.stream().flatMap(tradeItemGroup -> tradeItemGroup.getTradeItems().stream()).collect(Collectors.toList());
                Map<String, TradeItem> skuId2TradeItemMap = tradeItems.stream().collect(Collectors.toMap(TradeItem::getSkuId, Function.identity(), (k1, k2) -> k1));

                GoodsPackDetailResponse mainGoodsPackDetailTmp = null;
                for (GoodsPackDetailResponse goodsPackDetailTmp : goodsPackDetailListTmp) {
                    if (!Objects.equals(goodsPackDetailTmp.getGoodsId(), goodsPackDetailTmp.getPackId())) {
                        BigDecimal splitPriceTmp = splitPrice.multiply(goodsPackDetailTmp.getShareRate()).divide(rateAll,2, RoundingMode.HALF_UP);
                        BigDecimal pointsTmp = new BigDecimal(points + "").multiply(goodsPackDetailTmp.getShareRate()).divide(rateAll,0, RoundingMode.HALF_UP);
                        BigDecimal knowledgeTmp = new BigDecimal(knowledge + "").multiply(goodsPackDetailTmp.getShareRate()).divide(rateAll,0, RoundingMode.HALF_UP);

                        surplusSplitPrice = surplusSplitPrice.subtract(splitPriceTmp);
                        surplusPoint = surplusPoint.subtract(pointsTmp);
                        surplusKnowledge = surplusKnowledge.subtract(knowledgeTmp);

                        TradeItem tradeItem = skuId2TradeItemMap.get(goodsPackDetailTmp.getGoodsInfoId());
                        tradeItem.setOid(generatorService.generateOid());
//                        if (StringUtils.isBlank(tradeItem.getAdminId())) {
//                            tradeItem.setAdminId(String.format("%d", goodsPackDetailTmp.getSupplier().getSupplierId()));
//                        }
                        tradeItem.setSplitPrice(splitPriceTmp);
                        tradeItem.setPoints(pointsTmp.longValue());
                        tradeItem.setKnowledge(knowledgeTmp.longValue());

                        if (tradeItem.getPoints() > 0) {
                            tradeItem.setPointsPrice(pointsTmp.divide(new BigDecimal("100")));
                        } else if (tradeItem.getKnowledge() > 0) {
                            tradeItem.setPointsPrice(knowledgeTmp.divide(new BigDecimal("100")));
                        } else {
                            tradeItem.setPointsPrice(BigDecimal.ZERO);
                        }

                        BigDecimal sumPrice = tradeItem.getSplitPrice().add(tradeItem.getPointsPrice());
                        tradeItem.setPrice(sumPrice.divide(new BigDecimal(tradeItem.getNum()+""), 2, RoundingMode.HALF_UP));
                        tradeItem.setPackId(goodsPackDetailTmp.getPackId());
                        tradeItemListTmp.add(tradeItem); // add1
                        log.info("TradeService.dealGoodsPackDetail goods_Id:{} packId:{}", tradeItem.getSpuId(), tradeItem.getPackId());
                    } else {
                        mainGoodsPackDetailTmp = goodsPackDetailTmp;
                    }
                }


                tradeItemParam.setSplitPrice(surplusSplitPrice);
                tradeItemParam.setPoints(surplusPoint.longValue());
                tradeItemParam.setKnowledge(surplusKnowledge.longValue());

                if (tradeItemParam.getPoints() > 0) {
                    tradeItemParam.setPointsPrice(surplusPoint.divide(new BigDecimal("100")));
                } else if (tradeItemParam.getKnowledge() > 0) {
                    tradeItemParam.setPointsPrice(surplusKnowledge.divide(new BigDecimal("100")));
                } else {
                    tradeItemParam.setPointsPrice(BigDecimal.ZERO);
                }

                BigDecimal sumPrice = tradeItemParam.getSplitPrice().add(tradeItemParam.getPointsPrice());
                tradeItemParam.setPrice(sumPrice.divide(new BigDecimal(tradeItemParam.getNum()+""), 2, RoundingMode.HALF_UP));  //此处计算不正确
                tradeItemParam.setPackId(mainGoodsPackDetailTmp == null ? tradeItemParam.getSpuId() : mainGoodsPackDetailTmp.getPackId());

                log.info("TradeService.dealGoodsPackDetail goods_Id:{} packId:{}", tradeItemParam.getSpuId(), tradeItemParam.getPackId());
            }
            tradeItemListTmp.add(tradeItemParam); // add2
        }
        trade.setTradeItems(tradeItemListTmp);
    }


    /**
     *
     * @param request
     * @return
     */
    public List<TradeItemGroup> getTradeItemList(TradePurchaseRequest request) {
        List<String> skuIds = request.getTradeItems().stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
        String customerId = request.getCustomer().getCustomerId();

        //查询是否购买付费会员卡
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
                .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
                        .customerId(customerId)
                        .delFlag(DeleteFlag.NO)
                        .endTimeFlag(LocalDateTime.now())
                        .build())
                .getContext();
        PaidCardVO paidCardVO = new PaidCardVO();
        if (CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)) {
            paidCardVO = paidCardCustomerRelVOList.stream()
                    .map(PaidCardCustomerRelVO::getPaidCardVO)
                    .min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
        }
        GoodsInfoResponse response = tradeGoodsService.getGoodsResponse(skuIds, request.getCustomer());
//        for (GoodsVO goods : response.getGoodses()) {
//            log.info("TradeService getTradeItemList goods:{} goods.getGoodsChannelTypeSet() :{} "
//                    , goods.getGoodsChannelType()
//                    , JSON.toJSONString(goods.getGoodsChannelTypeSet()));
//        }
        for (GoodsVO goodsVo : response.getGoodses()) {
            log.info("TradeService getTradeItemList goodsInfo:{} goodsInfo.getGoodsChannelTypeSet() :{} request.getGoodsChannelTypeSet():{}"
                    , goodsVo.getGoodsChannelType()
                    , JSON.toJSONString(goodsVo.getGoodsChannelTypeSet())
                    , JSON.toJSONString(request.getGoodsChannelTypeSet()));
            if (!goodsVo.getGoodsChannelTypeSet().contains(request.getGoodsChannelTypeSet().get(0).toString())) {
                throw new SbcRuntimeException("K-050216");
            }
        }


        Map<String, GoodsVO> goodsMap = response.getGoodses().stream().filter(goods -> goods.getCpsSpecial() != null).collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        Map<String, Integer> cpsSpecialMap = response.getGoodsInfos().stream().collect(Collectors.toMap(goodsInfo -> goodsInfo.getGoodsInfoId(), goodsInfo2 -> goodsMap.get(goodsInfo2.getGoodsId()).getCpsSpecial()));
        List<TradeItem> tradeItems = KsBeanUtil.convert(request.getTradeItems(), TradeItem.class);
        //获取付费会员价
        if (Objects.nonNull(paidCardVO.getDiscountRate())) {
            for (GoodsInfoVO goodsInfoVO : response.getGoodsInfos()) {
                goodsInfoVO.setSalePrice(goodsInfoVO.getMarketPrice().multiply(paidCardVO.getDiscountRate()));
            }
//            if (CollectionUtils.isNotEmpty(tradeItems)) {
//                for (TradeItem tradeItem : tradeItems) {
//                    if (Objects.nonNull(tradeItem.getPrice())) {
//                        tradeItem.setPrice(tradeItem.getPrice().multiply(paidCardVO.getDiscountRate()));
//                    }
//                }
//            }
        }
        verifyService.verifyGoods(tradeItems, Collections.emptyList(), KsBeanUtil.convert(response, TradeGoodsListVO.class), null, true, null);
        verifyService.verifyStore(response.getGoodsInfos().stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList()));
        Map<String, GoodsInfoVO> goodsInfoVOMap = response.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItems.stream().forEach(tradeItem -> {
            tradeItem.setCpsSpecial(cpsSpecialMap.get(tradeItem.getSkuId()));
            tradeItem.setGoodsType(GoodsType.fromValue(goodsInfoVOMap.get(tradeItem.getSkuId()).getGoodsType()));
            tradeItem.setVirtualCouponId(goodsInfoVOMap.get(tradeItem.getSkuId()).getVirtualCouponId());
            tradeItem.setBuyPoint(goodsInfoVOMap.get(tradeItem.getSkuId()).getBuyPoint());
            tradeItem.setStoreId(goodsInfoVOMap.get(tradeItem.getSkuId()).getStoreId());
            tradeItem.setMarketPrice(goodsInfoVOMap.get(tradeItem.getSkuId()).getMarketPrice());
        });

        tradeItems = tradeGoodsService.fillActivityPrice(tradeItems, response.getGoodsInfos(), customerId);
        for (TradeItem tradeItem : tradeItems) {
            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
            if (priceByGoodsId.getContext() != null) {
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
                tradeItem.setOriginalPrice(new BigDecimal(priceByGoodsId.getContext())); //设置原始价格
            }

        }



        // 校验商品限售信息
        TradeItemGroup tradeItemGroupVOS = new TradeItemGroup();
        tradeItemGroupVOS.setTradeItems(tradeItems);
        tradeGoodsService.validateRestrictedGoods(tradeItemGroupVOS, request.getCustomer());

        //订单商品渠道校验信息

        //商品按店铺分组
        Map<Long, List<TradeItem>> map = tradeItems.stream().collect(Collectors.groupingBy(TradeItem::getStoreId));
        List<TradeItemGroup> itemGroups = new ArrayList<>();
        map.forEach((key,value)->{
            StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(key)
                    .build())
                    .getContext().getStoreVO();
            DefaultFlag freightTemplateType = store.getFreightTemplateType();
            Supplier supplier = Supplier.builder()
                    .storeId(store.getStoreId())
                    .storeName(store.getStoreName())
                    .isSelf(store.getCompanyType() == BoolFlag.NO)
                    .supplierCode(store.getCompanyInfo().getCompanyCode())
                    .supplierId(store.getCompanyInfo().getCompanyInfoId())
                    .supplierName(store.getCompanyInfo().getSupplierName())
                    .freightTemplateType(freightTemplateType)
                    .build();
            for (TradeItem tradeItem : value) {
                tradeItem.setProviderName(store.getSupplierName());
            }
            TradeItemGroup tradeItemGroup = new TradeItemGroup();
            tradeItemGroup.setTradeItems(value);
            tradeItemGroup.setSupplier(supplier);
            tradeItemGroup.setTradeMarketingList(new ArrayList<>());
            itemGroups.add(tradeItemGroup);
        });
        return itemGroups;
    }


    /**
     * 调用校验与封装单个订单信息 - [后端代客下单]
     * 业务员app/商家-共用
     *
     * @return
     */
    public Trade wrapperBackendCommitTrade(Operator operator, CompanyInfoVO companyInfo, StoreInfoResponse
            storeInfoResponse, TradeCreateRequest tradeCreateRequest) {
        //验证外部订单
        validateOutTrade(tradeCreateRequest);

        //1.获取代客下单操作人信息
        Seller seller = Seller.fromOperator(operator);

        List<String> skuIds =
                tradeCreateRequest.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);

        //2.获取商家信息
        Supplier supplier = Supplier.builder()
                .isSelf(storeInfoResponse.getCompanyType() == BoolFlag.NO)
                .supplierCode(companyInfo.getCompanyCode())
                .supplierId(companyInfo.getCompanyInfoId())
                .employeeId(operator.getUserId())
                .supplierName(companyInfo.getSupplierName())
                .employeeName(operator.getName())
                .freightTemplateType(storeInfoResponse.getFreightTemplateType())
                .storeName(storeInfoResponse.getStoreName())
                .storeId(storeInfoResponse.getStoreId())
                .build();

        //3.获取并验证客户信息
//        Customer customer = verifyService.verifyCustomer(tradeCreateRequest.getCustom());
        CustomerSimplifyOrderCommitVO customer = verifyService.simplifyById(tradeCreateRequest.getCustom());
        if (storeInfoResponse.getCompanyType().equals(CompanyType.SUPPLIER)) {
            verifyService.verifyCustomerWithSupplier(customer.getCustomerId(), companyInfo.getCompanyInfoId());
        }

        CustomerLevelByCustomerIdAndStoreIdRequest request = CustomerLevelByCustomerIdAndStoreIdRequest.builder()
                .customerId(customer.getCustomerId())
                .storeId(storeInfoResponse.getStoreId()).build();
        CommonLevelVO storeLevel = customerLevelQueryProvider.getCustomerLevelByCustomerIdAndStoreId(request)
                .getContext();

        //4.【公共方法】下单信息验证, 将信息包装成订单
        return this.validateAndWrapperTrade(new Trade(),
                TradeParams.builder()
                        .backendFlag(true) //表示后端操作
                        .commitFlag(true) //表示下单
                        .marketingList(tradeCreateRequest.getTradeMarketingList())
                        .tradePrice(tradeCreateRequest.getTradePrice())
                        .tradeItems(tradeCreateRequest.getTradeItems())
                        .oldGifts(Collections.emptyList())//下单,非修改订单
                        .oldTradeItems(Collections.emptyList())//下单,非修改订单
                        .storeLevel(storeLevel)
                        .customer(customer)
                        .supplier(supplier)
                        .seller(seller)
                        .directChargeMobile(tradeCreateRequest.getDirectChargeMobile())
                        .consigneeId(tradeCreateRequest.getConsigneeId())
                        .detailAddress(tradeCreateRequest.getConsigneeAddress())
                        .consigneeUpdateTime(tradeCreateRequest.getConsigneeUpdateTime())
                        .consignee(tradeCreateRequest.getConsignee())
                        .invoice(tradeCreateRequest.getInvoice())
                        .invoiceConsignee(tradeCreateRequest.getInvoiceConsignee())
                        .deliverWay(tradeCreateRequest.getDeliverWay())
                        .payType(tradeCreateRequest.getPayType())
                        .buyerRemark(tradeCreateRequest.getBuyerRemark())
                        .sellerRemark(tradeCreateRequest.getSellerRemark())
                        .encloses(tradeCreateRequest.getEncloses())
                        .ip(operator.getIp())
                        .platform(operator.getPlatform())
                        .forceCommit(tradeCreateRequest.isForceCommit())
                        .distributeChannel(new DistributeChannel())
                        .goodsInfoViewByIdsResponse(goodsInfoViewByIdsResponse)
                        .build());
    }

    private void validateOutTrade(TradeCreateRequest tradeCreateRequest) {
        if (Objects.isNull(tradeCreateRequest) || Objects.isNull(tradeCreateRequest.getOutTradeNo())) {
            return;
        }
        List<Trade> trades = detailByOutTradeNo(tradeCreateRequest.getOutTradeNo());
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        for (Trade item : trades) {
            if (!FlowState.VOID.getStateId().equals(item.getTradeState().getFlowState())) {
                throw new SbcRuntimeException(CommonErrorCode.REPEAT_REQUEST, "订单已经存在");
            }
        }
    }

    /**
     * 调用校验与封装单个订单信息 - [后端修改订单]
     * 业务员app/商家-共用
     *
     * @return
     */
    public Trade wrapperBackendRemedyTrade(Trade trade, Operator operator, TradeRemedyRequest tradeRemedyRequest) {
        tradeRemedyRequest.getInvoice().setOrderInvoiceId(
                Objects.nonNull(trade.getInvoice()) ?
                        trade.getInvoice().getOrderInvoiceId() : null);
        List<String> skuIds =
                tradeRemedyRequest.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = tradeCacheService.getGoodsInfoViewByIds(skuIds);
        //【公共方法】修改订单信息验证, 将修改的信息包装成新订单
        return this.validateAndWrapperTrade(trade,
                TradeParams.builder()
                        .backendFlag(true) //表示后端操作
                        .commitFlag(false) //表示修改订单
                        .marketingList(tradeRemedyRequest.getTradeMarketingList())
                        .tradePrice(tradeRemedyRequest.getTradePrice())
                        .tradeItems(tradeRemedyRequest.getTradeItems())
                        .oldGifts(trade.getGifts()) //修改订单,设置旧赠品
                        .oldTradeItems(trade.getTradeItems()) //修改订单,设置旧商品
                        .storeLevel(null) //修改订单,客户,商家,代理人都无法修改,所以设置为null
                        .customer(null)
                        .supplier(null)
                        .seller(null)
                        .consigneeId(tradeRemedyRequest.getConsigneeId())
                        .detailAddress(tradeRemedyRequest.getConsigneeAddress())
                        .consigneeUpdateTime(tradeRemedyRequest.getConsigneeUpdateTime())
                        .consignee(tradeRemedyRequest.getConsignee())
                        .invoice(tradeRemedyRequest.getInvoice())
                        .invoiceConsignee(tradeRemedyRequest.getInvoiceConsignee())
                        .deliverWay(tradeRemedyRequest.getDeliverWay())
                        .payType(tradeRemedyRequest.getPayType())
                        .buyerRemark(tradeRemedyRequest.getBuyerRemark())
                        .sellerRemark(tradeRemedyRequest.getSellerRemark())
                        .encloses(tradeRemedyRequest.getEncloses())
                        .ip(operator.getIp())
                        .forceCommit(tradeRemedyRequest.isForceCommit())
                        .goodsInfoViewByIdsResponse(goodsInfoViewByIdsResponse)
                        .build());
    }

    /**
     * 验证下单信息并封装订单信息
     * 【公共方法】-客户下单(PC/H5/APP...), 商家代客下单/修改订单(supplier/employeeApp/supplierAPP...)
     * 1.验证tradeParams中的用户下单信息
     * 2.封装trade,方便后面持久化
     *
     * @param tradeParams 用户下单信息
     * @return 待入库的订单对象
     */
    public Trade validateAndWrapperTrade(Trade trade, TradeParams tradeParams) {
        trade.setEmallSessionId(tradeParams.getEmallSessionId());
        //判断是否为秒杀抢购商品订单
        if (Objects.nonNull(tradeParams.getIsFlashSaleGoods()) && tradeParams.getIsFlashSaleGoods()) {
            trade.setIsFlashSaleGoods(tradeParams.getIsFlashSaleGoods());
        }
        if (Objects.nonNull(tradeParams.getIsBookingSaleGoods()) && tradeParams.getIsBookingSaleGoods() &&
                Objects.nonNull(tradeParams.getTradeItems().get(0).getBookingType()) && Objects.isNull(trade.getBookingType())) {
            trade.setBookingType(tradeParams.getTradeItems().get(0).getBookingType());
        }
        //判断是否为预售订单
        if (Objects.nonNull(tradeParams.getIsBookingSaleGoods()) && tradeParams.getIsBookingSaleGoods()) {
            trade.setIsBookingSaleGoods(tradeParams.getIsBookingSaleGoods());
            if (Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY && StringUtils.isEmpty(trade.getTailOrderNo())) {
                trade.setTailNoticeMobile(tradeParams.getTailNoticeMobile());
            }
        }
        //设定订单类型
        if (Objects.nonNull(tradeParams.getThirdPlatformType())) {
            trade.setThirdPlatformType(tradeParams.getThirdPlatformType());
        }

        // 2.1.设置订单基本信息(购买人,商家,代客下单操作人,收货地址,发票信息,配送方式,支付方式,备注,附件,操作人ip,订单商品,订单总价...)
        if (tradeParams.isCommitFlag()) {
            // 购买人,商家,代客下单操作人,订单项Oid,订单id,订单来源方等只有在下单的时候才设置(因为在修改订单时无法修改这些信息)
            Optional<CommonLevelVO> commonLevelVO;
            boolean flag = true;
            if (tradeParams.getStoreLevel() == null) {
                flag = false;
                commonLevelVO =
                        Optional.of(fromCustomerLevel(customerLevelQueryProvider.getDefaultCustomerLevel().getContext()));
            } else {
                commonLevelVO = Optional.of(tradeParams.getStoreLevel());
            }
            trade.setBuyer(Buyer.fromCustomer(tradeParams.getCustomer(), commonLevelVO, flag));
            trade.setSupplier(tradeParams.getSupplier());
            trade.setSeller(tradeParams.getSeller());
            trade.setDirectChargeMobile(tradeParams.getDirectChargeMobile());
            tradeParams.getTradeItems().forEach(t -> {
                t.setOid(generatorService.generateOid());
                if (StringUtils.isBlank(t.getAdminId())) {
                    t.setAdminId(String.format("%d", tradeParams.getSupplier().getSupplierId()));
                }
            });
            //赠品对象增加oid
            if(!CollectionUtils.isEmpty(trade.getGifts())){
                trade.getGifts().forEach(gift->{
                    gift.setOid(generatorService.generateOid());
                });
            }
            trade.setId(generatorService.generateTid());
            trade.setPlatform(tradeParams.getPlatform());
            trade.setOrderSource(tradeParams.getOrderSource());
            trade.setOrderType(OrderType.NORMAL_ORDER);
            trade.setShareUserId(tradeParams.getShareUserId());
            trade.setCycleBuyFlag(Objects.nonNull(tradeParams.getCycleBuyInfo()) ? Boolean.TRUE : Boolean.FALSE);
        }
        if (Objects.isNull(tradeParams.getIsBookingSaleGoodsTail()) || !tradeParams.getIsBookingSaleGoodsTail()) {
            trade.setConsignee(wrapperConsignee(tradeParams.getConsigneeId(), tradeParams.getDetailAddress(),
                    tradeParams.getConsigneeUpdateTime(), tradeParams.getConsignee()));

            //发票信息(必须在收货地址下面-因为使用临时发票收货地,却未填写的时候,将使用订单商品收货地址作为发票收货地)
            trade.setInvoice(wrapperTradeInvoice(tradeParams.getInvoice(), tradeParams.getInvoiceConsignee(),
                    trade.getConsignee()));
            trade.setDeliverWay(tradeParams.getDeliverWay());
            if (tradeParams.getPayType() != null) {
                trade.setPayInfo(PayInfo.builder()
                        .payTypeId(String.format("%d", tradeParams.getPayType().toValue()))
                        .payTypeName(tradeParams.getPayType().name())
                        .desc(tradeParams.getPayType().getDesc())
                        .build());
            }
            trade.setBuyerRemark(tradeParams.getBuyerRemark());
            trade.setSellerRemark(tradeParams.getSellerRemark());
            trade.setEncloses(tradeParams.getEncloses());
            trade.setRequestIp(tradeParams.getIp());
            trade.setTradeItems(tradeParams.getTradeItems());
            trade.setTradePrice(tradeParams.getTradePrice());
        }

        // 2.2.订单中商品信息填充(同时设置商品的客户级别价格/客户指定价salePrice)
        // 计算价格前换购商品排除
        List<TradeItem> markupTradeItems =
                trade.getTradeItems().stream()
                        .filter(tradeItem -> Objects.nonNull(tradeItem.getIsMarkupGoods()) && tradeItem.getIsMarkupGoods()).collect(Collectors.toList());
        trade.getTradeItems().removeAll(markupTradeItems);
        TradeGoodsListVO skuList;
        if (Objects.nonNull(tradeParams) && Objects.isNull(tradeParams.getCustomer())) {
            skuList = tradeGoodsService.getGoodsInfoResponse(trade);
        } else {
            skuList = tradeGoodsService.getGoodsInfoResponse(trade, tradeParams);
        }

        // 2.3.若是后端下单/修改,校验商家跟商品的关系(因为前端下单信息都是从库里读取的,无需验证)
        if (tradeParams.isBackendFlag()) {
            boolean existInvalidGoods = skuList.getGoodsInfos().parallelStream().anyMatch(goodsInfo -> !trade
                    .getSupplier().getSupplierId()
                    .equals(goodsInfo.getCompanyInfoId()));
            if (existInvalidGoods) {
                throw new SbcRuntimeException("K-030006");
            }
        }

        // 分销商品、开店礼包商品、拼团商品、企业购商品不验证起限定量
        boolean isIepCustomer = trade.getBuyer().isIepCustomer();
        skuList.getGoodsInfos().forEach(item -> {
            boolean isIepGoodsInfo = isIepCustomer && isEnjoyIepGoodsInfo(item.getEnterPriseAuditState());
            if (DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
                    || DefaultFlag.YES.equals(trade.getStoreBagsFlag())
                    || Objects.nonNull(tradeParams.getGrouponForm())
                    || isIepGoodsInfo) {
                item.setCount(null);
                item.setMaxCount(null);
            }
        });
        if (Objects.isNull(tradeParams.getIsBookingSaleGoodsTail()) || !tradeParams.getIsBookingSaleGoodsTail()) {
            // 2.4.校验sku 和 【商品价格计算第①步】: 商品的 客户级别价格 (完成客户级别价格/客户指定价/订货区间价计算) -> levelPrice
            verifyService.verifyGoods(trade.getTradeItems(), tradeParams.getOldTradeItems(), skuList,
                    trade.getSupplier()
                            .getStoreId(), true, null);
        }

        //填充linkedMall类型
        if (Objects.isNull(trade.getThirdPlatformType())) {
            if (trade.getTradeItems().stream().anyMatch(i -> ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType()))) {
                //验证linkedMall是否开启
                if ((!linkedMallTradeService.isOpen())) {
                    throw new SbcRuntimeException("K-050117");
                }
                trade.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
            }
        }
        Map<String, BigDecimal> enterprisePriceMap = new HashMap<>();
        if (isIepCustomer) {
            List<String> skuIds = new ArrayList<>();
            Map<String, Long> buyCountMap = new HashMap<>();
            trade.getTradeItems().forEach(tradeItem -> {
                skuIds.add(tradeItem.getSkuId());
                buyCountMap.put(tradeItem.getSkuId(), tradeItem.getNum());
            });

            //企业价
            EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
            enterprisePriceGetRequest.setGoodsInfoIds(skuIds);
            enterprisePriceGetRequest.setCustomerId(trade.getBuyer().getId());
            enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
            enterprisePriceGetRequest.setListFlag(false);
            enterprisePriceGetRequest.setOrderFlag(true);
            EnterprisePriceResponse enterprisePriceResponse = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();
            Map<String, BigDecimal> priceMap = enterprisePriceResponse.getPriceMap();
            if (priceMap != null) {
                enterprisePriceMap.putAll(priceMap);
            }

        }

        // 企业购商品价格回设,积分价商品优先级高
        Boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();
        if (Objects.isNull(trade.getIsFlashSaleGoods()) || !trade.getIsFlashSaleGoods()) {
            trade.getTradeItems().forEach(i -> {
                if (isIepCustomer && isEnjoyIepGoodsInfo(i.getEnterPriseAuditState())
                        && (!isGoodsPoint || (isGoodsPoint && (i.getBuyPoint() == null || i.getBuyPoint() == 0)))) {

                    BigDecimal price = i.getEarnestPrice();
                    BigDecimal bigDecimal = enterprisePriceMap.get(i.getSkuId());
                    if (bigDecimal != null) {
                        price = bigDecimal;
                    }
                    i.setEnterPrisePrice(price);
                    i.setSplitPrice(price.multiply(new BigDecimal(i.getNum())));
                    if (Objects.isNull(i.getIsAppointmentSaleGoods()) || !i.getIsAppointmentSaleGoods()) {
                        i.setPrice(price);
                    }
                    i.setLevelPrice(price);
                }
            });
        }

        // 2.5.处理分销
        dealDistribution(trade, tradeParams);

        // 2.6.商品营销信息冗余,验证,计算,设置各营销优惠,实付金额
        tradeParams.getMarketingList().forEach(i -> {
            List<TradeItem> items = trade.getTradeItems().stream().filter(s -> i.getSkuIds().contains(s.getSkuId()))
                    .collect(Collectors.toList());
            items.forEach(s -> s.getMarketingIds().add(i.getMarketingId()));
        });

        // 拼团订单--处理
        if (Objects.nonNull(tradeParams.getGrouponForm())) {
            dealGroupon(trade, tradeParams);
        }
        // 校验组合购活动信息
        dealSuitOrder(trade, tradeParams);

        //构建订单满系营销对象 优惠券
        this.wrapperMarketingForCommit(trade, tradeParams);

        long count = tradeParams.getTradeItems().stream().filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                && (GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType())
                || GoodsType.VIRTUAL_GOODS.equals( tradeItem.getGoodsType()))).count();
        if (tradeParams.getTradeItems().size() == count) {
            trade.setIsVirtualCouponGoods(Boolean.TRUE);
        }

        // 2.7.赠品信息校验与填充
        // 赠品
        List<String> giftIds = tradeParams.getMarketingList().stream()
                .filter(m -> CollectionUtils.isNotEmpty(m.getGiftSkuIds())).flatMap(
                        r -> r.getGiftSkuIds().stream()).distinct().collect(Collectors.toList());
        //周期购赠品
        if (Objects.nonNull(tradeParams.getCycleBuyInfo()) && CollectionUtils.isNotEmpty(tradeParams.getCycleBuyInfo().getCycleBuyGifts())) {
            giftIds.addAll(tradeParams.getCycleBuyInfo().getCycleBuyGifts());
        }
        TradeGetGoodsResponse giftResp = this.getGoodsResponse(giftIds);
        List<TradeItem> gifts = giftIds.stream().map(g -> TradeItem.builder().price(BigDecimal.ZERO).skuId(g)
                .build()).collect(Collectors.toList());
        verifyService.mergeGoodsInfo(gifts, giftResp);
        trade.setIsVirtualCouponGiveawayGoods(Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(gifts)) {
            count = gifts.stream().filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                    && ( GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType())
                    || GoodsType.VIRTUAL_GOODS.equals(tradeItem.getGoodsType()))).count();
            if (gifts.size() != count) {
                trade.setIsVirtualCouponGiveawayGoods(Boolean.FALSE);
            }
        }
        trade.setGifts(gifts);
        if (!trade.getCycleBuyFlag()) {
            giftSet(trade);
        } else {
            dealCycleBuyGifts(trade, tradeParams);
        }

        // 加价购商品价格添加
        addMarkupPrice(trade, markupTradeItems);
        //2.8.计算满系营销、优惠券均摊价，并设置结算信息
        calcMarketingPrice(trade);

        // 2.9.计算并设置订单总价(已减去营销优惠总金额)
        trade.setTradePrice(calc(trade));

        // 校验加价购是否满足条件
        verifyService.verifyMarkup(trade);
        // 推广人用户id
        trade.setPromoteUserId(tradeParams.getPromoteUserId());
        trade.setSource(tradeParams.getSource());
        trade.setMiniProgramScene(tradeParams.getMiniProgramScene());
        log.info("==================周期购订单1：{}===============",trade);


        //周期购信息填充
        dealCycleBuy(trade,tradeParams);

        // 2.10.计算运费
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal deliveryPrice = tradePrice.getDeliveryPrice();
        Map<Long, BigDecimal> splitDeliveryPrice = new HashMap<>();
        if (tradePrice.getDeliveryPrice() == null) {
            deliveryPrice = BigDecimal.ZERO;
            // 只看主商品是否全是虚拟
            boolean virtualCouponGoods = Objects.isNull(trade.getIsVirtualCouponGoods()) || Boolean.FALSE.equals(trade.getIsVirtualCouponGoods());
            if (virtualCouponGoods) {
                List<TradeItem> tradeItems = trade.getTradeItems();
                Map<Long, List<TradeItem>> splitTradeItems = tradeItems.stream().collect(Collectors.groupingBy(TradeItem::getProviderId));
                Set<Long> providerIds = splitTradeItems.keySet();
                for (Long providerId : providerIds) {
                    BigDecimal deliveryFee = calcTradeFreight(trade.getConsignee(), trade.getSupplier()
                            , trade.getDeliverWay(), tradePrice.getTotalPrice(), splitTradeItems.get(providerId), trade.getGifts());
                    splitDeliveryPrice.put(providerId, deliveryFee);
                    deliveryPrice = deliveryPrice.add(deliveryFee);
                }
            }
        }
        //判断是否为秒杀抢购订单
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()) {
            //秒杀商品是否包邮
            //获取秒杀抢购活动详情
            FlashSaleGoodsVO flashSaleGoodsVO = flashSaleGoodsQueryProvider.getById(FlashSaleGoodsByIdRequest.builder()
                    .id(trade.getTradeItems().get(0).getFlashSaleGoodsId())
                    .build())
                    .getContext().getFlashSaleGoodsVO();
            if (flashSaleGoodsVO.getPostage().equals(1)) {
                deliveryPrice = BigDecimal.ZERO;
            }
        }
        tradePrice.setSplitDeliveryPrice(splitDeliveryPrice);
        tradePrice.setDeliveryPrice(deliveryPrice);
        // 2.11.计算订单总价(追加运费)
        tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(deliveryPrice));

        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() &&
                Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY
                && Objects.isNull(tradePrice.getEarnestPrice())) {
            tradePrice.setEarnestPrice(trade.getTradeItems().get(0).getEarnestPrice());
            tradePrice.setSwellPrice(trade.getTradeItems().get(0).getSwellPrice());
            tradePrice.setTailPrice(trade.getTradeItems().get(0).getTailPrice());
        }

        if (tradePrice.isSpecial()) {
            // 2.12.【商品价格计算第③步】: 商品的 特价订单 均摊价 -> splitPrice
            tradeItemService.clacSplitPrice(trade.getTradeItems(), tradePrice.getPrivilegePrice());
            tradePrice.setTotalPrice(tradePrice.getPrivilegePrice().add(deliveryPrice));//应付金额 = 特价+运费
        } else {
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(deliveryPrice));//应付金额 = 应付+运费
        }
//
//        //判断打包商品
//        //获取商品的打包信息，
//        List<String> mainGoodsIdList = trade.getTradeItems().stream().map(TradeItem::getSpuId).collect(Collectors.toList());
//        BaseResponse<List<GoodsPackDetailResponse>> packResponse = goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(mainGoodsIdList));
//        List<GoodsPackDetailResponse> goodsPackDetailList = packResponse.getContext();
//
//        Map<String, List<GoodsPackDetailResponse>> packId2GoodsPackDetailMap = new HashMap<>();
//        if (!CollectionUtils.isEmpty(goodsPackDetailList)) {
//            for (GoodsPackDetailResponse goodsPackDetailParam : goodsPackDetailList) {
//                List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.computeIfAbsent(goodsPackDetailParam.getPackId(), k -> new ArrayList<>());
//                goodsPackDetailListTmp.add(goodsPackDetailParam);
//            }
//        }
//
//        tradeParams.getTradeItems().forEach(t -> {
//            t.setOid(generatorService.generateOid());
//            if (StringUtils.isBlank(t.getAdminId())) {
//                t.setAdminId(String.format("%d", tradeParams.getSupplier().getSupplierId()));
//            }
//        });
//
//        List<TradeItem> tradeItemListTmp = new ArrayList<>();
//        for (TradeItem tradeItemParam : trade.getTradeItems()) {
//            List<GoodsPackDetailResponse> goodsPackDetailListTmp = packId2GoodsPackDetailMap.get(tradeItemParam.getSpuId());
//            if (CollectionUtils.isNotEmpty(goodsPackDetailListTmp)) {
//                //拆分
//                BigDecimal splitPrice = tradeItemParam.getSplitPrice() == null ? BigDecimal.ZERO : tradeItemParam.getSplitPrice();
//                Long points = tradeItemParam.getPoints() == null ? 0L : tradeItemParam.getPoints();
//                Long knowledge = tradeItemParam.getKnowledge() == null ? 0L : tradeItemParam.getKnowledge();
//                BigDecimal rateAll = new BigDecimal("100");
//
//                //优先计算 子商品的价格，最后计算主要商品的价格；
//                BigDecimal surplusSplitPrice = splitPrice;
//                BigDecimal surplusPoint = new BigDecimal(splitPrice + "");
//                BigDecimal surplusKnowledge = new BigDecimal(knowledge + "");
////                GoodsPackDetailResponse mainGoodsPackDetail;
//
//                for (GoodsPackDetailResponse goodsPackDetailTmp : goodsPackDetailListTmp) {
//                    if (!Objects.equals(goodsPackDetailTmp.getGoodsId(), goodsPackDetailTmp.getPackId())) {
//                        BigDecimal splitPriceTmp = splitPrice.multiply(goodsPackDetailTmp.getShareRate().divide(rateAll,2, RoundingMode.HALF_UP));
//                        BigDecimal pointsTmp = new BigDecimal(points + "").multiply(goodsPackDetailTmp.getShareRate().divide(rateAll,2, RoundingMode.HALF_UP));
//                        BigDecimal knowledgeTmp = new BigDecimal(knowledge + "").multiply(goodsPackDetailTmp.getShareRate().divide(rateAll,2, RoundingMode.HALF_UP));
//
//                        surplusSplitPrice = surplusSplitPrice.subtract(splitPriceTmp);
//                        surplusPoint = surplusPoint.subtract(pointsTmp);
//                        surplusKnowledge = surplusKnowledge.subtract(knowledgeTmp);
//                    } /*else {
//                        mainGoodsPackDetail = goodsPackDetailTmp;
//                    }*/
//                }
//
//                PackRecord packRecord = tradeItemParam.getPackRecord();
//                if (packRecord == null) {
//                    packRecord = new PackRecord();
//                }
//                packRecord.setPackId(tradeItemParam.getSpuId());
//                packRecord.setPackPoint(surplusPoint.longValue());
//                packRecord.setPackKnowLedge(surplusKnowledge.longValue());
//                packRecord.setPackSplitPrice(surplusSplitPrice);
//                tradeItemParam.setPackRecord(packRecord);
//            } else {
//                tradeItemListTmp.add(tradeItemParam);
//            }
//        }
        return trade;
    }


    /**
     * 添加加价购商品的金额
     *
     * @param trade
     * @param markupTradeItems
     */
    private void addMarkupPrice(Trade trade, List<TradeItem> markupTradeItems) {
        if (CollectionUtils.isEmpty(markupTradeItems)) {
            return;
        }
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal markupPrice = markupTradeItems.stream().map(t -> {
            t.setSplitPrice(t.getPrice());
            t.setOriginalPrice(t.getPrice());
            t.setLevelPrice(t.getPrice());
            return t.getPrice().multiply(BigDecimal.valueOf(t.getNum()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
        tradePrice.setMarkupPrice(markupPrice);
        trade.getTradeItems().addAll(markupTradeItems);
    }

    /**
     * 处理组合购订单
     *
     * @param trade
     * @param tradeParams
     */
    private void dealSuitOrder(Trade trade, TradeParams tradeParams) {
        trade.setSuitMarketingFlag(tradeParams.getSuitMarketingFlag());
        trade.setSuitScene(tradeParams.getSuitScene());
        // 组合购标记
        if (Objects.equals(trade.getSuitMarketingFlag(), Boolean.TRUE) && CollectionUtils.isNotEmpty(tradeParams.getMarketingList())) {
            // 获取并校验组合购活动信息
            MarketingSuitsValidRequest marketingSuitsValidRequest = new MarketingSuitsValidRequest();
            marketingSuitsValidRequest.setMarketingId(tradeParams.getMarketingList().get(NumberUtils.INTEGER_ZERO).getMarketingId());
            marketingSuitsValidRequest.setUserId(trade.getBuyer().getId());
            BaseResponse<MarketingSuitsValidResponse> marketingSuits =
                    marketingSuitsQueryProvider.validSuitOrderBeforeCommit(marketingSuitsValidRequest);
            List<MarketingSuitsSkuVO> marketingSuitsSkuVOList =
                    marketingSuits.getContext().getMarketingSuitsSkuVOList();
            trade.getTradeItems().forEach(item -> {
                MarketingSuitsSkuVO suitsSku =
                        marketingSuitsSkuVOList.stream().filter(sku -> Objects.equals(sku.getSkuId(), item.getSkuId())
                        ).findFirst().orElse(new MarketingSuitsSkuVO());
                //设置组合购商品价格
                BigDecimal discountPrice = suitsSku.getDiscountPrice();
                BigDecimal splitPrice = discountPrice.multiply(new BigDecimal(suitsSku.getNum()));
                item.setSplitPrice(splitPrice);
                item.setLevelPrice(discountPrice);
                item.setPrice(discountPrice);
                item.setBuyPoint(NumberUtils.LONG_ZERO);
            });


        }
    }

    /**
     * 处理分销订单
     *
     * @param trade
     * @param tradeParams
     */
    private void dealDistribution(Trade trade, TradeParams tradeParams) {
        if ((Objects.isNull(trade.getIsFlashSaleGoods()) || (Objects.nonNull(trade.getIsFlashSaleGoods()) && !trade.getIsFlashSaleGoods())) && DefaultFlag.YES.equals(tradeParams.getOpenFlag())) {
            if (DefaultFlag.YES.equals(tradeParams.getStoreBagsFlag())) {
                // 开店礼包商品，使用市场价，且不计算营销
                trade.getTradeItems().forEach(item -> {
                    if (Objects.isNull(item.getIsAppointmentSaleGoods()) || !item.getIsAppointmentSaleGoods()
                            || !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods() && item.getBookingType() == BookingType.FULL_MONEY)) {
                        item.setPrice(item.getOriginalPrice());
                    }
                    item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                    item.setLevelPrice(item.getOriginalPrice());
                    item.setBuyPoint(NumberUtils.LONG_ZERO);
                });
                trade.setStoreBagsFlag(DefaultFlag.YES);
                tradeParams.setMarketingList(new ArrayList<>());
                trade.setTradeMarketings(new ArrayList<>());
                trade.setTradeCoupon(null);
                trade.setStoreBagsInviteeId(tradeParams.getDistributeChannel().getInviteeId());
            } else {
                // 非开店礼包，且为分销商品
                // 1.将分销商品设回市场价
                DistributeChannel channel = tradeParams.getDistributeChannel();
                Boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();
                trade.getTradeItems().forEach(item -> {
                    //不是商品抵扣模式 || 非积分价商品
                    Boolean isPointPriceGoods =
                            !isGoodsPoint || (isGoodsPoint && (item.getBuyPoint() == null || item.getBuyPoint() == 0));

                    // 非预售、预约商品
                    Boolean isAppointOrBookingGoods =
                            !(Objects.nonNull(item.getIsAppointmentSaleGoods()) && item.getIsAppointmentSaleGoods())
                                    && !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods());
                    if (DistributionGoodsAudit.CHECKED == item.getDistributionGoodsAudit()
                            && DefaultFlag.YES.equals(tradeParams.getStoreOpenFlag())
                            && ChannelType.PC_MALL != channel.getChannelType() && isPointPriceGoods && isAppointOrBookingGoods) {
                        item.setPrice(item.getOriginalPrice());
                        item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
                        item.setLevelPrice(item.getOriginalPrice());
                        // 初步计算分销佣金
                        item.setDistributionCommission(item.getSplitPrice().multiply(item.getCommissionRate()));
                    } else {
                        item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    }
                });

                List<TradeItem> distributionTradeItems = trade.getTradeItems().stream()
                        .filter(item -> DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())).collect(Collectors.toList());

                // 2.设置分销相关字段
                if (distributionTradeItems.size() != 0) {

                    MultistageSettingGetResponse multistageSetting =
                            distributionCacheQueryProvider.getMultistageSetting().getContext();

                    // 2.1.查询佣金受益人列表
                    DistributionCustomerListForOrderCommitRequest request =
                            new DistributionCustomerListForOrderCommitRequest();
                    request.setBuyerId(trade.getBuyer().getId());
                    request.setCommissionPriorityType(
                            CommissionPriorityType.fromValue(multistageSetting.getCommissionPriorityType().toValue())
                    );
                    request.setCommissionUnhookType(
                            CommissionUnhookType.fromValue(multistageSetting.getCommissionUnhookType().toValue())
                    );
                    request.setDistributorLevels(multistageSetting.getDistributorLevels());
                    request.setInviteeId(channel.getInviteeId());
                    request.setIsDistributor(tradeParams.getIsDistributor());
                    List<DistributionCustomerSimVO> inviteeCustomers = distributionCustomerQueryProvider
                            .listDistributorsForOrderCommit(request).getContext().getDistributorList();

                    List<TradeDistributeItem> distributeItems = new ArrayList<>();

                    // 商品分销佣金map(记录每个分销商品基础分销佣金)
                    Map<String, BigDecimal> skuBaseCommissionMap = new HashMap<>();
                    distributionTradeItems.forEach(item ->
                            skuBaseCommissionMap.put(item.getSkuId(), item.getDistributionCommission())
                    );

                    // 2.2.根据受益人列表设置分销相关字段
                    BigDecimal totalCommission = BigDecimal.ZERO;
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
                                    TradeDistributeItem distributeItem = new TradeDistributeItem();
                                    distributeItem.setGoodsInfoId(item.getSkuId());
                                    distributeItem.setNum(item.getNum());
                                    distributeItem.setActualPaidPrice(item.getSplitPrice());
                                    distributeItem.setCommissionRate(item.getCommissionRate());
                                    distributeItem.setCommission(item.getDistributionCommission());
                                    distributeItems.add(distributeItem);
                                });

                                // 设置trade.[inviteeId,distributorId,distributorName,commission]
                                trade.setInviteeId(customer.getCustomerId());
                                trade.setDistributorId(customer.getDistributionId());
                                trade.setDistributorName(customer.getCustomerName());
                                trade.setCommission(
                                        distributeItems.stream().map(TradeDistributeItem::getCommission)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                );
                                // 累加返利人佣金至总佣金
                                totalCommission = totalCommission.add(trade.getCommission());

                            } else {
                                // 2.2.2.设置提成人信息
                                BigDecimal percentageTotal = BigDecimal.ZERO;
                                for (int i = 0; i < distributeItems.size(); i++) {
                                    // 设置trade.distributeItems.commissions
                                    TradeDistributeItem item = distributeItems.get(i);
                                    TradeDistributeItemCommission itemCommission = new TradeDistributeItemCommission();
                                    itemCommission.setCustomerId(customer.getCustomerId());
                                    itemCommission.setDistributorId(customer.getDistributionId());
                                    itemCommission.setCommission(
                                            skuBaseCommissionMap.get(item.getGoodsInfoId()).multiply(
                                                    level.getPercentageRate()).setScale(2, BigDecimal.ROUND_DOWN));
                                    item.getCommissions().add(itemCommission);
                                    percentageTotal = percentageTotal.add(itemCommission.getCommission());
                                }

                                // 设置trade.commissions
                                TradeCommission tradeCommission = new TradeCommission();
                                tradeCommission.setCustomerId(customer.getCustomerId());
                                tradeCommission.setCommission(percentageTotal);
                                tradeCommission.setDistributorId(customer.getDistributionId());
                                tradeCommission.setCustomerName(customer.getCustomerName());
                                trade.getCommissions().add(tradeCommission);

                                // 累加提成人佣金至总佣金
                                totalCommission = totalCommission.add(tradeCommission.getCommission());
                            }

                        }

                        // 求和分销商品总佣金 trade.distributeItems.totalCommission
                        distributeItems.forEach(item -> {
                            // 追加返利人佣金
                            item.setTotalCommission(item.getCommission());
                            // 追加提成人佣金
                            item.getCommissions().forEach(i ->
                                    item.setTotalCommission(item.getTotalCommission().add(i.getCommission()))
                            );
                        });

                        // 设置总佣金、分销商品
                        trade.setTotalCommission(totalCommission);
                        trade.setDistributeItems(distributeItems);
                    }
                }
            }
        }
        //设置渠道信息、小店名称、小B-会员ID
        trade.setChannelType(tradeParams.getDistributeChannel().getChannelType());
        trade.setShopName(tradeParams.getShopName());
        trade.setDistributionShareCustomerId(tradeParams.getDistributeChannel().getInviteeId());
    }


    private CommonLevelVO fromCustomerLevel(CustomerLevelVO customerLevelVO) {
        if (customerLevelVO == null) {
            return null;
        }
        CommonLevelVO result = new CommonLevelVO();
        result.setLevelId(customerLevelVO.getCustomerLevelId());
        result.setLevelName(customerLevelVO.getCustomerLevelName());
        result.setLevelDiscount(customerLevelVO.getCustomerLevelDiscount());

        return result;
    }

    /**
     * 查询店铺订单应付的运费(需要参数具体如下)
     * consignee 收货地址 - 省id,市id
     * supplier 店铺信息 - 店铺id-使用运费模板类型
     * deliverWay 配送方式
     * totalPrice 订单总价(扣除营销优惠后)
     * oldTradeItems 订单商品List - 均摊价(计算营销后),件数   ,体积,重量,使用的运费模板id
     * oldGifts 订单赠品List - 价格为0,件数   ,体积,重量,使用的运费模板id
     *
     * @param tradeParams
     * @return
     */
    public TradeFreightResponse getFreight(TradeParams tradeParams) {
        // 纯卡券商品没有运费 只看主商品
        long virtualCouponSize = tradeParams.getOldTradeItems().stream()
                .filter(tradeItem -> Objects.nonNull(tradeItem)
                        && ( GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType())
                        || GoodsType.VIRTUAL_GOODS.equals(tradeItem.getGoodsType()))).count();
        BigDecimal deliveryPrice = BigDecimal.ZERO;
        if (tradeParams.getOldTradeItems().size() != virtualCouponSize ) {
            //  计算实物商品运费
            deliveryPrice = this.calcTradeFreight(tradeParams.getConsignee(), tradeParams.getSupplier(),
                    tradeParams.getDeliverWay(),
                    tradeParams.getTradePrice().getTotalPrice(), tradeParams.getOldTradeItems(), tradeParams.getOldGifts());
        }
        TradeFreightResponse freightResponse = new TradeFreightResponse();
        freightResponse.setStoreId(tradeParams.getSupplier().getStoreId());
        freightResponse.setDeliveryPrice(deliveryPrice);
        return freightResponse;
    }

    /**
     * 设置订单运费,并追加到订单原价/应付金额中
     * 若商家没有单独填写订单运费,则根据订单商品,赠品按照运费模板进行计算
     *
     * @param consignee  收货地址 - 省id,市id
     * @param supplier   店铺信息 - 店铺id-使用运费模板类型
     * @param deliverWay 配送方式
     * @param totalPrice 订单总价(扣除营销优惠后)
     * @param goodsList  订单商品List - 均摊价(计算营销后),件数   ,体积,重量,使用的运费模板id
     * @param giftList   订单赠品List - 价格为0,件数   ,体积,重量,使用的运费模板id
     * @return freight 订单应付运费
     */
    public BigDecimal calcTradeFreight(Consignee consignee, Supplier supplier, DeliverWay deliverWay, BigDecimal
            totalPrice, List<TradeItem> goodsList, List<TradeItem> giftList) {
        if(!tradeCacheService.queryIfnotSupportArea(consignee.getProvinceId(), consignee.getCityId()))
            throw new NotSupportDeliveryException("not support area,province:" + consignee.getProvinceId() + ", or city: " + consignee.getCityId());
        BigDecimal freight = BigDecimal.ZERO;
        //周期购商品
        Boolean cycleBuyFlag = goodsList.stream().filter(item -> GoodsType.CYCLE_BUY.equals(item.getGoodsType())).count() > 0 ? Boolean.TRUE : Boolean.FALSE;
        if (DefaultFlag.NO.equals(supplier.getFreightTemplateType())) {
            //1. 店铺运费模板计算
            FreightTemplateStoreVO templateStore;
            List<FreightTemplateStoreVO> storeTemplateList =
                    tradeCacheService.listStoreTemplateByStoreIdAndDeleteFlag(supplier.getStoreId(), DeleteFlag.NO);
            //1.1. 配送地匹配运费模板(若匹配不上则使用默认运费模板)
            Optional<FreightTemplateStoreVO> tempOptional = storeTemplateList.stream().filter(temp -> matchArea(
                    temp.getDestinationArea(), consignee.getProvinceId(), consignee.getCityId())).findFirst();
            if (tempOptional.isPresent()) {
                templateStore = tempOptional.get();
            } else {
                templateStore = storeTemplateList.stream().filter(temp ->
                        DefaultFlag.YES.equals(temp.getDefaultFlag())).findFirst().get();
            }
            if (DefaultFlag.NO.equals(templateStore.getFreightType())) {
                //1.2. 满金额包邮情况
                if (totalPrice.compareTo(templateStore.getSatisfyPrice()) < 0) {
                    freight = templateStore.getSatisfyFreight();
                }
            } else {
                //1.3. 固定运费情况
                freight = templateStore.getFixedFreight();
            }
        } else if (DefaultFlag.YES.equals(supplier.getFreightTemplateType())) {
            // 2.单品运费模板计算
            // 2.1.根据templateId分组聚合总件数,重量,体积,价格, 并查询各运费模板信息
            Map<Long, TradeItem> templateGoodsMap = new LinkedHashMap<>();
            this.setGoodsSumMap(templateGoodsMap, goodsList);
            this.setGoodsSumMap(templateGoodsMap, giftList);
            List<Long> tempIdList = new ArrayList<>(templateGoodsMap.keySet());
            List<FreightTemplateGoodsVO> templateList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(tempIdList)) {
                templateList = tradeCacheService.queryFreightTemplateGoodsListByIds(tempIdList);
                // 2.2.剔除满足指定条件包邮的运费模板(即剔除运费为0的)
                templateList = templateList.stream().filter(temp ->
                        getFreeFreightFlag(temp, templateGoodsMap, deliverWay, consignee.getProvinceId(), consignee.getCityId()))
                        .collect(Collectors.toList());

                /*2.3.逻辑已修改，原先的逻辑是取出首运费最大的运费模板来计算所有商品
                现逻辑变成：按运费模板对商品进行分组，不同组各自计算运费*/
                for (int i = 0; i < templateList.size(); i++) {
                    FreightTemplateGoodsVO temp = templateList.get(i);
                    FreightTemplateGoodsExpressVO freExp = getMatchFreightTemplate(temp.getFreightTemplateGoodsExpresses(),
                            consignee.getProvinceId(), consignee.getCityId());
                    temp.setExpTemplate(freExp);
                }
                for (FreightTemplateGoodsVO freightTemplateGoodsVO : templateList) {
                    TradeItem tradeItem = templateGoodsMap.get(freightTemplateGoodsVO.getFreightTempId());
                    if(cycleBuyFlag) {
                        freight = freight.add(getCycleBuySingleTemplateFreight(freightTemplateGoodsVO, tradeItem, goodsList.get(NumberUtils.INTEGER_ZERO)));
                    } else {
                        freight = freight.add(getSingleTemplateFreight(freightTemplateGoodsVO, tradeItem));
                    }
                }
            }
        }
        return freight;
    }

    /**
     * 是否包邮
     *
     * @param temp             单品运费模板
     * @param templateGoodsMap 按模板id分组的商品汇总信息
     * @param deliverWay       运送方式
     * @param provId           省份id
     * @param cityId           城市id
     * @return
     */
    private boolean getFreeFreightFlag(FreightTemplateGoodsVO temp, Map<Long, TradeItem> templateGoodsMap, DeliverWay
            deliverWay, Long provId, Long cityId) {
        if (DefaultFlag.YES.equals(temp.getSpecifyTermFlag())) {
            ValuationType valuationType = temp.getValuationType();
            List<FreightTemplateGoodsFreeVO> freeTemplateList = temp.getFreightTemplateGoodsFrees();
            Optional<FreightTemplateGoodsFreeVO> freeOptional = freeTemplateList.stream().filter(free -> matchArea(
                    free.getDestinationArea(), provId, cityId)).findFirst();

            //2.3.1. 找到收货地匹配的 并且 运送方式一致的指定包邮条件
            if (freeOptional.isPresent() && deliverWay.equals(freeOptional.get().getDeliverWay())) {
                FreightTemplateGoodsFreeVO freeObj = freeOptional.get();
                ConditionType conditionType = freeObj.getConditionType();

                //2.3.2. 根据计价方式,计算包邮条件是否满足
                switch (valuationType) {
                    case NUMBER: //按件数
                        switch (conditionType) {
                            case VALUATION:
                                if (BigDecimal.valueOf(templateGoodsMap.get(temp.getFreightTempId()).getNum())
                                        .compareTo(freeObj.getConditionOne()) >= 0) {//件数高于-包邮
                                    return false;
                                }
                                break;
                            case MONEY:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                        .compareTo(freeObj.getConditionTwo()) >= 0) {//金额高于-包邮
                                    return false;
                                }
                                break;
                            case VALUATIONANDMONEY:
                                if (BigDecimal.valueOf(templateGoodsMap.get(temp.getFreightTempId()).getNum())
                                        .compareTo(freeObj.getConditionOne()) >= 0 &&
                                        templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                                .compareTo(freeObj.getConditionTwo()) >= 0) {//件数高于,金额高于-包邮
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case WEIGHT: //按重量
                        switch (conditionType) {
                            case VALUATION:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getGoodsWeight()
                                        .compareTo(freeObj.getConditionOne()) <= 0) {//重量低于-包邮
                                    return false;
                                }
                                break;
                            case MONEY:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                        .compareTo(freeObj.getConditionTwo()) >= 0) {//金额高于-包邮
                                    return false;
                                }
                                break;
                            case VALUATIONANDMONEY:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getGoodsWeight()
                                        .compareTo(freeObj.getConditionOne()) <= 0 &&
                                        templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                                .compareTo(freeObj.getConditionTwo()) >= 0) {//重量低于,金额高于-包邮
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case VOLUME: //按体积
                        switch (conditionType) {
                            case VALUATION:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getGoodsCubage()
                                        .compareTo(freeObj.getConditionOne()) <= 0) {//体积低于-包邮
                                    return false;
                                }
                                break;
                            case MONEY:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                        .compareTo(freeObj.getConditionTwo()) >= 0) {//金额高于-包邮
                                    return false;
                                }
                                break;
                            case VALUATIONANDMONEY:
                                if (templateGoodsMap.get(temp.getFreightTempId()).getGoodsCubage()
                                        .compareTo(freeObj.getConditionOne()) <= 0 &&
                                        templateGoodsMap.get(temp.getFreightTempId()).getSplitPrice()
                                                .compareTo(freeObj.getConditionTwo()) >= 0) {//体积低于,金额高于-包邮
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * 计算某个单品模板的运费
     * @param temp             单品运费模板
     * @param traItem 按模板id分组的商品汇总信息
     * @return 模板的总运费
     */
    private BigDecimal getSingleTemplateFreight(FreightTemplateGoodsVO temp, TradeItem traItem) {
        switch (temp.getValuationType()) {
            case NUMBER: //按件数
                return getStartAndPlusFreight(BigDecimal.valueOf(traItem.getNum()), temp.getExpTemplate());
            case WEIGHT: //按重量
                return getStartAndPlusFreight(traItem.getGoodsWeight(), temp.getExpTemplate());
            case VOLUME: //按体积
                return getStartAndPlusFreight(traItem.getGoodsCubage(), temp.getExpTemplate());
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * 计算周期购单品模板的运费
     *
     * @param temp             单品运费模板
     * @param traItem          按模板id分组的商品汇总信息
     * @param goods            周期购商品
     * @return 模板的总运费
     */
    private BigDecimal getCycleBuySingleTemplateFreight(FreightTemplateGoodsVO temp, TradeItem traItem, TradeItem goods) {
        //是否需要计算首件运费标识
//        boolean startFlag = temp.getFreightTempId().equals(freightTempId);
//        TradeItem traItem = templateGoodsMap.get(temp.getFreightTempId());
        BigDecimal freight = BigDecimal.ZERO;
        BigDecimal giftFreight = BigDecimal.ZERO;

        //默认取模版中的数据
        Long goodsCount = traItem.getNum();
        BigDecimal goodsWeight = traItem.getGoodsWeight();
        BigDecimal goodsCubage = traItem.getGoodsCubage();

        //当前模版用的是主商品的模版，用主商品的数据覆盖模版中的数据
        Long goodsFreightTempId = goods.getFreightTempId();
        if(goodsFreightTempId.equals(temp.getFreightTempId())) {
            goodsCount = goods.getNum();
            goodsWeight = goods.getGoodsWeight().multiply(BigDecimal.valueOf(goodsCount));
            goodsCubage = goods.getGoodsCubage().multiply(BigDecimal.valueOf(goodsCount));
        }

        switch (temp.getValuationType()) {
            case NUMBER: //按件数
                freight = getStartAndPlusFreight(BigDecimal.valueOf(goodsCount), temp.getExpTemplate());
                if(!goodsCount.equals(traItem.getNum())) {
                    Long giftCount = traItem.getNum() - goodsCount;
                    giftFreight = getPlusFreight(BigDecimal.valueOf(giftCount), temp.getExpTemplate());
                }
                break;
            case WEIGHT: //按重量
                freight = getStartAndPlusFreight(traItem.getGoodsWeight(), temp.getExpTemplate());
                if(!goodsWeight.equals(traItem.getGoodsWeight())) {
                    BigDecimal giftWeight = traItem.getGoodsWeight().subtract(goodsWeight);
                    giftFreight = getPlusFreight(giftWeight, temp.getExpTemplate());
                }
                break;
            case VOLUME: //按体积
                freight = getStartAndPlusFreight(traItem.getGoodsCubage(), temp.getExpTemplate());
                if(!goodsCubage.equals(traItem.getGoodsCubage())) {
                    BigDecimal giftCubage = traItem.getGoodsCubage().subtract(goodsCubage);
                    giftFreight = getPlusFreight(giftCubage, temp.getExpTemplate());
                }
                break;
        }
        if(goodsFreightTempId.equals(temp.getFreightTempId())) {
            freight = dealCycleBuyDeliverPrice(goods.getSpuId(), goods.getCycleNum(), freight, giftFreight);
        }

        return freight;
    }

    /**
     * 计算 首件 + 续件 总费用
     *
     * @param itemCount
     * @param expTemplate
     * @return
     */
    private BigDecimal getStartAndPlusFreight(BigDecimal itemCount, FreightTemplateGoodsExpressVO expTemplate) {
        if (itemCount.compareTo(expTemplate.getFreightStartNum()) <= 0) {
            return expTemplate.getFreightStartPrice();//首件数以内,则只算首运费
        } else {
            //总费用 = 首件费用 + 续件总费用
            return expTemplate.getFreightStartPrice().add(
                    getPlusFreight(itemCount.subtract(expTemplate.getFreightStartNum()), expTemplate)
            );
        }
    }

    /**
     * 计算续件总费用
     *
     * @param itemCount   商品数量
     * @param expTemplate 匹配的运费模板
     * @return 续件总费用
     */
    private BigDecimal getPlusFreight(BigDecimal itemCount, FreightTemplateGoodsExpressVO expTemplate) {
        //商品数量/续件数量 * 续件金额
        return itemCount.divide(expTemplate.getFreightPlusNum(), 0, BigDecimal.ROUND_UP)
                .multiply(expTemplate.getFreightPlusPrice());
    }

    /**
     * 获取匹配的单品运费模板-用于计算运费
     *
     * @param temps  多个收货的运费模板
     * @param provId 省份id
     * @param cityId 地市id
     * @return 匹配上的运费模板
     */
    private FreightTemplateGoodsExpressVO getMatchFreightTemplate(List<FreightTemplateGoodsExpressVO> temps,
                                                                  Long provId,
                                                                  Long cityId) {
        Optional<FreightTemplateGoodsExpressVO> expOpt = temps.stream().filter(exp ->
                matchArea(exp.getDestinationArea(), provId, cityId)).findFirst();
        FreightTemplateGoodsExpressVO expTemp;
        if (expOpt.isPresent()) {
            expTemp = expOpt.get();
        } else {
            expTemp = temps.stream().filter(exp ->
                    DefaultFlag.YES.equals(exp.getDefaultFlag())).findFirst().get();
        }
        return expTemp;
    }

    /**
     * 匹配配送地区
     *
     * @param areaStr 存储的逗号相隔的areaId(provId,cityId都有可能)
     * @param provId  收货省份id
     * @param cityId  收货城市id
     * @return 是否匹配上
     */
    private boolean matchArea(String areaStr, Long provId, Long cityId) {
        String[] arr = areaStr.split(",");
        return Arrays.stream(arr).anyMatch(area -> area.equals(String.valueOf(provId)))
                || Arrays.stream(arr).anyMatch(area -> area.equals(String.valueOf(cityId)));
    }

    /**
     * 按模板id分组的商品汇总信息(模板Id,件数,重量,体积,小计均摊价)
     *
     * @param templateGoodsMap
     * @param items
     */
    private void setGoodsSumMap(Map<Long, TradeItem> templateGoodsMap, List<TradeItem> items) {
        if (items != null) {
            //下单不计算非商家商品,卡券商品的运费
            items.stream().filter(item -> !GoodsType.VIRTUAL_COUPON.equals(item.getGoodsType())
                    && !GoodsType.VIRTUAL_GOODS.equals(item.getGoodsType())).forEach(goods -> {
                TradeItem item = templateGoodsMap.get(goods.getFreightTempId());
                if (item == null) {
                    templateGoodsMap.put(goods.getFreightTempId(), TradeItem.builder()
                            .freightTempId(goods.getFreightTempId())
                            .num(goods.getNum())
                            .goodsWeight(goods.getGoodsWeight().multiply(BigDecimal.valueOf(goods.getNum())))
                            .goodsCubage(goods.getGoodsCubage().multiply(BigDecimal.valueOf(goods.getNum())))
                            .splitPrice(goods.getSplitPrice() == null ? BigDecimal.ZERO : goods.getSplitPrice())
                            .build());
                } else {
                    item.setNum(item.getNum() + goods.getNum());
                    item.setGoodsWeight(item.getGoodsWeight().add(goods.getGoodsWeight().multiply(BigDecimal.valueOf
                            (goods.getNum()))));
                    item.setGoodsCubage(item.getGoodsCubage().add(goods.getGoodsCubage().multiply(BigDecimal.valueOf
                            (goods.getNum()))));
                    item.setSplitPrice(item.getSplitPrice().add(goods.getSplitPrice() == null ? BigDecimal.ZERO :
                            goods.getSplitPrice()));
                }
            });
        }
    }

    /**
     * 获取订单商品详情,不包含区间价，会员级别价信息
     */
    public TradeGetGoodsResponse getGoodsResponse(List<String> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return new TradeGetGoodsResponse();
        }
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
        TradeGetGoodsResponse goodsResponse = new TradeGetGoodsResponse();
        goodsResponse.setGoodses(response.getGoodses());
        goodsResponse.setGoodsInfos(response.getGoodsInfos());
        return goodsResponse;
    }

    /**
     * 发货校验,检查请求发货商品数量是否符合应发货数量
     *
     * @param tid                 订单id
     * @param tradeDeliverRequest 发货请求参数结构
     */
    public void deliveryCheck(String tid, TradeDeliverRequest tradeDeliverRequest) {
        Trade trade = detail(tid);

        //周期购订单 重新设置商品数量 购买的数量*期数
        if (trade.getCycleBuyFlag()) {
            trade.getTradeItems().forEach(tradeItem -> {
                tradeItem.setNum(tradeItem.getCycleNum() * tradeItem.getNum());
            });
        }

        Map<String, TradeItem> skusMap = trade.getTradeItems().stream().collect(Collectors.toMap(TradeItem::getSkuId,
                Function.identity()));
        Map<String, TradeItem> giftsMap = trade.getGifts().stream().collect(Collectors.toMap(TradeItem::getSkuId,
                Function.identity()));
        tradeDeliverRequest.getShippingItemList().forEach(i -> {
            TradeItem tradeItem = skusMap.get(i.getSkuId());
            if (tradeItem.getDeliveredNum() + i.getItemNum() > tradeItem.getNum()) {
                throw new SbcRuntimeException("K-050315");
            }
        });
        tradeDeliverRequest.getGiftItemList().forEach(i -> {
            TradeItem tradeItem = giftsMap.get(i.getSkuId());
            if (tradeItem.getDeliveredNum() + i.getItemNum() > tradeItem.getNum()) {
                throw new SbcRuntimeException("K-050315");
            }
        });
    }

    /**
     * 根据用户提交的收货地址信息封装对象
     *
     * @param consigneeId         选择的收货地址id
     * @param detailAddress       详细地址(包括省市区)
     * @param consigneeUpdateTime 地址更新时间 - 可能已经用不到了
     * @param consigneeTmp        用户提交的临时收货地址
     * @return 封装后的收货地址对象
     */
    private Consignee wrapperConsignee(String consigneeId, String detailAddress, String consigneeUpdateTime,
                                       Consignee consigneeTmp) {
        if (StringUtils.isNotBlank(consigneeId)) {
            // 根据id查询收货人信息
            BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse =
                    tradeCacheService.getCustomerDeliveryAddressById(consigneeId);
            CustomerDeliveryAddressByIdResponse customerDeliveryAddressByIdResponse =
                    customerDeliveryAddressByIdResponseBaseResponse.getContext();
            if (customerDeliveryAddressByIdResponse == null || customerDeliveryAddressByIdResponse.getDelFlag() == DeleteFlag.YES) {
                throw new SbcRuntimeException("K-050313");
            }
            return Consignee
                    .builder()
                    .id(consigneeId)
                    .detailAddress(detailAddress)
                    .updateTime(consigneeUpdateTime)
                    .phone(customerDeliveryAddressByIdResponse.getConsigneeNumber())
                    .provinceId(customerDeliveryAddressByIdResponse.getProvinceId())
                    .cityId(customerDeliveryAddressByIdResponse.getCityId())
                    .areaId(customerDeliveryAddressByIdResponse.getAreaId())
                    .streetId(customerDeliveryAddressByIdResponse.getStreetId())
                    .address(customerDeliveryAddressByIdResponse.getDeliveryAddress())
                    .name(customerDeliveryAddressByIdResponse.getConsigneeName())
                    .build();
        } else {
            //若id为空,则赋值页面传入的临时地址(代客下单特殊-可以传临时地址)
            return Consignee
                    .builder()
                    .detailAddress(detailAddress)
                    .phone(consigneeTmp.getPhone())
                    .provinceId(consigneeTmp.getProvinceId())
                    .cityId(consigneeTmp.getCityId())
                    .areaId(consigneeTmp.getAreaId())
                    .streetId(consigneeTmp.getStreetId())
                    .address(consigneeTmp.getAddress())
                    .name(consigneeTmp.getName())
                    .build();
        }
    }

    /**
     * 根据用户提交的发票信息封装对象
     * 主要是为了补充 联系人 与 联系地址
     *
     * @param invoice             发票信息(至少缺联系人与联系地址)
     * @param invoiceConsigneeTmp 订单发票临时收货地址
     * @param consignee           订单商品收货地址
     * @return 完整的发票信息
     */
    private Invoice wrapperTradeInvoice(Invoice invoice, Consignee invoiceConsigneeTmp, Consignee consignee) {
        if (invoice.getType() != -1) {
            // 1.若用户选择了某个发票收货地址,查询该地址的联系人与联系方式
            if (StringUtils.isNotBlank(invoice.getAddressId())) {
                BaseResponse<CustomerDeliveryAddressByIdResponse> customerDeliveryAddressByIdResponseBaseResponse =
                        tradeCacheService.getCustomerDeliveryAddressById(invoice.getAddressId());
                CustomerDeliveryAddressByIdResponse customerDeliveryAddressByIdResponse =
                        customerDeliveryAddressByIdResponseBaseResponse.getContext();
                invoice.setPhone(customerDeliveryAddressByIdResponse.getConsigneeNumber());
                invoice.setContacts(customerDeliveryAddressByIdResponse.getConsigneeName());
                invoice.setProvinceId(customerDeliveryAddressByIdResponse.getProvinceId());
                invoice.setCityId(customerDeliveryAddressByIdResponse.getCityId());
                invoice.setAreaId(customerDeliveryAddressByIdResponse.getAreaId());
            }
            // 2.若用户没有选择发货地址，使用临时地址(代客下单特殊-可以传发票临时收货地址)
            else {
                // 2.1.临时地址为null，就用收货地址
                if (Objects.isNull(invoiceConsigneeTmp) || Objects.isNull(invoiceConsigneeTmp.getProvinceId())) {
                    invoice.setPhone(consignee.getPhone());
                    invoice.setContacts(consignee.getName());
                    invoice.setProvinceId(consignee.getProvinceId());
                    invoice.setCityId(consignee.getCityId());
                    invoice.setAreaId(consignee.getAreaId());
                    invoice.setAddress(consignee.getAddress());//依赖了前面步骤中封装的收货地址信息
                }
                // 2.2.使用填写的临时地址
                else {
                    invoice.setPhone(invoiceConsigneeTmp.getPhone());
                    invoice.setContacts(invoiceConsigneeTmp.getName());
                    invoice.setProvinceId(invoiceConsigneeTmp.getProvinceId());
                    invoice.setCityId(invoiceConsigneeTmp.getCityId());
                    invoice.setAreaId(invoiceConsigneeTmp.getAreaId());
                    invoice.setAddress(invoiceConsigneeTmp.getAddress());
                }
            }

            // 3.校验与填充增票信息
            if (invoice.getType() == 1) {
                SpecialInvoice spInvoice = invoice.getSpecialInvoice();
                CustomerInvoiceByIdAndDelFlagRequest customerInvoiceByCustomerIdRequest =
                        new CustomerInvoiceByIdAndDelFlagRequest();
                customerInvoiceByCustomerIdRequest.setCustomerInvoiceId(spInvoice.getId());
                BaseResponse<CustomerInvoiceByIdAndDelFlagResponse> customerInvoiceByIdAndDelFlagResponseBaseResponse = tradeCacheService.getCustomerInvoiceByIdAndDelFlag(spInvoice.getId());
                CustomerInvoiceByIdAndDelFlagResponse customerInvoiceByIdAndDelFlagResponse =
                        customerInvoiceByIdAndDelFlagResponseBaseResponse.getContext();
                if (Objects.nonNull(customerInvoiceByIdAndDelFlagResponse)) {
                    if (customerInvoiceByIdAndDelFlagResponse.getCheckState() != CheckState.CHECKED) {
                        throw new SbcRuntimeException("K-010013");
                    }
                    spInvoice.setAccount(customerInvoiceByIdAndDelFlagResponse.getBankNo());
                    spInvoice.setIdentification(customerInvoiceByIdAndDelFlagResponse.getTaxpayerNumber());
                    spInvoice.setAddress(customerInvoiceByIdAndDelFlagResponse.getCompanyAddress());
                    spInvoice.setBank(customerInvoiceByIdAndDelFlagResponse.getBankName());
                    spInvoice.setCompanyName(customerInvoiceByIdAndDelFlagResponse.getCompanyName());
                    spInvoice.setPhoneNo(customerInvoiceByIdAndDelFlagResponse.getCompanyPhone());
                }
            }
        }
        return invoice;
    }

    /**
     * 创建订单-入库
     *
     * @param trade
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public Trade create(Trade trade, Operator operator) {
        Long storeId = trade.getSupplier().getStoreId();
        // 1.下单校验店铺有效性, 校验店铺支持的发票项
        boolean resultVerifyStore = tradeCacheService.verifyStore(Collections.singletonList(storeId));
        if (!resultVerifyStore) {
            throw new SbcRuntimeException("K-050117");
        }
        if (operator.getPlatform() != Platform.SUPPLIER) {
            boolean resultVerifyInvoice = tradeCacheService.verifyInvoice(trade.getInvoice(), trade.getSupplier());
            if (!resultVerifyInvoice) {
                throw new SbcRuntimeException("K-050209", new String[]{trade.getSupplier().getStoreName()});
            }
        }
        // 2.减商品库存，仅处理本平台
        // 处理卡券库存
        verifyService.subCouponSkuListStock(trade.getTradeItems(), trade.getId(), operator);
        verifyService.subCouponSkuListStock(trade.getGifts(), trade.getId(), operator);
        //判断是否为秒杀抢购订单--进行扣减秒杀商品库存和增加销量操作
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()) {
            //扣减秒杀商品库存和增加销量
            verifyService.batchFlashSaleGoodsStockAndSalesVolume(trade.getTradeItems());
            //秒杀商品参与活动不扣减库存改造，下单时要同时扣库存
            verifyService.subSkuListStock(trade.getTradeItems());
        } else {
            List<TradeItem> items =
                    trade.getTradeItems().stream().filter(i -> !ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType())).collect(Collectors.toList());
            verifyService.subSkuListStock(items);
            // 减赠品库存，仅处理本平台
            if (CollectionUtils.isNotEmpty(trade.getGifts())) {
                List<TradeItem> gifts =
                        trade.getGifts().stream().filter(i -> !ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType())).collect(Collectors.toList());
                verifyService.subSkuListStock(gifts);
            }
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()) {
                TradeItem tradeItem = trade.getTradeItems().get(0);
                List<BookingSaleGoodsVO> bookingSaleGoodsVOList =
                        bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().goodsInfoId(tradeItem.getSkuId()).bookingSaleId(tradeItem.getBookingSaleId()).build()).getContext().getBookingSaleGoodsVOList();
                if (Objects.nonNull(bookingSaleGoodsVOList.get(0).getBookingCount())) {
                    bookingSaleGoodsProvider.subCanBookingCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                            bookingSaleId(tradeItem.getBookingSaleId()).stock(tradeItem.getNum()).build());
                }
            }

        }

        // 3.初始化订单提交状态
        FlowState flowState;
        AuditState auditState;
        //是否开启订单审核（同时判断是否为秒杀抢购商品订单）
        Boolean orderAuditSwitch = tradeCacheService.isSupplierOrderAudit();

        //如果是秒杀抢购商品不需要审核
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()) {
            flowState = FlowState.AUDIT;
            auditState = AuditState.CHECKED;
            orderAuditSwitch = Boolean.FALSE;
            //预售商品不需要审核
        } else if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()) {
            flowState = FlowState.AUDIT;
            orderAuditSwitch = Boolean.FALSE;
            auditState = AuditState.CHECKED;
        }
        // 如果是拼团订单商品不需要审
        else if (Objects.nonNull(trade.getGrouponFlag()) && trade.getGrouponFlag()) {
            flowState = FlowState.GROUPON;
            auditState = AuditState.CHECKED;
            orderAuditSwitch = Boolean.FALSE;
        } else {
            if (!orderAuditSwitch) {
                flowState = FlowState.AUDIT;
                auditState = AuditState.CHECKED;
            } else {
                //商家 boss 初始化状态是不需要审核的
                if (operator.getPlatform() == Platform.BOSS || operator.getPlatform() == Platform.SUPPLIER || operator.getPlatform() == Platform.WX_VIDEO) {
                    flowState = FlowState.AUDIT;
                    auditState = AuditState.CHECKED;
                } else {
                    flowState = FlowState.INIT;
                    auditState = AuditState.NON_CHECKED;
                }
            }
        }
        trade.setTradeState(TradeState
                .builder()
                .deliverStatus(DeliverStatus.NOT_YET_SHIPPED)
                .flowState(flowState)
                .payState(PayState.NOT_PAID)
                .createTime(LocalDateTime.now())
                .build());
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() &&
                Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY) {
            trade.setTradeState(TradeState
                    .builder()
                    .deliverStatus(DeliverStatus.NOT_YET_SHIPPED)
                    .flowState(FlowState.WAIT_PAY_EARNEST)
                    .payState(PayState.NOT_PAID)
                    .createTime(LocalDateTime.now())
                    .handSelStartTime(trade.getTradeItems().get(0).getHandSelStartTime())
                    .handSelEndTime(trade.getTradeItems().get(0).getHandSelEndTime())
                    .tailStartTime(trade.getTradeItems().get(0).getTailStartTime())
                    .tailEndTime(trade.getTradeItems().get(0).getTailEndTime())
                    .build());
        }

        // 4.若订单审核关闭了,直接创建订单开票跟支付单
        createPayOrder(trade, operator, orderAuditSwitch);

        trade.getTradeState().setAuditState(auditState);
        trade.setIsAuditOpen(orderAuditSwitch);


        // 查询订单支付顺序设置
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_PAYMENT_ORDER);
        Integer paymentOrder =
                tradeCacheService.getTradeConfigByType(ConfigType.ORDER_SETTING_PAYMENT_ORDER).getStatus();
        trade.setPaymentOrder(PaymentOrder.values()[paymentOrder]);

        // 先款后货且已审核订单（审核开关关闭）且线上支付单
        Boolean needTimeOut = Objects.equals(auditState, AuditState.CHECKED) &&
                trade.getPaymentOrder() == PaymentOrder.PAY_FIRST && !PayType.OFFLINE.name().equals(trade.getPayInfo().getPayTypeName());
        if (needTimeOut) {
            // 先货后款情况下，查询订单是否开启订单失效时间设置
            request.setConfigType(ConfigType.ORDER_SETTING_TIMEOUT_CANCEL);
            ConfigVO timeoutCancelConfig =
                    tradeCacheService.getTradeConfigByType(ConfigType.ORDER_SETTING_TIMEOUT_CANCEL);
            Integer timeoutSwitch = timeoutCancelConfig.getStatus();
            if (timeoutSwitch == 1) {
                //视频号、小程序订单
                if (Objects.equals(trade.getChannelType(), ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                    int outTime = 60; //1小时
                    try {
                        // 查询设置中订单超时时间
                        JSONObject timeoutCancelConfigJsonObj = JSON.parseObject(orderConfigProperties.getTimeOutJson());
                        Object minuteObj = timeoutCancelConfigJsonObj.get("wxOrderTimeOut");
                        if (minuteObj != null) {
                            outTime = Integer.parseInt(minuteObj.toString());
                        }
                    } catch (Exception ex) {
                        log.error("TradeService timeoutCancelConfig error", ex);
                    }
                    trade.setOrderTimeOut(LocalDateTime.now().plusMinutes(outTime));
                    orderProducerService.cancelOrder(trade.getId(), outTime * 60 * 1000L);
                } else  if (Objects.nonNull(trade.getGrouponFlag()) && !trade.getGrouponFlag()) {
                    // 查询设置中订单超时时间
                    int outTime = 60; //1小时
                    try {
                        JSONObject timeoutCancelConfigJsonObj = JSON.parseObject(timeoutCancelConfig.getContext());
                        Object minuteObj = timeoutCancelConfigJsonObj.get("minute");
                        if (minuteObj != null) {
                            outTime = Integer.parseInt(minuteObj.toString());
                        }
                    } catch (Exception ex) {
                        log.error("TradeService timeoutCancelConfig error", ex);
                    }

                    // 发送非拼团单取消订单延迟队列;
                    trade.setOrderTimeOut(LocalDateTime.now().plusMinutes(outTime));
                    orderProducerService.cancelOrder(trade.getId(), outTime * 60 * 1000L);
                }
            }
        }

        // 拼团订单--设置订单状态
        if (Objects.nonNull(trade.getGrouponFlag()) && trade.getGrouponFlag()) {
            // 发送拼团单取消订单延迟队列
            orderProducerService.cancelOrder(trade.getId(), 5 * 60 * 1000L);
        }


        trade.appendTradeEventLog(new TradeEventLog(operator, "创建订单", "创建订单", LocalDateTime.now()));

        // 订单商品id集合
        List<String> goodsInfoIdList = trade.getTradeItems().stream().map(TradeItem::getSkuId).distinct().collect(Collectors.toList());
        List<String> goodsIdList = trade.getTradeItems().stream().map(TradeItem::getSpuId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(trade.getGifts())) {
            goodsInfoIdList.addAll(trade.getGifts().stream().map(TradeItem::getSkuId).distinct().collect(Collectors.toList()));
            goodsIdList.addAll(trade.getGifts().stream().map(TradeItem::getSpuId).distinct().collect(Collectors.toList()));
        }

        BaseResponse<GoodsInfoListByIdsResponse> listByIdsResponse =
                goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(goodsInfoIdList).build());
        BaseResponse<GoodsListByIdsResponse> goodsListResponse = goodsQueryProvider
                .listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsIdList).build());
        List<GoodsVO> goodsVOList = goodsListResponse.getContext().getGoodsVOList();
        List<GoodsInfoVO> goodsInfoVOList = listByIdsResponse.getContext().getGoodsInfos();

        //todo 增加ERP商品SKU编码和SPU编码add by wugongjiang
        trade.getTradeItems().stream().forEach(tradeItem -> {
            Optional<String> erpSkuNoOptional = goodsInfoVOList.stream()
                    .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(tradeItem.getSkuId()))
                    .map(GoodsInfoVO::getErpGoodsInfoNo)
                    .findFirst();
            Optional<String> erpSpuOptional = goodsInfoVOList.stream()
                    .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(tradeItem.getSkuId()))
                    .map(GoodsInfoVO::getErpGoodsNo)
                    .findFirst();

            if (erpSkuNoOptional.isPresent()){
                tradeItem.setErpSkuNo(erpSkuNoOptional.get());
            }

            if ( erpSpuOptional.isPresent()){
                tradeItem.setErpSpuNo(erpSpuOptional.get());
            }


            //设置是否组合商品
            goodsInfoVOList.forEach(goodsInfoVO -> {
                if (Objects.equals(goodsInfoVO.getGoodsInfoId(),tradeItem.getSkuId()) && Objects.nonNull(goodsInfoVO.getCombinedCommodity())  && goodsInfoVO.getCombinedCommodity()) {
                    tradeItem.setCombinedCommodity(goodsInfoVO.getCombinedCommodity());
                }
            });

        });

        if (!CollectionUtils.isEmpty(trade.getGifts())){
            trade.getGifts().stream().forEach(gift->{
                Optional<String> erpSkuNoOptional = goodsInfoVOList.stream()
                        .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(gift.getSkuId()))
                        .map(GoodsInfoVO::getErpGoodsInfoNo)
                        .findFirst();
                Optional<String> erpSpuOptional = goodsInfoVOList.stream()  .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(gift.getSkuId()))
                        .map(GoodsInfoVO::getErpGoodsNo)
                        .findFirst();

                if (erpSkuNoOptional.isPresent() ){
                    gift.setErpSkuNo(erpSkuNoOptional.get());
                }


                if ( erpSpuOptional.isPresent()){
                    gift.setErpSpuNo(erpSpuOptional.get());
                }

                //设置是否组合商品
                goodsInfoVOList.forEach(goodsInfoVO -> {
                    if (Objects.equals(goodsInfoVO.getGoodsInfoId(),gift.getSkuId()) && Objects.nonNull(goodsInfoVO.getCombinedCommodity()) && goodsInfoVO.getCombinedCommodity()) {
                        gift.setCombinedCommodity(goodsInfoVO.getCombinedCommodity());
                    }
                });

                //生产oid
                gift.setOid(generatorService.generateOid());
            });
        }
        // 5.订单入库
        tradeService.addTrade(trade);

        if (Platform.SUPPLIER.equals(operator.getPlatform())) {
            this.operationLogMq.convertAndSend(operator, "代客下单", "订单号" + trade.getId());
        }
        return trade;
    }

    /**
     * 创建订单-入库
     *
     * @param trade
     * @param operator
     */
    @Transactional
    public Trade update(Trade trade, Operator operator) {

        trade.setTailOrderNo(generatorService.generateTailTid());
        trade.getTradeState().setFlowState(FlowState.AUDIT);
        //创建尾款支付单
        Optional<PayOrder> optional = payOrderService.generatePayOrderByOrderCode(
                new PayOrderGenerateRequest(trade.getTailOrderNo(),
                        trade.getBuyer().getId(),
                        trade.getTradePrice().getTailPrice(),
                        trade.getTradePrice().getPoints(),
                        trade.getTradePrice().getKnowledge(),
                        PayType.valueOf(trade.getPayInfo().getPayTypeName()),
                        trade.getSupplier().getSupplierId(),
                        trade.getTradeState().getCreateTime(),
                        trade.getOrderType()));

        optional.ifPresent(payOrder -> trade.setTailPayOrderId(payOrder.getPayOrderId()));
        trade.setPaymentOrder(PaymentOrder.PAY_FIRST);

        // 设置尾款订单超时时间
        trade.setOrderTimeOut(trade.getTradeState().getTailEndTime());
        orderProducerService.cancelOrder(trade.getId(), Duration.between(LocalDateTime.now(),
                trade.getTradeState().getTailEndTime()).toMillis());

        trade.appendTradeEventLog(new TradeEventLog(operator, "更新尾款订单", "更新尾款订单", LocalDateTime.now()));

        // 订单更新
        tradeService.updateTrade(trade);

        return trade;
    }

    /**
     * 创建积分订单-入库
     *
     * @param trade
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public Trade createPoints(Trade trade, Operator operator) {
        // 初始化订单提交状态
        trade.setTradeState(TradeState
                .builder()
                .deliverStatus(DeliverStatus.NOT_YET_SHIPPED)
                .flowState(FlowState.AUDIT)
                .createTime(LocalDateTime.now())
                .build());

        // 创建订单开票跟支付单
        createPayOrder(trade, operator, false);

        // 创建对账单
        saveAccountRecord(trade);
        trade.getTradeState().setAuditState(AuditState.CHECKED);
        trade.getTradeState().setPayState(PayState.PAID);
        trade.appendTradeEventLog(new TradeEventLog(operator, "创建订单", "创建订单", LocalDateTime.now()));
        // 订单入库
        tradeService.addTrade(trade);

        return trade;
    }

    /**
     * 更新订单
     *
     * @param tradeUpdateRequest
     */
    @GlobalTransactional
    public void updateTradeInfo(TradeUpdateRequest tradeUpdateRequest) {
        tradeService.updateTrade(KsBeanUtil.convert(tradeUpdateRequest.getTrade(), Trade.class));
    }


    /**
     * 更新订单集合
     *
     * @param tradeUpdateListTradeRequest
     */
    @GlobalTransactional
    public void updateListTradeInfo(TradeUpdateListTradeRequest tradeUpdateListTradeRequest) {
        tradeUpdateListTradeRequest.getTradeDTOS().forEach(tradeDTO -> {
            tradeService.updateTrade(KsBeanUtil.convert(tradeDTO, Trade.class));
        });
    }

    /**
     * 生成对账单
     *
     * @param trade
     */
    private void saveAccountRecord(Trade trade) {
        // 根据订单id查询交易流水号
        BaseResponse<PayTradeRecordResponse> payTradeRecord = payQueryProvider.getTradeRecordByOrderOrParentCode(new
                TradeRecordByOrderOrParentCodeRequest(trade.getId(), trade.getParentId()));
        String tradeNo = Objects.isNull(payTradeRecord.getContext()) ? null : payTradeRecord.getContext().getTradeNo();
        // 添加对账记录
        AccountRecordAddRequest record = AccountRecordAddRequest.builder()
                .customerId(trade.getBuyer().getId())
                .customerName(trade.getBuyer().getName())
                .orderCode(trade.getId())
                .tradeNo(tradeNo)
                .orderTime(trade.getTradeState().getCreateTime())
                .payWay(PayWay.POINT)
                .storeId(trade.getSupplier().getStoreId())
                .supplierId(trade.getSupplier().getSupplierId())
                .tradeTime(LocalDateTime.now())
                .type((byte) 0)
                .build();
        // 计算积分结算价
        BigDecimal settlementPrice = trade.getTradeItems().stream()
                .map(tradeItem -> tradeItem.getSettlementPrice().multiply(BigDecimal.valueOf(tradeItem.getNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        record.setAmount(settlementPrice);
        record.setPoints(trade.getTradePrice().getPoints());
        accountRecordProvider.add(record);
    }

    /**
     * 批量创建订单
     *
     * @param trades   各店铺订单
     * @param operator 操作人
     * @return 订单提交结果集
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> createBatch(List<Trade> trades, TradeGroup tradeGroup, Operator operator) {
        //linkedMall验证
        linkedMallTradeService.verify(trades);

        List<TradeCommitResult> resultList = new ArrayList<>();
        final String parentId = generatorService.generatePoId();
        trades.forEach(
                trade -> {
                    //创建订单
                    try {
                        trade.setParentId(parentId);
                        Trade result = create(trade, operator);
                        // 根据供货商拆单并入库
                        this.splitProvideTrade(result);
                        if (Objects.nonNull(result.getIsBookingSaleGoods()) && result.getIsBookingSaleGoods()
                                && Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY) {
                            resultList.add(new TradeCommitResult(result.getId(),
                                    result.getParentId(), result.getTradeState(),
                                    result.getPaymentOrder(), result.getTradePrice().getEarnestPrice(),
                                    result.getOrderTimeOut(), result.getSupplier().getStoreName(),
                                    result.getSupplier().getIsSelf(), result.getTradePrice().getOriginPrice(),orderCouponService.checkSendCoupon(trade)));
                        } else {
                            resultList.add(new TradeCommitResult(result.getId(),
                                    result.getParentId(), result.getTradeState(),
                                    result.getPaymentOrder(), result.getTradePrice().getTotalPrice(),
                                    result.getOrderTimeOut(), result.getSupplier().getStoreName(),
                                    result.getSupplier().getIsSelf(), result.getTradePrice().getOriginPrice(),orderCouponService.checkSendCoupon(trade)));
                        }
                    } catch (Exception e) {
                        log.error("commit trade error,trade={}，错误信息：{}", trade, e);
                        if (e instanceof SbcRuntimeException) {
                            throw e;
                        } else {
                            throw new SbcRuntimeException("K-020010");
                        }
                    }
                }
        );


        // 平台优惠券
        List<CouponCodeBatchModifyDTO> dtoList = new ArrayList<>();
        if (tradeGroup != null) {
            // 2.修改优惠券状态
            TradeCouponVO tradeCoupon = tradeGroup.getCommonCoupon();
            dtoList.add(CouponCodeBatchModifyDTO.builder()
                    .couponCodeId(tradeCoupon.getCouponCodeId())
                    .orderCode(null)
                    .customerId(operator.getUserId())
                    .useStatus(DefaultFlag.YES).build());
        }
        // 店铺优惠券
        // 批量修改优惠券状态
        trades.forEach(trade -> {
            if (trade.getTradeCoupon() != null) {
                TradeCouponVO tradeCoupon = trade.getTradeCoupon();
                dtoList.add(CouponCodeBatchModifyDTO.builder()
                        .couponCodeId(tradeCoupon.getCouponCodeId())
                        .orderCode(trade.getId())
                        .customerId(trade.getBuyer().getId())
                        .useStatus(DefaultFlag.YES).build());
            }
        });
        if (dtoList.size() > 0) {
            couponCodeProvider.batchModify(CouponCodeBatchModifyRequest.builder().modifyDTOList(dtoList).build());
        }

        trades.stream().filter(trade -> Objects.nonNull(trade.getTradePrice()) &&
                Objects.nonNull(trade.getTradePrice().getPoints()) && trade.getTradePrice().getPoints() > 0).forEach(trade -> {
            // 增加客户积分明细 扣除积分
            customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                    .customerId(trade.getBuyer().getId())
                    .type(OperateType.DEDUCT)
                    .serviceType(PointsServiceType.ORDER_DEDUCTION)
                    .points(trade.getTradePrice().getPoints())
                    .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                    .build());
        });

        trades.stream().filter(trade -> !AuditState.REJECTED.equals(trade.getTradeState().getAuditState())).forEach(trade -> {
            MessageMQRequest messageMQRequest = new MessageMQRequest();
            Map<String, Object> map = new HashMap<>();
            map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
            map.put("id", trade.getId());
            if (AuditState.CHECKED.equals(trade.getTradeState().getAuditState())) {
                messageMQRequest.setNodeCode(OrderProcessType.ORDER_COMMIT_SUCCESS.getType());
                map.put("node", OrderProcessType.ORDER_COMMIT_SUCCESS.toValue());
            } else {
                messageMQRequest.setNodeCode(OrderProcessType.ORDER_COMMIT_SUCCESS_CHECK.getType());
                map.put("node", OrderProcessType.ORDER_COMMIT_SUCCESS_CHECK.toValue());
            }
            messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
            messageMQRequest.setParams(Lists.newArrayList(trade.getTradeItems().get(0).getSkuName()));
            messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
            messageMQRequest.setRouteParam(map);
            messageMQRequest.setCustomerId(trade.getBuyer().getId());
            messageMQRequest.setMobile(trade.getBuyer().getAccount());
            orderProducerService.sendMessage(messageMQRequest);
        });

        return resultList;
    }


    /**
     * 提交积分订单
     *
     * @param trade    积分订单
     * @param commitRequest 请求参数
     * @return 订单提交结果
     */
    @Transactional
    @GlobalTransactional
    public PointsTradeCommitResult createPointsTrade(Trade trade, PointsTradeCommitRequest commitRequest) {
        Operator operator = commitRequest.getOperator();
        //linkedMall验证
        linkedMallTradeService.verify(Collections.singletonList(trade));
        PointsTradeCommitResult commitResult = null;

        //创建订单
        try {
            Trade result = createPoints(trade, operator);
            this.splitProvideTrade(trade);
            commitResult = new PointsTradeCommitResult(result.getId(), result.getTradePrice().getPoints());
        } catch (Exception e) {
            log.error("commit points trade error,trade={}", trade, e);
            if (e instanceof SbcRuntimeException) {
                throw e;
            } else {
                throw new SbcRuntimeException("K-020010");
            }
        }

        //linkedMall订单同步
        if (ThirdPlatformType.LINKED_MALL.equals(trade.getThirdPlatformType())) {
            LinkedMallTradeResult result = linkedMallTradeService.add(trade.getId());
            if (CollectionUtils.isNotEmpty(result.getAutoRefundTrades())) {
                throw new SbcRuntimeException("K-020010");
            }
            if (CollectionUtils.isNotEmpty(result.getSuccessTrades())) {
                for (Trade tmpTrade : result.getSuccessTrades()) {
                    try {
                        int res = linkedMallTradeService.pay(tmpTrade.getId());
                        if (res != 0) {
                            throw new SbcRuntimeException("K-020010");
                        }
                    } catch (Exception e) {
                        throw new SbcRuntimeException("K-020010");
                    }
                }
            }
        }

        // 增加客户积分明细 扣除积分
/*        customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                .customerId(trade.getBuyer().getId())
                .type(OperateType.DEDUCT)
                .serviceType(PointsServiceType.POINTS_EXCHANGE)
                .points(trade.getTradePrice().getPoints())
                .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                .build());*/
        // 调用积分锁定 然后直接扣除，积分兑换 不需要支付回调修改状态
        String deductCode = externalProvider.pointLock(FanDengPointLockRequest.builder()
                .desc("提交订单锁定(订单号:"+trade.getId()+")")
                .point(trade.getTradePrice().getPoints())
                .userNo(commitRequest.
                        getCustomer().getFanDengUserNo())
                .sourceId(trade.getId())
                .sourceType(NumberUtils.INTEGER_ONE)
                .build()).getContext().getDeductionCode();
        externalProvider.pointDeduct(FanDengPointDeductRequest.builder()
                .deductCode(deductCode).build());

        // 扣除商品库存、积分商品可兑换数量
        pointsGoodsSaveProvider.minusStock(PointsGoodsMinusStockRequest.builder().stock(trade.getTradeItems().get(0)
                .getNum()).pointsGoodsId(trade.getTradeItems().get(0).getPointsGoodsId()).build());

        return commitResult;
    }

    /**
     * 创建订单和订单组
     */
    @Transactional
    @GlobalTransactional
    public List<TradeCommitResult> createBatchWithGroup(List<Trade> trades, TradeGroup tradeGroup, Operator operator) {
        // 1.保存订单及订单组信息
        if (StringUtils.isEmpty(tradeGroup.getId())) {
            tradeGroup.setId(UUIDUtil.getUUID());
        }

        tradeGroupService.addTradeGroup(tradeGroup);
        trades.forEach(trade -> trade.setGroupId(tradeGroup.getId()));
        List<TradeCommitResult> resultList = this.createBatch(trades, tradeGroup, operator);


        return resultList;
    }

    /**
     * 订单改价
     *
     * @param request  包含修改后的订单总价和运费价格
     * @param tid      订单编号
     * @param operator 操作人信息
     */
    @Transactional
    @GlobalTransactional
    public void changePrice(TradePriceChangeRequest request, String tid, Operator operator) {
        //1.获取旧订单信息,并校验当前状态是否可修改
        Trade trade = this.detail(tid);
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            throw new SbcRuntimeException("K-050212");
        }
        BaseResponse<PayTradeRecordCountResponse> response = payQueryProvider.getTradeRecordCountByOrderOrParentCode(new
                TradeRecordCountByOrderOrParentCodeRequest(trade.getId(), trade.getParentId()));
        long tradeRecordCount = response.getContext().getCount();
        if (tradeRecordCount > 0) {
            throw new SbcRuntimeException("K-050211");
        }

        //2.校验客户有效性
        verifyService.verifyCustomer(trade.getBuyer().getId());
        //3.改价
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal oldDeliveryPrice = tradePrice.getDeliveryPrice() == null ? BigDecimal.ZERO : tradePrice
                .getDeliveryPrice();
        //3.1 重置特价和运费
        tradePrice.setDeliveryPrice(request.getFreight());
        tradePrice.setPrivilegePrice(request.getTotalPrice());
        tradePrice.setSpecial(true);
        tradePrice.setEnableDeliveryPrice(request.getFreight() != null);
        BigDecimal freight = request.getFreight() == null ? BigDecimal.ZERO : request.getFreight();
        //3.2 重置原价 原始商品总额+新运费
        tradePrice.setOriginPrice(tradePrice.getOriginPrice().subtract(oldDeliveryPrice).add(freight));
        //3.3 重置均摊价和应付金额
        tradeItemService.clacSplitPrice(trade.getTradeItems(), tradePrice.getPrivilegePrice());
        // 3.4. 已计算好均摊价后，如果有分销商品，重新赋值分销商品的实付金额
        if (CollectionUtils.isNotEmpty(trade.getDistributeItems())) {
            // 如果有分销商品
            this.reCalcDistributionItem(trade);
        }
        tradePrice.setTotalPrice(tradePrice.getPrivilegePrice().add(freight));//应付金额 = 特价+运费

        //4.在s2b模式下 如果存在支付单，删除，创建一笔新的支付单
        if (osUtil.isS2b() && trade.getTradeState().getAuditState().equals(AuditState.CHECKED)) {
            if (Objects.nonNull(trade.getPayOrderId())) {
                payOrderService.deleteByPayOrderId(trade.getPayOrderId());
                receivableService.deleteReceivables(Collections.singletonList(trade.getPayOrderId()));
            }
            //创建支付单
            Optional<PayOrder> optional = payOrderService.generatePayOrderByOrderCode(
                    new PayOrderGenerateRequest(trade.getId(),
                            trade.getBuyer().getId(),
                            trade.getTradePrice().getTotalPrice(),
                            trade.getTradePrice().getPoints(),
                            trade.getTradePrice().getKnowledge(),
                            PayType.valueOf(trade.getPayInfo().getPayTypeName()),
                            trade.getSupplier().getSupplierId(),
                            trade.getTradeState().getCreateTime(),
                            OrderType.NORMAL_ORDER));

            trade.getTradeState().setPayState(PayState.NOT_PAID);
            optional.ifPresent(payOrder -> trade.setPayOrderId(payOrder.getPayOrderId()));
        }

        //7.状态变更,订单修改入库
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(trade.getId())
                .operator(operator)
                .data(trade)
                .event(TradeEvent.REMEDY)
                .build();
        tradeFSMService.changeState(stateRequest);
    }

    /**
     * 订单改价后，重新计算分销商品的佣金
     *
     * @param trade
     */
    public void reCalcDistributionItem(Trade trade) {
        // 如果有分销商品
        IteratorUtils.zip(trade.getDistributeItems(), trade.getTradeItems(),
                (collect1, levels1) -> collect1.getGoodsInfoId().equals(levels1.getSkuId()),
                (collect2, levels2) -> {
                    collect2.setActualPaidPrice(levels2.getSplitPrice());
                    collect2.setCommission(levels2.getSplitPrice().multiply(levels2.getCommissionRate()));
                    levels2.setDistributionCommission(collect2.getCommission());
                }
        );
        // 重新计算订单总佣金
        BigDecimal totalCommission = trade.getDistributeItems().stream().map(item -> item.getCommission())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        trade.setCommission(totalCommission);
    }

    /**
     * 修改订单
     *
     * @param request
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void remedy(TradeRemedyRequest request, Operator operator, StoreInfoResponse storeInfoResponse) {
        //1.获取旧订单信息,并校验当前状态是否可修改
        Trade trade = this.detail(request.getTradeId());
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            throw new SbcRuntimeException("K-050212");
        }

        BaseResponse<PayTradeRecordCountResponse> response = payQueryProvider.getTradeRecordCountByOrderOrParentCode(new
                TradeRecordCountByOrderOrParentCodeRequest(trade.getId(), trade.getParentId()));
        long tradeRecordCount = response.getContext().getCount();
        if (tradeRecordCount > 0) {
            throw new SbcRuntimeException("K-050211");
        }

        //2.校验客户有效性
        verifyService.verifyCustomer(trade.getBuyer().getId());

        //批量新增旧订单商品，赠品库存
        List<GoodsInfoPlusStockDTO> plusStockList = trade.getTradeItems().stream().map(i -> {
            GoodsInfoPlusStockDTO dto = new GoodsInfoPlusStockDTO();
            dto.setStock(i.getNum());
            dto.setGoodsInfoId(i.getSkuId());
            return dto;
        }).collect(Collectors.toList());

        List<GoodsPlusStockDTO> spuStockList = trade.getTradeItems().stream()
                .map(i -> new GoodsPlusStockDTO(i.getNum(), i.getSpuId())).collect(Collectors.toList());

        trade.getGifts().forEach(i -> {
            GoodsInfoPlusStockDTO dto = new GoodsInfoPlusStockDTO();
            dto.setStock(i.getNum());
            dto.setGoodsInfoId(i.getSkuId());
            plusStockList.add(dto);

            spuStockList.add(new GoodsPlusStockDTO(i.getNum(), i.getSpuId()));
        });

        if (CollectionUtils.isNotEmpty(plusStockList)) {
            GoodsInfoBatchPlusStockRequest plusStockRequest = GoodsInfoBatchPlusStockRequest.builder()
                    .stockList(plusStockList).build();
            goodsInfoProvider.batchPlusStock(plusStockRequest);
        }

        goodsStockService.batchAddStock(spuStockList);

        //4.校验与包装待修改的订单信息
        trade.getSupplier().setFreightTemplateType(storeInfoResponse.getFreightTemplateType());
        trade = this.wrapperBackendRemedyTrade(trade, operator, request);

        //5.减商品,赠品库存
        verifyService.subSkuListStock(trade.getTradeItems());
        verifyService.subSkuListStock(trade.getGifts());

        //6.在s2b模式下 如果存在支付单，删除，创建一笔新的支付单
        if (osUtil.isS2b() && trade.getTradeState().getAuditState().equals(AuditState.CHECKED)) {
            if (Objects.nonNull(trade.getPayOrderId())) {
                createPayOrder(trade);
            }
        }

        //7.状态变更,订单修改入库
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(trade.getId())
                .operator(operator)
                .data(trade)
                .event(TradeEvent.REMEDY)
                .build();
        tradeFSMService.changeState(stateRequest);
    }


    /**
     * 修改订单-部分修改
     *
     * @param request           修改订单请求对象
     * @param operator          操作人
     * @param storeInfoResponse 店铺信息
     */
    @Transactional
    @GlobalTransactional
    public void remedyPart(TradeRemedyRequest request, Operator operator, StoreInfoResponse storeInfoResponse) {
        //1.获取旧订单信息,并校验当前状态是否可修改
        Trade trade = this.detail(request.getTradeId());
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            throw new SbcRuntimeException("K-050212");
        }

        BaseResponse<PayTradeRecordCountResponse> response = payQueryProvider.getTradeRecordCountByOrderOrParentCode(new
                TradeRecordCountByOrderOrParentCodeRequest(trade.getId(), trade.getParentId()));
        long tradeRecordCount = response.getContext().getCount();
        if (tradeRecordCount > 0) {
            throw new SbcRuntimeException("K-050211");
        }

        // 2.校验客户有效性
//        verifyService.verifyCustomer(trade.getBuyer().getId());

        // 3.将新数据设置到旧订单trade对象中（包括收货信息、发票信息、特价、运费信息）
        request.getInvoice().setOrderInvoiceId(
                Objects.nonNull(trade.getInvoice()) ?
                        trade.getInvoice().getOrderInvoiceId() : null);
        trade.setConsignee(wrapperConsignee(request.getConsigneeId(), request.getConsigneeAddress(),
                request.getConsigneeUpdateTime(), request.getConsignee()));
        //发票信息(必须在收货地址下面-因为使用临时发票收货地,却未填写的时候,将使用订单商品收货地址作为发票收货地)
        trade.setInvoice(wrapperTradeInvoice(request.getInvoice(), request.getInvoiceConsignee(),
                trade.getConsignee()));
        trade.setDeliverWay(request.getDeliverWay());
        if (request.getPayType() != null) {
            trade.setPayInfo(PayInfo.builder()
                    .payTypeId(String.format("%d", request.getPayType().toValue()))
                    .payTypeName(request.getPayType().name())
                    .desc(request.getPayType().getDesc())
                    .build());
        }
        trade.setBuyerRemark(request.getBuyerRemark());
        trade.setSellerRemark(request.getSellerRemark());
        trade.setEncloses(request.getEncloses());
        trade.setRequestIp(operator.getIp());
        TradePrice tradePrice = trade.getTradePrice();
        TradePrice newTradePrice = request.getTradePrice();
        tradePrice.setPrivilegePrice(newTradePrice.getPrivilegePrice());
        tradePrice.setEnableDeliveryPrice(newTradePrice.isEnableDeliveryPrice());
        tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(tradePrice.getDeliveryPrice()));
        tradePrice.setDeliveryPrice(newTradePrice.getDeliveryPrice());

        // 4.如果取消特价的情况，则要重新计算totalPrice和tradeItem的splitPrice
        if (newTradePrice.isSpecial() == false && tradePrice.isSpecial() == true) {
            trade.getTradeItems().forEach(tradeItem -> {
                BigDecimal splitPrice = tradeItem.getLevelPrice().multiply(
                        new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
                List<TradeItem.MarketingSettlement> marketings = tradeItem.getMarketingSettlements();
                List<TradeItem.CouponSettlement> coupons = tradeItem.getCouponSettlements();
                if (!CollectionUtils.isEmpty(coupons)) {
                    splitPrice = coupons.get(coupons.size() - 1).getSplitPrice();
                } else if (!CollectionUtils.isEmpty(marketings)) {
                    splitPrice = marketings.get(marketings.size() - 1).getSplitPrice();
                }
                tradeItem.setSplitPrice(splitPrice);
            });
            BigDecimal totalPrice = trade.getTradeItems().stream()
                    .map(TradeItem::getSplitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            tradePrice.setTotalPrice(totalPrice);
        }
        tradePrice.setSpecial(newTradePrice.isSpecial());

        // 5.计算运费
        trade.getSupplier().setFreightTemplateType(storeInfoResponse.getFreightTemplateType());
        BigDecimal deliveryPrice = tradePrice.getDeliveryPrice();
        if (tradePrice.getDeliveryPrice() == null) {
            boolean virtualCouponGoods = Objects.isNull(trade.getIsVirtualCouponGoods())
                    || Boolean.FALSE.equals(trade.getIsVirtualCouponGoods());
            deliveryPrice = virtualCouponGoods ? tradeService.calcTradeFreight(trade.getConsignee(), trade.getSupplier(),
                    trade.getDeliverWay(),
                    tradePrice.getTotalPrice(), trade.getTradeItems(), trade.getGifts()) : BigDecimal.ZERO;
            tradePrice.setDeliveryPrice(deliveryPrice);
        }

        // 6.计算订单总价
        tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(deliveryPrice));
        if (tradePrice.isSpecial()) {
            trade.getTradeItems().forEach(tradeItem ->
                    tradeItem.setSplitPrice(tradeItem.getLevelPrice().multiply(
                            new BigDecimal(tradeItem.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP))
            );
            // 6.1 计算特价均摊价
            tradeItemService.clacSplitPrice(trade.getTradeItems(), tradePrice.getPrivilegePrice());

            // 6.2 已计算好均摊价后，如果有分销商品，重新赋值分销商品的实付金额
            if (CollectionUtils.isNotEmpty(trade.getDistributeItems())) {
                // 如果有分销商品
                this.reCalcDistributionItem(trade);
            }
            tradePrice.setTotalPrice(tradePrice.getPrivilegePrice().add(deliveryPrice));//应付金额 = 特价+运费
        } else {
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(deliveryPrice));//应付金额 = 应付+运费
        }

        // 7.在s2b模式下 如果存在支付单，删除，创建一笔新的支付单
        if (osUtil.isS2b() && trade.getTradeState().getAuditState().equals(AuditState.CHECKED)) {
            if (Objects.nonNull(trade.getPayOrderId())) {
                createPayOrder(trade);
            }
        }

        //8.状态变更,订单修改入库
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(trade.getId())
                .operator(operator)
                .data(trade)
                .event(TradeEvent.REMEDY)
                .build();
        tradeFSMService.changeState(stateRequest);

        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(trade.getId());
        for (ProviderTrade providerTrade : providerTrades) {
            providerTrade.setConsignee(trade.getConsignee());//收货信息
            providerTrade.setInvoice(trade.getInvoice());//发票信息
            providerTrade.setDeliverWay(trade.getDeliverWay());//快递方式
            providerTrade.setPayInfo(trade.getPayInfo());//支付方式
            providerTrade.setBuyerRemark(trade.getBuyerRemark());//备注
            providerTrade.setEncloses(trade.getEncloses());//订单附件
            providerTrade.setRequestIp(trade.getRequestIp());//调用方的请求 IP
            TradePrice providerTradePrice = providerTrade.getTradePrice();
            if (providerTrade.getId().startsWith("S")) {
                //运费算在商家子单上，供应商商品不计算运费
                providerTradePrice.setDeliveryPrice(tradePrice.getDeliveryPrice());
                providerTradePrice.setEnableDeliveryPrice(tradePrice.isEnableDeliveryPrice());
            }
            List<String> skuIds =
                    providerTrade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
            BigDecimal orderPrice = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(providerTrade.getTradeItems())) {
                List<TradeItem> tradeItems = trade.getTradeItems().stream()
                        .filter(tradeItem -> skuIds.contains(tradeItem.getSkuId()))
                        .collect(Collectors.toList());
                for (TradeItem tradeItem : tradeItems) {
                    Optional<TradeItem> optional = providerTrade.getTradeItems().stream()
                            .filter(t -> t.getSkuId().equals(tradeItem.getSkuId())).findFirst();
                    //更新商品分摊价
                    optional.ifPresent(t -> t.setSplitPrice(tradeItem.getSplitPrice()));
                    orderPrice = orderPrice.add(tradeItem.getSplitPrice());
                }
            }
            providerTradePrice.setTotalPrice(orderPrice.add(providerTradePrice.getDeliveryPrice()));
            providerTradePrice.setTotalPayCash(orderPrice);
            providerTrade.setTradePrice(providerTradePrice);

            providerTradeService.updateProviderTrade(providerTrade);
        }
    }


    /**
     * 取消订单
     *
     * @param tid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void cancel(String tid, Operator operator) {

        Trade trade = detail(tid);

        //判断订单对应的支付记录是否存在
        List<String> tidList = new ArrayList<>();
        tidList.add(tid);
        tidList.add(trade.getParentId());

        List<PayCallBackResult> payCallBackResultList =
                payCallBackResultService.list(PayCallBackResultQueryRequest.builder().businessIds(tidList).build());
        if (payCallBackResultList.size() > 0) {
            throw new SbcRuntimeException("K-050202");
        }


        if (!trade.getBuyer().getId().equals(operator.getUserId())) {
            throw new SbcRuntimeException("K-050100", new Object[]{tid});
        }
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            throw new SbcRuntimeException("K-050202");
        }
        if (trade.getTradeState().getDeliverStatus() != DeliverStatus.NOT_YET_SHIPPED) {
            throw new SbcRuntimeException("K-050203");
        }
        if (trade.getTradeState().getAuditState() == AuditState.CHECKED) {
            //删除支付单
            payOrderService.deleteByPayOrderId(trade.getPayOrderId());
        }
        //卡券商品释放库存
        verifyService.addCouponSkuListStock(trade.getTradeItems(), tid, operator);
        verifyService.addCouponSkuListStock(trade.getGifts(), tid, operator);
        //是否是秒杀抢购商品订单
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()) {
            flashSaleGoodsOrderAddStock(trade);
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
        } else {
            //释放库存
            verifyService.addSkuListStock(trade.getTradeItems());
            verifyService.addSkuListStock(trade.getGifts());
            bookingSaleGoodsOrderAddStock(trade);
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
        }
        //状态变更
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(trade.getId())
                .operator(operator)
                .event(TradeEvent.VOID)
                .data("用户取消订单")
                .build();
        tradeFSMService.changeState(stateRequest);
        // 退优惠券
        returnCoupon(tid);
        //取消拼团订单
        grouponOrderService.cancelGrouponOrder(trade);
        // 返换限售记录——取消订单
        orderProducerService.backRestrictedPurchaseNum(trade.getId(), null, BackRestrictedType.ORDER_CANCEL);
        // 取消供应商订单
        providerTradeService.providerCancel(tid, operator, false);

        //视频号
        if (Objects.equals(trade.getChannelType(),ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
            if (StringUtils.isBlank(trade.getBuyer().getOpenId())) {
                log.error("WxOrderService sendWxCancelOrderMessage orderId:{} openId:{} 自动取消openid为空", trade.getId(), trade.getBuyer().getOpenId());
                return;
            }
            WxVideoOrderDetailResponse wechatVideoOrder = wxOrderService.getWechatVideoOrder(trade);
            wxOrderService.releaseWechatVideoStock(wechatVideoOrder);
        }
    }

    /**
     * 订单审核
     *
     * @param tid
     * @param auditState 审核 | 驳回
     * @param reason     驳回原因，用于审核驳回
     * @param operator   操作人
     */
    @Transactional
    @GlobalTransactional
    public void audit(String tid, AuditState auditState, String reason, Operator operator) {
        if (operator.getPlatform() != Platform.BOSS && operator.getPlatform() != Platform.SUPPLIER &&
                operator.getPlatform() != Platform.PLATFORM) {
            throw new SbcRuntimeException("K-000014");
        }
        //订单驳回释放库存
        Trade trade = detail(tid);
        if (trade.getTradeState().getAuditState() != AuditState.NON_CHECKED) {
            throw new SbcRuntimeException("K-050316");
        }
        if (auditState == AuditState.REJECTED) {
            trade.getTradeState().setObsoleteReason(reason);
            verifyService.addSkuListStock(trade.getTradeItems());
            verifyService.addSkuListStock(trade.getGifts());
            verifyService.addCouponSkuListStock(trade.getTradeItems(), tid, operator);
            verifyService.addCouponSkuListStock(trade.getGifts(), tid, operator);
            bookingSaleGoodsOrderAddStock(trade);
        } else {
            createPayOrder(trade);
        }

        tradeService.updateTrade(trade);
        //订单状态扭转
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(TradeEvent.AUDIT)
                .data(auditState)
                .build();
        tradeFSMService.changeState(stateRequest);
        if (auditState == AuditState.REJECTED) {
            // 退优惠券
            returnCoupon(tid);
        }
        //订单审核通过/未通过发送通知消息
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        List<String> params = new ArrayList<>();
        params.add(trade.getTradeItems().get(0).getSkuName());
        Map<String, Object> map = new HashMap<>();
        map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
        map.put("id", trade.getId());
        if (AuditState.CHECKED.equals(auditState)) {
            messageMQRequest.setNodeCode(OrderProcessType.ORDER_CHECK_PASS.getType());
            map.put("node", OrderProcessType.ORDER_CHECK_PASS.toValue());
        } else {
            messageMQRequest.setNodeCode(OrderProcessType.ORDER_CHECK_NOT_PASS.getType());
            map.put("node", OrderProcessType.ORDER_CHECK_NOT_PASS.toValue());
            params.add(reason);
        }
        messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
        messageMQRequest.setCustomerId(trade.getBuyer().getId());
        messageMQRequest.setMobile(trade.getBuyer().getAccount());
        orderProducerService.sendMessage(messageMQRequest);
        // 驳回的订单返还限售数据
        if (AuditState.REJECTED.equals(auditState)) {
            orderProducerService.backRestrictedPurchaseNum(tid, null, BackRestrictedType.REFUND_ORDER);
        }

        // 同步审核供应商订单
        providerTradeService.providerAudit(tid, reason, auditState);

    }

    /**
     * auditAction 创建支付单
     *
     * @param trade trade
     */
    private void createPayOrder(Trade trade) {
        if (trade.getPayOrderId() != null) {
            payOrderService.deleteByPayOrderId(trade.getPayOrderId());
            receivableService.deleteReceivables(Collections.singletonList(trade.getPayOrderId()));
        }
        //创建支付单
        Optional<PayOrder> optional = payOrderService.generatePayOrderByOrderCode(
                new PayOrderGenerateRequest(trade.getId(),
                        trade.getBuyer().getId(),
                        Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                                && StringUtils.isEmpty(trade.getTailOrderNo()) ?
                                trade.getTradePrice().getEarnestPrice() : trade.getTradePrice().getTotalPrice(),
                        trade.getTradePrice().getPoints(),
                        trade.getTradePrice().getKnowledge(),
                        PayType.valueOf(trade.getPayInfo().getPayTypeName()),
                        trade.getSupplier().getSupplierId(),
                        trade.getTradeState().getCreateTime(),
                        trade.getOrderType()));

        trade.getTradeState().setPayState(PayState.NOT_PAID);
        optional.ifPresent(payOrder -> trade.setPayOrderId(payOrder.getPayOrderId()));
    }

    /**
     * 订单回审
     *
     * @param tid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void retrial(String tid, Operator operator) {
        Boolean orderAuditSwitch = auditQueryProvider.isSupplierOrderAudit().getContext().isAudit();

        if (!orderAuditSwitch) {
            throw new SbcRuntimeException("K-050133");
        }
        //作废支付单
        Trade trade = detail(tid);
        if (trade.getTradeState().getPayState() != PayState.NOT_PAID) {
            throw new SbcRuntimeException("K-050127");
        }
        BaseResponse<PayTradeRecordCountResponse> response = payQueryProvider.getTradeRecordCountByOrderOrParentCode(new
                TradeRecordCountByOrderOrParentCodeRequest(trade.getId(), trade.getParentId()));
        long tradeRecordCount = response.getContext().getCount();
        if (tradeRecordCount > 0) {
            throw new SbcRuntimeException("K-050128");
        }
        payOrderService.deleteByPayOrderId(trade.getPayOrderId());
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(TradeEvent.RE_AUDIT)
                .build();
        tradeFSMService.changeState(stateRequest);
    }

    /**
     * 批量审核
     *
     * @param ids      ids
     * @param audit    审核状态
     * @param reason   驳回原因，用于审核驳回
     * @param operator 审核人信息
     */
    @Transactional
    @GlobalTransactional
    public void batchAudit(String[] ids, AuditState audit, String reason, Operator operator) {
        if (ArrayUtils.isNotEmpty(ids)) {
            Stream.of(ids).forEach(id -> audit(id, audit, reason, operator));
        }
    }


    /**
     * 修改卖家备注
     *
     * @param tid
     * @param sellerRemark
     */
    //@TccTransaction
    @Transactional
    public void remedySellerRemark(String tid, String sellerRemark, Operator operator) {
        //1、查找订单信息
        Trade trade = detail(tid);
        trade.setSellerRemark(sellerRemark);
        trade.appendTradeEventLog(new TradeEventLog(operator, "修改备注", "修改卖家备注", LocalDateTime.now()));
        //保存
        tradeService.updateTrade(trade);
        this.operationLogMq.convertAndSend(operator, "修改备注", "修改卖家备注");
    }


    /**
     * 查询订单
     *
     * @param orderId
     */
    public Trade detail(String orderId) {
        return tradeRepository.findById(orderId).orElse(null);
    }

    /**
     * 查询订单集合
     *
     * @param tids
     */
    public List<Trade> details(List<String> tids) {
        return org.apache.commons.collections4.IteratorUtils.toList(tradeRepository.findAllById(tids).iterator());
    }

    /**
     * 根据父订单号查询订单
     *
     * @param parentTid
     */
    public List<Trade> detailsByParentId(String parentTid) {
        return tradeRepository.findListByParentId(parentTid);
    }

    public List<Trade> detailByOutTradeNo(String outTradeNo) {
        return tradeRepository.findListByOutTradeNo(outTradeNo);
    }

    /**
     * 发货
     *
     * @param tid
     * @param tradeDeliver
     * @param operator
     * @param batchDeliverFlag 是否批量发货
     */
    public String deliver(String tid, TradeDeliver tradeDeliver, Operator operator, BoolFlag batchDeliverFlag) {
        Trade trade = detail(tid);
        //是否开启订单审核
        if (batchDeliverFlag == BoolFlag.NO
                && auditQueryProvider.isSupplierOrderAudit().getContext().isAudit()
                && trade.getTradeState().getAuditState() != AuditState.CHECKED) {
            //只有已审核订单才能发货
            throw new SbcRuntimeException("K-050317");
        }
        // 先款后货并且未支付的情况下禁止发货
        if (trade.getPaymentOrder() == PaymentOrder.PAY_FIRST && trade.getTradeState().getPayState() == PayState.NOT_PAID && trade.getPayInfo().getPayTypeId().equals(0)) {
            throw new SbcRuntimeException("K-050318");
        }
        if (verifyAfterProcessing(tid)) {
            throw new SbcRuntimeException("K-050114", new Object[]{tid});
        }

        // 生成ID
        tradeDeliver.setDeliverId(generatorService.generate("TD"));
        tradeDeliver.setStatus(DeliverStatus.NOT_YET_SHIPPED);
        tradeDeliver.setTradeId(tid);
        tradeDeliver.setProviderName(trade.getSupplier().getSupplierName());

        //快递订阅
        LogisticsLog logisticsLog = new LogisticsLog();
        logisticsLog.setOrderNo(tid);
        logisticsLog.setDeliverId(tradeDeliver.getDeliverId());
        logisticsLog.setStoreId(trade.getSupplier().getStoreId());
        logisticsLog.setCustomerId(trade.getBuyer().getId());
        logisticsLog.setPhone(trade.getConsignee().getPhone());
        logisticsLog.setComOld(tradeDeliver.getLogistics().getLogisticStandardCode());
        logisticsLog.setLogisticNo(tradeDeliver.getLogistics().getLogisticNo());
        logisticsLog.setTo(trade.getConsignee().getDetailAddress());
        if (CollectionUtils.isNotEmpty(trade.getTradeItems())) {
            logisticsLog.setGoodsImg(trade.getTradeItems().get(0).getPic());
            logisticsLog.setGoodsName(trade.getTradeItems().get(0).getSkuName());
        }
        logisticsLogService.add(logisticsLog);

        checkLogisticsNo(tradeDeliver.getLogistics().getLogisticNo(), tradeDeliver.getLogistics()
                .getLogisticStandardCode());

        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .data(tradeDeliver)
                .event(TradeEvent.DELIVER)
                .build();
        tradeFSMService.changeState(stateRequest);

        //发货完成发送通知消息
        Map<String, Object> map = new HashMap<>();
        map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
        map.put("id", tid);
        map.put("node", OrderProcessType.ORDER_SEND_GOODS.toValue());
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
        messageMQRequest.setNodeCode(OrderProcessType.ORDER_SEND_GOODS.getType());
        String skuName = StringUtils.EMPTY;
        String pic = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(tradeDeliver.getShippingItems())) {
            skuName = tradeDeliver.getShippingItems().get(0).getItemName();
            pic = tradeDeliver.getShippingItems().get(0).getPic();
        }
        messageMQRequest.setParams(Lists.newArrayList(skuName));
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(trade.getBuyer().getId());
        messageMQRequest.setPic(pic);
        messageMQRequest.setMobile(trade.getBuyer().getAccount());
        orderProducerService.sendMessage(messageMQRequest);

        return tradeDeliver.getDeliverId();
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param tid
     * @return true|false:存在售后，阻塞订单进程|不存在售后，订单进程正常
     */
    public boolean verifyAfterProcessing(String tid) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(tid);
        if (!CollectionUtils.isEmpty(returnOrders)) {
            // 查询是否存在正在进行中的退单(不是作废,不是拒绝退款,不是已结束)
            Optional<ReturnOrder> optional = returnOrders.stream().filter(item -> item.getReturnFlowState() !=
                    ReturnFlowState.VOID
                    && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                    && item.getReturnFlowState() != ReturnFlowState.COMPLETED).findFirst();
            if (optional.isPresent()) {
                return true;
            }

        }
        return false;
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param ptid
     * @return true|false:存在售后，阻塞订单进程|不存在售后，订单进程正常
     */
    public boolean providerVerifyAfterProcessing(String ptid) {
        List<ReturnOrder> returnOrders = returnOrderRepository.findByPtid(ptid);
        if (!CollectionUtils.isEmpty(returnOrders)) {
            // 查询是否存在正在进行中的退单(不是作废,不是拒绝退款,不是已结束)
            Optional<ReturnOrder> optional = returnOrders.stream().filter(item -> item.getReturnFlowState() !=
                    ReturnFlowState.VOID
                    && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                    && item.getReturnFlowState() != ReturnFlowState.COMPLETED).findFirst();
            if (optional.isPresent()) {
                return true;
            }

        }
        return false;
    }

    /**
     * 确认收货
     *
     * @param tid
     * @param operator
     */
    @GlobalTransactional
    public void confirmReceive(String tid, Operator operator) {
        Trade trade = detail(tid);
        //第三方平台确认收货
        linkedMallTradeService.confirmDisburse(tid, trade.getBuyer().getId());
        TradeEvent event;
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            event = TradeEvent.COMPLETE;
        } else {
            event = TradeEvent.CONFIRM;
        }
        StateRequest stateRequest = StateRequest
                .builder()
                .data(trade)
                .tid(tid)
                .operator(operator)
                .event(event)
                .build();
        tradeFSMService.changeState(stateRequest);
        wxOrderService.syncWxOrderReceive(trade);
        //将物流信息更新为结束
        logisticsLogService.modifyEndFlagByOrderNo(tid);

        // 发送订单完成MQ消息
        if (trade.getTradeState().getPayState() == PayState.PAID) {
            orderProducerService.sendMQForOrderComplete(tid);

            Map<String, Object> map = new HashMap<>();
            map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
            map.put("node", OrderProcessType.ORDER_COMPILE.toValue());
            MessageMQRequest messageMQRequest = new MessageMQRequest();
            messageMQRequest.setNodeCode(OrderProcessType.ORDER_COMPILE.getType());
            messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
            messageMQRequest.setParams(Lists.newArrayList(trade.getTradeItems().get(0).getSkuName()));
            messageMQRequest.setRouteParam(map);
            messageMQRequest.setCustomerId(trade.getBuyer().getId());
            messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
            messageMQRequest.setMobile(trade.getBuyer().getAccount());
            orderProducerService.sendMessage(messageMQRequest);
        }
    }

    /**
     * 退货 | 退款
     *
     * @param tid
     * @param operator
     */
    public void returnOrder(String tid, Operator operator) {
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(TradeEvent.REFUND)
                .build();
        tradeFSMService.changeState(stateRequest);
    }

    /**
     * 作废订单
     *
     * @param tid
     * @param operator
     */
    public void voidTrade(String tid, Operator operator) {
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(TradeEvent.VOID)
                .data("已全部退货或退款")
                .build();
        tradeFSMService.changeState(stateRequest);

        Trade trade = detail(tid);
        // 判断是否是退款订单，并且有分销员id和分销商品
        if (Objects.nonNull(trade.getRefundFlag()) && trade.getRefundFlag()
                && trade.getTradeState().getPayState() == PayState.PAID
                && StringUtils.isNotBlank(trade.getDistributorId())
                && CollectionUtils.isNotEmpty(trade.getDistributeItems())) {
            // trade对象转tradeVO对象
            TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);
            // 订单作废后，发送MQ消息
            orderProducerService.sendMQForOrderRefundVoid(tradeVO);
        }
    }

    public void reverse(String tid, Operator operator, TradeEvent tradeEvent) {
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(tradeEvent)
                .build();
        tradeFSMService.changeState(stateRequest);
    }

    /**
     * 退单作废后的订单状态扭转
     *
     * @param tid
     * @param operator
     */
    public void reverse(String tid, Operator operator, ReturnType returnType) {
        Trade trade = detail(tid);
        if (trade.getTradeState().getFlowState() != FlowState.VOID) {
            return;
        }
        TradeEvent event;
        Object data;
        if (returnType == ReturnType.RETURN) {
            event = TradeEvent.REVERSE_RETURN;
            data = trade;
        } else {
            event = TradeEvent.REVERSE_REFUND;
            data = AuditState.CHECKED;
        }
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(tid)
                .operator(operator)
                .event(event)
                .data(data)
                .build();
        tradeFSMService.changeState(stateRequest);
    }


    /**
     * 查询全部订单
     *
     * @param request
     * @return
     */
    public List<Trade> queryAll(TradeQueryRequest request) {
        return mongoTemplate.find(new Query(request.getWhereCriteria()), Trade.class);
    }


    /**
     * 发货记录作废
     *
     * @param tid
     * @param deliverId
     * @param operator
     */
    public void deliverRecordObsolete(String tid, String deliverId, Operator operator) {
        StateRequest stateRequest = StateRequest.builder()
                .tid(tid)
                .operator(operator)
                .data(deliverId)
                .event(TradeEvent.OBSOLETE_DELIVER)
                .build();
        tradeFSMService.changeState(stateRequest);

    }


    /**
     * 保存发票信息
     *
     * @param tid
     * @param invoice
     */
    @GlobalTransactional
    public void saveInvoice(String tid, Invoice invoice) {
        Trade trade = detail(tid);
        trade.setInvoice(invoice);
        tradeService.updateTrade(trade);
    }


    /**
     * 支付作废
     *
     * @param tid
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void payRecordObsolete(String tid, Operator operator) {
        Trade trade = detail(tid);
        //删除对账记录
        accountRecordProvider.deleteByOrderCodeAndType(
                AccountRecordDeleteByOrderCodeAndTypeRequest.builder().orderCode(trade.getId())
                        .accountRecordType(AccountRecordType.INCOME).build()
        );
        if (trade.getTradeState().getPayState() == PayState.NOT_PAID) {
            throw new SbcRuntimeException("K-050125", new Object[]{"作废支付"});
        }
        trade.getTradePrice().setTotalPayCash(null);
        if (trade.getTradeState().getFlowState() == FlowState.COMPLETED) {
            //已完成订单，扭转流程状态与支付状态
            StateRequest stateRequest = StateRequest.builder()
                    .tid(tid)
                    .operator(operator)
                    .event(TradeEvent.OBSOLETE_PAY)
                    .build();
            tradeFSMService.changeState(stateRequest);
        } else {
            //进行中订单，只扭转付款状态
            trade.getTradeState().setPayState(PayState.NOT_PAID);
            trade.getTradeState().setPayTime(null);
            //添加操作日志
            String detail = String.format("订单[%s]支付记录已作废，当前支付状态[%s],操作人：%s", trade.getId(),
                    trade.getTradeState().getPayState().getDescription(), operator.getName());
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.OBSOLETE_PAY.getDescription())
                    .eventDetail(detail)
                    .eventTime(LocalDateTime.now())
                    .build());
            tradeService.updateTrade(trade);
            this.operationLogMq.convertAndSend(operator, TradeEvent.OBSOLETE_PAY.getDescription(), detail);
        }
    }

    /**
     * 线上订单支付回调
     *
     * @param trade
     * @param payOrderOld
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void payCallBackOnline(Trade trade, PayOrder payOrderOld, Operator operator) {
        try {
            if (payOrderOld.getReceivable() == null) {
                BaseResponse<PayTradeRecordResponse> response;
                String payOrderId = trade.getPayOrderId();
                if (StringUtils.isNotEmpty(trade.getTailOrderNo()) && StringUtils.isNotEmpty(trade.getTailPayOrderId())) {
                    response = payQueryProvider.getTradeRecordByOrderCode(new
                            TradeRecordByOrderCodeRequest(trade.getTailOrderNo()));
                    payOrderId = trade.getTailPayOrderId();
                } else {
                    if (trade.getPayInfo().isMergePay()) {
                        response = payQueryProvider.getTradeRecordByOrderCode(new
                                TradeRecordByOrderCodeRequest(trade.getParentId()));
                    } else {
                        response = payQueryProvider.getTradeRecordByOrderCode(new
                                TradeRecordByOrderCodeRequest(trade.getId()));
                    }
                }
                PayChannelItemResponse chanelItemResponse = payQueryProvider.getChannelItemById(new
                        ChannelItemByIdRequest(response.getContext().getChannelItemId())).getContext();
                ReceivableAddRequest param = new ReceivableAddRequest(payOrderId,
                        DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1)
                        , trade.getSellerRemark(), 0L, chanelItemResponse.getName(), chanelItemResponse.getId(), null);
                addReceivable(param, operator.getPlatform()).ifPresent(payOrder ->
                        //订单状态变更
                        payCallBack(trade.getId(), payOrder.getPayOrderPrice(), operator, PayWay.valueOf
                                (chanelItemResponse
                                        .getChannel().toUpperCase()))
                );

            }
        } catch (SbcRuntimeException e) {
            logger.error("The {} order status modifies the exception.error={}", trade.getId(), e);
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        }
    }

    @GlobalTransactional
    @Transactional
    public void payCallBackOnlineBatch(List<PayCallBackOnlineBatch> request, Operator operator) {
        request.forEach(i -> payCallBackOnline(i.getTrade(), i.getPayOrderOld(), operator));

        //判断是否合并支付
        String businessId = request.get(0).getTrade().getId();
        if (request.size() > 1) {
            businessId = request.get(0).getTrade().getParentId();
        }
        orderProducerService.sendMQForThirdPlatformSync(businessId);
    }


    /**
     * 获取支付单
     *
     * @param payOrderId
     * @return
     */
    public PayOrder findPayOrder(String payOrderId) {
        return payOrderRepository.findById(payOrderId).orElse(null);
    }

    /**
     * 订单支付回调
     *
     * @param tid
     * @param payOrderPrice
     * @param operator
     */
    @Transactional
    @GlobalTransactional
    public void payCallBack(String tid, BigDecimal payOrderPrice, Operator operator, PayWay payWay) {
        Trade trade = detail(tid);
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal shouldPayPrice = tradePrice.getTotalPrice();
        //表示付定金的时候 获取的应该支付的金额
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                && StringUtils.isEmpty(trade.getTailOrderNo())) {
            shouldPayPrice = tradePrice.getEarnestPrice();
        }

        //表示有尾款订单号，则表示付尾款的时候 要支付的金额
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
            shouldPayPrice = tradePrice.getTailPrice();
        }
        if (payOrderPrice.compareTo(shouldPayPrice) != 0) {
            throw new SbcRuntimeException("K-050101", new Object[]{tid, shouldPayPrice, payOrderPrice});
        }

        trade.getTradePrice().setTotalPayCash(payOrderPrice);
        String eventStr = trade.getTradeState().getPayState() == PayState.UNCONFIRMED ? "确认支付" : "支付";
        if (osUtil.isS2b()) {
            trade.getTradeState().setPayState(operator.getPlatform() == Platform.PLATFORM ? PayState.PAID : PayState
                    .UNCONFIRMED);
        } else {
            trade.getTradeState().setPayState(operator.getPlatform() == Platform.BOSS ? PayState.PAID : PayState
                    .UNCONFIRMED);
        }

        if (PayType.fromValue(Integer.parseInt(trade.getPayInfo().getPayTypeId())) == PayType.ONLINE || PayType.fromValue(Integer.parseInt(trade.getPayInfo().getPayTypeId())) == PayType.INNER_SETTLE) {
            // 如果是拼团订单
            if (Objects.nonNull(trade.getGrouponFlag()) && trade.getGrouponFlag()) {
                // 拼团订单支付处理，拼团成功更新子单
                trade = grouponOrderService.handleGrouponOrderPaySuccess(trade);
            }
            // TODO duanlsh  设置支付状态
            trade.getTradeState().setPayState(PayState.PAID);
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && StringUtils.isEmpty(trade.getTailOrderNo()) &&
                    Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY) {
                trade.getTradeState().setPayState(PayState.PAID_EARNEST);
            }

            // TODO duanlsh  设置第三方支付
            trade.setPayWay(payWay);
            operator.setPlatform(Platform.CUSTOMER);
            operator.setName(trade.getBuyer().getName());
            operator.setAccount(trade.getBuyer().getAccount());
            operator.setUserId(trade.getBuyer().getId());
        }

        if (trade.getTradeState().getPayState() == PayState.PAID || trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
            String orderNo;
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                    && Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY
                    && StringUtils.isNotEmpty(trade.getTailOrderNo())) {
                orderNo = trade.getTailOrderNo();
            } else {
                orderNo = trade.getId();
            }
            PayOrderResponse payOrder = payOrderService.findPayOrder(orderNo);
            //线下付款订单付款时间取实际扭转为已付款的时间
            trade.getTradeState().setPayTime(LocalDateTime.now());
            // 查询交易流水号
            BaseResponse<PayTradeRecordResponse> payTradeRecord;
            if (StringUtils.isNotEmpty(trade.getTailOrderNo())) {
                payTradeRecord =
                        payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(trade.getTailOrderNo()));
            } else {
                if (trade.getPayInfo().isMergePay()) {
                    payTradeRecord =
                            payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(trade.getParentId()));
                } else {
                    payTradeRecord =
                            payQueryProvider.getTradeRecordByOrderCode(new TradeRecordByOrderCodeRequest(trade.getId()));
                }
            }

            String tradeNo = Objects.isNull(payTradeRecord.getContext()) ? null :
                    payTradeRecord.getContext().getTradeNo();
            //已支付，添加对账记录
            AccountRecordAddRequest record = AccountRecordAddRequest.builder()
                    .amount(payOrderPrice)
                    .customerId(trade.getBuyer().getId())
                    .customerName(trade.getBuyer().getName())
                    .orderCode(orderNo)
                    .tradeNo(tradeNo)
                    .orderTime(trade.getTradeState().getCreateTime())
                    .payWay(payWay)
                    .storeId(trade.getSupplier().getStoreId())
                    .supplierId(trade.getSupplier().getSupplierId())
                    .tradeTime(payOrder!=null && payOrder.getReceiveTime()!=null?payOrder.getReceiveTime():LocalDateTime.now())
                    .type((byte) 0)
                    .build();
            accountRecordProvider.add(record);

            //已支付或者，添加订单开票
            //删除存在的开票信息
            orderInvoiceService.deleteOrderInvoiceByOrderNo(trade.getId());
            //订单开票
            createOrderInvoice(trade, operator);

            if (StringUtils.isNotBlank(trade.getDeductCode())){

                //调用樊登积分扣除
                BaseResponse<FanDengConsumeResponse> fanDengConsumeResponseBaseResponse = new BaseResponse<>();
                ExceptionOfTradePoints exceptionOfTradePoints = exceptionOfTradePointsService.getByTradeId(trade.getId());
                try {
                    if (trade.getTradePrice().getPoints() != null && trade.getTradePrice().getPoints() > 0) {
                        fanDengConsumeResponseBaseResponse = externalProvider.pointDeduct(FanDengPointDeductRequest.builder()
                                .deductCode(trade.getDeductCode()).build());
                        if (Objects.nonNull(exceptionOfTradePoints)) {
                            //处理成功更新状态
                            exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
                            exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
                            exceptionOfTradePoints.setHandleStatus(HandleStatus.SUCCESSFULLY_PROCESSED);
                        }
                    }
                    //调用樊登知豆扣除
                    if (trade.getTradePrice().getKnowledge() != null && trade.getTradePrice().getKnowledge() > 0) {
                        fanDengConsumeResponseBaseResponse = externalProvider.knowledgeDeduct(FanDengPointDeductRequest.builder()
                                .deductCode(trade.getDeductCode()).build());
                        if (Objects.nonNull(exceptionOfTradePoints)) {
                            //处理成功更新状态
                            exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
                            exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
                            exceptionOfTradePoints.setHandleStatus(HandleStatus.SUCCESSFULLY_PROCESSED);
                        }
                    }
                } catch (Exception e) {
                    // 保存积分订单抵扣异常信息
                    if (Objects.nonNull(exceptionOfTradePoints)) {
                        //存在则更新，更新错误信息及处理状态
                        exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
                        exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
                        exceptionOfTradePoints.setHandleStatus(HandleStatus.PROCESSING_FAILED);
                        exceptionOfTradePoints.setErrorTime(exceptionOfTradePoints.getErrorTime() + 1);
                    } else {
                        //新增
                        exceptionOfTradePoints = new ExceptionOfTradePoints();
                        exceptionOfTradePoints.setTradeId(trade.getId());
                        if (trade.getTradePrice().getPoints() != null && trade.getTradePrice().getPoints() > 0) {
                            exceptionOfTradePoints.setPoints(trade.getTradePrice().getPoints());
                            exceptionOfTradePoints.setType(1);
                        }
                        if (trade.getTradePrice().getKnowledge() != null && trade.getTradePrice().getKnowledge() > 0) {
                            exceptionOfTradePoints.setPoints(trade.getTradePrice().getKnowledge());
                            exceptionOfTradePoints.setType(2);
                        }
                        exceptionOfTradePoints.setErrorCode(fanDengConsumeResponseBaseResponse.getCode());
                        exceptionOfTradePoints.setErrorDesc(fanDengConsumeResponseBaseResponse.getMessage());
                        exceptionOfTradePoints.setDeductCode(trade.getDeductCode());
                        exceptionOfTradePoints.setHandleStatus(HandleStatus.PENDING);
                        exceptionOfTradePoints.setErrorTime(Constants.yes);
                        exceptionOfTradePoints.setDelFlag(DeleteFlag.NO);
                        exceptionOfTradePoints.setCreateTime(LocalDateTime.now());
                    }
                } finally {
                    //发送积分订单异常消息
                    if (Objects.nonNull(exceptionOfTradePoints)) {
                        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_MODIFY_OR_ADD_TRADE_POINTS_EXCEPTION)
                                .send(new GenericMessage<>(JSONObject.toJSONString(exceptionOfTradePoints)));
                    }
                }
            }
        }
        if (trade.getTradeState().getFlowState() == FlowState.CONFIRMED && trade.getTradeState().getPayState() == PayState.PAID) {
            // 订单支付后，发送MQ消息
            //this.sendMQForOrderPayed(trade);
            //已支付并已收货，结束订单流程
            StateRequest stateRequest = StateRequest.builder()
                    .tid(tid)
                    .operator(operator)
                    .event(TradeEvent.COMPLETE)
                    .data(trade)
                    .build();
            tradeFSMService.changeState(stateRequest);
            // 订单完成后，发送MQ消息
            this.sendMQForOrderPayedAndComplete(trade);

        }
        else if (trade.getTradeState().getFlowState() != FlowState.CONFIRMED && (trade.getTradeState().getPayState() ==
                PayState.PAID || trade.getTradeState().getPayState() == PayState.PAID_EARNEST)) {
            if (trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_EARNEST) {
                trade.getTradeState().setFlowState(FlowState.WAIT_PAY_TAIL);
                // 设置尾款订单超时时间
                trade.setOrderTimeOut(trade.getTradeState().getTailEndTime());
                orderProducerService.cancelOrder(trade.getId(), Duration.between(LocalDateTime.now(),
                        trade.getTradeState().getTailEndTime()).toMillis());
            }

            //添加操作日志
            String detail = String.format("订单[%s]已%s,操作人：%s", trade.getId(), eventStr, operator.getName());
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.PAY.getDescription())
                    .eventTime(LocalDateTime.now())
                    .eventDetail(detail)
                    .build());
            // 卡券流程状态处理
            tradeService.virtualStatusHandle(trade, operator);

            // 卡券发放
            try {
                // 预售支付不需要发放
                if(trade.getTradeState().getFlowState() != FlowState.WAIT_PAY_TAIL){
                    virtualCouponIssue(trade);
                }
            } catch (Exception e) {
                log.error("卡券短信发放失败 ", e);
            }
            // 订单支付后，发送MQ消息
            this.sendMQForOrderPayed(trade);
            this.operationLogMq.convertAndSend(operator, TradeEvent.PAY.getDescription(), detail);
        }
        else {
            //添加操作日志
            String detail = String.format("订单[%s]已%s,操作人：%s", trade.getId(), eventStr, operator.getName());
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(TradeEvent.PAY.getDescription())
                    .eventTime(LocalDateTime.now())
                    .eventDetail(detail)
                    .build());
            tradeService.updateTrade(trade);
            this.operationLogMq.convertAndSend(operator, TradeEvent.PAY.getDescription(), detail);
        }
        //拼团成功
        if (Objects.nonNull(trade.getGrouponFlag()) && trade.getGrouponFlag() &&
                Objects.nonNull(trade.getTradeGroupon()) && GrouponOrderStatus.COMPLETE == trade.getTradeGroupon().getGrouponOrderStatus()) {
            StateRequest stateRequest = StateRequest
                    .builder()
                    .tid(trade.getId())
                    .operator(operator)
                    .event(TradeEvent.JOIN_GROUPON)
                    .build();
            tradeFSMService.changeState(stateRequest);
        }
        //拼团失败
        if (Objects.nonNull(trade.getGrouponFlag()) && trade.getGrouponFlag() &&
                Objects.nonNull(trade.getTradeGroupon()) && GrouponOrderStatus.FAIL == trade.getTradeGroupon().getGrouponOrderStatus()) {
            //自动退款流程
            orderProducerService.sendGrouponOrderAutoRefund(trade);
        }
        //同步子订单
        this.updateProviderTrade(trade);
        // 更新预约/预售购买数量
        changeActivityBuyCount(trade);
        // 判断订单是否是定金销售
        if (trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL){
            log.info("wx-or-aliPay支付回调处理======>订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
        }else {
            log.info("wx-or-aliPay支付回调处理======>推送至erp,订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
            //推送ERP订单
            this.pushTradeToErp(trade.getId());
        }
        //支付成功发放优惠券
        orderCouponService.addCouponRecord(trade);
    }

    /**
     * 处理卡券发放
     *
     * @param trade
     */
    private void virtualCouponIssue(Trade trade) {
        BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
        // 短信发放
        Stream.concat(trade.getTradeItems().stream(), trade.getGifts().stream())
                .flatMap(tradeItem -> tradeItem.getVirtualCoupons().stream())
                .forEach(virtualCoupon -> {
                    String phone = trade.getBuyer().getPhone();
                    Map<String, Object> params4Message = new HashMap<>();
                    params4Message.put("customerId", trade.getBuyer().getId());
                    params4Message.put("routeParam", null);
                    params4Message.put("nodeType", 2);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", 2);
                    map.put("node", 0);
                    map.put("id", trade.getId());


                    if (Objects.nonNull(virtualCoupon.getProvideType())
                            && virtualCoupon.getProvideType() == ProvideType.REDEMPTION_CODE.toValue()) {
                        smsSendUtil.send(OrderSmsTemplate.VIRTUAL_COUPON_CODE, new String[]{phone}, virtualCoupon.getCouponNo());
                        params4Message.put("params", Arrays.asList(
                                virtualCoupon.getCouponNo(),
                                config.getMobileWebsite() + OrderSmsTemplate.VIRTUAL_COUPON_CODE + "/" + trade.getId()
                        ));
                        params4Message.put("nodeCode", OrderSmsTemplate.VIRTUAL_COUPON_CODE.name());

                    } else if (Objects.nonNull(virtualCoupon.getProvideType())
                            && virtualCoupon.getProvideType() == ProvideType.CODE_KEY.toValue()) {
                        smsSendUtil.send(OrderSmsTemplate.VIRTUAL_COUPON_CODE_PASSWORD
                                , new String[]{phone}, virtualCoupon.getCouponNo(), virtualCoupon.getCouponSecret());
                        params4Message.put("params", Arrays.asList(
                                virtualCoupon.getCouponNo(), virtualCoupon.getCouponSecret(),
                                config.getMobileWebsite() + OrderSmsTemplate.VIRTUAL_COUPON_CODE_PASSWORD + "/" + trade.getId()
                        ));
                        params4Message.put("nodeCode", OrderSmsTemplate.VIRTUAL_COUPON_CODE_PASSWORD.name());
                    } else {
                        params4Message.put("params", Arrays.asList(
                                virtualCoupon.getCouponNo(),
                                config.getMobileWebsite() + OrderSmsTemplate.VIRTUAL_COUPON_CODE_LINK + "/" + trade.getId()
                        ));
                        params4Message.put("nodeCode", OrderSmsTemplate.VIRTUAL_COUPON_CODE_LINK.name());
                    }
                    params4Message.put("routeParam", map);
                    resolver.resolveDestination(MQConstant.PAID_CARD_MESSAGE_Q_NAME).send(new GenericMessage<>(JSONObject.toJSONString(params4Message)));
                });
    }

    /**
     * 处理卡券订单状态
     *
     * @param trade
     * @param operator
     */
    public void virtualStatusHandle(Trade trade, Operator operator) {
        if (Objects.isNull(trade) || Objects.isNull(operator)) {
            return;
        }

        Long couponCount = Stream.concat(trade.getTradeItems().stream(), trade.getGifts().stream())
                .filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                        && (GoodsType.VIRTUAL_COUPON == tradeItem.getGoodsType()
                        || GoodsType.VIRTUAL_GOODS == tradeItem.getGoodsType())).count();

        // 预售 不处理虚拟商品
        if ( trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL) {
            tradeService.updateTrade(trade);
            return;
        }
        // 没有虚拟商品  无需处理
        if (couponCount == 0 ) {
            tradeService.updateTrade(trade);
            return;
        }

        trade.getTradeState().setDeliverTime(LocalDateTime.now());
        int itemSize = trade.getTradeItems().size() + trade.getGifts().size();

        //查询子单
        List<ProviderTrade> providerTrades = providerTradeService.findListByParentId(trade.getId());

        if (couponCount == itemSize) {


            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(FlowState.DELIVERED.getDescription())
                    .eventDetail(String.format("订单[%s],虚拟商品已自动全部发货", trade.getId()))
                    .eventTime(LocalDateTime.now())
                    .build());
            TradeDeliver tradeDeliver = tradeDeliverGenerate(trade, true);
            tradeDeliver.setStatus(DeliverStatus.SHIPPED);
            trade.addTradeDeliver(tradeDeliver);
            trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
            tradeService.updateTrade(trade);

            //修改子单发货信息
            this.updateProviderTradeList(providerTrades,tradeDeliver,operator,true);
            StateRequest stateRequest = StateRequest.builder()
                    .tid(trade.getId())
                    .operator(operator)
                    .event(TradeEvent.COMPLETE)
                    .data(trade)
                    .build();
            tradeFSMService.changeState(stateRequest);

        } else {
            trade.appendTradeEventLog(TradeEventLog
                    .builder()
                    .operator(operator)
                    .eventType(FlowState.DELIVERED_PART.getDescription())
                    .eventDetail(String.format("订单[%s],虚拟商品已自动全部发货", trade.getId()))
                    .eventTime(LocalDateTime.now())
                    .build());
            TradeDeliver tradeDeliver = tradeDeliverGenerate(trade, false);
            // 定制逻辑 发货记录 发货完成
            tradeDeliver.setStatus(DeliverStatus.SHIPPED);
            trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
            // trade.addTradeDeliver(tradeDeliver);
            tradeService.updateTrade(trade);

            //修改子单发货信息
            this.updateProviderTradeList(providerTrades,tradeDeliver,operator,false);

            StateRequest stateRequest = StateRequest.builder()
                    .tid(trade.getId())
                    .operator(operator)
                    .data(tradeDeliver)
                    .event(TradeEvent.DELIVER)
                    .build();
            tradeFSMService.changeState(stateRequest);

        }
    }

    /**
     * 子单生成发货记录和状态
     * <p> 此方法逻辑适用于 樊登定制分支
     *
     * @param providerTradeList 子单列表
     */
    public void updateProviderTradeList(List<ProviderTrade> providerTradeList,TradeDeliver tradeDeliver,Operator operator,boolean isAllVirtual) {
        if (CollectionUtils.isEmpty(providerTradeList)) {
            return;
        }
        for (ProviderTrade trade : providerTradeList) {

            Long couponCount = Stream.concat(trade.getTradeItems().stream(), trade.getGifts().stream())
                    .filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                            && (GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType())
                            || GoodsType.VIRTUAL_GOODS.equals(tradeItem.getGoodsType()))).count();
            if (couponCount == 0) {
                continue;
            }
            trade.getTradeState().setDeliverTime(LocalDateTime.now());
            int itemSize = trade.getTradeItems().size() + trade.getGifts().size();
            if (couponCount == itemSize) {
                trade.appendTradeEventLog(TradeEventLog
                        .builder()
                        .operator(operator)
                        .eventType(FlowState.DELIVERED.getDescription())
                        .eventDetail(String.format("订单[%s],虚拟商品已自动全部发货", trade.getId()))
                        .eventTime(LocalDateTime.now())
                        .build());
                tradeDeliver.setStatus(DeliverStatus.SHIPPED);
                trade.addTradeDeliver(tradeDeliver);
                trade.getTradeState().setDeliverStatus(DeliverStatus.SHIPPED);
                trade.getTradeState().setFlowState(FlowState.DELIVERED);

                //修改商品的发货状态和发货数量
                trade.getTradeItems().forEach(tradeItem -> {
                    tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                    tradeItem.setDeliveredNum(tradeItem.getNum());
                });

                //修改赠品的发货状态和发货数量
                trade.getGifts().forEach(tradeItem -> {
                    tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                    tradeItem.setDeliveredNum(tradeItem.getNum());
                });

            } else {
                trade.appendTradeEventLog(TradeEventLog
                        .builder()
                        .operator(operator)
                        .eventType(FlowState.DELIVERED_PART.getDescription())
                        .eventDetail(String.format("订单[%s],虚拟商品已自动全部发货", trade.getId()))
                        .eventTime(LocalDateTime.now())
                        .build());
                tradeDeliver.setStatus(DeliverStatus.SHIPPED);
                trade.getTradeState().setDeliverStatus(DeliverStatus.PART_SHIPPED);
                trade.getTradeState().setFlowState(FlowState.DELIVERED_PART);
                trade.addTradeDeliver(tradeDeliver);
                //修改商品的发货状态和发货数量
                trade.getTradeItems().forEach(tradeItem -> {
                    if (tradeItem.getGoodsType() == GoodsType.VIRTUAL_COUPON || tradeItem.getGoodsType() == GoodsType.VIRTUAL_GOODS) {
                        tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                        tradeItem.setDeliveredNum(tradeItem.getNum());
                    }
                });

                //修改赠品的发货状态和发货数量
                trade.getGifts().forEach(tradeItem -> {
                    if (tradeItem.getGoodsType() == GoodsType.VIRTUAL_COUPON || tradeItem.getGoodsType() == GoodsType.VIRTUAL_GOODS) {
                        tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                        tradeItem.setDeliveredNum(tradeItem.getNum());
                    }
                });
            }

            providerTradeService.updateProviderTrade(trade);

        }
    }

    /**
     * 生成发货记录
     *
     * @param trade
     * @param
     * @return
     */
    private TradeDeliver tradeDeliverGenerate(Trade trade, boolean isAllVirtual) {
        // 生成ID
        TradeDeliver tradeDeliver = new TradeDeliver();
        tradeDeliver.setDeliverId(generatorService.generate("TD"));

        tradeDeliver.setTradeId(trade.getId());
        tradeDeliver.setConsignee(trade.getConsignee());
        tradeDeliver.setDeliverTime(LocalDateTime.now());
        tradeDeliver.setProviderName(trade.getSupplier().getSupplierName());
        tradeDeliver.setIsVirtualCoupon(Boolean.TRUE);
        List<ShippingItem> shippingItems = trade.getTradeItems().stream()
                .filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                        && (GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType()) ||
                        GoodsType.VIRTUAL_GOODS.equals(tradeItem.getGoodsType())))
                .map(tradeItem -> {
                    if (isAllVirtual) {
                        tradeItem.setDeliveredNum(tradeItem.getNum());
                    }
                    tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                    return ShippingItem.builder()
                            .itemNum(tradeItem.getNum())
                            .itemName(tradeItem.getSkuName())
                            .skuId(tradeItem.getSkuId())
                            .skuNo(tradeItem.getSkuNo())
                            .spuId(tradeItem.getSpuId())
                            .unit(tradeItem.getUnit())
                            .pic(tradeItem.getPic())
                            .specDetails(tradeItem.getSpecDetails())
                            .build();

                }).collect(Collectors.toList());
        tradeDeliver.setShippingItems(shippingItems);
        shippingItems = trade.getGifts().stream()
                .filter(tradeItem -> Objects.nonNull(tradeItem.getGoodsType())
                        && (GoodsType.VIRTUAL_COUPON.equals(tradeItem.getGoodsType()) ||
                        GoodsType.VIRTUAL_GOODS.equals(tradeItem.getGoodsType())))
                .map(tradeItem -> {
                    if (isAllVirtual) {
                        tradeItem.setDeliveredNum(tradeItem.getNum());
                    }
                    tradeItem.setDeliverStatus(DeliverStatus.SHIPPED);
                    return ShippingItem.builder()
                            .itemNum(tradeItem.getNum())
                            .itemName(tradeItem.getSkuName())
                            .skuId(tradeItem.getSkuId())
                            .skuNo(tradeItem.getSkuNo())
                            .spuId(tradeItem.getSpuId())
                            .unit(tradeItem.getUnit())
                            .specDetails(tradeItem.getSpecDetails())
                            .pic(tradeItem.getPic())
                            .build();

                }).collect(Collectors.toList());
        tradeDeliver.setGiftItemList(shippingItems);
        return tradeDeliver;
    }


    /**
     * 更新预约/预售购买数量
     *
     * @param trade
     */
    @Transactional
    @GlobalTransactional
    public void changeActivityBuyCount(Trade trade) {
        trade.getTradeItems().forEach(tradeItemVO -> {
            if (Objects.nonNull(tradeItemVO.getIsAppointmentSaleGoods()) && tradeItemVO.getIsAppointmentSaleGoods() && trade.getTradeState().getPayState() == PayState.PAID) {
                // appointmentBuyCount:customerId:appointmentSaleId:skuNo
                String appointmentBuyCountKey =
                        "appointmentBuyCount:" + trade.getBuyer().getId() + ":" + tradeItemVO.getAppointmentSaleId() + ":" + tradeItemVO.getSkuNo();
                if (!redisService.hasKey(appointmentBuyCountKey)) {
                    // 预约购买去重，同一个账户同一活动同一个商品只记一次
                    appointmentSaleGoodsProvider.updateBuyCount(AppointmentSaleGoodsCountRequest.builder()
                            .appointmentSaleId(tradeItemVO.getAppointmentSaleId()).stock(1L).goodsInfoId(tradeItemVO.getSkuId()).build());
                    AppointmentSaleVO appointmentSaleVO = appointmentSaleQueryProvider.getById(
                            (AppointmentSaleByIdRequest.builder().id(tradeItemVO.getAppointmentSaleId()).storeId(tradeItemVO.getStoreId()).build())).getContext().getAppointmentSaleVO();
                    ConfigVO timeoutCancelConfig =
                            tradeCacheService.getTradeConfigByType(ConfigType.ORDER_SETTING_TIMEOUT_CANCEL);
                    //TODO 此处没有使用到，没有修改掉 duanlsh
                    Integer hours =
                            Integer.valueOf(JSON.parseObject(timeoutCancelConfig.getContext()).get("hour").toString()) + 1;
                    long seconds =
                            Duration.between(LocalDateTime.now(), appointmentSaleVO.getSnapUpEndTime()).toMillis() / 1000 + (hours * 60 * 60 * 1000L);
                    redisService.setString(appointmentBuyCountKey, "1", seconds);
                }
            }
        });
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && Objects.isNull(trade.getBookingType())) {
            TradeItem tradeItem = trade.getTradeItems().get(0);
            BookingSaleByIdRequest bookingSaleByIdRequest = new BookingSaleByIdRequest();
            bookingSaleByIdRequest.setId(tradeItem.getBookingSaleId());
            BookingSaleByIdResponse bookingSaleResponse =
                    bookingSaleQueryProvider.getById(bookingSaleByIdRequest).getContext();
            if (Objects.nonNull(bookingSaleResponse) && Objects.nonNull(bookingSaleResponse.getBookingSaleVO())) {
                trade.setBookingType(BookingType.fromValue(bookingSaleResponse.getBookingSaleVO().getBookingType()));
            }
        }
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.FULL_MONEY) {
            if (trade.getTradeState().getPayState() == PayState.PAID) {
                TradeItem tradeItem = trade.getTradeItems().get(0);
                bookingSaleGoodsProvider.addBookingPayCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                        bookingSaleId(tradeItem.getBookingSaleId()).stock(tradeItem.getNum()).build());
            }
        }
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY) {
            TradeItem tradeItem = trade.getTradeItems().get(0);
            if (trade.getTradeState().getPayState() == PayState.PAID_EARNEST) {
                bookingSaleGoodsProvider.addBookinghandSelCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                        bookingSaleId(tradeItem.getBookingSaleId()).stock(tradeItem.getNum()).build());
            }
            if (trade.getTradeState().getPayState() == PayState.PAID) {
                bookingSaleGoodsProvider.addBookingTailCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                        bookingSaleId(tradeItem.getBookingSaleId()).stock(tradeItem.getNum()).build());
            }

        }
    }

    private void updateProviderTrade(Trade trade) {
        String parentId = trade.getId();
        List<ProviderTrade> tradeList = providerTradeService.findListByParentId(parentId);
//            List<ProviderTrade> tradeList222 = providerTradeService.findListByParentIdList(Arrays.asList(parentId));
        if (CollectionUtils.isNotEmpty(tradeList)) {
            tradeList.forEach(childTradeVO -> {
                childTradeVO.getTradeState().setPayState(trade.getTradeState().getPayState());
                childTradeVO.getTradeState().setFlowState(trade.getTradeState().getFlowState());
                //子订单拼团信息更新
                if (Objects.nonNull(trade.getTradeGroupon()) && Objects.equals(GrouponOrderStatus.COMPLETE,
                        trade.getTradeGroupon().getGrouponOrderStatus())) {
                    childTradeVO.setTradeGroupon(trade.getTradeGroupon());
                    childTradeVO.getTradeState().setFlowState(FlowState.AUDIT);
                    childTradeVO.getTradeGroupon().setGrouponOrderStatus(GrouponOrderStatus.COMPLETE);
                    childTradeVO.getTradeGroupon().setGrouponSuccessTime(LocalDateTime.now());
                }
                TradeUpdateRequest tradeUpdateRequest = new TradeUpdateRequest(KsBeanUtil.convert(childTradeVO,
                        TradeUpdateDTO.class));
                providerTradeService.updateProviderTrade(tradeUpdateRequest);
            });
        }
    }


    /**
     * 发送订单支付、订单完成MQ消息
     *
     * @param trade
     */
    private void sendMQForOrderPayedAndComplete(Trade trade) {
        TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);
        orderProducerService.sendMQForOrderPayedAndComplete(tradeVO);
    }

    /**
     * 订单支付后，发送MQ消息
     *
     * @param trade
     */
    private void sendMQForOrderPayed(Trade trade) {
        // trade对象转tradeVO对象
        TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);
        orderProducerService.sendMQForOrderPayed(tradeVO);

        String customerId = trade.getBuyer().getId();
        String pic = trade.getTradeItems().get(0).getPic();
        String account = trade.getBuyer().getAccount();

        Map<String, Object> map = new HashMap<>();
        map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
        map.put("id", trade.getId());
        map.put("node", OrderProcessType.ORDER_PAY_SUCCESS.toValue());
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
        messageMQRequest.setNodeCode(OrderProcessType.ORDER_PAY_SUCCESS.getType());
        messageMQRequest.setParams(Lists.newArrayList(trade.getTradeItems().get(0).getSkuName()));
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setPic(pic);
        messageMQRequest.setMobile(account);
        orderProducerService.sendMessage(messageMQRequest);

        //推广订单节点触发
        if (trade.getDistributorId() != null) {
            map.put("type", NodeType.DISTRIBUTION.toValue());
            map.put("node", DistributionType.PROMOTE_ORDER_PAY_SUCCESS.toValue());
            List<String> params = Lists.newArrayList(trade.getDistributorName(),
                    trade.getTradeItems().get(0).getSkuName(),
                    trade.getCommission().toString());
            this.sendMessage(NodeType.DISTRIBUTION, DistributionType.PROMOTE_ORDER_PAY_SUCCESS, params,
                    map, trade.getInviteeId(), pic, account);
        }

    }

    /**
     * 0 元订单默认支付
     *
     * @param trade
     * @param payWay
     *
     */
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public boolean tradeDefaultPay(Trade trade, PayWay payWay) {
        String tid = trade.getId();
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY) {
            PayOrder payOrder;
            if (StringUtils.isBlank(trade.getTailOrderNo())) {
                if (Objects.isNull(trade.getTradePrice().getEarnestPrice()) || trade.getTradePrice().getEarnestPrice().compareTo
                        (BigDecimal.ZERO) != 0) {
                    throw new SbcRuntimeException("K-050407");
                }
                payOrder = payOrderService.findPayOrderByOrderCode(tid).orElse(null);
            } else {
                if (Objects.isNull(trade.getTradePrice().getTailPrice()) || trade.getTradePrice().getTailPrice().compareTo
                        (BigDecimal.ZERO) != 0) {
                    throw new SbcRuntimeException("K-050407");
                }
                payOrder = payOrderService.findPayOrderByOrderCode(trade.getTailOrderNo()).orElse(null);
            }
            ReceivableAddRequest receivableAddRequest;
            if (Objects.nonNull(payOrder) && payOrder.getPayType() == PayType.OFFLINE) {
                receivableAddRequest = ReceivableAddRequest.builder().accountId(Constants.DEFAULT_RECEIVABLE_ACCOUNT)
                        .createTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1))
                        .payOrderId(payOrder.getPayOrderId()).build();
            } else {
                receivableAddRequest = ReceivableAddRequest.builder().payChannelId(Constants
                        .DEFAULT_RECEIVABLE_ACCOUNT).payChannel("默认支付").createTime(DateUtil.format(LocalDateTime.now(),
                        DateUtil.FMT_TIME_1))
                        .payOrderId(payOrder.getPayOrderId()).build();
            }
            this.addReceivable(receivableAddRequest, Platform.PLATFORM).ifPresent(pay ->
                    this.payCallBack(tid, BigDecimal.ZERO,
                            Operator.builder().adminId("0").name("system").account("system").platform
                                    (Platform.PLATFORM).build(), payWay)
            );
        } else {
            if (Objects.isNull(trade.getTradePrice().getTotalPrice()) || trade.getTradePrice().getTotalPrice().compareTo
                    (BigDecimal.ZERO) != 0) {
                throw new SbcRuntimeException("K-050407");
            }
            PayOrder payOrder = payOrderService.findPayOrderByOrderCode(tid).orElse(null);
            ReceivableAddRequest receivableAddRequest = null;
            if (Objects.nonNull(payOrder) && payOrder.getPayType() == PayType.OFFLINE) {
                receivableAddRequest = ReceivableAddRequest.builder().accountId(Constants.DEFAULT_RECEIVABLE_ACCOUNT)
                        .createTime(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_1))
                        .payOrderId(trade.getPayOrderId()).build();
            } else {
                receivableAddRequest = ReceivableAddRequest.builder().payChannelId(Constants
                        .DEFAULT_RECEIVABLE_ACCOUNT).payChannel("默认支付").createTime(DateUtil.format(LocalDateTime.now(),
                        DateUtil.FMT_TIME_1))
                        .payOrderId(trade.getPayOrderId()).build();
            }
            this.addReceivable(receivableAddRequest, Platform.PLATFORM).ifPresent(pay ->
                    this.payCallBack(tid, BigDecimal.ZERO,
                            Operator.builder().adminId("0").name("system").account("system").platform
                                    (Platform.PLATFORM).build(), payWay)
            );

            //同步第三方订单
            if (ThirdPlatformType.LINKED_MALL.equals(trade.getThirdPlatformType())) {
                orderProducerService.sendMQForThirdPlatformSync(trade.getId());
            }
        }
        if (trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL){
            log.info("定金预售:0元定单======>订单号:{},订单类型:{},订单状态：{}",trade.getId(),trade.getBookingType(),trade.getTradeState().getFlowState());
        }else {
            log.info("推送0元订单到ERP系统,订单号:{}",tid);
            //推送订单到ERP系统()
            providerTradeService.defalutPayOrderAsycToERP(tid);
        }
        sensorsDataService.sendPaySuccessEvent(Arrays.asList(trade));
        orderCouponService.addCouponRecord(trade);
        return true;
    }

    /**
     * 0元订单批量支付
     *
     * @param trades
     * @param payWay
     * @return true|false
     */
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public void tradeDefaultPayBatch(List<Trade> trades, PayWay payWay) {
        trades.forEach(i -> this.tradeDefaultPay(i, payWay));
    }

    /**
     * 新增线下收款单(包含线上线下的收款单)
     *
     * @param receivableAddRequest receivableAddRequest
     * @param platform             platform
     * @return 收款单
     */
    @Transactional
    public Optional<PayOrder> addReceivable(ReceivableAddRequest receivableAddRequest, Platform platform) {
        PayOrder payOrder = payOrderRepository.findById(receivableAddRequest.getPayOrderId()).orElse(null);
        if (Objects.isNull(payOrder) || DeleteFlag.YES.equals(payOrder.getDelFlag())) {
            throw new SbcRuntimeException("K-070001");
        }
        if (!CollectionUtils.isEmpty(receivableRepository.findByDelFlagAndPayOrderId(DeleteFlag.NO, payOrder
                .getPayOrderId()))) {
            throw new SbcRuntimeException("K-070005");
        }

        /**1.创建收款单*/
        Receivable receivable = new Receivable();
        BeanUtils.copyProperties(receivableAddRequest, receivable);
        receivable.setOfflineAccountId(receivableAddRequest.getAccountId());
        String createTime = receivableAddRequest.getCreateTime();
        // 2020-06-02T11:41:31.123
        if (createTime.contains("T")) {
            receivable.setCreateTime(LocalDateTime.parse(createTime));
        } else {
            if (createTime.length() == 10) {
                receivable.setCreateTime(LocalDateTime.of(LocalDate.parse(createTime,
                        DateTimeFormatter.ofPattern(DateUtil.FMT_DATE_1)), LocalTime.MIN));
            } else if (createTime.length() == DateUtil.FMT_TIME_1.length()) {
                receivable.setCreateTime(LocalDateTime.parse(createTime,
                        DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));
            } else {
                receivable.setCreateTime(LocalDateTime.parse(createTime));
            }
        }

        receivable.setDelFlag(DeleteFlag.NO);
        receivable.setReceivableNo(generatorService.generateSid());
        receivable.setPayChannel(receivableAddRequest.getPayChannel());
        receivable.setPayChannelId(receivableAddRequest.getPayChannelId());

        //这里往缓存里面写
        payOrder.setReceivable(receivableRepository.saveAndFlush(receivable));
        /**2.更改支付单状态*/
        PayOrderStatus status;
        if (osUtil.isS2b()) {
            status = platform == Platform.PLATFORM ? PayOrderStatus.PAYED : PayOrderStatus.TOCONFIRM;
        } else {
            status = platform == Platform.BOSS ? PayOrderStatus.PAYED : PayOrderStatus.TOCONFIRM;
        }
        if (PayType.ONLINE.equals(payOrder.getPayType())) {
            status = PayOrderStatus.PAYED;
        }
        payOrder.setPayOrderStatus(status);
        payOrderService.updatePayOrder(receivableAddRequest.getPayOrderId(), status);


        return Optional.of(payOrder);
    }

    /**
     * 新增线下收款单(包含线上线下的收款单)(包含支付回调)
     *
     * @param receivableAddRequest receivableAddRequest
     * @param platform             platform
     * @return 收款单
     */
    @Transactional
    @GlobalTransactional
    public void addReceivable(ReceivableAddRequest receivableAddRequest, Platform platform, Operator operator) {
        PayOrder payOrder = payOrderRepository.findById(receivableAddRequest.getPayOrderId()).orElse(null);
        if (Objects.isNull(payOrder) || DeleteFlag.YES.equals(payOrder.getDelFlag())) {
            throw new SbcRuntimeException("K-070001");
        }
        if (!CollectionUtils.isEmpty(receivableRepository.findByDelFlagAndPayOrderId(DeleteFlag.NO, payOrder
                .getPayOrderId()))) {
            throw new SbcRuntimeException("K-070005");
        }

        /**1.创建收款单*/
        Receivable receivable = new Receivable();
        BeanUtils.copyProperties(receivableAddRequest, receivable);
        receivable.setOfflineAccountId(receivableAddRequest.getAccountId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (receivableAddRequest.getCreateTime().length() == 10) {
            receivable.setCreateTime(LocalDateTime.of(LocalDate.parse(receivableAddRequest.getCreateTime(),
                    formatter), LocalTime.MIN));
        } else {
            receivable.setCreateTime(LocalDateTime.parse(receivableAddRequest.getCreateTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        receivable.setDelFlag(DeleteFlag.NO);
        receivable.setReceivableNo(generatorService.generateSid());
        receivable.setPayChannel(receivableAddRequest.getPayChannel());
        receivable.setPayChannelId(receivableAddRequest.getPayChannelId());

        //这里往缓存里面写
        payOrder.setReceivable(receivableRepository.saveAndFlush(receivable));
        /**2.更改支付单状态*/
        PayOrderStatus status;
        if (osUtil.isS2b()) {
            status = platform == Platform.PLATFORM ? PayOrderStatus.PAYED : PayOrderStatus.TOCONFIRM;
        } else {
            status = platform == Platform.BOSS ? PayOrderStatus.PAYED : PayOrderStatus.TOCONFIRM;
        }
        if (PayType.ONLINE.equals(payOrder.getPayType())) {
            status = PayOrderStatus.PAYED;
        }
        payOrder.setPayOrderStatus(status);
        payOrderService.updatePayOrder(receivableAddRequest.getPayOrderId(), status);
        Optional.of(payOrder).ifPresent(p ->
                this.payCallBack(p.getOrderCode(), p.getPayOrderPrice(), operator, PayWay.CASH));

    }


    /**
     * 确认支付单
     * //todo PayService doPay
     *
     * @param payOrderIds payOrderIds
     */
    @GlobalTransactional
    @Transactional
    public void confirmPayOrder(List<String> payOrderIds, Operator operator) {
        List<PayOrder> offlinePayOrders = null;
        if (CollectionUtils.isEmpty(payOrderIds)) {
            throw new SbcRuntimeException("K-020002");
        }
        // 页面不区分线上付款还是线下付款，都会传过来，这里先过滤一遍，得到线下付款的
        List<PayOrder> payOrders = payOrderRepository.findByPayOrderIds(payOrderIds);

        if (!CollectionUtils.isEmpty(payOrders)) {
            offlinePayOrders = payOrders.stream().filter(payOrder -> payOrder.getPayType() == PayType.OFFLINE)
                    .collect(Collectors.toList());
            List<String> offlineIds = offlinePayOrders.stream().map(PayOrder::getPayOrderId).collect(Collectors
                    .toList());
            if (!CollectionUtils.isEmpty(offlineIds)) {
                payOrderRepository.updatePayOrderStatus(offlineIds, PayOrderStatus.PAYED);
            }
        }
        Optional.ofNullable(offlinePayOrders).ifPresent(payOrderVOS -> payOrderVOS.forEach(e -> {
            this.payCallBack(e.getOrderCode(), e.getPayOrderPrice(), operator, PayWay.CASH);

            //第三方渠道同步订单
            orderProducerService.sendMQForThirdPlatformSync(e.getOrderCode());
        }));

    }


    /**
     * 更新订单的结算状态
     *
     * @param storeId
     * @param startTime
     * @param endTime
     */
    public void updateSettlementStatus(Long storeId, Date startTime, Date endTime) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("supplier.storeId").is(storeId)
                , new Criteria().orOperator(
                        Criteria.where("tradeState.flowState").is(FlowState.COMPLETED),
                        Criteria.where("refundFlag").is(true))
                , Criteria.where("tradeState.deliverStatus").in(Arrays.asList(DeliverStatus.SHIPPED,
                        DeliverStatus.PART_SHIPPED))
                , Criteria.where("tradeState.endTime").lt(endTime).gte(startTime)
        );

        mongoTemplate.updateMulti(new Query(criteria), new Update().set("hasBeanSettled", true), Trade.class);
    }

    /**
     * 查询订单信息作为结算原始数据
     *
     * @param storeId
     * @param startTime
     * @param endTime
     * @param pageRequest
     * @return
     */
    public List<Trade> findTradeListForSettlement(Long storeId, Date startTime, Date endTime, Pageable pageRequest) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("supplier.storeId").is(storeId)
                , new Criteria().orOperator(
                        Criteria.where("tradeState.flowState").is(FlowState.COMPLETED),
                        Criteria.where("tradeState.flowState").is(FlowState.VOID),
                        Criteria.where("refundFlag").is(true))
                , Criteria.where("tradeState.deliverStatus").in(Arrays.asList(DeliverStatus.SHIPPED,
                        DeliverStatus.PART_SHIPPED))
                , Criteria.where("returnOrderNum").is(0)
                , Criteria.where("tradeState.finalTime").lt(endTime).gte(startTime)
        );

        return mongoTemplate.find(
                new Query(criteria).skip(pageRequest.getPageNumber() * pageRequest.getPageSize()).limit(pageRequest
                        .getPageSize())
                , Trade.class);
    }

    /**
     * 根据快照封装订单确认页信息
     *
     * @param g
     * @return
     */
    public TradeConfirmItem getPurchaseInfo(TradeItemGroup g, List<TradeItem> gifts, List<TradeItem> markupList) {
        TradeConfirmItem item = new TradeConfirmItem();
        TradePrice price = new TradePrice();
        item.setTradeItems(g.getTradeItems());
        item.setSupplier(g.getSupplier());
        //计算商品总价
        handlePrice(g.getTradeItems(), price);
        //验证并计算各营销活动的优惠金额,实付金额,赠品List
        List<TradeMarketingVO> tradeMarketings = wrapperMarketingForConfirm(g.getTradeItems(), g.getTradeMarketingList());
        List<Discounts> discountsList = new ArrayList<>();
        //每个订单的多个优惠信息(满折优惠了xx,满减优惠了yy)
        item.setDiscountsPrice(discountsList);


        List<TradeMarketingVO> tempList = tradeMarketings.stream().filter(i -> i.getMarketingType() != MarketingType.GIFT
                && i.getMarketingType() != MarketingType.MARKUP).collect(Collectors.toList());
        tempList.forEach(i -> {
            Discounts discounts = Discounts.builder()
                    .amount(i.getDiscountsAmount())
                    .type(i.getMarketingType())
                    .build();
            discountsList.add(discounts);
            //设置营销商品优惠后的均摊价 (用于计算运费)
            List<TradeItem> items = item.getTradeItems().stream().filter(t -> i.getSkuIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            tradeItemService.clacSplitPrice(items, i.getRealPayAmount());
        });

        //应付金额 = 商品总金额 - 优惠总金额
        if (!price.isSpecial()) {
            BigDecimal discountsPrice = tempList.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal
                    .ZERO, BigDecimal::add);
            price.setTotalPrice(price.getTotalPrice().subtract(discountsPrice));
        }

        // 加价购商品
        if (CollectionUtils.isNotEmpty(markupList)) {
            BigDecimal markupPrice = markupList.stream().map(TradeItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            price.setMarkupPrice(markupPrice);
            price.setTotalPrice(price.getTotalPrice().add(markupPrice));
            price.setGoodsPrice(price.getGoodsPrice().add(markupPrice));
        }
        item.setTradePrice(price);
        //赠品信息
        item.setGifts(wrapperGifts(g.getTradeMarketingList(), tradeMarketings, gifts));
        item.setGifts(giftNumCheck(item.getGifts()));
        item.getTradeItems().addAll(markupList);
        return item;
    }

    /**
     * 包装营销信息(供确认订单使用)
     */
    public List<TradeMarketingVO> wrapperMarketingForConfirm(List<TradeItem> skus, List<TradeMarketingDTO>
            tradeMarketingRequests) {

        //积分换购活动只允许购物车存在一件商品
        for (TradeMarketingDTO tradeMarketingRequest : tradeMarketingRequests) {
            Integer marketingSubType = tradeMarketingRequest.getMarketingSubType();
            if(marketingSubType != null && MarketingSubType.POINT_BUY.toValue() == marketingSubType){
                if(skus.size() > 1){
                    throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "不满足积分换购活动条件");
                }
                for (TradeItem tradeItem : skus) {
                    if(tradeItem.getNum() > 1){
                        throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "不满足积分换购活动条件");
                    }
                }
            }
        }
        // 1.构建营销插件请求对象
        List<TradeMarketingWrapperDTO> requests = new ArrayList<>();
        List<TradeMarketingVO> tradeMarketings = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tradeMarketingRequests)) {
            tradeMarketingRequests.forEach(tradeMarketing -> {
                List<TradeItemInfoDTO> tradeItems = skus.stream()
                        .filter(s -> tradeMarketing.getSkuIds().contains(s.getSkuId()))
                        .map(t -> TradeItemInfoDTO.builder()
                                .num(t.getNum())
                                .price(t.getPrice())
                                .skuId(t.getSkuId())
                                .storeId(t.getStoreId())
                                .goodsType(t.getGoodsType())
                                .distributionGoodsAudit(t.getDistributionGoodsAudit())
                                .build())
                        .collect(Collectors.toList());
                requests.add(TradeMarketingWrapperDTO.builder().tradeMarketingDTO(tradeMarketing)
                        .tradeItems(tradeItems).build());
            });


        }
        // requests.stream().filter(r->CollectionUtils.isNotEmpty(r.getTradeItems())).collect(Collectors.toList());
        // 2.调用营销插件，并设置满系营销信息
        if (CollectionUtils.isNotEmpty(requests)) {
            List<TradeMarketingWrapperVO> voList = marketingTradePluginProvider.batchWrapper(MarketingTradeBatchWrapperRequest.builder()
                    .wraperDTOList(requests).build()).getContext().getWraperVOList();
            if (CollectionUtils.isNotEmpty(voList)) {
                voList.forEach(tradeMarketingWrapperVO -> {
                    tradeMarketings.add(tradeMarketingWrapperVO.getTradeMarketing());
                });
            }
        }

        return tradeMarketings;
    }


    /**
     * 包装营销信息(供提交订单使用)
     */
    public void wrapperMarketingForCommit(Trade trade, TradeParams tradeParams) {

        // 1.构建订单满系营销对象
        trade.setTradeMarketings(this.wrapperMarketingForConfirm(trade.getTradeItems(),
                tradeParams.getMarketingList()));

        // 2.构建订单优惠券对象
        if (StringUtils.isNotEmpty(tradeParams.getCouponCodeId())) {
            trade.setTradeCoupon(tradeMarketingService.buildTradeCouponInfo(
                    trade.getTradeItems(), tradeParams.getCouponCodeId(), tradeParams.isForceCommit(),
                    trade.getBuyer().getId()));
        }

    }


    /**
     * 用于编辑订单前的展示信息，包含了原订单信息和最新关联的订单商品价格（计算了会员价和级别价后的商品单价）
     *
     * @param tid tid
     * @return 返回订单与订单商品最新价格信息
     */
    public TradeRemedyDetails getTradeRemedyDetails(String tid) {
        Trade trade = detail(tid);
        TradeGoodsListVO goodsInfoResponse = tradeGoodsService.getGoodsInfoResponse(trade);
        List<TradeItem> items = trade.getTradeItems().stream().map(i ->
                TradeItem.builder()
                        .skuId(i.getSkuId())
                        .num(i.getNum())
                        .build()).collect(Collectors.toList());
        calcGoodsPrice(items, goodsInfoResponse);
        Map<String, TradeItemPrice> tradeItemPriceMap = items.stream().map(i -> new TradeItemPrice(i.getSkuId(), i
                .getLevelPrice())).collect(Collectors.toMap(TradeItemPrice::getSkuId, Function.identity()));
        return new TradeRemedyDetails(trade, tradeItemPriceMap);
    }

    /**
     * 获取赠品信息
     * 主要是设置各赠品应赠送的数量
     *
     * @param marketingRequests
     * @param tradeMarketings
     * @param gifts             @return
     */
    private List<TradeItem> wrapperGifts(List<TradeMarketingDTO> marketingRequests,
                                         List<TradeMarketingVO> tradeMarketings, List<TradeItem> gifts) {
        if (CollectionUtils.isEmpty(gifts)) {
            return Collections.emptyList();
        }
        List<TradeItem> resultList = new ArrayList<>();
        Map<Long, TradeMarketingVO> tradeMarketingMap = tradeMarketings.stream().filter(m -> m.getMarketingType() ==
                MarketingType.GIFT)
                .collect(Collectors.toMap(TradeMarketingVO::getMarketingId, Function.identity()));
        for (TradeMarketingDTO i : marketingRequests) {
            TradeMarketingVO marketing = tradeMarketingMap.get(i.getMarketingId());
            if (marketing == null) {
                //若传入的营销并非满赠,则跳过循环
                continue;
            }
            MarketingFullGiftLevelVO level = marketing.getGiftLevel();
            FullGiftDetailListByMarketingIdAndLevelIdRequest request =
                    FullGiftDetailListByMarketingIdAndLevelIdRequest.builder().build();
            request.setMarketingId(i.getMarketingId());
            request.setGiftLevelId(level.getGiftLevelId());
            FullGiftDetailListByMarketingIdAndLevelIdResponse fullGiftDetailListByMarketingIdAndLevelIdResponse =
                    fullGiftQueryProvider.listDetailByMarketingIdAndLevelId(request).getContext();

            Map<String, MarketingFullGiftDetailVO> detailMap = fullGiftDetailListByMarketingIdAndLevelIdResponse
                    .getFullGiftDetailVOList().stream().filter(d -> i.getGiftSkuIds().contains(d.getProductId()))
                    .collect(Collectors.toMap(MarketingFullGiftDetailVO::getProductId, Function.identity()));

            level.setFullGiftDetailList(new ArrayList<>(detailMap.values()));
            List<String> giftIds = new ArrayList<>(detailMap.keySet());
            //校验是否满足满赠条件
            boolean flag = i.getGiftSkuIds().stream().anyMatch(g -> !giftIds.contains(g));
            if (flag) {
                throw new SbcRuntimeException("K-050312");
            }

            List<TradeItem> giftItems = gifts.stream().filter(g -> i.getGiftSkuIds().contains(g.getSkuId()))
                    .collect(Collectors.toList());
            List<TradeItem> tpList = giftItems.stream().map(g -> {
                TradeItem item = new TradeItem();
                KsBeanUtil.copyProperties(g, item);
                item.setNum(detailMap.get(g.getSkuId()).getProductNum());
                if (item.getMarketingIds() != null) {
                    item.getMarketingIds().add(i.getMarketingId());
                }

                if (item.getMarketingLevelIds() != null) {
                    item.getMarketingLevelIds().add(level.getGiftLevelId());
                }
                return item;
            }).collect(Collectors.toList());
            resultList.addAll(tpList);
        }
        return resultList;

    }

    /**
     * 平台,商家带客下单，审核关闭都要创建支付单
     *
     * @param trade
     * @param operator
     * @param orderAuditSwitch
     */
    private void createPayOrder(Trade trade, Operator operator, Boolean orderAuditSwitch) {
        if (operator.getPlatform() == Platform.BOSS || operator.getPlatform() == Platform.WX_VIDEO || operator.getPlatform() == Platform.SUPPLIER ||
                !orderAuditSwitch) {
            createPayOrder(trade);
        }
    }

    private void createOrderInvoice(Trade trade, Operator operator) {
        OrderInvoiceSaveRequest request = buildOrderInvoiceSaveRequest(trade);
        if (request == null) {
            return;
        }
        Optional<OrderInvoice> optional = orderInvoiceService.generateOrderInvoice(request, operator.getUserId(),
                InvoiceState.WAIT);
        optional.ifPresent(invoice -> trade.getInvoice().setOrderInvoiceId(invoice.getOrderInvoiceId()));
    }

    private OrderInvoiceSaveRequest buildOrderInvoiceSaveRequest(Trade trade) {
        Invoice invoice;
        if ((invoice = trade.getInvoice()) == null || trade.getInvoice().getType() == -1) {
            return null;
        }
        boolean isGeneral = invoice.getType() == 0;
        OrderInvoiceSaveRequest request = new OrderInvoiceSaveRequest();
        request.setCustomerId(trade.getBuyer().getId());
        if(invoice.getEmail() != null){
            request.setInvoiceEmail(invoice.getEmail());
        }
        if (Objects.nonNull(invoice.getAddress())) {
            request.setInvoiceAddress(trade.getInvoice().getContacts() + " " + trade.getInvoice().getPhone() + " " +
                    invoice.getAddress());
        } else {
            request.setInvoiceAddress(trade.getBuyer().getName() + " " + trade.getBuyer().getPhone() + " " + trade
                    .getConsignee().getDetailAddress());
        }
        request.setInvoiceTitle(isGeneral ? invoice.getGeneralInvoice().getFlag() == 0 ? null :
                invoice.getGeneralInvoice().getTitle()
                : invoice.getSpecialInvoice().getCompanyName());
        if(invoice.getType() == 2 && StringUtils.isNotEmpty(invoice.getGeneralInvoice().getTitle())){
            request.setInvoiceTitle(invoice.getGeneralInvoice().getTitle());
        }
        request.setInvoiceType(InvoiceType.NORMAL.fromValue(invoice.getType()));
        request.setOrderNo(trade.getId());
        request.setProjectId(invoice.getProjectId());
        request.setOrderInvoiceId(invoice.getOrderInvoiceId());
        request.setCompanyInfoId(trade.getSupplier().getSupplierId());
        request.setStoreId(trade.getSupplier().getStoreId());
        return request;
    }

    /**
     * 计算订单价格
     * 订单价格 = 商品总价 - 营销优惠总金额
     *
     * @param trade
     */
    private TradePrice calc(Trade trade) {
        TradePrice tradePriceTemp = trade.getTradePrice();
        if (tradePriceTemp == null) {
            tradePriceTemp = new TradePrice();
            trade.setTradePrice(tradePriceTemp);
        }
        final TradePrice tradePrice = tradePriceTemp;

        // 1.计算商品总价
        handlePrice(trade.getTradeItems(), tradePrice);
        List<TradeMarketingVO> list = trade.getTradeMarketings().stream().filter(i -> i.getMarketingType()
                != MarketingType.GIFT && i.getMarketingType()
                != MarketingType.MARKUP).collect(Collectors.toList());

        // 2.计算所有营销活动的总优惠金额(非满赠)
        BigDecimal discountPrice = list.stream().filter(i -> i.getMarketingType() != MarketingType.GIFT && i.getMarketingType()
                != MarketingType.MARKUP).map
                (TradeMarketingVO
                        ::getDiscountsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        //营销活动优惠总额
        tradePrice.setMarketingDiscountPrice(discountPrice);

        if (trade.getTradeCoupon() != null) {
            discountPrice = discountPrice.add(trade.getTradeCoupon().getDiscountsAmount());
        }

        // 3.计算各类营销活动的优惠金额(比如:满折优惠xxx,满减优惠yyy)
        List<DiscountsPriceDetail> discountsPriceDetails = new ArrayList<>();
        list.stream().collect(Collectors.groupingBy(TradeMarketingVO::getMarketingType)).forEach((key, value) -> {
            DiscountsPriceDetail detail = DiscountsPriceDetail.builder()
                    .marketingType(key)
                    .discounts(value.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal.ZERO,
                            BigDecimal::add))
                    .build();
            discountsPriceDetails.add(detail);
        });
        tradePrice.setDiscountsPriceDetails(discountsPriceDetails);

        // 4.设置优惠券优惠金额
        if (trade.getTradeCoupon() != null) {
            BigDecimal couponPrice = trade.getTradeCoupon().getDiscountsAmount();
            tradePrice.setCouponPrice(couponPrice);
        }

        // 5.设置优惠总金额、应付金额 = 商品总金额 - 总优惠金额
        tradePrice.setDiscountsPrice(discountPrice);
        tradePrice.setTotalPrice(tradePrice.getTotalPrice().subtract(discountPrice));
        return tradePrice;
    }


    private void calcGoodsPrice(List<TradeItem> tradeItems, TradeGoodsListVO goodsInfoResponse) {
        List<GoodsInfoVO> goodsInfos = goodsInfoResponse.getGoodsInfos();
        Map<String, GoodsVO> goodsMap = goodsInfoResponse.getGoodses().stream().collect(Collectors.toMap
                (GoodsVO::getGoodsId, Function.identity()));
        Map<String, GoodsInfoVO> goodsInfoMap =
                goodsInfos.stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId,
                        Function.identity()));
        tradeItems
                .forEach(tradeItem -> {
                    GoodsInfoVO goodsInfo = goodsInfoMap.get(tradeItem.getSkuId());
                    GoodsVO goods = goodsMap.get(goodsInfo.getGoodsId());
                    //4. 填充价格
                    List<GoodsIntervalPriceVO> goodsIntervalPrices = goodsInfoResponse.getGoodsIntervalPrices();
                    // 订货区间设价
                    if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
                        Long buyNum = tradeItem.getNum();
                        Optional<GoodsIntervalPriceVO> first = goodsIntervalPrices.stream()
                                .filter(item -> item.getGoodsInfoId().equals(tradeItem.getSkuId()))
                                .filter(intervalPrice -> buyNum >= intervalPrice.getCount()).max(Comparator
                                        .comparingLong(GoodsIntervalPriceVO::getCount));
                        if (first.isPresent()) {
                            GoodsIntervalPriceVO goodsIntervalPrice = first.get();
                            tradeItem.setLevelPrice(goodsIntervalPrice.getPrice());
                            tradeItem.setPrice(goodsIntervalPrice.getPrice());
                            return;
                        }
                    }
                    tradeItem.setPrice(goodsInfo.getSalePrice());
                    tradeItem.setLevelPrice(goodsInfo.getSalePrice());
                });
    }

    private void checkLogisticsNo(String logisticsNo, String logisticStandardCode) {
        if (tradeRepository
                .findTopByTradeDelivers_Logistics_LogisticNoAndTradeDelivers_Logistics_logisticStandardCode(logisticsNo,
                        logisticStandardCode)
                .isPresent()) {
            throw new SbcRuntimeException("K-050124");
        }

    }

    /**
     * 计算商品总价
     *
     * @param tradeItems 多个订单项(商品)
     * @param tradePrice 订单价格对象(其中包括商品商品总金额,原始金额,应付金额)
     */
    private void handlePrice(List<TradeItem> tradeItems, TradePrice tradePrice) {
        tradePrice.setGoodsPrice(BigDecimal.ZERO);
        tradePrice.setOriginPrice(BigDecimal.ZERO);
        tradePrice.setTotalPrice(BigDecimal.ZERO);
        tradePrice.setBuyPoints(null);
        tradeItems.forEach(t -> {
            BigDecimal buyItemPrice = t.getPrice().multiply(BigDecimal.valueOf(t.getNum()));
            // 订单商品总价
            tradePrice.setGoodsPrice(tradePrice.getGoodsPrice().add(buyItemPrice));
            // 订单应付总金额
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(buyItemPrice));
            // 订单原始总金额
            tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(buyItemPrice));
            // 订单积分价商品总积分
            if (Objects.nonNull(t.getBuyPoint())) {
                tradePrice.setBuyPoints(Objects.isNull(tradePrice.getBuyPoints()) ?
                        t.getBuyPoint() * t.getNum() : tradePrice.getBuyPoints() + t.getBuyPoint() * t.getNum());
            }
        });

        System.out.println("");
    }

    private List<TradeItem> giftNumCheck(List<TradeItem> gifts) {
        if (CollectionUtils.isEmpty(gifts)) {
            return Collections.emptyList();
        }
        List<TradeItem> distinctGifts = new ArrayList<>();
        //相同赠品累加
        Map<String, List<TradeItem>> giftMap = gifts.stream().collect(Collectors.groupingBy(TradeItem::getSkuId));
        giftMap.forEach((key, item) -> {
            Long num = item.stream().map(TradeItem::getNum).reduce(0L, (a, b) -> a + b);
            TradeItem tradeItem = item.get(0);
            tradeItem.setNum(num);
            distinctGifts.add(tradeItem);
        });

        List<String> giftIds = new ArrayList<>(giftMap.keySet());

        Map<String, GoodsInfoVO> skusMap = goodsInfoQueryProvider.listByIds(
                GoodsInfoListByIdsRequest.builder().goodsInfoIds(giftIds).build()
        ).getContext().getGoodsInfos().stream()
                .filter(i -> Objects.isNull(i.getThirdPlatformType()))
                .collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, Function.identity()));
        distinctGifts.forEach(i -> {
            //赠品根据库存剩余，赠完为止
            GoodsInfoVO goodsInfo = skusMap.get(i.getSkuId());
            if (goodsInfo != null && i.getNum() > goodsInfo.getStock()) {
                i.setNum(goodsInfo.getStock());
            }
        });
        return distinctGifts;
    }

    /**
     * 根据营销活动,检查并设置各赠品数量
     *
     * @param trade
     */
    private void giftSet(Trade trade) {
        //赠品设置
        List<TradeMarketingDTO> marketingRequests = new ArrayList<>();
        trade.getTradeMarketings().forEach(i -> {
            if (i.getMarketingType() == MarketingType.GIFT) {
                TradeMarketingDTO req = TradeMarketingDTO.builder()
                        .giftSkuIds(i.getGiftIds())
                        .marketingId(i.getMarketingId())
                        .marketingLevelId(i.getGiftLevel().getGiftLevelId())
                        .skuIds(i.getSkuIds())
                        .build();
                marketingRequests.add(req);
            }
        });
        trade.setGifts(wrapperGifts(marketingRequests, trade.getTradeMarketings(), trade.getGifts()));
        trade.setGifts(giftNumCheck(trade.getGifts()));
    }

    /**
     * 营销价格计算-结算信息设置
     * 【商品价格计算第②步】: 商品的 满折/满减营销活动 均摊价 -> splitPrice
     *
     * @param trade
     */
    private void calcMarketingPrice(Trade trade) {
        // 1.设置满系营销商品优惠后的均摊价、结算信息
        trade.getTradeMarketings().stream()
                .filter(i -> i.getMarketingType() != MarketingType.GIFT && i.getMarketingType() != MarketingType.MARKUP).forEach(i -> {
            List<TradeItem> items = trade.getTradeItems().stream().filter(t -> Objects.isNull(t.getIsMarkupGoods()) || !t.getIsMarkupGoods())
                    .filter(t -> i.getSkuIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            tradeItemService.clacSplitPrice(items, i.getRealPayAmount());
            items.forEach(t -> {
                List<TradeItem.MarketingSettlement> settlements = new ArrayList<>();
                settlements.add(TradeItem.MarketingSettlement.builder().marketingType(i.getMarketingType())
                        .splitPrice(t.getSplitPrice()).build());
                t.setMarketingSettlements(settlements);
            });
        });

        // 2.设置店铺优惠券后的均摊价、结算信息
        TradeCouponVO tradeCoupon = trade.getTradeCoupon();
        if (tradeCoupon != null) {
            // 2.1.查找出优惠券关联的商品，及总价
            List<TradeItem> items = trade.getTradeItems().stream()
                    .filter(t -> trade.getTradeCoupon().getGoodsInfoIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            // 换购商品加入店铺优惠计算
            items.addAll(trade.getTradeItems().stream()
                    .filter(t -> Objects.nonNull(t.getIsMarkupGoods()) && t.getIsMarkupGoods())
                    .collect(Collectors.toList()));
            BigDecimal total = tradeItemService.calcSkusTotalPrice(items);

            // 2.2.判断是否达到优惠券使用门槛
            BigDecimal fullBuyPrice = tradeCoupon.getFullBuyPrice();
            if (fullBuyPrice != null && fullBuyPrice.compareTo(total) == 1) {
                throw new SbcRuntimeException(CouponErrorCode.CUSTOMER_ORDER_COUPON_INVALID);
            }

            // 2.3.如果商品总价小于优惠券优惠金额，设置优惠金额为商品总价
            if (total.compareTo(tradeCoupon.getDiscountsAmount()) == -1) {
                tradeCoupon.setDiscountsAmount(total);
            }

            // 2.4.计算均摊价、结算信息
            items.forEach(item -> {
                if (Objects.isNull(item.getCouponSettlements())) {
                    item.setCouponSettlements(new ArrayList<>());
                }
                item.getCouponSettlements().add(TradeItem.CouponSettlement.builder()
                        .couponType(tradeCoupon.getCouponType())
                        .couponCodeId(tradeCoupon.getCouponCodeId())
                        .couponCode(tradeCoupon.getCouponCode())
                        .splitPrice(item.getSplitPrice()).build());
            });
            tradeItemService.calcSplitPrice(items, total.subtract(trade.getTradeCoupon().getDiscountsAmount()), total);
            items.forEach(item -> {
                TradeItem.CouponSettlement settlement =
                        item.getCouponSettlements().get(item.getCouponSettlements().size() - 1);
                settlement.setReducePrice(settlement.getSplitPrice().subtract(item.getSplitPrice()));
                settlement.setSplitPrice(item.getSplitPrice());
            });
        }


    }

    /**
     * 更新订单的业务员
     *
     * @param employeeId 业务员
     * @param customerId 客户
     */
    public void updateEmployeeId(String employeeId, String customerId) {
        mongoTemplate.updateMulti(new Query(Criteria.where("buyer.id").is(customerId)), new Update().set("buyer" +
                ".employeeId", employeeId), Trade.class);
    }

    /**
     * 更新是否返利标志
     *
     * @param tradeId
     * @param commissionFlag
     */
    public void updateCommissionFlag(String tradeId, Boolean commissionFlag) {
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(tradeId)),
                new Update().set("commissionFlag", commissionFlag), Trade.class);
    }

    /**
     * 更新入账时间
     */
    public void updateFinalTime(String tradeId, LocalDateTime finalTime) {
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(tradeId)),
                new Update().set("tradeState.finalTime", finalTime), Trade.class);
    }

    /**
     * 更新正在进行的退单数量、入账时间
     *
     * @param tradeId 订单id
     * @param addFlag 退单数加减状态
     */
    public void updateReturnOrderNum(String tradeId, boolean addFlag) {
        Trade trade = tradeRepository.findById(tradeId).orElse(null);
        if (Objects.isNull(trade)) {
            log.error("订单ID:{},查询不到订单信息", tradeId);
            return;
        }
        // 1.根据addFlag加减正在进行的退单
        Integer num = trade.getReturnOrderNum();

        // 2.如果当前退单完成时间比入账时间晚时,或者订单未完成直接进行退款操作，则将当前退单完成时间设置为入账时间
        LocalDateTime finalTime = trade.getTradeState().getFinalTime();
        LocalDateTime nowTime = LocalDateTime.now();
        if (Objects.isNull(finalTime) || (!addFlag && nowTime.isAfter(finalTime))) {
            finalTime = nowTime;
        }
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(tradeId)), new Update()
                .set("returnOrderNum", addFlag ? ++num : --num)
                .set("tradeState.finalTime", finalTime), Trade.class);
    }


    /**
     * 完善没有业务员的订单
     */
    public void fillEmployeeId() {
        List<Trade> trades = mongoTemplate.find(new Query(Criteria.where("buyer.employeeId").is(null)), Trade.class);
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        List<String> buyerIds = trades.stream()
                .filter(t -> Objects.nonNull(t.getBuyer()) && StringUtils.isNotBlank(t.getBuyer().getId()))
                .map(Trade::getBuyer)
                .map(Buyer::getId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(buyerIds)) {
            return;
        }

        Map<String, String> customerId = customerCommonService.listCustomerDetailByCondition(
                CustomerDetailListByConditionRequest.builder().customerIds(buyerIds).build())
                .stream()
                .filter(customerDetail -> StringUtils.isNotBlank(customerDetail.getEmployeeId()))
                .collect(Collectors.toMap(CustomerDetailVO::getCustomerId, CustomerDetailVO::getEmployeeId));

        customerId.forEach((key, value) -> this.updateEmployeeId(value, key));
    }

    /**
     * 退优惠券
     *
     * @param tradeId 订单id
     */
    public void returnCoupon(String tradeId) {
        // 获取当前的======订单
        Trade trade = this.detail(tradeId);
        // 获取订单中购买的商品数量
        Map<String, TradeItem> boughtSkuNum = trade.getTradeItems().stream()
                .collect(Collectors.toMap(TradeItem::getSkuId, Function.identity()));
        // 累加所有已退商品的数量
        Map<String, Integer> returnSkuNum = new HashMap<>();
        // 商家驳回订单
        if (trade.getTradeState().getAuditState() == AuditState.REJECTED) {
            setReturnNum(returnSkuNum, boughtSkuNum);
        } else if (trade.getTradeState().getFlowState() == FlowState.VOID
                && trade.getTradeState().getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED
                && trade.getTradeState().getPayState() == PayState.NOT_PAID) {
            // 用户取消订单
            setReturnNum(returnSkuNum, boughtSkuNum);
        }
        // 获取所有已退的===退单
        List<ReturnOrder> returnOrders = returnOrderRepository.findByTid(trade.getId()).stream()
                .filter(item -> item.getReturnFlowState() == ReturnFlowState.COMPLETED)
                .collect(Collectors.toList());
        // 获取已退商品数量集合
        returnOrders.forEach(r -> {
            r.getReturnItems().forEach(returnItem -> {
                Integer returnNum = returnSkuNum.get(returnItem.getSkuId());
                if (Objects.isNull(returnNum)) {
                    returnSkuNum.put(returnItem.getSkuId(), returnItem.getNum());
                } else {
                    returnSkuNum.put(returnItem.getSkuId(), returnNum + returnItem.getNum());
                }
            });
        });
        // 获取订单组信息
        TradeGroup tradeGroup = StringUtils.isNotEmpty(trade.getGroupId()) ?
                tradeGroupRepository.findById(trade.getGroupId()).orElse(null) : null;
        List<String> storeIds = new ArrayList<>();
        // 循环进行记录或者退券
        returnSkuNum.forEach((key, value) -> {
            // 退款中的该商品使用了优惠券 并且 退款商品的数量和订单中商品购买数量一致.
            if (Objects.nonNull(boughtSkuNum.get(key)) &&
                    !CollectionUtils.isEmpty(boughtSkuNum.get(key).getCouponSettlements()) &&
                    value == boughtSkuNum.get(key).getNum().intValue()) {
                // 订单组中订单使用了平台优惠券(全场赠券)
                if (boughtSkuNum.get(key).getCouponSettlements().stream()
                        .filter(f -> f.getCouponType() == CouponType.GENERAL_VOUCHERS).findFirst().isPresent() &&
                        Objects.nonNull(tradeGroup) && Objects.nonNull(tradeGroup.getCommonCoupon())) {
                    // 退货的商品使用了全场赠券
                    if (tradeGroup.getCommonCoupon().getGoodsInfoIds().contains(key)) {
                        // 此时认为该商品已经完全退货, 需要在"订单组"中做记录.
                        List<String> ids = tradeGroup.getCommonSkuIds();
                        if (!ids.contains(key)) {
                            ids.add(key);
                            tradeGroup.setCommonSkuIds(ids);
                            tradeGroupRepository.save(tradeGroup);
                        }
                        List<String> skuIds = tradeGroup.getCommonCoupon().getGoodsInfoIds();
                        // 如果全场赠券中商品集合为空, 则不执行后续操作
                        if (CollectionUtils.isEmpty(skuIds)) {
                            return;
                        }
                        // 如果已退商品集合和参加全场赠券的商品集合完全一致
                        if (CollectionUtils.isEqualCollection(skuIds, ids)) {
                            // 设置平台券完全已退
                            tradeGroup.setCommonCouponIsReturn(Boolean.TRUE);
                            tradeGroupRepository.save(tradeGroup);
                            // 退券(全场赠券)
                            couponCodeProvider.returnById(CouponCodeReturnByIdRequest.builder()
                                    .couponCodeId(tradeGroup.getCommonCoupon().getCouponCodeId())
                                    .build());
                        }
                    }
                }
                // 该订单存在使用店铺优惠券
                if (!Boolean.TRUE.equals(boughtSkuNum.get(key).getIsMarkupGoods())
                        &&boughtSkuNum.get(key).getCouponSettlements().stream()
                        .filter(f -> f.getCouponType() == CouponType.STORE_VOUCHERS).findFirst().isPresent() &&
                        Objects.nonNull(trade.getTradeCoupon())) {
                    storeIds.add(key);
                    if (CollectionUtils.isEqualCollection(
                            trade.getTradeCoupon().getGoodsInfoIds(), storeIds)) {
                        // 退券(店铺券)
                        couponCodeProvider.returnById(CouponCodeReturnByIdRequest.builder()
                                .couponCodeId(trade.getTradeCoupon().getCouponCodeId())
                                .build());
                    }
                }
            }
        });
    }

    /**
     * 设置退货数量
     *
     * @param returnSkuNum
     * @param boughtSkuNum
     */
    private void setReturnNum(Map<String, Integer> returnSkuNum, Map<String, TradeItem> boughtSkuNum) {
        boughtSkuNum.forEach((key, value) -> {
            Integer returnNum = returnSkuNum.get(key);
            if (Objects.isNull(returnNum)) {
                returnSkuNum.put(key, value.getNum().intValue());
            } else {
                returnSkuNum.put(key, returnNum + value.getNum().intValue());
            }
        });
    }


    /**
     * 根据查询条件获取订单列表--不分页
     *
     * @param whereCriteria
     * @return
     */
    public List<Trade> getTradeList(Criteria whereCriteria) {
        Query query = new Query(whereCriteria);
        List<Trade> tradeList = mongoTemplate.find(query, Trade.class);
        return tradeList;
    }

    /**
     * 订单超时未支付，系统自动取消订单
     *
     * @param tid
     */
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public void autoCancelOrder(String tid, Operator operator) {
        Trade trade = detail(tid);
        if (trade == null) {
            return;
        }
        String baseTid = tid;
        if (Boolean.TRUE.equals(trade.getIsBookingSaleGoods()) && BookingType.EARNEST_MONEY.equals(trade.getBookingType())) {
            baseTid = trade.getTailOrderNo(); //尾款订单号
        }
        //判断订单对应的支付记录是否存在
        List<String> tidList = new ArrayList<>();
        tidList.add(baseTid);
        tidList.add(trade.getParentId());

        //异常状态订单无需抛异常，不作处理即可
        if (trade.getTradeState().getPayState() == PayState.PAID) {
//            throw new SbcRuntimeException("K-050202");
            return;
        }
        if (trade.getTradeState().getPayState() == PayState.PAID_EARNEST && trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL) {
            if (LocalDateTime.now().isBefore(trade.getTradeState().getTailEndTime())) {
                return;
            }
        }
        if (trade.getTradeState().getDeliverStatus() != DeliverStatus.NOT_YET_SHIPPED) {
//            throw new SbcRuntimeException("K-050203");
            return;
        }

        if (trade.getTradeState().getFlowState() == FlowState.VOID) {
//            throw new SbcRuntimeException("K-050317");
            return;
        }

        //如果是微信支付，查询微信支付单，对应的支付状态是否是已支付，已支付则不进行取消操作
        WxPayOrderDetailReponse wxPayOrderDetailReponse = new WxPayOrderDetailReponse();
        if (trade.getPayWay() == PayWay.WECHAT) {
            if (trade.getPayInfo().isMergePay()) {
                wxPayOrderDetailReponse =
                        wxPayProvider.getWxPayOrderDetail(WxPayOrderDetailRequest.builder().businessId(trade.getParentId()).storeId(Constants.BOSS_DEFAULT_STORE_ID).build()).getContext();
            } else {
                wxPayOrderDetailReponse =
                        wxPayProvider.getWxPayOrderDetail(WxPayOrderDetailRequest.builder().businessId(baseTid).storeId(Constants.BOSS_DEFAULT_STORE_ID).build()).getContext();
            }
        }
        if ("SUCCESS".equals(wxPayOrderDetailReponse.getReturn_code()) && "SUCCESS".equals(wxPayOrderDetailReponse.getResult_code())
                && "SUCCESS".equals(wxPayOrderDetailReponse.getTrade_state())) {
            log.info("==========订单超时未支付取消，订单已支付取消失败，订单Id为：{}", baseTid);
            return;
        }
        List<PayCallBackResult> payCallBackResultList =
                payCallBackResultService.list(PayCallBackResultQueryRequest.builder().businessIds(tidList).build());
        if (payCallBackResultList.size() > 0) {
            return;
        }

        //校验是有视频号支付订单信息
        WxVideoOrderDetailResponse context = null;
        if (Objects.equals(trade.getChannelType(),ChannelType.MINIAPP) && Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
            context = wxOrderService.getWechatVideoOrder(trade);
            if (context != null) {
                WxVideoOrderDetailResponse.PayInfo payInfo = context.getOrder().getOrderDetail().getPayInfo();
                if (payInfo != null) {
                    log.info("==========视频号订单超时未支付取消，订单已支付取消失败，订单Id为：{}", baseTid);
                    return;
                }
            }
        }

        if (trade.getTradeState().getAuditState() == AuditState.CHECKED) {
            //删除支付单
            // payOrderService.deleteByPayOrderId(trade.getPayOrderId());
            PayOrder payOrder = payOrderRepository.getOne(trade.getPayOrderId());
            if (payOrder != null) {
                payOrder.setDelFlag(DeleteFlag.YES);
                payOrderRepository.save(payOrder);
            }

        }

        // 卡券商品释放库存
        verifyService.addCouponSkuListStock(trade.getTradeItems(), tid, operator);
        verifyService.addCouponSkuListStock(trade.getGifts(), tid, operator);
        //是否是秒杀抢购商品订单
        if (Objects.nonNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()) {
            flashSaleGoodsOrderAddStock(trade);
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
        } else {
            //释放库存
            verifyService.addSkuListStock(trade.getTradeItems());
            verifyService.addSkuListStock(trade.getGifts());
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
            bookingSaleGoodsOrderAddStock(trade);
        }

        //状态变更
        StateRequest stateRequest = StateRequest
                .builder()
                .tid(trade.getId())
                .operator(operator)
                .event(TradeEvent.VOID)
                .data("订单超时未支付，系统自动取消")
                .build();
        tradeFSMService.changeState(stateRequest);

        // 退优惠券
        returnCoupon(tid);

        //取消拼团订单
        grouponOrderService.cancelGrouponOrder(trade);
        // 退还限售的数量
        orderProducerService.backRestrictedPurchaseNum(tid, null, BackRestrictedType.ORDER_SETTING_TIMEOUT_CANCEL);

        // 取消供应商订单
        providerTradeService.providerCancel(tid, operator, true);
        //小程序发送取消消息
        if (context != null) {
            wxOrderService.sendWxCancelOrderMessage(trade, context);
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 秒杀商品订单还库存
     * @Date 13:47 2019/7/2
     * @Param [trade]
     **/
    @Transactional
    @GlobalTransactional
    public void flashSaleGoodsOrderAddStock(Trade trade) {
        //获取秒杀抢购活动详情
        FlashSaleGoodsVO flashSaleGoodsVO = flashSaleGoodsQueryProvider.getById(FlashSaleGoodsByIdRequest.builder()
                .id(trade.getTradeItems().get(0).getFlashSaleGoodsId())
                .build())
                .getContext().getFlashSaleGoodsVO();
        //判断秒杀活动是否还在进行中，如果在进行中，将库存加到秒杀活动商品的库存，否则加到原商品库存
        if (LocalDateTime.now().isAfter(flashSaleGoodsVO.getActivityFullTime()) &&
                LocalDateTime.now().isBefore(flashSaleGoodsVO.getActivityFullTime().plusHours(2))) {
            trade.getBuyer().getId();
            verifyService.addFlashSaleGoodsStock(trade.getTradeItems(), trade.getBuyer().getId());
            //秒杀商品参与活动不扣减库存改造，下单时要同时加库存
            verifyService.addSkuListStock(trade.getTradeItems());
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
        } else {
            //释放库存
            verifyService.addSkuListStock(trade.getTradeItems());
            verifyService.addSkuListStock(trade.getGifts());
            //释放冻结
            verifyService.releaseFrozenStock(trade.getTradeItems());
        }
        // 取消订单退还用户的已抢购数量
        String havePanicBuyingKey =
                RedisKeyConstant.FLASH_SALE_GOODS_HAVE_PANIC_BUYING + trade.getBuyer().getId() + flashSaleGoodsVO.getId();
        Integer totalBuyNum = 0;
        String hasBuyNum = redisService.getString(havePanicBuyingKey);
        if (StringUtils.isNotBlank(hasBuyNum)) {
            totalBuyNum = Integer.valueOf(hasBuyNum) - trade.getTradeItems().get(0).getNum().intValue();
        }
        redisService.setString(havePanicBuyingKey, totalBuyNum.toString());
    }


    /**
     * @param trade
     */
    @Transactional
    public void bookingSaleGoodsOrderAddStock(Trade trade) {
        if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()) {
            TradeItem tradeItem = trade.getTradeItems().get(0);
            List<BookingSaleGoodsVO> bookingSaleGoodsVOList =
                    bookingSaleGoodsQueryProvider.list(BookingSaleGoodsListRequest.builder().goodsInfoId(tradeItem.getSkuId()).bookingSaleId(tradeItem.getBookingSaleId()).build()).getContext().getBookingSaleGoodsVOList();
            if (Objects.nonNull(bookingSaleGoodsVOList.get(0).getBookingCount())) {
                bookingSaleGoodsProvider.addCanBookingCount(BookingSaleGoodsCountRequest.builder().goodsInfoId(tradeItem.getSkuId()).
                        bookingSaleId(tradeItem.getBookingSaleId()).stock(tradeItem.getNum()).build());
            }
        }
    }

    /**
     * 订单选择银联企业支付通知财务
     *
     * @param customerId
     * @param orderId
     * @param url
     */
    public void sendEmailToFinance(String customerId, String orderId, String url) {
        // 客户id、订单id、PC端服务器路径url不能为空
        if (StringUtils.isBlank(customerId) || StringUtils.isBlank(orderId) || StringUtils.isBlank(url)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        } else {
            BaseResponse<EmailConfigQueryResponse> config = emailConfigProvider.queryEmailConfig();
            // 邮箱停用状态下直接返回
            if (config.getContext().getStatus() == EmailStatus.DISABLE) {
                return;
            }
            // 查询客户收信邮箱
//            List<CustomerEmail> customerEmails = customerEmailRepository
//                    .findCustomerEmailsByCustomerIdAndDelFlagOrderByCreateTime(customerId, DeleteFlag.NO);
            List<CustomerEmailVO> customerEmails = customerEmailQueryProvider
                    .list(new NoDeleteCustomerEmailListByCustomerIdRequest(customerId)).getContext()
                    .getCustomerEmails();
            if (customerEmails.isEmpty()) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            Trade tradedetail = this.detail(orderId);
            tradeEmailService.sendMail(config, customerEmails, tradedetail, url);
        }
    }

    /**
     * 查询导出数据
     *
     * @param tradeQueryRequest
     */
    public List<Trade> listTradeExport(TradeQueryRequest tradeQueryRequest) {
        long count = this.countNum(tradeQueryRequest.getWhereCriteria(), tradeQueryRequest);
        if (count > 1000) {
            count = 1000;
        }
        tradeQueryRequest.putSort(tradeQueryRequest.getSortColumn(), tradeQueryRequest.getSortRole());
        tradeQueryRequest.setPageNum(0);
        tradeQueryRequest.setPageSize((int) count);

        //设置返回字段
        Map fieldsObject = new HashMap();
        fieldsObject.put("_id", true);
        fieldsObject.put("tradeState.createTime", true);
        fieldsObject.put("buyer.name", true);
        fieldsObject.put("buyer.account", true);
        fieldsObject.put("buyer.levelName", true);
        fieldsObject.put("consignee.name", true);
        fieldsObject.put("consignee.phone", true);
        fieldsObject.put("consignee.detailAddress", true);
        fieldsObject.put("payInfo.desc", true);
        fieldsObject.put("deliverWay", true);
        fieldsObject.put("tradePrice.deliveryPrice", true);
        fieldsObject.put("tradePrice.goodsPrice", true);
        fieldsObject.put("tradePrice.special", true);
        fieldsObject.put("tradePrice.privilegePrice", true);
        fieldsObject.put("tradePrice.totalPrice", true);
        fieldsObject.put("tradeItems.oid", true);
        fieldsObject.put("tradeItems.skuId", true);
        fieldsObject.put("tradeItems.skuNo", true);
        fieldsObject.put("tradeItems.goodsType", true);
        fieldsObject.put("tradeItems.num", true);
        fieldsObject.put("tradeItems.cateId", true);
        fieldsObject.put("buyerRemark", true);
        fieldsObject.put("sellerRemark", true);
        fieldsObject.put("tradeState.flowState", true);
        fieldsObject.put("tradeState.payState", true);
        fieldsObject.put("tradeState.deliverStatus", true);
        fieldsObject.put("invoice.type", true);
        fieldsObject.put("invoice.projectName", true);
        fieldsObject.put("invoice.generalInvoice.title", true);
        fieldsObject.put("invoice.specialInvoice.companyName", true);
        fieldsObject.put("supplier.supplierName", true);
        fieldsObject.put("gifts", true);
        fieldsObject.put("directChargeMobile", true);//返回直充手机号
//        fieldsObject.put("buyer", false);
        Query query = new BasicQuery(new Document(), new Document(fieldsObject));
        query.addCriteria(tradeQueryRequest.getWhereCriteria());
        List<Trade> tradeList = mongoTemplate.find(query.with(tradeQueryRequest.getPageRequest()), Trade.class);

        return tradeList;
    }

    /**
     * 生成积分兑换优惠券的积分订单
     */
    @GlobalTransactional
    @Transactional
    public PointsTradeCommitResult pointsCouponCommit(PointsCouponTradeCommitRequest commitRequest) {
        // 1.验证用户
        CustomerSimplifyOrderCommitVO customer =
                verifyService.simplifyById(commitRequest.getCustomer().getCustomerId());
        commitRequest.setCustomer(customer);

        // 2.包装积分优惠券订单信息
        Trade trade = this.wrapperPointsCouponTrade(commitRequest);

        // 3.提交积分兑换优惠券订单
        PointsTradeCommitResult result = this.createPointsCouponTrade(trade);

        // 4.扣除用户积分
        customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                .customerId(customer.getCustomerId())
                .type(OperateType.DEDUCT)
                .serviceType(PointsServiceType.COUPON_EXCHANGE)
                .points(trade.getTradePrice().getPoints())
                .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                .build());

        return result;
    }

    private Trade wrapperPointsCouponTrade(PointsCouponTradeCommitRequest commitRequest) {
        Trade trade = new Trade();
        // 设置订单基本信息
        Optional<CommonLevelVO> commonLevelVO;
        boolean flag = true;
        commonLevelVO =
                Optional.of(fromCustomerLevel(customerLevelQueryProvider.getDefaultCustomerLevel().getContext()));
        trade.setBuyer(Buyer.fromCustomer(commitRequest.getCustomer(), commonLevelVO, flag));

        TradePointsCouponItem tradeItem = new TradePointsCouponItem();
        tradeItem.setOid(generatorService.generateOid());
        tradeItem.setCouponInfoVO(commitRequest.getCouponInfoVO());

        trade.setId(generatorService.generateTid());
        trade.setPlatform(Platform.CUSTOMER);
        trade.setOrderSource(OrderSource.WECHAT);
        trade.setOrderType(OrderType.POINTS_ORDER);
        trade.setPointsOrderType(PointsOrderType.POINTS_COUPON);
        trade.setPayInfo(PayInfo.builder()
                .payTypeId(String.format("%d", PayType.ONLINE.toValue()))
                .payTypeName(PayType.ONLINE.name())
                .desc(PayType.ONLINE.getDesc())
                .build());
        trade.setRequestIp(commitRequest.getOperator().getIp());
        trade.setTradeCouponItem(tradeItem);
        trade.setTradePrice(TradePrice.builder().points(commitRequest.getPoints()).build());

        return trade;
    }

    /**
     * 提交积分订单
     *
     * @param trade 积分订单
     * @return 订单提交结果
     */
    @Transactional
    public PointsTradeCommitResult createPointsCouponTrade(Trade trade) {
        PointsTradeCommitResult commitResult = null;

        //创建订单
        try {
            // 订单状态默认为已完成
            trade.setTradeState(TradeState
                    .builder()
                    .deliverStatus(DeliverStatus.SHIPPED)
                    .payState(PayState.PAID)
                    .flowState(FlowState.COMPLETED)
                    .createTime(LocalDateTime.now())
                    .build());
            // 订单入库
            tradeService.addTrade(trade);

            commitResult = new PointsTradeCommitResult(trade.getId(), trade.getTradePrice().getPoints());
        } catch (Exception e) {
            log.error("commit points coupon trade error,trade={}", trade, e);
            if (e instanceof SbcRuntimeException) {
                throw e;
            } else {
                throw new SbcRuntimeException("K-020010");
            }
        }
        return commitResult;
    }

    /**
     * 发送消息
     *
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param routeParam
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, DistributionType nodeCode, List<String> params,
                             Map<String, Object> routeParam, String customerId, String pic, String mobile) {
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(routeParam);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setPic(pic);
        messageMQRequest.setMobile(mobile);
        orderProducerService.sendMessage(messageMQRequest);
    }

    /**
     * 判断商品是否企业购商品
     *
     * @return
     */
    private boolean isEnjoyIepGoodsInfo(EnterpriseAuditState enterpriseAuditState) {
        return !Objects.isNull(enterpriseAuditState)
                && enterpriseAuditState == EnterpriseAuditState.CHECKED;
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 支付回调处理，将原有逻辑迁移到order处理
     * @Date 14:56 2020/7/2
     * @Param [tradePayOnlineCallBackRequest]
     **/
    @Transactional
    @GlobalTransactional
    public void wxPayOnlineCallBack(TradePayOnlineCallBackRequest tradePayOnlineCallBackRequest) throws Exception {
        String businessId = "";
        try {
            PayGatewayConfigResponse payGatewayConfig = payQueryProvider.getGatewayConfigByGateway(new
                    GatewayConfigByGatewayRequest(PayGatewayEnum.WECHAT, tradePayOnlineCallBackRequest.getStoreId())).getContext();
            String apiKey = payGatewayConfig.getApiKey();
            XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
            xStream.alias("xml", WxPayResultResponse.class);
            WxPayResultResponse wxPayResultResponse =
                    (WxPayResultResponse) xStream.fromXML(tradePayOnlineCallBackRequest.getWxPayCallBackResultStr());
            log.info("-------------微信支付回调,wxPayResultResponse：{}------------", wxPayResultResponse);
            //判断当前回调是否是合并支付
            businessId = wxPayResultResponse.getOut_trade_no();
            boolean isMergePay = isMergePayOrder(businessId);
            String lockName;
            //非组合支付，则查出该单笔订单。
            if (businessId.startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)) {
                lockName = businessId;
            } else {
                if (!isMergePay) {
                    Trade trade = new Trade();
                    if (isTailPayOrder(businessId)) {
                        trade = tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(businessId).build()).get(0);
                    } else {
                        trade = tradeService.detail(businessId);
                    }
                    // 锁资源：无论是否组合支付，都锁父单号，确保串行回调
                    lockName = trade.getParentId();
                } else {
                    lockName = businessId;
                }
            }
            //redis锁，防止同一订单重复回调
            RLock rLock = redissonClient.getFairLock(lockName);
            rLock.lock();
            //执行回调
            try {
                //支付回调事件成功
                if (wxPayResultResponse.getReturn_code().equals(WXPayConstants.SUCCESS) &&
                        wxPayResultResponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
                    log.info("微信支付异步通知回调状态---成功");
                    //微信回调参数数据map
                    Map<String, String> params =
                            WXPayUtil.xmlToMap(tradePayOnlineCallBackRequest.getWxPayCallBackResultXmlStr());
                    String trade_type = wxPayResultResponse.getTrade_type();
                    //app支付回调对应的api key为开放平台对应的api key
                    if (trade_type.equals("APP")) {
                        apiKey = payGatewayConfig.getOpenPlatformApiKey();
                    }
                    //微信签名校验
                    if (WXPayUtil.isSignatureValid(params, apiKey)) {
                        //签名正确，进行逻辑处理--对订单支付单以及操作信息进行处理并添加交易数据
                        List<Trade> trades = new ArrayList<>();
                        //查询交易记录
                        TradeRecordByOrderCodeRequest tradeRecordByOrderCodeRequest =
                                new TradeRecordByOrderCodeRequest(businessId);
                        PayTradeRecordResponse recordResponse =
                                payQueryProvider.getTradeRecordByOrderCode(tradeRecordByOrderCodeRequest).getContext();
                        PayCallBackResult payCallBackResult =
                                payCallBackResultService.list(PayCallBackResultQueryRequest.builder().businessId(businessId).build()).get(0);
                        if (isMergePay) {
                            /*
                             * 合并支付
                             * 查询订单是否已支付或过期作废
                             */
                            trades = tradeService.detailsByParentId(businessId);
                            //订单合并支付场景状态采样
                            boolean paid =
                                    trades.stream().anyMatch(i -> i.getTradeState().getPayState() == PayState.PAID);
                            boolean cancel =
                                    trades.stream().anyMatch(i -> i.getTradeState().getFlowState() == FlowState.VOID);
                            if (cancel || (paid && !recordResponse.getTradeNo().equals(wxPayResultResponse.getTransaction_id()))) {
                                //同一批订单重复支付或过期作废，直接退款
                                wxRefundHandle(wxPayResultResponse, businessId, -1L);
                            } else if (payCallBackResult.getResultStatus() != PayCallBackResultStatus.SUCCESS) {
                                wxPayCallbackHandle(payGatewayConfig, wxPayResultResponse, businessId, trades, true);
                            }
                        } else {
                            Trade trade = new Trade();
                            if (isTailPayOrder(businessId)) {
                                trade =
                                        tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(businessId).build()).get(0);
                            } else {
                                trade = tradeService.detail(businessId);
                            }
                            trades.add(trade);
                            if (trade.getTradeState().getFlowState() == FlowState.VOID || (trade.getTradeState()
                                    .getPayState() == PayState.PAID
                                    && !recordResponse.getTradeNo().equals(wxPayResultResponse.getTransaction_id()))) {
                                //同一批订单重复支付或过期作废，直接退款
                                wxRefundHandle(wxPayResultResponse, businessId,tradePayOnlineCallBackRequest.getStoreId());
                            } else if (payCallBackResult.getResultStatus() != PayCallBackResultStatus.SUCCESS) {
                                wxPayCallbackHandle(payGatewayConfig, wxPayResultResponse, businessId, trades, false);
                            }
                            //单笔支付
                           /* if (businessId.startsWith(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)) {
                                //订单金额
                                String total_amount = wxPayResultResponse.getTotal_fee();

                                //支付终端类型
                                String type = wxPayResultResponse.getTrade_type();
                                // 处理付费会员回调业务
                                StringBuilder key = new StringBuilder();
                                key.append(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE)
                                        .append("_")
                                        .append(businessId);
                                redisTemplate.setKeySerializer(new StringRedisSerializer());
                                redisTemplate.setValueSerializer(new StringRedisSerializer());
                                Object o = redisTemplate.opsForValue().get(key.toString());
                                String result = o.toString();
                                PaidCardRedisDTO paidCardRedisDTO = JSON.parseObject(result, PaidCardRedisDTO.class);
                                log.info("===========开始执行付费会员回调逻辑");
                                paidCardCallBack(paidCardRedisDTO, total_amount, type);
                                log.info("===========结束执行付费会员回调逻辑");

                            } else {
                                Trade trade = new Trade();
                                if (isTailPayOrder(businessId)) {
                                    trade =
                                            tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(businessId).build()).get(0);
                                } else {
                                    trade = tradeService.detail(businessId);
                                }
                                if (trade.getTradeState().getFlowState() == FlowState.VOID || (trade.getTradeState()
                                        .getPayState() == PayState.PAID
                                        && !recordResponse.getTradeNo().equals(wxPayResultResponse.getTransaction_id()))) {
                                    //同一批订单重复支付或过期作废，直接退款
                                    wxRefundHandle(wxPayResultResponse, businessId, -1L);
                                } else if (payCallBackResult.getResultStatus() != PayCallBackResultStatus.SUCCESS) {
                                    trades.add(trade);
                                    wxPayCallbackHandle(payGatewayConfig, wxPayResultResponse, businessId, trades, false);
                                }
                            }*/
                        }
                        //支付回调处理成功
                        payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.SUCCESS);
                        sensorsDataService.sendPaySuccessEvent(trades);
                    } else {
                        log.info("微信支付异步回调验证签名结果[失败].");
                        //支付处理结果回写回执支付结果表
                        payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.FAILED);
                    }
                } else {
                    log.info("微信支付异步通知回调状态---失败");
                    //支付处理结果回写回执支付结果表
                    payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.FAILED);
                }
                log.info("微信支付异步通知回调end---------");
            } catch (Exception e) {
                log.error("微信支付异步通知回调end2---------", e);
                //支付处理结果回写回执支付结果表
                payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.FAILED);
            } finally {
                //解锁
                rLock.unlock();
            }
        } catch (Exception ex) {
            if (StringUtils.isNotBlank(businessId)) {
                payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.FAILED);
            }
            log.error(ex.getMessage());
        }
    }

    private void paidCardCallBack(PaidCardRedisDTO paidCardRedisDTO, String total_amount, String type) {
        // 执行业务回调
        BaseResponse response = paidCardSaveProvider.dealPayCallBack(paidCardRedisDTO);
        if (response.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
            // 修改支付流水信息
            //异步回调添加交易数据
            PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
            //流水号
            payTradeRecordRequest.setTradeNo(null);
            //商品订单号
            payTradeRecordRequest.setBusinessId(paidCardRedisDTO.getBusinessId());
            payTradeRecordRequest.setResult_code("SUCCESS");
            payTradeRecordRequest.setPracticalPrice(new BigDecimal(total_amount));
            payTradeRecordRequest.setChannelItemId(Long.valueOf(type));
            //添加交易数据（与微信共用）

            payProvider.wxPayCallBack(payTradeRecordRequest);
        }
    }

    /**
     * 是否是尾款订单号
     *
     * @param businessId
     * @return
     */
    private boolean isTailPayOrder(String businessId) {
        return businessId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID);
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 微信支付退款处理
     * @Date 15:29 2020/7/2
     * @Param [wxPayResultResponse, businessId, storeId]
     **/
    private void wxRefundHandle(WxPayResultResponse wxPayResultResponse, String businessId, Long storeId) {
        WxPayRefundInfoRequest refundInfoRequest = new WxPayRefundInfoRequest();

        refundInfoRequest.setStoreId(storeId);
        refundInfoRequest.setOut_refund_no(businessId);
        refundInfoRequest.setOut_trade_no(businessId);
        refundInfoRequest.setTotal_fee(wxPayResultResponse.getTotal_fee());
        refundInfoRequest.setRefund_fee(wxPayResultResponse.getTotal_fee());
        String tradeType = wxPayResultResponse.getTrade_type();
        if (!tradeType.equals("APP")) {
            tradeType = "PC/H5/JSAPI";
        }
        refundInfoRequest.setPay_type(tradeType);
        //重复支付进行退款处理标志
        refundInfoRequest.setRefund_type("REPEATPAY");
        BaseResponse<WxPayRefundResponse> wxPayRefund =
                wxPayProvider.wxPayRefund(refundInfoRequest);
        WxPayRefundResponse wxPayRefundResponse = wxPayRefund.getContext();
    }

    private void wxPayCallbackHandle(PayGatewayConfigResponse payGatewayConfig, WxPayResultResponse wxPayResultResponse,
                                     String businessId, List<Trade> trades, boolean isMergePay) {
        //异步回调添加交易数据
        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
        //微信支付订单号--及流水号
        payTradeRecordRequest.setTradeNo(wxPayResultResponse.getTransaction_id());
        //商户订单号或父单号
        payTradeRecordRequest.setBusinessId(businessId);
        payTradeRecordRequest.setResult_code(wxPayResultResponse.getResult_code());
        payTradeRecordRequest.setPracticalPrice(new BigDecimal(wxPayResultResponse.getTotal_fee()).
                divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        ChannelItemByGatewayRequest channelItemByGatewayRequest = new ChannelItemByGatewayRequest();
        channelItemByGatewayRequest.setGatewayName(payGatewayConfig.getPayGateway().getName());
        PayChannelItemListResponse payChannelItemListResponse =
                payQueryProvider.listChannelItemByGatewayName(channelItemByGatewayRequest).getContext();
        List<PayChannelItemVO> payChannelItemVOList =
                payChannelItemListResponse.getPayChannelItemVOList();
        String tradeType = wxPayResultResponse.getTrade_type();
        ChannelItemSaveRequest channelItemSaveRequest = new ChannelItemSaveRequest();
        String code = "wx_qr_code";
        if (tradeType.equals("APP")) {
            code = "wx_app";
        } else if (tradeType.equals("JSAPI")) {
            code = "js_api";
        } else if (tradeType.equals("MWEB")) {
            code = "wx_mweb";
        }
        channelItemSaveRequest.setCode(code);
        payChannelItemVOList.forEach(payChannelItemVO -> {
            if (channelItemSaveRequest.getCode().equals(payChannelItemVO.getCode())) {
                //更新支付项
                payTradeRecordRequest.setChannelItemId(payChannelItemVO.getId());
            }
        });
        //微信支付异步回调添加交易数据
        payProvider.wxPayCallBack(payTradeRecordRequest);
        //        //订单 支付单 操作信息
        Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.WECHAT.name())
                .account(PayGatewayEnum.WECHAT.name()).platform(Platform.THIRD).build();
        payCallbackOnline(trades, operator, isMergePay);
        //微信支付同步支付结果,失败处理
        wxOrderService.syncWxOrderPay(trades.get(0),wxPayResultResponse.getTransaction_id());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 支付回调处理，将原有逻辑迁移到order处理
     * @Date 14:56 2020/7/2
     * @Param [tradePayOnlineCallBackRequest]
     **/
    @Transactional
    @GlobalTransactional
    public void aliPayOnlineCallBack(TradePayOnlineCallBackRequest tradePayOnlineCallBackRequest) throws IOException {
        log.info("===============支付宝回调开始==============");
        GatewayConfigByGatewayRequest gatewayConfigByGatewayRequest = new GatewayConfigByGatewayRequest();
        gatewayConfigByGatewayRequest.setGatewayEnum(PayGatewayEnum.ALIPAY);
        gatewayConfigByGatewayRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        //查询支付宝配置信息
        PayGatewayConfigResponse payGatewayConfigResponse =
                payQueryProvider.getGatewayConfigByGateway(gatewayConfigByGatewayRequest).getContext();
        //支付宝公钥
        String aliPayPublicKey = payGatewayConfigResponse.getPublicKey();
        boolean signVerified = false;
        Map<String, String> params =
                JSONObject.parseObject(tradePayOnlineCallBackRequest.getAliPayCallBackResultStr(), Map.class);
        //商户订单号
        String out_trade_no = params.get("out_trade_no");
        try {
            if (Objects.equals(whiteOrder, out_trade_no)) {
                log.info("订单：{} 不走签名验证", out_trade_no);
                signVerified = true;
            } else {
                signVerified = AlipaySignature.rsaCheckV1(params, aliPayPublicKey, "UTF-8", "RSA2"); //调用SDK验证签名
            }
            log.info("{} 验证签名返回的结果为：{}" ,out_trade_no, signVerified);
        } catch (AlipayApiException e) {
            log.error("支付宝回调签名校验异常：", e);
        }

        if (signVerified) {
            try {
                //支付宝交易号
                String trade_no = params.get("trade_no");
                //交易状态
                String trade_status = params.get("trade_status");
                //订单金额
                String total_amount = params.get("total_amount");
                //支付终端类型
                String type = params.get("passback_params");

                boolean isMergePay = isMergePayOrder(out_trade_no);
                log.info("-------------支付回调,单号：{}，流水：{}，交易状态：{}，金额：{}，是否合并支付：{}------------",
                        out_trade_no, trade_no, trade_status, total_amount, isMergePay);
                String lockName;
                //非组合支付，则查出该单笔订单。
                //非组合支付，则查出该单笔订单。
                if (!isMergePay) {
                    Trade trade = new Trade();
                    if (isTailPayOrder(out_trade_no)) {
                        trade =
                                tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(out_trade_no).build()).get(0);
                    } else {
                        trade = tradeService.detail(out_trade_no);
                    }
                    // 锁资源：无论是否组合支付，都锁父单号，确保串行回调
                    lockName = trade.getParentId();
                } else {
                    lockName = out_trade_no;
                }
                Operator operator =
                        Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.ALIPAY.name())
                                .account(PayGatewayEnum.ALIPAY.name()).platform(Platform.THIRD).build();
                //redis锁，防止同一订单重复回调
                RLock rLock = redissonClient.getFairLock(lockName);
                rLock.lock();
                //执行
                try {
                    List<Trade> trades = new ArrayList<>();
                    //查询交易记录
                    TradeRecordByOrderCodeRequest tradeRecordByOrderCodeRequest =
                            new TradeRecordByOrderCodeRequest(out_trade_no);
                    PayTradeRecordResponse recordResponse =
                            payQueryProvider.getTradeRecordByOrderCode(tradeRecordByOrderCodeRequest).getContext();
                    PayCallBackResult payCallBackResult =
                            payCallBackResultService.list(PayCallBackResultQueryRequest.builder().businessId(out_trade_no).build()).get(0);
                    if (isMergePay) {
                        /*
                         * 合并支付
                         * 查询订单是否已支付或过期作废
                         */
                        trades = tradeService.detailsByParentId(out_trade_no);
                        //订单合并支付场景状态采样
                        boolean paid =
                                trades.stream().anyMatch(i -> i.getTradeState().getPayState() == PayState.PAID);
                        boolean cancel =
                                trades.stream().anyMatch(i -> i.getTradeState().getFlowState() == FlowState.VOID);
                        //订单的支付渠道。17、18、19是我们自己对接的支付宝渠道， 表：pay_channel_item
                        if (cancel || (paid && recordResponse.getChannelItemId() != 17L && recordResponse.getChannelItemId()
                                != 18L && recordResponse.getChannelItemId() != 19L)) {
                            //重复支付，直接退款
                            alipayRefundHandle(out_trade_no, total_amount);
                        } else if (payCallBackResult.getResultStatus() != PayCallBackResultStatus.SUCCESS) {
                            alipayCallbackHandle(out_trade_no, trade_no, trade_status, total_amount, type,
                                    operator, trades, true, recordResponse);
                        }
                    } else {
                        //单笔支付
                        //单笔支付
                        Trade trade = new Trade();
                        if (isTailPayOrder(out_trade_no)) {
                            trade =
                                    tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(out_trade_no).build()).get(0);
                        } else {
                            trade = tradeService.detail(out_trade_no);
                        }
                        if (trade.getTradeState().getFlowState() == FlowState.VOID || (trade.getTradeState()
                                .getPayState() == PayState.PAID && recordResponse.getChannelItemId() != 17L && recordResponse.getChannelItemId()
                                != 18L && recordResponse.getChannelItemId() != 19L)) {
                            //同一批订单重复支付或过期作废，直接退款
                            alipayRefundHandle(out_trade_no, total_amount);
                        } else if (payCallBackResult.getResultStatus() != PayCallBackResultStatus.SUCCESS) {
                            trades.add(trade);
                            alipayCallbackHandle(out_trade_no, trade_no, trade_status, total_amount, type,
                                    operator, trades, false, recordResponse);
                        }
                    }
                    payCallBackResultService.updateStatus(out_trade_no, PayCallBackResultStatus.SUCCESS);
                    // 判断订单类型是否是 全款销售 或 定金销售

//                    Trade trade = tradeService.queryAll(TradeQueryRequest.builder().tailOrderNo(out_trade_no).build()).get(0);
//                    if (trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL){
//                        log.info("ali支付回调处理======>订单号:{},订单类型:{},订单状态：{}",out_trade_no,trade.getBookingType(),trade.getTradeState().getFlowState());
//                    }else {
//                        log.info("ali支付回调处理======>推送至erp,订单号:{},订单类型:{},订单状态：{}",out_trade_no,trade.getBookingType(),trade.getTradeState().getFlowState());
//                        //推送ERP订单
//                        this.pushTradeToErp(out_trade_no);
//                    }
                    sensorsDataService.sendPaySuccessEvent(trades);

                    Trade trade = null;
                    if (isTailPayOrder(out_trade_no)) {
                        //如果是尾款订单号代表是订单需要推送到ERP系统进行发货
                        log.info("ali支付回调处理======>订单号:{} 尾款订单，同步到erp", out_trade_no);
                        this.pushTradeToErp(out_trade_no);
                    } else if (isMergePayOrder(out_trade_no)) {
                        log.error("ali支付回调处理======>订单号:{} 当前为合并支付，当前系统不支持合并支付；订单有误", out_trade_no);
                    } else {
                        trade = tradeService.detail(out_trade_no);
                        //如果是定金预付款订单不需要推送订单到ERP系统，否则全款销售则推送订单
                        if (trade.getBookingType() == BookingType.EARNEST_MONEY && trade.getTradeState().getFlowState() == FlowState.WAIT_PAY_TAIL){
                            log.info("ali支付回调处理======>订单号:{},订单类型:{},订单状态：{} 不同同步到erp",out_trade_no,trade.getBookingType(),trade.getTradeState().getFlowState());
                        }else {
                            log.info("ali支付回调处理======>推送至erp,订单号:{},订单类型:{},订单状态：{}",out_trade_no,trade.getBookingType(),trade.getTradeState().getFlowState());
                            this.pushTradeToErp(out_trade_no);
                        }
                    }

                } finally {
                    //解锁
                    rLock.unlock();
                }
            } catch (Exception e) {
                log.error("支付宝回调异常：", e);
                payCallBackResultService.updateStatus(out_trade_no, PayCallBackResultStatus.FAILED);
            }
        }
    }

    /**
     * 支付宝退款处理
     *
     * @param out_trade_no
     * @param total_amount
     */
    private void alipayRefundHandle(String out_trade_no, String total_amount) {
        //调用退款接口。直接退款。不走退款流程，没有交易对账，只记了操作日志
        AliPayRefundResponse aliPayRefundResponse =
                aliPayProvider.aliPayRefund(AliPayRefundRequest.builder().businessId(out_trade_no)
                        .amount(new BigDecimal(total_amount)).description("重复支付退款").build()).getContext();
        log.info("支付宝重复支付、超时订单退款,单号：{}", out_trade_no);
    }

    private void alipayCallbackHandle(String out_trade_no, String trade_no, String trade_status, String total_amount,
                                      String type, Operator operator, List<Trade> trades, boolean isMergePay,
                                      PayTradeRecordResponse recordResponse) {
        if (recordResponse.getApplyPrice().compareTo(new BigDecimal(total_amount)) == 0 && trade_status.equals(
                "TRADE_SUCCESS")) {
            //异步回调添加交易数据
            PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
            //流水号
            payTradeRecordRequest.setTradeNo(trade_no);
            //商品订单号
            payTradeRecordRequest.setBusinessId(out_trade_no);
            payTradeRecordRequest.setResult_code("SUCCESS");
            payTradeRecordRequest.setPracticalPrice(new BigDecimal(total_amount));
            payTradeRecordRequest.setChannelItemId(Long.valueOf(type));
            //添加交易数据（与微信共用）
            payProvider.wxPayCallBack(payTradeRecordRequest);
            payCallbackOnline(trades, operator, isMergePay);
            log.info("支付回调成功,单号：{}", out_trade_no);
        } else {
            log.error("支付回调 out_trade_no：{}, payTradeRecord.applyPrice为：{}, 支付宝回调的金额为：{} 两个不相同，不进行交易数据变更等等，为异常数据",
                    out_trade_no, recordResponse.getApplyPrice(), total_amount);
        }
    }


    /**
     * @return boolean
     * @Author lvzhenwei
     * @Description 判断是否为主订单
     * @Date 15:36 2020/7/2
     * @Param [businessId]
     **/
    private boolean isMergePayOrder(String businessId) {
        return businessId.startsWith(GeneratorService._PREFIX_PARENT_TRADE_ID);
    }

    /**
     * 订单提交成功，增加限售商品的购买记录
     *
     * @param trades todo 入限售记录
     */
    public void insertRestrictedRecord(List<Trade> trades) {
        if (CollectionUtils.isEmpty(trades)) {
            return;
        }
        Trade trade = trades.get(0);
        String customerId = trade.getBuyer().getId();
        LocalDateTime orderTime = trade.getTradeState().getCreateTime();
        List<RestrictedRecordSimpVO> restrictedRecordSimpVOS =
                trades.stream().filter(t -> !((Objects.nonNull(t.getGrouponFlag()) && t.getGrouponFlag())
                        || Objects.nonNull(t.getIsFlashSaleGoods()) && t.getIsFlashSaleGoods()
                        || (Objects.nonNull(t.getStoreBagsFlag())
                        && DefaultFlag.YES.equals(t.getStoreBagsFlag())) ||
                        (Objects.nonNull(t.getSuitMarketingFlag()) && t.getSuitMarketingFlag())))
                        .flatMap(t -> t.getTradeItems().stream()).map(tradeItem ->
                        KsBeanUtil.convert(tradeItem, RestrictedRecordSimpVO.class)
                ).collect(Collectors.toList());
        restrictedRecordSaveProvider.batchAdd(RestrictedRecordBatchAddRequest.builder()
                .restrictedRecordSimpVOS(restrictedRecordSimpVOS)
                .customerId(customerId)
                .orderTime(orderTime)
                .build());
    }

    /**
     * 根据供货商拆单并入库
     *
     * @param trade
     */
    private void splitProvideTrade(Trade trade) {

        log.info("TradeService splitProvideTrade trade:{} ", JSON.toJSONString(trade));
        List<TradeItem> tradeItemList = trade.getTradeItems();
        List<TradeItem> gifts = trade.getGifts();

        // 订单商品id集合
        List<String> goodsInfoIdList = tradeItemList.stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        List<String> goodsIdList = tradeItemList.stream().map(TradeItem::getSpuId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(gifts)) {
            goodsInfoIdList.addAll(gifts.stream().map(TradeItem::getSkuId).distinct().collect(Collectors.toList()));
            goodsIdList.addAll(gifts.stream().map(TradeItem::getSpuId).distinct().collect(Collectors.toList()));
        }

        BaseResponse<GoodsInfoListByIdsResponse> listByIdsResponse =
                goodsInfoQueryProvider.listByIds(GoodsInfoListByIdsRequest.builder().goodsInfoIds(goodsInfoIdList).build());
        BaseResponse<GoodsListByIdsResponse> goodsListResponse = goodsQueryProvider
                .listByIds(GoodsListByIdsRequest.builder().goodsIds(goodsIdList).build());
//        List<GoodsVO> goodsVOList = goodsListResponse.getContext().getGoodsVOList();
        List<GoodsInfoVO> goodsInfoVOList = listByIdsResponse.getContext().getGoodsInfos();
        tradeItemList.forEach(tradeItem -> goodsInfoVOList.forEach(goodsInfoVO -> {
            if (tradeItem.getSkuId().equals(goodsInfoVO.getGoodsInfoId())) {
                // tradeItem设置供应商id
                tradeItem.setProviderId(goodsInfoVO.getProviderId());
                // 供货价
                tradeItem.setSupplyPrice(goodsInfoVO.getSupplyPrice());
                BigDecimal supplyPrice = Objects.nonNull(goodsInfoVO.getSupplyPrice()) ?
                        goodsInfoVO.getSupplyPrice() : BigDecimal.ZERO;
                // 供货价总额
                tradeItem.setTotalSupplyPrice(supplyPrice.multiply(new BigDecimal(tradeItem.getNum())));

                tradeItem.setProviderSkuNo(goodsInfoVO.getProviderGoodsInfoNo());
            }
        }));

        log.info("TradeService splitProvideTrade tradeItemList {} ", JSON.toJSONString(tradeItemList));

        if (CollectionUtils.isNotEmpty(gifts)) {
            gifts.forEach(tradeItem -> goodsInfoVOList.forEach(goodsInfoVO -> {
                if (tradeItem.getSkuId().equals(goodsInfoVO.getGoodsInfoId())) {
                    // tradeItem设置供应商id
                    tradeItem.setProviderId(goodsInfoVO.getProviderId());
                    // 供货价
                    tradeItem.setSupplyPrice(goodsInfoVO.getSupplyPrice());
                    BigDecimal supplyPrice = Objects.nonNull(goodsInfoVO.getSupplyPrice()) ?
                            goodsInfoVO.getSupplyPrice() : BigDecimal.ZERO;
                    // 供货价总额
                    tradeItem.setTotalSupplyPrice(supplyPrice.multiply(new BigDecimal(tradeItem.getNum())));

                    tradeItem.setProviderSkuNo(goodsInfoVO.getProviderGoodsInfoNo());
                }
            }));
        }

        // 查询订单商品所属供应商id集合
        List<Long> providerIds = goodsInfoVOList.stream().filter(
                goodsInfoVO -> Objects.nonNull(goodsInfoVO.getProviderId()))
                .map(GoodsInfoVO::getProviderId).distinct().collect(Collectors.toList());

        //赠品是供应商的商品
        List<TradeItem> providerGifts = new ArrayList<>();
        List<TradeItem> otherProviderGifts = new ArrayList<>();
        //商户赠品信息
        List<TradeItem> storeGifts = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(gifts)) {
            //商户赠品
            storeGifts.addAll(gifts.stream()
                    .filter(g -> Objects.isNull(g.getProviderId()))
                    .collect(Collectors.toList()));
            //供应商赠品
            providerGifts.addAll(gifts.stream()
                    .filter(g -> Objects.nonNull(g.getProviderId()))
                    .collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(providerGifts)) {
                //赠品不属于下单商品的供应商
                otherProviderGifts.addAll(providerGifts.stream()
                        .filter(g -> !providerIds.contains(g.getProviderId()))
                        .collect(Collectors.toList()));
                //如果赠品不属于下单商品的供应商，则再另外拆单
                if (CollectionUtils.isNotEmpty(otherProviderGifts)) {
                    List<Long> otherProviderIds = otherProviderGifts.stream()
                            .map(TradeItem::getProviderId)
                            .collect(Collectors.toList());
                    providerIds.addAll(otherProviderIds);
                }
            }
        }

        // 判断是否有供应商id，有则需要根据供应商拆单
        if (CollectionUtils.isNotEmpty(providerIds)) {
            // 1. 商户自己的商品信息，单独作为一个拆单项保存
            List<TradeItem> storeItemList =
                    tradeItemList.stream().filter(tradeItem -> Objects.isNull(tradeItem.getProviderId())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(storeItemList) || CollectionUtils.isNotEmpty(storeGifts)) {
                ProviderTrade storeTrade = KsBeanUtil.convert(trade, ProviderTrade.class);
                // 用经营商户订单id作为供应商订单的父id
                storeTrade.setParentId(trade.getId());
                storeTrade.setId(generatorService.generateStoreTid());
                storeTrade.setTradeItems(storeItemList);

                // 拆单后，重新计算价格信息
                TradePrice tradePrice = storeTrade.getTradePrice();
                // 商品总价
                BigDecimal goodsPrice = BigDecimal.ZERO;
                // 订单总价:实付金额
                BigDecimal orderPrice = BigDecimal.ZERO;
                // 订单供货价总额
                BigDecimal orderSupplyPrice = BigDecimal.ZERO;
                //积分价
                Long buyPoints = NumberUtils.LONG_ZERO;

                for (TradeItem providerTradeItem : storeItemList) {
                    //积分
                    if (Objects.nonNull(providerTradeItem.getBuyPoint())) {
                        buyPoints += providerTradeItem.getBuyPoint();
                    }
                    // 商品总价
                    goodsPrice =
                            goodsPrice.add(providerTradeItem.getPrice().multiply(new BigDecimal(providerTradeItem.getNum())));
                    // 商品分摊价格
                    BigDecimal splitPrice = Objects.isNull(providerTradeItem.getSplitPrice()) ? BigDecimal.ZERO :
                            providerTradeItem.getSplitPrice();
                    // 订单总价:用分摊金额乘以数量，计算订单实际价格
                    orderPrice = orderPrice.add(splitPrice);
                    // 订单供货价总额
                    orderSupplyPrice = orderSupplyPrice.add(providerTradeItem.getTotalSupplyPrice());
                }
                // 商品总价
                tradePrice.setGoodsPrice(goodsPrice);
                tradePrice.setOriginPrice(goodsPrice);
                // 订单总价
                tradePrice.setTotalPrice(orderPrice.add(tradePrice.getDeliveryPrice()));
                tradePrice.setTotalPayCash(orderPrice);
                // 订单供货价总额
                tradePrice.setOrderSupplyPrice(orderSupplyPrice);
                //积分价
                tradePrice.setBuyPoints(buyPoints);

                storeTrade.setTradePrice(tradePrice);
                storeTrade.setThirdPlatformType(null);
                //赠品
                storeTrade.setGifts(storeGifts);
                //平摊金额
                providerTradeService.addProviderTrade(storeTrade);
            }

            // 查询供货商店铺信息
            BaseResponse<ListNoDeleteStoreByIdsResponse> storesResposne =
                    storeQueryProvider.listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder().storeIds(providerIds).build());
            List<StoreVO> storeVOList = storesResposne.getContext().getStoreVOList();

            // 2. 根据供货商id拆单
            providerIds.forEach(providerId -> {
                ProviderTrade providerTrade = KsBeanUtil.convert(trade, ProviderTrade.class);

                // 用经营商户订单id作为供应商订单的父id
                providerTrade.setParentId(trade.getId());
                providerTrade.setId(generatorService.generateProviderTid());
                // 筛选当前供应商的订单商品信息
                List<TradeItem> providerTradeItems =
                        tradeItemList.stream().filter(tradeItem -> providerId.equals(tradeItem.getProviderId())).collect(Collectors.toList());
                log.info("TradeService splitProvideTrade providerId:{} tradeItem:{}", providerId, JSON.toJSONString(providerTradeItems));
                providerTrade.setTradeItems(providerTradeItems);
                // 原订单所属商家名称
                providerTrade.setSupplierName(trade.getSupplier().getSupplierName());
                // 原订单所属商家编号
                providerTrade.setSupplierCode(trade.getSupplier().getSupplierCode());
                // 原订单所属商户id
                providerTrade.setStoreId(trade.getSupplier().getStoreId());
                Supplier supplier = providerTrade.getSupplier();

                // 供应商信息
                StoreVO provider =
                        storeVOList.stream().filter(store -> store.getStoreId().equals(providerId)).findFirst().get();
                // 保存供应商店铺信息
                supplier.setStoreId(provider.getStoreId());
                supplier.setSupplierName(provider.getSupplierName());
                supplier.setSupplierId(provider.getCompanyInfo().getCompanyInfoId());
                supplier.setSupplierCode(provider.getCompanyInfo().getCompanyCode());
                // 使用的运费模板类别(0:店铺运费,1:单品运费)
                supplier.setFreightTemplateType(provider.getFreightTemplateType());
                // providerTrade中supplier对象更新为供应商信息
                providerTrade.setSupplier(supplier);

                // 拆单后，重新计算价格信息
                TradePrice tradePrice = providerTrade.getTradePrice();
                // 商品总价
                BigDecimal goodsPrice = BigDecimal.ZERO;
                // 订单总价:实付金额
                BigDecimal orderPrice = BigDecimal.ZERO;
                // 订单供货价总额
                BigDecimal orderSupplyPrice = BigDecimal.ZERO;
                //积分价
                Long buyPoints = NumberUtils.LONG_ZERO;
                for (TradeItem providerTradeItem : providerTradeItems) {
                    log.info("TradeService.splitProvideTrade providerTradeItem packId:{}", providerTradeItem.getPackId());
                    if (!OrderType.POINTS_ORDER.equals(trade.getOrderType())) {
                        //积分
                        if (Objects.nonNull(providerTradeItem.getBuyPoint())) {
                            buyPoints += providerTradeItem.getBuyPoint();
                        }
                        // 商品总价
                        goodsPrice =
                                goodsPrice.add(providerTradeItem.getPrice().multiply(new BigDecimal(providerTradeItem.getNum())));
                        // 商品分摊价格
                        BigDecimal splitPrice = Objects.isNull(providerTradeItem.getSplitPrice()) ? BigDecimal.ZERO :
                                providerTradeItem.getSplitPrice();
                        orderPrice = orderPrice.add(splitPrice);
                    }

                    //todo 增加ERP商品SKU编码和SPU编码add by wugongjiang
                    Optional<String> erpSkuNoOptional = goodsInfoVOList.stream()
                            .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(providerTradeItem.getSkuId()) && StringUtils.isNotBlank(goodsInfoVO.getErpGoodsInfoNo()))
                            .map(GoodsInfoVO::getErpGoodsInfoNo)
                            .findFirst();
                    Optional<String> erpSpuOptional = goodsInfoVOList.stream()
                            .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(providerTradeItem.getSkuId()))
                            .map(GoodsInfoVO::getErpGoodsNo)
                            .findFirst();
                    if (erpSkuNoOptional.isPresent() && erpSpuOptional.isPresent()){
                        providerTradeItem.setErpSkuNo(erpSkuNoOptional.get());
                        providerTradeItem.setErpSpuNo(erpSpuOptional.get());
                    }

                    if (erpSkuNoOptional.isPresent()){
                        providerTradeItem.setErpSkuNo(erpSkuNoOptional.get());
                    }


                    if (erpSpuOptional.isPresent()){
                        providerTradeItem.setErpSpuNo(erpSpuOptional.get());
                    }

                    //设置是否组合商品
                    goodsInfoVOList.forEach(goodsInfoVO -> {
                        if (Objects.equals(goodsInfoVO.getGoodsInfoId(),providerTradeItem.getSkuId())   && Objects.nonNull(goodsInfoVO.getCombinedCommodity()) && goodsInfoVO.getCombinedCommodity()) {
                            providerTradeItem.setCombinedCommodity(goodsInfoVO.getCombinedCommodity());
                        }
                    });

                    log.info("================设置是否组合商品：{}==============",providerTradeItem);

                    // 订单供货价总额
                    orderSupplyPrice = orderSupplyPrice.add(providerTradeItem.getTotalSupplyPrice());
                    // 供应商名称
                    providerTradeItem.setProviderName(provider.getSupplierName());
                    // 供应商编号
                    providerTradeItem.setProviderCode(provider.getCompanyInfo().getCompanyCode());
                }


                List<TradeItem> pGifts = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(providerGifts)) {
                    pGifts = providerGifts.stream()
                            .filter(g -> providerId.equals(g.getProviderId()))
                            .collect(Collectors.toList());
                    for (TradeItem pgift : pGifts) {
                        // 供应商名称
                        pgift.setProviderName(provider.getSupplierName());
                        // 供应商编号
                        pgift.setProviderCode(provider.getCompanyInfo().getCompanyCode());
                        //供货价
                        orderSupplyPrice = orderSupplyPrice.add(pgift.getTotalSupplyPrice());

                        //todo 增加ERP商品SKU编码和SPU编码add by wugongjiang
                        Optional<String> erpSkuNoOptional = goodsInfoVOList.stream()
                                .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(pgift.getSkuId()) && StringUtils.isNotBlank(goodsInfoVO.getErpGoodsInfoNo()))
                                .map(GoodsInfoVO::getErpGoodsInfoNo)
                                .findFirst();
                        Optional<String> erpSpuOptional = goodsInfoVOList.stream()
                                .filter(goodsInfoVO -> goodsInfoVO.getGoodsInfoId().equals(pgift.getSkuId()))
                                .map(GoodsInfoVO::getErpGoodsNo)
                                .findFirst();
                        if (erpSkuNoOptional.isPresent()){
                            pgift.setErpSkuNo(erpSkuNoOptional.get());
                        }

                        if (erpSpuOptional.isPresent()){
                            pgift.setErpSpuNo(erpSpuOptional.get());
                        }

                        //设置是否组合商品
                        goodsInfoVOList.forEach(goodsInfoVO -> {
                            if (Objects.equals(goodsInfoVO.getGoodsInfoId(),pgift.getSkuId()) && Objects.nonNull(goodsInfoVO.getCombinedCommodity()) && goodsInfoVO.getCombinedCommodity()) {
                                pgift.setCombinedCommodity(goodsInfoVO.getCombinedCommodity());
                            }
                        });

                        log.info("================设置是否组合商品：{}==============",pgift);

                    }
                }

                // 商品总价
                tradePrice.setGoodsPrice(goodsPrice);
                tradePrice.setOriginPrice(goodsPrice);
                // 订单总价
                tradePrice.setTotalPrice(orderPrice);
                tradePrice.setTotalPayCash(orderPrice);
                // 订单供货价总额
                tradePrice.setOrderSupplyPrice(orderSupplyPrice);
                //积分价
                tradePrice.setBuyPoints(buyPoints);
                tradePrice.setDeliveryPrice(BigDecimal.ZERO);
                //实际金额
                if(providerTradeItems.stream().anyMatch(p->p.getSplitPrice()!=null)){
                    tradePrice.setActualPrice(providerTradeItems.stream().map(p -> Objects.isNull(p.getSplitPrice()) ? new BigDecimal("0") : p.getSplitPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
                if(providerTradeItems.stream().anyMatch(p->p.getPoints()!=null)){
                    tradePrice.setActualPoints(providerTradeItems.stream().map(p->Objects.isNull(p.getPointsPrice()) ? new BigDecimal("0") : p.getPointsPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
                if(providerTradeItems.stream().anyMatch(p->p.getKnowledge()!=null)){
                    tradePrice.setActualKnowledge(providerTradeItems.stream().mapToLong(p->Objects.isNull(p.getKnowledge()) ? 0L : p.getKnowledge()).sum());
                }
                //复制运费过来
                tradePrice.setSplitDeliveryPrice(trade.getTradePrice().getSplitDeliveryPrice());
                //运费
                if(tradePrice.getSplitDeliveryPrice()!=null && !tradePrice.getSplitDeliveryPrice().isEmpty() && tradePrice.getSplitDeliveryPrice().containsKey(providerId)){
                    tradePrice.setDeliveryPrice(tradePrice.getSplitDeliveryPrice().get(providerId));

                    Map<Long, BigDecimal> splitDeliverPriceMap = new HashMap<>();
                    splitDeliverPriceMap.put(providerId, tradePrice.getDeliveryPrice());
                    tradePrice.setSplitDeliveryPrice(splitDeliverPriceMap);

                }

                providerTrade.setTradePrice(tradePrice);
                //赠品
                providerTrade.setGifts(pGifts);

                //linkedMall供应商
                if (CompanySourceType.LINKED_MALL.equals(provider.getCompanySourceType())) {
                    providerTrade.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                } else {
                    providerTrade.setThirdPlatformType(null);
                }

                providerTradeService.addProviderTrade(providerTrade);
                //linkedMall供应商
                if (CompanySourceType.LINKED_MALL.equals(provider.getCompanySourceType())) {
                    linkedMallTradeService.splitTrade(providerTrade);
                }
            });
        }
    }


    /**
     * 补充子单
     * @param oid
     * @param userId
     */
    public void addProviderTrade(String oid, String userId) {
        logger.info("TradeService addProviderTrade 补充子单，开始 oid:{} userId:{}", oid, userId);
        // 根据供货商拆单并入库
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(oid);
        if (!CollectionUtils.isEmpty(providerTradeList)) {
            logger.info("订单：{} 存在 providerTrade数据", oid);
            return;
        }

        List<Trade> trades = tradeService.getTradeList(TradeQueryRequest.builder().id(oid).build().getWhereCriteria());
        List<TradeCommitResult> resultList = new ArrayList<>();
        for (Trade trade : trades) {
            try{
                this.splitProvideTrade(trade);
                if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods()
                        && Objects.nonNull(trade.getBookingType()) && trade.getBookingType() == BookingType.EARNEST_MONEY) {
                    resultList.add(new TradeCommitResult(trade.getId(),
                            trade.getParentId(), trade.getTradeState(),
                            trade.getPaymentOrder(), trade.getTradePrice().getEarnestPrice(),
                            trade.getOrderTimeOut(), trade.getSupplier().getStoreName(),
                            trade.getSupplier().getIsSelf()));
                } else {
                    resultList.add(new TradeCommitResult(trade.getId(),
                            trade.getParentId(), trade.getTradeState(),
                            trade.getPaymentOrder(), trade.getTradePrice().getTotalPrice(),
                            trade.getOrderTimeOut(), trade.getSupplier().getStoreName(),
                            trade.getSupplier().getIsSelf()));
                }
            } catch (Exception e) {
                log.error("myself commit trade error,trade={}，错误信息：{}", trade, e);
                if (e instanceof SbcRuntimeException) {
                    throw e;
                } else {
                    throw new SbcRuntimeException("K-020010");
                }
            }
        }


        // 平台优惠券
        List<CouponCodeBatchModifyDTO> dtoList = new ArrayList<>();
//        if (tradeGroup != null) {
//            // 2.修改优惠券状态
//            TradeCouponVO tradeCoupon = tradeGroup.getCommonCoupon();
//            dtoList.add(CouponCodeBatchModifyDTO.builder()
//                    .couponCodeId(tradeCoupon.getCouponCodeId())
//                    .orderCode(null)
//                    .customerId(userId)
//                    .useStatus(DefaultFlag.YES).build());
//        }
        // 店铺优惠券
        // 批量修改优惠券状态
        trades.forEach(trade -> {
            if (trade.getTradeCoupon() != null) {
                TradeCouponVO tradeCoupon = trade.getTradeCoupon();
                dtoList.add(CouponCodeBatchModifyDTO.builder()
                        .couponCodeId(tradeCoupon.getCouponCodeId())
                        .orderCode(trade.getId())
                        .customerId(trade.getBuyer().getId())
                        .useStatus(DefaultFlag.YES).build());
            }
        });
        if (dtoList.size() > 0) {
            logger.info("TradeService addProviderTrade dtoList: {}", JSON.toJSONString(dtoList));
            couponCodeProvider.batchModify(CouponCodeBatchModifyRequest.builder().modifyDTOList(dtoList).build());
        }

        trades.stream().filter(trade -> Objects.nonNull(trade.getTradePrice()) &&
                Objects.nonNull(trade.getTradePrice().getPoints()) && trade.getTradePrice().getPoints() > 0).forEach(trade -> {
            // 增加客户积分明细 扣除积分
            customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                    .customerId(trade.getBuyer().getId())
                    .type(OperateType.DEDUCT)
                    .serviceType(PointsServiceType.ORDER_DEDUCTION)
                    .points(trade.getTradePrice().getPoints())
                    .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                    .build());
        });

        trades.stream().filter(trade -> !AuditState.REJECTED.equals(trade.getTradeState().getAuditState())).forEach(trade -> {
            MessageMQRequest messageMQRequest = new MessageMQRequest();
            Map<String, Object> map = new HashMap<>();
            map.put("type", NodeType.ORDER_PROGRESS_RATE.toValue());
            map.put("id", trade.getId());
            if (AuditState.CHECKED.equals(trade.getTradeState().getAuditState())) {
                messageMQRequest.setNodeCode(OrderProcessType.ORDER_COMMIT_SUCCESS.getType());
                map.put("node", OrderProcessType.ORDER_COMMIT_SUCCESS.toValue());
            } else {
                messageMQRequest.setNodeCode(OrderProcessType.ORDER_COMMIT_SUCCESS_CHECK.getType());
                map.put("node", OrderProcessType.ORDER_COMMIT_SUCCESS_CHECK.toValue());
            }
            messageMQRequest.setNodeType(NodeType.ORDER_PROGRESS_RATE.toValue());
            messageMQRequest.setParams(Lists.newArrayList(trade.getTradeItems().get(0).getSkuName()));
            messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
            messageMQRequest.setRouteParam(map);
            messageMQRequest.setCustomerId(trade.getBuyer().getId());
            messageMQRequest.setMobile(trade.getBuyer().getAccount());
            orderProducerService.sendMessage(messageMQRequest);
        });

        logger.info("TradeService addProviderTrade 补充子单，结束 oid:{} userId:{}", oid, userId);
    }


    /**
     * 补单 根据trade 生成 payOrder
     * @param oid
     */
    public void addFixPayOrder(String oid) {
        log.info("addPayOrder generatePayOrderByOrderCode oid:{} begin running", oid);
        List<Trade> trades = tradeService.getTradeList(TradeQueryRequest.builder().id(oid).build().getWhereCriteria());
        Optional<PayOrder> payOrderByOrderCode = payOrderService.findPayOrderByOrderCode(oid);
        if (payOrderByOrderCode.isPresent()) {
            log.info("addPayOrder generatePayOrderByOrderCode oid:{} 存在不进行创建操作", oid);
            log.info("addPayOrder generatePayOrderByOrderCode oid: {} 提前结束 end running", oid);
            return;
        }
        for (Trade trade : trades) {
            //创建支付单

            PayOrder payOrder = new PayOrder();
            BaseResponse<CustomerDetailGetCustomerIdResponse> response = tradeCacheService.getCustomerDetailByCustomerId(trade.getBuyer().getId());
            CustomerDetailVO customerDetail = response.getContext();
            payOrder.setPayOrderId(trade.getPayOrderId());

            payOrder.setCustomerDetailId(customerDetail.getCustomerDetailId());
            payOrder.setOrderCode(trade.getId());
            payOrder.setUpdateTime(LocalDateTime.now());
            payOrder.setCreateTime(trade.getTradeState().getCreateTime());
            payOrder.setDelFlag(DeleteFlag.NO);
            payOrder.setCompanyInfoId(trade.getSupplier().getSupplierId());
            payOrder.setPayOrderNo(generatorService.generateOid());
            BigDecimal payPrice = Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY
                    && StringUtils.isEmpty(trade.getTailOrderNo()) ?
                    trade.getTradePrice().getEarnestPrice() : trade.getTradePrice().getTotalPrice();
            if (payPrice == null) {
                payOrder.setPayOrderStatus(PayOrderStatus.PAYED);
            } else {
                payOrder.setPayOrderStatus(PayOrderStatus.NOTPAY);
            }
            payOrder.setPayOrderPrice(payPrice);
            payOrder.setPayOrderPoints(trade.getTradePrice().getPoints());
            payOrder.setPayOrderKnowledge(trade.getTradePrice().getKnowledge());
            payOrder.setPayType(PayType.valueOf(trade.getPayInfo().getPayTypeName()));
//            if (OrderType.POINTS_ORDER.equals(trade.getOrderType())) {
//                payOrderRepository.saveAndFlush(payOrder);
//                // 积分订单生成收款单
//                Receivable receivable = new Receivable();
//                receivable.setPayOrderId(payOrder.getPayOrderId());
//                receivable.setReceivableNo(generatorService.generateSid());
//                receivable.setPayChannel("积分支付");
//                receivable.setPayChannelId(Constants.DEFAULT_RECEIVABLE_ACCOUNT);
//                receivable.setCreateTime(trade.getTradeState().getCreateTime());
//                receivable.setDelFlag(DeleteFlag.NO);
//                receivableRepository.save(receivable);
//            }
//            payOrderRepository.save()
//            payOrderRepository.saveAndFlush(payOrder);
            payOrderRepository.insertPayOrderCustomer(payOrder.getPayOrderId(), payOrder.getPayOrderNo(), payOrder.getOrderCode(), payOrder.getPayOrderStatus().toValue()+"",
                    payOrder.getPayType().toValue(), payOrder.getCustomerDetailId(), payOrder.getCreateTime(), payOrder.getUpdateTime(), payOrder.getDelFlag().toValue(),
                    payOrder.getPayOrderPrice(), payOrder.getCompanyInfoId(), payOrder.getPayOrderPoints(), payOrder.getPayOrderKnowledge());
            log.info("addPayOrder generatePayOrderByOrderCode optional ");
        }
        log.info("addPayOrder generatePayOrderByOrderCode oid: {} end running", oid);
    }

    /**
     * 线上订单支付回调
     * 订单 支付单 操作信息
     *
     * @return 操作结果
     */
    private void payCallbackOnline(List<Trade> trades, Operator operator, boolean isMergePay) {
        List<PayCallBackOnlineBatch> payCallBackOnlineBatchList = trades.stream().map(trade -> {
            //每笔订单做是否合并支付标识
            trade.getPayInfo().setMergePay(isMergePay);
            tradeService.updateTrade(trade);
            if (Objects.nonNull(trade.getIsBookingSaleGoods()) && trade.getIsBookingSaleGoods() && trade.getBookingType() == BookingType.EARNEST_MONEY &&
                    StringUtils.isNotEmpty(trade.getTailOrderNo()) && StringUtils.isNotEmpty(trade.getTailPayOrderId())) {
                //支付单信息
                PayOrder payOrder = tradeService.findPayOrder(trade.getTailPayOrderId());
                PayCallBackOnlineBatch backOnlineBatch = new PayCallBackOnlineBatch();
                backOnlineBatch.setTrade(trade);
                backOnlineBatch.setPayOrderOld(payOrder);
                return backOnlineBatch;
            } else {
                //支付单信息
                PayOrder payOrder = tradeService.findPayOrder(trade.getPayOrderId());
                PayCallBackOnlineBatch backOnlineBatch = new PayCallBackOnlineBatch();
                backOnlineBatch.setTrade(trade);
                backOnlineBatch.setPayOrderOld(payOrder);
                return backOnlineBatch;
            }
        }).collect(Collectors.toList());
        tradeService.payCallBackOnlineBatch(payCallBackOnlineBatchList, operator);

        // 订单支付回调同步供应商订单状态
        //this.providerTradePayCallBack(trades);
    }

    /**
     * 订单支付回调同步供应商订单状态
     *
     * @param trades
     */
    private void providerTradePayCallBack(List<Trade> trades) {
        if (CollectionUtils.isNotEmpty(trades)) {
            trades.forEach(parentTradeVO -> {
                String parentId = parentTradeVO.getId();
                List<ProviderTrade> tradeList =
                        providerTradeService.findListByParentId(parentId);
                // 父订单号对应的子订单的买家信息应该是相同的
                if (CollectionUtils.isNotEmpty(tradeList)) {
                    ProviderTrade trade = tradeList.get(0);
                    final Buyer buyer = trade.getBuyer();
                    //统一设置账号加密后的买家信息
                    List<TradeVO> tradeVOList = tradeList.stream().map(i -> {
                        i.setBuyer(buyer);
                        return KsBeanUtil.convert(i, TradeVO.class);
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tradeVOList)) {
                        tradeVOList.forEach(childTradeVO -> {
                            childTradeVO.getTradeState().setPayState(PayState.PAID);
                            TradeUpdateRequest tradeUpdateRequest =
                                    new TradeUpdateRequest(KsBeanUtil.convert(childTradeVO, TradeUpdateDTO.class));
                            providerTradeService.updateProviderTrade(tradeUpdateRequest);
                        });
                    }
                }
            });
        }
    }

    /**
     * 更新订单的错误标训
     *
     * @param tradeId   订单号
     * @param errorFlag 错误标识
     */
    @Transactional
    public void updateThirdPlatformPay(String tradeId, Boolean errorFlag) {
        mongoTemplate.updateMulti(new Query(Criteria.where("id").is(tradeId)), new Update().set(
                "thirdPlatformPayErrorFlag", errorFlag), Trade.class);
    }

    /**
     * 订单分页(优化版)
     *
     * @param whereCriteria 条件
     * @param request       参数
     * @return
     */
    public MicroServicePage<TradeVO> pageOptimize(Criteria whereCriteria, TradeQueryRequest request) {
        long totalSize = this.countNum(whereCriteria, request);
        if (totalSize < 1) {
            return new MicroServicePage<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        request.putSort(request.getSortColumn(), request.getSortRole());
        Query query = new Query(whereCriteria);
        List<Trade> trades = mongoTemplate.find(query.with(request.getPageRequest()), Trade.class);
        ConfigVO config = tradeCacheService.getTradeConfigByType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        final boolean flag = config.getStatus() == 1;
        int days = JSONObject.parseObject(config.getContext()).getInteger("day");
        List<ProviderTrade> listByParentIdList =
                providerTradeService.findListByParentIdList(trades.parallelStream().map(Trade::getId).collect(Collectors.toList()));
        return new MicroServicePage<>(trades.stream().map(trade -> {
            TradeState tradeState = trade.getTradeState();
            boolean canReturnFlag =
                    tradeState.getFlowState() == FlowState.COMPLETED || (tradeState.getPayState() == PayState.PAID
                            && tradeState.getDeliverStatus() == DeliverStatus.NOT_YET_SHIPPED && tradeState
                            .getFlowState() != FlowState.VOID);
            canReturnFlag = isCanReturnTime(flag, days, tradeState, canReturnFlag);
            //开店礼包不支持退货退款
            canReturnFlag = canReturnFlag && DefaultFlag.NO == trade.getStoreBagsFlag();
            if (trade.getCycleBuyFlag()) {
                trade.setCanReturnFlag(Boolean.TRUE);
            }else {
                trade.setCanReturnFlag(canReturnFlag);
            }


            TradeVO tradeVo = tradeMapper.tradeToTradeVo(trade);
            if (Objects.nonNull(tradeVo.getIsBookingSaleGoods()) && tradeVo.getIsBookingSaleGoods() &&
                    tradeVo.getBookingType() == BookingType.EARNEST_MONEY &&
                    tradeVo.getTradeState().getPayState() == PayState.NOT_PAID) {
                tradeVo.getTradePrice().setTotalPrice(tradeVo.getTradePrice().getEarnestPrice());
            }
            if (Objects.nonNull(tradeVo.getIsBookingSaleGoods()) && tradeVo.getIsBookingSaleGoods()
                    && tradeVo.getBookingType() == BookingType.EARNEST_MONEY && tradeVo.getTradeState().getPayState() == PayState.PAID_EARNEST) {
                tradeVo.getTradePrice().setTotalPrice(tradeVo.getTradePrice().getTailPrice());
            }
            if (CollectionUtils.isNotEmpty(listByParentIdList)) {
                tradeVo.setTradeVOList(listByParentIdList.stream().filter(vo -> StringUtils.equals(vo.getParentId(),
                        tradeVo.getId())).map(vo -> tradeMapper.providerTradeToTradeVo(vo)).collect(Collectors.toList()));
            } else {
                tradeVo.setTradeVOList(Lists.newArrayList());
            }
            if (CollectionUtils.isNotEmpty(tradeVo.getTags())) {
                tradeVo.setGiftFlag(tradeVo.getTags().contains(OrderTagEnum.GIFT.getCode()));
            }
            return tradeVo;
        }).collect(Collectors.toList()), request
                .getPageable(), totalSize);
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
    private boolean isCanReturnTime(boolean flag, int days, TradeState tradeState, boolean canReturnFlag) {
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
     * 周期购信息组装
     *
     * @param trade
     */
    public void dealCycleBuy(Trade trade,TradeParams tradeParams) {
        if (trade.getCycleBuyFlag()) {
            CycleBuyInfoDTO cycleBuyInfo= tradeParams.getCycleBuyInfo();
            TradeCycleBuyInfo tradeCycleBuyInfo = KsBeanUtil.convert(cycleBuyInfo,TradeCycleBuyInfo.class);
            String rule = tradeCycleBuyInfo.getCycleBuySendDateRule().getSendDateRule();

            //生成发货日历
            LocalDate date = LocalDate.now();
            List<DeliverCalendar> calendarList = new ArrayList<>();
            for (int i = 0; i < tradeCycleBuyInfo.getCycleNum(); i++) {
                LocalDate deliverDate = cycleBuyDeliverTimeService.getLatestDeliverTime(date, tradeCycleBuyInfo.getDeliveryCycle(), rule);
                DeliverCalendar deliverCalendar = new DeliverCalendar();
                deliverCalendar.setCycleDeliverStatus(CycleDeliverStatus.NOT_SHIPPED);
                deliverCalendar.setDeliverDate(deliverDate);
                calendarList.add(deliverCalendar);
                date = deliverDate;
            }
            tradeCycleBuyInfo.setDeliverCalendar(calendarList);
            trade.setTradeCycleBuyInfo(tradeCycleBuyInfo);
        }
    }

    /**
     * 周期购赠品填充数量
     *
     * @param trade
     * @param tradeParams
     */
    public void dealCycleBuyGifts(Trade trade, TradeParams tradeParams) {
        CycleBuyInfoDTO cycleBuyInfo = tradeParams.getCycleBuyInfo();
        //赠品填充数量
        TradeItem item = trade.getTradeItems().get(NumberUtils.INTEGER_ZERO);
        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider
                .getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(item.getSpuId()).build())
                .getContext().getCycleBuyVO();
        Map<String, Long> giftsMap = cycleBuyVO.getCycleBuyGiftVOList().stream().collect(Collectors.toMap(CycleBuyGiftVO::getGoodsInfoId, CycleBuyGiftVO::getFreeQuantity));
        //筛选出周期购的赠品
        trade.getGifts().stream().filter(tradeItem -> cycleBuyInfo.getCycleBuyGifts().contains(tradeItem.getSkuId())).forEach(tradeItem -> {
            tradeItem.setNum(giftsMap.get(tradeItem.getSkuId()));
        });
        trade.setGifts(giftNumCheck(trade.getGifts()));
    }

    /**
     * 计算周期购运费
     *
     * @return
     */
    public BigDecimal dealCycleBuyDeliverPrice(String goodsId, Integer cycleNum, BigDecimal deliveryPrice, BigDecimal giftFreight) {
        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider
                .getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsId).build())
                .getContext().getCycleBuyVO();


        boolean freeFlag = cycleNum.compareTo(cycleBuyVO.getCycleFreightType()) >= 0;

        if (NumberUtils.INTEGER_ZERO.equals(cycleBuyVO.getCycleFreightType()) || !freeFlag) {
            //每期运费x期数(主商品) + 赠品单次运费
            return deliveryPrice.multiply(new BigDecimal(cycleNum)).add(giftFreight);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 处理加价购加购商品
     *
     * @param tradeItemGroups
     * @param goodsInfoVOList
     */
    public void wrapperMarkup(List<TradeItemGroup> tradeItemGroups, List<GoodsInfoVO> goodsInfoVOList,boolean forceCommit) {
        List<String> skuIds = tradeItemGroups.stream()
                .filter(tradeItemGroup -> CollectionUtils.isNotEmpty(tradeItemGroup.getTradeMarketingList()))
                .flatMap(tradeItemGroup -> tradeItemGroup.getTradeMarketingList().stream())
                .filter(tradeMarketingDTO -> CollectionUtils.isNotEmpty(tradeMarketingDTO.getMarkupSkuIds()))
                .flatMap(tradeMarketingDTO -> tradeMarketingDTO.getMarkupSkuIds().stream())
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuIds)) {
            return;
        }
        TradeGetGoodsResponse goodsResponse = this.getGoodsResponse(skuIds);
        Map<String, GoodsVO> goodsMap = goodsResponse.getGoodses().stream().collect(Collectors.toMap
                (GoodsVO::getGoodsId, Function.identity()));
        Map<String, GoodsInfoVO> goodsInfoMap = goodsResponse.getGoodsInfos().stream().collect(Collectors.toMap
                (GoodsInfoVO::getGoodsInfoId, Function.identity()));
        tradeItemGroups.stream()
                .forEach(g -> {
                    // 加价购商品信息填充
                    List<Long> marketingIds =
                            g.getTradeMarketingList().parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                                    .map(r -> r.getMarketingId()).distinct().collect(Collectors.toList());
                    MarkupListRequest markupListRequest = MarkupListRequest.builder().marketingId(marketingIds).build();
                    List<MarkupLevelVO> levelList = markupQueryProvider.getMarkupList(markupListRequest).getContext().getLevelList();

                    Map<String, List<MarkupLevelVO>> listMap = levelList.stream().collect(Collectors.groupingBy(l -> "" + l.getMarkupId() + l.getId()));
                    List<TradeItem> markupItem = new ArrayList<>();
                    // 通过加价购id+阶梯id+加购商品sku定位 换购价格
                    List<TradeItem> finalMarkupItem = markupItem;
                    g.getTradeMarketingList().parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
                            .forEach(m -> {
                                List<MarkupLevelVO> markupLevelVOS = listMap.get("" + m.getMarketingId() + m.getMarketingLevelId());
                                if (CollectionUtils.isNotEmpty(markupLevelVOS)) {
                                    m.getMarkupSkuIds().stream().forEach(sku -> {
                                        markupLevelVOS.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
                                                .filter(detailVO -> sku.equals(detailVO.getGoodsInfoId()))
                                                .forEach(detail -> {
                                                    TradeItem itemDTO = TradeItem.builder()
                                                            .price(detail.getMarkupPrice())
                                                            .originalPrice(detail.getMarkupPrice())
                                                            .splitPrice(detail.getMarkupPrice())
                                                            .levelPrice(detail.getMarkupPrice())
                                                            .skuId(detail.getGoodsInfoId())
                                                            .num(1L)
                                                            .isMarkupGoods(Boolean.TRUE)
                                                            .build();
                                                    GoodsInfoVO goodsInfo = goodsInfoMap.get(itemDTO.getSkuId());
                                                    GoodsVO goods = goodsMap.get(goodsInfo.getGoodsId());
                                                    // 校验删除，上下架状态
                                                    if (goodsInfo.getDelFlag().equals(DeleteFlag.YES) || goodsInfo.getAddedFlag().equals(0)
                                                            || goods.getAuditStatus().equals(CheckStatus.FORBADE)) {
                                                        //  强制提交订单 去掉多余换购商品
                                                        if(forceCommit){
                                                            return;
                                                        }
                                                        throw new SbcRuntimeException("K-050117");
                                                    }
                                                    verifyService.merge(itemDTO, goodsInfo, goods, goodsInfo.getStoreId());
                                                    finalMarkupItem.add(itemDTO);
                                                });
                                    });

                                }
                            });
                    g.getTradeItems().addAll(markupItem);

                });

    }

    /**
     * 推送订单
     * @param tradeNo
     */
    public void pushTradeToErp(String tradeNo){
        //根据父订单号,查询子订单集合
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(tradeNo);
//        List<ProviderTrade> providerTradeListin = providerTradeService.findListByParentIdList(Arrays.asList(tradeNo));
        Trade trade = tradeRepository.findById(tradeNo).get();
        if (CollectionUtils.isNotEmpty(providerTradeList)){
            providerTradeList.stream().forEach(providerTrade -> {
                providerTrade.setPayWay(trade.getPayWay());
                if (!providerTrade.getGrouponFlag()
                        ||  GrouponOrderStatus.COMPLETE.equals(trade.getTradeGroupon().getGrouponOrderStatus())) {
                    log.info(" TradeService.pushTradeToErp push order: {}", tradeNo);
                    providerTradeService.singlePushOrder(providerTrade);
                }
            });
        }
    }



    /**
     * 查询对账数据
     */
    public TradeAccountRecordResponse getTradeAccountRecord(TradeAccountRecordRequest recordRequest) {

        log.info("===========查询开始时间：{}，结束时间：{},storeId={}===============",recordRequest.getBeginTime(),recordRequest.getEndTime(),recordRequest.getCompanyInfoId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FMT_TIME_1);
        LocalDateTime beginTime = LocalDateTime.parse(recordRequest.getBeginTime(),formatter);
        LocalDateTime endTime = LocalDateTime.parse(recordRequest.getEndTime(),formatter);


        log.info("===========查询开始时间：{}，结束时间：{},storeId={}===============",beginTime,endTime,recordRequest.getCompanyInfoId());

        List<Criteria> criterias = new ArrayList<>();
        //criterias.add(Criteria.where("supplier.storeId").is(recordRequest.getCompanyInfoId()));
        criterias.add(Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()));
        criterias.add(Criteria.where("tradeState.flowState").ne(FlowState.VOID.getStateId()));
        criterias.add(Criteria.where("tradeState.payTime").gte(beginTime));
        criterias.add(Criteria.where("tradeState.payTime").lt(endTime));
        criterias.add(Criteria.where("yzTid").exists(false));
        criterias.add(Criteria.where("tradePrice.points").exists(true));

        Criteria newCriteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        Query query = new Query(newCriteria);

        List<Trade> trades = mongoTemplate.find(query, Trade.class);

        log.info("===========查询查询条件：{}===============",query);

        List<TradePrice> tradePrices=trades.stream().map(Trade::getTradePrice).collect(Collectors.toList());

        //计算积分的总和
        Long points=tradePrices.stream().mapToLong(TradePrice::getPoints).sum();

        //计算积分金额的总和
        BigDecimal pointsPrice =tradePrices.stream().map(TradePrice::getPointsPrice).reduce(BigDecimal.ZERO,BigDecimal::add);

        log.info("===========查询对账积分：{}===============",points);
        log.info("===========查询对账积分金额：{}===============",pointsPrice);

        return TradeAccountRecordResponse.builder().points(points).pointsPrice(pointsPrice).build();
    }


    /**
     * s视频号微信支付回调，
     * @param tid
     */
    @Transactional
    @GlobalTransactional
    public void wxPayCallBack(String tid,String transationId){
        try {
            log.info("-------------微信支付成功回调,tid：{},transationId:{}------------", tid,transationId);
            Trade trade = tradeService.detail(tid);
            if(trade == null || !Objects.equals(trade.getChannelType(),ChannelType.MINIAPP)){
                return;
            }
            // 锁资源：无论是否组合支付，都锁父单号，确保串行回调
            String lockName = trade.getParentId();
            //redis锁，防止同一订单重复回调
            RLock rLock = redissonClient.getFairLock(lockName);
            rLock.lock();
            //执行回调
            try {
                List<Trade> trades =new ArrayList<>();
                trades.add(trade);
                if (trade.getTradeState().getFlowState() == FlowState.VOID || (trade.getTradeState().getPayState() == PayState.PAID)) {
                    //同一批订单重复支付或过期作废，直接退款
                    //wxRefundHandle(wxPayResultResponse, businessId, -1L);
                } else {
                    wxNewPayCallbackHandle(transationId, tid, trades);
                }
                //支付回调处理成功
                //payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.SUCCESS);
                wxOrderService.orderReportCache(trade.getId());
                sensorsDataService.sendPaySuccessEvent(trades);

                log.info("TradeService wxPayCallBack 微信支付异步通知回调end---------");
            } catch (Exception e) {
                log.error("TradeService wxPayCallBack 微信支付异步通知回调 异常---------", e);
                //支付处理结果回写回执支付结果表
                // payCallBackResultService.updateStatus(businessId, PayCallBackResultStatus.FAILED);
            } finally {
                //解锁
                rLock.unlock();
            }
        } catch (Exception ex) {
            //失败回执表更新
            log.error("TradeService wxPayCallBack 微信支付异步通知回调异常" ,ex);
        }

    }

    /**
     * 视频号支付回调
     * @param businessId
     * @param trades
     */
    private void wxNewPayCallbackHandle(String transactionId,String businessId, List<Trade> trades) {
        //异步回调添加交易数据
        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
        //微信支付订单号--及流水号
        payTradeRecordRequest.setTradeNo(transactionId);
        //商户订单号或父单号
        payTradeRecordRequest.setBusinessId(businessId);
        payTradeRecordRequest.setResult_code("success");
        payTradeRecordRequest.setPracticalPrice(trades.get(0).getTradePrice().getTotalPrice());
        ChannelItemByGatewayRequest channelItemByGatewayRequest = new ChannelItemByGatewayRequest();
        channelItemByGatewayRequest.setGatewayName(PayGatewayEnum.WECHAT);
        PayChannelItemListResponse payChannelItemListResponse =
                payQueryProvider.listChannelItemByGatewayName(channelItemByGatewayRequest).getContext();
        List<PayChannelItemVO> payChannelItemVOList =
                payChannelItemListResponse.getPayChannelItemVOList();
        ChannelItemSaveRequest channelItemSaveRequest = new ChannelItemSaveRequest();
        channelItemSaveRequest.setCode("wx_app");
        payChannelItemVOList.forEach(payChannelItemVO -> {
            if (channelItemSaveRequest.getCode().equals(payChannelItemVO.getCode())) {
                //更新支付项
                payTradeRecordRequest.setChannelItemId(payChannelItemVO.getId());
            }
        });
        //微信支付异步回调添加交易数据
        payProvider.wxPayCallBack(payTradeRecordRequest);
        //        //订单 支付单 操作信息
        Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name(PayGatewayEnum.WECHAT.name())
                .account(PayGatewayEnum.WECHAT.name()).platform(Platform.THIRD).build();
        payCallbackOnline(trades, operator, false);
    }

    /**
     * 更新发票的类型
     * @param autoUpdateInvoiceRequest
     * @return
     */
    @GlobalTransactional
    public BaseResponse updateInvoice(AutoUpdateInvoiceRequest autoUpdateInvoiceRequest) {
        mongoTemplate.updateMulti(new Query(Criteria.where("_id").is(autoUpdateInvoiceRequest.getTradeId())),
                new Update().set("invoice.type", InvoiceType.ELECTRONIC.toValue()), Trade.class);
        Optional<OrderInvoice> byOrderNo = orderInvoiceService.findByOrderNo(autoUpdateInvoiceRequest.getTradeId());
        if(byOrderNo.isPresent()){
            OrderInvoice orderInvoice = byOrderNo.get();
            if(InvoiceState.WAIT.equals(orderInvoice.getInvoiceState())) {
                orderInvoiceService.updateOrderInvoiceState(Lists.newArrayList(orderInvoice.getOrderInvoiceId()));
            }
            log.info("orderInvoice tradeNo:{}, origin state:{}",autoUpdateInvoiceRequest.getTradeId(), orderInvoice.getInvoiceState());
        }else{
            OrderInvoiceSaveRequest saveRequest = new OrderInvoiceSaveRequest();
            saveRequest.setInvoiceType(InvoiceType.ELECTRONIC);
            saveRequest.setOrderNo(autoUpdateInvoiceRequest.getTradeId());
            orderInvoiceService.generateOrderInvoice(saveRequest,"system", InvoiceState.ALREADY);
        }
        return BaseResponse.SUCCESSFUL();
    }
}