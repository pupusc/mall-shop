//package com.wanmi.sbc.erp.api.request;
//
//import com.sbc.wanmi.erp.bean.dto.ERPTradeInvoiceDTO;
//import com.sbc.wanmi.erp.bean.dto.ERPTradeItemDTO;
//import com.sbc.wanmi.erp.bean.dto.ERPTradePaymentDTO;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * @program: sbc-background
// * @description: ERP订单推送
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-30 09:40
// **/
//@ApiModel
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class PushTradeRequest implements Serializable {
//
//    /**
//     * 店铺编号
//     */
//    @ApiModelProperty(value = "店铺编号")
//    private String shopCode;
//
//    /**
//     * 会员代码
//     */
//    @ApiModelProperty(value = "会员代码")
//    private String vipCode;
//
//    /**
//     * 平台单号
//     */
//    @ApiModelProperty(value = "平台单号")
//    private String platformCode;
//
//    /**
//     * 仓库代码
//     */
//    @ApiModelProperty(value = "仓库代码")
//    private String warehouseCode;
//
//    /**
//     * 业务员
//     */
//    @ApiModelProperty(value = "业务员")
//    private String businessManCode;
//
//    /**
//     * 物流公司
//     */
//    @ApiModelProperty(value = "物流公司")
//    private String expressCode;
//
//    /**
//     * 物流费用
//     */
//    @ApiModelProperty(value = "物流费用")
//    private BigDecimal postFee;
//
//    /**
//     * 币别代码
//     */
//    @ApiModelProperty(value = "币别代码")
//    private String currencyCode;
//
//    /**
//     * 卖家备注
//     */
//    @ApiModelProperty(value = "卖家备注")
//    private String sellerMemo;
//
//    /**
//     * 是否货到付款
//     */
//    @ApiModelProperty(value = "是否货到付款")
//    private boolean cod;
//
//    /**
//     * 拍单时间
//     */
//    @ApiModelProperty(value = "拍单时间")
//    private String dealDatetime;
//
//    /**
//     * 订单类型(
//     * Sales-销售订单
//     * Return-换货订单
//     * Charge-费用订单
//     * Delivery-补发货订单
//     * Invoice-补发票订单
//     * )
//     */
//    @ApiModelProperty(value = "订单类型")
//    private String orderTypeCode;
//
//    /**
//     * 预计发货时间
//     */
//    @ApiModelProperty(value = "预计发货时间")
//    private String planDeliveryDate;
//
//    /**
//     * 买家到付服务
//     */
//    @ApiModelProperty(value = "买家到付服务")
//    private BigDecimal codFee;
//
//    /**
//     * 其他服务费用
//     */
//    @ApiModelProperty(value = "其他服务费用")
//    private  BigDecimal otherServiceFee;
//
//    /**
//     * 买家留言
//     */
//    @ApiModelProperty(value = "买家留言")
//    private String buyerMemo;
//
//    /**
//     * 二次备注
//     */
//    @ApiModelProperty(value = "二次备注")
//    private String sellerMemoLate;
//
//    /**
//     *附加信息
//     */
//    @ApiModelProperty(value = "附加信息")
//    private String extendMemo;
//
//    /**
//     * 是否淘宝家装订单
//     */
//    @ApiModelProperty(value = "是否淘宝家装订单")
//    private boolean jz;
//
//    /**
//     * 是否强制指定为分销商订单
//     */
//    @ApiModelProperty(value = "是否强制指定为分销商订单")
//    private boolean distributionOrder;
//
//    /**
//     *商品明细
//     */
//    @ApiModelProperty(value = "商品明细")
//    private List<ERPTradeItemDTO> details;
//
//
//    /**
//     * 支付明细
//     */
//    @ApiModelProperty(value = "支付明细")
//    private List<ERPTradePaymentDTO> payments;
//
//    /**
//     * 发票信息
//     */
//    @ApiModelProperty("invoices")
//    private List<ERPTradeInvoiceDTO> invoices;
//
//    /**
//     * 收货人
//     */
//    @ApiModelProperty(value = "收货人")
//    private String receiverName;
//
//    /**
//     *固定电话
//     */
//    @ApiModelProperty(value = "固定电话")
//    private String receiverPhone;
//
//    /**
//     * 手机号码
//     */
//    @ApiModelProperty(value = "手机号码")
//    private String receiverMobile;
//
//    /**
//     * 邮政编码
//     */
//    @ApiModelProperty(value = "邮政编码")
//    private String receiverZip;
//
//    /**
//     * 省名称
//     */
//    @ApiModelProperty(value = "省名称")
//    private String receiverProvince;
//
//    /**
//     * 市名称
//     */
//    @ApiModelProperty(value = "市名称")
//    private String receiverCity;
//
//    /**
//     * 区名称
//     */
//    @ApiModelProperty(value = "区名称")
//    private String receiverDistrict;
//
//    /**
//     *收货地址
//     */
//    @ApiModelProperty(value = "收货地址")
//    private String receiverAddress;
//
//    /**
//     * 真实姓名
//     */
//    @ApiModelProperty(value = "真实姓名")
//    private String vipRealName;
//
//    /**
//     * 身份证号
//     */
//    @ApiModelProperty(value = "身份证号")
//    private String vipIdCard;
//
//    /**
//     * 电子邮箱
//     */
//    @ApiModelProperty(value = "电子邮箱")
//    private String vipEmail;
//
//    /**
//     * 订单标记
//     */
//    @ApiModelProperty(value = "订单标记")
//    private String tagCode;
//}
