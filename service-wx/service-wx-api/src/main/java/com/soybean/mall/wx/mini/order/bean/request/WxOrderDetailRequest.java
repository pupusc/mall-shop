package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxOrderDetailRequest implements Serializable {
//    @JSONField(name="out_order_id")
//    private String outOrderId;
//    private String openid;

    @JSONField(name="start_create_time")
    private String startTime;
    @JSONField(name="end_create_time")
    private String endTime;
    private Integer page =1;
    @JSONField(name="page_size")
    private Integer pageSize =20;

}
