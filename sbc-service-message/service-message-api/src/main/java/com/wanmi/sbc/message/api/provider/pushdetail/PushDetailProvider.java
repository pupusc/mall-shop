package com.wanmi.sbc.message.api.provider.pushdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailAddRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailModifyRequest;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailAddResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>推送详情保存服务Provider</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@FeignClient(value = "${application.message.name}", contextId = "PushDetailProvider")
public interface PushDetailProvider {

	/**
	 * 新增推送详情API
	 *
	 * @author Bob
	 * @param pushDetailAddRequest 推送详情新增参数结构 {@link PushDetailAddRequest}
	 * @return 推送详情信息 {@link PushDetailAddResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/add")
	BaseResponse<PushDetailAddResponse> add(@RequestBody @Valid PushDetailAddRequest pushDetailAddRequest);

	/**
	 * 修改推送详情API
	 *
	 * @author Bob
	 * @param pushDetailModifyRequest 推送详情修改参数结构 {@link PushDetailModifyRequest}
	 * @return 修改的推送详情信息 {@link PushDetailModifyResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/modify")
	BaseResponse<PushDetailModifyResponse> modify(@RequestBody @Valid PushDetailModifyRequest pushDetailModifyRequest);

	/**
	 * 单个删除推送详情API
	 *
	 * @author Bob
	 * @param pushDetailDelByIdRequest 单个删除参数结构 {@link PushDetailDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PushDetailDelByIdRequest pushDetailDelByIdRequest);

}

