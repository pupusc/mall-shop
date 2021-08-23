package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.trade.model.root.TradeDay;
import com.wanmi.ares.report.trade.model.root.TradeThirty;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.utils.ParseData;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SaveFlowDataForThirtyStrategy
 * @Description 定义最近30天统计数据的处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:26
 **/
@Component
public class TradeDataForThirtyStrategy extends TradeDataStrategy {

    @Resource
    private TradeDayService tradeDayService;

    @Resource
    private TradeThirtyService tradeThirtyService;

    @Override
    public StatisticsDataType[] supports() {
        return new StatisticsDataType[]{StatisticsDataType.THIRTY};
    }

    @Override
    public List<TradeView> getTradeData(FlowDataListRequest flowDataListRequest) {
        flowDataListRequest.setBeginDate(LocalDate.now().minusDays(30));
        flowDataListRequest.setEndDate(LocalDate.now());
        flowDataListRequest.setSortOrder(SortOrder.ASC);
        flowDataListRequest.setSortName("date");
        List<TradeDay> tradeDays = tradeDayService.queryTradeDay(flowDataListRequest);
        List<TradeView> collect = tradeDays.stream().map(this::buildTitleForDayList).collect(Collectors.toList());
        return collect;
    }

    @Override
    public TradeView getTradeView(FlowDataListRequest flowDataListRequest) {
        flowDataListRequest.setBeginDate(LocalDate.now());
        flowDataListRequest.setEndDate(LocalDate.now());
        TradeThirty tradeThirty = tradeThirtyService.queryTradeThirtyOne(flowDataListRequest);
        TradeView tradeView = ParseData.parseData(tradeThirty);
        return tradeView;
    }

    @Override
    public TradePageView getTradePage(FlowDataListRequest flowDataListRequest) {
        flowDataListRequest.setPageNum(flowDataListRequest.getPageNum() * flowDataListRequest.getPageSize() - flowDataListRequest.getPageSize());
        flowDataListRequest.setBeginDate(LocalDate.now().minusDays(30));
        flowDataListRequest.setEndDate(LocalDate.now());
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
        flowDataListRequest.setBeginDate(LocalDate.now().minusDays(30));
        flowDataListRequest.setEndDate(LocalDate.now());
        List<TradeThirty> tradeDays = tradeThirtyService.pageTradeStore(flowDataListRequest);
        int count = tradeThirtyService.pageTradeStoreCount(flowDataListRequest);
        List<TradeView> collect = tradeDays.stream().map(this::buildTitleForStorePage).collect(Collectors.toList());
        TradePageView tradePageView = new TradePageView();
        tradePageView.setContent(collect);
        tradePageView.setTotalElements(count);
        tradePageView.setTotalPages(count/flowDataListRequest.getPageSize()+1);
        tradePageView.setSize(flowDataListRequest.getPageSize());
        return tradePageView;
    }

}
