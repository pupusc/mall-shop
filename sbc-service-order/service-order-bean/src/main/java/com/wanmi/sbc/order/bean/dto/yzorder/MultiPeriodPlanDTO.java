package com.wanmi.sbc.order.bean.dto.yzorder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 周期购订单最新一期多期发货计划
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodPlanDTO {

    /**
     * 当前期数
     */
    private Integer issue;

    /**
     * 子订单Id
     */
    private Long oid;

    /**
     * 预计送达时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime estimated_delivery_time;

    /**
     * 货单号
     */
    private String delivery_no;

    /**
     * 订单号
     */
    private String tid;

    /**
     * 店铺Id
     */
    private Long kdt_id;

    /**
     * 配送状态
     */
    private Integer delivery_state;
}
