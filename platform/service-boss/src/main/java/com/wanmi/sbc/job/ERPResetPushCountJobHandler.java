package com.wanmi.sbc.job;

import com.wanmi.sbc.order.api.provider.trade.ProviderTradeProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeErpRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuxia
 * @description 重置erp推送次数
 */
@JobHandler(value = "erpResetPushCountJobHandler")
@Component
@Slf4j
public class ERPResetPushCountJobHandler extends IJobHandler {

    @Autowired
    private ProviderTradeProvider providerTradeProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("=====重置订单推送次数开始======");
        providerTradeProvider.resetPushCount(ProviderTradeErpRequest.builder().ptid(param).build());
        log.info("=====重置订单推送次数结束======");
        return SUCCESS;
    }

}