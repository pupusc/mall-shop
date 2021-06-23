package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理失效券码的定时任务
 */
@JobHandler(value = "expireVirtualCouponCodeJobHandler")
@Component
@Slf4j
public class ExpireVirtualCouponCodeJobHandler extends IJobHandler {

    @Autowired
    private VirtualCouponCodeProvider virtualCouponCodeProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        virtualCouponCodeProvider.expireVirtualCouponCode();
        return SUCCESS;
    }
}
