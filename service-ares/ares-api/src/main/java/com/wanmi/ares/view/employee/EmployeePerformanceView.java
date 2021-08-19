
package com.wanmi.ares.view.employee;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmployeePerformanceView {

  /**
   * 业务员id
   */
  public java.lang.String employeeId; // required
  /**
   * 业务员名称
   */
  public java.lang.String employeeName; // required
  /**
   * 下单总笔数
   */
  public long orderCount; // required
  /**
   * 下单总人数
   */
  public long customerCount; // required
  /**
   * 订单总金额
   */
  public double amount; // required
  /**
   * 付款总金额
   */
  public double payAmount; // required
  /**
   * 付款订单数
   */
  public long payCount; // required
  /**
   * 付款总人数
   */
  public long payCustomerCount; // required
  /**
   * 退单总笔数
   */
  public long returnCount; // required
  /**
   * 退单总金额
   */
  public double returnAmount; // required
  /**
   * 退单总人数
   */
  public long returnCustomerCount; // required
  /**
   * 笔单价
   */
  public double orderUnitPrice; // required
  /**
   * 客单价
   */
  public double customerUnitPrice; // required
}