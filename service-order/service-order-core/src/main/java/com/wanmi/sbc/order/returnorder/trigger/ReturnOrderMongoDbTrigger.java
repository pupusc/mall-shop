package com.wanmi.sbc.order.returnorder.trigger;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/12 3:23 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
@Slf4j
public class ReturnOrderMongoDbTrigger implements BeforeConvertCallback<ReturnOrder>{

    @Override
    public ReturnOrder onBeforeConvert(ReturnOrder entity, String collection) {
        log.info("ReturnOrderMongoDbTrigger.onBeforeConvert before ReturnOrder {}", JSON.toJSONString(entity));
        entity.setUpdateTime(LocalDateTime.now());
        log.info("ReturnOrderMongoDbTrigger.onBeforeConvert end ReturnOrder {}", JSON.toJSONString(entity));
        return entity;
    }
}
