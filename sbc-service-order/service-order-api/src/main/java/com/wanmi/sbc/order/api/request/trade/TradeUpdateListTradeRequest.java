package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeDTO;
import com.wanmi.sbc.order.bean.dto.TradeUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: weiwenhao
 * @Description:
 * @Date: 2021-02-03 15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeUpdateListTradeRequest implements Serializable {

    /**
     * 交易单集合
     */
    @ApiModelProperty(value = "交易单")
    private List<TradeDTO> tradeDTOS;
}
