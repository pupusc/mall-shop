package com.wanmi.sbc.order.returnorder.fsm.action;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRefundRequest;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateListRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 退款操作
 * Created by jinwei on 22/4/2017.
 */
@Component
public class RefundReturnAction extends ReturnAction {
    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private GoodsTobeEvaluateSaveProvider goodsTobeEvaluateSaveProvider;

    @Autowired
    private GoodsTobeEvaluateQueryProvider goodsTobeEvaluateQueryProvider;

    @Autowired
    private StoreTobeEvaluateSaveProvider storeTobeEvaluateSaveProvider;

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;

    @Autowired
    private ExternalProvider externalProvider;
    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.COMPLETED);
        returnOrder.getReturnPrice().setActualReturnPrice(rsc.findRequestData());
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REFUND.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format("退单[%s]已退款，退单完成,操作人:%s", returnOrder.getId(), operator.getName()))
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrder.setFinishTime(LocalDateTime.now());
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.REFUND.getDesc(), eventLog.getEventDetail());

        Long points = Objects.nonNull(returnOrder.getReturnPoints()) ? returnOrder.getReturnPoints().getApplyPoints() : null;
        if (points != null && points > 0) {
    /*        customerPointsDetailSaveProvider.returnPoints(CustomerPointsDetailAddRequest.builder()
                    .customerId(returnOrder.getBuyer().getId())
                    .type(OperateType.GROWTH)
                    .serviceType(PointsServiceType.RETURN_ORDER_BACK)
                    .points(points)
                    .content(JSONObject.toJSONString(Collections.singletonMap("returnOrderNo", returnOrder.getId())))
                    .build());*/
            FanDengPointRefundRequest refundRequest = FanDengPointRefundRequest.builder()
                    .point(points).userNo(returnOrder.getFanDengUserNo()).sourceId(returnOrder.getId()).sourceType(1)
                    .desc("退单返还(退单号:"+returnOrder.getId()+")").build();
            externalProvider.pointRefund(refundRequest);
        }
        delEvaluate(returnOrder);
    }

    /**
     * @Author lvzhenwei
     * @Description 订单退货完成删除对应的订单以及商品待评价数据
     * @Date 11:28 2019/4/11
     * @Param [returnOrder]
     **/
    private void delEvaluate(ReturnOrder returnOrder) {
        String tid = returnOrder.getTid();
        returnOrder.getReturnItems().forEach(returnItem -> {
            //判断退单商品是否全部退完，如果已经退完，则删除对应的商品待评论数据
            List<String> goodsTobeEvaluateIds;
            if (returnItem.getCanReturnNum() == returnItem.getNum()) {
                GoodsTobeEvaluateListRequest goodsTobeEvaluateListReq = new GoodsTobeEvaluateListRequest();
                goodsTobeEvaluateListReq.setOrderNo(tid);
                goodsTobeEvaluateListReq.setGoodsInfoId(returnItem.getSkuId());
                goodsTobeEvaluateIds = goodsTobeEvaluateQueryProvider.list(goodsTobeEvaluateListReq).getContext().getGoodsTobeEvaluateVOList()
                        .stream().map(GoodsTobeEvaluateVO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(goodsTobeEvaluateIds)) {
                    GoodsTobeEvaluateDelByIdListRequest goodsTobeEvaluateDelByIdListRequest = new GoodsTobeEvaluateDelByIdListRequest();
                    goodsTobeEvaluateDelByIdListRequest.setIdList(goodsTobeEvaluateIds);
                    goodsTobeEvaluateSaveProvider.deleteByIdList(goodsTobeEvaluateDelByIdListRequest);
                }
            }
        });
        //如果订单全部退完，则删除对应订单店铺服务待评价数据
        if (returnOrderService.isReturnFull(returnOrder)) {
            StoreTobeEvaluateListRequest storeTobeEvaluateListReq = new StoreTobeEvaluateListRequest();
            storeTobeEvaluateListReq.setOrderNo(tid);
            List<String> storeTobeEvaluateIds;
            storeTobeEvaluateIds = storeTobeEvaluateQueryProvider.list(storeTobeEvaluateListReq).getContext().getStoreTobeEvaluateVOList()
                    .stream().map(StoreTobeEvaluateVO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(storeTobeEvaluateIds)) {
                StoreTobeEvaluateDelByIdListRequest storeTobeEvaluateDelByIdListRequest = new StoreTobeEvaluateDelByIdListRequest();
                storeTobeEvaluateDelByIdListRequest.setIdList(storeTobeEvaluateIds);
                storeTobeEvaluateSaveProvider.deleteByIdList(storeTobeEvaluateDelByIdListRequest);
            }
        }
    }
}
