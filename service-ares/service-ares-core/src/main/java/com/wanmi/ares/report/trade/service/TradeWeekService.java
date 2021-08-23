package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.flow.service.FlowWeekService;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.dao.TradeWeekMapper;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeWeek;
import com.wanmi.ares.request.flow.FlowDataListRequest;
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
public class TradeWeekService extends ComputerUvHandler{

    @Resource
    private TradeWeekMapper tradeWeekMapper;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private FlowWeekService flowWeekService;

    @Transactional(rollbackFor = Exception.class)
    public void add(TradeWeek tradeWeek) {
        tradeWeekMapper.insertSelective(tradeWeek);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addByBase(List<TradeBase> tradeBases, LocalDate date,Integer type,String yearMonth,TradeCollect tradeCollect) {
        tradeBases.forEach(i->{
            this.computeUv(i, date);
            TradeWeek tradeWeek = KsBeanUtil.convert(i, TradeWeek.class);
            tradeWeek.setMonth(yearMonth);
            tradeWeek.setType(type);
            tradeWeek.setWeekStartDate(tradeCollect.getBeginDate());
            tradeWeek.setWeekEndDate(tradeCollect.getEndDate());
            add(tradeWeek);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(LocalDate date,TradeCollect tradeCollect,Integer type,String yearMonth){
        //生成最近三十天报表
        List<TradeBase> tradeBaseThirtyDay = tradeReportMapper.collectTrade(tradeCollect);
        TradeBase tradeBase = tradeReportMapper.collectAllTrade(tradeCollect);
        tradeBaseThirtyDay.add(tradeBase);
        this.addByBase(tradeBaseThirtyDay,date,type,yearMonth,tradeCollect);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(List<TradeCollect> tradeCollects, LocalDate date,Integer type,String yearMonth){
        //删除最近三十天周数据
        if(type.equals(0)){
            tradeWeekMapper.deleteThirtyWeek(type);
        }

        //删除最近三十天周数据
        if(type.equals(1)){
            tradeWeekMapper.deleteMonthWeek(yearMonth);
        }

        tradeCollects.forEach(i->{
            this.reduceTradeBase(date, i, type, yearMonth);
        });
    }

    public List<TradeWeek> queryWeek(FlowDataListRequest flowDataListRequest){
        return tradeWeekMapper.queryWeek(flowDataListRequest);
    }
}
