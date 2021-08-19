package com.wanmi.ares.report.trade.dao;

import com.wanmi.ares.report.base.dao.MyBatisBaseDao;
import com.wanmi.ares.report.trade.model.root.TradeMonth;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TradeMonthMapper继承基类
 */
@Repository
public interface TradeMonthMapper extends MyBatisBaseDao<TradeMonth, Long> {

    /**
     * 删除表数据
     */
    void deleteTable();

    /**
     * 按单月删除数据
     * @param month
     */
    void deleteRecentMonth(String month);

    /**
     * 按多月删除数据
     * @param months
     */
    void deleteRecentMonths(List months);

    /**
     * @param flowDataListRequest
     * @return
     */
    TradeMonth queryTradeMonthOne(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    List<TradeMonth> pageTradeStore(FlowDataListRequest flowDataListRequest);

    /**
     *
     * @param flowDataListRequest
     * @return
     */
    int pageTradeStoreCount(FlowDataListRequest flowDataListRequest);

}