package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.account.bean.enums.PayWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeDefaultPayRequest implements Serializable {

    /**
     * 交易id
     */
    @NotNull
    @ApiModelProperty(value = "交易id")
    private String tid;

    @NotNull
    @ApiModelProperty(value = "支付网关")
    private PayWay payWay;

}
