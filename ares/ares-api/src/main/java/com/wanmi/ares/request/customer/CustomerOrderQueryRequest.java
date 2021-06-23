
package com.wanmi.ares.request.customer;

import lombok.Data;

@Data
public class CustomerOrderQueryRequest {

  /**
   * 按日期周期查询，如果按自然月查询，此项可为空
   *
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle dateCycle; // optional
  public int pageNum; // required
  public int pageSize; // required
  public CustomerQueryType queryType; // required
  public String queryText; // optional
  /**
   * @see com.wanmi.ares.base.SortType
   */
  public com.wanmi.ares.base.SortType sortType; // optional
  public String sortField; // optional
  public String companyId; // optional
  public java.util.List<String> cityList; // optional
  public String sortTypeText; // optional
  public String month; // optional
  public String customerId; // optional
}