
package com.wanmi.ares.request.flow;

import lombok.Data;

@Data
public class FlowRequest {

  public String companyId = "0"; // required
  public String beginDate; // optional
  public String endDate; // optional
  public boolean isWeek  = false; // required
  public String sortName = "date"; // optional
  /**
   * @see com.wanmi.ares.enums.SortOrder
   */
  public com.wanmi.ares.enums.SortOrder sortOrder = com.wanmi.ares.enums.SortOrder.DESC; // optional
  public String selectType  = "0"; // optional
  public int pageNum = 1; // optional
  public int pageSize = 10; // optional
}