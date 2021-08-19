package com.wanmi.sbc.message.api.provider.pushdetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailByIdRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailListRequest;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailPageRequest;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailByIdResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailListResponse;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>推送详情查询服务Provider</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@FeignClient(value = "${application.message.name}", contextId = "PushDetailQueryProvider")
public interface PushDetailQueryProvider {

	/**
	 * 分页查询推送详情API
	 *
	 * @author Bob
	 * @param pushDetailPageReq 分页请求参数和筛选对象 {@link PushDetailPageRequest}
	 * @return 推送详情分页列表信息 {@link PushDetailPageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/page")
	BaseResponse<PushDetailPageResponse> page(@RequestBody @Valid PushDetailPageRequest pushDetailPageReq);

	/**
	 * 列表查询推送详情API
	 *
	 * @author Bob
	 * @param pushDetailListReq 列表请求参数和筛选对象 {@link PushDetailListRequest}
	 * @return 推送详情的列表信息 {@link PushDetailListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/list")
	BaseResponse<PushDetailListResponse> list(@RequestBody @Valid PushDetailListRequest pushDetailListReq);

	/**
	 * 单个查询推送详情API
	 *
	 * @author Bob
	 * @param pushDetailByIdRequest 单个查询推送详情请求参数 {@link PushDetailByIdRequest}
	 * @return 推送详情详情 {@link PushDetailByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushdetail/get-by-id")
	BaseResponse<PushDetailByIdResponse> getById(@RequestBody @Valid PushDetailByIdRequest pushDetailByIdRequest);

}

