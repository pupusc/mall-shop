package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class WxCreateOrderResponse extends WxResponseBase {

    private ResponseData data;

    @Data
    static class ResponseData{
        @JSONField(name ="order_id")
        private Long orderId;
        @JSONField(name ="out_order_id")
        private String outOrderId;
        private String ticket;
        @JSONField(name ="ticket_expire_time")
        private String ticketExpireTime;
        @JSONField(name ="final_price")
        private BigDecimal finalPrice;
    }

}