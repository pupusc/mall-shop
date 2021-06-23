package com.wanmi.sbc.goods.response;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateInfoResponse {

    /**
     * 商品评论信息
     */
    @ApiModelProperty(value = "商品评论信息")
    private GoodsEvaluateVO goodsEvaluateVO;

    /**
     * 商品评论信息-图片
     */
    @ApiModelProperty(value = "商品评论信息-图片")
    private List<GoodsEvaluateImageVO> goodsEvaluateImageVOS;

    /**
     * 店铺评分信息
     */
    @ApiModelProperty(value = "店铺评分信息")
    private StoreEvaluateVO storeEvaluateVO;

    /**
     * 订单信息
     */
    @ApiModelProperty(value = "订单信息")
    private TradeItemVO tradeVO;

    /**
     * 店铺评分聚合信息
     */
    @ApiModelProperty(value = "店铺评分聚合信息")
    private StoreEvaluateSumVO storeEvaluateSumVO;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private String tid;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreVO storeVO;

    /**
     * 订单时间
     */
    @ApiModelProperty(value = "订单时间")
    private String createTime;
}
