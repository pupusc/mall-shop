package com.soybean.mall.order.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressInfoVO implements Serializable {
    private static final long serialVersionUID = 4328784181600791529L;
    private String receiverName;
    private String detailedAddress;
    private String telNumber;
    private String country;
    private String province;
    private String city;
    private String town;
}
