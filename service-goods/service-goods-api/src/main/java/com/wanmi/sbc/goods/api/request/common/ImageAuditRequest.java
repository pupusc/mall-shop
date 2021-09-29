package com.wanmi.sbc.goods.api.request.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImageAuditRequest {


    private String customerId;

    @ApiModelProperty("图片地址")
    private String content;

    private String imgType;

    private String verifyTool;
}
