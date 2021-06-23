package com.wanmi.sbc.erp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: sbc-background
 * @description: 管易云ERP订单对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 09:57
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ERPTrade implements Serializable {

    /**
     * 店铺编号
     */
    @JsonProperty("shop_code")
    private String shopCode;

    /**
     * 会员代码
     */
    @JsonProperty("vip_code")
    private String vipCode;

    /**
     * 平台单号
     */
    @JsonProperty("platform_code")
    private String platformCode;

    /**
     * 仓库代码
     */
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    /**
     * 业务员
     */
    @JsonProperty("business_man_code")
    private String businessManCode;

    /**
     * 物流公司
     */
    @JsonProperty("express_code")
    private String expressCode;

    /**
     * 物流费用
     */
    @JsonProperty("post_fee")
    private BigDecimal postFee;

    /**
     * 币别代码
     */
    @JsonProperty("currency_code")
    private String currencyCode;

    /**
     * 卖家备注
     */
    @JsonProperty("seller_memo")
    private String sellerMemo;

    /**
     * 是否货到付款
     */
    @JsonProperty("cod")
    private boolean cod;

    /**
     * 拍单时间
     */
    @JsonProperty("deal_datetime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime dealDatetime;

    /**
     * 订单类型(
     * Sales-销售订单
     * Return-换货订单
     * Charge-费用订单
     * Delivery-补发货订单
     * Invoice-补发票订单
     * )
     */
    @JsonProperty("order_type_code")
    private String orderTypeCode;

    /**
     * 预计发货时间
     */
    @JsonProperty("plan_delivery_date")
    private String planDeliveryDate;

    /**
     * 买家到付服务
     */
    @JsonProperty("cod_fee")
    private BigDecimal codFee;

    /**
     * 其他服务费用
     */
    @JsonProperty("other_service_fee")
    private  BigDecimal otherServiceFee;

    /**
     * 买家留言
     */
    @JsonProperty("buyer_memo")
    private String buyerMemo;

    /**
     * 二次备注
     */
    @JsonProperty("seller_memo_late")
    private String sellerMemoLate;

    /**
     *附加信息
     */
    @JsonProperty("extend_memo")
    private String extendMemo;

    /**
     * 是否淘宝家装订单
     */
    @JsonProperty("jz")
    private boolean jz;

    /**
     * 是否强制指定为分销商订单
     */
    @JsonProperty("distribution_order")
    private boolean distributionOrder;

    /**
     *商品明细
     */
    @JsonProperty("details")
    private List<ERPTradeItem> details;


    /**
     * 支付明细
     */
    @JsonProperty("payments")
    private List<ERPTradePayment> payments;

    /**
     * 发票信息
     */
    @JsonProperty("invoices")
    private List<ERPTradeInvoice> invoices;

    /**
     * 收货人
     */
    @JsonProperty("receive_name")
    private String receiveName;

    /**
     *固定电话
     */
    @JsonProperty("receiver_phone")
    private String receiverPhone;

    /**
     * 手机号码
     */
    @JsonProperty("receiver_mobile")
    private String receiverMobile;

    /**
     * 邮政编码
     */
    @JsonProperty("receiver_zip")
    private String receiverZip;

    /**
     * 省名称
     */
    @JsonProperty("receiver_zip")
    private String receiverProvince;

    /**
     * 市名称
     */
    @JsonProperty("receiver_city")
    private String receiverCity;

    /**
     * 区名称
     */
    @JsonProperty("receiver_district")
    private String receiverDistrict;

    /**
     *收货地址
     */
    @JsonProperty("receiver_address")
    private String receiverAddress;

    /**
     * 真实姓名
     */
    @JsonProperty("vipRealName")
    private String vipRealName;

    /**
     * 身份证号
     */
    @JsonProperty("vipIdCard")
    private String vipIdCard;

    /**
     * 电子邮箱
     */
    @JsonProperty("vipEmail")
    private String vipEmail;

    /**
     * 订单标记
     */
    @JsonProperty("tag_code")
    private String tagCode;

}
