package com.wanmi.sbc.goods.mini.wx.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    static class DescInfo{
        private String desc;
        private List<String> imgs;
    }

    @Data
    static class Sku{

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
    static class skuAttrs{

        public skuAttrs(String attrKey, String attrValue){
            this.attrKey = attrKey;
            this.attrValue = attrValue;
        }

        @JsonProperty("sku_attrs")
        private String attrKey;
        @JsonProperty("attr_value")
        private String attrValue;
    }

    public static WxAddProductRequest createByGoods(Goods goods, List<GoodsInfo> goodsInfos, Integer thirdCatId){
        WxAddProductRequest addProductRequest = new WxAddProductRequest();
        addProductRequest.setOutProductId(goods.getGoodsId());
        addProductRequest.setTitle(goods.getGoodsSubtitle());
        addProductRequest.setPath("http://www.baidu.com");
        addProductRequest.setHeadImg(Collections.singletonList("https://mmecimage.cn/p/wx77e672d6d34a4bed/HNTiaPWTllJ5R2pq9Jv9jRD5bZOWmq2svUUzJcZbcg"));
//        addProductRequest.setHeadImg(Collections.singletonList(goods.getGoodsImg()));
//        addProductRequest.setQualificationics(Collections.singletonList(goods.getGoodsImg()));
        //商品详情截取图片
        String detail = goods.getGoodsMobileDetail();
        if(detail != null){
            List<String> detailImgs = new ArrayList<>();
            String[] split = detail.split("<img");
            for (String s1 : split) {
                String[] split1 = s1.split("src=\"");
                for (String s2 : split1) {
                    if(s2.contains("http")){
                        detailImgs.add(s2.substring(0, s2.indexOf("\"")));
                    }
                }
            }
            addProductRequest.setDescInfo(DescInfo.builder().imgs(detailImgs).desc("").build());
        }
        //todo
        addProductRequest.setThirdCatId(378031);
        addProductRequest.setBrandId(2100000000);
        addProductRequest.setInfoVersion("1");
        addProductRequest.setSkus(createSkus(goods, goodsInfos));

        return addProductRequest;
    }

    private static List<Sku> createSkus(Goods goods, List<GoodsInfo> goodsInfos){
        List<Sku> skus = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            Sku sku = new Sku();
            sku.setOutProductId(goods.getGoodsId());
            sku.setOutSkuId(goodsInfo.getGoodsInfoId());
            sku.setThumbImg(goodsInfo.getGoodsInfoImg());
            sku.setSalePrice(goodsInfo.getMarketPrice());
            sku.setMarketPrice(goodsInfo.getMarketPrice());
            sku.setStockNum(goodsInfo.getStock().intValue());
//            sku.setSkuAttrs();
            skus.add(sku);
        }
        return skus;
    }
}
