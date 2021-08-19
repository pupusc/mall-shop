package com.wanmi.sbc.crm.provider.impl.customerplanconversion;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customerplanconversion.CustomerPlanConversionQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionByPlanIdRequest;
import com.wanmi.sbc.crm.api.response.customerplanconversion.CustomerPlanConversionByIdResponse;
import com.wanmi.sbc.crm.customerplanconversion.model.root.CustomerPlanConversion;
import com.wanmi.sbc.crm.customerplanconversion.service.CustomerPlanConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>运营计划转化效果查询服务接口实现</p>
 *
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@RestController
@Validated
public class CustomerPlanConversionQueryController implements CustomerPlanConversionQueryProvider {
    @Autowired
    private CustomerPlanConversionService customerPlanConversionService;

    @Override
    public BaseResponse<CustomerPlanConversionByIdResponse> getByPlanId(@RequestBody @Valid CustomerPlanConversionByPlanIdRequest customerPlanConversionByPlanIdRequest) {
        CustomerPlanConversion customerPlanConversion =
                customerPlanConversionService.getByPlanId(customerPlanConversionByPlanIdRequest.getPlanId());
        return BaseResponse.success(new CustomerPlanConversionByIdResponse(customerPlanConversionService.wrapperVo(customerPlanConversion)));
    }
}

