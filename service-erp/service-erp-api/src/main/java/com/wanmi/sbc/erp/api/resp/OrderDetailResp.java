package com.wanmi.sbc.erp.api.resp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/11 2:18 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderDetailResp {

//    /**
//     * 订单ID
//     */
//    private Long tid;

    /**
     * 订单状态
     *
     * @see com.soybean.unified.order.api.enums.UnifiedOrderStatusEnum
     */
    private Integer status;

//    /**
//     * 售后单个数
//     */
//    private Integer saleAfterCount;

//    /**
//     * 销售渠道ID
//     */
//    private Integer saleChannelId;
//
//    /**
//     * 快照销售渠道ID
//     */
//    private Integer fkSaleChannelId;
//
//    /**
//     * 渠道订单号
//     */
//    private String platformOrderId;

    /**
     * 订单号
     */
    private Long orderNumber;

//    /**
//     * 子订单号
//     */
//    private String orderItemTid;

    /**
     * 订单金额
     */
    private Integer totalFee;

//    /**
//     * 推广人类型
//     *
//     * @see com.soybean.unified.order.api.enums.UnifiedOrderPromoTypeEnum
//     */
//    private Integer promoType;

//    /**
//     * 推广人ID
//     */
//    private Integer promoUserId;

    /**
     * 下单人ID
     */
    private Integer userId;

    /**
     * 下单人手机号
     */
    private String mobile;

    /**
     * 商品总金额，单位：分
     */
    private Integer goodsAmount;

    /**
     * 渠道下单-时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookTime;

    /**
     * 优惠总金额
     */
    private Integer discountFee;

    /**
     * 运费
     */
    private Integer postFee;

//    /**
//     * 运费成本-粗算 待确定
//     */
//    private Integer postFeeEstimate;

//    /**
//     * 运费成本-实际
//     */
//    private Integer futurePostFee;

    /**
     * 支付时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime payTime;

    /**
     * 支付状态：0：未支付，1：已支付，2：部分支付
     */
    private Integer paymentStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 取消/关闭时间
     */
    private Date cancelTime;

//    /**
//     * 错误原因
//     */
//    private String errorReason;

    /**
     * 应付金额
     */
    private Integer oughtFee;

    /**
     * 交易完成时间
     */
    private Date completeTime;

    /**
     * 积分支付金额
     */
    private Integer jiFenAmount;

    /**
     * 知豆支付金额
     */
    private Integer zhiDouAmount;

    /**
     * 实付金额
     */
    private Integer actualFee;

    /**
     * 已退金额
     */
    private Integer refundFee;

    /**
     * 买家留言
     */
    private String buyerMemo;

    /**
     * 卖家留言
     */
    private String sellerMemo;

    /**
     * 商户ID
     */
    private String shopId;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 子订单
     */
    private List<OrderItemReq> orderItemBOS;

    /**
     * 包信息
     */
    private List<OrderPackReq> orderPackBOS;

    /**
     * 收件信息
     */
    private OrderAddressReq orderAddressBO;

    /**
     * 优惠成本承担-渠道平台后返
     */
    private Integer costDiscountFee;


    /**
     * 子单信息
     */
    @Data
    public static class OrderItemReq {
        /**
         * 子订单号
         */
        private Long tid;

        /**
         * 平台子单ID
         */
        private String  platformItemId;

        /**
         * 价格
         */
        private Integer price;

        /**
         * 仓库ID
         */
        private Long whId;

        /**
         * 仓库编码
         */
        private String whCode;


        /**
         * 业务类型 1:实物商品 2：虚拟商品-樊登，3：图书订阅商品 4-历史虚拟
         */
        private Integer metaGoodsType;

        /**
         * 成本价
         */
        private Integer costPrice;

        /**
         * 成本价
         */
        private Integer marketPrice;

        /**
         * 购买件数
         */
        private Integer num;

        /**
         * 物流编码
         */
        private String expressCode;

        /**
         * 物流单号
         */
        private String expressNo;

        /**
         * 签收时间
         */
        private Date signatureTime;

        /**
         * 优惠金额
         */
        private Integer discountFee;

        /**
         * 实付金额
         */
        private Integer oughtFee;

        /**
         * 商品包Id，可用于判断是否为包商品
         */
        private Long orderPackId;

        /**
         * 是否为赠品，0非赠送，1赠送，3补偿
         */
        private Integer giftFlag;

        /**
         * 销售码
         */
        private String saleCode;

        /**
         * 第三方平台商品ID
         */
        private String platformGoodsId;

        /**
         * 第三方平台规格ID
         */
        private String platformSkuId;

        /**
         * 第三方平台商品名称
         */
        private String platformGoodsName;

        /**
         * 第三方平台规格名称
         */
        private String platformSkuName;

        /**
         * 状态
         *
         * @see com.soybean.unified.order.api.enums.UnifiedOrderItemStatusEnum
         */
        private Integer status;

        /**
         * 货品ID
         */
        private Long metaGoodsId;

        /**
         * 货品规格ID
         */
        private Long metaSkuId;

        /**
         * 商品ID
         */
        private Integer goodsId;

        /**
         * 产品ID
         */
        private Long productId;

        /**
         * 会期类型 0 体验，1 正式
         */
        private Integer rightsCategory;
        /**
         * 退货数量
         */
        private Integer refundNum;

        /**
         * 退款金额
         */
        private Integer refundFee;


        /**
         *库存类型 1-真实库存 2-预售库存
         */
        private Integer stockType;

        /**
         * 发货时间
         */
        private Date deliveryTime;

        /**
         * 计划发货时间
         */
        private Date planDeliveryTime;
    }



    /**
     * 打包对象
     */
    @Data
    public static class OrderPackReq {

        private String saleCode;

        private Integer num;

        /**
         * 第三方平台商品ID
         */
        private String platformGoodsId;

        /**
         * 第三方平台规格ID
         */
        private String platformSkuId;

        /**
         * 第三方平台商品名称
         */
        private String platformGoodsName;

        /**
         * 第三方平台规格名称
         */
        private String platformSkuName;

        private Long tid;
    }


    /**
     * 订单地址信息
     */
    @Data
    public static class OrderAddressReq {
        private Long tid;
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
         * 地址类型
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
         * 联系人手机
         */
        private String contactArea;

        /**
         * 联系人手机区号, 1 女，2 男
         */
        private Integer contactGenders;
    }
}
