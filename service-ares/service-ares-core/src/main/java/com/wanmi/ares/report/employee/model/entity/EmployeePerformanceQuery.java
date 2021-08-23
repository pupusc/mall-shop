package com.wanmi.ares.report.employee.model.entity;

import com.wanmi.ares.enums.EmployeePerformanceSort;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * <p>业务员业绩报表查询参数</p>
 * Created by of628-wenzhi on 2017-10-25-下午6:25.
 */
@ToString
public class EmployeePerformanceQuery extends EmployeePageQuery {
    private EmployeePerformanceSort sort;

    public EmployeePerformanceQuery(EmployeePerformanceSort sort) {
        switch (sort) {
            case CUSTOMER_UNIT_PRICE_ASC:
                this.setProperty("customer_unit_price");
                this.setDirection(Sort.Direction.ASC);
                break;
            case CUSTOMER_UNIT_PRICE_DESC:
                this.setProperty("customer_unit_price");
                this.setDirection(Sort.Direction.DESC);
                break;
            case ORDER_UNIT_PRICE_ASC:
                this.setProperty("order_unit_price");
                this.setDirection(Sort.Direction.ASC);
                break;
            case ORDER_UNIT_PRICE_DESC:
                this.setProperty("order_unit_price");
                this.setDirection(Sort.Direction.DESC);
                break;
            case ORDER_AMT_ASC:
                this.setProperty("amount");
                this.setDirection(Sort.Direction.ASC);
                break;
            case ORDER_AMT_DESC:
                this.setProperty("amount");
                this.setDirection(Sort.Direction.DESC);
                break;
            case ORDER_COUNT_ASC:
                this.setProperty("order_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case ORDER_COUNT_DESC:
                this.setProperty("order_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            case ORDER_CUSTOMER_ASC:
                this.setProperty("customer_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case ORDER_CUSTOMER_DESC:
                this.setProperty("customer_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            case PAY_AMT_ASC:
                this.setProperty("pay_amount");
                this.setDirection(Sort.Direction.ASC);
                break;
            case PAY_AMT_DESC:
                this.setProperty("pay_amount");
                this.setDirection(Sort.Direction.DESC);
                break;
            case PAY_COUNT_ASC:
                this.setProperty("pay_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case PAY_COUNT_DESC:
                this.setProperty("pay_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            case PAY_CUSTOMER_ASC:
                this.setProperty("pay_customer_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case PAY_CUSTOMER_DESC:
                this.setProperty("pay_customer_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            case RETURN_AMT_ASC:
                this.setProperty("return_amount");
                this.setDirection(Sort.Direction.ASC);
                break;
            case RETURN_AMT_DESC:
                this.setProperty("return_amount");
                this.setDirection(Sort.Direction.DESC);
                break;
            case RETURN_COUNT_ASC:
                this.setProperty("return_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case RETURN_COUNT_DESC:
                this.setProperty("return_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            case RETURN_CUSTOMER_ASC:
                this.setProperty("return_customer_count");
                this.setDirection(Sort.Direction.ASC);
                break;
            case RETURN_CUSTOMER_DESC:
                this.setProperty("return_customer_count");
                this.setDirection(Sort.Direction.DESC);
                break;
            default:
                this.setProperty("amount");
                this.setDirection(Sort.Direction.DESC);
                break;
        }
    }


    public String getTable() {
        if (StringUtils.isNotBlank(this.getYearMonth())) {
            return "employee_performance_month";
        }
        switch (this.getDateCycle()) {
            case today:
                return "employee_performance";
            case yesterday:
                return "employee_performance";
            case latest7Days:
                return "employee_performance_recent_seven";
            case latest30Days:
                return "employee_performance_recent_thirty";
            default:
                return "employee_performance";
        }
    }

}
