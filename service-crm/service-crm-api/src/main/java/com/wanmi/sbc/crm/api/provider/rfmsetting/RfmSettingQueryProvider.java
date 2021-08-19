package com.wanmi.sbc.crm.api.provider.rfmsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingPageRequest;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingPageResponse;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingListRequest;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingListResponse;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingByIdRequest;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingByIdResponse;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingResponse;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>rfm参数配置查询服务Provider</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@FeignClient(value = "${application.crm.name}",contextId = "RfmSettingQueryProvider")
public interface RfmSettingQueryProvider {

	/**
	 * 分页查询rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingPageReq 分页请求参数和筛选对象 {@link RfmSettingPageRequest}
	 * @return rfm参数配置分页列表信息 {@link RfmSettingPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/page")
	BaseResponse<RfmSettingPageResponse> page(@RequestBody @Valid RfmSettingPageRequest rfmSettingPageReq);

	/**
	 * 列表查询rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingListReq 列表请求参数和筛选对象 {@link RfmSettingListRequest}
	 * @return rfm参数配置的列表信息 {@link RfmSettingListResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/list")
	BaseResponse<RfmSettingListResponse> list(@RequestBody @Valid RfmSettingListRequest rfmSettingListReq);

	/**
	 * 单个查询rfm参数配置API
	 *
	 * @author zhanglingke
	 * @param rfmSettingByIdRequest 单个查询rfm参数配置请求参数 {@link RfmSettingByIdRequest}
	 * @return rfm参数配置详情 {@link RfmSettingByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/rfmsetting/get-by-id")
	BaseResponse<RfmSettingByIdResponse> getById(@RequestBody @Valid RfmSettingByIdRequest rfmSettingByIdRequest);

	/**
	 * 查询RFMSetting详情
	 * @return rfm参数配置详情 {@link RfmSettingResponse}
	 */
	@GetMapping("/crm/${application.crm.version}/rfmsetting/detail")
    BaseResponse<RfmSettingResponse> getRfmSetting();
}

