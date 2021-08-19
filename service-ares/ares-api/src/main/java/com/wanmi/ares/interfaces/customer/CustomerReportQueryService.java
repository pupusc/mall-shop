package com.wanmi.ares.interfaces.customer;

public class CustomerReportQueryService {

  public interface Iface {

    public com.wanmi.ares.view.customer.CustomerOrderPageView queryCustomerOrders(com.wanmi.ares.request.customer.CustomerOrderQueryRequest request);

  }
}