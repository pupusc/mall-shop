package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>获取当前终端对应网关下已开启的支付渠道项request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:15.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenedChannelItemRequest extends PayBaseRequest{
    private static final long serialVersionUID = 4344977503858756794L;

    /**
     * 支付网关名称
     */
    @ApiModelProperty(value = "支付网关名称")
    @NotNull
    private PayGatewayEnum gatewayName;

    /**
     * 终端类型
     */
    @ApiModelProperty(value = "终端类型")
    @NotNull
    private TerminalType terminalType;
}
