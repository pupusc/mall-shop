package com.wanmi.sbc.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.SyncOrderDataRequest;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

@JobHandler(value = "SyncOrderHistoryJobHandler")
@Component
@Slf4j
public class SyncOrderHistoryJobHandler extends IJobHandler {
	
	@Autowired
	private TradeProvider tradeProvider;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		SyncOrderDataRequest syncOrderDataRequest = new SyncOrderDataRequest();
		tradeProvider.syncOrderDataAll(syncOrderDataRequest);
		return SUCCESS;
	}

}
