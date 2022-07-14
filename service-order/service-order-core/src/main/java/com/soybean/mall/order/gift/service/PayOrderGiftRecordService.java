package com.soybean.mall.order.gift.service;


import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.api.response.mq.SimpleTradeResp;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.soybean.mall.order.gift.model.OrderGiftRecordLog;
import com.soybean.mall.order.gift.repository.PayOrderGiftRecordLogRepository;
import com.soybean.mall.order.gift.repository.PayOrderGiftRecordRepository;
import com.soybean.marketing.api.enums.ActivityCategoryEnum;
import com.soybean.marketing.api.provider.activity.NormalActivityPointSkuProvider;
import com.soybean.marketing.api.req.SpuNormalActivityReq;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.enums.FanDengChangeTypeEnum;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengAddPointReq;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.order.api.enums.RecordStateEnum;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 8:22 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public abstract class PayOrderGiftRecordService {


    @Autowired
    protected TradeService tradeService;

    @Autowired
    private NormalActivityPointSkuProvider normalActivityPointSkuProvider;

    @Autowired
    private PayOrderGiftRecordLogRepository payOrderGiftRecordLogRepository;

    @Autowired
    protected PayOrderGiftRecordRepository payOrderGiftRecordRepository;

    @Autowired
    protected ExternalProvider externalProvider;

    @Autowired
    protected CustomerQueryProvider customerQueryProvider;

    @Autowired
    protected GoodsBlackListProvider goodsBlackListProvider;

    /**
     * 过滤订单信息
     * @param recordMessageMq
     * @return
     */
    private SimpleTradeResp filterSimpleTrade(RecordMessageMq recordMessageMq) {

        if (StringUtils.isBlank(recordMessageMq.getOrderId())) {
            return null;
        }
        if (CollectionUtils.isEmpty(recordMessageMq.getChannelTypes())) {
            return null;
        }

        //获取订单信息
        Trade orderDetail = tradeService.detail(recordMessageMq.getOrderId());
        if (orderDetail == null) {
            log.error("PayOrderGiftRecordService filterSimpleTrade orderId:{} 不存在", recordMessageMq.getOrderId());
            return null;
        }

        //获取sku信息
        List<String> skuIds = new ArrayList<>();
        for (TradeItem tradeItem : orderDetail.getTradeItems()) {
            skuIds.add(tradeItem.getSkuId());
        }

        if (CollectionUtils.isEmpty(skuIds)) {
            return null;
        }

        SimpleTradeResp simpleTradeResp = new SimpleTradeResp();
        simpleTradeResp.setOrderId(orderDetail.getId());
        simpleTradeResp.setCustomerId(orderDetail.getBuyer().getId());
        simpleTradeResp.setSkuIds(skuIds);
        return simpleTradeResp;
    }


    /**
     *
     * 获取商品所在运行中的活动
     * @param skuIds
     * @return
     */
    private List<SkuNormalActivityResp> listRunningNormalActivity(List<String> skuIds, List<Integer> channelTypes) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        SpuNormalActivityReq spuNormalActivityReq = new SpuNormalActivityReq();
        spuNormalActivityReq.setSkuIds(skuIds);
        spuNormalActivityReq.setChannelTypes(channelTypes);
        List<SkuNormalActivityResp> context = normalActivityPointSkuProvider.listSpuRunningNormalActivity(spuNormalActivityReq).getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new ArrayList<>();
        }
        return context;
    }



    /**
     * 新增日志记录信息
     * @param orderGiftRecord
     */
    private void addGiftRecordLog(OrderGiftRecord orderGiftRecord) {
        //记录日志
        OrderGiftRecordLog orderGiftRecordLog = new OrderGiftRecordLog();
        orderGiftRecordLog.setCustomerId(orderGiftRecord.getCustomerId());
        orderGiftRecordLog.setOrderId(orderGiftRecord.getOrderId());
        orderGiftRecordLog.setActivityId(orderGiftRecord.getActivityId());
        orderGiftRecordLog.setRecordId(orderGiftRecord.getId());
        orderGiftRecordLog.setRecordStatus(orderGiftRecord.getRecordStatus());
        orderGiftRecordLog.setRemark(orderGiftRecord.getName());
        orderGiftRecordLog.setCreateTime(LocalDateTime.now());
        orderGiftRecordLog.setUpdateTime(LocalDateTime.now());
        orderGiftRecordLog.setDelFlag(DeleteFlag.NO);
        payOrderGiftRecordLogRepository.save(orderGiftRecordLog);
    }



    /**
     * 根据客户id获取客户信息
     * @param customerId
     * @return
     */
    private CustomerGetByIdResponse getCustomer(String customerId){
        return customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
    }

    /**
     * 获取活动分类
     * @return
     */
    protected abstract ActivityCategoryEnum getActivityCategory();

    /**
     * 查看购买的商品是否有已经处理在处理返积分的记录
     * @param
     */
    protected abstract List<OrderGiftRecord> listValidBackRecord(String activityId, String customerId, String skuId, ActivityCategoryEnum activityCategoryEnum);


    /**
     * 获取 OrderGiftRecord 对象信息
     * @return
     */
    protected abstract OrderGiftRecord getOrderGiftRecordModel(SkuNormalActivityResp skuNormalActivityParam, SimpleTradeResp simpleTradeResp);


    protected abstract List<String> listBlackListCustomerId();

    /**
     * 新增记录/更新记录
     * @param orderGiftRecord
     */
    private OrderGiftRecord saveGiftRecord(OrderGiftRecord orderGiftRecord) {
        orderGiftRecord.setUpdateTime(LocalDateTime.now());
        OrderGiftRecord orderGiftRecordModel = payOrderGiftRecordRepository.save(orderGiftRecord);
        this.addGiftRecordLog(orderGiftRecordModel);
        return orderGiftRecordModel;
    }

    /**
     * 创建订单成功后调用
     * @param message
     */
    public void afterCreateOrder(String message){
        //转换对象
        log.info("PayOrderGiftRecordService afterCreateOrder message: {}", message);
        RecordMessageMq recordMessageMq = JSON.parseObject(message, RecordMessageMq.class);
        SimpleTradeResp simpleTradeResp = this.filterSimpleTrade(recordMessageMq);
        if (simpleTradeResp == null) {
            return;
        }

        List<SkuNormalActivityResp> skuNormalActivitys = this.listRunningNormalActivity(simpleTradeResp.getSkuIds(), recordMessageMq.getChannelTypes());
        if (CollectionUtils.isEmpty(skuNormalActivitys)) {
            log.info("PayOrderGiftRecordService afterCreateOrder message: {} 没有活动信息不执行返积分操作", message);
            return;
        }

        for (SkuNormalActivityResp skuNormalActivityParam : skuNormalActivitys) {
            //查看购买的商品是否有已经处理在处理返积分的记录
            List<OrderGiftRecord> orderGiftRecords = this.listValidBackRecord(skuNormalActivityParam.getNormalActivityId()+"", simpleTradeResp.getCustomerId(), skuNormalActivityParam.getSkuId(), this.getActivityCategory());
            if (!CollectionUtils.isEmpty(orderGiftRecords)) {
                log.info("PayOrderGiftRecordService afterCreateOrder activityId:{},customerId:{},skuId:{} 存在正在处理中的记录 不再添加记录",
                        skuNormalActivityParam.getNormalActivityId(), simpleTradeResp.getCustomerId(), skuNormalActivityParam.getSkuId());
                continue;
            }
            //新增记录
            this.saveGiftRecord(this.getOrderGiftRecordModel(skuNormalActivityParam, simpleTradeResp));
        }
    }

    /**
     * 支付成功后调用
     * @param message
     */
    public void afterPayOrderLock(String message) {
        log.info("PayOrderGiftRecordService afterPayOrder message: {}", message);
        RecordMessageMq recordMessageMq = JSON.parseObject(message, RecordMessageMq.class);
        SimpleTradeResp simpleTradeResp = this.filterSimpleTrade(recordMessageMq);
        if (simpleTradeResp == null) {
            return;
        }


        //查看当前是否有下单返积分记录
        OrderGiftRecordSearchReq req = new OrderGiftRecordSearchReq();
        req.setOrderId(simpleTradeResp.getOrderId());
        req.setRecordCategory(ActivityCategoryEnum.ACTIVITY_POINT.getCode());
        List<OrderGiftRecord> orderGiftRecordsByOrder = payOrderGiftRecordRepository.findAll(payOrderGiftRecordRepository.packageWhere(req));
        //如果为空则表示没有记录信息
        if (CollectionUtils.isEmpty(orderGiftRecordsByOrder)) {
            log.info("PayOrderGiftRecordService afterPayOrder message: {} 订单下没有对应的记录信息", message);
            return;
        }

        //查看用户是否在返积分黑名单里面
        List<String> blackListCustomerIds = this.listBlackListCustomerId();

        //如果有记录，则查看当前用户是否有其他订单已经返还积分
        for (OrderGiftRecord orderGiftRecord : orderGiftRecordsByOrder) {

            //黑名单强制取消
            if (blackListCustomerIds.contains(orderGiftRecord.getCustomerId())) {
                //如果有记录则作废当前记录
                orderGiftRecord.setRecordStatus(RecordStateEnum.FORCE_CANCEL.getCode());
                this.saveGiftRecord(orderGiftRecord);
                continue;
            }


            List<OrderGiftRecord> orderGiftRecords = this.listValidBackRecord(orderGiftRecord.getActivityId(), simpleTradeResp.getCustomerId(), orderGiftRecord.getQuoteId(), this.getActivityCategory());
            //存在记录则作废
            if (!CollectionUtils.isEmpty(orderGiftRecords)) {
                log.info("PayOrderGiftRecordService afterPayOrderLock activityId:{},customerId:{},skuId:{} 存在记录不再执行返积分",
                        orderGiftRecord.getActivityId(), simpleTradeResp.getCustomerId(), orderGiftRecord.getQuoteId());

                //如果有记录则作废当前记录
                orderGiftRecord.setRecordStatus(RecordStateEnum.NORMAL_CANCEL.getCode());
                this.saveGiftRecord(orderGiftRecord);
                continue;
            }

            //如果没有记录，则锁定当前记录
            orderGiftRecord.setRecordStatus(RecordStateEnum.LOCK.getCode());
            this.saveGiftRecord(orderGiftRecord);


            try {
                //获取樊登账号信息
                CustomerGetByIdResponse customer = this.getCustomer(orderGiftRecord.getCustomerId());
                FanDengAddPointReq fanDengAddPointReq = new FanDengAddPointReq();
                fanDengAddPointReq.setUserNo(customer.getFanDengUserNo());
                fanDengAddPointReq.setNum(orderGiftRecord.getPer().longValue());
                fanDengAddPointReq.setType(FanDengChangeTypeEnum.plus.getCode());
                fanDengAddPointReq.setSourceId(orderGiftRecord.getOrderId());
                fanDengAddPointReq.setDescription(String.format("订单%s返还积分%s", orderGiftRecord.getOrderId(), orderGiftRecord.getPer()));
                BaseResponse baseResponse = externalProvider.changePoint(fanDengAddPointReq);
                if (Objects.equals(CommonErrorCode.SUCCESSFUL, baseResponse.getCode())) {
                    orderGiftRecord.setRecordStatus(RecordStateEnum.SUCCESS.getCode());
                    this.saveGiftRecord(orderGiftRecord);
                }
            } catch (Exception ex) {
                log.error("PayOrderGiftRecordService afterPayOrderLock activityId:{},customerId:{},skuId:{} 加减积分异常",
                        orderGiftRecord.getActivityId(), simpleTradeResp.getCustomerId(), orderGiftRecord.getQuoteId());
            }
        }
    }
}
