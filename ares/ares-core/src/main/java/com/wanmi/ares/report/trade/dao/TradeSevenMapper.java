package com.wanmi.ares.report.trade.dao;

import com.wanmi.ares.report.base.dao.MyBatisBaseDao;
import com.wanmi.ares.report.trade.model.root.TradeSeven;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * TradeSevenMapper继承基类
 */
@Repository
public interface TradeSevenMapper extends MyBatisBaseDao<TradeSeven, Long> {

    /**
     * @param flowDataListRequest
     * @return
     */
    TradeSeven querySevenOnly(FlowDataListRequest flowDataListRequest);

    /**
     * @param flowDataListRequest
     * @return
     */
    List<TradeSeven> pageTradeSeven(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    List<TradeSeven> pageTradeStore(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    int pageTradeStoreCount(FlowDataListRequest flowDataListRequest);

    void deleteSevenTrade(LocalDate date);
}