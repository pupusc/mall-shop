
package com.wanmi.ares.request.customer;

import lombok.Data;

/**
 * 导出
 *
 */
@Data
public class CustomerReportExportRequest {

  public java.lang.String startDate; // required
  public java.lang.String endDate; // required
  /**
   * @see CustomerQueryType
   */
  public CustomerQueryType queryType; // required
  public java.lang.String operator; // required
  public int start; // optional
  public int size; // optional
}