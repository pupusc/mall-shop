package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员登录注册-验证手机号是否可发送验证码Request
 */
@ApiModel
@Data
public class CustomerValidateSendMobileCodeRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = -5160438284019550150L;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @NotBlank
    private String mobile;
}
