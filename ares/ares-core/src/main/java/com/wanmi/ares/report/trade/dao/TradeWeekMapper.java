package com.wanmi.ares.report.trade.dao;

import com.wanmi.ares.report.base.dao.MyBatisBaseDao;
import com.wanmi.ares.report.trade.model.root.TradeWeek;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TradeWeekMapper继承基类
 */
@Repository
public interface TradeWeekMapper extends MyBatisBaseDao<TradeWeek, Long> {
    /**
     * @param flowDataListRequest
     * @return
     */
    List<TradeWeek> queryWeek(FlowDataListRequest flowDataListRequest);

    /**
     * @param type
     */
    void deleteThirtyWeek(Integer type);

    void deleteMonthWeek(String yearMonth);
}