package com.wanmi.sbc.account.provider.impl.customerdrawcash;

import com.wanmi.sbc.account.api.request.customerdrawcash.*;
import com.wanmi.sbc.account.api.response.customerdrawcash.*;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.account.bean.enums.AuditStatus;
import com.wanmi.sbc.account.bean.enums.CustomerOperateStatus;
import com.wanmi.sbc.account.bean.enums.DrawCashStatus;
import com.wanmi.sbc.account.bean.enums.FinishStatus;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsIdBase;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailStatusByCustomerIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerStatusBaseVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashQueryProvider;
import com.wanmi.sbc.account.bean.vo.CustomerDrawCashVO;
import com.wanmi.sbc.account.customerdrawcash.service.CustomerDrawCashService;
import com.wanmi.sbc.account.customerdrawcash.model.root.CustomerDrawCash;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员提现管理查询服务接口实现</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@RestController
@Validated
public class CustomerDrawCashQueryController implements CustomerDrawCashQueryProvider {
	@Autowired
	private CustomerDrawCashService customerDrawCashService;

	@Override
	public BaseResponse<CustomerDrawCashPageResponse> page(@RequestBody @Valid CustomerDrawCashPageRequest customerDrawCashPageReq) {
		CustomerDrawCashQueryRequest queryReq = new CustomerDrawCashQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerDrawCashPageReq, queryReq);

		Page<CustomerDrawCash> customerDrawCashPage = customerDrawCashService.page(queryReq);
		Page<CustomerDrawCashVO> newPage = customerDrawCashPage.map(entity -> customerDrawCashService.wrapperVo(entity));

		MicroServicePage<CustomerDrawCashVO> microPage = new MicroServicePage<>(newPage, customerDrawCashPageReq.getPageable());
		CustomerDrawCashPageResponse finalRes = new CustomerDrawCashPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CustomerDrawCashExportResponse> export(@RequestBody @Valid CustomerDrawCashExportRequest customerDrawCashExportReq) {
		CustomerDrawCashQueryRequest queryReq = new CustomerDrawCashQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerDrawCashExportReq, queryReq);

		List<CustomerDrawCashVO> dataRecords = new ArrayList<>();
		// 分批次查询，避免出现hibernate事务超时 共查询1000条
		for (int i = 0; i < 10; i++) {
			queryReq.setPageNum(i);
			queryReq.setPageSize(100);

			Page<CustomerDrawCash> customerDrawCashPage = customerDrawCashService.page(queryReq);
			Page<CustomerDrawCashVO> newPage = customerDrawCashPage.map(entity -> customerDrawCashService.wrapperVo(entity));

			List<CustomerDrawCashVO> data = newPage.getContent();
			dataRecords.addAll(data);
			if (data.size() < 100) {
				break;
			}
		}
		MicroServicePage<CustomerDrawCashVO> microPage = new MicroServicePage<>(dataRecords);
		CustomerDrawCashExportResponse finalRes = new CustomerDrawCashExportResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<CustomerDrawCashListResponse> list(@RequestBody @Valid CustomerDrawCashListRequest customerDrawCashListReq) {
		CustomerDrawCashQueryRequest queryReq = new CustomerDrawCashQueryRequest();
		KsBeanUtil.copyPropertiesThird(customerDrawCashListReq, queryReq);
		List<CustomerDrawCash> customerDrawCashList = customerDrawCashService.list(queryReq);
		List<CustomerDrawCashVO> newList = customerDrawCashList.stream().map(entity -> customerDrawCashService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new CustomerDrawCashListResponse(newList));
	}

	@Override
	public BaseResponse<CustomerDrawCashByIdResponse> getById(@RequestBody @Valid CustomerDrawCashByIdRequest customerDrawCashByIdRequest) {
		CustomerDrawCash customerDrawCash = customerDrawCashService.getById(customerDrawCashByIdRequest.getDrawCashId());
		return BaseResponse.success(new CustomerDrawCashByIdResponse(customerDrawCashService.wrapperVo(customerDrawCash)));
	}

	/**
	 * 统计某个会员当日提现金额
	 * @param request
	 * @return
	 */
	@Override
	public BaseResponse<BigDecimal> countDrawCashSum(@RequestBody CustomerDrawCashQueryRequest request){
		return BaseResponse.success(customerDrawCashService.countDrawCashSum(request));
	}

	/**
	 * 统计当前标签页
	 * @return
	 */
	@Override
	public BaseResponse<CustomerDrawCashStatusResponse> countDrawCashTabNum() {
		return BaseResponse.success(CustomerDrawCashStatusResponse
				.builder()
				.waitAuditTotal(this.customerDrawCashService.countDrawCashTabNum(CustomerDrawCashStatusQueryRequest
						.builder()
						.auditStatus(AuditStatus.WAIT) //待审核
						.drawCashStatus(DrawCashStatus.WAIT) //待提现
						.customerOperateStatus(CustomerOperateStatus.APPLY) //已申请
						.finishStatus(FinishStatus.UNSUCCESS) //未完成
						.delFlag(DeleteFlag.NO) //未删除
						.build()))
				.finishTotal(this.customerDrawCashService.countDrawCashTabNum(CustomerDrawCashStatusQueryRequest
						.builder()
						.auditStatus(AuditStatus.PASS) //审核通过
						.drawCashStatus(DrawCashStatus.SUCCESS) //提现成功
						.customerOperateStatus(CustomerOperateStatus.APPLY) //已申请
						.finishStatus(FinishStatus.SUCCESS) //已完成
						.delFlag(DeleteFlag.NO) //未删除
						.build()))
				.drawCashFailedTotal(this.customerDrawCashService.countDrawCashTabNum(CustomerDrawCashStatusQueryRequest
						.builder()
						.auditStatus(AuditStatus.PASS) //审核通过
						.drawCashStatus(DrawCashStatus.FAIL) //提现失败
						.customerOperateStatus(CustomerOperateStatus.APPLY) //已申请
						.finishStatus(FinishStatus.UNSUCCESS) //未完成
						.delFlag(DeleteFlag.NO) //未删除
						.build()))
				.auditNotPassTotal(this.customerDrawCashService.countDrawCashTabNum(CustomerDrawCashStatusQueryRequest
						.builder()
						.auditStatus(AuditStatus.REJECT) //审核拒绝
						.drawCashStatus(DrawCashStatus.WAIT) //待提现
						.customerOperateStatus(CustomerOperateStatus.APPLY) //已申请
						.finishStatus(FinishStatus.UNSUCCESS) //未完成
						.delFlag(DeleteFlag.NO) //未删除
						.build()))
				.canceledTotal(this.customerDrawCashService.countDrawCashTabNum(CustomerDrawCashStatusQueryRequest
						.builder()
						.auditStatus(AuditStatus.WAIT) //待审核
						.drawCashStatus(DrawCashStatus.WAIT) //待提现
						.customerOperateStatus(CustomerOperateStatus.CANCEL) //已取消
						.finishStatus(FinishStatus.UNSUCCESS) //未完成
						.delFlag(DeleteFlag.NO) //未删除
						.build()))
				.build());
	}

}

