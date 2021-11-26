package com.wanmi.sbc.crm.api.provider.customerplansendcount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountAddRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountAddResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountModifyRequest;
import com.wanmi.sbc.crm.api.response.customerplansendcount.CustomerPlanSendCountModifyResponse;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplansendcount.CustomerPlanSendCountDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>权益礼包优惠券发放统计表保存服务Provider</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomerPlanSendCountSaveProvider")
public interface CustomerPlanSendCountSaveProvider {

	/**
	 * 新增权益礼包优惠券发放统计表API
	 *
	 * @author zhanghao
	 * @param customerPlanSendCountAddRequest 权益礼包优惠券发放统计表新增参数结构 {@link CustomerPlanSendCountAddRequest}
	 * @return 新增的权益礼包优惠券发放统计表信息 {@link CustomerPlanSendCountAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplansendcount/add")
	BaseResponse<CustomerPlanSendCountAddResponse> add(@RequestBody @Valid CustomerPlanSendCountAddRequest customerPlanSendCountAddRequest);

	/**
	 * 修改权益礼包优惠券发放统计表API
	 *
	 * @author zhanghao
	 * @param customerPlanSendCountModifyRequest 权益礼包优惠券发放统计表修改参数结构 {@link CustomerPlanSendCountModifyRequest}
	 * @return 修改的权益礼包优惠券发放统计表信息 {@link CustomerPlanSendCountModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplansendcount/modify")
	BaseResponse<CustomerPlanSendCountModifyResponse> modify(@RequestBody @Valid CustomerPlanSendCountModifyRequest customerPlanSendCountModifyRequest);

	/**
	 * 单个删除权益礼包优惠券发放统计表API
	 *
	 * @author zhanghao
	 * @param customerPlanSendCountDelByIdRequest 单个删除参数结构 {@link CustomerPlanSendCountDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplansendcount/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CustomerPlanSendCountDelByIdRequest customerPlanSendCountDelByIdRequest);

	/**
	 * 批量删除权益礼包优惠券发放统计表API
	 *
	 * @author zhanghao
	 * @param customerPlanSendCountDelByIdListRequest 批量删除参数结构 {@link CustomerPlanSendCountDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplansendcount/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid CustomerPlanSendCountDelByIdListRequest customerPlanSendCountDelByIdListRequest);

}

