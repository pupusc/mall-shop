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
 * 周期购配置信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodDetailDTO {

    /**
     * 订单号
     */
    private String tid;

    /**
     * 子订单Id
     */
    private String oid;

    /**
     * 店铺id
     */
    private Long kdt_id;

    /**
     * 配送时间维度描述
     */
    private String dist_time_dim_str;

    /**
     * 总期数
     */
    private Integer total_issue;

    /**
     * 配送时间模式描述
     */
    private String dist_time_mode_str;

    /**
     * 最大顺延次数
     */
    private String max_postpone_sum;

    /**
     * 买家下单时选择的送达时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delivery_time;

    /**
     * 扩展信息
     */
    private String extend;

    /**
     * 配送时间模式，0— 每日一期，1—每周一期，2—每月一期
     */
    private Integer dist_time_mode;

    /**
     * 配送时间维度
     */
    private Integer dist_time_dim;

    /**
     * 配送提前期
     */
    private MultiPeriodLeadTimeDTO lead_time;
}
