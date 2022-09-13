package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ImportMallRefundParamVO$Item {
    /**
     * 商城skuId需要转换成中台的子单id
     */
    @NotNull
    private String mallSkuId;
    /**
     * 子订单ID/主订单ID
     */
    private String objectId;
    /**
     * 对象类型
     */
    @NotNull
    private String objectType;
    /**
     * 售后类型：1退货退款 2仅退款 3换货 4补偿 5主订单退款 6赠品
     */
    @NotNull
    private Integer refundType;
    /**
     * 退款费用
     */
    @NotNull
    private Integer refundFee;
    /**
     * 退款数量
     */
    @NotNull
    private Integer refundNum;

    /**
     * 快递编码
     */
    private String expressCode;
    /**
     * 快递号
     */
    private String expressNo;
    /**
     * 物流状态
     */
    private Integer deliveryStatus;
    /**
     * 签收时间
     */
    private Date signatureTime;
    /**
     * 退款明细
     */
    private List<ImportMallRefundParamVO$Detail> saleAfterRefundDetailBOList;
}
