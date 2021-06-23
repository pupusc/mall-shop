
package com.wanmi.ares.view.customer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerLevelDistrView {

  /**
   * 客户等级id
   */
  public java.lang.String levelId; // required
  /**
   * 客户等级名称
   */
  public java.lang.String levelName; // required
  /**
   * 当前等级下客户数所占总客户数的百分比(包含'%')
   */
  public java.lang.String centage; // required
  /**
   * 当前等级下客户人数
   */
  public long num; // required
}