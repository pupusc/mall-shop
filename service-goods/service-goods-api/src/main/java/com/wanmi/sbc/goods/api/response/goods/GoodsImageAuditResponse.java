package com.wanmi.sbc.goods.api.response.goods;

import lombok.Data;

@Data
public class GoodsImageAuditResponse {
    private String ocrStr;
    private String requestId;
    private Integer supplierType;
}
