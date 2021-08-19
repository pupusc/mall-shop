package com.wanmi.sbc.crm.api.provider.customerplan;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanAddRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanModifyPauseFlagRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p> 人群运营计划保存服务Provider</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomerPlanSaveProvider")
public interface CustomerPlanSaveProvider {

	/**
	 * 新增 人群运营计划API
	 *
	 * @author dyt
	 * @param customerPlanAddRequest  人群运营计划新增参数结构 {@link CustomerPlanAddRequest}
	 * @return 新增结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplan/add")
	BaseResponse add(@RequestBody @Valid CustomerPlanAddRequest customerPlanAddRequest);

	/**
	 * 修改 人群运营计划API
	 *
	 * @author dyt
	 * @param customerPlanModifyRequest  人群运营计划修改参数结构 {@link CustomerPlanModifyRequest}
	 * @return 修改结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplan/modify")
	BaseResponse modify(@RequestBody @Valid CustomerPlanModifyRequest
                                                            customerPlanModifyRequest);

	/**
	 * 单个删除 人群运营计划API
	 *
	 * @author dyt
	 * @param customerPlanDelByIdRequest 单个删除参数结构 {@link CustomerPlanDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/customerplan/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CustomerPlanDelByIdRequest customerPlanDelByIdRequest);

    /**
     * 修改暂停标志 人群运营计划API
     *
     * @author dyt
     * @param request  人群运营计划修改修改暂停标志参数结构 {@link CustomerPlanModifyPauseFlagRequest}
     * @return 修改结果 {@link BaseResponse}
     */
    @PostMapping("/crm/${application.crm.version}/customerplan/modify-pause-flag")
    BaseResponse modifyPauseFlag(@RequestBody @Valid CustomerPlanModifyPauseFlagRequest request);
}

