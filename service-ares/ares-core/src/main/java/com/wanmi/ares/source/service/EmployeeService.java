package com.wanmi.ares.source.service;

import com.wanmi.ares.report.customer.dao.EmployeeMapper;
import com.wanmi.ares.request.mq.EmployeeRequest;
import com.wanmi.ares.source.model.root.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 业务员基础信息service
 * Created by sunkun on 2017/9/22.
 */
@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private CustomerService customerService;

    @Resource
    private EmployeeMapper employeeMapper;

    public void delete(String employeeId) throws Exception{
        if(StringUtils.isNotEmpty(employeeId)){
            employeeMapper.deleteById(employeeId);
            //删除时,解绑业务员与客户关系
            customerService.unbindCustomer(employeeId);
        }
    }

    public void modify(EmployeeRequest request) throws Exception{
        Employee employee = new Employee();
        BeanUtils.copyProperties(request, employee);
        employeeMapper.updateById(employee);
        if(request.getIsEmployee() != null && request.getIsEmployee() == 1){
            //若不是业务员,则解绑业务员与客户关系
            customerService.unbindCustomer(request.getId());
        }
    }

    /**
     * 按照id查询业务员详情
     * @param employeeId
     * @return
     */
    public Employee queryByEmployeeId(String employeeId){
        return employeeMapper.queryById(employeeId);
    }

    /**
     * 按照id集合查询业务员集合
     * @param employeeIds
     * @return
     */
    public List<Employee> queryByEmployeeIds(List<String> employeeIds){
        return employeeMapper.queryByIds(employeeIds);
    }

    /**
     * 按照id集合查询业务员集合
     * @param employeeIds
     * @return
     */
    public List<Map<String,Object>> findByEmployeeIds(List<String> employeeIds){
        return employeeMapper.queryEmployeeByIds(employeeIds);
    }

    /**
     * 按照关键字查询业务员集合
     * @param keyWords
     * @return
     */
    public List<Employee> queryByKeyWords(String keyWords){
        return employeeMapper.queryByKeyWords(keyWords);
    }

    /**
     * 按照关键字查询业务员集合
     * @param keyWords
     * @return
     */
    public List<Map<String,Object>> findByKeyWords(String keyWords){
        return employeeMapper.queryEmployeeByKeyWords(keyWords);
    }

    /**
     * 新增业务员
     * @param employee
     * @return
     */
    public int insertEmployee(Employee employee){
        return employeeMapper.insert(employee);
    }
}
