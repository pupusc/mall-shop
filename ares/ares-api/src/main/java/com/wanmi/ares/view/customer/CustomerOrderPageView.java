
package com.wanmi.ares.view.customer;

import lombok.Data;

/**
 * 客户订单查询返回结果集
 *
 */
@Data
public class CustomerOrderPageView {

  public int current; // required
  public long total; // required
  public java.util.List<CustomerOrderView> CustomerOrderViewList; // required
}