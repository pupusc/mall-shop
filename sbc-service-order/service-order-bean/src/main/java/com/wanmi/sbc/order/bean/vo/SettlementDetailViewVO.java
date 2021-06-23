package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.account.bean.vo.SettleGoodVO;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
/**
 * 结算明细前台和导出公用展示实体
 */
@ApiModel
@Data
public class SettlementDetailViewVO {

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Long index;

    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间")
    private String tradeCreateTime;

    /**
     * 订单入账时间
     */
    @ApiModelProperty(value = "订单入账时间")
    private String finalTime;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String tradeCode;

    /**
     * 订单类型（普通订单  积分订单）
     */
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型", dataType = "com.wanmi.sbc.account.bean.enums.TradeType")
    private String tradeType;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private String deliveryPrice;

    /**
     * 收款方
     */
    @ApiModelProperty(value = "收款方", dataType = "com.wanmi.sbc.account.bean.enums.GatherType")
    private String gatherType;

    /**
     * 退货金额
     */
    @ApiModelProperty(value = "退货金额")
    private String returnPrice;

    /**
     * 退单改价差额
     */
    @ApiModelProperty(value = "退单改价差额")
    private BigDecimal returnSpecialPrice;

    /**
     * 店铺应收金额
     */
    @ApiModelProperty(value = "店铺应收金额")
    private BigDecimal storePrice;

    /**
     * 供货总额
     */
    @ApiModelProperty(value = "供货总额")
    private BigDecimal providerPrice;

    /**
     * 积分商品结算价
     */
    @ApiModelProperty(value = "积分商品结算价")
    private BigDecimal pointsSettlementPrice;

