package com.wanmi.sbc.order.groupon.service;

import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.node.OrderProcessType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsStockErrorCode;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.ProviderGoodsStockSyncRequest;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivitySaveProvider;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordProvider;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityByIdRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityingByGoodsInfoIdsRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordByCustomerRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordDecrBuyNumRequest;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityByIdResponse;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponRecordVO;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.GrouponOrderCheckStatus;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.constant.OrderErrorCode;
import com.wanmi.sbc.order.mq.OrderProducerService;
import com.wanmi.sbc.order.trade.fsm.TradeFSMService;
import com.wanmi.sbc.order.trade.fsm.event.TradeEvent;
import com.wanmi.sbc.order.trade.fsm.params.StateRequest;
import com.wanmi.sbc.order.trade.model.entity.GrouponTradeValid;
import com.wanmi.sbc.order.trade.model.entity.PayInfo;
import com.wanmi.sbc.order.trade.model.entity.TradeGroupForm;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeGroupon;
import com.wanmi.sbc.order.trade.repository.TradeGrouponInstanceRepository;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: Geek Wang
 * @createDate: 2019/5/21 11:21
 * @version: 1.0
 */
@Slf4j
@Service
public class GrouponOrderService {

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeGrouponInstanceRepository tradeGrouponInstanceRepository;

    @Autowired
    private OrderProducerService orderProducerService;

    @Autowired
    private TradeFSMService tradeFSMService;

    @Autowired
    private GrouponRecordProvider grouponRecordProvider;

    @Autowired
    private GrouponRecordQueryProvider grouponRecordQueryProvider;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private GrouponActivitySaveProvider activityProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private ProviderTradeService providerTradeService;

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param grouponInstance
     */
    public GrouponInstance updateGrouponInstance(GrouponInstance grouponInstance) {
        return tradeGrouponInstanceRepository.save(grouponInstance);
    }

    /**
     * 处理拼团订单支付成功业务,更新拼团订单状态（支付回调）
     *
     * @param trade 订单信息
     */
    public Trade handleGrouponOrderPaySuccess(Trade trade) {
        GrouponOrderCheckStatus checkStatus = checkGrouponTrade(trade);
        //订单对象不存在/不是拼团订单
        if (checkStatus == GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_PARAMS_ERROR ||
                checkStatus == GrouponOrderCheckStatus.NOT_ORDER_MARKETING_GROUPON) {
            log.info("订单对象不存在/不是拼团订单=====>>{}", checkStatus);
            return trade;
        }
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        String grouponActivityId = tradeGroupon.getGrouponActivityId();
        tradeGroupon.setPayState(PayState.PAID);

        List<String> goodsInfoId = trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        Long goodsSalesNum = trade.getTradeItems().stream().map(TradeItem::getNum).reduce(Long::sum).orElse(NumberUtils.LONG_ZERO);
        //更新商品销售量、订单量、交易额
        orderProducerService.updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId, goodsInfoId.get(0), goodsSalesNum.intValue(), NumberUtils.INTEGER_ONE, trade.getTradePrice().getTotalPrice());

        //拼团订单信息不正确
        if (checkStatus != GrouponOrderCheckStatus.PASS) {
            log.info("拼团订单信息不正确,请检查对应参数信息=====>>{}，具体失败原因信息：{}", trade, checkStatus);
            return joinGrouponActivityFail(trade, checkStatus);
        }

        //当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        //验证拼团活动状态
        GrouponActivityVO grouponActivityVO = getGrouponActivityById(grouponActivityId);
        checkStatus = checkGrouponActivityStatus(grouponActivityVO, currentTime);
        Boolean leader = tradeGroupon.getLeader();
        if (leader) {
            if (checkStatus != GrouponOrderCheckStatus.PASS) {
                log.info("拼团活动未开始/已过期/不存在，具体失败原因信息：{}", trade, checkStatus);
                return joinGrouponActivityFail(trade, checkStatus);
            }
        } else {
            if (checkStatus == GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_EXIST
                    || checkStatus == GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_START) {
                log.info("拼团活动未开始/不存在，具体失败原因信息：{}", trade, checkStatus);
                return joinGrouponActivityFail(trade, checkStatus);
            }
        }


