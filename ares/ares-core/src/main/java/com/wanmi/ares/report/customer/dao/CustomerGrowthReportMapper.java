package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.base.PageRequest;
import com.wanmi.ares.report.customer.model.root.CustomerGrowthReport;
import com.wanmi.ares.request.customer.CustomerGrowthReportRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户增长数据源
 */
@Repository
public interface CustomerGrowthReportMapper {

    /**
     * 生成客户增长数据
     *
     * @param customerGrowthReportList customerGrowthReportList
     * @return rows
     */
    int saveCustomerGrowthReport(@Param("customerGrowthReportList") List<CustomerGrowthReport> customerGrowthReportList);

    /**
     * 查前一天的allCount
     *
     * @return
     */
    CustomerGrowthReport findPreCustomerAllCount(@Param("preDate") String preDate, @Param("companyId") String companyId);


    /**
     * 根据时间清理那天的数据
     * @param deleteDate
     */
    void deleteCustomerGrowthReportByDate(@Param("deleteDate") String deleteDate);


    /**
     * 查客户增长报表
     *
     * @param customerGrowthReportRequest
     * @return List<CustomerGrowthReport>
     */
    List<CustomerGrowthReport> findCustomerGrowReport(@Param("customerGrowthReportRequest") CustomerGrowthReportRequest customerGrowthReportRequest, @Param("pageRequest") PageRequest pageRequest);

    /**
     * 查询平台所有的增长数量
     * @param customerGrowthReportRequest
     * @param pageRequest
     * @return
     */
    List<CustomerGrowthReport> findAllCustomerGrowReport(@Param("customerGrowthReportRequest") CustomerGrowthReportRequest customerGrowthReportRequest, @Param("pageRequest") PageRequest pageRequest);

    /**
     * 查询数量
     * @param customerGrowthReportRequest
     * @return
     */
    Integer countCustomerReport(@Param("customerGrowthReportRequest") CustomerGrowthReportRequest customerGrowthReportRequest);


    /**
     * 根据时间段查列表
     * @param startDate startDate
     * @param endDate endDate
     * @return list
     */
    List<CustomerGrowthReport> findCustomerGrowthReportByDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("companyId") String companyId);

    /**
     * 查询所有的列表
     * @param startDate
     * @param endDate
     * @return
     */
    List<CustomerGrowthReport> findAllCustomerGrowthReportByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
