package com.wanmi.sbc.marketing.bean.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerAndOpenIdDTO implements Serializable {
    private static final long serialVersionUID = 2883565825700835861L;
    private String customerId;
    private String openId;
}
