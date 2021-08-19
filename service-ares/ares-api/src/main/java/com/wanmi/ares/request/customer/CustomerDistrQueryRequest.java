
package com.wanmi.ares.request.customer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerDistrQueryRequest{

  /**
   * 按日期周期查询，如果按自然月查询，此项可为空
   * 
   * @see com.wanmi.ares.enums.QueryDateCycle
   */
  public com.wanmi.ares.enums.QueryDateCycle dateCycle; // optional
  /**
   * 按自然月查询时，传入年和月，格式："yyyyMM",若按日期周期统计，此项可为空
   */
  public java.lang.String month; // optional
  /**
   * 商户id
   */
  public java.lang.String companyId; // required

}

