package com.wanmi.sbc.erp.api.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    UNKNOWN(0, "未知"),
    IOS(1, "IOS"),
    ANDROID(2, "ANDROID"),
    WEB(3, "WEB");


    private Integer type;
    private String message;
}
