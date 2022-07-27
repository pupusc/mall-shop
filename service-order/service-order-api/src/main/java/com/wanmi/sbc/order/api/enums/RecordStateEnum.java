package com.wanmi.sbc.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum RecordStateEnum {
//     0、创建 1 锁定 2 成功 3 普通取消 4 黑名单取消
    CREATE(0, "创建"),
    LOCK(1, "锁定"),
    SUCCESS(2, "成功"),
    NORMAL_CANCEL(3, "普通取消"),
    FORCE_CANCEL(4, "黑名单取消"),
    FAIL(5, "添加失败");

    private final Integer code;
    private final String message;

    public static RecordStateEnum getByCode(int code) {
        for (RecordStateEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
