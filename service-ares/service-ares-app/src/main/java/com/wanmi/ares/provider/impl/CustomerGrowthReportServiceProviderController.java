package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.CustomerGrowthReportServiceProvider;
import com.wanmi.ares.report.customer.service.CustomerGrowthReportThriftService;
import com.wanmi.ares.request.customer.CustomerGrowthReportRequest;
import com.wanmi.ares.request.customer.CustomerTrendQueryRequest;
import com.wanmi.ares.view.customer.CustomerGrowthPageView;
import com.wanmi.ares.view.customer.CustomerGrowthTrendView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:33
 */
@RestController
public class CustomerGrowthReportServiceProviderController implements CustomerGrowthReportServiceProvider {
    @Autowired
    private CustomerGrowthReportThriftService customerGrowthReportThriftService;

    @Override
    public CustomerGrowthPageView queryCustomerGrouthList(@RequestBody @Valid CustomerGrowthReportRequest customerGrowthReportRequest)  {
        return customerGrowthReportThriftService.queryCustomerGrouthList(customerGrowthReportRequest);
    }

    @Override
    public List<CustomerGrowthTrendView> queryCustomerTrendList(@RequestBody @Valid CustomerTrendQueryRequest customerTrendQueryRequest)  {
        return customerGrowthReportThriftService.queryCustomerTrendList(customerTrendQueryRequest);
    }
}
