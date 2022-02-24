package com.soybean.mall.wx.mini.order.bean.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class WxDeliveryReceiveRequest implements Serializable {
    private static final long serialVersionUID = 968921343398633933L;
    /**
     * 商家自定义订单ID
     */
    @JsonProperty("out_order_id")
    private String outOrderId;
    private String openid;
}
