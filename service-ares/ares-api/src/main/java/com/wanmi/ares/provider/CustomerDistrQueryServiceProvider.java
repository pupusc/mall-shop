package com.wanmi.ares.provider;

import com.wanmi.ares.request.customer.CustomerDistrQueryRequest;
import com.wanmi.ares.view.customer.CustomerAreaDistrResponse;
import com.wanmi.ares.view.customer.CustomerLevelDistrResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:35
 */
@FeignClient(name = "${application.ares.name}", contextId = "CustomerDistrQueryServiceProvider")
public interface CustomerDistrQueryServiceProvider {

    @PostMapping("/ares/${application.ares.version}/customerDistr/queryLevelDistrView")
    CustomerLevelDistrResponse queryLevelDistrView(@RequestBody @Valid CustomerDistrQueryRequest request);

    @PostMapping("/ares/${application.ares.version}/customerDistr/queryAreaDistrView")
    CustomerAreaDistrResponse queryAreaDistrView(@RequestBody @Valid CustomerDistrQueryRequest request);

    @PostMapping("/ares/${application.ares.version}/customerDistr/totalCount")
    int totalCount(@RequestBody @Valid CustomerDistrQueryRequest request);

}
