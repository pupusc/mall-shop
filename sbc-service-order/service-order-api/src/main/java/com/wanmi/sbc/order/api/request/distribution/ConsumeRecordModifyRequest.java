package com.wanmi.sbc.order.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 消费记录修改请求
 * @Autho qiaokang
 * @Date：2019-03-05 18:44:58
 */
@Data
@ApiModel
public class ConsumeRecordModifyRequest implements Serializable {

    private static final long serialVersionUID = -6242844330565554805L;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NotNull
    private String orderId;

    /**
     * 有效消费额
     */
    @ApiModelProperty(value = "有效消费额")
    @NotNull
    private BigDecimal validConsumeSum;

    @ApiModelProperty(value = "订单状态")
    private String flowState;

}
