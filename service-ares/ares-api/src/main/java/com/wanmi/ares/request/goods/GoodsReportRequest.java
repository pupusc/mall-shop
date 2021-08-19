
package com.wanmi.ares.request.goods;

import lombok.Data;

@Data
public class GoodsReportRequest {

  /**
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle selectType; // optional
  public String dateStr; // optional
  public String keyword; // optional
  public String id; // optional
  public long pageNum; // optional
  public long pageSize; // optional
  /**
   * @see com.wanmi.ares.enums.SortOrder
   */
  public com.wanmi.ares.enums.SortOrder sortType; // optional
  public String companyId; // optional
  public int sortCol; // optional
}