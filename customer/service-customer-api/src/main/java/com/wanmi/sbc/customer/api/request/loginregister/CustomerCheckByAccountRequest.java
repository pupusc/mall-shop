package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员登录注册-根据会员账号查询Request
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckByAccountRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 4395801428118010660L;
    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    @NotBlank
    private String customerAccount;
}
