package com.wanmi.sbc.message.api.provider.pushsend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushsend.PushSendAddRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendDelByIdListRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendModifyRequest;
import com.wanmi.sbc.message.api.response.pushsend.PushSendAddResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送信息保存服务Provider</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@FeignClient(value = "${application.message.name}", contextId = "PushSendProvider")
public interface PushSendProvider {

	/**
	 * 新增会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendAddRequest 会员推送信息新增参数结构 {@link PushSendAddRequest}
	 * @return 新增的会员推送信息信息 {@link PushSendAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/add")
	BaseResponse<PushSendAddResponse> add(@RequestBody @Valid PushSendAddRequest pushSendAddRequest);

	/**
	 * 修改会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendModifyRequest 会员推送信息修改参数结构 {@link PushSendModifyRequest}
	 * @return 修改的会员推送信息信息 {@link PushSendModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/modify")
	BaseResponse<PushSendModifyResponse> modify(@RequestBody @Valid PushSendModifyRequest pushSendModifyRequest);

	/**
	 * 单个删除会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendDelByIdRequest 单个删除参数结构 {@link PushSendDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PushSendDelByIdRequest pushSendDelByIdRequest);

	/**
	 * 批量删除会员推送信息API
	 *
	 * @author Bob
	 * @param pushSendDelByIdListRequest 批量删除参数结构 {@link PushSendDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushsend/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PushSendDelByIdListRequest pushSendDelByIdListRequest);

}

