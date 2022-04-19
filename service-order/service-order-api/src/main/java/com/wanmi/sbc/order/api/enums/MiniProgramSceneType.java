package com.wanmi.sbc.order.api.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;

@ApiEnum
public enum MiniProgramSceneType {

    MINI_PROGRAM(1,"小程序"),

    WECHAT_VIDEO(2,"视频号");





    private Integer index;
    private String desc;

    MiniProgramSceneType(Integer index,String desc) {
        this.index = index;
        this.desc = desc;
    }


    public Integer getIndex() {
        return index;
    }

    public String getDesc() {
        return desc;
    }
}
