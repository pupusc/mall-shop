package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordDelByTimeRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@JobHandler(value="priceAdjustJobHandler")
@Component
public class PriceAdjustJobHandler extends IJobHandler {

    @Autowired
    private PriceAdjustmentRecordProvider priceAdjustmentRecordProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //前一天
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        PriceAdjustmentRecordDelByTimeRequest request = PriceAdjustmentRecordDelByTimeRequest.builder().time(time).build();
        priceAdjustmentRecordProvider.deleteByTime(request);
        return SUCCESS;
    }
}
