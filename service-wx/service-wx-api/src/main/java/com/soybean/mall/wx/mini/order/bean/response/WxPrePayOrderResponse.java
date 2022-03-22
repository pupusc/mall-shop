package com.soybean.mall.wx.mini.order.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxPrePayOrderResponse implements Serializable {
    @JSONField(name="prepay_id")
    private String prepayId;
}
