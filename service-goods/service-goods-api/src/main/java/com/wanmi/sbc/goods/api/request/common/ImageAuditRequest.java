package com.wanmi.sbc.goods.api.request.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageAuditRequest {


    private String customerId;

    @ApiModelProperty("图片地址")
    private String content;

    private String imgType;

    private String verifyTool;
}
