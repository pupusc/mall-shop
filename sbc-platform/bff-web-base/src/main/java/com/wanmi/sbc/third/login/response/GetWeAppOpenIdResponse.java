package com.wanmi.sbc.third.login.response;

import com.wanmi.sbc.customer.response.LoginResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GetWeAppOpenIdResponse {

    @ApiModelProperty(value = "openid")
    private String openid;

    /**
     * session_key,用来解密
     */
    @ApiModelProperty(value = "sessionKey")
    private String sessionKey;


    @ApiModelProperty(value = "unionId")
    private String unionId;

    /**
     * 小程序appId
     */
    @ApiModelProperty(value = "appId")
    private String appId;

    /**
     * 小程序appSecret
     */
    @ApiModelProperty(value = "appSecret")
    private String appSecret;


    /**
     * 登录还是注册，true为登录，false为注册
     */
    @ApiModelProperty(value = "loginFlag")
    private Boolean loginFlag;

    @ApiModelProperty(value = "微信返回结果")
    private WechatBaseInfoResponse info;

    @ApiModelProperty(value = "登录返回结果")
    private LoginResponse login;
}
