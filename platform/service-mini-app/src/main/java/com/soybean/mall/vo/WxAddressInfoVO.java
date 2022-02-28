package com.soybean.mall.vo;

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
public class WxAddressInfoVO implements Serializable {
    private static final long serialVersionUID = -9101306416885429078L;
    @JSONField(name = "receiver_name")
    private String receiverName;
    @JSONField(name = "detailed_address")
    private String detailedAddress;
    @JSONField(name = "tel_number")
    private String telNumber;
    private String country;
    private String province;
    private String city;
    private String town;

}

