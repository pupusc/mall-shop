
package com.wanmi.ares.request.customer;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求参数
 */
@Data
@Accessors(chain = true)
public class CustomerGrowthReportRequest {

  /**
   * 
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle dateCycle; // optional
  /**
   * 
   * @see com.wanmi.ares.base.SortType
   */
  public com.wanmi.ares.base.SortType sortType; // optional
  public java.lang.String sortField; // optional
  public int pageNum; // required
  public int pageSize; // required
  public java.lang.String companyId; // optional
  public java.lang.String startDate; // optional
  public java.lang.String enDate; // optional
  public java.lang.String month; // optional
  public java.lang.String sortTypeText; // optional

}

