
package com.wanmi.ares.view.employee;

import lombok.Data;

/**
 * 业务员获客报表视图
 *
 */
@Data
public class EmployeeClientView {

  /**
   * 业务员id
   */
  public String employeeId; // required
  /**
   * 业务员名称
   */
  public String employeeName; // required
  /**
   * 客户总数
   */
  public long total; // required
  /**
   * 客户新增数
   */
  public long newlyNum; // required
}