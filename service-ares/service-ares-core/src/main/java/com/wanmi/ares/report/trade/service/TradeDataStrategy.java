package com.wanmi.ares.report.trade.service;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeWeek;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.ParseData;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SaveFlowDataForMonthStrategy
 * @Description 保存流量统计数据采用策略模式--抽象业务处理器
 * @Author lvzhenwei
 * @Date 2019/8/26 10:19
 **/
@Component
public abstract class TradeDataStrategy {

    /**
     * @return com.wanmi.ares.enums.FlowDataType[]
     * @Author lvzhenwei
     * @Description 对应的策略值参数
     * @Date 15:35 2019/8/27
     * @Param []
     **/
    public abstract StatisticsDataType[] supports();

    /**
     * @return com.wanmi.ares.view.flow.FlowReportView
     * @Author lvzhenwei
     * @Description 根据查询条件获取流量统计数据信息
     * @Date 14:19 2019/8/27
     * @Param [flowDataListRequest]
     **/
    public abstract List<TradeView> getTradeData(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    public abstract TradeView getTradeView(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    public abstract TradePageView getTradePage(FlowDataListRequest flowDataListRequest);

    public abstract TradePageView getTradeStorePage(FlowDataListRequest flowDataListRequest);

    public TradeView buildTitleForWeek(TradeWeek tradeWeek){
        TradeView tradeView = ParseData.parseData(tradeWeek);
        tradeView.setTitle(DateUtil.format(tradeWeek.getWeekStartDate(), DateUtil.FMT_DATE_3)+"-"+DateUtil.format(tradeWeek.getWeekEndDate(), DateUtil.FMT_DATE_3));
        return tradeView;
    }

    public TradeView buildTitleForDayList(TradeBase tradeBase){
        TradeView tradeView = ParseData.parseData(tradeBase);
        tradeView.setTitle(DateUtil.formatLocalDate(tradeBase.getDate(),DateUtil.FMT_DATE_3)+DateUtil.getWeekStr(tradeBase.getDate()));
        return tradeView;
    }

    public TradeView buildTitleForDayPage(TradeBase tradeBase){
        TradeView tradeView = ParseData.parseData(tradeBase);
        tradeView.setTitle(DateUtil.formatLocalDate(tradeBase.getDate(),DateUtil.FMT_DATE_1));
        return tradeView;
    }

    public TradeView buildTitleForStorePage(TradeBase tradeBase){
        TradeView tradeView = ParseData.parseData(tradeBase);
        tradeView.setTitle(tradeBase.getStoreName());
        return tradeView;
    }

}
