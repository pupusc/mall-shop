package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;

/**
 * 子订单返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class FindProviderTradeResponse implements Serializable {

    private static final long serialVersionUID = -8896514637363846221L;

    @ApiModelProperty(value = "查询子订单返回结果")
    private List<TradeVO> tradeVOList;
}
