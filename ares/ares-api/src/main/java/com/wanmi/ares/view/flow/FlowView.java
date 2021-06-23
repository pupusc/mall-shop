
package com.wanmi.ares.view.flow;

import lombok.Data;

@Data
public class FlowView {

  public long totalPv; // required
  public long totalUv; // required
  public long skuTotalPv; // required
  public long skuTotalUv; // required
  public String date; // required
  public String title; // required
}