package com.soybean.mall.order.api.request.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateWxOrderAndPayRequest implements Serializable {
    private static final long serialVersionUID = 1071264710390642647L;
    @JSONField(name ="create_time")
    private String createTime;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    @JSONField(name ="order_detail")
    private OrderDetailDTO orderDetail;
    @JSONField(name ="delivery_detail")
    private DeliveryDetailDTO deliveryDetail;
    @JSONField(name ="address_info")
    private AddressInfoDTO addressInfo;


    @Data
    public static class DeliveryDetailDTO implements Serializable {
        private static final long serialVersionUID = -5887144793610324631L;
        @JSONField(name ="delivery_type")
        private Integer deliveryType =1;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetailDTO implements Serializable {
        private static final long serialVersionUID = -2840521021603354162L;
        @JSONField(name ="product_infos")
        private List<ProductInfoDTO> productInfos;
        @JSONField(name ="pay_info")
        private PayInfoDTO payInfo;
        @JSONField(name ="price_info")
        private PriceInfoDTO priceInfo;
    }

    @Data
    public static class AddressInfoDTO implements Serializable {
        private static final long serialVersionUID = 7884006462476933964L;
        @JSONField(name ="receiver_name")
        private String receiverName;
        @JSONField(name ="detailed_address")
        private String detailedAddress;
        @JSONField(name ="tel_number")
        private String telNumber;
        private String country;
        private String province;
        private String city;
        private String town;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfoDTO implements Serializable {
        private static final long serialVersionUID = -8930731569741336371L;
        @JSONField(name ="out_product_id")
        private String outProductId;
        @JSONField(name ="out_sku_id")
        private String outSkuId;
        @JSONField(name ="product_cnt")
        private Long productNum;
        @JSONField(name ="sale_price")
        private Integer salePrice;
        @JSONField(name ="real_price")
        private Integer realPrice;
        private String path;
        private String title;
        @JSONField(name ="head_img")
        private String headImg;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class PayInfoDTO implements Serializable {
        private static final long serialVersionUID = 1227044415106015323L;
        @JSONField(name ="pay_method_type")
        private Integer payMethodType;
        @JSONField(name ="prepay_id")
        private String prepayId;
        @JSONField(name ="prepay_time")
        private String prepayTime;
    }

    @Data
    public class PriceInfoDTO implements Serializable {
        private static final long serialVersionUID = 1821182651497127881L;
        @JSONField(name ="order_price")
        private Integer orderPrice;
        private Integer freight;
        @JSONField(name ="discounted_price")
        private Integer discountePrice;
    }

}
