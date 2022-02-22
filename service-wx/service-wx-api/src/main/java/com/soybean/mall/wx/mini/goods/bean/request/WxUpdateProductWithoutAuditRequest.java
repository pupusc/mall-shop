package com.soybean.mall.wx.mini.goods.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WxUpdateProductWithoutAuditRequest extends WxResponseBase {

    @JsonProperty("out_product_id")
    private String outProductId;
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("skus")
    private List<Sku> skus;

    @Data
    public static class Sku{

        @JsonProperty("out_sku_id")
        private String outSkuId;
        @JsonProperty("sale_price")
        private BigDecimal salePrice;
        @JsonProperty("market_price")
        private BigDecimal marketPrice;
        @JsonProperty("stock_num")
        private Integer stockNum;
    }
}
