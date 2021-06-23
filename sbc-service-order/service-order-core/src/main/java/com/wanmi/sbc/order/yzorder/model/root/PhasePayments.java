package com.wanmi.sbc.order.yzorder.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 多阶段支付信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhasePayments implements Serializable {

    private static final long serialVersionUID = -7521281355817667182L;

    /**
     * 支付开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime pay_start_time;

    /**
     * 支付结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime pay_end_time;

    /**
     * 外部支付流水号
     */
    private String outer_transaction_no;

    /**
     * 支付阶段
     */
    private Long phase;

    /**
     * 阶段支付金额
     */
    private String real_price;

    /**
     * 内部支付流水号
     */
    private String inner_transaction_no;

    /**
     * 支付类型
     */
    private String pay_way_str;
}
