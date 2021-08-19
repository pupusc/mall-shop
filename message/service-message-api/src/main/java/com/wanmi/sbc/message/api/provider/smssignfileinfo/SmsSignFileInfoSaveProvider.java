package com.wanmi.sbc.message.api.provider.smssignfileinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoAddRequest;
import com.wanmi.sbc.message.api.response.smssignfileinfo.SmsSignFileInfoAddResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoModifyRequest;
import com.wanmi.sbc.message.api.response.smssignfileinfo.SmsSignFileInfoModifyResponse;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoDelByIdRequest;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>短信签名文件信息保存服务Provider</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@FeignClient(value = "${application.message.name}",contextId = "SmsSignFileInfoSaveProvider")
public interface SmsSignFileInfoSaveProvider {

	/**
	 * 新增短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoAddRequest 短信签名文件信息新增参数结构 {@link SmsSignFileInfoAddRequest}
	 * @return 新增的短信签名文件信息信息 {@link SmsSignFileInfoAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/add")
	BaseResponse<SmsSignFileInfoAddResponse> add(@RequestBody @Valid SmsSignFileInfoAddRequest smsSignFileInfoAddRequest);

	/**
	 * 修改短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoModifyRequest 短信签名文件信息修改参数结构 {@link SmsSignFileInfoModifyRequest}
	 * @return 修改的短信签名文件信息信息 {@link SmsSignFileInfoModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/modify")
	BaseResponse<SmsSignFileInfoModifyResponse> modify(@RequestBody @Valid SmsSignFileInfoModifyRequest smsSignFileInfoModifyRequest);

	/**
	 * 单个删除短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoDelByIdRequest 单个删除参数结构 {@link SmsSignFileInfoDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid SmsSignFileInfoDelByIdRequest smsSignFileInfoDelByIdRequest);

	/**
	 * 批量删除短信签名文件信息API
	 *
	 * @author lvzhenwei
	 * @param smsSignFileInfoDelByIdListRequest 批量删除参数结构 {@link SmsSignFileInfoDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/smssignfileinfo/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid SmsSignFileInfoDelByIdListRequest smsSignFileInfoDelByIdListRequest);

}

