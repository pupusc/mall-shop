
package com.wanmi.ares.request;

import lombok.Data;

@Data
public class TradeRequest {

  public String companyId; // required
  public String beginDate; // required
  public String endDate; // required
  public boolean isWeek; // required
}