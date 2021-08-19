package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据支付网关枚举获取网关配置request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:30.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayConfigByGatewayRequest extends PayBaseRequest{
    private static final long serialVersionUID = 7055714468473113982L;

    /**
     * 支付网关枚举
     */
    @ApiModelProperty(value = "支付网关枚举")
    @NotNull
    private PayGatewayEnum gatewayEnum;

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
