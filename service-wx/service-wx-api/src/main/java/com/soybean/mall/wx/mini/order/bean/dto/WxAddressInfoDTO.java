package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxAddressInfoDTO implements Serializable {
    private static final long serialVersionUID = 153825031830359192L;
    @JSONField(name ="receiver_name")
    private String receiverName;
    @JSONField(name ="detailed_address")
    private String detailedAddress;
    @JSONField(name ="tel_number")
    private String telNumber;
    private String country;
    private String province;
    private String city;
    private String town;
}
