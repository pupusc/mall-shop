package com.wanmi.sbc.setting.api.provider.weibologinset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetAddRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetAddResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetModifyRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetModifyResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetDelByIdRequest;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信登录配置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@FeignClient(value = "${application.setting.name}", contextId = "WeiboLoginSetSaveProvider")
public interface WeiboLoginSetSaveProvider {

	/**
	 * 新增微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetAddRequest 微信登录配置新增参数结构 {@link WeiboLoginSetAddRequest}
	 * @return 新增的微信登录配置信息 {@link WeiboLoginSetAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/add")
	BaseResponse<WeiboLoginSetAddResponse> add(@RequestBody @Valid WeiboLoginSetAddRequest weiboLoginSetAddRequest);

	/**
	 * 修改微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetModifyRequest 微信登录配置修改参数结构 {@link WeiboLoginSetModifyRequest}
	 * @return 修改的微信登录配置信息 {@link WeiboLoginSetModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/modify")
	BaseResponse<WeiboLoginSetModifyResponse> modify(@RequestBody @Valid WeiboLoginSetModifyRequest
                                                             weiboLoginSetModifyRequest);

	/**
	 * 单个删除微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetDelByIdRequest 单个删除参数结构 {@link WeiboLoginSetDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid WeiboLoginSetDelByIdRequest weiboLoginSetDelByIdRequest);

	/**
	 * 批量删除微信登录配置API
	 *
	 * @author lq
	 * @param weiboLoginSetDelByIdListRequest 批量删除参数结构 {@link WeiboLoginSetDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/weibologinset/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid WeiboLoginSetDelByIdListRequest weiboLoginSetDelByIdListRequest);

}

