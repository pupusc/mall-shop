package com.wanmi.sbc.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.employee.EmployeeHandoverRequest;
import com.wanmi.sbc.goods.api.provider.restrictedrecord.RestrictedRecordSaveProvider;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordBatchAddRequest;
import com.wanmi.sbc.goods.bean.vo.RestrictedRecordSimpVO;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.request.trade.TradeBackRestrictedRequest;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import com.wanmi.sbc.order.exceptionoftradepoints.service.ExceptionOfTradePointsService;
import com.wanmi.sbc.order.open.TradeDeliverService;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.request.ReturnQueryRequest;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.returnorder.service.ThirdPlatformReturnOrderService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@EnableBinding(OrderSink.class)
public class OrderConsumerService {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private RestrictedRecordSaveProvider restrictedRecordSaveProvider;

    @Autowired
    private ThirdPlatformReturnOrderService thirdPlatformReturnOrderService;

    @Autowired
    private ExceptionOfTradePointsService exceptionOfTradePointsService;

    @Autowired
    private TradeDeliverService tradeDeliverService;

    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_MODIFY_EMPLOYEE_DATA)
    public void modifyEmployeeData(EmployeeHandoverRequest request){
        Integer pageNum = 0;
        Integer pageSize = 1000;
        while(true){
            TradeQueryRequest tradeQueryRequest = TradeQueryRequest.builder().employeeIds(request.getEmployeeIds()).build();
            tradeQueryRequest.setPageNum(pageNum);
            tradeQueryRequest.setPageSize(pageSize);
            Page<Trade> page = tradeService.page(tradeQueryRequest.getWhereCriteria(), tradeQueryRequest);
            if(page.getTotalElements() == 0){
                break;
            }
            List<Trade> tradeList = page.getContent();
            tradeList.stream().forEach(trade -> {
                tradeService.updateEmployeeId(request.getNewEmployeeId(), trade.getBuyer().getId());
            });
            log.info("业务员交接订单数量：" + tradeList.size());
            pageNum ++;
        }

        while(true){
            ReturnQueryRequest returnQueryRequest = new ReturnQueryRequest();
            returnQueryRequest.setEmployeeIds(request.getEmployeeIds());
            Page<ReturnOrder> page = returnOrderService.page(returnQueryRequest);
            if(page.getTotalElements() == 0){
                break;
            }
            List<ReturnOrder> returnOrders = page.getContent();
            returnOrders.stream().forEach(returnOrder -> {
                returnOrderService.updateEmployeeId(request.getNewEmployeeId(), returnOrder.getBuyer().getId());
            });
            log.info("业务员交接订单数量：" + returnOrders.size());
            pageNum ++;
        }

    }

    /**
     * 返还限售数量
     * @param restrictedRequest
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_REDUCE_RESTRICTED_PURCHASE_NUM)
    public void backRestrictedPurchaseNum(TradeBackRestrictedRequest restrictedRequest){
        log.info("======================= 1. 退还限售库存开始 ==================");
        //直接退款和审批驳回的的流程
        if(StringUtils.isNotBlank(restrictedRequest.getTradeId())){
            Trade trade = tradeService.detail(restrictedRequest.getTradeId());
            //订单为拼团，秒杀类不做处理
            if(this.checkBackRestrictedNum(trade)){
                log.info("======================= 1.1. 秒杀和拼团的订单不做处理 ==================");
                return ;
            }
            List<RestrictedRecordSimpVO> simpVOS = KsBeanUtil.convert(trade.getTradeItems(),RestrictedRecordSimpVO.class);
            log.info("======================= 2. 退还会员：{} ///// 限售的数据 ：{} ==================", trade.getBuyer().getId(), simpVOS);
            //获取订单的Item信息，会员的信息
            restrictedRecordSaveProvider.reduceRestrictedRecord(RestrictedRecordBatchAddRequest.builder()
                    .restrictedRecordSimpVOS(simpVOS)
                    .customerId(trade.getBuyer().getId())
                    .orderTime(trade.getTradeState().getCreateTime())
                    .build());
        }
        //退单流程
        if(StringUtils.isNotBlank(restrictedRequest.getBackOrderId())){
            ReturnOrder returnOrder = returnOrderService.findById(restrictedRequest.getBackOrderId());
            if(returnOrder == null) {return;}
            Trade trade = tradeService.detail(returnOrder.getTid());
            if(this.checkBackRestrictedNum(trade)){
                log.info("======================= 1.1. 秒杀和拼团的订单不做处理 ==================");
                return ;
            }
            List<RestrictedRecordSimpVO> restrictedRecordSimpVOS = KsBeanUtil.convert(returnOrder.getReturnItems(),RestrictedRecordSimpVO.class);
            log.info("======================= 2. 退还会员：{} ///// 限售的数据 ：{} ==================", trade.getBuyer().getId(), restrictedRecordSimpVOS);
            //获取退单的Item信息，会员的信息
            restrictedRecordSaveProvider.reduceRestrictedRecord(RestrictedRecordBatchAddRequest.builder()
                    .restrictedRecordSimpVOS(restrictedRecordSimpVOS)
                    .customerId(trade.getBuyer().getId())
                    .orderTime(trade.getTradeState().getCreateTime())
                    .build());
        }
    }

    /**
     * 积分订单积分抵扣异常信息记录
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_MODIFY_OR_ADD_TRADE_POINTS_EXCEPTION)
    public void addTradePointsException(String json){
        log.info("======================= 积分订单抵扣异常信息：{} ==================", json);
        ExceptionOfTradePoints exceptionOfTradePoints = JSONObject.parseObject(json, ExceptionOfTradePoints.class);
        exceptionOfTradePointsService.add(exceptionOfTradePoints);
    }

    /**
     * 拼团和抢购不需要校验
     * @param trade
     * @return
     */
    private boolean checkBackRestrictedNum(Trade trade){
        if(Objects.isNull(trade)){
            return true;
        }
        if(!Objects.isNull(trade.getGrouponFlag()) && trade.getGrouponFlag()){
            return true;
        }
        if(!Objects.isNull(trade.getIsFlashSaleGoods()) && trade.getIsFlashSaleGoods()){
            return true;
        }
        return false;
    }

    /**
     * 订单发货消息通知
     */
    @StreamListener(JmsDestinationConstants.Q_OPEN_ORDER_DELIVERED_CONSUMER)
    public void onMessageForOrderDelivered(Trade trade) {
        log.info("收到发货消息通知：tradeNo = {}, outTradeNo = {}", trade.getId(), trade.getOutTradeNo());
        tradeDeliverService.onDeliveredForOutPlat(trade.getId());
    }
}
