package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxDealAftersaleRequest {

    @JSONField(name = "aftersale_id")
    private Long aftersaleId;
    @JSONField(name = "out_aftersale_id")
    private String outAftersaleId;
}
