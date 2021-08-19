package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

/**
 * <p>根据支付/退款对象id获取单笔交易记录request</p>
 * Created by of628-wenzhi on 2018-08-13-下午3:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordByChargeRequest extends PayBaseRequest {
    private static final long serialVersionUID = -6945934185133177198L;

    /**
     * 支付/退款对象id
     */
    @ApiModelProperty(value = "支付/退款对象id")
    @NotBlank
    private String chargeId;
}
