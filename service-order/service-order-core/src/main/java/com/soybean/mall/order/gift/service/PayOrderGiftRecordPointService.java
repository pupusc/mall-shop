package com.soybean.mall.order.gift.service;
import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.api.request.mq.RecordMessageMq;
import com.soybean.mall.order.api.response.mq.SimpleTradeResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.time.LocalDateTime;

import com.soybean.mall.order.api.request.record.OrderGiftRecordSearchReq;
import com.soybean.mall.order.gift.model.OrderGiftRecord;
import com.soybean.marketing.api.enums.ActivityCategoryEnum;
import com.soybean.marketing.api.resp.SkuNormalActivityResp;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.enums.FanDengChangeTypeEnum;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengAddPointReq;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.order.api.enums.RecordStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Description: 返还积分
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 8:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class PayOrderGiftRecordPointService extends PayOrderGiftRecordService {


    /**
     * 创建订单调用
     * @param message
     */
    @Override
    public void afterCreateOrder(String message) {
        //转换对象
        log.info("PayOrderGiftRecordPointService afterCreateOrder message: {}", message);
        RecordMessageMq recordMessageMq = JSON.parseObject(message, RecordMessageMq.class);
        SimpleTradeResp simpleTradeResp = super.filterSimpleTrade(recordMessageMq);
        if (simpleTradeResp == null) {
            return;
        }

        List<SkuNormalActivityResp> skuNormalActivitys = super.listRunningNormalActivity(simpleTradeResp.getSkuIds(), recordMessageMq.getChannelTypes());
        if (CollectionUtils.isEmpty(skuNormalActivitys)) {
            log.info("PayOrderGiftRecordPointService afterCreateOrder message: {} 没有活动信息不执行返积分操作", message);
            return;
        }

        for (SkuNormalActivityResp skuNormalActivityParam : skuNormalActivitys) {
            //查看购买的商品是否有已经处理在处理返积分的记录
            List<OrderGiftRecord> orderGiftRecords =
                    super.listValidBackRecord(
                            skuNormalActivityParam.getNormalActivityId() + "",
                            simpleTradeResp.getCustomerId(),
                            skuNormalActivityParam.getSkuId(),
                            ActivityCategoryEnum.ACTIVITY_POINT);
            if (!CollectionUtils.isEmpty(orderGiftRecords)) {
                log.info("PayOrderGiftRecordPointService afterCreateOrder activityId:{},customerId:{},skuId:{} 存在正在处理中的记录 不再添加记录",
                        skuNormalActivityParam.getNormalActivityId(), simpleTradeResp.getCustomerId(), skuNormalActivityParam.getSkuId());
                continue;
            }

            //新增记录
            OrderGiftRecord orderGiftRecord = new OrderGiftRecord();
            orderGiftRecord.setName("返积分活动");
            orderGiftRecord.setCustomerId(simpleTradeResp.getCustomerId());
            orderGiftRecord.setOrderId(simpleTradeResp.getOrderId());
            orderGiftRecord.setActivityId(skuNormalActivityParam.getNormalActivityId() + "");
            orderGiftRecord.setPer(skuNormalActivityParam.getNum());
            orderGiftRecord.setQuoteId(skuNormalActivityParam.getSkuId());
            orderGiftRecord.setQuoteName("goods_info");
            orderGiftRecord.setRecordCategory(ActivityCategoryEnum.ACTIVITY_POINT.getCode());
            orderGiftRecord.setRecordStatus(RecordStateEnum.CREATE.getCode());
            orderGiftRecord.setCreateTime(LocalDateTime.now());
            orderGiftRecord.setUpdateTime(LocalDateTime.now());
            orderGiftRecord.setDelFlag(DeleteFlag.NO);
            super.saveGiftRecord(orderGiftRecord);
        }
    }


    /**
     * 支付成功后调用
     * @param message
     */
    @Override
    public void afterPayOrderLock(String message) {
        log.info("PayOrderGiftRecordPointService afterPayOrder message: {}", message);
        RecordMessageMq recordMessageMq = JSON.parseObject(message, RecordMessageMq.class);
        SimpleTradeResp simpleTradeResp = super.filterSimpleTrade(recordMessageMq);
        if (simpleTradeResp == null) {
            return;
        }

        //查看用户是否在返积分黑名单里面 TODO


        //如果没有在黑名单
        OrderGiftRecordSearchReq req = new OrderGiftRecordSearchReq();
        req.setOrderId(simpleTradeResp.getOrderId());
        req.setRecordCategory(ActivityCategoryEnum.ACTIVITY_POINT.getCode());
        List<OrderGiftRecord> orderGiftRecordsByOrder = payOrderGiftRecordRepository.findAll(payOrderGiftRecordRepository.packageWhere(req));
        //如果为空则表示没有记录信息
        if (CollectionUtils.isEmpty(orderGiftRecordsByOrder)) {
            log.info("PayOrderGiftRecordPointService afterPayOrder message: {} 订单下没有对应的记录信息", message);
            return;
        }

        //如果有记录，则查看当前用户是否有其他订单已经返还积分
        for (OrderGiftRecord orderGiftRecord : orderGiftRecordsByOrder) {
            List<OrderGiftRecord> orderGiftRecords = super.listValidBackRecord(
                                        orderGiftRecord.getActivityId(),
                                        simpleTradeResp.getCustomerId(),
                                        orderGiftRecord.getQuoteId(),
                                        ActivityCategoryEnum.ACTIVITY_POINT);
            if (!CollectionUtils.isEmpty(orderGiftRecords)) {
                log.info("PayOrderGiftRecordPointService afterPayOrderLock activityId:{},customerId:{},skuId:{} 存在记录不再执行返积分",
                        orderGiftRecord.getActivityId(), simpleTradeResp.getCustomerId(), orderGiftRecord.getQuoteId());

                //如果有记录则作废当前记录
                orderGiftRecord.setRecordStatus(RecordStateEnum.NORMAL_CANCEL.getCode());
                super.saveGiftRecord(orderGiftRecord);
                continue;
            }

            //如果没有记录，则锁定当前记录
            orderGiftRecord.setRecordStatus(RecordStateEnum.LOCK.getCode());
            super.saveGiftRecord(orderGiftRecord);


            try {
                //调用添加接口信息
                //获取樊登账号信息
                CustomerGetByIdResponse customer = super.getCustomer(orderGiftRecord.getCustomerId());
                FanDengAddPointReq fanDengAddPointReq = new FanDengAddPointReq();
                fanDengAddPointReq.setUserNo(customer.getFanDengUserNo());
                fanDengAddPointReq.setNum(orderGiftRecord.getPer().longValue());
                fanDengAddPointReq.setType(FanDengChangeTypeEnum.plus.getCode());
                fanDengAddPointReq.setSourceId(orderGiftRecord.getOrderId());
                fanDengAddPointReq.setDescription(String.format("订单%s返还积分%s", orderGiftRecord.getOrderId(), orderGiftRecord.getPer()));
                BaseResponse baseResponse = externalProvider.changePoint(fanDengAddPointReq);
                if (Objects.equals(CommonErrorCode.SUCCESSFUL, baseResponse.getCode())) {
                    orderGiftRecord.setRecordStatus(RecordStateEnum.SUCCESS.getCode());
                    super.saveGiftRecord(orderGiftRecord);
                }
            } catch (Exception ex) {
                log.error("PayOrderGiftRecordPointService afterPayOrderLock activityId:{},customerId:{},skuId:{} 加减积分异常",
                        orderGiftRecord.getActivityId(), simpleTradeResp.getCustomerId(), orderGiftRecord.getQuoteId());
            }
        }
    }
}
