package com.wanmi.sbc.goods.bean.enums;

public enum PublishState {

    NOT_ENABLE, ENABLE;

    public static PublishState fromValue(int value) {
        return values()[value];
    }

    public int toValue() {
        return this.ordinal();
    }
}
