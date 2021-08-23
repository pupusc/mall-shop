
package com.wanmi.ares.view.goods;

import lombok.Data;

@Data
public class GoodsReportPageView {

  public boolean first; // required
  public boolean last; // required
  public int number; // required
  public int numberOfElements; // required
  public int size; // required
  public int totalElements; // required
  public int totalPages; // required
  public java.util.List<GoodsReportView> goodsReportList; // required
  public java.util.List<GoodsSkuView> goodsSkuViewList; // required
  public java.util.List<GoodsCateView> goodsCateViewList; // required
  public java.util.List<GoodsBrandView> goodsBrandViewList; // required
}