package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class WxVideoOrderDetailResponse extends WxResponseBase {

    @JSONField(name = "order")
    private WxVideoOrderResponse order;

    @Data
    public static class WxVideoOrderResponse {

        @JSONField(name = "order_id")
        private String orderId;

        @JSONField(name = "out_order_id")
        private String outOrderId;

        @JSONField(name = "status")
        private Integer status;

        @JSONField(name = "path")
        private String path;


    }
}