package com.wanmi.sbc.third.login.response;

import com.wanmi.sbc.customer.response.LoginResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ThirdLoginResponse {

    @ApiModelProperty(value = "是否登录")
    private Boolean loginFlag;

    @ApiModelProperty(value = "微信返回结果")
    private WechatBaseInfoResponse info;

    @ApiModelProperty(value = "登录返回结果")
    private LoginResponse login;

    @ApiModelProperty(value = "登录凭证")
    private String token;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "手机号")
    private String phoneNum;
}
