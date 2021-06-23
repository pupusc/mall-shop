package com.wanmi.sbc.message.api.provider.smssign;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssign.*;
import com.wanmi.sbc.message.api.response.smssign.SmsSignPageResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignListResponse;
import com.wanmi.sbc.message.api.response.smssign.SmsSignByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信签名查询服务Provider</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSignQueryProvider")
public interface SmsSignQueryProvider {

	/**
	 * 分页查询短信签名API
	 *
	 * @author lvzhenwei
	 * @param smsSignPageReq 分页请求参数和筛选对象 {@link SmsSignPageRequest}
	 * @return 短信签名分页列表信息 {@link SmsSignPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssign/page")
	BaseResponse<SmsSignPageResponse> page(@RequestBody @Valid SmsSignPageRequest smsSignPageReq);

	/**
	 * 列表查询短信签名API
	 *
	 * @author lvzhenwei
	 * @param smsSignListReq 列表请求参数和筛选对象 {@link SmsSignListRequest}
	 * @return 短信签名的列表信息 {@link SmsSignListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssign/list")
	BaseResponse<SmsSignListResponse> list(@RequestBody @Valid SmsSignListRequest smsSignListReq);

	/**
	 * 单个查询短信签名API
	 *
	 * @author lvzhenwei
	 * @param smsSignByIdRequest 单个查询短信签名请求参数 {@link SmsSignByIdRequest}
	 * @return 短信签名详情 {@link SmsSignByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssign/get-by-id")
	BaseResponse<SmsSignByIdResponse> getById(@RequestBody @Valid SmsSignByIdRequest smsSignByIdRequest);

    /**
     * 单个检测短信签名API
     *
     * @author lvzhenwei
     * @param request 单个检测短信签名请求参数 {@link SmsSignCheckByIdRequest}
     * @return 短信签名详情 {@link SmsSignByIdResponse}
     */
    @PostMapping("/sms/${application.sms.version}/smssign/check-by-id")
	BaseResponse check(@RequestBody @Valid SmsSignCheckByIdRequest request);

	@PostMapping("/sms/${application.sms.version}/smssign/get-by-sms-sign-name-and-delFlag")
	BaseResponse<SmsSignListResponse> getBySmsSignNameAndAndDelFlag(@RequestBody @Valid SmsSignQueryRequest request);
}

