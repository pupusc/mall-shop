package com.wanmi.sbc.account.api.provider.customerdrawcash;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashAddRequest;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashAddResponse;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashModifyRequest;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashModifyResponse;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashDelByIdRequest;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>会员提现管理保存服务Provider</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerDrawCashSaveProvider")
public interface CustomerDrawCashSaveProvider {

	/**
	 * 新增会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashAddRequest 会员提现管理新增参数结构 {@link CustomerDrawCashAddRequest}
	 * @return 新增的会员提现管理信息 {@link CustomerDrawCashAddResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/add")
	BaseResponse<CustomerDrawCashAddResponse> add(@RequestBody @Valid CustomerDrawCashAddRequest customerDrawCashAddRequest);

	/**
	 * 修改会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashModifyRequest 会员提现管理修改参数结构 {@link CustomerDrawCashModifyRequest}
	 * @return 修改的会员提现管理信息 {@link CustomerDrawCashModifyResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/modify")
	BaseResponse<CustomerDrawCashModifyResponse> modify(@RequestBody @Valid CustomerDrawCashModifyRequest customerDrawCashModifyRequest);

	/**
	 * 单个删除会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashDelByIdRequest 单个删除参数结构 {@link CustomerDrawCashDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid CustomerDrawCashDelByIdRequest customerDrawCashDelByIdRequest);

	/**
	 * 批量删除会员提现管理API
	 *
	 * @author chenyufei
	 * @param customerDrawCashDelByIdListRequest 批量删除参数结构 {@link CustomerDrawCashDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/account/${application.account.version}/customerdrawcash/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid CustomerDrawCashDelByIdListRequest customerDrawCashDelByIdListRequest);

}

