
package com.wanmi.ares.view.flow;

import lombok.Data;

@Data
public class FlowPageView {

  public java.util.List<FlowView> content; // required
  public boolean first; // required
  public boolean last; // required
  public int number; // required
  public int numberOfElements; // required
  public int size; // required
  public int totalElements; // required
  public int totalPages; // required
}