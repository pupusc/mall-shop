package com.wanmi.sbc.customer.provider.impl.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.levelrights.CustomerLevelRightsCouponAnalyseProvider;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsCouponAnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>定时任务</p>
 */
@Validated
@RestController
public class CustomerLevelRightsCouponAnalyseController implements CustomerLevelRightsCouponAnalyseProvider {

    @Autowired
    private CustomerLevelRightsCouponAnalyseService customerLevelRightsCouponAnalyseService;

    @Override
    public BaseResponse<CustomerLevelRightsListResponse> queryIssueCouponsData() {
        return BaseResponse.success(customerLevelRightsCouponAnalyseService.queryIssueCouponsData());
    }
}
