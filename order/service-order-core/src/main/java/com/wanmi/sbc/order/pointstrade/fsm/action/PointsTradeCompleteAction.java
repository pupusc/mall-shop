package com.wanmi.sbc.order.pointstrade.fsm.action;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSalesModifyRequest;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.common.OrderCommonService;
import com.wanmi.sbc.order.common.PointsGoodsSalesNumMq;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeAction;
import com.wanmi.sbc.order.pointstrade.fsm.PointsTradeStateContext;
import com.wanmi.sbc.order.pointstrade.fsm.params.PointsTradeStateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author lvzhenwei
 * @Description 积分订单状态机已完成订单Action
 * @Date 11:06 2019/5/29
 * @Param
 * @return
 **/
@Component
public class PointsTradeCompleteAction extends PointsTradeAction {

    @Autowired
    private PointsGoodsSalesNumMq pointsGoodsSalesNumMq;


    @Autowired
    private OrderCommonService orderCommonService;

    @Autowired
    private ProviderTradeService providerTradeService;
    /**
     * 已收货
     *
     * @param trade
     * @param request
     * @param tsc
     */
    @Override
    protected void evaluateInternal(Trade trade, PointsTradeStateRequest request, PointsTradeStateContext tsc) {
        trade = (Trade) request.getData();

        TradeState tradeState = trade.getTradeState();

        if (!tradeState.getPayState().equals(PayState.PAID)) {
            throw new SbcRuntimeException("K-050103");
        }

        String detail = String.format("积分订单%s已操作完成", trade.getId());
        if (tradeState.getFlowState() == FlowState.VOID) {
            detail = String.format("作废退款单，积分订单%s状态扭转为已完成", trade.getId());
        } else {
            trade.getTradeState().setEndTime(LocalDateTime.now());
        }
        tradeState.setFlowState(FlowState.COMPLETED);

        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(tsc.getOperator())
                .eventType(FlowState.COMPLETED.getDescription())
                .eventDetail(detail)
                .eventTime(LocalDateTime.now())
                .build());

        LocalDateTime finalTime=orderCommonService.queryReturnTime();
        //订单可入账时间（订单可退时间依据）--状态流转时修改trade信息
        trade.getTradeState().setFinalTime(finalTime);

        save(trade);

        //同步子订单
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(trade.getId());
        for (ProviderTrade providerTrade : providerTradeList){
            if(providerTrade.getTradeState()!=null){
                providerTrade.getTradeState().setFlowState(FlowState.COMPLETED);
                providerTrade.getTradeState().setEndTime(LocalDateTime.now());
                providerTradeService.updateProviderTrade(providerTrade);
            }
        }
        //积分订单完成增加销量
        trade.getTradeItems().forEach(tradeItem -> {
            pointsGoodsSalesNumMq.updatePointsGoodsSalesNumMq(
                    PointsGoodsSalesModifyRequest.builder().pointsGoodsId(tradeItem.getPointsGoodsId()).salesNum(tradeItem.getNum()).build());
        });
        super.operationLogMq.convertAndSend(tsc.getOperator(), FlowState.COMPLETED.getDescription(), detail);
    }



}
