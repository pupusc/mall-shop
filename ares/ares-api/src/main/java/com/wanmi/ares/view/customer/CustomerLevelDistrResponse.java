
package com.wanmi.ares.view.customer;

import lombok.Data;

@Data
public class CustomerLevelDistrResponse {

  /**
   * 客户总数
   */
  public long total; // required
  /**
   * 商户id
   */
  public java.lang.String companyId; // required
  /**
   * 视图集合
   */
  public java.util.List<CustomerLevelDistrView> viewList; // required
}