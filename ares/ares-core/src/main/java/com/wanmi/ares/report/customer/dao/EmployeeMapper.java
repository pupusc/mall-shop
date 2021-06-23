package com.wanmi.ares.report.customer.dao;

import com.wanmi.ares.source.model.root.Employee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmployeeMapper {

    Employee queryById(@Param("employeeId") String employeeId);

    List<Employee> queryByIds(@Param("employeeIds") List<String> employeeIds);

    List<Employee> queryByKeyWords(@Param("keyWords") String keyWords);

    int deleteById(@Param("employeeId") String employeeId);

    int updateById(@Param("employee") Employee employee);

    int insert(@Param("employee") Employee employee);

    List<Map<String,Object>> queryEmployeeByIds(@Param("employeeIds") List<String> employeeIds);

    List<Map<String,Object>> queryEmployeeByKeyWords(@Param("keyWords") String keyWords);
}

