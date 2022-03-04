package com.wanmi.sbc.order.provider.impl.open;

import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.open.OpenDeliverProvider;
import com.wanmi.sbc.order.api.request.open.OrderDeliverInfoReqBO;
import com.wanmi.sbc.order.api.response.open.DeliverResBO;
import com.wanmi.sbc.order.api.response.open.OrderDeliverInfoResBO;
import com.wanmi.sbc.order.bean.enums.OutTradePlatEnum;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.open.TradeDeliverService;
import com.wanmi.sbc.order.open.model.OrderDeliverInfoResDTO;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author Liang Jun
 * @date 2022-02-21 20:12:00
 */
@Slf4j
@RestController
public class OpenDeliverController implements OpenDeliverProvider {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeDeliverService tradeDeliverService;
    @Autowired
    public OrderProducerService orderProducerService;

    @Override
    public BusinessResponse<OrderDeliverInfoResBO> deliverInfo(OrderDeliverInfoReqBO param) {
        Trade trade = tradeService.detail(param.getOrderNo());
        if (Objects.isNull(trade)) {
            return BusinessResponse.error(CommonErrorCode.DATA_NOT_EXISTS);
        }
        if (!OutTradePlatEnum.FDDS_PERFORM.getCode().equals(trade.getOutTradePlat())) {
            return BusinessResponse.error(CommonErrorCode.DATA_NOT_EXISTS);
        }

        OrderDeliverInfoResDTO deliverInfo = tradeDeliverService.buildOrderDeliverInfo(trade);
        OrderDeliverInfoResBO resultBO = new OrderDeliverInfoResBO();
        resultBO.setTradeNo(deliverInfo.getTradeNo());
        resultBO.setOutTradeNo(deliverInfo.getOutTradeNo());
        resultBO.setOrderStatus(deliverInfo.getOrderStatus());
        resultBO.setDeliverStatus(deliverInfo.getDeliverStatus());

        if (!CollectionUtils.isEmpty(deliverInfo.getDelivers())) {
            resultBO.setDelivers(KsBeanUtil.convert(deliverInfo.getDelivers(), DeliverResBO.class));
        }
        return BusinessResponse.success(resultBO);
    }

    @Override
    public BusinessResponse<Object> deliverSync(Map<String, Object> param) {
        log.info("订单发货状态消息同步开始....");
        if (Objects.isNull(param) || Objects.isNull(param.get("orderNo"))) {
            log.warn("订单编号为空, 同步结束");
            return BusinessResponse.error(CommonErrorCode.PARAMETER_ERROR, "参数错误");
        }


        Object orderNo = param.get("orderNo");
        Trade trade = tradeService.detail(orderNo.toString());
        if (Objects.isNull(trade)) {
            log.warn("订单信息不存在，同步结束");
            return BusinessResponse.error(CommonErrorCode.PARAMETER_ERROR, "订单信息不存在");
        }
        orderProducerService.sendMQForOrderDelivered(trade);

        log.info("订单发货状态消息同步完成!!!");
        return BusinessResponse.success();
    }
}
