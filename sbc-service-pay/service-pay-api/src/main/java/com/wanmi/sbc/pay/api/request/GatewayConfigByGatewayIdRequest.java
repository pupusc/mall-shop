package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>根据网关id查询网关配置request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:34.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayConfigByGatewayIdRequest extends PayBaseRequest{
    private static final long serialVersionUID = -7184646772496044992L;

    /**
     * 支付网关id
     */
    @ApiModelProperty(value = "支付网关id")
    @NotNull
    private Long gatewayId;


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

}
