package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class WxGetProductDetailResponse extends WxResponseBase {

    private Spu spu;

    @Data
    public static class Spu {

        private Integer status;
        @JSONField(name = "edit_status")
        private Integer editStatus;
        @JSONField(name = "product_id")
        private Long productId;
        @JSONField(name = "out_product_id")
        private String outProductId;
        @JSONField(name = "title")
        private String title;
        @JSONField(name = "audit_info")
        private AuditInfo auditInfo;
        @JSONField(name = "skus")
        private List<Sku> skus;
    }

    @Data
    public static class AuditInfo {

        @JSONField(name = "reject_reason")
        private String rejectReason;
        @JSONField(name = "audit_time")
        private String auditTime;
        @JSONField(name = "submit_time")
        private String submitTime;
    }


    @Data
    public static class Sku {

        @JSONField(name = "out_product_id")
        private String outProductId;
        @JSONField(name = "out_sku_id")
        private String outSkuId;
        //小图
        @JSONField(name = "thumb_img")
        private String thumbImg;
        //售价 单位分
        @JSONField(name = "sale_price")
        private String salePrice;
        //市场价
        @JSONField(name = "market_price")
        private String marketPrice;
        //库存
        @JSONField(name = "stock_num")
        private Integer stockNum;
    }
}
