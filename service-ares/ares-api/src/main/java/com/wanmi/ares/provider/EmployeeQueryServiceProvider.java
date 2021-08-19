package com.wanmi.ares.provider;

import com.wanmi.ares.request.employee.EmployeeClientQueryRequest;
import com.wanmi.ares.request.employee.EmployeePerformanceQueryRequest;
import com.wanmi.ares.view.employee.EmployeeClientResponse;
import com.wanmi.ares.view.employee.EmployeePerormanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:40
 */
@FeignClient(name = "${application.ares.name}", contextId="EmployeeQueryServiceProvider")
public interface EmployeeQueryServiceProvider {

    /**
     * 查询业务员获客报表
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/employeeQuery/queryViewByClient")
    EmployeeClientResponse queryViewByClient(@RequestBody @Valid EmployeeClientQueryRequest request);

    /**
     * 查询业务员业绩报表
     *
     * @param request
     */
    @PostMapping("/ares/${application.ares.version}/employeeQuery/queryViewByPerformance")
    EmployeePerormanceResponse queryViewByPerformance(@RequestBody @Valid EmployeePerformanceQueryRequest request);

}
