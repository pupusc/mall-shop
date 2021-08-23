package com.wanmi.ares.interfaces.customer;

public class CustomerGrowthReportService {

  /**
   * *用户增长服务*
   */
  public interface Iface {

    public com.wanmi.ares.view.customer.CustomerGrowthPageView queryCustomerGrouthList(com.wanmi.ares.request.customer.CustomerGrowthReportRequest customerGrowthReportRequest);

    public java.util.List<com.wanmi.ares.view.customer.CustomerGrowthTrendView> queryCustomerTrendList(com.wanmi.ares.request.customer.CustomerTrendQueryRequest customerTrendQueryRequest);

  }

}
