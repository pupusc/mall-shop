package com.wanmi.sbc.crm.api.provider.rfmsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.rfmsetting.*;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingAddResponse;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>rfm参数配置保存服务Provider</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@FeignClient(value = "${application.crm.name}",contextId = "RfmSettingSaveProvider")
public interface RfmSettingSaveProvider {

	/**
	 * 新增rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingAddRequest rfm参数配置新增参数结构 {@link RfmSettingAddRequest}
	 * @return 新增的rfm参数配置信息 {@link RfmSettingAddResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/add")
	BaseResponse<RfmSettingAddResponse> add(@RequestBody @Valid RfmSettingAddRequest rfmSettingAddRequest);

	/**
	 * 修改rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingModifyRequest rfm参数配置修改参数结构 {@link RfmSettingModifyRequest}
	 * @return 修改的rfm参数配置信息 {@link RfmSettingModifyResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/modify")
	BaseResponse<RfmSettingModifyResponse> modify(@RequestBody @Valid RfmSettingModifyRequest rfmSettingModifyRequest);

	/**
	 * 单个删除rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingDelByIdRequest 单个删除参数结构 {@link RfmSettingDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid RfmSettingDelByIdRequest rfmSettingDelByIdRequest);

	/**
	 * 批量删除rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingDelByIdListRequest 批量删除参数结构 {@link RfmSettingDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid RfmSettingDelByIdListRequest rfmSettingDelByIdListRequest);

	/**
	 * 配置rfm参数API
	 *
	 * @author zhanglingke
	 * @param rfmSettingRequest rfm参数配置新增参数结构 {@link RfmSettingRequest}
	 * @return 配置rfm参数API {@link BaseResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/allocation")
	BaseResponse allocation(@RequestBody @Valid RfmSettingRequest rfmSettingRequest);

}

