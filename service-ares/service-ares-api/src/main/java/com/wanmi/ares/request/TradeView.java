
package com.wanmi.ares.request;

import lombok.Data;

@Data
public class TradeView {
  public String id; // required
  public long orderCount; // required
  public long orderNum; // required
  public double orderAmt; // required
  public long PayOrderCount; // required
  public long PayOrderNum; // required
  public double payOrderAmt; // required
  public double orderConversionRate; // required
  public double payOrderConversionRate; // required
  public double wholeStoreConversionRate; // required
  public double customerUnitPrice; // required
  public double everyUnitPrice; // required
  public long returnOrderCount; // required
  public long returnOrderNum; // required
  public long totalUv; // required
  public double returnOrderAmt; // required
  public String title; // required
}