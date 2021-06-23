package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.source.model.root.CustomerAndLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface CustomerAndLevelMapper {

    /**
     * 统一根据旧的级别,修改成新的级别
     * @param oldLevelId
     * @param newLevelId
     * @return
     */
    int updateCustomerLevel(@Param("oldLevelId") String oldLevelId, @Param("newLevelId") String newLevelId);

    int update(@Param("id") String id, @Param("newLevelId") Long newLevelId, @Param("employeeId") String employeeId);

    int updateEmployeeId(@Param("customerId") String customerId, @Param("employeeId") String employeeId, @Param("bindDate") LocalDate bindDate);

    int insert(@Param("customerAndLevel")CustomerAndLevel customerAndLevel);

    int delete(String id);

    /**
     * 根据用户id,商家id查询用户等级
     * @param customerId
     * @param companyInfoId
     * @return
     */
    CustomerAndLevel selectCustomerStoreLevel(@Param("customerId") String customerId, @Param("companyInfoId") String companyInfoId);
}

