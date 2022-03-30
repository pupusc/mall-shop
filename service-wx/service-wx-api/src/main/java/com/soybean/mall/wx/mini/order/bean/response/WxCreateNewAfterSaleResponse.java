package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import lombok.Data;

@Data
public class WxCreateNewAfterSaleResponse extends WxResponseBase {

    @JSONField(name = "aftersale_id")
    private Long aftersaleId;
}
