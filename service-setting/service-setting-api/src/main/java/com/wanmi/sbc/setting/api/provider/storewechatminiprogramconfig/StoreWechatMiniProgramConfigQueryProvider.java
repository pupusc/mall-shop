package com.wanmi.sbc.setting.api.provider.storewechatminiprogramconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.*;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>门店微信小程序配置查询服务Provider</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@FeignClient(value = "${application.setting.name}",contextId = "StoreWechatMiniProgramConfigQueryProvider")
public interface StoreWechatMiniProgramConfigQueryProvider {

	/**
	 * 分页查询门店微信小程序配置API
	 *
	 * @author tangLian
	 * @param storeWechatMiniProgramConfigPageReq 分页请求参数和筛选对象 {@link StoreWechatMiniProgramConfigPageRequest}
	 * @return 门店微信小程序配置分页列表信息 {@link StoreWechatMiniProgramConfigPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/page")
	BaseResponse<StoreWechatMiniProgramConfigPageResponse> page(@RequestBody @Valid StoreWechatMiniProgramConfigPageRequest storeWechatMiniProgramConfigPageReq);

	/**
	 * 列表查询门店微信小程序配置API
	 *
	 * @author tangLian
	 * @param storeWechatMiniProgramConfigListReq 列表请求参数和筛选对象 {@link StoreWechatMiniProgramConfigListRequest}
	 * @return 门店微信小程序配置的列表信息 {@link StoreWechatMiniProgramConfigListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/list")
	BaseResponse<StoreWechatMiniProgramConfigListResponse> list(@RequestBody @Valid StoreWechatMiniProgramConfigListRequest storeWechatMiniProgramConfigListReq);

	/**
	 * 单个查询门店微信小程序配置API
	 *
	 * @author tangLian
	 * @param storeWechatMiniProgramConfigByIdRequest 单个查询门店微信小程序配置请求参数 {@link StoreWechatMiniProgramConfigByIdRequest}
	 * @return 门店微信小程序配置详情 {@link StoreWechatMiniProgramConfigByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/get-by-id")
	BaseResponse<StoreWechatMiniProgramConfigByIdResponse> getById(@RequestBody @Valid StoreWechatMiniProgramConfigByIdRequest storeWechatMiniProgramConfigByIdRequest);

	/**
	 * 门店id查询微信小程序配置
	 * @param storeWechatMiniProgramConfigByIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/getByStoreId")
	BaseResponse<StoreWechatMiniProgramConfigByStoreIdResponse> getByStoreId(@RequestBody @Valid StoreWechatMiniProgramConfigByStoreIdRequest storeWechatMiniProgramConfigByIdRequest);

	/**
	 * 门店id查询微信小程序配置 优先缓存获取
	 * @param storeWechatMiniProgramConfigByCacheRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/getCacheByStoreId")
	BaseResponse<StoreWechatMiniProgramConfigByCacheResponse> getCacheByStoreId(@RequestBody @Valid StoreWechatMiniProgramConfigByCacheRequest storeWechatMiniProgramConfigByCacheRequest);
}

