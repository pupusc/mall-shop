package com.soybean.mall.wx.mini.goods.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WxUpdateProductWithoutAuditRequest extends WxResponseBase {

    @JSONField(name = "out_product_id")
    private String outProductId;
    @JSONField(name = "product_id")
    private Long productId;
    @JSONField(name = "skus")
    private List<Sku> skus;

    @Data
    public static class Sku{

        @JSONField(name = "out_sku_id")
        private String outSkuId;
        @JSONField(name = "sale_price")
        private BigDecimal salePrice;
        @JSONField(name = "market_price")
        private BigDecimal marketPrice;
        @JSONField(name = "stock_num")
        private Integer stockNum;
        /**
         * 条形码
         */
        @JSONField(name = "barcode")
        private String barcode;

        /**
         * 商品编码，字符类型，最长不超过20
         */
        @JSONField(name = "sku_code")
        private String skuCode;
    }
}
