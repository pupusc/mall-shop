package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProviderTradeDeliveryStatusSyncRequest implements Serializable {
    private static final long serialVersionUID = 5487818147209551733L;
    @ApiModelProperty("平台单号")
    private String platformCode;

    @ApiModelProperty("发货状态")
    private Integer deliveryStatus;

    @ApiModelProperty("已发货数据")
    private List<DeliveryInfoDTO> deliveryInfoList;

    @ApiModelProperty("取消商品信息")
    private List<GoodsItemDTO> cancelGoods;

    @ApiModelProperty("分仓标记0未分仓1分仓")
    private Integer packing = 0;

    @Data
    public static class DeliveryInfoDTO implements Serializable {

        private static final long serialVersionUID = -3833840457197522090L;
        /**
         * 发货单编号
         */
        @ApiModelProperty(value = "发货单编号")
        private String code;

        /**
         * 订单商品发货状态(
         * 0:未发货
         * 1:已发货
         * 2:部分发货)
         */
        @ApiModelProperty(value = "订单商品发货状态")
        private Integer deliveryStatus;

        /**
         * 发货时间
         */
        @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime deliverTime;


        /**
         * 发货单商品集合
         */
        @ApiModelProperty(value = "发货单商品集合")
        private List<GoodsItemDTO> goodsList;


        /**
         * 快递公司代码
         */
        @ApiModelProperty(value = "快递公司代码")
        private String expressCode;

        /**
         * 快递公司名称
         */
        @ApiModelProperty(value = "快递公司名称")
        private String expressName;

        /**
         * 快递单号
         */
        @ApiModelProperty(value = "快递单号")
        private String expressNo;

        /**
         * 子订单号
         */
        @ApiModelProperty(value = "子订单号")
        private String oid;

        /**
         * 平台单号
         */
        @ApiModelProperty(value = "平台单号")
        private String platformCode;
    }

    @Data
    public static class GoodsItemDTO implements Serializable {
        @ApiModelProperty(name = "BookID")
        private String bookId;
        @ApiModelProperty(name = "BookNum")
        private Integer bookNum;
        @ApiModelProperty(name = "SourceSpbs")
        private String sourceSpbs;
        @ApiModelProperty(name = "Status")
        private Integer status;
        @ApiModelProperty(name = "Report")
        private String report;
        @ApiModelProperty(name = "BookSendNum")
        private Integer bookSendNum;
        @ApiModelProperty(name = "FixedPrice")
        private BigDecimal fixedPrice;
        @ApiModelProperty(name = "Price")
        private BigDecimal price;
        @ApiModelProperty(name = "CancelReason")
        private String cancelReason;
    }


}
