package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据支付/退款对象id获取交易记录数request</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordCountByOrderCodeRequest extends PayBaseRequest{
    private static final long serialVersionUID = 1227800173539009538L;

    /**
     * 业务单号（订单号/退单号）
     */
    @ApiModelProperty(value = "业务单号（订单号/退单号）")
    @NotBlank
    private String orderId;
}
