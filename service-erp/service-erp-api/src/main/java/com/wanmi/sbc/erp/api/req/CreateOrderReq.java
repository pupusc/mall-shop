package com.wanmi.sbc.erp.api.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/10 1:53 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CreateOrderReq {

    /**
     * 平台号 主单
     */
    private String platformCode;

    /**
     * 订单来源 XX_MALL
     */
    private String orderSource;

//    /**
//     * 平台订单号[orderNumber] 不传
//     */
//    private String platformOrderId;

    /**
     * 用户id [主占用户id]
     */
    private Long userId;

    /**
     * 买家留言
     */
    private String buyerMemo;

    /**
     * 设备类型 WEB
     */
    private Integer deviceType;

    /**
     * 销售渠道ID  SELECT * from t_sale_platform;
     */
    private Integer saleChannelId;

    /**
     * 邮费
     */
    private Integer postFee;

    /**
     * 支付超时
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime payTimeOut;

//    /**
//     * 订单优惠 【不用】
//     */
//    private List<BuyDiscountReq> orderDiscounts;

    /**
     * 商品信息
     */
    private List<BuyGoodsReq> buyGoodsBOS;

    /**
     * 地址
     */
    private BuyAddressReq buyAddressBO;

//    /**
//     * 支付流水 走真实支付
//     */
//    private List<BuyPaymentReq> buyPaymentBO;

    /**
     * 商城ID t_sale_platform TODO
     */
    private String shopId;

//    /**
//     * 推广人类型，1、站外推广人类型，2、openid，3、thrid，4、樊登用户编号，5、手机号
//     */
//    private Integer promoType;

    /**
     * 卖家备注
     */
    private String sellerMemo;

    /**
     * 下单类型，1：表示普通的下单流程、先下单、占库存，再完成支付的，存在支付超时时间的概念，2：表示支付后下单，无支付超时概念
     */
    private Integer bookModel = 1;

    /**
     * 下单时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookTime;

//    /**
//     * 订单完成时间
//     */
//    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
//    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
//    private LocalDateTime completeTime;

    /**
     * 快照存储信息 [存储信息]
     */
    private Map<String, String> orderSnapshot = new HashMap<>();

    /**
     * 购买商品
     */
    @Data
    public static class BuyGoodsReq {
//        /**
//         * 商品ID，站内下单【不实用】
//         */
//        private Long goodsId;

        /**
         * 商品编码，对接平台下单
         */
        private String goodsCode;

//        /**
//         * 库存ID 图书订阅下单 【不使用】
//         */
//        private Long stockId;

        /**
         * 数量
         */
        @NotNull(message = "数量不能为空")
        private Integer num;

//        /**
//         * 平台子订单编号 不使用
//         */
//        private String platformItemId;

        /**
         * 价格，单位：分 【市场价】
         */
        private Integer price;

        /**
         * 是否赠送, 1 赠送，0 非赠送，2 补偿 【0元非赠送】
         */
        private Integer giftFlag;


        /**
         * 优惠 商品层面的优惠
         */
        @Valid
        private List<BuyDiscountReq> goodsDiscounts;
    }


    /**
     * 优惠对象
     */
    @Data
    public static class BuyDiscountReq {
        /**
         * 优惠金额，单位：分
         */
        private Integer amount;

        /**
         * 优惠券ID
         */
        private String couponId;

        /**
         * 优惠券编码
         */
        private String discountNo;

        /**
         * 优惠名称
         */
        private String discountName;

        /**
         * 金额类型[UnifiedOrderChangeTypeEnum]
         */
        private String changeType;

        /**
         * 备注
         */
        private String memo;

        /**
         * 费用分担 CHANNEL
         *
         */
        private String costAssume;
    }




    /**
     * 收件地址
     */
    @Data
    public static class BuyAddressReq {
        /**
         * 省
         */
        private String provinceId;

        /**
         * 市
         */
        private String cityId;

        /**
         * 区
         */
        private String countryId;

        /**
         * 详细地址
         */
        private String fullAddress;

        /**
         * 地址类型 ORDER
         */
        private String addressType;

        /**
         * 联系人
         */
        private String contactName;

        /**
         * 联系人手机
         */
        private String contactMobile;

        /**
         * 联系人手机 +86
         */
        private String contactArea;

        /**
         * 联系人手机区号, 1 女，2 男
         */
        private Integer contactGenders;
    }

}
