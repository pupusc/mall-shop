package com.wanmi.sbc.order.open;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.order.open.model.DeliverResDTO;
import com.wanmi.sbc.order.open.model.OrderDeliverInfoResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 订单发货
 * @date 2022-02-20 18:00:00
 */
@Service
@Slf4j
public class TradeDeliverService {

    /**
     * 订单已发货
     */
    public OrderDeliverInfoResDTO delivered(String orderId) {
        if (Objects.isNull(orderId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        OrderDeliverInfoResDTO resultVO = new OrderDeliverInfoResDTO();
        resultVO.setTradeNo(trade.getId());
        resultVO.setOutTradeNo(!CollectionUtils.isEmpty(trade.getOutOrderIds()) ? trade.getOutOrderIds().get(0) : null);
        resultVO.setOrderStatus(Objects.nonNull(trade.getTradeState()) && Objects.nonNull(trade.getTradeState().getFlowState())
                ? trade.getTradeState().getFlowState().getStateId() : "UNKNOW");
        resultVO.setDeliverStatus(Objects.nonNull(trade.getTradeState()) && Objects.nonNull(trade.getTradeState().getDeliverStatus())
                ? trade.getTradeState().getDeliverStatus().getStatusId() : "UNKNOW");
        //发货单
        if (!CollectionUtils.isEmpty(trade.getTradeDelivers())) {
            List<DeliverResDTO> deliverRessDTOS = trade.getTradeDelivers().stream().map(item -> {
                DeliverResDTO deliverResDTO = new DeliverResDTO();
                deliverResDTO.setDeliverId(item.getDeliverId());
                //收货人信息
                fillConsinee(deliverResDTO, item.getConsignee());
                //物流信息
                fillLogistics(deliverResDTO, item.getLogistics());
                //发货清单
                fillDeliverItems(deliverResDTO, item.getShippingItems());
                return deliverResDTO;
            }).collect(Collectors.toList());
            resultVO.setDelivers(deliverRessDTOS);
        }
    }
}
