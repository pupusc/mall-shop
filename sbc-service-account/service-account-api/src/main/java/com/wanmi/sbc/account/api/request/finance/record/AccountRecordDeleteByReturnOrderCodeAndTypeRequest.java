package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>根据退单号和对账类型删除参数结构</p>
 * Created by daiyitian on 2018-10-25-下午7:39.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountRecordDeleteByReturnOrderCodeAndTypeRequest extends BasePageRequest {


    private static final long serialVersionUID = -8330617644249101191L;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    @NotBlank
    private String returnOrderCode;


    /**
     * 需要查询的对账记录类型 {@link AccountRecordType}
     */
    @ApiModelProperty(value = "需要查询的对账记录类型")
    @NotNull
    private AccountRecordType accountRecordType;

}
