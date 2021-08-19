package com.wanmi.ares.interfaces.customer;

public class CustomerReportExportService {

  /**
   * 用户报表导出服务
   */
  public interface Iface {

    public void exportCustomerReport(com.wanmi.ares.request.customer.CustomerReportExportRequest customerReportExportRequest);

  }
}