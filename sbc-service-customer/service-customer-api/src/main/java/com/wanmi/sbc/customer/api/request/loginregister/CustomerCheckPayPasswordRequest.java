package com.wanmi.sbc.customer.api.request.loginregister;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员支付密码校验参数入参
 */
@ApiModel
@Data
public class CustomerCheckPayPasswordRequest implements Serializable {

    private static final long serialVersionUID = -7269340689827623310L;


    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank
    private String payPassword;
}
