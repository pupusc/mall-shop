package com.soybean.mall.wx.mini.order.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class WxCreateOrderResponse extends WxResponseBase {

    private ResponseData data;

    @Data
    static class ResponseData{
        @JsonProperty("order_id")
        private Long orderId;
        @JsonProperty("out_order_id")
        private String outOrderId;
        private String ticket;
        @JsonProperty("ticket_expire_time")
        private String ticketExpireTime;
        @JsonProperty("final_price")
        private BigDecimal finalPrice;
    }

}