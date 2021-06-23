package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.flow.service.FlowMonthService;
import com.wanmi.ares.report.trade.dao.TradeMonthMapper;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeMonth;
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
public class TradeMonthService extends ComputerUvHandler {

    @Resource
    private TradeMonthMapper tradeMonthMapper;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private TradeWeekService tradeWeekService;

    @Resource
    private FlowMonthService flowMonthService;

    @Transactional(rollbackFor = Exception.class)
    public void add(TradeMonth tradeMonth) {
        tradeMonthMapper.insertSelective(tradeMonth);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addByBase(List<TradeBase> tradeBases, LocalDate date,String yearMonth) {
        tradeBases.forEach(i->{
            this.computeUv(i, date);
            TradeMonth tradeMonth = KsBeanUtil.convert(i, TradeMonth.class);
            tradeMonth.setMonth(yearMonth);
            add(tradeMonth);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(LocalDate date){
        //生成统计数据
        for (int i = 1; i < 7; i++) {
            //删除过期数据
            String oldYearMonth = DateUtil.yearMonth(date.minusMonths(i));
            tradeMonthMapper.deleteRecentMonth(oldYearMonth);
            TradeCollect tradeCollect = DateUtil.computeDateIntervalMonthDay(i, date);
            List<TradeBase> tradeBaseMon = tradeReportMapper.collectTrade(tradeCollect);
            TradeBase tradeBase = tradeReportMapper.collectAllTrade(tradeCollect);
            tradeBaseMon.add(tradeBase);
            String yearMonth = DateUtil.yearMonth(tradeCollect.getBeginDate());
            this.addByBase(tradeBaseMon, date, yearMonth);
            tradeWeekService.reduceTradeBase(DateUtil.getWeekLastDayTrade(tradeCollect.getBeginDate(), tradeCollect.getEndDate()), date, 1,yearMonth);
        }
    }

    public TradeMonth queryTradeMonthOne(FlowDataListRequest flowDataListRequest){
        return tradeMonthMapper.queryTradeMonthOne(flowDataListRequest);
    }

    public List<TradeMonth> pageTradeStore(FlowDataListRequest flowDataListRequest) {
//        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum()-1);
        return tradeMonthMapper.pageTradeStore(flowDataListRequest);
    }

    public int pageTradeStoreCount(FlowDataListRequest flowDataListRequest) {
        return tradeMonthMapper.pageTradeStoreCount(flowDataListRequest);
    }
}
