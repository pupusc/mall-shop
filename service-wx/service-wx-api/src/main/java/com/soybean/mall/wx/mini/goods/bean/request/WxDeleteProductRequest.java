package com.soybean.mall.wx.mini.goods.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxDeleteProductRequest {

    /**
     * spu id
     */
    @JSONField(name = "product_id")
    private Long productId;

    @JSONField(name = "out_product_id")
    private String outProductId;
}
