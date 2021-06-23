package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class VerifyTradeMarketingResponse implements Serializable {

    private static final long serialVersionUID = -4682800427411972926L;

    @ApiModelProperty(value = "订单营销信息列表")
    private List<TradeMarketingDTO> tradeMarketingList;
}
