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

    private String createTime;
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    private OrderDetailDTO orderDetail;
    private DeliveryDetailDTO deliveryDetail;
    private AddressInfoDTO addressInfo;


    @Data
    public static class DeliveryDetailDTO implements Serializable {
        private static final long serialVersionUID = -5887144793610324631L;
        private Integer deliveryType =1;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetailDTO implements Serializable {
        private static final long serialVersionUID = -2840521021603354162L;
        private List<WxProductInfoDTO> productInfos;
        private PayInfoDTO payInfo;
        private PriceInfoDTO priceInfo;
    }

    @Data
    public class AddressInfoDTO implements Serializable {
        private static final long serialVersionUID = 7884006462476933964L;
        private String receiverName;
        private String detailedAddress;
        private String telNumber;
        private String country;
        private String province;
        private String city;
        private String town;
    }


    @Data
    public class ProductInfoDTO implements Serializable {
        private static final long serialVersionUID = -8930731569741336371L;
        private String outProductId;
        private String outSkuId;
        private Long productNum;
        private Integer salePrice;
        private Integer realPrice;
        private String path;
        private String title;
        private String headImg;
    }

    @Data
    public class PayInfoDTO implements Serializable {
        private static final long serialVersionUID = 1227044415106015323L;
        private Integer payMethodType;
        private String prepayId;
        private String prepayTime;
    }

    @Data
    public class PriceInfoDTO implements Serializable {
        private static final long serialVersionUID = 1821182651497127881L;
        private Integer orderPrice;
        private Integer freight;
        private Integer discountePrice;
    }

}
