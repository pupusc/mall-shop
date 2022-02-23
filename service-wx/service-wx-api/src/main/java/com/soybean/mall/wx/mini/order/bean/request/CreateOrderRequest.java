package com.soybean.mall.wx.mini.order.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    @JsonProperty("order_detail")
    private OrderDetail orderDetail;
    @JsonProperty("delivery_detail")
    private DeliveryDetail deliveryDetail;
    @JsonProperty("address_info")
    private AddressInfo addressInfo;

    @Data
    public static class OrderDetail {
        @JsonProperty("product_infos")
        private List<ProductInfo> productInfos;
        @JsonProperty("pay_info")
        private PayInfo payInfo;
        @JsonProperty("price_info")
        private PriceInfo priceInfo;
        private AddressInfo addressInfo;
    }

    @Data
    public static class ProductInfo {
        @JsonProperty("out_product_id")
        private String outProductId;
        @JsonProperty("out_sku_id")
        private String outSkuId;
        @JsonProperty("product_cnt")
        private Integer prroductNum;
        @JsonProperty("sale_price")
        private BigDecimal salePrice;
        @JsonProperty("real_price")
        private BigDecimal realPrice;
        private String path;
        private String title;
        @JsonProperty("head_img")
        private String headImg;
    }

    @Data
    public static class PayInfo {
        @JsonProperty("pay_method_type")
        private Integer payMethodType;
        @JsonProperty("prepay_id")
        private String prepayId;
        @JsonProperty("prepay_time")
        private String prepayTime;
    }

    @Data
    public static class PriceInfo {
        @JsonProperty("order_price")
        private BigDecimal orderPrice;
        private BigDecimal freight;
        @JsonProperty("discounted_price")
        private BigDecimal discountePrice;
        @JsonProperty("additional_price")
        private BigDecimal additionalPrice;
        @JsonProperty("additional_remarks")
        private String additionalRemarks;

    }

    @Data
    public static class DeliveryDetail {
        @JsonProperty("delivery_type")
       private Integer deliveryType;
    }

    @Data
    public static class AddressInfo {
        @JsonProperty("receiver_name")
        private String receiverName;
        @JsonProperty("detailed_address")
        private String detailedAddress;
        @JsonProperty("tel_number")
        private BigDecimal telNumber;
        private String country;
        private String province;
        private String city;
        private String town;

    }
}
