package com.wanmi.sbc.authorize.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@ApiModel
public class WechatAuthRequest implements Serializable {

    private static final long serialVersionUID = 1085711596801309350L;

    @ApiModelProperty("微信临时授权码")
    private String code;

    @ApiModelProperty("解密密钥")
    @NotBlank(message = "参数不能为空！")
    private String iv;

    @ApiModelProperty("微信加密数据")
    @NotBlank(message = "授权失败, 获取不到手机信息, 请重新授权!")
    private String encryptedData;

    @javax.validation.constraints.NotBlank
    private String sessionKey;
}
