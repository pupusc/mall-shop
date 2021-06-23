
package com.wanmi.ares.view.customer;

import lombok.Data;

/**
 * 增专长趋势图
 */
@Data
public class CustomerGrowthTrendView {

  public java.lang.String xValue; // optional
  public long customerAllCount; // optional
  public long customerDayGrowthCount; // optional
  public long customerDayRegisterCount; // optional
}