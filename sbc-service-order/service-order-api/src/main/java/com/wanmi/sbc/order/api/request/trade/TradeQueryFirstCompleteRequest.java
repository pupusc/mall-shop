package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 查询首笔完成订单请求
 * @Autho qiaokang
 * @Date：2019-03-07 16:12:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeQueryFirstCompleteRequest implements Serializable {

    private static final long serialVersionUID = -6618940082608261405L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private String customerId;
}
