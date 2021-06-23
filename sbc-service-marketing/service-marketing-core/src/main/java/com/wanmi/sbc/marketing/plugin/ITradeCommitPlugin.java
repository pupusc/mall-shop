package com.wanmi.sbc.marketing.plugin;


import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;

/**
 * <p>订单提交营销插件</p>
 * Created by of628-wenzhi on 2018-03-09-下午5:34.
 */
public interface ITradeCommitPlugin {

    TradeMarketingResponse wraperMarketingFullInfo(TradeMarketingPluginRequest request);

}
