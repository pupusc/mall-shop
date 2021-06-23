package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.flow.service.FlowSevenService;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.dao.TradeSevenMapper;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeSeven;
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
public class TradeSevenService extends ComputerUvHandler{

    @Resource
    private TradeSevenMapper tradeSevenMapper;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private FlowSevenService flowSevenService;

    @Transactional(rollbackFor = Exception.class)
    public void add(TradeSeven tradeSeven) {
        tradeSevenMapper.insertSelective(tradeSeven);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addByBase(List<TradeBase> tradeBases,LocalDate date) {
        tradeBases.forEach(i->{
            this.computeUv(i,date);
            TradeSeven tradeSeven = KsBeanUtil.convert(i, TradeSeven.class);
            add(tradeSeven);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(LocalDate date){
        //删除七天报表
        tradeSevenMapper.deleteSevenTrade(date);
        //生成七天报表
        List<TradeBase> tradeBaseSeven = tradeReportMapper.collectTrade(DateUtil.computeDateIntervalSeven(date));
        TradeBase tradeBase = tradeReportMapper.collectAllTrade(DateUtil.computeDateIntervalSeven(date));
        tradeBaseSeven.add(tradeBase);
        this.addByBase(tradeBaseSeven,date);
    }

    public TradeSeven queryTradeSevenOne(FlowDataListRequest flowDataListRequest){
        return tradeSevenMapper.querySevenOnly(flowDataListRequest);
    }

    public List<TradeSeven> pageTradeStore(FlowDataListRequest flowDataListRequest) {
//        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum()-1);
        return tradeSevenMapper.pageTradeStore(flowDataListRequest);
    }

    public int pageTradeStoreCount(FlowDataListRequest flowDataListRequest) {
        return tradeSevenMapper.pageTradeStoreCount(flowDataListRequest);
    }
}
