package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>新增支付网关request</p>
 * Created by of628-wenzhi on 2018-08-13-下午5:28.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayAddRequest extends PayBaseRequest{
    private static final long serialVersionUID = 551458013286187240L;

    /**
     * 网关名称
     */
    @ApiModelProperty(value = "网关名称")
    @NotNull
    private PayGatewayEnum gatewayEnum;

    /**
     * 是否聚合支付
     */
    @ApiModelProperty(value = "是否聚合支付")
    @NotNull
    private Boolean type;

}
