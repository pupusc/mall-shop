package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author lvzhenwei
 * @Description 短信签名签名来源枚举类
 * @Date 10:23 2019/12/6
 * @Param
 * @return
 **/
@ApiEnum
public enum SignSource {

    @ApiEnumProperty(" 0：企事业单位的全称或简称")
    ENTERPRISE,

    @ApiEnumProperty("1：工信部备案网站的全称或简称")
    WEBSITE,

    @ApiEnumProperty("2：APP应用的全称或简称")
    APP,

    @ApiEnumProperty("3：公众号或小程序的全称或简称")
    APPLETS,

    @ApiEnumProperty("4：电商平台店铺名的全称或简称")
    ECOMMERCEPLATFORMSTORENAME,

    @ApiEnumProperty("5：商标名的全称或简称")
    TRADEMARK;
    @JsonCreator
    public SignSource fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
