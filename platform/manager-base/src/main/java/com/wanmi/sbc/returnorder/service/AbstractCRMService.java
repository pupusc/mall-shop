package com.wanmi.sbc.returnorder.service;

import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/1/6 9:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCRMService {

    @Autowired
    protected GuanyierpProvider guanyierpProvider;

    @Autowired
    protected ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    protected TradeQueryProvider tradeQueryProvider;

    /**
     * 管易云
     */
    @Value("${default.providerId}")
    protected String defaultProviderId;


}
