package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PayGatewayUploadPayCertificateRequest
 * @Description 上传微信支付证书request类
 * @Author lvzhenwei
 * @Date 2019/5/7 10:00
 **/
@ApiModel
@Data
public class PayGatewayUploadPayCertificateRequest implements Serializable {
    private static final long serialVersionUID = 9059873658951058274L;

    /**
     * 网关id
     */
    @ApiModelProperty(value = "网关id")
    private Long id;

    /**
     * 微信支付证书二进制流
     */
    private byte[] payCertificate;

    /**
     * 微信支付证书类型 1:公众平台微信支付证书；2:开放平台微信支付证书
     */
    private Integer payCertificateType;

}
