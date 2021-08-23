package com.wanmi.ares.report.employee.model.entity;

import com.wanmi.ares.enums.QueryDateCycle;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>业务员报表分页排序查询参数</p>
 * Created by of628-wenzhi on 2017-10-25-下午6:29.
 */
public class EmployeePageQuery{
    /**
     * 商户id
     */
    private String companyId;

    /**
     * 业务员id集合
     */
    private List<String> employeeIds;

    /**
     * 年月周期信息
     */
    private String yearMonth;

    /**
     * 日期周期信息
     */
    private QueryDateCycle dateCycle;

    /**
     * 分页开始index
     */
    private int pageBegin;

    /**
     * 页数
     */
    private int pageSize;

    /**
     * 排序规则
     */
    private Sort.Direction direction;

    /**
     * 排序字段
     */
    private String property;

    QueryDateCycle getDateCycle() {
        return dateCycle;
    }

    public String getCompanyId() {
        return companyId;
    }

    public List<String> getEmployeeIds() {
        return employeeIds;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public int getPageBegin() {
        return pageBegin;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public String getProperty() {
        return property;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setEmployeeIds(List<String> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public void setDateCycle(QueryDateCycle dateCycle) {
        this.dateCycle = dateCycle;
    }

    public void setPageBegin(int pageBegin) {
        this.pageBegin = pageBegin;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    void setProperty(String property) {
        this.property = property;
    }

    //天报表查询时使用
    public String getDay() {
        switch (this.getDateCycle()) {
            case today:
                return LocalDate.now().toString();
            case yesterday:
                return LocalDate.now().minusDays(1).toString();
            default:
                return LocalDate.now().toString();
        }

    }
}
