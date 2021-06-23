package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据支付网关id获取单个支付渠道项request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:11.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelItemByGatewayRequest extends PayBaseRequest{
    private static final long serialVersionUID = -4592026026215180200L;

    /**
     * 支付网关类型
     */
    @ApiModelProperty(value = "支付网关类型")
    @NotNull
    private PayGatewayEnum gatewayName;
}
