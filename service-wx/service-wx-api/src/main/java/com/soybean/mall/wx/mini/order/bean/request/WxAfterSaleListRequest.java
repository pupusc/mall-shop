package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxAfterSaleListRequest implements Serializable {

    @JSONField(name ="out_order_id")
    private String outOrderId;

    private String openid;

    private Integer offset;

    private Integer limit;

    private Integer status;
}
