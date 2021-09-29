package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProviderTradeStatusSyncRequest {
    @ApiModelProperty("第三方订单号")
    private String orderId;
    @ApiModelProperty("发货单号")
    private String platformCode;
    @ApiModelProperty("快递公司")
    private String post;
    @ApiModelProperty("快递单号")
    private String postNumber;
    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postDate;
    private Integer orderStatus;
    @ApiModelProperty("下单状态0成功1失败")
    private Integer status;
    @ApiModelProperty("下单失败描述")
    private String statusDesc;

    private List<GoodsItemDTO> goodsList;

    @Data
    public static  class GoodsItemDTO implements Serializable {
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
