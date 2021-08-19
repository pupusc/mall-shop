package com.wanmi.sbc.account.provider.impl.customerdrawcash;

import com.wanmi.sbc.account.api.constant.CustomerDrawCashErrorCode;
import com.wanmi.sbc.account.api.request.customerdrawcash.*;
import com.wanmi.sbc.account.bean.enums.CustomerOperateStatus;
import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashSaveProvider;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashAddResponse;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashModifyResponse;
import com.wanmi.sbc.account.customerdrawcash.service.CustomerDrawCashService;
import com.wanmi.sbc.account.customerdrawcash.model.root.CustomerDrawCash;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>会员提现管理保存服务接口实现</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@RestController
@Validated
public class CustomerDrawCashSaveController implements CustomerDrawCashSaveProvider {
	@Autowired
	private CustomerDrawCashService customerDrawCashService;

	@Autowired
	private CustomerFundsService customerFundsService;

	/**
	 * 	每日限定最大提现金额10000元
	 */
	private final static BigDecimal MAX_DRAW_CASH = BigDecimal.valueOf(10000.00);


	@Override
	public BaseResponse<CustomerDrawCashAddResponse> add(@RequestBody @Valid CustomerDrawCashAddRequest customerDrawCashAddRequest) {
		CustomerFunds customerFunds = customerFundsService.findByCustomerId(customerDrawCashAddRequest.getCustomerId());
		// 预提现金额
		BigDecimal preDrawCash = customerDrawCashAddRequest.getDrawCashSum();
		//判断提现金额是否大于会员账户可提现金额
		if(preDrawCash.compareTo(customerFunds.getWithdrawAmount())==1){
			throw new SbcRuntimeException(CustomerDrawCashErrorCode.BALANCE_NOT_ENOUGH);
		}

		CustomerDrawCashQueryRequest request = new CustomerDrawCashQueryRequest();
		request.setCustomerId(customerDrawCashAddRequest.getCustomerId());
		// 统计会员当日提现金额
		BigDecimal drawedCash = customerDrawCashService.countDrawCashSum(request);
		if(Objects.nonNull(drawedCash)) {
			// 预提现金额 = 预提现金额 + 已提现金额
			preDrawCash = preDrawCash.add(drawedCash);
		}
		// 会员当日已提现金额 + 将要提现金额 不能超过每日限定10000元
		if (preDrawCash.compareTo(MAX_DRAW_CASH) == 1) {
			throw new SbcRuntimeException(CustomerDrawCashErrorCode.BEYOND_MAX_DRAW_CASH);
		}

		CustomerDrawCash customerDrawCash = new CustomerDrawCash();
		KsBeanUtil.copyPropertiesThird(customerDrawCashAddRequest, customerDrawCash);
		return BaseResponse.success(new CustomerDrawCashAddResponse(
				customerDrawCashService.wrapperVo(customerDrawCashService.add(customerDrawCash))));
	}

	@Override
	public BaseResponse<CustomerDrawCashModifyResponse> modify(@RequestBody @Valid CustomerDrawCashModifyRequest customerDrawCashModifyRequest) {
		CustomerDrawCash customerDrawCash = new CustomerDrawCash();
		KsBeanUtil.copyPropertiesThird(customerDrawCashModifyRequest, customerDrawCash);
		if(customerDrawCashModifyRequest.getCustomerOperateStatus() == CustomerOperateStatus.CANCEL){
			CustomerFunds customerFunds = customerFundsService.findByCustomerId(customerDrawCashModifyRequest.getCustomerId());
			//将冻结金额减去
			customerFundsService.rejectWithdrawCashApply(customerFunds.getCustomerFundsId(), customerDrawCashModifyRequest.getDrawCashSum());
		}
		return BaseResponse.success(new CustomerDrawCashModifyResponse(
				customerDrawCashService.wrapperVo(customerDrawCashService.modify(customerDrawCash))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid CustomerDrawCashDelByIdRequest customerDrawCashDelByIdRequest) {
		customerDrawCashService.deleteById(customerDrawCashDelByIdRequest.getDrawCashId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid CustomerDrawCashDelByIdListRequest customerDrawCashDelByIdListRequest) {
		customerDrawCashService.deleteByIdList(customerDrawCashDelByIdListRequest.getDrawCashIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

