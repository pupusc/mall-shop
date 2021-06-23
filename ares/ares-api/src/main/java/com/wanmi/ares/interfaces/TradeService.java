
package com.wanmi.ares.interfaces;

public class TradeService {

  public interface Iface {

    public java.util.List<com.wanmi.ares.view.trade.TradeView> getTradeList(com.wanmi.ares.request.flow.FlowRequest request);

    public com.wanmi.ares.view.trade.TradePageView getTradePage(com.wanmi.ares.request.flow.FlowRequest request);

    public com.wanmi.ares.view.trade.TradeView getOverview(com.wanmi.ares.request.flow.FlowRequest request);

    public com.wanmi.ares.view.trade.TradePageView getStoreTradePage(com.wanmi.ares.request.flow.FlowRequest request);

  }
}