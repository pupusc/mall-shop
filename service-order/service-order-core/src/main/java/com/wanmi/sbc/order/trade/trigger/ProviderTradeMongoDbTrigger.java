package com.wanmi.sbc.order.trade.trigger;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/9 2:08 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
@Slf4j
public class ProviderTradeMongoDbTrigger implements BeforeSaveCallback<ProviderTrade>, Ordered {

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public ProviderTrade onBeforeSave(ProviderTrade entity, Document document, String collection) {
        log.info("TradeMongoDbTrigger.onBeforeSave ProviderTrade {}", JSON.toJSONString(entity));
        if (entity.getTradeState() != null) {
            entity.getTradeState().setModifyTime(LocalDateTime.now());
        }
        return entity;
    }
}
