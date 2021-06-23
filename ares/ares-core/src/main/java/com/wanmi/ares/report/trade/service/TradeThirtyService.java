package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.report.flow.model.root.FlowThirty;
import com.wanmi.ares.report.flow.service.FlowThirtyService;
import com.wanmi.ares.report.trade.dao.TradeReportMapper;
import com.wanmi.ares.report.trade.dao.TradeThirtyMapper;
import com.wanmi.ares.report.trade.model.request.TradeCollect;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeThirty;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.request.flow.FlowThirtyRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.KsBeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-08-27 10:51
 */
@Service
public class TradeThirtyService {

    @Resource
    private TradeThirtyMapper tradeThirtyMapper;

    @Resource
    private TradeReportMapper tradeReportMapper;

    @Resource
    private TradeWeekService tradeWeekService;

    @Resource
    private FlowThirtyService flowThirtyService;

    @Transactional(rollbackFor = Exception.class)
    public void add(TradeThirty tradeThirty) {
        tradeThirtyMapper.insertSelective(tradeThirty);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addByBase(List<TradeBase> tradeBases, LocalDate date) {
        tradeBases.forEach(i->{
            this.computeUv(i, date);
            TradeThirty tradeThirty = KsBeanUtil.convert(i, TradeThirty.class);
            add(tradeThirty);
        });
    }

    private void computeUv(TradeBase tradeBase,LocalDate date){
        FlowThirtyRequest flowThirtyRequest= new FlowThirtyRequest();
        flowThirtyRequest.setFlowDate(date);
        flowThirtyRequest.setCompanyId(tradeBase.getCompanyId().toString());
        FlowThirty flowThirty = flowThirtyService.queryFlowThirtInfo(flowThirtyRequest);
        tradeBase.setDate(date);
        tradeBase.setCreateTime(LocalDateTime.now());
        //填充下单转化率 统计时间内，下单人数/访客数UV
        if (flowThirty == null || flowThirty.getUv() == null || flowThirty.getUv().equals(0L)){
            tradeBase.setUv(0L);
            tradeBase.setOrderConversion(new BigDecimal("100.00"));
            //填充全店转换率 统计时间内，付款人数/访客数UV
            tradeBase.setAllConversion(new BigDecimal("100.00"));
        }else {
            tradeBase.setUv(flowThirty.getUv());
            tradeBase.setOrderConversion(new BigDecimal(tradeBase.getOrderUserNum())
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(flowThirty.getUv()), 2, RoundingMode.HALF_UP));
            //填充全店转换率 统计时间内，付款人数/访客数UV
            tradeBase.setAllConversion(new BigDecimal(tradeBase.getPayUserNum())
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(flowThirty.getUv()), 2, RoundingMode.HALF_UP));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void reduceTradeBase(LocalDate date){
        //删除最近三十天报表
        tradeThirtyMapper.deleteThirtyTrade(date);
        //生成最近三十天报表
        TradeCollect tradeCollect = DateUtil.computeDateIntervalThirtyDay(date);
        List<TradeBase> tradeBaseThirtyDay = tradeReportMapper.collectTrade(tradeCollect);
        TradeBase tradeBase = tradeReportMapper.collectAllTrade(tradeCollect);
        tradeBaseThirtyDay.add(tradeBase);
        this.addByBase(tradeBaseThirtyDay,date);
        tradeWeekService.reduceTradeBase(DateUtil.getWeekLastDayTrade(tradeCollect.getBeginDate(), tradeCollect.getEndDate()), date, 0,null);
    }

    public TradeThirty queryTradeThirtyOne(FlowDataListRequest flowDataListRequest){
        return tradeThirtyMapper.queryTradeThirtyOne(flowDataListRequest);
    }

    public List<TradeThirty> pageTradeStore(FlowDataListRequest flowDataListRequest){
//        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum()-1);
        return tradeThirtyMapper.pageTradeStore(flowDataListRequest);
    }

    public int pageTradeStoreCount(FlowDataListRequest flowDataListRequest){
        return tradeThirtyMapper.pageTradeStoreCount(flowDataListRequest);
    }
}
