package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.trade.TradeSettingProvider;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoAuditRequest;
import com.wanmi.sbc.order.api.request.trade.ReturnOrderModifyAutoReceiveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeSettingModifyRequest;
import com.wanmi.sbc.order.follow.request.TradeSettingRequest;
import com.wanmi.sbc.order.trade.service.TradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-04
 */
@Validated
@RestController
public class TradeSettingController implements TradeSettingProvider {

    @Autowired
    private TradeSettingService tradeSettingService;

    /**
     * @param tradeSettingModifyRequest {@link TradeSettingModifyRequest}
     * @return
     */
    @Override
    public BaseResponse modifyTradeConfigs(@RequestBody @Valid TradeSettingModifyRequest tradeSettingModifyRequest) {
        tradeSettingService.updateTradeConfigs(KsBeanUtil.convertList(tradeSettingModifyRequest.getTradeSettingRequests(), TradeSettingRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param returnOrderModifyAutoAuditRequest {@link ReturnOrderModifyAutoAuditRequest}
     * @return
     */
    @Override
    public BaseResponse modifyReturnOrderAutoAudit(@RequestBody @Valid ReturnOrderModifyAutoAuditRequest returnOrderModifyAutoAuditRequest) {
        tradeSettingService.returnOrderAutoAudit(returnOrderModifyAutoAuditRequest.getDay());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param returnOrderModifyAutoReceiveRequest {@link ReturnOrderModifyAutoReceiveRequest}
     * @return
     */
    @Override
    public BaseResponse modifyReturnOrderAutoReceive(@RequestBody @Valid ReturnOrderModifyAutoReceiveRequest returnOrderModifyAutoReceiveRequest) {
        tradeSettingService.returnOrderAutoReceive(returnOrderModifyAutoReceiveRequest.getDay());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse orderAutoReceive() {
        tradeSettingService.orderAutoReceive();
        return BaseResponse.SUCCESSFUL();
    }
}
