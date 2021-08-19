package com.wanmi.sbc.account.api.provider.finance.record;

import com.wanmi.sbc.account.api.request.finance.record.AccountRecordAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByReturnOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordToExcelRequest;
import com.wanmi.sbc.account.api.response.finance.record.AccountRecordToExcelResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对账记录操作接口</p>
 * Created by daiyitian on 2018-10-25-下午5:51.
 */
@FeignClient(value = "${application.account.name}", contextId = "AccountRecordProvider")
public interface AccountRecordProvider {

    /**
     * 新增对账记录
     *
     * @param request 记录 {@link AccountRecordAddRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/add")
    BaseResponse add(@RequestBody @Valid AccountRecordAddRequest request);

    /**
     * 根据订单号和交易类型删除
     *
     * @param request 删除条件 {@link AccountRecordDeleteByOrderCodeAndTypeRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/delete-by-order-code-and-type")
    BaseResponse deleteByOrderCodeAndType(@RequestBody @Valid AccountRecordDeleteByOrderCodeAndTypeRequest
                                                  request);

    /**
     * 根据退单号和交易类型删除
     *
     * @param request 删除条件 {@link AccountRecordDeleteByReturnOrderCodeAndTypeRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/delete-by-return-order-code-and-type")
    BaseResponse deleteByReturnOrderCodeAndType(@RequestBody @Valid AccountRecordDeleteByReturnOrderCodeAndTypeRequest
                                                        request);

    /**
     * 对账数据导出Excel
     *
     * @param request
     * @return
     */
    @PostMapping("/account/${application.account.version}/finance/record/export")
    BaseResponse<AccountRecordToExcelResponse> writeAccountRecordToExcel(@RequestBody @Valid AccountRecordToExcelRequest
                                                                                 request);
}
