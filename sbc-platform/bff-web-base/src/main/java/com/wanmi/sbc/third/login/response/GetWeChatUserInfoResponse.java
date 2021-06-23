package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class GetWeChatUserInfoResponse {

    @ApiModelProperty(value = "错误码")
    private String errcode;

    @ApiModelProperty(value = "错误信息")
    private String errmsg;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "头像路径")
    private String headimgurl;

    @ApiModelProperty(value = "权限列表")
    private List privilege;

    @ApiModelProperty(value = "unionid")
    private String unionid;


}
