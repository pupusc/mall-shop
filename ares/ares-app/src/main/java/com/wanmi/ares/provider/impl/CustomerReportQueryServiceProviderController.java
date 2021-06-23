package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.CustomerReportQueryServiceProvider;
import com.wanmi.ares.report.customer.service.CustomerReportThriftService;
import com.wanmi.ares.request.customer.CustomerOrderQueryRequest;
import com.wanmi.ares.view.customer.CustomerOrderPageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:44
 */
@RestController
public class CustomerReportQueryServiceProviderController implements CustomerReportQueryServiceProvider {
    @Autowired
    private CustomerReportThriftService customerReportThriftService;

    @Override
    public CustomerOrderPageView queryCustomerOrders(@RequestBody @Valid CustomerOrderQueryRequest request)  {
        return customerReportThriftService.queryCustomerOrders(request);
    }


}
