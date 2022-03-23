package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxPayInfoDTO implements Serializable {
    @JSONField(name ="pay_method_type")
    private Integer payMethodType;
    @JSONField(name ="prepay_id")
    private String prepayId;
    @JSONField(name ="prepay_time")
    private String prepayTime;
}
