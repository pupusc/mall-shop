package com.wanmi.sbc.job;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.common.GoodsRedisProvider;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * tag存redis
 */

@JobHandler(value = "goodsRedisHandler")
@Component
@Slf4j
public class GoodsRedisHandler extends IJobHandler {

    @Autowired
    private GoodsRedisProvider goodsRedisProvider;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        try {
            goodsRedisProvider.refreshRedis();
            return SUCCESS;
        } catch (RuntimeException e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }
}