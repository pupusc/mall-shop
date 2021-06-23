package com.wanmi.sbc.setting.api.provider.systemprivacypolicy;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.systemprivacypolicy.*;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyAddResponse;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyModifyResponse;
import com.wanmi.sbc.setting.api.response.systemprivacypolicy.SystemPrivacyPolicyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>隐私政策保存服务Provider</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemPrivacyPolicySaveProvider")
public interface SystemPrivacyPolicySaveProvider {

	/**
	 * 新增隐私政策API
	 *
	 * @author yangzhen
	 * @param systemPrivacyPolicyAddRequest 隐私政策新增参数结构 {@link SystemPrivacyPolicyAddRequest}
	 * @return 新增的隐私政策信息 {@link SystemPrivacyPolicyAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemprivacypolicy/add")
	BaseResponse<SystemPrivacyPolicyAddResponse> add(@RequestBody @Valid SystemPrivacyPolicyAddRequest systemPrivacyPolicyAddRequest);

	/**
	 * 修改隐私政策API
	 *
	 * @author yangzhen
	 * @param systemPrivacyPolicyModifyRequest 隐私政策修改参数结构 {@link SystemPrivacyPolicyModifyRequest}
	 * @return 修改的隐私政策信息 {@link SystemPrivacyPolicyModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemprivacypolicy/modify")
	BaseResponse<SystemPrivacyPolicyModifyResponse> modify(@RequestBody @Valid SystemPrivacyPolicyModifyRequest systemPrivacyPolicyModifyRequest);

	/**
	 * 单个删除隐私政策API
	 *
	 * @author yangzhen
	 * @param systemPrivacyPolicyDelByIdRequest 单个删除参数结构 {@link SystemPrivacyPolicyDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemprivacypolicy/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SystemPrivacyPolicyDelByIdRequest systemPrivacyPolicyDelByIdRequest);

	/**
	 * 批量删除隐私政策API
	 *
	 * @author yangzhen
	 * @param systemPrivacyPolicyDelByIdListRequest 批量删除参数结构 {@link SystemPrivacyPolicyDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemprivacypolicy/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid SystemPrivacyPolicyDelByIdListRequest systemPrivacyPolicyDelByIdListRequest);



	/**
	 * 修改/保存隐私政策API
	 *
	 * @author yangzhen
	 * @param systemPrivacyPolicyRequest 隐私政策修改参数结构 {@link SystemPrivacyPolicyRequest}
	 * @return 修改的隐私政策信息 {@link SystemPrivacyPolicyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/systemprivacypolicy/modify-or-add")
	BaseResponse modifyOrAdd(@RequestBody @Valid SystemPrivacyPolicyRequest systemPrivacyPolicyRequest);

}

