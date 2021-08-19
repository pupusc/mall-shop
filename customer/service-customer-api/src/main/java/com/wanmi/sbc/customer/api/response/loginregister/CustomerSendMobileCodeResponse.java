package com.wanmi.sbc.customer.api.response.loginregister;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员登录注册-发送手机验证码Request
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSendMobileCodeResponse implements Serializable {

    private static final long serialVersionUID = 8546438564385689810L;

    @ApiModelProperty(value = "验证码")
    private Integer result;
}
