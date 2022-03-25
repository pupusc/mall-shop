package com.wanmi.sbc.job;

import com.soybean.mall.order.api.provider.order.OrderCouponProvider;
import com.soybean.mall.wx.mini.common.bean.request.WxSendMessageRequest;
import com.soybean.mall.wx.mini.common.controller.CommonController;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerWithOpenIdPageRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplerVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeByCustomerIdsRequest;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 支付完成发放优惠券
 */
@Component
@JobHandler(value = "sendCouponAfterPaidJobHandler")
@Slf4j
public class SendCouponAfterPaidJobHandler extends IJobHandler {

    //分布式锁名称
    private static final String SEND_COUPON_AFTER_PAID_LOCK = "SEND_COUPON_AFTER_PAID_LOCK";

    @Autowired
    private OrderCouponProvider orderCouponProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {

        RLock lock = redissonClient.getLock(SEND_COUPON_AFTER_PAID_LOCK);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("支付完成发放优惠券start");
        orderCouponProvider.sendCouponAfterPaid();
        return SUCCESS;

    }

}