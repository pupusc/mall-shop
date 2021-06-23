package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.trade.model.root.TradeDay;
import com.wanmi.ares.report.trade.model.root.TradeMonth;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.ParseData;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SaveFlowDataForMonthStrategy
 * @Description 定义按月统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:22
 **/
@Component
public class TradeDataForMonthStrategy extends TradeDataStrategy {

    @Resource
    private TradeDayService tradeDayService;

    @Resource
    private TradeMonthService tradeMonthService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.MONTH};
    }

    @Override
    public List<TradeView> getTradeData(FlowDataListRequest flowDataListRequest) {
        LocalDate beginDate = DateUtil.parse2Date(flowDataListRequest.getMonth() + "-01", DateUtil.FMT_DATE_1);
        LocalDate endDate = beginDate.plusDays(beginDate.lengthOfMonth() - 1);
        flowDataListRequest.setBeginDate(beginDate);
        flowDataListRequest.setEndDate(endDate);
        List<TradeDay> tradeDays = tradeDayService.queryTradeDay(flowDataListRequest);
        List<TradeView> collect = tradeDays.stream().map(this::buildTitleForDayList).collect(Collectors.toList());
        return collect;
    }

    @Override
    public TradeView getTradeView(FlowDataListRequest flowDataListRequest) {
        flowDataListRequest.setBeginDate(LocalDate.now());
        flowDataListRequest.setEndDate(LocalDate.now());
        TradeMonth tradeMonth = tradeMonthService.queryTradeMonthOne(flowDataListRequest);
        TradeView tradeView = ParseData.parseData(tradeMonth);
        return tradeView;
    }

    @Override
    public TradePageView getTradePage(FlowDataListRequest flowDataListRequest) {
        LocalDate beginDate = DateUtil.parse2Date(flowDataListRequest.getMonth() + "-01", DateUtil.FMT_DATE_1);
        LocalDate endDate = beginDate.plusDays(beginDate.lengthOfMonth() - 1);
        flowDataListRequest.setBeginDate(beginDate);
        flowDataListRequest.setEndDate(endDate);
        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum() * flowDataListRequest.getPageSize() - flowDataListRequest.getPageSize());
        List<TradeDay> tradeDays = tradeDayService.pageTradeDay(flowDataListRequest);
        int count = tradeDayService.pageTradeDayCount(flowDataListRequest);
        List<TradeView> collect = tradeDays.stream().map(this::buildTitleForDayPage).collect(Collectors.toList());
        TradePageView tradePageView = new TradePageView();
        tradePageView.setContent(collect);
        tradePageView.setTotalElements(count);
        tradePageView.setTotalPages(count/flowDataListRequest.getPageSize()+1);
        tradePageView.setSize(flowDataListRequest.getPageSize());
        return tradePageView;
    }

    @Override
    public TradePageView getTradeStorePage(FlowDataListRequest flowDataListRequest) {
        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum() * flowDataListRequest.getPageSize() - flowDataListRequest.getPageSize());
        List<TradeMonth> tradeMonths = tradeMonthService.pageTradeStore(flowDataListRequest);
        int count = tradeMonthService.pageTradeStoreCount(flowDataListRequest);
        List<TradeView> collect = tradeMonths.stream().map(this::buildTitleForStorePage).collect(Collectors.toList());
        TradePageView tradePageView = new TradePageView();
        tradePageView.setContent(collect);
        tradePageView.setTotalElements(count);
        tradePageView.setTotalPages(count/flowDataListRequest.getPageSize()+1);
        tradePageView.setSize(flowDataListRequest.getPageSize());
        return tradePageView;
    }

}