        Buyer buyer = trade.getBuyer();
        String buyCustomerId = buyer.getId();
        String grouponNo = tradeGroupon.getGrouponNo();
        //检验团长是否可开团或者团员是否可参团
        TradeGroupForm tradeGroupForm = TradeGroupForm.builder().buyCustomerId(buyCustomerId).goodsId(tradeGroupon
                .getGoodId())
                .grouponActivityId(grouponActivityId).grouponNo(grouponNo)
                .leader(leader).build();
        //检验团长是否可开团或者团员是否可参团
        checkStatus = checkLeaderIsOpenGrouponOrMemberIsJoinGroupon(tradeGroupForm);

        //验证通过
        if (checkStatus == GrouponOrderCheckStatus.PASS) {
            Operator operator = Operator.builder().adminId(buyer.getId()).name(buyer.getName()).account(buyer.getAccount()).ip("127.0.0.1").platform(Platform
                    .CUSTOMER).build();
            GrouponInstance grouponInstance = getGrouponInstanceByActivityIdAndGroupon(grouponNo);
            //参团人数
            grouponInstance.setJoinNum(Objects.isNull(grouponInstance.getJoinNum()) ? NumberUtils.INTEGER_ONE : grouponInstance.getJoinNum() + NumberUtils.INTEGER_ONE);
            //团状态
            grouponInstance.setGrouponStatus(GrouponOrderStatus.WAIT);
            tradeGroupon.setGrouponOrderStatus(GrouponOrderStatus.WAIT);
            if (leader) {
                //开团时间
                grouponInstance.setCreateTime(currentTime);
                LocalDateTime endTime = currentTime.plusDays(NumberUtils.LONG_ONE);

                //LocalDateTime endTimeTest = currentTime.plusMinutes(5);

                //参团人数不足提醒时间
                LocalDateTime remindTime = endTime.minusHours(3);
                //团截止时间：默认延迟24小时
                grouponInstance.setEndTime(endTime);
                long millis = Duration.between(currentTime, endTime).toMillis();
                long remindMillis = Duration.between(currentTime, remindTime).toMillis();
                //创建拼团-团长延迟消息队列
                Boolean send = orderProducerService.sendOpenGroupon(grouponNo, millis);
                log.info("团长-会员ID======>>{},创建团编号====>>{}，延迟消息队列,是否成功=====>>{}", buyCustomerId, grouponNo, send ?
                        "成功" : "失败");
                Boolean sendMsgToC = orderProducerService.sendOpenGrouponMsgToC(grouponNo, grouponActivityId);
                log.info("团长-会员ID======>>{},创建团编号====>>{}，WebSocket消息推送,是否成功=====>>{}", buyCustomerId, grouponNo, sendMsgToC ?
                        "成功" : "失败");
                //参团人数不足提醒-拼团失败前3小时
                Boolean sendGroupNumLimit = orderProducerService.sendGrouponNumLimit(grouponNo, remindMillis);
                log.info("团长-会员ID======>>{},创建团编号====>>{}，校验拼团人数,是否成功=====>>{}", buyCustomerId, grouponNo, sendGroupNumLimit ?
                        "成功" : "失败");
                //开团发送通知消息
                this.sendNoticeMessage(NodeType.ORDER_PROGRESS_RATE, OrderProcessType.GROUP_OPEN_SUCCESS, trade, grouponNo, null);
            } else {
                if (grouponInstance.getGrouponNum().equals(grouponInstance.getJoinNum())) {
                    grouponInstance.setGrouponStatus(GrouponOrderStatus.COMPLETE);
                    grouponInstance.setCompleteTime(currentTime);
                    trade.getTradeGroupon().setGrouponOrderStatus(GrouponOrderStatus.COMPLETE);
                }
            }
            GrouponInstance reslut = updateGrouponInstance(grouponInstance);
            log.info("更新拼团实例信息：{},是否成功=====>>{}", reslut, Objects.nonNull(reslut) ? "成功" : "失败");
            //拼团成功
            if (grouponInstance.getGrouponStatus() == GrouponOrderStatus.COMPLETE) {
                List<Trade> tradeList = getTradeByGrouponNo(grouponInstance.getGrouponNo());
                tradeList.add(trade);
                //同团订单，设置为已成团、待发货状态
                autoGrouponSuccess(grouponNo, operator);
                //拼团成功，待成团人数+1（当前订单）
                orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, NumberUtils.INTEGER_ONE, GrouponOrderStatus.WAIT);
                //更新已成团人数
                orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, grouponInstance.getGrouponNum(), GrouponOrderStatus.COMPLETE);

