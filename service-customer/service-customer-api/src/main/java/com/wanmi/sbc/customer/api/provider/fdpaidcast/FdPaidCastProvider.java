package com.wanmi.sbc.customer.api.provider.fdpaidcast;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastAddRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastAddResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastModifyRequest;
import com.wanmi.sbc.customer.api.response.fdpaidcast.FdPaidCastModifyResponse;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastDelByIdRequest;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>樊登付费类型 映射商城付费类型保存服务Provider</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@FeignClient(value = "${application.customer.name}", contextId = "FdPaidCastProvider")
public interface FdPaidCastProvider {

	/**
	 * 新增樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastAddRequest 樊登付费类型 映射商城付费类型新增参数结构 {@link FdPaidCastAddRequest}
	 * @return 新增的樊登付费类型 映射商城付费类型信息 {@link FdPaidCastAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/add")
	BaseResponse<FdPaidCastAddResponse> add(@RequestBody @Valid FdPaidCastAddRequest fdPaidCastAddRequest);

	/**
	 * 修改樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastModifyRequest 樊登付费类型 映射商城付费类型修改参数结构 {@link FdPaidCastModifyRequest}
	 * @return 修改的樊登付费类型 映射商城付费类型信息 {@link FdPaidCastModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/modify")
	BaseResponse<FdPaidCastModifyResponse> modify(@RequestBody @Valid FdPaidCastModifyRequest fdPaidCastModifyRequest);

	/**
	 * 单个删除樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastDelByIdRequest 单个删除参数结构 {@link FdPaidCastDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid FdPaidCastDelByIdRequest fdPaidCastDelByIdRequest);

	/**
	 * 批量删除樊登付费类型 映射商城付费类型API
	 *
	 * @author tzx
	 * @param fdPaidCastDelByIdListRequest 批量删除参数结构 {@link FdPaidCastDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/fdpaidcast/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid FdPaidCastDelByIdListRequest fdPaidCastDelByIdListRequest);

}

