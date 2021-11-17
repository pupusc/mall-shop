package com.wanmi.sbc.setting.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AtmosphereQueryRequest {
    @ApiModelProperty("sku编码")
    private List<String> skuNo;

    private Integer pageNum;

    private Integer pageSize;

}
