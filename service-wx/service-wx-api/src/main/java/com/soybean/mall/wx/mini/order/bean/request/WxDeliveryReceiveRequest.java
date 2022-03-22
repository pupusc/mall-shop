package com.soybean.mall.wx.mini.order.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxDeliveryReceiveRequest implements Serializable {
    private static final long serialVersionUID = 968921343398633933L;
    /**
     * 商家自定义订单ID
     */
    @JSONField(name ="out_order_id")
    private String outOrderId;
    private String openid;
}
