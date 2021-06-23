
package com.wanmi.ares.request.customer;

import lombok.Data;

/**
 * 趋势图统计请求参数
 *
 */
@Data
public class CustomerTrendQueryRequest{

  public boolean weekly; // required
  /**
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle queryDateCycle; // optional
  public java.lang.String month; // optional
  public java.lang.String companyInfoId; // optional
}