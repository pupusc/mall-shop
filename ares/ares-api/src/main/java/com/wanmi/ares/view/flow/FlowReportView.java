
package com.wanmi.ares.view.flow;

import lombok.Data;

@Data
public class FlowReportView {

  public String id; // required
  public long totalPv; // required
  public long totalUv; // required
  public long skuTotalPv; // required
  public long skuTotalUv; // required
  public java.util.List<FlowView> flowList; // required
  public String date; // required
}