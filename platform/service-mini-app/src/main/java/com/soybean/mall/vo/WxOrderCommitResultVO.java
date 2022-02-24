package com.soybean.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WxOrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 556503414689064030L;

    private String createTime;
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    private OrderDetailVO orderDetail;
    private WxAddressInfoVO addressInfo;

    @Data
    public static class OrderDetailVO {
        private List<WxProductInfoVO> productInfos;
        private PriceInfoVO priceInfo;
        private WxAddressInfoVO addressInfo;
    }



    @Data
    public static class PriceInfoVO {
        private BigDecimal orderPrice;
        private BigDecimal freight;
        private BigDecimal discountePrice;
        private BigDecimal additionalPrice;
        private String additionalRemarks;

    }



}
