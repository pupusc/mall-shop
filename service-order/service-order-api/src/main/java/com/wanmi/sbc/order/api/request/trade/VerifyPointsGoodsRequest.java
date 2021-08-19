package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.goods.bean.vo.PointsGoodsVO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: yinxianzhi
 * @createDate: 2018/5/20 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class VerifyPointsGoodsRequest implements Serializable {

    private static final long serialVersionUID = -1254527104385663788L;

    /**
     * 订单商品数据，仅包含skuId与购买数量
     */
    @ApiModelProperty(value = "订单商品数据，仅包含skuId与购买数量")
    private TradeItemDTO tradeItem;

    /**
     * 积分商品信息
     */
    @ApiModelProperty(value = "积分商品信息")
    private PointsGoodsVO pointsGoodsVO;

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

}
