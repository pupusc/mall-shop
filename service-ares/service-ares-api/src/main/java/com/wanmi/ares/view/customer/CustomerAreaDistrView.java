
package com.wanmi.ares.view.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAreaDistrView {

  /**
   * 城市id
   */
  public java.lang.String cityId; // required
  /**
   * 当前城市下的客户数
   */
  public long num; // required
}