package com.wanmi.sbc.account.api.request.offline;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 线下账户列表请求
 */
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfflineAccountListWithoutDeleteFlagByBankNoRequest extends AccountBaseRequest {
    private static final long serialVersionUID = 6272841426077091524L;

    /**
     * 线下银行账号
     */
    @ApiModelProperty(value = "线下银行账号")
    @NotNull
    private String bankNo;
}
