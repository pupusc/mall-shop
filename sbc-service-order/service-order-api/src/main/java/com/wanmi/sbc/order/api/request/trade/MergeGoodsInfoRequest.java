package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
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
public class MergeGoodsInfoRequest implements Serializable {

    private static final long serialVersionUID = 5194608011638426144L;

    /**
     * 订单商品数据，仅包含skuId/价格
     */
    @ApiModelProperty(value = "订单商品数据，仅包含skuId/价格")
    private List<TradeItemDTO> tradeItems;

    /**
     * 关联商品信息
     */
    @ApiModelProperty(value = "关联商品信息")
    private TradeGoodsListDTO goodsInfoResponse;
}
