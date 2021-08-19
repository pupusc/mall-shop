package com.wanmi.sbc.customer.api.provider.paidcard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardAddResponse;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardModifyResponse;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>付费会员保存服务Provider</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@FeignClient(value = "${application.customer.name}", contextId = "PaidCardSaveProvider")
public interface PaidCardSaveProvider {

	/**
	 * 新增付费会员API
	 *
	 * @author xuhai
	 * @param paidCardAddRequest 付费会员新增参数结构 {@link PaidCardAddRequest}
	 * @return 新增的付费会员信息 {@link PaidCardAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/add")
	BaseResponse<PaidCardAddResponse> add(@RequestBody @Valid PaidCardAddRequest paidCardAddRequest);

	/**
	 * 修改付费会员API
	 *
	 * @author xuhai
	 * @param paidCardModifyRequest 付费会员修改参数结构 {@link PaidCardModifyRequest}
	 * @return 修改的付费会员信息 {@link PaidCardModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/modify")
	BaseResponse<PaidCardModifyResponse> modify(@RequestBody @Valid PaidCardModifyRequest paidCardModifyRequest);

	/**
	 * 单个删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardDelByIdRequest 单个删除参数结构 {@link PaidCardDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PaidCardDelByIdRequest paidCardDelByIdRequest);

	/**
	 * 批量删除付费会员API
	 *
	 * @author xuhai
	 * @param paidCardDelByIdListRequest 批量删除参数结构 {@link PaidCardDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid PaidCardDelByIdListRequest paidCardDelByIdListRequest);

	/**
	 * 修改付费会员卡状态
	 * @param request
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/change-enable-status")
    BaseResponse changeEnableStatus(@RequestBody @Valid PaidCardEnableRequest request);

	/**
	 * 支付回调处理
	 * @param paidCardRedisDTO
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/deal-pay-call-back")
    BaseResponse dealPayCallBack( @RequestBody PaidCardRedisDTO paidCardRedisDTO);

	/**
	 * 发送短信
	 * @param requestList
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/send-will-expire-sms")
    BaseResponse sendWillExpireSms(@RequestBody List<PaidCardExpireRequest> requestList);

	/**
	 * 发送短信
	 * @param requestList
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/paidcard/send-expire-sms")
	BaseResponse sendExpireSms(@RequestBody List<PaidCardExpireRequest> requestList);
}

