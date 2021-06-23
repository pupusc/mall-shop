package com.wanmi.ares.provider;

import com.wanmi.ares.request.customer.CustomerOrderQueryRequest;
import com.wanmi.ares.view.customer.CustomerOrderPageView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:43
 */
@FeignClient(name = "${application.ares.name}", contextId = "CustomerReportQueryServiceProvider")
public interface CustomerReportQueryServiceProvider {
    @PostMapping("/ares/${application.ares.version}/customerReport/queryCustomerOrders")
    CustomerOrderPageView queryCustomerOrders(@RequestBody @Valid CustomerOrderQueryRequest request);


}
