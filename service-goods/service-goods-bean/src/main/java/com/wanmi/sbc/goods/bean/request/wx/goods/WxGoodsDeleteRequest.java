package com.wanmi.sbc.goods.bean.request.wx.goods;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxGoodsDeleteRequest {

    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("out_product_id")
    private String outProductId;
}
