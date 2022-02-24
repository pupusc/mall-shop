package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class WxAddProductResponse extends WxResponseBase {

    private ResponseData data;

    @Data
    public static class ResponseData{

        @JSONField(name = "product_id")
        private Long productId;
        @JSONField(name = "out_product_id")
        private String outProductId;
        @JSONField(name = "create_time")
        private String createTime;
        @JSONField(name = "skus")
        private List<Sku> skus;
    }

    @Data
    public static class Sku{
        @JSONField(name = "sku_id")
        private String skuId;
        @JSONField(name = "out_sku_id")
        private String outSkuId;
    }
}
