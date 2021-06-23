package com.wanmi.sbc.setting.api.provider.umengpushconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigListRequest;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigPageRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigListResponse;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>友盟push接口配置查询服务Provider</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@FeignClient(value = "${application.setting.name}",contextId = "UmengPushConfigQueryProvider")
public interface UmengPushConfigQueryProvider {

	/**
	 * 分页查询友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigPageReq 分页请求参数和筛选对象 {@link UmengPushConfigPageRequest}
	 * @return 友盟push接口配置分页列表信息 {@link UmengPushConfigPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/page")
	BaseResponse<UmengPushConfigPageResponse> page(@RequestBody @Valid UmengPushConfigPageRequest umengPushConfigPageReq);

	/**
	 * 列表查询友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigListReq 列表请求参数和筛选对象 {@link UmengPushConfigListRequest}
	 * @return 友盟push接口配置的列表信息 {@link UmengPushConfigListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/list")
	BaseResponse<UmengPushConfigListResponse> list(@RequestBody @Valid UmengPushConfigListRequest umengPushConfigListReq);

	/**
	 * 单个查询友盟push接口配置API
	 *
	 * @author bob
	 * @param umengPushConfigByIdRequest 单个查询友盟push接口配置请求参数 {@link UmengPushConfigByIdRequest}
	 * @return 友盟push接口配置详情 {@link UmengPushConfigByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/umengpushconfig/get-by-id")
	BaseResponse<UmengPushConfigByIdResponse> getById(@RequestBody @Valid UmengPushConfigByIdRequest umengPushConfigByIdRequest);

}

