package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.EmployeeQueryServiceProvider;
import com.wanmi.ares.report.employee.service.EmployeeQueryServiceImpl;
import com.wanmi.ares.request.employee.EmployeeClientQueryRequest;
import com.wanmi.ares.request.employee.EmployeePerformanceQueryRequest;
import com.wanmi.ares.view.employee.EmployeeClientResponse;
import com.wanmi.ares.view.employee.EmployeePerormanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:41
 */
@RestController
public class EmployeeQueryServiceProviderController implements EmployeeQueryServiceProvider {

    @Autowired
    private EmployeeQueryServiceImpl employeeQueryService;

    @Override
    public EmployeeClientResponse queryViewByClient(@RequestBody @Valid EmployeeClientQueryRequest request) {
        return employeeQueryService.queryViewByClient(request);
    }

    @Override
    public EmployeePerormanceResponse queryViewByPerformance(@RequestBody @Valid EmployeePerformanceQueryRequest request) {
        return employeeQueryService.queryViewByPerformance(request);
    }
}
