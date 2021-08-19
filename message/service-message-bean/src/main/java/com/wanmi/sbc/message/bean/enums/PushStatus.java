package com.wanmi.sbc.message.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 友盟接口返回的推送状态
 * 友盟API：https://developer.umeng.com/docs/66632/detail/68343#h2-u4EFBu52A1u7C7Bu6D88u606Fu72B6u6001u67E5u8BE25
 */
@ApiEnum
public enum PushStatus {
    @ApiEnumProperty(" 0:排队中")
    QUEUE,
    @ApiEnumProperty(" 1:发送中")
    SEND,
    @ApiEnumProperty(" 2:发送完成")
    SEND_END,
    @ApiEnumProperty(" 3:发送失败")
    SEND_FAIL,
    @ApiEnumProperty(" 4:消息被撤销")
    CANCEL,
    @ApiEnumProperty(" 5:消息过期")
    OVERDUE,
    @ApiEnumProperty(" 6:筛选结果为空")
    RESULT_IS_NULL,
    @ApiEnumProperty(" 7:定时任务尚未开始处理")
    JOB_NOT_START;

    @JsonCreator
    public static PushStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
