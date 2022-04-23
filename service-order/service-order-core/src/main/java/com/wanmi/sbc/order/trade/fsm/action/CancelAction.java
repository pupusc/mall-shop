package com.wanmi.sbc.order.trade.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointCancelRequest;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import com.wanmi.sbc.order.growthvalue.repository.OrderGrowthValueTempRepository;
import com.wanmi.sbc.order.growthvalue.service.OrderGrowthValueTempService;
import com.wanmi.sbc.order.trade.fsm.TradeAction;
import com.wanmi.sbc.order.trade.fsm.TradeStateContext;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.TradeEventLog;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroupon;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/4/21.
 */
@Component
public class CancelAction extends TradeAction {

    @Autowired
    private OrderGrowthValueTempService orderGrowthValueTempService;

    @Autowired
    private OrderGrowthValueTempRepository orderGrowthValueTempRepository;

    @Autowired
    private CustomerPointsDetailQueryProvider customerPointsDetailQueryProvider;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private ExternalProvider externalProvider;

    @Override
    protected void evaluateInternal(Trade trade, StateRequest request, TradeStateContext tsc) {
        Operator operator = tsc.getOperator();
        TradeState tradeState = trade.getTradeState();
        // 调用此作废订单action的行为共有4种，分别为取消订单、审核驳回、仅退款、全部退货或退款
        // 此处返还订单积分需要排除全部退货(已完成)和仅退款(已支付未发货) 变成 已作废的这2种情况
        if (!(tradeState.getPayState().equals(PayState.PAID))) {
            // 全额返还订单使用积分
            /*    BaseResponse<CustomerPointsDetailResponse> response = customerPointsDetailQueryProvider.getOne(
                    CustomerPointsDetailQueryRequest.builder()
                            .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                            .build());
            Long points = response.getContext().getCustomerPointsDetailVO().getPoints();
            if (points != null && points > 0) {
                customerPointsDetailSaveProvider.returnPoints(CustomerPointsDetailAddRequest.builder()
                        .customerId(trade.getBuyer().getId())
                        .type(OperateType.GROWTH)
                        .serviceType(PointsServiceType.ORDER_CANCEL_BACK)
                        .points(points)
                        .content(JSONObject.toJSONString(Collections.singletonMap("orderNo", trade.getId())))
                        .build());
            }*/
            if (StringUtils.isNotEmpty(trade.getDeductCode())){

                try {
                    //TODO 这里应该做的是 添加到表里面，异步去处理积分或者知豆信息
                    FanDengPointCancelRequest cancelRequest =
                            FanDengPointCancelRequest.builder().deductCode(trade.getDeductCode())
                                    .desc("订单取消返还(退单号:"+trade.getId()+")").build();
                    if (trade.getTradePrice().getPoints() != null && trade.getTradePrice().getPoints() > 0) {
                        externalProvider.pointCancel(cancelRequest);
                    }
                    if (trade.getTradePrice().getKnowledge() != null && trade.getTradePrice().getKnowledge() > 0) {
                        externalProvider.knowledgeCancel(cancelRequest);
                    }
                } catch (Exception ex) {
                    logger.error("CancelAction pointCancel or knowledgeCandel execute Exception", ex);
                }
            }
        }

        tradeState.setEndTime(LocalDateTime.now());
        tradeState.setFlowState(FlowState.VOID);
        tradeState.setObsoleteReason(Objects.toString(request.getData(), ""));
        String detail = String.format("[%s]作废了订单%s", operator.getName(), trade.getId());
        trade.appendTradeEventLog(TradeEventLog
                .builder()
                .operator(operator)
                .eventTime(LocalDateTime.now())
                .eventType(FlowState.VOID.getDescription())
                .eventDetail(detail)
                .build());
        Boolean grouponFlag = trade.getGrouponFlag();
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        //拼团订单
        if (Objects.nonNull(grouponFlag) && Boolean.TRUE.equals(grouponFlag) && Objects.nonNull(tradeGroupon)
                && (PayState.NOT_PAID == tradeState.getPayState() || PayState.PAID_EARNEST == tradeState.getPayState())) {
            tradeGroupon.setGrouponOrderStatus(GrouponOrderStatus.FAIL);
            tradeGroupon.setFailTime(LocalDateTime.now());
        }
        save(trade);
        //同步子订单
//        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(trade.getId());
//        for (ProviderTrade providerTrade : providerTradeList){
//            if(providerTrade.getTradeState()!=null){
//                providerTrade.getTradeState().setFlowState(FlowState.VOID);
//                providerTrade.getTradeState().setEndTime(LocalDateTime.now());
//                saveProviderTrade(providerTrade);
//            }
//        }
        super.operationLogMq.convertAndSend(operator, FlowState.VOID.getDescription(), detail);

        // 删除订单成长值临时表中的数据
        List<OrderGrowthValueTemp> result = orderGrowthValueTempService.list(
                OrderGrowthValueTempQueryRequest.builder().orderNo(trade.getId()).build());
        if (CollectionUtils.isNotEmpty(result)) {
            orderGrowthValueTempRepository.deleteAll(result);
        }

    }
}
