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
import java.util.List;

/**
 * 订单退款信息结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrder implements Serializable {

    private static final long serialVersionUID = 7901580173673204588L;

    /**
     * 用户申请退款时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime refund_time;

    /**
     * 有赞生成的退款id
     */
    private String refund_id;

    /**
     * 退款金额，单位：元
     */
    private String refund_fee;

    /**
     * 退款状态
     */
    private Integer refund_state;

    /**
     * 退款类型
     */
    private Integer refund_type;

    /**
     * 退款交易明细
     */
    private List<Oid> oids;

    /**
     * 退款流水
     */
    private List<RefundFund> refund_fund;
}
