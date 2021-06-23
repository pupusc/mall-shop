package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class WechatBaseInfoResponse {

    @ApiModelProperty(value = "微信unionid")
    private String id;

    @ApiModelProperty(value = "微信昵称")
    private String name;

    @ApiModelProperty(value = "微信头像路径")
    private String headImgUrl;
}
