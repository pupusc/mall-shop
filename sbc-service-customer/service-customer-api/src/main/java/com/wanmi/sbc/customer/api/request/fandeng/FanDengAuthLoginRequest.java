package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 樊登登陆及注册接口入参
 * @author: Mr.Tian
 * @create: 2021-01-28 13:45
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FanDengAuthLoginRequest implements Serializable {

    private static final long serialVersionUID = -4876836075205692691L;

    /**
     * 手机区号
     */
    @ApiModelProperty(value = "手机区号")
    private String areaCode;
    /**
     * 用户手机号码
     */
    @ApiModelProperty(value = "用户手机号码")
    @NotBlank
    private String mobile;

    /**
     * 用户登录验证码
     */
    @ApiModelProperty(value = "用户登录验证码")
    @NotBlank
    private String verificationCode;

}