    /**
     * 商品集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<SettlementDetailGoodsViewVO> goodsViewList;




    /**
     * 组装前台展示的实体
     *
     * @param settlementDetails
     * @return
     */
    public static List<SettlementDetailViewVO> renderSettlementDetailForView(List<SettlementDetailVO> settlementDetails,
                                                                           boolean isExcel) {
        if (settlementDetails == null || settlementDetails.isEmpty()) return new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        return settlementDetails.stream().map(detail -> {
            SettlementDetailViewVO view = new SettlementDetailViewVO();
            BigDecimal returnPrice = detail.getSettleTrade().getReturnPrice();
            BigDecimal deliveryPrice = detail.getSettleTrade().getDeliveryPrice();
            //组装前台显示的实体，excel导出和页面展示
            view.setIndex((long) count.incrementAndGet());
            view.setTradeCreateTime(DateUtil.format(detail.getSettleTrade().getTradeCreateTime(), DateUtil.FMT_TIME_1));
            LocalDateTime finalTime = detail.getSettleTrade().getFinalTime();
            view.setFinalTime(Objects.nonNull(finalTime) ? DateUtil.format(finalTime, DateUtil.FMT_TIME_1) : "");
            view.setTradeCode(detail.getSettleTrade().getTradeCode());
            view.setTradeType(detail.getSettleTrade().getTradeType() == TradeType.NORMAL_TRADE ? "普通" : "促销");
            view.setOrderType(detail.getSettleTrade().getOrderType() == OrderType.POINTS_ORDER ? "积分订单" : "普通订单");
            view.setDeliveryPrice(deliveryPrice != null ? deliveryPrice.toString() : "-");
            view.setGatherType(detail.getSettleTrade().getGatherType() == GatherType.PLATFORM ? "平台" : "供应商");

            if(detail.getSettleGoodList().stream().map(SettleGoodVO::getReturnNum).max(Long::compare).get() > 0){
                view.setReturnPrice(returnPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }else{
                view.setReturnPrice("-");
            }

            view.setGoodsViewList(detail.getSettleGoodList().stream().map(goods -> {
                SettlementDetailGoodsViewVO goodsView = new SettlementDetailGoodsViewVO();
                goodsView.setGoodsName(goods.getGoodsName());
                goodsView.setSpecial(detail.isSpecial());
                if (goods.getSkuNo() != null && !"".equals(goods.getSkuNo())) {
                    goodsView.setSkuNo(goods.getSkuNo());
                    if (isExcel) {
                        goodsView.setGoodsName(goods.getSkuNo().concat(" ").concat(goodsView.getGoodsName()));
                    }
                }
                if (goods.getSpecDetails() != null && !"".equals(goods.getSpecDetails())) {
                    goodsView.setSpecDetails(goods.getSpecDetails());
                    if (isExcel) {
                        goodsView.setGoodsName(goodsView.getGoodsName().concat(" ").concat(goods.getSpecDetails()));
                    }
                }
                goodsView.setGoodsPrice(goods.getGoodsPrice());
                goodsView.setCateName(goods.getCateName());
                if(Objects.nonNull(goods.getCateRate())){
                    goodsView.setCateRate(goods.getCateRate().toString().concat("%"));
                    //因为解析的时候是先算订单所得佣金，再算退单所还佣金，floor保留2位，保持统一
                    //订单对应佣金
                    goodsView.setPlatformPrice(goods.getSplitPayPrice() != null ? // 现在按照实付金额算，不需要乘以数量
                            goods.getSplitPayPrice().multiply(goods.getCateRate()).divide(new BigDecimal(100)).setScale(2, RoundingMode.FLOOR)
                            : BigDecimal.ZERO);
                }else{
                    goodsView.setCateRate(BigDecimal.ZERO.toString().concat("%"));
                }

                goodsView.setNum(goods.getNum());

                //计算营销和特价各个优惠价
                if (detail.getSettleTrade().getOrderType() != OrderType.POINTS_ORDER) {
                    setSpecialOrMarketingPrice(goodsView, goods);
                }

                goodsView.setSplitPayPrice(goods.getSplitPayPrice() != null ? goods.getSplitPayPrice() : BigDecimal.ZERO);

                if (goods.getReturnStatus() != null) {
                    goodsView.setReturnStatus(goods.getReturnStatus() == ReturnStatus.RETURNED ? "已退" : "进行中");
                } else {
                    goodsView.setReturnStatus("-");
                }

                goodsView.setReturnNum(goods.getReturnNum() != null && goods.getReturnNum() != 0L ? goods
                        .getReturnNum().toString() : "-");

                if (goods.getReturnNum() != null && goods.getReturnNum() != 0L && goods.getShouldReturnPrice() != null){
                    goodsView.setShouldReturnPrice(goods.getShouldReturnPrice().toString());
                }else{
                    goodsView.setShouldReturnPrice("-");
                }
                goodsView.setPointPrice(goods.getPointPrice());
                goodsView.setCommissionRate(goods.getCommissionRate());
                goodsView.setCommission(goods.getCommission());
                if(null != goods.getSupplyPrice()){
                    goodsView.setSupplyPrice(goods.getSupplyPrice());
                    goodsView.setProviderPrice(goods.getSupplyPrice().multiply(new BigDecimal(goods.getNum())));
                }
                return goodsView;
            }).collect(Collectors.toList()));

            // 店铺应收金额
            BigDecimal storePrice = detail.getSettleTrade().getStorePrice();
            view.setStorePrice(Objects.nonNull(storePrice) ? storePrice : BigDecimal.ZERO);
            // 退单改价差额
            BigDecimal returnSpecialPrice = detail.getSettleTrade().getReturnSpecialPrice();
            view.setReturnSpecialPrice(Objects.nonNull(returnSpecialPrice) ? returnSpecialPrice : BigDecimal.ZERO);

            BigDecimal providerPrice =  view.getGoodsViewList().stream().filter(goodsView->null!=goodsView.getProviderPrice()).map(SettlementDetailGoodsViewVO::getProviderPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
            view.setProviderPrice(providerPrice);
            return view;
        }).collect(Collectors.toList());
    }

    /**
     * 按顺序执行，运算营销的优惠和特价
     * 1。满减/满折
     * 2。特价
     *
     * @param goodsView
     * @param settleGood
     */
    private static void setSpecialOrMarketingPrice(SettlementDetailGoodsViewVO goodsView, SettleGoodVO settleGood) {
        BigDecimal priceForSettle = settleGood.getGoodsPrice().multiply(new BigDecimal(settleGood.getNum()));
        //按顺序获取优惠价格
        //1.满折或者满减
        if (CollectionUtils.isNotEmpty(settleGood.getMarketingSettlements())) {
            for (SettleGoodVO.MarketingSettlement marketingSettlement : settleGood.getMarketingSettlements()) {
                //如果存在满减或者满折营销活动，计算满折/满减优惠价格
                if (marketingSettlement.getMarketingType() == SettleMarketingType.DISCOUNT) {
                    goodsView.setDiscountPrice(priceForSettle.subtract(marketingSettlement.getSplitPrice()).toString());
                    priceForSettle = marketingSettlement.getSplitPrice();
                }

                if (marketingSettlement.getMarketingType() == SettleMarketingType.REDUCTION) {
                    goodsView.setReductionPrice(priceForSettle.subtract(marketingSettlement.getSplitPrice()).toString());
                    priceForSettle = marketingSettlement.getSplitPrice();
                }
            }
        }
        //2.优惠券
        if (CollectionUtils.isNotEmpty(settleGood.getCouponSettlements())){
            //先计算店铺券
            Optional<SettleGoodVO.CouponSettlement> couponSettle = settleGood.getCouponSettlements().stream()
                    .filter(item -> item.getCouponType() == SettleCouponType.STORE_VOUCHERS).findFirst();
            if (couponSettle.isPresent()){
                goodsView.setStoreCouponPrice(priceForSettle.subtract(couponSettle.get().getSplitPrice()).toString());
                priceForSettle = couponSettle.get().getSplitPrice();
            }
            //计算平台券
            couponSettle = settleGood.getCouponSettlements().stream()
                    .filter(item -> item.getCouponType() == SettleCouponType.GENERAL_VOUCHERS).findFirst();
            if (couponSettle.isPresent()){
                goodsView.setCommonCouponPrice(priceForSettle.subtract(couponSettle.get().getSplitPrice()));
                priceForSettle = couponSettle.get().getSplitPrice();
            }
        }

        // 3.积分抵扣
        if (Objects.nonNull(settleGood.getPointPrice())) {
            goodsView.setPointPrice(settleGood.getPointPrice());
            priceForSettle = priceForSettle.subtract(settleGood.getPointPrice());
        }

        // 4.特价
        BigDecimal specialPrice = priceForSettle.subtract(settleGood.getSplitPayPrice() != null ? settleGood.getSplitPayPrice() : BigDecimal.ZERO);
        switch (specialPrice.compareTo(BigDecimal.ZERO)) {
            //大于0 -- 原价比特价高
            case 1:
                goodsView.setSpecialPrice("-".concat(specialPrice.toString()));
                break;
            //等于0 -- 特价和原价相同
            case 0:
                goodsView.setSpecialPrice(specialPrice.toString());
                break;
            //小于0 -- 原价比特价低
            case -1:
                goodsView.setSpecialPrice("+".concat(specialPrice.abs().toString()));
                break;
            default:
                goodsView.setSpecialPrice(specialPrice.toString());
                break;
        }
    }

}
