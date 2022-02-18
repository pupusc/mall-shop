package com.soybean.mall.wx.mini.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxDeleteProductRequest {

    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("out_product_id")
    private String outProductId;
}
