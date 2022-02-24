package com.soybean.mall.order.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.order.bean.dto.ProductInfoDTO;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCommitResult implements Serializable {
    private static final long serialVersionUID = -9056625560291022361L;
    /**
     * 订单编号
     */
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentTid;

    @JSONField(name ="order_detail")
    private OrderDetail orderDetail;
    @JSONField(name ="address_info")
    private AddressInfo addressInfo;

    @Data
    public static class OrderDetail {
        private List<ProductInfoDTO> productInfos;
        private PriceInfo priceInfo;
        private AddressInfo addressInfo;
    }



    @Data
    public static class PriceInfo {
        /**
         * 订单最终金额
         */
        private BigDecimal orderPrice;
        /**
         * 运费，单位分
         */
        private BigDecimal freight;
        /**
         * 优惠金额，单位分
         */
        private BigDecimal discountPrice;
    }


    @Data
    public static class AddressInfo {
        private String receiverName;
        private String detailedAddress;
        private String telNumber;
        private String country;
        private String province;
        private String city;
        private String town;

    }

}
