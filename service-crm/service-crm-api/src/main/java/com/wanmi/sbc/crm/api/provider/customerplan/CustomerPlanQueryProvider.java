package com.wanmi.sbc.crm.api.provider.customerplan;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanByActivityIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanPageRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByActivityIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanByIdResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanListResponse;
import com.wanmi.sbc.crm.api.response.customerplan.CustomerPlanPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p> 人群运营计划查询服务Provider</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomerPlanQueryProvider")
public interface CustomerPlanQueryProvider {

	/**
	 * 分页查询 人群运营计划API
	 *
	 * @author dyt
	 * @param customerPlanPageReq 分页请求参数和筛选对象 {@link CustomerPlanPageRequest}
	 * @return  人群运营计划分页列表信息 {@link CustomerPlanPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplan/page")
	BaseResponse<CustomerPlanPageResponse> page(@RequestBody @Valid CustomerPlanPageRequest customerPlanPageReq);

	/**
	 * 单个查询 人群运营计划API
	 *
	 * @author dyt
	 * @param customerPlanByIdRequest 单个查询 人群运营计划请求参数 {@link CustomerPlanByIdRequest}
	 * @return  人群运营计划详情 {@link CustomerPlanByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplan/get-by-id")
	BaseResponse<CustomerPlanByIdResponse> getById(@RequestBody @Valid CustomerPlanByIdRequest customerPlanByIdRequest);

    /**
     * 根据活动id查询 人群运营计划API
     *
     * @author dyt
     * @param customerPlanByIdRequest 活动id查询 人群运营计划请求参数 {@link CustomerPlanByActivityIdRequest}
     * @return  人群运营计划详情 {@link CustomerPlanByActivityIdResponse}
     */
    @PostMapping("/crm/${application.crm.version}/customerplan/get-by-activity-id")
    BaseResponse<CustomerPlanByActivityIdResponse> getByActivityId(@RequestBody @Valid CustomerPlanByActivityIdRequest customerPlanByIdRequest);

    /**
     * 普通查询 人群运营计划API
     *
     * @author dyt
     * @param customerPlanListReq 请求参数和筛选对象 {@link CustomerPlanListRequest}
     * @return  人群运营计划列表信息 {@link CustomerPlanListResponse}
     */
    @PostMapping("/crm/${application.crm.version}/customerplan/list")
    BaseResponse<CustomerPlanListResponse> list(@RequestBody @Valid CustomerPlanListRequest customerPlanListReq);

}

