package com.soybean.mall.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.soybean.mall.wx.mini.order.bean.dto.WxAddressInfoDTO;
import com.soybean.mall.wx.mini.order.bean.dto.WxOrderDetailDTO;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class WxOrderCommitResultVO implements Serializable {
    private static final long serialVersionUID = 556503414689064030L;


    @JSONField(name ="create_time")
    private String createTime;
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
    private String path;
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
