package com.wanmi.sbc.crm.customerplansendcount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.crm.api.provider.customerplansendcount.CustomerPlanSendCountQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountByIdRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountByIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "权益礼包优惠券发放统计表管理API", tags = "CustomerPlanSendCountController")
@RestController
@RequestMapping(value = "/customerplansendcount")
public class CustomerPlanSendCountController {

    @Autowired
    private CustomerPlanSendCountQueryProvider customerPlanSendCountQueryProvider;


    @ApiOperation(value = "根据planId查询权益礼包优惠券发放统计表")
    @GetMapping("/{planId}")
    public BaseResponse<CustomerPlanSendCountByIdResponse> getById(@PathVariable Long planId) {
        if (planId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CustomerPlanSendCountByIdRequest idReq = new CustomerPlanSendCountByIdRequest();
        idReq.setPlanId(planId);
        return customerPlanSendCountQueryProvider.getByPlanId(idReq);
    }

}
