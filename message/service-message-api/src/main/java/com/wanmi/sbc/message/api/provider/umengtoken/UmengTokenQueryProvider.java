package com.wanmi.sbc.message.api.provider.umengtoken;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenByIdRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenListRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenPageRequest;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenByIdResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenListResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>友盟推送设备与会员关系查询服务Provider</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@FeignClient(value = "${application.message.name}",contextId = "UmengTokenQueryProvider")
public interface UmengTokenQueryProvider {

	/**
	 * 分页查询友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenPageReq 分页请求参数和筛选对象 {@link UmengTokenPageRequest}
	 * @return 友盟推送设备与会员关系分页列表信息 {@link UmengTokenPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/page")
	BaseResponse<UmengTokenPageResponse> page(@RequestBody @Valid UmengTokenPageRequest umengTokenPageReq);

	/**
	 * 列表查询友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenListReq 列表请求参数和筛选对象 {@link UmengTokenListRequest}
	 * @return 友盟推送设备与会员关系的列表信息 {@link UmengTokenListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/list")
	BaseResponse<UmengTokenListResponse> list(@RequestBody @Valid UmengTokenListRequest umengTokenListReq);

	/**
	 * 单个查询友盟推送设备与会员关系API
	 *
	 * @author bob
	 * @param umengTokenByIdRequest 单个查询友盟推送设备与会员关系请求参数 {@link UmengTokenByIdRequest}
	 * @return 友盟推送设备与会员关系详情 {@link UmengTokenByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/umengtoken/get-by-id")
	BaseResponse<UmengTokenByIdResponse> getById(@RequestBody @Valid UmengTokenByIdRequest umengTokenByIdRequest);

}

