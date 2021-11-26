package com.wanmi.sbc.crm.api.provider.customerplansendcount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountByIdRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>权益礼包优惠券发放统计表查询服务Provider</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomerPlanSendCountQueryProvider")
public interface CustomerPlanSendCountQueryProvider {

	/**
	 * 单个查询权益礼包优惠券发放统计表API
	 *
	 * @author zhanghao
	 * @param customerPlanSendCountByIdRequest 单个查询权益礼包优惠券发放统计表请求参数 {@link CustomerPlanSendCountByIdRequest}
	 * @return 权益礼包优惠券发放统计表详情 {@link CustomerPlanSendCountByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplansendcount/get-by-plan-id")
	BaseResponse<CustomerPlanSendCountByIdResponse> getByPlanId(@RequestBody @Valid CustomerPlanSendCountByIdRequest customerPlanSendCountByIdRequest);

}

