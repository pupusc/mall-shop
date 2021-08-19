package com.wanmi.sbc.job;

import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @Author lvzhenwei
 * @Description 店铺服务自动评价定时任务
 * @Date 14:17 2019/4/9
 * @Param
 * @return
 **/
@Component
@Slf4j
@JobHandler(value="autoStoreEvaluateJobHandler")
public class AutoStoreEvaluateJobHandler extends IJobHandler {

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;

    @Override
    @TransactionalEventListener
    public ReturnT<String> execute(String s) throws Exception {
        storeTobeEvaluateQueryProvider.autoStoreEvaluate();
        return SUCCESS;
    }
}
