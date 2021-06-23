package com.wanmi.sbc.message.api.provider.smssetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingPageRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingPageResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingListRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingListResponse;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingByIdRequest;
import com.wanmi.sbc.message.api.response.smssetting.SmsSettingByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信配置查询服务Provider</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSettingQueryProvider")
public interface SmsSettingQueryProvider {

	/**
	 * 分页查询短信配置API
	 *
	 * @author lvzhenwei
	 * @param smsSettingPageReq 分页请求参数和筛选对象 {@link SmsSettingPageRequest}
	 * @return 短信配置分页列表信息 {@link SmsSettingPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssetting/page")
	BaseResponse<SmsSettingPageResponse> page(@RequestBody @Valid SmsSettingPageRequest smsSettingPageReq);

	/**
	 * 列表查询短信配置API
	 *
	 * @author lvzhenwei
	 * @param smsSettingListReq 列表请求参数和筛选对象 {@link SmsSettingListRequest}
	 * @return 短信配置的列表信息 {@link SmsSettingListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssetting/list")
	BaseResponse<SmsSettingListResponse> list(@RequestBody @Valid SmsSettingListRequest smsSettingListReq);

	/**
	 * 单个查询短信配置API
	 *
	 * @author lvzhenwei
	 * @param smsSettingByIdRequest 单个查询短信配置请求参数 {@link SmsSettingByIdRequest}
	 * @return 短信配置详情 {@link SmsSettingByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssetting/get-by-id")
	BaseResponse<SmsSettingByIdResponse> getById(@RequestBody @Valid SmsSettingByIdRequest smsSettingByIdRequest);

}

