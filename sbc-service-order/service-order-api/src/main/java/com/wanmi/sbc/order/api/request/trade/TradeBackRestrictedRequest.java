package com.wanmi.sbc.order.api.request.trade;


import com.wanmi.sbc.order.bean.enums.BackRestrictedType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回限售数量的请求实体
 * @author baijz
 * @date 2018/5/5.13:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeBackRestrictedRequest {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String tradeId;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String backOrderId;

    /**
     * 退还限售数量的类型
     */
    @ApiModelProperty(value = "退还限售数量的类型")
    private BackRestrictedType backRestrictedType;
}
