package com.wanmi.ares.report.customer.model.root;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerTrendReport implements Serializable {

    /**
     * x 轴数据
     */
    private String xValue;

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
}
