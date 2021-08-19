package com.wanmi.sbc.customer.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * Created by chenli on 2018/8/7.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVerificationCodeRequest {

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @NotBlank
    private String customerAccount;

    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码")
    @NotBlank
    private String verificationCode;
}
