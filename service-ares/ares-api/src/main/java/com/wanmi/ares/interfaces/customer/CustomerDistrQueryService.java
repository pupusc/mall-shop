package com.wanmi.ares.interfaces.customer;



public class CustomerDistrQueryService {

  public interface Iface {

    public com.wanmi.ares.view.customer.CustomerLevelDistrResponse queryLevelDistrView(com.wanmi.ares.request.customer.CustomerDistrQueryRequest request);

    public com.wanmi.ares.view.customer.CustomerAreaDistrResponse queryAreaDistrView(com.wanmi.ares.request.customer.CustomerDistrQueryRequest request);

    public int totalCount(com.wanmi.ares.request.customer.CustomerDistrQueryRequest request);

  }
}
