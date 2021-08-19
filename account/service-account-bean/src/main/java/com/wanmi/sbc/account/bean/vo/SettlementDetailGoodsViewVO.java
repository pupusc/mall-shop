package com.wanmi.sbc.account.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 结算明细前台和导出公用展示实体
 */
@ApiModel
@Data
public class SettlementDetailGoodsViewVO {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品sku编码
     */
    @ApiModelProperty(value = "商品sku编码")
    private String skuNo;

    /**
     * 商品规格值
     */
    @ApiModelProperty(value = "商品规格值")
    private String specDetails;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal goodsPrice;

    /**
     * 商品供货价
     */
    @ApiModelProperty(value = "商品供货价")
    private BigDecimal providerPrice;

    /**
     * 供货单价
     */
    @ApiModelProperty(value = "供货价单价")
    private BigDecimal supplyPrice;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 扣点
     */
    @ApiModelProperty(value = "扣点")
    private String cateRate;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Long num;

    /**
     * 是否特价
     */
    @ApiModelProperty(value = "是否特价")
    private boolean isSpecial;

    /**
     * 平台佣金
     */
    @ApiModelProperty(value = "平台佣金")
    private BigDecimal platformPrice;

    /**
     * 平台佣金，用于显示
     */
    @ApiModelProperty(value = "平台佣金，用于显示")
    private String platformPriceString;

    /**
     * 退还平台佣金
     */
    @ApiModelProperty(value = "退还平台佣金")
    private BigDecimal backPlatformPrice;

    /**
     * 订单改价差额
     */
    @ApiModelProperty(value = "订单改价差额")
    private String specialPrice;

    /**
     * 订单满减金额
     */
    @ApiModelProperty(value = "订单满减金额")
    private String reductionPrice = "-";

    /**
     * 订单满折金额
     */
    @ApiModelProperty(value = "订单满折金额")
    private String discountPrice = "-";

    /**
     * 通用券优惠
     */
    @ApiModelProperty(value = "通用券优惠")
    private BigDecimal commonCouponPrice;

    /**
     * 通用券优惠，作为显示
     */
    @ApiModelProperty(value = "通用券优惠")
    private String commonCouponPriceString;

    /**
     * 店铺券优惠金额
     */
    @ApiModelProperty(value = "店铺券优惠金额")
    private String storeCouponPrice = "-";

    /**
     * 退货返还通用券补偿
     */
    @ApiModelProperty(value = "退货返还通用券补偿")
    private BigDecimal commonReturnCouponPrice;

    /**
     * 退货返还通用券补偿
     */
    @ApiModelProperty(value = "退货返还通用券补偿")
    private String commonReturnCouponPriceString;

    /**
     * 实付金额（分摊价）
     */
    @ApiModelProperty(value = "实付金额（分摊价）")
    private BigDecimal splitPayPrice;

    /**
     * 退款状态
     */
    @ApiModelProperty(value = "退款状态", dataType = "com.wanmi.sbc.account.bean.enums.ReturnStatus")
    private String returnStatus;

    /**
     * 退货数量
     */
    @ApiModelProperty(value = "退货数量")
    private String returnNum;

    /**
     * 应退金额
     */
    @ApiModelProperty(value = "应退金额")
    private String shouldReturnPrice;

    /**
     * 积分抵扣金额
     */
    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal pointPrice;

    /**
     * 佣金比例
     */
    @ApiModelProperty(value = "佣金比例")
    private BigDecimal commissionRate = BigDecimal.ZERO;

    /**
     * 分销佣金
     */
    @ApiModelProperty(value = "分销佣金")
    private BigDecimal commission = BigDecimal.ZERO;

    /**
     * 复写通用券优惠券get方法
     * @return
     */
    public String getCommonCouponPriceString(){
        if (commonCouponPrice != null){
            return commonCouponPrice.toString();
        }else{
            return "-";
        }
    }

    /**
     * 复写通用券优惠券get方法
     * @return
     */
    public String getCommonReturnCouponPriceString(){
        if (commonReturnCouponPrice != null){
            return commonReturnCouponPrice.toString();
        }else{
            return "-";
        }
    }

    /**
     * 复写特价get方法
     * @return
     */
    public String getSpecialPrice(){
        if(isSpecial){
            return specialPrice;
        }else{
            return "-";
        }
    }

    /**
     * 复写平台佣金get方法
     * @return
     */
    public String getPlatformPriceString(){
        return platformPrice.toString();
    }

    /**
     * 复写积分抵扣get方法
     * @return
     */
    public String getPointPrice() {
        if (pointPrice != null){
            return String.format("%.2f", pointPrice);
        }else{
            return "-";
        }
    }

    /**
     * 复写佣金比例get方法
     * @return
     */
    public String getCommissionRate() {
        if (commissionRate != null){
            return commissionRate.multiply(new BigDecimal(100)).toString() + "%";
        }else{
            return "-";
        }
    }

    /**
     * 复写分销佣金get方法
     * @return
     */
    public String getCommission() {
        if (commission != null){
            return String.format("%.2f",  commission);
        }else{
            return "-";
        }
    }

}
