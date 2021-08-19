
package com.wanmi.ares.view.customer;

import lombok.Data;

/**
 * 用户增长视图
 *
 */
@Data
public class CustomerGrowthPageView {

  public int current; // required
  public long total; // required
  public java.util.List<CustomerGrowthReportView> grouthList; // required
}