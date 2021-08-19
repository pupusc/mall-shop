package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@ApiModel
public class PurchaseCalcAmountDTO implements Serializable {

    private static final long serialVersionUID = 4138124432178749237L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsVO> goodses;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    /**
     * 公司信息
     */
    @ApiModelProperty(value = "公司信息")
    private List<CompanyInfoVO> companyInfos;

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<StoreVO> stores;

    /**
     * 店铺营销信息，storeId作为map的key
     */
    @ApiModelProperty(value = "店铺营销信息,storeId作为map的key")
    private Map<Long, List<PurchaseMarketingCalcDTO>> storeMarketingMap;

    /**
     * 店铺优惠券信息，storeId作为map的key，Boolean代表有没有优惠券活动
     */
    @ApiModelProperty(value = "店铺优惠券信息,storeId作为map的key，Boolean代表有没有优惠券活动")
    private Map<Long, Boolean> storeCouponMap;

    /**
     * 商品营销信息，skuId作为map的key
     */
    @ApiModelProperty(value = "商品营销信息,skuId作为map的key")
    private Map<String, List<MarketingViewVO>> goodsMarketingMap;

    /**
     * 商品选择的营销
     */
    @ApiModelProperty(value = "商品选择的营销")
    private List<GoodsMarketingVO> goodsMarketings;

    /**
     * 采购单商品总金额
     */
    @ApiModelProperty(value = "采购单商品总金额")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * 采购单商品总金额减去优惠后的金额
     */
    @ApiModelProperty(value = "采购单商品总金额减去优惠后的金额")
    private BigDecimal tradePrice = BigDecimal.ZERO;

    /**
     * 采购单优惠金额
     */
    @ApiModelProperty(value = "采购单优惠金额")
    private BigDecimal discountPrice = BigDecimal.ZERO;

    /**
     * 是否自购-显示返利
     */
    @ApiModelProperty(value = "是否自购")
    private boolean selfBuying = false;

    /**
     * 购买积分，被用于普通订单的积分+金额混合商品
     */
    @ApiModelProperty(value = "购买积分")
    private Long totalBuyPoint = 0L;

    /**
     * 积分是否不足
     */
    @ApiModelProperty(value = "购买积分是否不足")
    private Boolean buyPointValid = true;

}
