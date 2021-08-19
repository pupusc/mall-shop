package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
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
public class VerifyGoodsRequest implements Serializable {

    private static final long serialVersionUID = 7404104804202415949L;

    /**
     * 订单商品数据，仅包含skuId与购买数量
     */
    @ApiModelProperty(value = "订单商品数据，仅包含skuId与购买数量")
    private List<TradeItemDTO> tradeItems;

    /**
     * 旧订单商品数据，可以为emptyList，用于编辑订单的场景，由于旧订单商品库存已先还回但事务未提交，因此在处理中将库存做逻辑叠加
     */
    @ApiModelProperty(value = "旧订单商品数据")
    private List<TradeItemDTO> oldTradeItems;

    /**
     * 关联商品信息
     */
    @ApiModelProperty(value = "关联商品信息")
    private TradeGoodsInfoPageDTO goodsInfoResponse;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 是否填充订单商品信息与设价(区间价/已经算好的会员价)
     */
    @ApiModelProperty(value = "是否填充订单商品信息与设价(区间价/已经算好的会员价)",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isFull;
}
