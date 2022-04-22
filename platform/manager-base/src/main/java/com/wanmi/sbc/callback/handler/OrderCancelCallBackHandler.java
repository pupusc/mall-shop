package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeCancelRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.WxTradePayCallBackRequest;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/22 12:11 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Component
@Slf4j
public class OrderCancelCallBackHandler implements CallbackHandler{

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;


    @Override
    public boolean support(String eventType) {
        return "open_product_order_cancel".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("OrderCancelCallBackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();

        Map<String, String> orderResult = (Map<String, String>) paramMap.get("order_info");
        String outOrderId = orderResult.get("out_order_id");
        String orderId = orderResult.get("order_id");
        //获取订单信息

        //根据订单号获取订单详细信息
        TradeVO tradeVo = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(orderId).build()).getContext().getTradeVO();
        if (CollectionUtils.isEmpty(tradeVo.getTradeItems())) {
            log.error("OrderCancelCallBackHandler handle orderId:{} outOrderId:{} 订单不存在 ", orderId, outOrderId);
            return "fail";
        }
        if (tradeVo.getTradeState().getPayState() == PayState.PAID) {
            log.error("OrderCancelCallBackHandler handle orderId:{} outOrderId:{} 订单已经支付、取消失败 ", orderId, outOrderId);
            return "fail";
        }

        Operator operator = new Operator();
        operator.setUserId(tradeVo.getBuyer().getId());
        operator.setName(tradeVo.getBuyer().getName());
        operator.setStoreId(tradeVo.getSupplier().getStoreId().toString());
        operator.setIp("127.0.0.1");
        operator.setAccount(tradeVo.getBuyer().getAccount());
        operator.setCompanyInfoId(tradeVo.getSupplier().getSupplierId());

        TradeCancelRequest tradeCancelRequest = new TradeCancelRequest();
        tradeCancelRequest.setTid(outOrderId);
        tradeCancelRequest.setOperator(operator);
        BaseResponse cancel = tradeProvider.cancel(tradeCancelRequest);

        log.info("OrderCancelCallBackHandler  orderId:{} outOrderId:{} handle result:{} --> end cost: {} ms",
                orderId, outOrderId, JSON.toJSONString(cancel), System.currentTimeMillis() - beginTime);
        return null;
    }
}
