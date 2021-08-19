package com.wanmi.sbc.message.api.provider.smstemplate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplatePageRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplatePageResponse;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateListRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateListResponse;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateByIdRequest;
import com.wanmi.sbc.message.api.response.smstemplate.SmsTemplateByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信模板查询服务Provider</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsTemplateQueryProvider")
public interface SmsTemplateQueryProvider {

	/**
	 * 分页查询短信模板API
	 *
	 * @author lvzhenwei
	 * @param smsTemplatePageReq 分页请求参数和筛选对象 {@link SmsTemplatePageRequest}
	 * @return 短信模板分页列表信息 {@link SmsTemplatePageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smstemplate/page")
	BaseResponse<SmsTemplatePageResponse> page(@RequestBody @Valid SmsTemplatePageRequest smsTemplatePageReq);

	/**
	 * 列表查询短信模板API
	 *
	 * @author lvzhenwei
	 * @param smsTemplateListReq 列表请求参数和筛选对象 {@link SmsTemplateListRequest}
	 * @return 短信模板的列表信息 {@link SmsTemplateListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smstemplate/list")
	BaseResponse<SmsTemplateListResponse> list(@RequestBody @Valid SmsTemplateListRequest smsTemplateListReq);

	/**
	 * 单个查询短信模板API
	 *
	 * @author lvzhenwei
	 * @param smsTemplateByIdRequest 单个查询短信模板请求参数 {@link SmsTemplateByIdRequest}
	 * @return 短信模板详情 {@link SmsTemplateByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smstemplate/get-by-id")
	BaseResponse<SmsTemplateByIdResponse> getById(@RequestBody @Valid SmsTemplateByIdRequest smsTemplateByIdRequest);

}

