package com.wanmi.ares.interfaces;

public class FlowService {

  public interface Iface {

    public com.wanmi.ares.view.flow.FlowReportView getFlowList(com.wanmi.ares.request.flow.FlowRequest request);

    public com.wanmi.ares.view.flow.FlowPageView getFlowPage(com.wanmi.ares.request.flow.FlowRequest request);

    public com.wanmi.ares.view.flow.FlowPageView getStoreList(com.wanmi.ares.request.flow.FlowRequest request);

  }
}