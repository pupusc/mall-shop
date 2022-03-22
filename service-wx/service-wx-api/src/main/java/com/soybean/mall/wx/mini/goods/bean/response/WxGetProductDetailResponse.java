package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

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
        @JSONField(name = "audit_info")
        private AuditInfo auditInfo;
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
}
