package com.wanmi.sbc.order.returnorder.model.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.base.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 退货日志记录
 * Created by jinwei on 21/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnEventLog {

    /**
     * 操作人
     */
    private Operator operator;

    /**
     * eventType
     */
    private String eventType;

    /**
     * eventDetail
     */
    private String eventDetail;

    /**
     * 描述
     */
    private String remark;

    /**
     * eventTime
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime eventTime = LocalDateTime.now();
}
