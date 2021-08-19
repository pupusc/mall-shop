
package com.wanmi.ares.view.goods;

import lombok.Data;

@Data
public class GoodsReportView {

  public String id; // optional
  public long orderCount; // optional
  public double orderAmt; // optional
  public long orderNum; // optional
  public long payCount; // optional
  public long payNum; // optional
  public double payAmt; // optional
  public long returnOrderCount; // optional
  public double returnOrderAmt; // optional
  public long returnOrderNum; // optional
  public double orderConversion; // optional
}