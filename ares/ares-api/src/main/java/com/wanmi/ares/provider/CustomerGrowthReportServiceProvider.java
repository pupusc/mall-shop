package com.wanmi.ares.provider;

import com.wanmi.ares.request.customer.CustomerGrowthReportRequest;
import com.wanmi.ares.request.customer.CustomerTrendQueryRequest;
import com.wanmi.ares.view.customer.CustomerGrowthPageView;
import com.wanmi.ares.view.customer.CustomerGrowthTrendView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:31
 */
@FeignClient(name = "${application.ares.name}", contextId = "CustomerGrowthReportServiceProvider")
public interface CustomerGrowthReportServiceProvider {

    @PostMapping("/ares/${application.ares.version}/customerGrowth/queryCustomerGrouthList")
    CustomerGrowthPageView queryCustomerGrouthList(@RequestBody @Valid CustomerGrowthReportRequest customerGrowthReportRequest);

    @PostMapping("/ares/${application.ares.version}/customerGrowth/queryCustomerTrendList")
    List<CustomerGrowthTrendView> queryCustomerTrendList(@RequestBody @Valid CustomerTrendQueryRequest customerTrendQueryRequest);

}
