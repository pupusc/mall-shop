package com.wanmi.sbc.message.api.provider.smssignfileinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoPageRequest;
import com.wanmi.sbc.message.api.response.smssignfileinfo.SmsSignFileInfoPageResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoListRequest;
import com.wanmi.sbc.message.api.response.smssignfileinfo.SmsSignFileInfoListResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoByIdRequest;
import com.wanmi.sbc.message.api.response.smssignfileinfo.SmsSignFileInfoByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信签名文件信息查询服务Provider</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSignFileInfoQueryProvider")
public interface SmsSignFileInfoQueryProvider {

	/**
	 * 分页查询短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoPageReq 分页请求参数和筛选对象 {@link SmsSignFileInfoPageRequest}
	 * @return 短信签名文件信息分页列表信息 {@link SmsSignFileInfoPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/page")
	BaseResponse<SmsSignFileInfoPageResponse> page(@RequestBody @Valid SmsSignFileInfoPageRequest smsSignFileInfoPageReq);

	/**
	 * 列表查询短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoListReq 列表请求参数和筛选对象 {@link SmsSignFileInfoListRequest}
	 * @return 短信签名文件信息的列表信息 {@link SmsSignFileInfoListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/list")
	BaseResponse<SmsSignFileInfoListResponse> list(@RequestBody @Valid SmsSignFileInfoListRequest smsSignFileInfoListReq);

	/**
	 * 单个查询短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoByIdRequest 单个查询短信签名文件信息请求参数 {@link SmsSignFileInfoByIdRequest}
	 * @return 短信签名文件信息详情 {@link SmsSignFileInfoByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/get-by-id")
	BaseResponse<SmsSignFileInfoByIdResponse> getById(@RequestBody @Valid SmsSignFileInfoByIdRequest smsSignFileInfoByIdRequest);

}

