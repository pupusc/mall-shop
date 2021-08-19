package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.flow.service.FlowReportService;
import com.wanmi.ares.report.trade.dao.TradeDayMapper;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeDay;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.KsBeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-08-27 10:51
 */
@Service
public class TradeDayService extends ComputerUvHandler{

    @Resource
    private TradeDayMapper tradeDayMapper;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private FlowReportService flowReportService;

    @Transactional(rollbackFor = Exception.class)
    public void add(TradeDay tradeDay) {
        tradeDayMapper.insertSelective(tradeDay);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addByBase(List<TradeBase> tradeBases, LocalDate date) {
        tradeBases.forEach(i->{
            this.computeUv(i,date);
            TradeDay tradeDay = KsBeanUtil.convert(i, TradeDay.class);
            add(tradeDay);
        });
    }

    public List<TradeDay> queryTradeDay(FlowDataListRequest flowDataListRequest){
        return tradeDayMapper.queryDay(flowDataListRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(LocalDate date){
        //清空今日报表
        tradeDayMapper.deleteRecentDay(date);
        //生成今天报表
        TradeCollect tradeCollect = DateUtil.computeDateIntervalDay(date);
        List<TradeBase> tradeBaseDay = tradeReportMapper.collectTrade(tradeCollect);
        TradeBase tradeBase = tradeReportMapper.collectAllTrade(tradeCollect);
        tradeBaseDay.add(tradeBase);
        this.addByBase(tradeBaseDay, date);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceYesterdayTradeBase(LocalDate date){
        LocalDate yesterday = date.minusDays(1);
        //清空昨日报表
        tradeDayMapper.deleteRecentDay(yesterday);
        //生成昨日报表
        TradeCollect tradeCollect = DateUtil.computeDateIntervalDay(yesterday);
        List<TradeBase> tradeBaseDay = tradeReportMapper.collectTrade(tradeCollect);
        TradeBase tradeBase = tradeReportMapper.collectAllTrade(tradeCollect);
        tradeBaseDay.add(tradeBase);
        this.addByBase(tradeBaseDay, yesterday);
    }

    public List<TradeDay> pageTradeDay(FlowDataListRequest flowDataListRequest){
//        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum()-1);
        return tradeDayMapper.pageTradeDay(flowDataListRequest);
    }

    public int pageTradeDayCount(FlowDataListRequest flowDataListRequest){
        return tradeDayMapper.pageTradeDayCount(flowDataListRequest);
    }

    public List<TradeDay> pageTradeStore(FlowDataListRequest flowDataListRequest){
//        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum()-1);
        return tradeDayMapper.pageTradeStore(flowDataListRequest);
    }

    public int pageTradeStoreCount(FlowDataListRequest flowDataListRequest){
        return tradeDayMapper.pageTradeStoreCount(flowDataListRequest);
    }

    public TradeDay queryTradeDayOne(FlowDataListRequest flowDataListRequest){
        return tradeDayMapper.queryDayOne(flowDataListRequest);
    }

}
