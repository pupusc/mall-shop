package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.CustomerDistrQueryServiceProvider;
import com.wanmi.ares.report.customer.service.DistributeQueryServiceImpl;
import com.wanmi.ares.request.customer.CustomerDistrQueryRequest;
import com.wanmi.ares.view.customer.CustomerAreaDistrResponse;
import com.wanmi.ares.view.customer.CustomerLevelDistrResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:37
 */
@RestController
public class CustomerDistrQueryServiceProviderController implements CustomerDistrQueryServiceProvider {

    @Autowired
    private DistributeQueryServiceImpl distributeQueryService;

    @Override
    public CustomerLevelDistrResponse queryLevelDistrView(@RequestBody @Valid CustomerDistrQueryRequest request)  {
        return distributeQueryService.queryLevelDistrView(request);
    }

    @Override
    public CustomerAreaDistrResponse queryAreaDistrView(@RequestBody @Valid CustomerDistrQueryRequest request)  {
        return distributeQueryService.queryAreaDistrView(request);
    }

    @Override
    public int totalCount(@RequestBody @Valid CustomerDistrQueryRequest request) {
        return distributeQueryService.totalCount(request);
    }
}
