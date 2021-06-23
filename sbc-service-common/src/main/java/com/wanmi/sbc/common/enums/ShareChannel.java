package com.wanmi.sbc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片
 *
 * @author zhangwenchang
 */
@ApiEnum
public enum ShareChannel {
    @ApiEnumProperty("微信")
    WECHAT,
    @ApiEnumProperty("朋友圈")
    MOMENTS,
    @ApiEnumProperty("QQ")
    QQ,
    @ApiEnumProperty("QQ空间")
    QZONE,
    @ApiEnumProperty("微博")
    WEIBO,
    @ApiEnumProperty("复制链接")
    COPYURL,
    @ApiEnumProperty("保存图片")
    SAVEPICTURE;

    @JsonCreator
    public ShareChannel fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
