package com.wanmi.sbc.customer.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员银行账户添加/修改共用DTO
 */
@ApiModel
@Data
public class CustomerAccountAddOrModifyDTO implements Serializable {
    private static final long serialVersionUID = -7500032632159374088L;
    /**
     * 账户ID
     */
    @ApiModelProperty(value = "账户ID")
    private String customerAccountId;

    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 账户名字
     */
    @ApiModelProperty(value = "账户名字")
    @NotBlank
    private String customerAccountName;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    @NotBlank
    private String customerAccountNo;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @NotBlank
    private String customerBankName;
}
