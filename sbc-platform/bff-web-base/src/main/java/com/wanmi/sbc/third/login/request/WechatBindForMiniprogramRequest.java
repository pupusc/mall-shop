package com.wanmi.sbc.third.login.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sbc-micro-service
 * @description: 小程序绑定微信
 * @create: 2020-03-12 17:23
 **/
@Data
public class WechatBindForMiniprogramRequest {

    @ApiModelProperty(value = "第三方关系关联(union)Id")
    private String unionid;

    @ApiModelProperty(value = "微信授权openId")
    private String openid;

    @ApiModelProperty(value = "头像路径")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickname;
}