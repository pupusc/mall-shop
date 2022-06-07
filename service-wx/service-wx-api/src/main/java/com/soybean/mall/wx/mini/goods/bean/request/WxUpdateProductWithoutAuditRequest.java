package com.soybean.mall.wx.mini.goods.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.constant.ConstantUtil;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
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

        //库存
        @JSONField(name = "stock_num")
        private BigInteger tmpStockNum;

        public void setTmpStockNum(BigInteger tmpStockNum) {
            this.tmpStockNum = tmpStockNum;
            if (tmpStockNum.compareTo(new BigInteger(ConstantUtil.WX_MAX_STOCK + "")) > 0) {
                this.stockNum = ConstantUtil.WX_MAX_STOCK;
            }
        }

        private Long stockNum;

        public void setStockNum(Long stockNum) {
            this.stockNum = stockNum;
            this.tmpStockNum = new BigInteger(stockNum + "");
        }

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
