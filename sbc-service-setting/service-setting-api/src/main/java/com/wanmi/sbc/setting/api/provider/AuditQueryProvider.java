package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.setting.name}", contextId = "AuditQueryProvider")
public interface AuditQueryProvider {

    /**
     * 自营商品是否需审核
     *
     * @return 自营商品是否需审核 {@link BossGoodsAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-boss-goods-audit")
    BaseResponse<BossGoodsAuditResponse> isBossGoodsAudit();

    /**
     * 商家商品是否需审核
     *
     * @return 商家商品是否需审核 {@link SupplierGoodsAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-supplier-goods-audit")
    BaseResponse<SupplierGoodsAuditResponse> isSupplierGoodsAudit();

    /**
     * 商家订单是否需审核
     *
     * @return 商家订单是否需审核 {@link SupplierOrderAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-supplier-order-audit")
    BaseResponse<SupplierOrderAuditResponse> isSupplierOrderAudit();

    /**
     * 查询审核开关状态
     *
     * @return 审核开关列表 {@link AuditConfigListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/list-audit-config")
    BaseResponse<AuditConfigListResponse> listAuditConfig();

    /**
     * 客户审核是否需审核
     *
     * @return 客户审核是否需审核 {@link CustomerAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-customer-audit")
    BaseResponse<CustomerAuditResponse> isCustomerAudit();

    /**
     * 增专资质审核开关
     *
     * @return 增专资质审核开关 {@link TicketAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-ticket-audit")
    BaseResponse<TicketAuditResponse> isTicketAudit();

    /**
     * 客户信息完善开关
     *
     * @return 客户信息完善开关 {@link CustomerInfoPerfectResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-perfect-customer-info")
    BaseResponse<CustomerInfoPerfectResponse> isPerfectCustomerInfo();

    /**
     * 查询增专资质配置
     *
     * @return 增专资质配置 {@link InvoiceConfigGetResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/get-invoice-config")
    BaseResponse<InvoiceConfigGetResponse> getInvoiceConfig();

    /**
     * 根据type查询configList
     *
     * @return 订单设置列表 {@link TradeConfigListResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/list-trade-config")
    BaseResponse<TradeConfigListResponse> listTradeConfig();

    /**
     * 根据type获取订单设置
     *
     * @param request {@link TradeConfigGetByTypeRequest}
     * @return 订单设置 {@link TradeConfigGetByTypeResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/get-trade-config-by-type")
    BaseResponse<TradeConfigGetByTypeResponse> getTradeConfigByType(@RequestBody TradeConfigGetByTypeRequest request);

    /**
     * 查询订单代发货自动收货配置
     *
     * @return 订单代发货自动收货配置 {@link OrderAutoReceiveConfigGetResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/get-order-auto-receive-config")
    BaseResponse<OrderAutoReceiveConfigGetResponse> getOrderAutoReceiveConfig();

    /**
     * 访问是否需要登录
     *
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-visit-withLogin")
    BaseResponse<UserAuditResponse> getIsVisitWithLogin();

    /**
     * 查询PC端商品列表展示默认设置
     *
     * @return {@link GoodsDisplayConfigGetResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/pc/get-goodsdisplay-config")
    BaseResponse<GoodsDisplayConfigGetResponse> getGoodsDisplayConfigForPc();

    /**
     * 查询mobile端商品列表展示默认设置
     *
     * @return {@link GoodsDisplayConfigGetResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/mobile/get-goodsdisplay-config")
    BaseResponse<GoodsDisplayConfigGetResponse> getGoodsDisplayConfigForMobile();

    /**
     * 根据设置的键查询配置
     *
     * @return {@link GoodsDisplayConfigGetResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/get-system-config")
    BaseResponse<AuditConfigListResponse> getByConfigKey(@RequestBody @Valid ConfigQueryRequest configQueryRequest);

    /**
     * 自营商品是否需审核
     *
     * @return 自营商品是否需审核 {@link BossGoodsAuditResponse}
     */
    @PostMapping("/setting/${application.setting.version}/audit/is-goods-evaluate")
    BaseResponse<BossGoodsEvaluateResponse> isGoodsEvaluate();
}
