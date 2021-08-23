package com.wanmi.ares.interfaces;

public class GoodsService {

  public interface Iface {

    public com.wanmi.ares.view.goods.GoodsTotalView queryGoodsTotal(com.wanmi.ares.request.goods.GoodsReportRequest request);

    public com.wanmi.ares.view.goods.GoodsReportPageView querySkuReport(com.wanmi.ares.request.goods.GoodsReportRequest request);

    public com.wanmi.ares.view.goods.GoodsReportPageView queryCateReport(com.wanmi.ares.request.goods.GoodsReportRequest request);

    public com.wanmi.ares.view.goods.GoodsReportPageView queryBrandReport(com.wanmi.ares.request.goods.GoodsReportRequest request);

    public com.wanmi.ares.view.goods.GoodsReportPageView queryStoreCateReport(com.wanmi.ares.request.goods.GoodsReportRequest request);

  }
}