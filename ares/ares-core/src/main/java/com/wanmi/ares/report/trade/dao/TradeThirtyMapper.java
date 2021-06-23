package com.wanmi.ares.report.trade.dao;

import com.wanmi.ares.report.base.dao.MyBatisBaseDao;
import com.wanmi.ares.report.trade.model.root.TradeDay;
import com.wanmi.ares.report.trade.model.root.TradeThirty;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * TradeThirtyMapper继承基类
 */
@Repository
public interface TradeThirtyMapper extends MyBatisBaseDao<TradeThirty, Long> {

    /**
     * @param flowDataListRequest
     * @return
     */
    TradeThirty queryTradeThirtyOne(FlowDataListRequest flowDataListRequest);

    /**
     * @param flowDataListRequest
     * @return
     */
    List<TradeDay> pageTradeThirty(FlowDataListRequest flowDataListRequest);

    /**
     * @param flowDataListRequest
     * @return
     */
    List<TradeThirty> pageTradeStore(FlowDataListRequest flowDataListRequest);

    /**
     * @param flowDataListRequest
     * @return
     */
    int pageTradeStoreCount(FlowDataListRequest flowDataListRequest);

    void deleteThirtyTrade(LocalDate date);
}