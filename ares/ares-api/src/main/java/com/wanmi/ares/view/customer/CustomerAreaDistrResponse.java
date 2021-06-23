
package com.wanmi.ares.view.customer;

import lombok.Data;

@Data
public class CustomerAreaDistrResponse {

  /**
   * 商户id
   */
  public java.lang.String companyId; // required
  public java.util.List<CustomerAreaDistrView> viewList; // required
}