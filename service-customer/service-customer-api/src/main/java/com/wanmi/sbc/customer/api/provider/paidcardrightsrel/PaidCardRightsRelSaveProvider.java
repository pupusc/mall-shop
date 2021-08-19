package com.wanmi.sbc.customer.api.provider.paidcardrightsrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardrightsrel.PaidCardRightsRelModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>付费会员保存服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardRightsRelSaveProvider")
public interface PaidCardRightsRelSaveProvider {

	/**
	 * 新增付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelAddRequest 付费会员新增参数结构 {@link PaidCardRightsRelAddRequest}
	 * @return 新增的付费会员信息 {@link PaidCardRightsRelAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/add")
	BaseResponse<PaidCardRightsRelAddResponse> add(@RequestBody @Valid PaidCardRightsRelAddRequest paidCardRightsRelAddRequest);

	/**
	 * 修改付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelModifyRequest 付费会员修改参数结构 {@link PaidCardRightsRelModifyRequest}
	 * @return 修改的付费会员信息 {@link PaidCardRightsRelModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/modify")
	BaseResponse<PaidCardRightsRelModifyResponse> modify(@RequestBody @Valid PaidCardRightsRelModifyRequest paidCardRightsRelModifyRequest);

	/**
	 * 单个删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelDelByIdRequest 单个删除参数结构 {@link PaidCardRightsRelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PaidCardRightsRelDelByIdRequest paidCardRightsRelDelByIdRequest);

	/**
	 * 批量删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardRightsRelDelByIdListRequest 批量删除参数结构 {@link PaidCardRightsRelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardrightsrel/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PaidCardRightsRelDelByIdListRequest paidCardRightsRelDelByIdListRequest);

}

