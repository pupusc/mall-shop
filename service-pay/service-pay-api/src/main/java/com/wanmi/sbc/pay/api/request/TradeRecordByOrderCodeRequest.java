package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据交易单号获取单笔交易记录request</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordByOrderCodeRequest extends PayBaseRequest{
    private static final long serialVersionUID = 8721394521394530165L;

    /**
     * 订单/退单号
     */
    @ApiModelProperty(value = "订单/退单号")
    @NotBlank
    private String orderId;
}
