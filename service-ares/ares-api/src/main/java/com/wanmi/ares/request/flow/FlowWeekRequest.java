package com.wanmi.ares.request.flow;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowWeekRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 16:45
 **/
@Data
public class FlowWeekRequest implements Serializable {
    private static final long serialVersionUID = 888393540540207365L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流量统计汇总时间
     */
    private LocalDate flowDate;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计月份
     */
    private String month;

    /**
     * 统计类型，0：：最近30天按周统计，1：最近6个月按周统计
     */
    private Integer flowWeekType;

    /**
     * 按周统计，周开始日期
     */
    private LocalDate weekStartDate;

    /**
     * 按周统计，周结束日期
     */
    private LocalDate weekEndDate;
}
