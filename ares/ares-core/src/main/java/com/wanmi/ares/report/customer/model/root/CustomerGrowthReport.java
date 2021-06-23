package com.wanmi.ares.report.customer.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户增长报表
 * Created by zhangjin on 2017/10/11.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerGrowthReport implements Serializable{

    private String id;

    /**
     * 日期 yyyy-mm-dd
     */
    private String baseDate;

    /**
     * 客户总数
     */
    private Long customerAllCount = 0L;

    /**
     * 客户新增数
     */
    private Long customerDayGrowthCount;

    /**
     * 注册客户数
     */
    private Long customerDayRegisterCount;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 创建时间
     */
    private Date createDate;
}
