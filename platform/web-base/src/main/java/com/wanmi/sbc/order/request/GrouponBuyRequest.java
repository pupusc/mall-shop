package com.wanmi.sbc.order.request;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:49 2019/5/24
 * @Description:
 */
@ApiModel
@Data
public class GrouponBuyRequest {

    /**
     * 商品skuId
     */
    @ApiModelProperty("商品skuId")
    @NotNull
    private String goodsInfoId;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    @NotNull
    private Long buyCount;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    @ApiModelProperty("是否开团购买")
    @NotNull
    private Boolean openGroupon;

    /**
     * 团号
     */
    @ApiModelProperty("团号")
    private String grouponNo;

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    private DeliveryCycle deliveryCycle;

    /**
     * 用户选择的发货日期
     */
    @ApiModelProperty(value = "用户选择的发货日期")
    private String sendDateRule;

    /**
     *  配送方案 0:商家主导配送 1:客户主导配送
     */
    @ApiModelProperty(value = "配送方案")
    private DeliveryPlan deliveryPlan;

}
