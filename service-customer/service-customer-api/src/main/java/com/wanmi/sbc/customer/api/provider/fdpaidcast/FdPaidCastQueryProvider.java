package com.wanmi.sbc.customer.api.provider.fdpaidcast;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastPageRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastPageResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastListRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastListResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastByIdRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>樊登付费类型 映射商城付费类型查询服务Provider</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@FeignClient(value = "${application.customer.name}", contextId = "FdPaidCastQueryProvider")
public interface FdPaidCastQueryProvider {

	/**
	 * 分页查询樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastPageReq 分页请求参数和筛选对象 {@link FdPaidCastPageRequest}
	 * @return 樊登付费类型 映射商城付费类型分页列表信息 {@link FdPaidCastPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/page")
	BaseResponse<FdPaidCastPageResponse> page(@RequestBody @Valid FdPaidCastPageRequest fdPaidCastPageReq);

	/**
	 * 列表查询樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastListReq 列表请求参数和筛选对象 {@link FdPaidCastListRequest}
	 * @return 樊登付费类型 映射商城付费类型的列表信息 {@link FdPaidCastListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/list")
	BaseResponse<FdPaidCastListResponse> list(@RequestBody @Valid FdPaidCastListRequest fdPaidCastListReq);

	/**
	 * 单个查询樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastByIdRequest 单个查询樊登付费类型 映射商城付费类型请求参数 {@link FdPaidCastByIdRequest}
	 * @return 樊登付费类型 映射商城付费类型详情 {@link FdPaidCastByIdResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/get-by-id")
	BaseResponse<FdPaidCastByIdResponse> getById(@RequestBody @Valid FdPaidCastByIdRequest fdPaidCastByIdRequest);

}

