package com.wanmi.sbc.account.invoice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 开票项返回
 * Created by zhangjin on 2017/5/7.
 */
@Data
public class InvoiceResponse implements Serializable{

    /**
     * 主键
     */
    private String projectId;

    /**
     * 开票项目名称
     */
    private String projectName;

    /**
     * 开票项目修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime projectUpdateTime;
}
