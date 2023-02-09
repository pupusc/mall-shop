package com.wanmi.sbc.setting.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;

/**
 * @Description 新版楼层类型
 * @Author zh
 * @Date  2023/2/6 10:33
 * @param: null
 * @return: null
 */
@ApiEnum
public enum TopicStoreyTypeV2 {

    SCROLLIMAGE(6, "轮播"),

    ROLLINGMESSAGE(12, "滚动消息"),

    VOUCHER(17, "抵用券组件"),

    POINTS(15, "积分"),

    RANKLIST(11,"大家都在买（榜单）"),

    NEWBOOK( 13,"新书速递");

    private Integer id;

    private String desc;

    TopicStoreyTypeV2(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return desc;
    }

}