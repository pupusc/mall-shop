package com.wanmi.sbc.pay.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * Created by sunkun on 2017/8/9.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PayGatewayConfigDTO extends BaseRequest {

    /**
     * 身份标识
     */
    @ApiModelProperty(value = "身份标识")
    private String apiKey;

    /**
     * secret key
     */
    @ApiModelProperty(value = "secret key")
    @Length(max = 60)
    private String secret;

    /**
     * 第三方应用标识
     */
    @ApiModelProperty(value = "第三方应用标识")
    @Length(max = 40)
    private String appId;

    /**
     * 微信app_id
     */
    @ApiModelProperty(value = "微信app_id")
    private String appId2;

    /**
     * 收款账号
     */
    @ApiModelProperty(value = "收款账号")
    private String account;

    /**
     * 私钥
     */
    @ApiModelProperty(value = "私钥")
    private String privateKey;

    /**
     * 公钥
     */
    @ApiModelProperty(value = "公钥")
    private String publicKey;

    /**
     * PC前端后台接口地址
     */
    @ApiModelProperty(value = "PC前端后台接口地址")
    private String pcBackUrl;

    /**
     * PC前端web地址
     */
    @ApiModelProperty(value = "PC前端web地址")
    private String pcWebUrl;

    /**
     * boss后台接口地址
     */
    @ApiModelProperty(value = "boss后台接口地址")
    private String bossBackUrl;

    /**
     * 微信开放平台app_id---微信app支付使用
     */
    private String openPlatformAppId;

    /**
     * 微信开放平台secret---微信app支付使用
     */
    private String openPlatformSecret;

    /**
     * 微信开放平台api key---微信app支付使用
     */
    private String openPlatformApiKey;

    /**
     * 微信开放平台商户号---微信app支付使用
     */
    private String openPlatformAccount;
}
