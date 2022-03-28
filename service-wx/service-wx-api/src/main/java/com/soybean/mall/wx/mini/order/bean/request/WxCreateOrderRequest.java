package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
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
    /**
     * 订单类型：0，普通单，1，二级商户单
     */
    @JSONField(name ="fund_type")
    private Integer fundType;

    /**
     * 秒级时间戳，订单超时时间，获取支付参数将使用此时间作为prepay_id 过期时间;时间到期之后，微信会流转订单超时取消（status = 181）
     */
    @JSONField(name="expire_time")
    private Long expireTime;

    /**
     * 确认收货之后多久禁止发起售后，单位：天，需>=5天，default=5天
     */
    @JSONField(name="aftersale_duration")
    private Integer aftersaleDuration;

    @Data
    public static class DeliveryDetail {
        @JSONField(name ="delivery_type")
       private Integer deliveryType =1;
    }

}
