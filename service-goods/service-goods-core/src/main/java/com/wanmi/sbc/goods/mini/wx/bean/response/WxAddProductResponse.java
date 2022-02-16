package com.wanmi.sbc.goods.mini.wx.bean.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WxAddProductResponse extends WxResponseBase {

    private ResponseData data;

    @Data
    static class ResponseData{

        @JsonProperty("product_id")
        private Long productId;
        @JsonProperty("out_product_id")
        private String outProductId;
        @JsonProperty("create_time")
        private String createTime;
        @JsonProperty("skus")
        private List<Sku> skus;
    }

    @Data
    static class Sku{
        @JsonProperty("sku_id")
        private String skuId;
        @JsonProperty("out_sku_id")
        private String outSkuId;
    }
}
