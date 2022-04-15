package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.erp.entity.ERPReturnTradeItem;
import com.wanmi.sbc.erp.entity.ERPTradePayment;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @program: sbc-background
 * @description: ERP创建退货单接口请求参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 11:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPReturnTradeCreateRequest extends ERPBaseRequest{

    /**
     * 店铺代码
     */
    @JsonProperty("shop_code")
    private String shopCode;

    /**
     * 店铺代码
     */
    @JsonProperty("vip_code")
    private String vipCode;

    /**
     * 销售订单单据编号
     */
    @JsonProperty("trade_code")
    private String tradeCode;

    /**
     * 销售订单平台单号
     */
    @JsonProperty("trade_platform_code")
    private String tradePlatformCode;

    /**
     * 售后阶段(1：买家确认收货前
     * 2：买家确认收货后)
     */
    @JsonProperty("refund_phase")
    private int refundPhase;

    /**
     * 售后阶段(
     * 1：退货
     * 2：换货
     * 3：补发
     * 4：其他
     * 5：维护
     * 未提供时默认”退货”)
     */
    @JsonProperty("return_type")
    private int returnType;

    /**
     * 退货原因代码
     */
    @JsonProperty("type_code")
    private String typeCode;

    /**
     * 退回仓库代码
     */
    @JsonProperty("warehousein_code")
    private String warehouseinCode;

    /**
     * 退入商品明细
     */
    @JsonProperty("item_detail")
    private List<ERPReturnTradeItem> itemDetail;

    /**
     * 支付明细
     */
    @JsonProperty("refund_detail")
    private List<ERPTradePayment> refundDetail;


    /**
     * 收货人
     */
    @JsonProperty("receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @JsonProperty("receiver_phone")
    private String receiverPhone;

    /**
     * 收货人手机
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
    @JsonProperty("receiver_province")
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
     * 退回运单号
     */
    @JsonProperty("express_num")
    private String expressNum;

    /**
     * 退回快递名称
     */
    @JsonProperty("express_name")
    private String expressName;

    /**
     * 备注
     */
    @JsonProperty("note")
    private String note;
}
