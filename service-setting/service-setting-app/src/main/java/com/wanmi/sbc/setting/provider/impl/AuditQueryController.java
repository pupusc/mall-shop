package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.*;
import com.wanmi.sbc.setting.audit.AuditService;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AuditQueryController implements AuditQueryProvider {
    @Autowired
    private AuditService auditService;

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public BaseResponse<BossGoodsAuditResponse> isBossGoodsAudit() {
        BossGoodsAuditResponse response = new BossGoodsAuditResponse();
        response.setAudit(auditService.isBossGoodsAudit());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<SupplierGoodsAuditResponse> isSupplierGoodsAudit() {
        SupplierGoodsAuditResponse response = new SupplierGoodsAuditResponse();
        response.setAudit(auditService.isSupplierGoodsAudit());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<SupplierOrderAuditResponse> isSupplierOrderAudit() {
        SupplierOrderAuditResponse response = new SupplierOrderAuditResponse();
        response.setAudit(auditService.isSupplierOrderAudit());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<AuditConfigListResponse> listAuditConfig() {
        AuditConfigListResponse response = new AuditConfigListResponse();
        response.setConfigVOList(auditService.listAuditConfigs());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerAuditResponse> isCustomerAudit() {
        CustomerAuditResponse response = new CustomerAuditResponse();
        response.setAudit(auditService.isCustomerAudit());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<TicketAuditResponse> isTicketAudit() {
        TicketAuditResponse response = new TicketAuditResponse();
        response.setAduit(auditService.isTicketAudit());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerInfoPerfectResponse> isPerfectCustomerInfo() {
        CustomerInfoPerfectResponse response = new CustomerInfoPerfectResponse();
        response.setPerfect(auditService.isPerfectCustomerInfo());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<InvoiceConfigGetResponse> getInvoiceConfig() {
        InvoiceConfigGetResponse response = new InvoiceConfigGetResponse();

        Config config = auditService.getInvoiceConfig();

        KsBeanUtil.copyPropertiesThird(config, response);

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<TradeConfigListResponse> listTradeConfig() {
        TradeConfigListResponse response = new TradeConfigListResponse();

        List<Config> configList = auditService.listTradeConfig();

        KsBeanUtil.copyList(configList, response.getConfigVOList());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<TradeConfigGetByTypeResponse> getTradeConfigByType(@RequestBody @Valid TradeConfigGetByTypeRequest request) {
        TradeConfigGetByTypeResponse response = new TradeConfigGetByTypeResponse();
        Config config = auditService.getTradeConfigByType(request.getConfigType());
        if(config != null) {
            response = configMapper.ConfigToTradeConfigGetByTypeResponse(config);
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<OrderAutoReceiveConfigGetResponse> getOrderAutoReceiveConfig() {
            OrderAutoReceiveConfigGetResponse response = new OrderAutoReceiveConfigGetResponse();

        Config config = auditService.getOrderAutoReceiveConfig();

        KsBeanUtil.copyPropertiesThird(config, response);

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<UserAuditResponse> getIsVisitWithLogin() {
        UserAuditResponse response = new UserAuditResponse();
        response.setAudit(auditService.isUserAudit());
        return BaseResponse.success(response);
    }

    /**
     * 查询pc端商品列表默认展示维度详情
     *
     * @return
     */
    @Override
    public BaseResponse<GoodsDisplayConfigGetResponse> getGoodsDisplayConfigForPc() {
        GoodsDisplayConfigGetResponse response = auditService.getGoodsDisplay(0);
        return BaseResponse.success(response);
    }

    /**
     * 查询移动端商品列表默认展示维度详情
     *
     * @return
     */
    @Override
    public BaseResponse<GoodsDisplayConfigGetResponse> getGoodsDisplayConfigForMobile() {
        GoodsDisplayConfigGetResponse response = auditService.getGoodsDisplay(1);
        return BaseResponse.success(response);
    }

    /**
     * 根据键查询配置
     *
     * @param configQueryRequest
     * @return
     */
    @Override
    public BaseResponse<AuditConfigListResponse> getByConfigKey(@RequestBody @Valid ConfigQueryRequest configQueryRequest) {
        AuditConfigListResponse response = new AuditConfigListResponse();
        List<Config> configList = auditService.findByConfigKey(configQueryRequest.getConfigKey());
        KsBeanUtil.copyList(configList, response.getConfigVOList());
        return BaseResponse.success(response);
    }

    /**
     * 是否开启商品审核
     *
     * @return true:开启 false:不开启
     */
    @Override
    public BaseResponse<BossGoodsEvaluateResponse> isGoodsEvaluate() {
        BossGoodsEvaluateResponse response = new BossGoodsEvaluateResponse();
        response.setEvaluate(auditService.isGoodsEvaluate());

        return BaseResponse.success(response);
    }
}
