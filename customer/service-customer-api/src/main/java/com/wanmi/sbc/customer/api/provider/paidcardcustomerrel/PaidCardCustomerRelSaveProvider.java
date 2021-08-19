package com.wanmi.sbc.customer.api.provider.paidcardcustomerrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.CustomerDeletePaidCardRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelAddRequest;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelAddResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelModifyRequest;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelModifyResponse;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelDelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员保存服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardCustomerRelSaveProvider")
public interface PaidCardCustomerRelSaveProvider {

	/**
	 * 新增付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelAddRequest 付费会员新增参数结构 {@link PaidCardCustomerRelAddRequest}
	 * @return 新增的付费会员信息 {@link PaidCardCustomerRelAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/add")
	BaseResponse<PaidCardCustomerRelAddResponse> add(@RequestBody @Valid PaidCardCustomerRelAddRequest paidCardCustomerRelAddRequest);

	/**
	 * 修改付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelModifyRequest 付费会员修改参数结构 {@link PaidCardCustomerRelModifyRequest}
	 * @return 修改的付费会员信息 {@link PaidCardCustomerRelModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/modify")
	BaseResponse<PaidCardCustomerRelModifyResponse> modify(@RequestBody @Valid PaidCardCustomerRelModifyRequest paidCardCustomerRelModifyRequest);

	/**
	 * 单个删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelDelByIdRequest 单个删除参数结构 {@link PaidCardCustomerRelDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PaidCardCustomerRelDelByIdRequest paidCardCustomerRelDelByIdRequest);

	/**
	 * 批量删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardCustomerRelDelByIdListRequest 批量删除参数结构 {@link PaidCardCustomerRelDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PaidCardCustomerRelDelByIdListRequest paidCardCustomerRelDelByIdListRequest);

	@DeleteMapping("/customer/${application.customer.version}/paidcardcustomerrel/delete-customer-paid-card")
    BaseResponse deleteCustomerPaidCard( @RequestBody CustomerDeletePaidCardRequest request);

	/**
	 * 变更卡实体发送短信标识
	 * @param relIdList
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/change-send-msg-flag")
	BaseResponse changeSendMsgFlag( @RequestBody List<String> relIdList);

	@PostMapping("/customer/${application.customer.version}/paidcardcustomerrel/change-expire-send-msg-flag")
	BaseResponse changeExpireSendMsgFlag(@RequestBody List<String> relIdList);
}

