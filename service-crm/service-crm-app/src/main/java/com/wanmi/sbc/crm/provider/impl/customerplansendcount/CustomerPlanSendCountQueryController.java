package com.wanmi.sbc.crm.provider.impl.customerplansendcount;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customerplansendcount.CustomerPlanSendCountQueryProvider;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountByIdRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountByIdResponse;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanSendCountService;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlanSendCount;
import javax.validation.Valid;
/**
 * <p>权益礼包优惠券发放统计表查询服务接口实现</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@RestController
@Validated
public class CustomerPlanSendCountQueryController implements CustomerPlanSendCountQueryProvider {
	@Autowired
	private CustomerPlanSendCountService customerPlanSendCountService;

	@Override
	public BaseResponse<CustomerPlanSendCountByIdResponse> getByPlanId(@RequestBody @Valid CustomerPlanSendCountByIdRequest customerPlanSendCountByIdRequest) {
		CustomerPlanSendCount customerPlanSendCount =
				customerPlanSendCountService.getByPlanId(customerPlanSendCountByIdRequest.getPlanId());
		return BaseResponse.success(new CustomerPlanSendCountByIdResponse(customerPlanSendCountService.wrapperVo(customerPlanSendCount)));
	}

}

