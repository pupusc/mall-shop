package com.wanmi.ares.provider;

import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.view.flow.FlowPageView;
import com.wanmi.ares.view.flow.FlowReportView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 10:21
 */
@FeignClient(name = "${application.ares.name}", contextId = "FlowServerProvider")
public interface FlowServerProvider {
    @PostMapping("/ares/${application.ares.version}/flow/getFlowList")
    FlowReportView getFlowList(@RequestBody @Valid FlowRequest request);

    @PostMapping("/ares/${application.ares.version}/flow/getFlowPage")
    FlowPageView getFlowPage(@RequestBody @Valid FlowRequest request);

    @PostMapping("/ares/${application.ares.version}/flow/getStoreList")
    FlowPageView getStoreList(@RequestBody @Valid FlowRequest request);
}
