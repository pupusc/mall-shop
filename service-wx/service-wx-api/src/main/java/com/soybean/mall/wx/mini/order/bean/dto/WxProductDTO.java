package com.soybean.mall.wx.mini.order.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxProductDTO implements Serializable {
    private static final long serialVersionUID = -3003557842211905798L;
    @JsonProperty("out_product_id")
    private String outProductId;
    @JsonProperty("out_sku_id")
    private String outSkuId;
    @JsonProperty("product_cnt")
    private Integer prroductNum;
}
