package com.wanmi.sbc.marketing.coupon.model.entity;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午7:58 2018/9/27
 * @Description:
 * 订单优惠券相关信息快照;
 * PS：确认订单页初始方法中生成，确认订单页勾选优惠券时使用；主要是因为，确认订单初始方法中的数据在勾选优惠券时还要使用
 */
@Data
@Document(collection = "tradeCouponSnapshot")
public class TradeCouponSnapshot implements Serializable {

    private static final long serialVersionUID = -3685442997546003511L;

    @Id
    private String id;

    /**
     * 客户id
     */
    private String buyerId;

    /**
     * 订单商品列表
     */
    private List<CheckGoodsInfo> goodsInfos = new ArrayList<>();


    /**
     * 可用优惠券列表
     */
    private List<CheckCouponCode> couponCodes = new ArrayList<>();


    @Data
    public static class CheckGoodsInfo {

        /**
         * 店铺id
         */
        private Long storeId;

        /**
         * 商品id
         */
        private String goodsInfoId;

        /**
         * 均摊价
         */
        private BigDecimal splitPrice;

    }

    @Data
    public static class CheckCouponCode {

        /**
         * 优惠券码id
         */
        private String couponCodeId;

        /**
         * 优惠券面值
         */
        private BigDecimal denomination;

        /**
         * 购满多少钱
         */
        private BigDecimal fullBuyPrice;

        /**
         * 优惠券类型 0通用券 1店铺券 2运费券
         */
        private CouponType couponType;

        /**
         * 优惠券关联的商品
         */
        private List<String> goodsInfoIds;

    }

}
