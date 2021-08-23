
package com.wanmi.ares.view.customer;

import lombok.Data;

@Data
public class CustomerGrowthReportView {

  public java.lang.String id; // optional
  /**
   * 日期 yyyy-mm-dd
   */
  public java.lang.String baseDate; // optional
  /**
   * 客户总数
   */
  public long customerAllCount; // optional
  /**
   * 客户新增数
   */
  public long customerDayGrowthCount; // optional
  /**
   * 注册客户数
   */
  public long customerDayRegisterCount; // optional
  /**
   * 店铺id
   */
  public java.lang.String companyId; // optional
  /**
   * 创建时间
   */
  public java.lang.String createDate; // optional
}