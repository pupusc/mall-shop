package com.wanmi.ares.report.employee.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>业务员客户统计报表结构</p>
 * Created by of628-wenzhi on 2017-10-19-上午10:07.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeClientReport implements Serializable {

    private static final long serialVersionUID = -4615904777456927465L;
    /**
     * 业务员id
     */
    private String employeeId;

    /**
     * 客户总数
     */
    private Long total = 0L;

    /**
     * 新增客户数
     */
    private Long newlyNum = 0L;

    /**
     * 商户id
     */
    private String companyId;

}
