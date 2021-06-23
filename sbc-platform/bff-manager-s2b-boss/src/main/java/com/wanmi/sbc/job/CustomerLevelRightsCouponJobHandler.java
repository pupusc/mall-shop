package com.wanmi.sbc.job;


import com.wanmi.sbc.marketing.api.provider.coupon.CouponCustomerRightsProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务Handler（Bean模式）
 * 查询是否存在符合会员等级权益发券的会员，每月n号发放优惠券
 *
 * @author bail 2019-3-24
 */
@JobHandler(value = "customerLevelRightsCouponJobHandler")
@Component
@Slf4j
public class CustomerLevelRightsCouponJobHandler extends IJobHandler {

    @Autowired
    private CouponCustomerRightsProvider couponCustomerRightsProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        couponCustomerRightsProvider.customerRightsIssueCoupons();
        return SUCCESS;
    }

}
