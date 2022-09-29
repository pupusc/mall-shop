package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterItemResp implements Serializable {

    private static final long serialVersionUID = 1423926482809737355L;
    /**
     * 关联子订单
     */
    private OrderItemRsp ordItemBO;
    /**
     * 售后子单号
     */
    private Long saItemId;

    /**
     * 售后类型
     */
    private Integer refundType;

    /**
     * 退货数量
     */
    private Integer refundNum;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款金额
     */
    private Integer refundFee;
    /**
     * 退款运费
     */
    private Integer refundPostFee;
    /**
     * 快递编码
     */
    private String expressCode;
    /**
     * 快递号
     */
    private String expressNo;

    /**
     * 补偿ID
     */
    private Long compensateId;
    /**
     * 补偿类型
     */
    private String compensateType;

    /**
     * 补偿数量
     */
    private Integer compensateNum;

    /**
     * 关联退款明细
     */
    private List<SaleAfterRefundDetailResp> detailResponseBOList;

    /**
     * 补偿关联子订单
     */
    private List<OrderItemRsp> compensateOrdItemBO;
    /**
     * 换货订单
     */
    private List<OrderItemRsp> replaceOrdItemBOs;

}
