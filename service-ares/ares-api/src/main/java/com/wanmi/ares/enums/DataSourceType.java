package com.wanmi.ares.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>数据池数据的来源操作类型</p>
 * Created by of628-wenzhi on 2017-09-25-下午5:16.
 */
public enum DataSourceType {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单支付
     */
    PAY,

    /**
     * 退单创建
     */
    RETURN;

    @JsonCreator
    public DataSourceType fromValue(String name) {
        return valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
