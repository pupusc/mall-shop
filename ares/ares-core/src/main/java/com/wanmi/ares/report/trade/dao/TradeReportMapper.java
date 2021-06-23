package com.wanmi.ares.report.trade.dao;

import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.report.trade.model.request.TradeReportRequest;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by sunkun on 2017/10/18.
 */
@Mapper
public interface TradeReportMapper {

    void saveTrade(TradeReport report);

    List<TradeReport> queryTrade(TradeReportRequest tradeReportRequest);

    int queryTradeCount(TradeReportRequest tradeReportRequest);

    List<TradeReport> queryTradePage(TradeReportRequest tradeReportRequest);

    void deleteTrade(String id);

    int clearTradeReport(String date);

    List<TradeReport> queryStoreTradePage(TradeReportRequest tradeReportRequest);

    int countTradePageByStore(TradeReportRequest tradeReportRequest);

    List<TradeBase> collectTrade(TradeCollect tradeCollect);

    TradeBase collectAllTrade(TradeCollect tradeCollect);

    List<TradeBase> collectTradeStore(TradeCollect tradeCollect);

}
