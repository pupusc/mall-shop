package com.wanmi.sbc.marketing.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.marketing.api.provider.constants.MarketingJmsDestinationConstants;
import com.wanmi.sbc.marketing.api.request.coupon.CouponGroupAddRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityModifyStatisticsNumByIdRequest;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityService;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityConsumerService;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * MQ消费者
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Slf4j
@Component
@EnableBinding(MarketingSink.class)
public class ConsumerService {

    @Autowired
    private CouponActivityService couponActivityService;

    @Autowired
    private GrouponActivityService grouponActivityService;

    @Autowired
    private GrouponActivityConsumerService grouponActivityConsumerService;

    /**
     * 邀新注册-发放优惠券
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_MARKET_COUPON_INVITE_NEW_ADD)
    public void addCouponGroupFromInviteNew(String json) {
        try {
            CouponGroupAddRequest request = JSONObject.parseObject(json, CouponGroupAddRequest.class);
            Boolean result = couponActivityService.sendCouponGroup(request);
            log.info("邀新注册-发放优惠券，是否成功 ? {}",Boolean.FALSE.equals(result) ? "失败" : "成功");
        } catch (Exception e) {
            log.error("邀新注册-发放优惠券，发生异常! param={}", json, e);
        }
    }

    /**
     * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
     * @return
     */
    @StreamListener(MarketingJmsDestinationConstants.Q_MARKET_GROUPON_MODIFY_STATISTICS_NUM)
    @Transactional
    public void  updateStatisticsNumByGrouponActivityId(String json) {
        try {
            GrouponActivityModifyStatisticsNumByIdRequest request = JSONObject.parseObject(json, GrouponActivityModifyStatisticsNumByIdRequest.class);
            Integer result = grouponActivityConsumerService.updateStatisticsNumByGrouponActivityId(request.getGrouponActivityId(),request.getGrouponNum(),request.getGrouponOrderStatus());
            log.info("根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数），参数详细信息：{}，是否成功 ? {}",json,result > 0 ? "成功" : "失败" );
        } catch (Exception e) {
            log.error("根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数），发生异常! param={}", json, e);
        }
    }

}
