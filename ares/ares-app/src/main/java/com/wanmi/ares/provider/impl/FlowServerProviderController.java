package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.FlowServerProvider;
import com.wanmi.ares.report.flow.service.FlowDataService;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.thrift.FlowServiceImpl;
import com.wanmi.ares.view.flow.FlowPageView;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 10:23
 */
@RestController
public class FlowServerProviderController implements FlowServerProvider {

    @Autowired
    private FlowServiceImpl flowServiceImpl;

    @Resource
    private FlowDataService flowDataService;

    @Override
    public FlowReportView getFlowList(@RequestBody @Valid FlowRequest request) {
        return flowDataService.getFlowDataList(request);
    }

    @Override
    public FlowPageView getFlowPage(@RequestBody @Valid FlowRequest request) {
        return flowServiceImpl.getFlowPage(request);
    }

    @Override
    public FlowPageView getStoreList(@RequestBody @Valid FlowRequest request) {
        return flowServiceImpl.getStoreList(request);
    }
}
