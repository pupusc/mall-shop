package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.customer.bean.vo.MiniCompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.MiniStoreVO;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingSimVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.bean.vo.PurchaseMarketingCalcVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseListResponse implements Serializable {

    private static final long serialVersionUID = -1568352064104571291L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private List<GoodsInfoVO> goodsInfos = new ArrayList<>();

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsVO> goodses = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

    /**
     * 商品级别价格列表
     */
    @ApiModelProperty(value = "商品级别价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();

    /**
     * 公司信息
     */
    @ApiModelProperty(value = "公司信息")
    private List<MiniCompanyInfoVO> companyInfos = new ArrayList<>();

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<MiniStoreVO> stores = new ArrayList<>();

    /**
     * 店铺营销信息，storeId作为map的key
     */
    @ApiModelProperty(value = "店铺营销信息,key为店铺id，value为营销信息列表")
    private Map<Long, List<PurchaseMarketingCalcVO>> storeMarketingMap;

    /**
     * 店铺优惠券信息，storeId作为map的key，Boolean代表有没有优惠券活动
     */
    @ApiModelProperty(value = "店铺是否有优惠券活动map,key为店铺id，value为是否存在优惠券活动")
    private Map<Long, Boolean> storeCouponMap;

    /**
     * 商品营销信息，skuId作为map的key
     */
    @ApiModelProperty(value = "单品营销信息map,key为单品id，value为营销列表")
    private Map<String, List<MarketingViewVO>> goodsMarketingMap = new HashMap<>();

    /**
     * 商品选择的营销
     */
    @ApiModelProperty(value = "商品选择的营销")
    private List<GoodsMarketingVO> goodsMarketings = new ArrayList<>();

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
     * 采购单商品总分销佣金
     */
    @ApiModelProperty(value = "采购单商品总分销佣金")
    private BigDecimal distributeCommission = BigDecimal.ZERO;

    /**
     * 是否自购-显示返利
     */
    @ApiModelProperty(value = "是否自购")
    private boolean selfBuying = false;

    /***
     * 分销设置VO
     */
    DistributionSettingSimVO distributionSettingSimVO;

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

    /**
     * 预约活动信息
     */
    @ApiModelProperty(value = "预约活动信息")
    private List<AppointmentSaleVO> appointmentSaleVOList = new ArrayList<>();

    /**
     * 预售活动信息
     */
    @ApiModelProperty(value = "预售活动信息")
    private List<BookingSaleVO> bookingSaleVOList = new ArrayList<>();

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;
}
