package com.wanmi.sbc.goods.api.request.common;

import lombok.Data;

@Data
public class ImageVerifyRequest {
    private String customerId;
    private String hitReason;
    private String requestId;
    private String suggestion;
    private String ocrStr;
}
