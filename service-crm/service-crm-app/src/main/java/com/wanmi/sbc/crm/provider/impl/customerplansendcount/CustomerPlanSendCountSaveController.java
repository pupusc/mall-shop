package com.wanmi.sbc.crm.provider.impl.customerplansendcount;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customerplansendcount.CustomerPlanSendCountSaveProvider;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountAddRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountAddResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountModifyRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountModifyResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountDelByIdListRequest;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanSendCountService;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSendCount;
import javax.validation.Valid;

/**
 * <p>权益礼包优惠券发放统计表保存服务接口实现</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@RestController
@Validated
public class CustomerPlanSendCountSaveController implements CustomerPlanSendCountSaveProvider {

	@Autowired
	private CustomerPlanSendCountService customerPlanSendCountService;

	@Override
	public BaseResponse<CustomerPlanSendCountAddResponse> add(@RequestBody @Valid CustomerPlanSendCountAddRequest customerPlanSendCountAddRequest) {
		CustomerPlanSendCount customerPlanSendCount = new CustomerPlanSendCount();
		KsBeanUtil.copyPropertiesThird(customerPlanSendCountAddRequest, customerPlanSendCount);
		return BaseResponse.success(new CustomerPlanSendCountAddResponse(
				customerPlanSendCountService.wrapperVo(customerPlanSendCountService.add(customerPlanSendCount))));
	}

	@Override
	public BaseResponse<CustomerPlanSendCountModifyResponse> modify(@RequestBody @Valid CustomerPlanSendCountModifyRequest customerPlanSendCountModifyRequest) {
		CustomerPlanSendCount customerPlanSendCount = new CustomerPlanSendCount();
		KsBeanUtil.copyPropertiesThird(customerPlanSendCountModifyRequest, customerPlanSendCount);
		return BaseResponse.success(new CustomerPlanSendCountModifyResponse(
				customerPlanSendCountService.wrapperVo(customerPlanSendCountService.modify(customerPlanSendCount))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerPlanSendCountDelByIdRequest customerPlanSendCountDelByIdRequest) {
		customerPlanSendCountService.deleteById(customerPlanSendCountDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerPlanSendCountDelByIdListRequest customerPlanSendCountDelByIdListRequest) {
		customerPlanSendCountService.deleteByIdList(customerPlanSendCountDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

