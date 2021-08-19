package com.wanmi.sbc.order.api.response.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 查询客户首笔完成订单返回体
 * @Autho qiaokang
 * @Date：2019-03-07 16:14:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeQueryFirstCompleteResponse implements Serializable {

    private static final long serialVersionUID = -6958861156975079002L;

    /**
     * 交易号
     */
    @ApiModelProperty(value = "交易号")
    private String tradeId;

}
