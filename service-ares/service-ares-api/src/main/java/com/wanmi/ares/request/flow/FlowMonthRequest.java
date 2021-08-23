package com.wanmi.ares.request.flow;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowMonthRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 16:44
 **/
@Data
public class FlowMonthRequest implements Serializable {

    private static final long serialVersionUID = 5950450856584178429L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流量统计汇总时间
     */
    private LocalDate date;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计月份
     */
    private String month;
}
