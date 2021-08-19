package com.wanmi.sbc.crm.provider.impl.customerplan;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customerplan.CustomerPlanSaveProvider;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanAddRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanModifyPauseFlagRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanModifyRequest;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p> 人群运营计划保存服务接口实现</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@RestController
@Validated
public class CustomerPlanSaveController implements CustomerPlanSaveProvider {
	@Autowired
	private CustomerPlanService customerPlanService;

	@Override
	public BaseResponse add(@RequestBody @Valid CustomerPlanAddRequest customerPlanAddRequest) {
        customerPlanService.add(customerPlanAddRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modify(@RequestBody @Valid CustomerPlanModifyRequest customerPlanModifyRequest) {
        customerPlanService.modify(customerPlanModifyRequest);
        return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerPlanDelByIdRequest customerPlanDelByIdRequest) {
		customerPlanService.deleteById(customerPlanDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

    @Override
	public BaseResponse modifyPauseFlag(@RequestBody @Valid CustomerPlanModifyPauseFlagRequest request) {
        customerPlanService.modifyPauseFlag(request);
        return BaseResponse.SUCCESSFUL();
    }
}

