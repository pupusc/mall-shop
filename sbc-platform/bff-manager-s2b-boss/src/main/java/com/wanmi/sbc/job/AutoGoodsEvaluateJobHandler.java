package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateQueryProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author lvzhenwei
 * @Description 商品自动评价定时任务
 * @Date 14:56 2019/4/10
 * @Param
 * @return
 **/
@Component
@Slf4j
@JobHandler(value="autoGoodsEvaluateJobHandler")
public class AutoGoodsEvaluateJobHandler extends IJobHandler {

    @Autowired
    private GoodsTobeEvaluateQueryProvider goodsTobeEvaluateQueryProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        goodsTobeEvaluateQueryProvider.autoGoodsEvaluate();
        return SUCCESS;
    }
}
