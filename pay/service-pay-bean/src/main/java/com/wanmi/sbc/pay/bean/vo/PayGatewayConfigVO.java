package com.wanmi.sbc.pay.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>支付网关配置项Response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:25.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayGatewayConfigVO implements Serializable{
    private static final long serialVersionUID = -6118164414356673936L;

    @ApiModelProperty(value = "支付网关配置项id")
    private Long id;

    @ApiModelProperty(value = "支付网关")
    private PayGatewayVO payGateway;

    /**
     * 身份标识
     */
    @ApiModelProperty(value = "身份标识")
    private String apiKey;

    /**
     * secret key
     */
    @ApiModelProperty(value = "secret key")
    private String secret;

    /**
     * 收款账户
     */
    @ApiModelProperty(value = "收款账户")
    private String account;

    /**
     * 第三方应用标识
     */
    @ApiModelProperty(value = "第三方应用标识")
    private String appId;

    /**
     * 微信app_id
     */
    @ApiModelProperty(value = "微信app_id")
    private String appId2;

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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

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
