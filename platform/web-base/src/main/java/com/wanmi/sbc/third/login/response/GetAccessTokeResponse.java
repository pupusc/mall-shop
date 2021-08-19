package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GetAccessTokeResponse {

    @ApiModelProperty(value = "错误码")
    private String errcode;

    @ApiModelProperty(value = "错误信息")
    private String errmsg;

    @ApiModelProperty(value = "access_token")
    private String access_token;

    @ApiModelProperty(value = "失效时间")
    private String expires_in;

    @ApiModelProperty(value = "refresh_token")
    private String refresh_token;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "unionid")
    private String unionid;

    @ApiModelProperty(value = "scope")
    private String scope;
}
