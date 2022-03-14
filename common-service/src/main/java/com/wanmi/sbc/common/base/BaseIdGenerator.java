package com.wanmi.sbc.common.base;


/**
 * 主键生成器
 * Created by MicLee on 16/5/3.
 */
public class BaseIdGenerator extends AbstractBaseIdGenerator {

    @Override
    protected String segmentValue() {
        return "PK_ID";
    }

    @Override
    protected String prefix() {
        return "PK";
    }

    @Override
    protected String dateStr() {
        return "";
    }
}
