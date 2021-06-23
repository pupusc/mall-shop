package com.wanmi.sbc.account.api.provider.customerdrawcash;

import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashBatchModifyAuditStatusRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 对会员提现单操作接口
 * @author chenyufei
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerDrawCashProvider")
public interface CustomerDrawCashProvider {

    /**
     * 批量修改提现单审核状态
     *
     * @param request 批量修改提现单审核数据结构 {@link com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashBatchModifyAuditStatusRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/customer/draw/cash/batch-modify-audit-status")
    BaseResponse batchModifyAuditStatus(@RequestBody @Valid CustomerDrawCashBatchModifyAuditStatusRequest request);



}
