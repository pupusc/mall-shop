package com.wanmi.sbc.marketing.coupon.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeQueryRequest;
import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import com.wanmi.sbc.marketing.coupon.model.root.CouponActivityConfig;
import com.wanmi.sbc.marketing.coupon.model.root.CouponCode;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.repository.CouponInfoRepository;
import com.wanmi.sbc.marketing.coupon.service.CouponActivityConfigService;
import com.wanmi.sbc.marketing.coupon.service.CouponCodeService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableBinding(PaidCouponMqSink.class)
public class PaidCouponsMqService {

    @Autowired
    private CouponActivityConfigService couponActivityConfigService;

    @Autowired
    private CouponInfoRepository couponInfoRepository;

    @Autowired
    private CouponCodeService couponCodeService;


    /**
     * mq接收发放权益优惠券的信息
     *
     * @param msg
     */
    @StreamListener(PaidCouponMqSink.INPUT)
    public void receiveCoupon(String msg) {
        log.info("开始消费发券消息:{} couponCodeService: {}",msg, couponCodeService);
        // 解析消息数据
        Map<String, Object> response = JSONObject.parseObject(msg);
        String customerId = response.get("customerId").toString();
        String activityId = response.get("activityId").toString();
/*        List<CouponCode> statusCouponCode = couponCodeService.findNotUseStatusCouponCode(
                CouponCodeQueryRequest.builder().customerId(customerId)
                        .activityId(activityId).build());*/
//        if (CollectionUtils.isEmpty(statusCouponCode)) {
            // 查询券礼包权益关联的优惠券活动配置列表
            List<CouponActivityConfig> couponActivityConfigList = couponActivityConfigService.queryByActivityId(activityId);
            // 根据配置查询需要发放的优惠券列表
            List<CouponInfo> couponInfoList = couponInfoRepository.queryByIds(couponActivityConfigList.stream().map(
                    CouponActivityConfig::getCouponId).collect(Collectors.toList()));
//            log.info("根据配置查询需要发放的优惠券列表:{}",couponInfoList);
            // 组装优惠券发放数据
            List<GetCouponGroupResponse> getCouponGroupResponse = KsBeanUtil.copyListProperties(couponInfoList, GetCouponGroupResponse.class);
            getCouponGroupResponse = getCouponGroupResponse.stream().peek(item -> couponActivityConfigList.forEach(config -> {
                if (item.getCouponId().equals(config.getCouponId())) {
                    item.setTotalCount(config.getTotalCount());
                }
            })).collect(Collectors.toList());
//            log.info("需要发放的优惠券：{}",getCouponGroupResponse);
            // 批量发放优惠券
            couponCodeService.sendBatchCouponCodeByCustomer(getCouponGroupResponse, customerId, activityId);
//        }
        log.info("优惠券发放结束:{} ",customerId);
    }
}
