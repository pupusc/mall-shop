//package com.wanmi.sbc.erp.api.request;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.sbc.wanmi.erp.bean.vo.ERPTradePaymentVO;
//import com.sbc.wanmi.erp.bean.vo.ReturnTradeItemVO;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * @program: sbc-background
// * @description: 创建退货单接口
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-07 16:39
// **/
//@ApiModel
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class ReturnTradeCreateRequst implements Serializable {
//
//    /**
//     * 店铺代码
//     */
//    @ApiModelProperty(value = "店铺代码")
//    private String shopCode;
//
//    /**
//     * 购买人手机号-默认采用用户手机
//     */
//    @ApiModelProperty(value = "购买人手机号")
//    private String buyerMobile;
//
//    /**
//     * 订单号
//     */
//    @ApiModelProperty(value = "订单号")
//    private String tradeNo;
//
//    /**
//     * 售后阶段(
//     * 1：退货
//     * 2：换货
//     * 3：补发
//     * 4：其他
//     * 5：维护
//     * 未提供时默认”退货”)
//     */
//    @ApiModelProperty(value = "退单类型")
//    private int returnType;
//
//    /**
//     * 退货原因代码
//     */
//    @ApiModelProperty(value = "退货原因代码")
//    private String typeCode;
//
//    /**
//     * 退货商品明细
//     */
//    @ApiModelProperty(value = "退货商品明细")
//    private List<ReturnTradeItemVO> tradeItems;
//
//    /**
//     * 支付明细
//     */
//    @ApiModelProperty(value = "退货商品明细")
//    private List<ERPTradePaymentVO> refundDetail;
//
//
//    /**
//     * 收货人
//     */
//    @ApiModelProperty(value = "收货人")
//    private String receiveName;
//
//    /**
//     * 收货人电话
//     */
//    @ApiModelProperty(value = "收货人电话")
//    private String receiverPhone;
//
//    /**
//     * 收货人手机
//     */
//    @ApiModelProperty(value = "收货人手机")
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
//     * 退回运单号
//     */
//    @ApiModelProperty(value = "退回运单号")
//    private String expressNum;
//
//    /**
//     * 退回快递名称
//     */
//    @ApiModelProperty(value = "退回快递名称")
//    private String expressName;
//
//
//
//}
