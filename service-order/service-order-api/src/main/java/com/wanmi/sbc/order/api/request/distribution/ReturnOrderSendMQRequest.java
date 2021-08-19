package com.wanmi.sbc.order.api.request.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 退单状态变更发送MQ请求体
 * @Autho qiaokang
 * @Date：2019-03-06 15:43:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderSendMQRequest implements Serializable {

    private static final long serialVersionUID = 2690163210283482630L;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderId;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String returnId;

    /**
     * 购买人id
     */
    @ApiModelProperty(value = "购买人id")
    private String customerId;

    /**
     * 是否增加退单 true：增加，false：减少
     */
    @ApiModelProperty(value = "是否增加退单")
    private boolean addFlag;

}
