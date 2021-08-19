package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>支付请求扩展参数结构，主要针对PC，H5端的支付请求</p>
 * Created by of628-wenzhi on 2017-08-05-下午5:08.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PayExtraRequest extends PayRequest {

    /**
     * 支付成功后的前端回调url
     */
    @ApiModelProperty(value = "支付成功后的前端回调url")
    private String successUrl;
}
