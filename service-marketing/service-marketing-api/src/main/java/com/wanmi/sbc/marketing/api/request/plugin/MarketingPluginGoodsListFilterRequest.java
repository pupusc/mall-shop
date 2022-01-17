package com.wanmi.sbc.marketing.api.request.plugin;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.MarketingPluginDTO;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>插件公共Request</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketingPluginGoodsListFilterRequest extends MarketingPluginDTO {

    private static final long serialVersionUID = -1088504297015472177L;

    @ApiModelProperty(value = "商品信息列表")
    private List<GoodsInfoDTO> goodsInfos;

    /**
     * 是否魔方商品列表
     */
    @ApiModelProperty(value = "是否魔方商品列表")
    private Boolean moFangFlag;


    /**
     * 是否为秒杀走购物车普通商品提交订单
     */
    private Boolean isFlashSaleMarketing;

    /**
     * 是否提交订单commit接口
     */
    private Boolean commitFlag = Boolean.FALSE;

    /**
     * 是否设置独立字段（付费会员价使用）
     */
    private Boolean isIndependent = Boolean.FALSE;

    @ApiModelProperty("优惠券使用场景1商详页2领券中心3购物车4专题页")
    private List<String> couponScene = Stream.of(CouponSceneType.CART.getType().toString(), CouponSceneType.COUPON_CENTER.getType().toString(), CouponSceneType.GOODS_DETAIL.getType().toString()).collect(Collectors.toList());

}
