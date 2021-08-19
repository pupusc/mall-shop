package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: yinxianzhi
 * @createDate: 2019/5/20 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class VerifyPointsGoodsResponse implements Serializable {

    private static final long serialVersionUID = 5946412709807610687L;

    /**
     * 订单商品数据，仅包含skuId与购买数量
     */
    @ApiModelProperty(value = "订单商品数据")
    private TradeItemVO tradeItem;
}
