package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>根据订单号和对账类型删除参数结构</p>
 * Created by daiyitian on 2018-10-25-下午7:39.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRecordDeleteByOrderCodeAndTypeRequest extends BasePageRequest {

    private static final long serialVersionUID = -2135305953760396349L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @NotBlank
    private String orderCode;


    /**
     * 需要查询的对账记录类型 {@link AccountRecordType}
     */
    @ApiModelProperty(value = "需要查询的对账记录类型")
    @NotNull
    private AccountRecordType accountRecordType;

}
