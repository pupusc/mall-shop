package com.wanmi.sbc.crm.api.provider.preferencetagdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailAddRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailAddResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailModifyRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailModifyResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailDelByIdRequest;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>偏好标签明细保存服务Provider</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@FeignClient(value = "${application.crm.name}", contextId = "PreferenceTagDetailProvider")
public interface PreferenceTagDetailProvider {

	/**
	 * 新增偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailAddRequest 偏好标签明细新增参数结构 {@link PreferenceTagDetailAddRequest}
	 * @return 新增的偏好标签明细信息 {@link PreferenceTagDetailAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/add")
	BaseResponse<PreferenceTagDetailAddResponse> add(@RequestBody @Valid PreferenceTagDetailAddRequest
                                                             preferenceTagDetailAddRequest);

	/**
	 * 修改偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailModifyRequest 偏好标签明细修改参数结构 {@link PreferenceTagDetailModifyRequest}
	 * @return 修改的偏好标签明细信息 {@link PreferenceTagDetailModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/modify")
	BaseResponse<PreferenceTagDetailModifyResponse> modify(@RequestBody @Valid PreferenceTagDetailModifyRequest preferenceTagDetailModifyRequest);

	/**
	 * 单个删除偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailDelByIdRequest 单个删除参数结构 {@link PreferenceTagDetailDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PreferenceTagDetailDelByIdRequest preferenceTagDetailDelByIdRequest);

	/**
	 * 批量删除偏好标签明细API
	 *
	 * @author dyt
	 * @param preferenceTagDetailDelByIdListRequest 批量删除参数结构 {@link PreferenceTagDetailDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/preferencetagdetail/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PreferenceTagDetailDelByIdListRequest preferenceTagDetailDelByIdListRequest);

}

