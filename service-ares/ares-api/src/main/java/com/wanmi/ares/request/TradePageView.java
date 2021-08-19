
package com.wanmi.ares.request;

import lombok.Data;

@Data
public class TradePageView {

  public java.util.List<TradeView> content; // required
  public boolean first; // required
  public boolean last; // required
  public int number; // required
  public int numberOfElements; // required
  public int size; // required
  public int totalElements; // required
  public int totalPages; // required
}