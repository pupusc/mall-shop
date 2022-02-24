package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WxCreateOrderRequest implements Serializable {
    private static final long serialVersionUID = 5403388427341418460L;
    @JSONField(name ="create_time")
    private String createTime;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    @JSONField(name ="order_detail")
    private OrderDetail orderDetail;
    @JSONField(name ="delivery_detail")
    private DeliveryDetail deliveryDetail;
    @JSONField(name ="address_info")
    private AddressInfo addressInfo;

    @Data
    public static class OrderDetail {
        @JSONField(name ="product_infos")
        private List<ProductInfo> productInfos;
        @JSONField(name ="pay_info")
        private PayInfo payInfo;
        @JSONField(name ="price_info")
        private PriceInfo priceInfo;
        private AddressInfo addressInfo;
    }

    @Data
    public static class ProductInfo {
        @JSONField(name ="out_product_id")
        private String outProductId;
        @JSONField(name ="out_sku_id")
        private String outSkuId;
        @JSONField(name ="product_cnt")
        private Integer prroductNum;
        @JSONField(name ="sale_price")
        private BigDecimal salePrice;
        @JSONField(name ="real_price")
        private BigDecimal realPrice;
        private String path;
        private String title;
        @JSONField(name ="head_img")
        private String headImg;
    }

    @Data
    public static class PayInfo {
        @JSONField(name ="pay_method_type")
        private Integer payMethodType;
        @JSONField(name ="prepay_id")
        private String prepayId;
        @JSONField(name ="prepay_time")
        private String prepayTime;
    }

    @Data
    public static class PriceInfo {
        @JSONField(name ="order_price")
        private BigDecimal orderPrice;
        private BigDecimal freight;
        @JSONField(name ="discounted_price")
        private BigDecimal discountePrice;
        @JSONField(name ="additional_price")
        private BigDecimal additionalPrice;
        @JSONField(name ="additional_remarks")
        private String additionalRemarks;

    }

    @Data
    public static class DeliveryDetail {
        @JSONField(name ="delivery_type")
       private Integer deliveryType;
    }

    @Data
    public static class AddressInfo {
        @JSONField(name ="receiver_name")
        private String receiverName;
        @JSONField(name ="detailed_address")
        private String detailedAddress;
        @JSONField(name ="tel_number")
        private BigDecimal telNumber;
        private String country;
        private String province;
        private String city;
        private String town;

    }
}
