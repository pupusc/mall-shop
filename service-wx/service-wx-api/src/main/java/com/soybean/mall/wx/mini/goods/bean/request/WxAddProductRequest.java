package com.soybean.mall.wx.mini.goods.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WxAddProductRequest {

    @JsonProperty("out_product_id")
    private String outProductId;
    private String title;
    private String path;
    @JsonProperty("head_img")
    private List<String> headImg;
    @JsonProperty("qualification_pics")
    private List<String> qualificationics;
    @JsonProperty("desc_info")
    private DescInfo descInfo;
    @JsonProperty("third_cat_id")
    private Integer thirdCatId;
    @JsonProperty("brand_id")
    private Integer brandId;
    @JsonProperty("info_version")
    private String infoVersion;
    private List<Sku> skus;

    @Data
    @Builder
    public static class DescInfo{
        private String desc;
        private List<String> imgs;
    }

    @Data
    public static class Sku{

        @JsonProperty("out_product_id")
        private String outProductId;
        @JsonProperty("out_sku_id")
        private String outSkuId;
        @JsonProperty("thumb_img")
        private String thumbImg;
        @JsonProperty("sale_price")
        private BigDecimal salePrice;
        @JsonProperty("market_price")
        private BigDecimal marketPrice;
        @JsonProperty("stock_num")
        private Integer stockNum;
        @JsonProperty("sku_code")
        private String skuCode;
        @JsonProperty("barcode")
        private String barcode;
        @JsonProperty("sku_attrs")
        private List<skuAttrs> skuAttrs;
    }

    @Data
    public static class skuAttrs{

        public skuAttrs(String attrKey, String attrValue){
            this.attrKey = attrKey;
            this.attrValue = attrValue;
        }

        @JsonProperty("sku_attrs")
        private String attrKey;
        @JsonProperty("attr_value")
        private String attrValue;
    }

}
