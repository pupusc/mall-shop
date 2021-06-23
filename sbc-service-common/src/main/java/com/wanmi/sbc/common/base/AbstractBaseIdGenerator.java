package com.wanmi.sbc.common.base;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

/**
 * 主键生成器
 */
public abstract class AbstractBaseIdGenerator extends TableGenerator {
    private static final String GROUP_DIMENSION_STR = "base_primary_key";
    private static final String SEGMENT_COLUMN_NAME = "name";
    private static final String VALUE_COLUMN_NAME = "value";
    private static final String INIT_VALUE = "1";
    private static final int INIT_LENGTH = 5;

    /**
     * name值, eg, PK_ID
     *
     * @return name值
     */
    protected abstract String segmentValue();

    protected abstract String prefix();

    protected String dateStr() {
        return new SimpleDateFormat("yyyyMMdd").format(Date.from(Instant.now()));
    }


    protected String initialValue() {
        return INIT_VALUE;
    }

    protected int initialLength() {
        return INIT_LENGTH;
    };

    @Override
    public void configure(Type type, Properties params,ServiceRegistry serviceRegistry) {
        params.setProperty(TABLE_PARAM, GROUP_DIMENSION_STR);
        params.setProperty(SEGMENT_COLUMN_PARAM, SEGMENT_COLUMN_NAME);
        params.setProperty(VALUE_COLUMN_PARAM, VALUE_COLUMN_NAME);
        params.setProperty(SEGMENT_VALUE_PARAM, segmentValue());
        params.setProperty(INITIAL_PARAM, initialValue());
        super.configure(new IntegerType(), params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        return prefix() + dateStr() + completeByZero(super.generate(session, obj).toString(), initialLength());
    }

    private String completeByZero(String strBase, int len) {
        if (strBase.length() >= len) {
            return strBase;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len - strBase.length(); i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.append(strBase).toString();
    }
}