                List<String> skuIds = tradeList.stream().flatMap(t -> t.getTradeItems().stream().map(TradeItem::getSkuId)).collect(Collectors.toList());
                orderProducerService.updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId, skuIds, NumberUtils.INTEGER_ONE);

                //拼团发送通知消息
                this.handleSendMessage(grouponNo, tradeList, NodeType.ORDER_PROGRESS_RATE, OrderProcessType.JOIN_GROUP_SUCCESS);
            } else {
                orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, NumberUtils.INTEGER_ONE, GrouponOrderStatus.WAIT);
            }
            return trade;
        } else {
            //支付成功，待成团人数+1
            orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, NumberUtils.INTEGER_ONE, GrouponOrderStatus.WAIT);
            return joinGrouponActivityFail(trade, checkStatus);
        }

    }

    /**
     * 拼团节点发送通知消息
     *
     * @param nodeType
     * @param nodeCode
     * @param trade
     * @param groupNo
     */
    public void sendNoticeMessage(NodeType nodeType, OrderProcessType nodeCode, Trade trade, String groupNo, Integer limitNum) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", nodeType.toValue());
        map.put("node", nodeCode.toValue());
        map.put("id", groupNo);
        List<String> params = new ArrayList<>();
        params.add(trade.getTradeItems().get(0).getSkuName());
        if (limitNum != null) {
            params.add(String.valueOf(limitNum));
        }
        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(trade.getBuyer().getId());
        messageMQRequest.setPic(trade.getTradeItems().get(0).getPic());
        messageMQRequest.setMobile(trade.getBuyer().getAccount());
        orderProducerService.sendMessage(messageMQRequest);
    }

    /**
     * 拼团成功/参团人数不足通知消息预处理
     *
     * @param grouponNo
     * @param tradeList
     * @param nodeType
     * @param nodeCode
     */
    public void handleSendMessage(String grouponNo, List<Trade> tradeList, NodeType nodeType, OrderProcessType nodeCode) {
        for (Trade trade : tradeList) {
            this.sendNoticeMessage(nodeType, nodeCode, trade, grouponNo, null);
        }
    }


    /**
     * 开团/参团失败统一调用方法
     *
     * @param trade
     * @param checkStatus
     * @return
     */
    public Trade joinGrouponActivityFail(Trade trade, GrouponOrderCheckStatus checkStatus) {
        TradeGroupon tradeGroupon = trade.getTradeGroupon();

        //验证不通过流程
        tradeGroupon.setGrouponOrderStatus(GrouponOrderStatus.FAIL);
        tradeGroupon.setGrouponFailReason(checkStatus);
        tradeGroupon.setFailTime(LocalDateTime.now());
        return trade;
    }

    /**
     * 自动拼团成功
     *
     * @param grouponNo
     */
    public void autoGrouponSuccess(String grouponNo, Operator operator) {
        List<Trade> tradeList = getTradeByGrouponNo(grouponNo);
        tradeList.stream().forEach(trade -> {
            StateRequest stateRequest = StateRequest
                    .builder()
                    .tid(trade.getId())
                    .operator(operator)
                    .event(TradeEvent.JOIN_GROUPON)
                    .build();
            tradeFSMService.changeState(stateRequest);
        });
        List<String> tradeIdList = tradeList.stream().map(Trade::getId).collect(Collectors.toList());
        //更新子单
        List<ProviderTrade> providerTradeList = providerTradeService.findListByParentIdList(tradeIdList);
        if (CollectionUtils.isNotEmpty(providerTradeList)){
            providerTradeList.forEach(providerTrade -> {
                Trade parentTrade = tradeList.stream().filter(trade -> trade.getId().equals(providerTrade.getParentId())).findFirst().get();
                providerTrade.setTradeGroupon(parentTrade.getTradeGroupon());
                providerTrade.getTradeState().setFlowState(FlowState.AUDIT);
                providerTrade.getTradeGroupon().setGrouponOrderStatus(GrouponOrderStatus.COMPLETE);
                providerTrade.getTradeGroupon().setGrouponSuccessTime(LocalDateTime.now());
            });
            providerTradeService.updateProviderTradeList(providerTradeList);
        }

//        providerTradeList.forEach(providerTrade -> {
//            providerTradeService.singlePushOrder(providerTrade);
//        });
        for (Trade trade : tradeList) {
            providerTradeService.singlePushOrder(Collections.singletonList(trade));
        }
    }

    /**
     * 更新拼团活动-已成团、待成团、团失败人数；拼团活动商品-已成团人数
     *
     * @param grouponInstance
     */
    public void updateStatisticsNum(GrouponInstance grouponInstance) {
        if (Objects.isNull(grouponInstance)) {
            return;
        }
        String grouponActivityId = grouponInstance.getGrouponActivityId();
        GrouponOrderStatus grouponStatus = grouponInstance.getGrouponStatus();
        if (GrouponOrderStatus.COMPLETE == grouponStatus) {
            orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, grouponInstance.getGrouponNum(), grouponStatus);
            List<Trade> tradeList = getTradeByGrouponNo(grouponInstance.getGrouponNo());
            List<String> skuIds = tradeList.stream().flatMap(t -> t.getTradeItems().stream().map(TradeItem::getSkuId)).collect(Collectors.toList());
            orderProducerService.updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(grouponActivityId, skuIds, NumberUtils.INTEGER_ONE);
        } else if (GrouponOrderStatus.WAIT == grouponStatus) {
            orderProducerService.updateStatisticsNumByGrouponActivityId(grouponActivityId, NumberUtils.INTEGER_ONE, grouponStatus);
        }
    }

    /**
     * 验证拼团订单是否可付款（准备支付时）
     *
     * @param orderId
     */
    public GrouponOrderCheckStatus checkGrouponOrderBeforePay(String orderId) {
        Trade trade = tradeService.detail(orderId);
        GrouponOrderCheckStatus checkStatus = checkGrouponTrade(trade);
        if (checkStatus != GrouponOrderCheckStatus.PASS) {
            return checkStatus;
        }

        //验证拼团活动信息是否合法
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        String grouponActivityId = tradeGroupon.getGrouponActivityId();
        LocalDateTime currentTime = LocalDateTime.now();
        GrouponActivityVO grouponActivityVO = getGrouponActivityById(grouponActivityId);
        checkStatus = checkGrouponActivityStatus(grouponActivityVO, currentTime);

        Boolean leader = tradeGroupon.getLeader();
        if (leader) {
            if (checkStatus != GrouponOrderCheckStatus.PASS) {
                return checkStatus;
            }
        } else {
            if (checkStatus == GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_EXIST
                    || checkStatus == GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_START) {
                return checkStatus;
            }
        }

        String buyCustomerId = trade.getBuyer().getId();
        String grouponNo = tradeGroupon.getGrouponNo();

        TradeGroupForm tradeGroupForm = TradeGroupForm.builder().buyCustomerId(buyCustomerId).goodsId(tradeGroupon
                .getGoodId())
                .grouponActivityId(grouponActivityId).grouponNo(grouponNo)
                .leader(leader).build();
        //检验团长是否可开团或者团员是否可参团
        return checkLeaderIsOpenGrouponOrMemberIsJoinGroupon(tradeGroupForm);
    }

    /**
     * 取消拼团订单
     *
     * @param trade
     */
    public void cancelGrouponOrder(Trade trade) {
        if (Objects.isNull(trade)) {
            return;
        }
        Boolean grouponFlag = trade.getGrouponFlag();
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        //不是拼团订单
        if (Objects.isNull(grouponFlag) || Boolean.FALSE.equals(grouponFlag) || Objects.isNull(tradeGroupon)) {
            return;
        }

        Boolean leader = tradeGroupon.getLeader();
        //是否团长
        if (leader) {
            //更新团实例
            GrouponInstance grouponInstance = getGrouponInstanceByActivityIdAndGroupon(tradeGroupon.getGrouponNo());
            grouponInstance.setGrouponStatus(GrouponOrderStatus.FAIL);
            grouponInstance.setFailTime(LocalDateTime.now());
            updateGrouponInstance(grouponInstance);

        }

        //拼团记录表-减少已购数据
        List<String> skuIds = trade.getTradeItems().stream().map(TradeItem::getSkuId).collect(Collectors.toList());
        List<Long> buyNums = trade.getTradeItems().stream().map(TradeItem::getNum).collect(Collectors.toList());
        GrouponRecordDecrBuyNumRequest request = GrouponRecordDecrBuyNumRequest.builder().grouponActivityId(tradeGroupon.getGrouponActivityId())
                .customerId(trade.getBuyer().getId()).goodsInfoId(skuIds.get(0)).buyNum(buyNums.get(0).intValue()).build();
        grouponRecordProvider.decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(request);
    }

    /**
     * 验证团长是否可开团/团员是否可以参团
     *
     * @param form
     * @return
     */
    public GrouponOrderCheckStatus checkLeaderIsOpenGrouponOrMemberIsJoinGroupon(TradeGroupForm form) {
        Boolean leader = form.getLeader();
        LocalDateTime currentTime = LocalDateTime.now();
        Trade trade = getTradeByLeaderOrMember(form);
        if (leader) {
            //已开团，不可再开团
            if (Objects.nonNull(trade)) {
                return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_WAIT;
            }
        } else {
            String grouponNo = form.getGrouponNo();
            //已参同一团活动，不可再参团
            if (Objects.nonNull(trade)) {
                return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ALREADY_JOINED;
            }
            GrouponInstance grouponInstance = getGrouponInstanceByActivityIdAndGroupon(grouponNo);
            if (Objects.isNull(grouponInstance)) {
                return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ORDER_INST_NOT_EXIST;
            }
            GrouponOrderStatus grouponStatus = grouponInstance.getGrouponStatus();
            //已成团/团已作废，不可参团
            if (grouponStatus == GrouponOrderStatus.COMPLETE || grouponStatus == GrouponOrderStatus.FAIL) {
                return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ORDER_INST_ALREADY_OR_FAIL;
            }

            //查询团是否已过24小时
            LocalDateTime endTime = grouponInstance.getEndTime();
            //倒计时结束（已超团结束时长24小时）
            if (endTime.isBefore(currentTime)) {
                return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_COUNT_DOWN_END;
            }
        }
        return GrouponOrderCheckStatus.PASS;
    }

    /**
     * 获取团长/参团订单记录信息
     *
     * @param form
     * @return
     */
    public Trade getTradeByLeaderOrMember(TradeGroupForm form) {
        Boolean leader = form.getLeader();

        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("buyer.id").is(form.getBuyCustomerId()));
        criterias.add(Criteria.where("tradeGroupon.grouponActivityId").is(form.getGrouponActivityId()));
        criterias.add(Criteria.where("tradeGroupon.payState").is(PayState.PAID.getStateId()));
        criterias.add(Criteria.where("grouponFlag").is(Boolean.TRUE));
        criterias.add(Criteria.where("tradeGroupon.grouponOrderStatus").is(GrouponOrderStatus.WAIT.toString()));

        if (leader) {
            criterias.add(Criteria.where("tradeGroupon.leader").is(leader));
            criterias.add(Criteria.where("tradeGroupon.goodId").is(form.getGoodsId()));
        } else {
            criterias.add(Criteria.where("tradeGroupon.grouponNo").is(form.getGrouponNo()));
        }

        Criteria criteria = new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
        return mongoTemplate.findOne(new Query(criteria), Trade.class);
    }

    /**
     * 根据团编号查询拼团订单信息（已支付 & 拼团订单 & 待成团 & 未退款）
     *
     * @param grouponNo
     * @return
     */
    public List<Trade> getTradeByGrouponNo(String grouponNo) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("tradeState.payState").is(PayState.PAID.getStateId()),
                Criteria.where("grouponFlag").is(Boolean.TRUE),
                Criteria.where("tradeGroupon.grouponOrderStatus").is(GrouponOrderStatus.WAIT.toString()),
                Criteria.where("tradeGroupon.grouponNo").is(grouponNo),
                Criteria.where("tradeGroupon.grouponFailReason").exists(false)
        );
        return mongoTemplate.find(new Query(criteria), Trade.class);
    }

    /**
     * 根据团编号查询团长开团信息
     *
     * @param grouponNo
     * @return
     */
    public GrouponInstance getGrouponInstanceByActivityIdAndGroupon(String grouponNo) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("grouponNo").is(grouponNo));
        return mongoTemplate.findOne(new Query(criteria), GrouponInstance.class);
    }

    /**
     * 计算团结束时间
     *
     * @param openGrouponTime
     * @param grouponMarketingEndTime
     * @return
     */
    public LocalDateTime getMarketingGrouponInstEndTime(LocalDateTime openGrouponTime, LocalDateTime
            grouponMarketingEndTime) {
        LocalDateTime endTime = openGrouponTime.plusDays(NumberUtils.LONG_ONE);
        if (endTime.isAfter(grouponMarketingEndTime)) {
            endTime = grouponMarketingEndTime;
        }
        return endTime;
    }

    /**
     * 根据拼团活动ID查询拼团活动信息
     *
     * @param grouponActivityId
     * @return
     */
    public GrouponActivityVO getGrouponActivityById(String grouponActivityId) {
        //查询拼团活动信息
        BaseResponse<GrouponActivityByIdResponse> baseResponse = grouponActivityQueryProvider.getById(new
                GrouponActivityByIdRequest(grouponActivityId));
        GrouponActivityByIdResponse response = baseResponse.getContext();
        return response.getGrouponActivity();
    }

    /**
     * 验证拼团活动状态是否正常
     *
     * @param grouponActivityVO
     * @param currentTime
     */
    public GrouponOrderCheckStatus checkGrouponActivityStatus(GrouponActivityVO grouponActivityVO, LocalDateTime
            currentTime) {

        //拼团活动不存在
        if (Objects.isNull(grouponActivityVO)) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_EXIST;
        }
        //活动开始时间
        LocalDateTime grouponActivityStartTime = grouponActivityVO.getStartTime();
        //活动结束时间
        LocalDateTime grouponActivityEndTime = grouponActivityVO.getEndTime();

        //活动未开始
        if (currentTime.isBefore(grouponActivityStartTime)) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_NOT_START;
        }
        //活动已失效
        if (currentTime.isAfter(grouponActivityEndTime)) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ACTIVITY_DISABLE;
        }
        return GrouponOrderCheckStatus.PASS;
    }

    /**
     * 验证拼团订单信息是否正确
     *
     * @param trade
     * @return
     */
    public GrouponOrderCheckStatus checkGrouponTrade(Trade trade) {
        //订单对象不存在
        if (Objects.isNull(trade)) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_PARAMS_ERROR;
        }
        Boolean grouponFlag = trade.getGrouponFlag();
        //不是拼团订单
        if (Objects.isNull(grouponFlag) || Boolean.FALSE.equals(grouponFlag)) {
            return GrouponOrderCheckStatus.NOT_ORDER_MARKETING_GROUPON;
        }
        TradeGroupon tradeGroupon = trade.getTradeGroupon();
        //拼团对象信息不存在
        if (Objects.isNull(tradeGroupon)) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_PARAMS_ERROR;
        }
        //拼团订单支付信息不正确
        TradeState tradeState = trade.getTradeState();
        PayInfo payInfo = trade.getPayInfo();
        if (Objects.isNull(tradeState) || Objects.isNull(payInfo) || PayType.fromValue(Integer.parseInt(payInfo.getPayTypeId())) != PayType.ONLINE) {
            return GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_PAY_STATUS_ERROR;
        }
        return GrouponOrderCheckStatus.PASS;
    }


    /**
     * 订单提交前验证拼团订单信息
     */
    public GrouponGoodsInfoVO validGrouponOrderBeforeCommit(GrouponTradeValid valid) {

        String goodsId = valid.getGoodsId();
        String goodsInfoId = valid.getGoodsInfoId();
        String customerId = valid.getCustomerId();
        String grouponNo = valid.getGrouponNo();
        Boolean openGroupon = valid.getOpenGroupon();
        Integer buyCount = valid.getBuyCount();
        GrouponGoodsInfoVO grouponGoodsInfo = null;
        //团长开团
        if (openGroupon) {
            // 商品未关联进行中的拼团活动
            grouponGoodsInfo = grouponActivityQueryProvider.listActivityingByGoodsInfoIds(
                    new GrouponActivityingByGoodsInfoIdsRequest(Arrays.asList(goodsInfoId))).getContext().getGrouponGoodsInfoMap().get(goodsInfoId);
        } else {
            GrouponInstance grouponInstance = getGrouponInstanceByActivityIdAndGroupon(grouponNo);
            if (Objects.isNull(grouponInstance) || StringUtils.isBlank(grouponInstance.getGrouponActivityId())) {
                log.error("团编号:{},未查询到拼团订单数据，请检查团编号是否正确！", grouponNo);
                throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ORDER_NOT_FUND);
            }
            GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest request = GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest.builder().grouponActivityId(grouponInstance.getGrouponActivityId())
                    .goodsInfoId(goodsInfoId).build();
            BaseResponse<GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse> baseResponse = grouponGoodsInfoQueryProvider.getByGrouponActivityIdAndGoodsInfoId(request);
            grouponGoodsInfo = baseResponse.getContext().getGrouponGoodsInfoVO();
        }
        if (Objects.isNull(grouponGoodsInfo) || Objects.isNull(grouponGoodsInfo.getGoodsInfo()) || grouponGoodsInfo.getGoodsInfo().getAddedFlag().equals(0)) {
            throw new SbcRuntimeException("K-050205");
        }

        TradeGroupForm tradeGroupForm = TradeGroupForm.builder().buyCustomerId(customerId)
                .goodsId(grouponGoodsInfo.getGoodsId())
                .grouponActivityId(grouponGoodsInfo.getGrouponActivityId()).grouponNo(grouponNo)
                .leader(openGroupon).build();
        GrouponOrderCheckStatus checkStatus = checkLeaderIsOpenGrouponOrMemberIsJoinGroupon(tradeGroupForm);

        //已开团，不可再开团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_WAIT.equals(checkStatus)) {
            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_WAIT);
        }

        //已参同一团活动，不可再参团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ALREADY_JOINED.equals(checkStatus)) {

            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ALREADY_JOINED);
        }

        //已成团/团已作废，不可参团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ORDER_INST_ALREADY_OR_FAIL.equals(checkStatus)
                || GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_COUNT_DOWN_END.equals(checkStatus)) {

            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ALREADY_END);
        }
        if (Objects.nonNull(buyCount)) {
            // 未达到拼团商品起售数
            Integer startSellingNum = grouponGoodsInfo.getStartSellingNum();
            if (Objects.nonNull(startSellingNum) && buyCount < startSellingNum || buyCount <= Constants.no) {
                throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_START_NUM);
            }

            // 已超过拼团商品限购数
            GrouponRecordVO record = grouponRecordQueryProvider.getByCustomerIdAndGoodsInfoId(new
                    GrouponRecordByCustomerRequest(
                    grouponGoodsInfo.getGrouponActivityId(),
                    customerId,
                    goodsInfoId)).getContext().getGrouponRecordVO();

            //限售数如果为0，则不限制拼团数量
            if (Objects.nonNull(record) && record.getLimitSellingNum() != 0) {
                if (Objects.nonNull(record) && Objects.nonNull(record.getLimitSellingNum())
                        && (record.getBuyNum() + buyCount) > record.getLimitSellingNum()) {
                    throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_LIMIT_NUM);
                } else {
                    //第一次购买拼团商品
                    if (Objects.nonNull(grouponGoodsInfo.getLimitSellingNum())
                            && buyCount > grouponGoodsInfo.getLimitSellingNum()) {
                        throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_LIMIT_NUM);
                    }
                }
            }
        }
        return grouponGoodsInfo;
    }


    /**
     * 拼团去购买提示
     */
    public GrouponGoodsInfoVO validGrouponImmediately(GrouponTradeValid valid) {

        String goodsId = valid.getGoodsId();
        String goodsInfoId = valid.getGoodsInfoId();
        String customerId = valid.getCustomerId();
        String grouponNo = valid.getGrouponNo();
        Boolean openGroupon = valid.getOpenGroupon();
        Integer buyCount = valid.getBuyCount();
        GrouponGoodsInfoVO grouponGoodsInfo = null;

        //根据goodsinfoid查询商品详情
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(goodsInfoId).build
                ()).getContext();

        if (Objects.isNull(goodsInfo) || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        //获取供应商库存
        if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && goodsInfo.getThirdPlatformType() == null) {
            List<GoodsInfoDTO> goodsInfoDTOS = new ArrayList<>();
            goodsInfoDTOS.add(KsBeanUtil.copyPropertiesThird(goodsInfo, GoodsInfoDTO.class));
            GoodsInfoVO goodsInfoVO = goodsInfoProvider.providerGoodsStockSync(new ProviderGoodsStockSyncRequest(goodsInfoDTOS))
                    .getContext().getGoodsInfoList().get(0);
            goodsInfo.setStock(goodsInfoVO.getStock());
        }
        //校验商品库存
        if (Objects.nonNull(buyCount) && buyCount.longValue() > goodsInfo.getStock()) {
            throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
        }

        //团长开团
        if (openGroupon) {
            // 商品未关联进行中的拼团活动
            grouponGoodsInfo = grouponActivityQueryProvider.listActivityingByGoodsInfoIds(
                    new GrouponActivityingByGoodsInfoIdsRequest(Arrays.asList(goodsInfoId))).getContext().getGrouponGoodsInfoMap().get(goodsInfoId);
        } else {
            GrouponInstance grouponInstance = getGrouponInstanceByActivityIdAndGroupon(grouponNo);
            if (Objects.isNull(grouponInstance) || StringUtils.isBlank(grouponInstance.getGrouponActivityId())) {
                log.error("团编号:{},未查询到拼团订单数据，请检查团编号是否正确！", grouponNo);
                throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ORDER_NOT_FUND);
            }
            GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest request = GrouponGoodsByGrouponActivityIdAndGoodsInfoIdRequest.builder().grouponActivityId(grouponInstance.getGrouponActivityId())
                    .goodsInfoId(goodsInfoId).build();
            BaseResponse<GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse> baseResponse = grouponGoodsInfoQueryProvider.getByGrouponActivityIdAndGoodsInfoId(request);
            grouponGoodsInfo = baseResponse.getContext().getGrouponGoodsInfoVO();
        }
        if (Objects.isNull(grouponGoodsInfo) || Objects.isNull(grouponGoodsInfo.getGoodsInfo()) || grouponGoodsInfo.getGoodsInfo().getAddedFlag().equals(0)) {
            throw new SbcRuntimeException("K-050116");
        }

        TradeGroupForm tradeGroupForm = TradeGroupForm.builder().buyCustomerId(customerId)
                .goodsId(grouponGoodsInfo.getGoodsId())
                .grouponActivityId(grouponGoodsInfo.getGrouponActivityId()).grouponNo(grouponNo)
                .leader(openGroupon).build();
        GrouponOrderCheckStatus checkStatus = checkLeaderIsOpenGrouponOrMemberIsJoinGroupon(tradeGroupForm);

        //已开团，不可再开团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_WAIT.equals(checkStatus)) {
            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_WAIT);
        }

        //已参同一团活动，不可再参团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ALREADY_JOINED.equals(checkStatus)) {

            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ALREADY_JOINED);
        }

        //已成团/团已作废，不可参团
        if (GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_ORDER_INST_ALREADY_OR_FAIL.equals(checkStatus)
                || GrouponOrderCheckStatus.ORDER_MARKETING_GROUPON_COUNT_DOWN_END.equals(checkStatus)) {

            throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_ALREADY_END);
        }
        if (Objects.nonNull(buyCount)) {
            // 未达到拼团商品起售数
            Integer startSellingNum = grouponGoodsInfo.getStartSellingNum();
            if (Objects.nonNull(startSellingNum) && buyCount < startSellingNum || buyCount <= Constants.no) {
                throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_START_NUM);
            }

            // 已超过拼团商品限购数
            GrouponRecordVO record = grouponRecordQueryProvider.getByCustomerIdAndGoodsInfoId(new
                    GrouponRecordByCustomerRequest(
                    grouponGoodsInfo.getGrouponActivityId(),
                    customerId,
                    goodsInfoId)).getContext().getGrouponRecordVO();

            //限售数如果为0，则不限制拼团数量
            if (Objects.nonNull(record) && record.getLimitSellingNum() != 0) {
                if (Objects.nonNull(record) && Objects.nonNull(record.getLimitSellingNum())
                        && (record.getBuyNum() + buyCount) > record.getLimitSellingNum()) {
                    throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_LIMIT_NUM);
                } else {
                    //第一次购买拼团商品
                    if (Objects.nonNull(grouponGoodsInfo.getLimitSellingNum())
                            && buyCount > grouponGoodsInfo.getLimitSellingNum()) {
                        throw new SbcRuntimeException(OrderErrorCode.ORDER_MARKETING_GROUPON_LIMIT_NUM);
                    }
                }
            }
        }
        return grouponGoodsInfo;
    }
}
