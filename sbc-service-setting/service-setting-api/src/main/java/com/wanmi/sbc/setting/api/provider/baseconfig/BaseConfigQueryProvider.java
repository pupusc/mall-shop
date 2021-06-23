package com.wanmi.sbc.setting.api.provider.baseconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigPageRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigPageResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigListRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigListResponse;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.api.response.baseconfig.BossLogoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>基本设置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@FeignClient(value = "${application.setting.name}", contextId = "BaseConfigQueryProvider")
public interface BaseConfigQueryProvider {

	/**
	 * 分页查询基本设置API
	 *
	 * @author lq
	 * @param baseConfigPageReq 分页请求参数和筛选对象 {@link BaseConfigPageRequest}
	 * @return 基本设置分页列表信息 {@link BaseConfigPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/page")
	BaseResponse<BaseConfigPageResponse> page(@RequestBody @Valid BaseConfigPageRequest baseConfigPageReq);

	/**
	 * 列表查询基本设置API
	 *
	 * @author lq
	 * @param baseConfigListReq 列表请求参数和筛选对象 {@link BaseConfigListRequest}
	 * @return 基本设置的列表信息 {@link BaseConfigListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/list")
	BaseResponse<BaseConfigListResponse> list(@RequestBody @Valid BaseConfigListRequest baseConfigListReq);

	/**
	 * 单个查询基本设置API
	 *
	 * @author lq
	 * @param baseConfigByIdRequest 单个查询基本设置请求参数 {@link BaseConfigByIdRequest}
	 * @return 基本设置详情 {@link BaseConfigByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/baseconfig/get-by-id")
	BaseResponse<BaseConfigByIdResponse> getById(@RequestBody @Valid BaseConfigByIdRequest baseConfigByIdRequest);


	@PostMapping("/setting/${application.setting.version}/baseconfig/get-base-config")
	BaseResponse<BaseConfigRopResponse> getBaseConfig();


	@PostMapping("/setting/${application.setting.version}/baseconfig/query-boss-logo")
	BaseResponse<BossLogoResponse> queryBossLogo();
}
