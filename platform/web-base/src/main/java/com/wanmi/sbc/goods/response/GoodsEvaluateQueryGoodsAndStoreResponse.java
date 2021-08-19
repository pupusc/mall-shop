package com.wanmi.sbc.goods.response;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/12 10:11
 * @Description:跳转到评价页面的 商品信息和商家信息
 */
@ApiModel
@Data
public class GoodsEvaluateQueryGoodsAndStoreResponse {

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
     * 订单商品是否已评价
     * 0：未评价 1：已评价
     */
    @ApiModelProperty(value = "订单商品是否已评价")
    private Integer goodsTobe = 0;

    /**
     * 店铺服务是否已评价
     * 0：未评价 1：已评价
     */
    @ApiModelProperty(value = "店铺服务是否已评价")
    private Integer storeTobe = 0;

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
