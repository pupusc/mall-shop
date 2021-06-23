package com.wanmi.sbc.job;

import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateStatisticsProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 店铺评价统计
 *
 * @author liutao 2019-02-27
 */
@JobHandler(value="storeEvaluateStatisticsHandler")
@Component
@Slf4j
public class StoreEvaluateStatisticsHandler extends IJobHandler {

    @Autowired
    private StoreEvaluateStatisticsProvider storeEvaluateStatisticsProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        storeEvaluateStatisticsProvider.statistics();
        return SUCCESS;
    }

}
