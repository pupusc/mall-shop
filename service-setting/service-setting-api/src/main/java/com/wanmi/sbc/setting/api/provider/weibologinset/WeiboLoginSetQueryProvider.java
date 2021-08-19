package com.wanmi.sbc.setting.api.provider.weibologinset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetPageRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetPageResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetListRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetListResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetByIdRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信登录配置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@FeignClient(value = "${application.setting.name}", contextId = "WeiboLoginSetQueryProvider")
public interface WeiboLoginSetQueryProvider {

	/**
	 * 分页查询微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetPageReq 分页请求参数和筛选对象 {@link WeiboLoginSetPageRequest}
	 * @return 微信登录配置分页列表信息 {@link WeiboLoginSetPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/page")
	BaseResponse<WeiboLoginSetPageResponse> page(@RequestBody @Valid WeiboLoginSetPageRequest weiboLoginSetPageReq);

	/**
	 * 列表查询微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetListReq 列表请求参数和筛选对象 {@link WeiboLoginSetListRequest}
	 * @return 微信登录配置的列表信息 {@link WeiboLoginSetListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/list")
	BaseResponse<WeiboLoginSetListResponse> list(@RequestBody @Valid WeiboLoginSetListRequest weiboLoginSetListReq);

	/**
	 * 单个查询微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetByIdRequest 单个查询微信登录配置请求参数 {@link WeiboLoginSetByIdRequest}
	 * @return 微信登录配置详情 {@link WeiboLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/get-by-id")
	BaseResponse<WeiboLoginSetByIdResponse> getById(@RequestBody @Valid WeiboLoginSetByIdRequest
                                                            weiboLoginSetByIdRequest);

}

