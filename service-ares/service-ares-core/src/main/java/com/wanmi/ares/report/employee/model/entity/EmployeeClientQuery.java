package com.wanmi.ares.report.employee.model.entity;

import com.wanmi.ares.enums.EmployeeClientSort;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * <p>业务员获客报表查询参数</p>
 * Created by of628-wenzhi on 2017-10-19-下午6:13.
 */
@ToString
public class EmployeeClientQuery extends EmployeePageQuery {

    public EmployeeClientQuery(EmployeeClientSort sort) {
        switch (sort) {
            case NEWLY_ASC:
                this.setProperty("newly_num");
                this.setDirection(Sort.Direction.ASC);
                break;
            case NEWLY_DESC:
                this.setProperty("newly_num");
                this.setDirection(Sort.Direction.DESC);
                break;
            case TOTAL_ASC:
                this.setProperty("total");
                this.setDirection(Sort.Direction.ASC);
                break;
            case TOTAL_DESC:
                this.setProperty("total");
                this.setDirection(Sort.Direction.DESC);
                break;
            default:
                this.setProperty("total");
                this.setDirection(Sort.Direction.DESC);
                break;
        }
    }

    public String getTable() {
        if (StringUtils.isNotBlank(this.getYearMonth())) {
            return "employee_client_month";
        }
        switch (this.getDateCycle()) {
            case today:
                return "employee_client";
            case yesterday:
                return "employee_client";
            case latest7Days:
                return "employee_client_recent_seven";
            case latest30Days:
                return "employee_client_recent_thirty";
            default:
                return "employee_client";
        }
    }

}
