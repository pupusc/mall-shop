package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class WxCreateOrderRequest implements Serializable {
    private static final long serialVersionUID = 5403388427341418460L;
    @JSONField(name ="create_time")
    private String createTime;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
    private Integer scene;
    @JSONField(name ="order_detail")
    private WxOrderDetailDTO orderDetail;
    @JSONField(name ="delivery_detail")
    private DeliveryDetail deliveryDetail;
    @JSONField(name ="address_info")
    private WxAddressInfoDTO addressInfo;


    @Data
    public static class DeliveryDetail {
        @JSONField(name ="delivery_type")
       private Integer deliveryType =1;
    }

}
