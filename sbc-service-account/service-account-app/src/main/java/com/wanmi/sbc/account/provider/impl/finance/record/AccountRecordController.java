package com.wanmi.sbc.account.provider.impl.finance.record;

import com.wanmi.sbc.account.api.request.finance.record.AccountRecordToExcelRequest;
import com.wanmi.sbc.account.api.response.finance.record.AccountRecordToExcelResponse;
import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordProvider;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordDeleteByReturnOrderCodeAndTypeRequest;
import com.wanmi.sbc.account.finance.record.model.entity.Reconciliation;
import com.wanmi.sbc.account.finance.record.service.AccountRecordService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对账记录操作接口</p>
 * Created by daiyitian on 2018-10-25-下午5:51.
 */
@RestController
@Validated
public class AccountRecordController implements AccountRecordProvider {

    @Autowired
    private AccountRecordService accountRecordService;


    /**
     * 新增对账记录
     *
     * @param request 记录 {@link AccountRecordAddRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */

    public BaseResponse add(@RequestBody @Valid AccountRecordAddRequest request) {
        Reconciliation reconciliation = new Reconciliation();
        KsBeanUtil.copyPropertiesThird(request, reconciliation);
        accountRecordService.add(reconciliation);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据订单号和交易类型删除
     *
     * @param request 删除条件 {@link AccountRecordDeleteByOrderCodeAndTypeRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */

    public BaseResponse deleteByOrderCodeAndType(@RequestBody @Valid AccountRecordDeleteByOrderCodeAndTypeRequest
                                                         request) {
        accountRecordService.deleteByOrderCodeAndType(request.getOrderCode(), toType(request.getAccountRecordType()));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据退单号和交易类型删除
     *
     * @param request 删除条件 {@link AccountRecordDeleteByReturnOrderCodeAndTypeRequest}
     * @return 成功失败结果 {@link BaseResponse}
     */

    public BaseResponse deleteByReturnOrderCodeAndType(@RequestBody @Valid
                                                               AccountRecordDeleteByReturnOrderCodeAndTypeRequest
                                                               request) {
        accountRecordService.deleteByReturnOrderCodeAndType(request.getReturnOrderCode(), toType(request
                .getAccountRecordType()));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 对账数据导出Excel
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<AccountRecordToExcelResponse> writeAccountRecordToExcel(@RequestBody @Valid AccountRecordToExcelRequest request) {
        return BaseResponse.success(AccountRecordToExcelResponse.builder().file(accountRecordService.writeAccountRecordToExcel(request)).build());
    }

    private Byte toType(AccountRecordType type) {
        return Byte.parseByte(String.valueOf(type.toValue()));
    }
}
