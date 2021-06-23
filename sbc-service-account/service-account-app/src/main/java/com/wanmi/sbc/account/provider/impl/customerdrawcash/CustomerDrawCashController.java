package com.wanmi.sbc.account.provider.impl.customerdrawcash;

import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashProvider;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashBatchModifyAuditStatusRequest;
import com.wanmi.sbc.account.customerdrawcash.service.CustomerDrawCashService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 提现单操作接口
 * @author chenyufei
 */
@RestController
@Validated
public class CustomerDrawCashController implements CustomerDrawCashProvider {

    @Autowired
    private CustomerDrawCashService customerDrawCashService;

    /**
     * 批量修改提现单审核状态
     *
     * @param request 批量修改提现单审核数据结构 {@link CustomerDrawCashBatchModifyAuditStatusRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse batchModifyAuditStatus(@RequestBody @Valid CustomerDrawCashBatchModifyAuditStatusRequest request) {
        customerDrawCashService.updateDrawCashAuditStatus(request.getDrawCashIdList(),request.getAuditStatus(),
                request.getRejectReason(),request.getFinishStatus(),request.getDrawCashStatus(),request.getDrawCashFailedReason(),request.getAccountBalance());
        return BaseResponse.SUCCESSFUL();
    }
}
