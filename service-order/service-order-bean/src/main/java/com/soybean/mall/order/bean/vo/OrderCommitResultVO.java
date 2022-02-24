package com.soybean.mall.order.bean.vo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 3015950032444463773L;

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 父订单号，用于不同商家订单合并支付场景
     */
    private String parentTid;

    private OrderDetail orderDetail;
    
    private AddressInfoVO addressInfo;

    @Data
    public static class OrderDetail {
        private List<ProductInfoVO> productInfos;
        private PriceInfo priceInfo;
        private AddressInfoVO addressInfo;
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
    
}
