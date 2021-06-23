package com.wanmi.sbc.third.login.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-08 10:36
 **/
@Data
@ApiModel
public class WechatAuthRequest implements Serializable {
    @ApiModelProperty("微信临时授权码")
    @NotBlank(message = "授权失败, 微信授权码为空, 请重新授权!")
    private String code;

    @ApiModelProperty("解密密钥")
    @NotBlank(message = "参数不能为空！")
    private String iv;

    @ApiModelProperty("微信加密数据")
    @NotBlank(message = "授权失败, 获取不到手机信息, 请重新授权!")
    private String encryptedData;
}