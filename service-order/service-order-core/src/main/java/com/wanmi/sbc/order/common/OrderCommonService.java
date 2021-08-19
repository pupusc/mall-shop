package com.wanmi.sbc.order.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author: lq
 * @CreateTime:2019-08-16 15:34
 * @Description:todo
 */

@Component
public class OrderCommonService {

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询订单可退时间
     */
    public LocalDateTime queryReturnTime() {
        LocalDateTime returnTime = LocalDateTime.now();
        // 查询已完成订单允许申请退单天数配置
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        // 是否支持退货，不支持退货时，订单可退时间设置为当前时间
        if (Objects.nonNull(config)&& (config.getStatus() != 0)) {
            JSONObject content = JSON.parseObject(config.getContext());
            Long day = content.getObject("day", Long.class);
            returnTime = returnTime.plusDays(day);
        }
        return returnTime;
    }

    /**
     * 根据订单号或父订单号获取订单信息
     *
     * @param businessId 本地业务编号
     * @return 订单信息集合
     */
    public List<Trade> findTradesByBusinessId(String businessId) {
        TradeQueryRequest request = new TradeQueryRequest();
        if (businessId.startsWith(GeneratorService._PREFIX_TRADE_TAIL_ID)) {
            request.setTailOrderNo(businessId);
            return mongoTemplate.find(new Query(request.getWhereCriteria()), Trade.class);
        } else if (businessId.startsWith(GeneratorService._PREFIX_PARENT_TRADE_ID)) {
            request.setParentId(businessId);
            return mongoTemplate.find(new Query(request.getWhereCriteria()), Trade.class);
        } else if (businessId.startsWith(GeneratorService._PREFIX_TRADE_ID)) {
            return Collections.singletonList(mongoTemplate.findById(businessId, Trade.class));
        }
        return Collections.emptyList();
    }
}
