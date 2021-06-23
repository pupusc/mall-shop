package com.wanmi.sbc.order.trade.model.entity.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.wanmi.sbc.common.base.Operator;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单操作日志
 * Created by jinwei on 19/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeEventLog implements Serializable {

    /**
     * 操作员
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
     * eventTime
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime eventTime;
}
