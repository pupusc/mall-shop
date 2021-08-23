package com.wanmi.ares.interfaces.customer;

public class CustomerGrowthReportExportService {

  /**
   * 用户增长报表导出服务
   * 
   */
  public interface Iface {

    public void exportCustomerGrowthReport(com.wanmi.ares.request.customer.CustomerReportExportRequest customerReportExportRequest);

  }
}
