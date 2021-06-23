package com.wanmi.ares.report.flow.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ms.util.CustomLocalDateTimeDeserializer;
import com.wanmi.ms.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * flow_data
 *
 * @author
 */
@Data
public class FlowData implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流量统计汇总时间
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;

    /**
     * 访问人数
     */
    private Long uv;

    /**
     * 访问量
     */
    private Long pv;

    /**
     * 商品访问人数
     */
    private Long goodsUv;

    /**
     * 商品访问量
     */
    private Long goodsPv;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计月份
     */
    private String month;

    /**
     * 统计类型，0：今天，1：昨天，2：最近七天；3：最近30天；4：按月统计，5：最近30天按周统计，6：最近6个月按周统计
     */
    private Integer type;

    /**
     * 按周统计，周开始日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate weekStartDate;

    /**
     * 按周统计，周结束日期
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate weekEndDate;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime creatTime;

}