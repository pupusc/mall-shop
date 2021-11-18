package com.wanmi.sbc.order.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.order.bean.vo.CycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.DiscountsVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmMarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:58 2018/9/28
 * @Description: 订单确认返回结构
 */
@ApiModel
@Data
public class TradeConfirmResponse {

    /**
     * 是否为分销员
     */
    @ApiModelProperty(value = "是否为分销员")
    private DefaultFlag isDistributor;

    /**
     * 按商家拆分后的订单项
     */
    @ApiModelProperty(value = "按商家拆分后的订单项")
    private List<TradeConfirmItemVO> tradeConfirmItems;

    /**
     * 订单总额
     */
    @ApiModelProperty(value = "订单总额")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * 商品总额
     */
    @ApiModelProperty(value = "商品总额")
    private BigDecimal goodsTotalPrice = BigDecimal.ZERO;

    /**
     * 优惠总额
     */
    @ApiModelProperty(value = "优惠总额")
    private BigDecimal discountsTotalPrice = BigDecimal.ZERO;

    /**
     * 未使用的优惠券
     */
    @ApiModelProperty(value = "未使用的优惠券")
    private List<CouponCodeVO> couponCodes;
    /**
     * 加价购信息
     */
    @ApiModelProperty(value = "加价购信息")
    private List<MarkupLevelDetailVO> markupLevel;

    /**
     * 小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;

    /**
     * 邀请人名称
     */
    @ApiModelProperty(value = "邀请人名称")
    private String inviteeName;

    /**
     * 返利总额
     */
    @ApiModelProperty(value = "返利总额")
    private BigDecimal totalCommission;

    /**
     * 是否开店礼包
     */
    @ApiModelProperty(value = "是否开店礼包")
    private DefaultFlag storeBagsFlag = DefaultFlag.NO;

    /**
     * 是否组合套装
     */
    private Boolean suitMarketingFlag;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    @ApiModelProperty(value = "是否开团购买")
    private Boolean openGroupon;

    /**
     * 拼团活动是否包邮
     */
    @ApiModelProperty(value = "拼团活动是否包邮")
    private boolean grouponFreeDelivery;

    /**
     * 秒杀活动是否包邮
     */
    @ApiModelProperty(value = "秒杀活动是否包邮")
    private Boolean flashFreeDelivery;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    @ApiModelProperty(value = "购买积分")
    private Long totalBuyPoint = 0L;

    /**
     * 是否为周期购订单
     */
    @ApiModelProperty(value = "是否为周期购订单")
    private Boolean cycleBuyFlag = Boolean.FALSE;


    public BigDecimal getTotalPrice() {
        return tradeConfirmItems.stream().map(i -> i.getTradePrice().getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getGoodsTotalPrice() {
        return tradeConfirmItems.stream().map(i -> i.getTradePrice().getGoodsPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getDiscountsTotalPrice() {
        return tradeConfirmItems.stream().flatMap(i -> i.getDiscountsPrice().stream()).map(DiscountsVO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}