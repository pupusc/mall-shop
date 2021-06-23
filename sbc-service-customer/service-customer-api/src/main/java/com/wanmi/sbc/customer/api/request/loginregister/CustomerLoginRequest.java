package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员登录注册-登录Request
 */
@ApiModel
@Data
public class CustomerLoginRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 761117089236331874L;
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    @NotBlank
    private String customerAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;
}
