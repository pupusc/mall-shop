package com.wanmi.sbc.erp.api.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/21 10:42 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SaleAfterCreateNewReq {


    private String orderNumber;

    /**
     * 售后类型：1退货退款 2仅退款 3换货 4补偿 5主订单仅退款 6赠品
     */
    @NotEmpty
    private List<Integer> refundTypeList;


    /**
     * 售后主单
     */
    private SaleAfterOrderReq saleAfterOrderBO;

    /**
     * 快递费
     */
    private SaleAfterPostFeeReq saleAfterPostFee;


    /**
     * 售后子单
     */
    private List<SaleAfterItemReq> saleAfterItemBOList;

    /**
     * 退款主单
     */
    private List<SaleAfterRefundReq> saleAfterRefundBOList;


    private Integer saleAfterCreateEnum = 2;

    /**
     * 售后主单
     */
    @Data
    public static class SaleAfterOrderReq {
        /**
         * 平台退款ID [商城售后id]
         */
        private String platformRefundId;


        /**
         * 执行时间 【售后创建时间】
         */
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime applyTime;

        /**
         * 关闭时间 【售后完成时间】
         */
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime closeTime;


        /**
         * 备注 【退款说明】
         */
        private String memo;
    }

    /**
     * 快递费
     */
    @Data
    public static class SaleAfterPostFeeReq {
        /**
         * 费用信息
         */
        private List<SaleAfterRefundDetailReq> saleAfterRefundDetailBOList;
    }
    /**
     * 支付信息
     */
    @Data
    public static class SaleAfterRefundDetailReq {

        /**
         * 支付类型 {@link PaymentPayTypeEnum}
         */
        private Integer payType;

        /**
         * 退款金额
         */
        private Integer amount;

        /**
         * 退款理由
         */
        private String refundReason;
    }

    @Data
    public static class SaleAfterItemReq {
        /**
         * 售后类型：1退货退款 2仅退款 3换货 4补偿 5赠品
         */
        private Integer refundType;

        /**
         * 快递编码
         */
        private String expressCode;

        /**
         * 快递号
         */
        private String expressNo;

        /**
         * 退货数量
         */
        private Integer refundNum;

        /**
         * 子订单ID/主订单ID [如果主单退的时候【4补偿 5主订单仅退款 6赠品 主订单】，放主订单id，子订单退的时候，放自订单id，]
         */
        private Long objectId;

        /**
         * 对象类型  ORDER("ORD_ORDER", "订单"), ORDER_ITEM("ORD_ITEM", "订单"),
         */
        private String objectType;

        /**
         * 物流状态 【待定】
         */
        private Integer deliveryStatus;

        /**
         * 签收时间 【待定】
         */
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime signatureTime;

        /**
         * 退款明细
         */
        private List<SaleAfterRefundDetailReq> saleAfterRefundDetailBOList;
    }

    /**
     * 退款主单
     */
    @Data
    public static class SaleAfterRefundReq {
        /**
         * 退款单号 【待定 不传递试试】
         */
        private String refundNo;

        /**
         * 退款流水号
         */
        private String refundTradeNo;

        /**
         * 退款网关
         */
        private String refundGateway;

        /**
         * 退款金额
         */
        private Integer amount;

        /**
         * 支付类型
         */
        private String payType;

        /**
         * 退款时间
         */
        private LocalDateTime refundTime;

        /**
         * 退款商户号
         */
        private String refundMchid;
    }
}
