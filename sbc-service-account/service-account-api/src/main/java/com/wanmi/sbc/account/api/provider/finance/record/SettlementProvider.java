package com.wanmi.sbc.account.api.provider.finance.record;

import com.wanmi.sbc.account.api.request.finance.record.SettlementAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.SettlementBatchModifyStatusRequest;
import com.wanmi.sbc.account.api.request.finance.record.SettlementDeleteRequest;
import com.wanmi.sbc.account.api.response.finance.record.SettlementAddResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对结算单操作接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@FeignClient(value = "${application.account.name}", contextId = "SettlementProvider")
public interface SettlementProvider {

    /**
     * 新增结算单
     *
     * @param request 新增结算单数据结构 {@link SettlementAddRequest}
     * @return {@link SettlementAddResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/add")
    BaseResponse<SettlementAddResponse> add(@RequestBody SettlementAddRequest request);

    /**
     * 删除结算单
     *
     * @param request 删除结算单数据结构 {@link SettlementDeleteRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/delete")
    BaseResponse delete(@RequestBody @Valid SettlementDeleteRequest request);

    /**
     * 批量修改结算单状态
     *
     * @param request 批量修改结算单状态数据结构 {@link SettlementBatchModifyStatusRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/batch-modify-status")
    BaseResponse batchModifyStatus(@RequestBody @Valid SettlementBatchModifyStatusRequest request);
}
