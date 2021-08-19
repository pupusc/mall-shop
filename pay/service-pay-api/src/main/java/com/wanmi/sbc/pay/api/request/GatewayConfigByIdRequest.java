package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据id查询单个支付网关配置request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:27.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayConfigByIdRequest extends PayBaseRequest{
    private static final long serialVersionUID = -4887150861025909558L;

    /**
     * 支付网关配置id
     */
    @ApiModelProperty(value = "支付网关配置id")
    @NotNull
    private Long gatewayConfigId;
}
