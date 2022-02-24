package com.soybean.mall.wx.mini.goods.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WxAddProductRequest {

    @JSONField(name = "out_product_id")
    private String outProductId;
    private String title;
    private String path;
    @JSONField(name = "head_img")
    private List<String> headImg;
    @JSONField(name = "qualification_pics")
    private List<String> qualificationics;
    @JSONField(name = "desc_info")
    private DescInfo descInfo;
    @JSONField(name = "third_cat_id")
    private Integer thirdCatId;
    @JSONField(name = "brand_id")
    private Integer brandId;
    @JSONField(name = "info_version")
    private String infoVersion;
    private List<Sku> skus;

    @Data
    public static class DescInfo{

        public DescInfo(){}

        public DescInfo(String desc, List<String> imgs) {
            this.desc = desc;
            this.imgs = imgs;
        }

        private String desc;
        private List<String> imgs;
    }

    @Data
    public static class Sku{

        @JSONField(name = "out_product_id")
        private String outProductId;
        @JSONField(name = "out_sku_id")
        private String outSkuId;
        @JSONField(name = "thumb_img")
        private String thumbImg;
        @JSONField(name = "sale_price")
        private BigDecimal salePrice;
        @JSONField(name = "market_price")
        private BigDecimal marketPrice;
        @JSONField(name = "stock_num")
        private Integer stockNum;
        @JSONField(name = "sku_code")
        private String skuCode;
        @JSONField(name = "barcode")
        private String barcode;
        @JSONField(name = "sku_attrs")
        private List<skuAttrs> skuAttrs;
    }

    @Data
    public static class skuAttrs{

        public skuAttrs(String attrKey, String attrValue){
            this.attrKey = attrKey;
            this.attrValue = attrValue;
        }

        @JSONField(name = "attr_key")
        private String attrKey;
        @JSONField(name = "attr_value")
        private String attrValue;
    }

}
