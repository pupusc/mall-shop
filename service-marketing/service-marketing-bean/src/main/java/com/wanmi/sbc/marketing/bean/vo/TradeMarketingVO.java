package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单营销信息</p>
 * Created by of628-wenzhi on 2018-02-26-下午5:57.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeMarketingVO implements Serializable {

    private static final long serialVersionUID = -1134244923271427258L;
    /**
     * 营销Id
     */
    @ApiModelProperty(value = "营销Id")
    private Long marketingId;

    /**
     * 营销名称
     */
    @ApiModelProperty(value = "营销名称")
    private String marketingName;

    /**
     * 营销类型 0：满减 1:满折 2:满赠
     */
    @ApiModelProperty(value = "营销活动类型")
    private MarketingType marketingType;

    /**
     * 该营销活动关联的订单商品id集合
     */
    @ApiModelProperty(value = "该营销活动关联的订单商品id集合")
    private List<String> skuIds;

    /**
     * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
     */
    @ApiModelProperty(value = "营销子类型")
    private MarketingSubType subType;

    /**
     * 满折信息
     */
    @ApiModelProperty(value = "营销满折多级优惠信息")
    private MarketingFullDiscountLevelVO fullDiscountLevel;

    /**
     * 满赠信息
     */
    @ApiModelProperty(value = "营销满赠多级优惠信息")
    private MarketingFullGiftLevelVO giftLevel;

    /**
     * 满减信息
     */
    @ApiModelProperty(value = "营销满减多级优惠信息")
    private MarketingFullReductionLevelVO reductionLevel;
    /**
     * 加价购信息
     */
    private MarkupLevelVO markupLevel;

    /**
     * 打包一口价信息
     */
    @ApiModelProperty(value = "营销打包一口价多级优惠信息")
    private MarketingBuyoutPriceLevelVO buyoutPriceLevel;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountsAmount;

    /**
     * 该活动关联商品除去优惠金额外的应付金额
     */
    @ApiModelProperty(value = "该活动关联商品除去优惠金额外的应付金额")
    private BigDecimal realPayAmount;

    /**
     * 当前满赠活动关联的赠品id列表，非满赠活动则为空
     */
    @ApiModelProperty(value = "当前满赠活动关联的赠品id列表，非满赠活动则为空")
    private List<String> giftIds = new ArrayList<>();
}
