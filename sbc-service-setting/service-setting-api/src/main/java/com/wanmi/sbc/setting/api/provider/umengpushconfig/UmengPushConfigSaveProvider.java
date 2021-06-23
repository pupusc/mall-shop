package com.wanmi.sbc.setting.api.provider.umengpushconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigAddRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigDelByIdRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigAddResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>友盟push接口配置保存服务Provider</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@FeignClient(value = "${application.setting.name}", contextId = "UmengPushConfigSaveProvider")
public interface UmengPushConfigSaveProvider {

	/**
	 * 新增友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigAddRequest 友盟push接口配置新增参数结构 {@link UmengPushConfigAddRequest}
	 * @return 新增的友盟push接口配置信息 {@link UmengPushConfigAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/add")
	BaseResponse<UmengPushConfigAddResponse> add(@RequestBody @Valid UmengPushConfigAddRequest umengPushConfigAddRequest);

	/**
	 * 修改友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigModifyRequest 友盟push接口配置修改参数结构 {@link UmengPushConfigModifyRequest}
	 * @return 修改的友盟push接口配置信息 {@link UmengPushConfigModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/modify")
	BaseResponse<UmengPushConfigModifyResponse> modify(@RequestBody @Valid UmengPushConfigModifyRequest umengPushConfigModifyRequest);

	/**
	 * 单个删除友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigDelByIdRequest 单个删除参数结构 {@link UmengPushConfigDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid UmengPushConfigDelByIdRequest umengPushConfigDelByIdRequest);

	/**
	 * 批量删除友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigDelByIdListRequest 批量删除参数结构 {@link UmengPushConfigDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid UmengPushConfigDelByIdListRequest umengPushConfigDelByIdListRequest);

}

