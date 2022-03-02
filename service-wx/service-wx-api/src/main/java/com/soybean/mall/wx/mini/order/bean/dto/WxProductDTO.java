package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxProductDTO implements Serializable {
    private static final long serialVersionUID = -3003557842211905798L;
    @JSONField(name="out_product_id")
    private String outProductId;
    @JSONField(name="out_sku_id")
    private String outSkuId;
    @JSONField(name="product_cnt")
    private Integer prroductNum;
}
