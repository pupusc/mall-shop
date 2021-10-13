package com.wanmi.sbc.order.trade.trigger;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
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
public class ProviderTradeMongoDbTrigger implements BeforeConvertCallback<ProviderTrade> {

    @Override
    public ProviderTrade onBeforeConvert(ProviderTrade entity, String collection) {
        log.info("TradeMongoDbTrigger.onBeforeConvert before ProviderTrade {}", JSON.toJSONString(entity));
//        if (entity.getTradeState() != null) {
//            entity.getTradeState().setModifyTime(LocalDateTime.now());
//        }
        entity.setUpdateTime(LocalDateTime.now());
        log.info("TradeMongoDbTrigger.onBeforeConvert end ProviderTrade {}", JSON.toJSONString(entity));
        return entity;
    }
}
