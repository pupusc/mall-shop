package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.dto.TradeDTO;
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
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradePayCallBackOnlineRequest implements Serializable {

    private static final long serialVersionUID = 8653696875457710194L;
    /**
     * 交易单
     */
    @ApiModelProperty(value = "交易单")
    private TradeDTO trade;

    /**
     * 支付单
     */
    @ApiModelProperty(value = "支付单")
    private PayOrderDTO payOrderOld;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
