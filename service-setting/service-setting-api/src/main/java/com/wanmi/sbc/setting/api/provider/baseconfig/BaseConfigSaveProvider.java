package com.wanmi.sbc.setting.api.provider.baseconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigAddRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigAddResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigModifyResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>基本设置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@FeignClient(value = "${application.setting.name}", contextId = "BaseConfigSaveProvider")
public interface BaseConfigSaveProvider {

	/**
	 * 新增基本设置API
	 *
	 * @author lq
	 * @param baseConfigAddRequest 基本设置新增参数结构 {@link BaseConfigAddRequest}
	 * @return 新增的基本设置信息 {@link BaseConfigAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/add")
	BaseResponse<BaseConfigAddResponse> add(@RequestBody @Valid BaseConfigAddRequest baseConfigAddRequest);

	/**
	 * 修改基本设置API
	 *
	 * @author lq
	 * @param baseConfigModifyRequest 基本设置修改参数结构 {@link BaseConfigModifyRequest}
	 * @return 修改的基本设置信息 {@link BaseConfigModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/modify")
	BaseResponse<BaseConfigModifyResponse> modify(@RequestBody @Valid BaseConfigModifyRequest baseConfigModifyRequest);

	/**
	 * 单个删除基本设置API
	 *
	 * @author lq
	 * @param baseConfigDelByIdRequest 单个删除参数结构 {@link BaseConfigDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid BaseConfigDelByIdRequest baseConfigDelByIdRequest);

	/**
	 * 批量删除基本设置API
	 *
	 * @author lq
	 * @param baseConfigDelByIdListRequest 批量删除参数结构 {@link BaseConfigDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid BaseConfigDelByIdListRequest baseConfigDelByIdListRequest);

}

