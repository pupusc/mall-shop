package com.wanmi.ares.report.customer.model.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * @ClassName CustomerOrderDataRequest
 * @Description 客户统计--客户订货量报表统计request类
 * @Author lvzhenwei
 * @Date 2019/9/18 15:27
 **/
@Data
public class CustomerOrderDataRequest {

    /**
     * 开始时候
     */
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    private LocalDate endDate;

    /**
     * 统计日期
     */
    private LocalDate statisticsDate;

    /**
     * 统计类型：0：今天，1：昨天，2：最近七天；3：最近30天；4：按月统计，5：最近30天按周统计，6：最近6个月按周统计
     */
    private int flowDataType;

    /**
     * 0：运营后台；1：第三方商家
     */
    private int shopType;

    /**
     *  页码
     */
    private Long number;

    /**
     * 条数
     */
    private Long size;

    /**
     * 商家标识
     */
    private int companyInfoId;
}
