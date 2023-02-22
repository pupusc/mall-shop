package com.wanmi.sbc.setting.api.request.tradeOrder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoodsMonthRequest implements Serializable {
    private static final long serialVersionUID = -3354087848195632606L;

    private String id;

    private String goodsInfoId;

    private Integer statMonth;

    private BigDecimal payCount;

    private BigDecimal payNum;

    private BigDecimal payMoney;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creatTM;
}
