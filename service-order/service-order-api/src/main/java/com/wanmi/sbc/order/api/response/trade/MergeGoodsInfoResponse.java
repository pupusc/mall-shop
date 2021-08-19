package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class MergeGoodsInfoResponse implements Serializable {


    private static final long serialVersionUID = -8353082327240263253L;
    /**
     * 订单商品数据，仅包含skuId/价格
     */
    @ApiModelProperty(value = "订单商品数据 仅包含skuId/价格")
    private List<TradeItemVO> tradeItems;
}
