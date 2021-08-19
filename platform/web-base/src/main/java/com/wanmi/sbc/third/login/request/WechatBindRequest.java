package com.wanmi.sbc.third.login.request;

import com.wanmi.sbc.third.login.TerminalStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class WechatBindRequest {

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    String code;

    /**
     *  终端类型
     */
    @ApiModelProperty(value = "终端类型")
    @NotNull
    TerminalStringType type;

    /**
     * 微信登录appId
     */
    @ApiModelProperty(value = "微信登录appId")
    private String appId;

    /**
     * 微信登录appSecret
     */
    @ApiModelProperty(value = "微信登录appSecret")
    private String appSecret;
}
