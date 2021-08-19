package com.wanmi.sbc.customer.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员银行账户DTO
 * @Author: wanggang
 * @CreateDate: 2018/9/11 11:29
 * @Version: 1.0
 */
@ApiModel
@Data
public class CustomerAccountDTO implements Serializable {

    private static final long serialVersionUID = -3771237954670632653L;

    @ApiModelProperty(value = "账户ID")
    @NotBlank
    private String customerAccountId;
}
