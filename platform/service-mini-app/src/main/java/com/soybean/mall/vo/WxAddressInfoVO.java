package com.soybean.mall.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxAddressInfoVO implements Serializable {
    private static final long serialVersionUID = -9101306416885429078L;
    private String receiverName;
    private String detailedAddress;
    private String telNumber;
    private String country;
    private String province;
    private String city;
    private String town;
}
