package com.wanmi.sbc.customer.provider.impl.storeevaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateStatisticsProvider;
import com.wanmi.sbc.customer.storeevaluate.service.StoreEvaluateStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liutao
 * @date 2019/2/27 11:15 AM
 */
@RestController
@Validated
public class StoreEvaluateStatisticsController implements StoreEvaluateStatisticsProvider {

    @Autowired
    private StoreEvaluateStatisticsService storeEvaluateStatisticsService;

    /**
     *
     * @return
     */
    @Override
    public BaseResponse statistics() {
        storeEvaluateStatisticsService.statistics();
        return  BaseResponse.SUCCESSFUL();
    }
}
