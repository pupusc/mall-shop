package com.soybean.mall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WxOrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 556503414689064030L;


    @JSONField(name ="create_time")
    private String createTime;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
    @JSONField(name ="order_detail")
    private WxOrderDetail orderDetail;
    @JSONField(name ="delivery_detail")
    private DeliveryDetail deliveryDetail;
    @JSONField(name ="address_info")
    private WxAddressInfoVO addressInfo;


    @Data
    public static class DeliveryDetail {
        @JSONField(name ="delivery_type")
        private Integer deliveryType =1;
    }

    @Data
    public static class WxOrderDetail implements Serializable {
        @JSONField(name ="product_infos")
        private List<WxProductInfoVO> productInfos;
        @JSONField(name ="pay_info")
        private WxPayInfo payInfo;
        @JSONField(name ="price_info")
        private WxPriceInfo priceInfo;
    }

    @Data
    public static class WxPriceInfo implements Serializable {
        private static final long serialVersionUID = 1334358700142612459L;
        @JSONField(name ="order_price")
        private Integer orderPrice;
        private Integer freight;
        @JSONField(name ="discounted_price")
        private Integer discountePrice;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WxPayInfo implements Serializable {
        private static final long serialVersionUID = 7109742866634829932L;
        @JSONField(name ="pay_method_type")
        private Integer payMethodType;
        @JSONField(name ="prepay_id")
        private String prepayId;
        @JSONField(name ="prepay_time")
        private String prepayTime;
    }


}
