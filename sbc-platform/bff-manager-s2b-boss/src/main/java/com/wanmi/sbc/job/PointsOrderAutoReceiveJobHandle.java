package com.wanmi.sbc.job;

import com.wanmi.sbc.order.api.provider.pointstrade.PointsTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeSettingProvider;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName PointsOrderAutoReceiveJobHandle
 * @Description 积分订单自动确认收货定时任务
 * @Author lvzhenwei
 * @Date 2019/5/28 9:51
 **/
@JobHandler(value = "pointsOrderAutoReceiveJobHandle")
@Component
@Slf4j
public class PointsOrderAutoReceiveJobHandle extends IJobHandler {

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private PointsTradeQueryProvider pointsTradeQueryProvider;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //订单自动确认收货
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_AUTO_RECEIVE);
        Integer autoReceive = auditQueryProvider
                .getTradeConfigByType(request).getContext().getStatus();
        //开关只有开的时候才执行
        if (Integer.valueOf(1).equals(autoReceive)) {
            pointsTradeQueryProvider.pointsOrderAutoReceive();
        }
        return SUCCESS;
    }
}
