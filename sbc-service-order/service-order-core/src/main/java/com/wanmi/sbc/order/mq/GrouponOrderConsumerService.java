package com.wanmi.sbc.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdListRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdForEsResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsListResponse;
import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.request.esCustomerFunds.EsCustomerFundsSaveListRequest;
import com.wanmi.sbc.order.api.request.esCustomerFunds.EsCustomerFundsSaveRequest;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.groupon.service.GrouponOrderService;
import com.wanmi.sbc.order.returnorder.service.GrouponReturnOrderService;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 拼团订单消息处理
 */
@Service
@Slf4j
@EnableBinding(OrderSink.class)
public class GrouponOrderConsumerService {

    @Autowired
    private GrouponOrderService grouponOrderService;

    @Autowired
    private GrouponReturnOrderService grouponReturnOrderService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private OrderProducerService orderProducerService;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;


    /**
     * 取消订单
     * @param orderId
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_CANCEL_ORDER_CONSUMER)
//    @GlobalTransactional
//    @Transactional
    public void cancelOrder(String orderId) {
        log.info("订单号：{},取消订单MQ消息，开始运行处理",orderId);
        Operator operator = Operator.builder().adminId("1").name("system").account("system").ip("127.0.0.1").platform(Platform
                .PLATFORM).build();
        tradeService.autoCancelOrder(orderId,operator);
    }

    /**
     * 团长开团MQ处理
     *
     * @param grouponNo
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_OPEN_GROUPON_CONSUMER)
    @GlobalTransactional
    @Transactional
    public void openGroupon(String grouponNo) {
        log.info("团编号：{},延迟消息开始运行处理",grouponNo);
        //根据团编号查询团长开团信息
        GrouponInstance grouponInstance = grouponOrderService.getGrouponInstanceByActivityIdAndGroupon(grouponNo);
        if (null == grouponInstance || StringUtils.isBlank(grouponInstance.getGrouponActivityId())){
            log.info("团编号:{},未查询到拼团订单数据，请检查团编号是否正确！",grouponNo);
            return;
        }
        GrouponActivityVO grouponActivityVO = grouponOrderService.getGrouponActivityById(grouponInstance.getGrouponActivityId());
        if (Objects.isNull(grouponActivityVO)){
            log.info("团编号:{},活动ID:{},未查询到拼团活动信息，请检查团编号是否正确！",grouponNo,grouponInstance.getGrouponActivityId());
            return;
        }
        log.info("团编号：{},具体的团实例信息如下:{}",grouponNo,grouponInstance);
        //已成团/团已作废不作处理
        if (grouponInstance.getGrouponStatus() == GrouponOrderStatus.COMPLETE || grouponInstance.getGrouponStatus() == GrouponOrderStatus.FAIL ){
            log.info("团编号：{},已成团或者已作废,不作任何处理！",grouponNo);
            return;
        }else{
            //团截止时间
            LocalDateTime endTime =  grouponInstance.getEndTime();
            log.info("团编号:{}，截止时间:{},当前系统时间:{}", grouponNo, endTime, LocalDateTime.now());
            //倒计时结束（已超团结束时长24小时) && 团状态未成团
            if (grouponInstance.getGrouponStatus() == GrouponOrderStatus.WAIT){
                boolean autoGroupon = grouponActivityVO.isAutoGroupon();
                final LocalDateTime currentTime = LocalDateTime.now();
                //自动成团
                if (autoGroupon){
                    //1、更新订单信息-已成团、待发货
                    grouponOrderService.autoGrouponSuccess(grouponNo,Operator.builder().adminId("1").name("system").account("system").ip("127.0.0.1").platform(Platform
                            .PLATFORM).build());
                    //3、修改团实例信息
                    grouponInstance.setCompleteTime(currentTime);
                    grouponInstance.setGrouponStatus(GrouponOrderStatus.COMPLETE);
                    grouponOrderService.updateGrouponInstance(grouponInstance);
                    grouponInstance.setGrouponNum(grouponInstance.getJoinNum());
                    //更新拼团活动-已成团、待成团、团失败人数；拼团活动商品-已成团人数
                    grouponOrderService.updateStatisticsNum(grouponInstance);
                }else{
                    //自动退款
                    List<Trade> tradeList = grouponReturnOrderService.handleGrouponOrderRefund(grouponNo);
                    if(CollectionUtils.isNotEmpty(tradeList)){
                        //新增更改会员资金es
                        CustomerFundsByCustomerIdListRequest byCustomerIdRequest = new CustomerFundsByCustomerIdListRequest();
                        byCustomerIdRequest.setCustomerIds(tradeList.stream().map(Trade::getBuyer).map(Buyer::getId).collect(Collectors.toList()));
                        BaseResponse<CustomerFundsListResponse> response = customerFundsQueryProvider.getByCustomerIdListForEs(byCustomerIdRequest);
                        if(Objects.nonNull(response.getContext())){
                            //es处理会员资金
                            orderProducerService.sendEsCustomerFundsList(response.getContext().getLists());
                        }
                    }

                }
            }
        }
    }

    /**
     * 拼团订单-支付成功，订单异常，自动退款
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_PAY_SUCCESS_AUTO_REFUND)
    @GlobalTransactional
    @Transactional
    public void handleGrouponOrderRefund(String json) {
        try {
            log.info("订单信息：{}，拼团订单-支付成功，订单异常，自动退款,开始运行处理!",json);
            Trade trade = JSONObject.parseObject(json,Trade.class);
            grouponReturnOrderService.handleGrouponOrderRefund(trade);

            //新增更改会员资金es
            CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
            byCustomerIdRequest.setCustomerId(trade.getBuyer().getId());
            BaseResponse<CustomerFundsByCustomerIdForEsResponse> response = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
            if(Objects.nonNull(response.getContext())){
                //es处理会员资金
                EsCustomerFundsSaveRequest request = KsBeanUtil.convert(response.getContext(),EsCustomerFundsSaveRequest.class);
                orderProducerService.sendEsCustomerFunds(request);
            }


        } catch (Exception e) {
            log.error("订单信息：{}，拼团订单-支付成功，订单异常，自动退款! param={}", json, e);
        }
    }

    /**
     * 拼团人员不足MQ处理
     * @param grouponNo
     */
    @StreamListener(JmsDestinationConstants.Q_ORDER_SERVICE_GROUPON_JOIN_NUM_LIMIT_CONSUMER)
    public void handleGrouponNumLimit(String grouponNo){
        log.info("团编号：{},拼团人员不足开始运行处理",grouponNo);
        //根据团编号查询团长开团信息
        GrouponInstance grouponInstance = grouponOrderService.getGrouponInstanceByActivityIdAndGroupon(grouponNo);
        if (null == grouponInstance || StringUtils.isBlank(grouponInstance.getGrouponActivityId())){
            log.info("团编号:{},未查询到拼团订单数据，请检查团编号是否正确！",grouponNo);
            return;
        }
        GrouponActivityVO grouponActivityVO = grouponOrderService.getGrouponActivityById(grouponInstance.getGrouponActivityId());
        if (Objects.isNull(grouponActivityVO)){
            log.info("团编号:{},活动ID:{},未查询到拼团活动信息，请检查团编号是否正确！",grouponNo,grouponInstance.getGrouponActivityId());
            return;
        }
        log.info("团编号：{},具体的团实例信息如下:{}",grouponNo,grouponInstance);
        //已成团/团已作废不作处理
        if (grouponInstance.getGrouponStatus() == GrouponOrderStatus.COMPLETE || grouponInstance.getGrouponStatus() == GrouponOrderStatus.FAIL ){
            log.info("团编号：{},已成团或者已作废,不作任何处理！",grouponNo);
            return;
        }else{
            //团截止时间
            LocalDateTime endTime =  grouponInstance.getEndTime();
            log.info("团编号:{}，截止时间:{},当前系统时间:{}", grouponNo, endTime, LocalDateTime.now());
            //团状态未成团
            if (grouponInstance.getGrouponStatus() == GrouponOrderStatus.WAIT){
                List<Trade> tradeList = grouponOrderService.getTradeByGrouponNo(grouponNo);
                for (Trade trade : tradeList) {
                    grouponOrderService.sendNoticeMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.GROUP_NUM_LIMIT, trade, grouponNo, grouponActivityVO.getWaitGrouponNum());
                }
            }
        }
    }
}
