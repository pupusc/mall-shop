
package com.wanmi.ares.view.employee;

import lombok.Data;

@Data
public class EmployeePerormanceResponse {

  /**
   * 结果集视图
   */
  public java.util.List<EmployeePerformanceView> viewList; // required
  /**
   * 商户id
   */
  public java.lang.String companyId; // required
  /**
   * 当前页数
   */
  public int pageNo; // required
  /**
   * 每页条数
   */
  public int pageSize; // required
  /**
   * 查询结果总数
   */
  public long count; // required
}