package com.wanmi.sbc.setting.api.provider.businessconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigPageRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigPageResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigListRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigListResponse;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigRopResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>招商页设置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@FeignClient(value = "${application.setting.name}", contextId = "BusinessConfigQueryProvider")
public interface BusinessConfigQueryProvider {

	/**
	 * 分页查询招商页设置API
	 *
	 * @author lq
	 * @param businessConfigPageReq 分页请求参数和筛选对象 {@link BusinessConfigPageRequest}
	 * @return 招商页设置分页列表信息 {@link BusinessConfigPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/page")
	BaseResponse<BusinessConfigPageResponse> page(@RequestBody @Valid BusinessConfigPageRequest businessConfigPageReq);

	/**
	 * 列表查询招商页设置API
	 *
	 * @author lq
	 * @param businessConfigListReq 列表请求参数和筛选对象 {@link BusinessConfigListRequest}
	 * @return 招商页设置的列表信息 {@link BusinessConfigListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/list")
	BaseResponse<BusinessConfigListResponse> list(@RequestBody @Valid BusinessConfigListRequest businessConfigListReq);

	/**
	 * 单个查询招商页设置API
	 *
	 * @author lq
	 * @param businessConfigByIdRequest 单个查询招商页设置请求参数 {@link BusinessConfigByIdRequest}
	 * @return 招商页设置详情 {@link BusinessConfigByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/get-by-id")
	BaseResponse<BusinessConfigByIdResponse> getById(@RequestBody @Valid BusinessConfigByIdRequest
                                                             businessConfigByIdRequest);

	/**
	 * 无参查找，获取招商配置信息
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/businessconfig/get-info")
	BaseResponse<BusinessConfigRopResponse> getInfo();
}

