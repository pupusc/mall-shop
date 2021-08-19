package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 小程序授权
 * @version 0.1.0
 */
@ApiModel
@Data
public class LittleProgramAuthResponse implements Serializable {
    private static final long serialVersionUID = -1690877923261557172L;

    @ApiModelProperty(value = "用户唯一标识")
    private String openid;

    @ApiModelProperty(value = "会话密钥")
    private String session_key;

    @ApiModelProperty(value = "用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回")
    private String unionid;

    @ApiModelProperty(value = "错误码")
    private Long   errcode;

    @ApiModelProperty(value = "错误信息")
    private String errMsg;
}
