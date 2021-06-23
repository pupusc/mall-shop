package com.wanmi.sbc.message.api.provider.umengtoken;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenAddRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenDelByIdListRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenDelByIdRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenModifyRequest;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenAddResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>友盟推送设备与会员关系保存服务Provider</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@FeignClient(value = "${application.message.name}",contextId = "UmengTokenSaveProvider")
public interface UmengTokenSaveProvider {

	/**
	 * 新增友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenAddRequest 友盟推送设备与会员关系新增参数结构 {@link UmengTokenAddRequest}
	 * @return 新增的友盟推送设备与会员关系信息 {@link UmengTokenAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/add")
	BaseResponse<UmengTokenAddResponse> add(@RequestBody @Valid UmengTokenAddRequest umengTokenAddRequest);

	/**
	 * 修改友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenModifyRequest 友盟推送设备与会员关系修改参数结构 {@link UmengTokenModifyRequest}
	 * @return 修改的友盟推送设备与会员关系信息 {@link UmengTokenModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/modify")
	BaseResponse<UmengTokenModifyResponse> modify(@RequestBody @Valid UmengTokenModifyRequest umengTokenModifyRequest);

	/**
	 * 单个删除友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenDelByIdRequest 单个删除参数结构 {@link UmengTokenDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid UmengTokenDelByIdRequest umengTokenDelByIdRequest);

	/**
	 * 批量删除友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenDelByIdListRequest 批量删除参数结构 {@link UmengTokenDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid UmengTokenDelByIdListRequest umengTokenDelByIdListRequest);

}

