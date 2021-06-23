package com.wanmi.sbc.setting.api.provider.wechatshareset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoByStoreIdRequest;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoRequest;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetInfoResponse;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetByIdRequest;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信分享配置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@FeignClient(value = "${application.setting.name}", contextId = "WechatShareSetQueryProvider")
public interface WechatShareSetQueryProvider {

	/**
	 * 单个查询微信分享配置API
	 *
	 * @author lq
	 * @param wechatShareSetByIdRequest 单个查询微信分享配置请求参数 {@link WechatShareSetByIdRequest}
	 * @return 微信分享配置详情 {@link WechatShareSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatshareset/get-by-id")
	BaseResponse<WechatShareSetByIdResponse> getById(@RequestBody @Valid WechatShareSetByIdRequest
                                                             wechatShareSetByIdRequest);

	/**
	 * 查询微信分享配置API
	 *
	 * @author lq
	 * @param wechatShareSetInfoRequest 查询微信分享配置请求参数 {@link WechatShareSetInfoRequest}
	 * @return 微信分享配置详情 {@link WechatShareSetInfoResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatshareset/get-info")
	BaseResponse<WechatShareSetInfoResponse> getInfo(@RequestBody @Valid WechatShareSetInfoRequest
                                                             wechatShareSetInfoRequest);

	/**
	 * 门店id查询微信分享配置API
	 * @param wechatShareSetInfoByStoreIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/wechatshareset/getInfoByStoreId")
	BaseResponse<WechatShareSetInfoResponse> getInfoByStoreId(@RequestBody @Valid WechatShareSetInfoByStoreIdRequest
															 wechatShareSetInfoByStoreIdRequest);

	/**
	 * 门店id查询微信分享配置API
	 * @param wechatShareSetInfoByStoreIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/wechatshareset/getInfoCacheByStoreId")
	BaseResponse<WechatShareSetInfoResponse> getInfoCacheByStoreId(@RequestBody @Valid WechatShareSetInfoByStoreIdRequest
																	  wechatShareSetInfoByStoreIdRequest);

}

