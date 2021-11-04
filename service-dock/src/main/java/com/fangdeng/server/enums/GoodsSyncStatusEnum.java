package com.fangdeng.server.enums;

public enum GoodsSyncStatusEnum {
    UN_AUDIT(0, "待审核"),

    AUDITING(1, "审核中"),

    AUDITED(2, "审核通过"),

    SYNC(3,"已同步"),

    AUDIT_FAIL(4,"审核失败");

    private Integer key;

    private String value;

    GoodsSyncStatusEnum( Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
