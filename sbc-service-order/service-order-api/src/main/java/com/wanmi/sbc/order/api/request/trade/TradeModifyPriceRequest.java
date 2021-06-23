package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.TradePriceChangeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 11:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeModifyPriceRequest implements Serializable {

    /**
     * 交易价格
     */
    @ApiModelProperty(value = "交易价格")
    private TradePriceChangeDTO tradePriceChangeDTO;

    /**
     * 交易单号
     */
    @ApiModelProperty(value = "交易单号")
    private String tid;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
