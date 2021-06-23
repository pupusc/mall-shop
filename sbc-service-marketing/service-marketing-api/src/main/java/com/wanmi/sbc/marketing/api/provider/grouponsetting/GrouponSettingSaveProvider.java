package com.wanmi.sbc.marketing.api.provider.grouponsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponsetting.*;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingAddResponse;
import com.wanmi.sbc.marketing.api.response.grouponsetting.GrouponSettingModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>拼团活动设置表保存服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponSettingSaveProvider")
public interface GrouponSettingSaveProvider {

	/**
	 * 新增拼团活动设置表API
	 *
	 * @author groupon
	 * @param grouponSettingAddRequest 拼团活动设置表新增参数结构 {@link GrouponSettingAddRequest}
	 * @return 新增的拼团活动设置表信息 {@link GrouponSettingAddResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/add")
	BaseResponse<GrouponSettingAddResponse> add(@RequestBody @Valid GrouponSettingAddRequest grouponSettingAddRequest);

	/**
	 * 修改拼团活动设置表API
	 *
	 * @author groupon
	 * @param grouponSettingModifyRequest 拼团活动设置表修改参数结构 {@link GrouponSettingModifyRequest}
	 * @return 修改的拼团活动设置表信息 {@link GrouponSettingModifyResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/modify")
	BaseResponse<GrouponSettingModifyResponse> modify(@RequestBody @Valid GrouponSettingModifyRequest
                                                              grouponSettingModifyRequest);

	/**
	 * 单个删除拼团活动设置表API
	 *
	 * @author groupon
	 * @param grouponSettingDelByIdRequest 单个删除参数结构 {@link GrouponSettingDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GrouponSettingDelByIdRequest grouponSettingDelByIdRequest);


	/**
	 * 保存拼团商品审核开关设置
	 * @param grouponAuditFlagSaveRequest
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/save-audit")
	BaseResponse saveAudit(@RequestBody @Valid GrouponAuditFlagSaveRequest
								   grouponAuditFlagSaveRequest);


	/**
	 *保存拼团广告设置
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/save-poster")
	BaseResponse savePoster(@RequestBody @Valid GrouponPosterSaveRequest
									grouponPosterSaveRequest);

	/**
	 * 保存拼团规则
	 * @param grouponRuleSaveRequest
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/grouponsetting/save-rule")
	BaseResponse saveRule(@RequestBody @Valid GrouponRuleSaveRequest
								  grouponRuleSaveRequest);
}

