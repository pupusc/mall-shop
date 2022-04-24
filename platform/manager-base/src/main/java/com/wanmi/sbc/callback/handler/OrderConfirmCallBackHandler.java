package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.callback.service.CallBackCommonService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeConfirmReceiveRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * Description: 确认收货
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/23 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class OrderConfirmCallBackHandler implements CallbackHandler {


    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private CallBackCommonService callBackCommonService;

    @Override
    public boolean support(String eventType) {
        return "open_product_order_confirm".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("OrderConfirmCallBackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();

        Map<String, String> orderResult = (Map<String, String>) paramMap.get("order_info");
        String outOrderId = orderResult.get("out_order_id");
        String wxOrderId = orderResult.get("order_id");

        //根据订单号获取订单详细信息
        TradeVO tradeVo = tradeQueryProvider.getById(TradeGetByIdRequest.builder().tid(outOrderId).build()).getContext().getTradeVO();
        if (CollectionUtils.isEmpty(tradeVo.getTradeItems())) {
            log.error("OrderConfirmCallBackHandler handle wxOrderId:{} outOrderId:{} 订单不存在 ", wxOrderId, outOrderId);
            return CommonHandlerUtil.FAIL;
        }
        if (tradeVo.getTradeState().getPayState() == PayState.PAID) {
            log.error("OrderConfirmCallBackHandler handle wxOrderId:{} outOrderId:{} 订单已经支付、取消失败 ", wxOrderId, outOrderId);
            return CommonHandlerUtil.FAIL;
        }

        Operator operator = callBackCommonService.packOperator(tradeVo);
        TradeConfirmReceiveRequest tradeConfirmReceiveRequest = TradeConfirmReceiveRequest.builder().operator(operator).tid(tradeVo.getId()).build();
        BaseResponse baseResponse = tradeProvider.confirmReceive(tradeConfirmReceiveRequest);
        log.info("OrderConfirmCallBackHandler  wxOrderId:{} outOrderId:{} handle result:{} --> end cost: {} ms",
                wxOrderId, outOrderId, JSON.toJSONString(baseResponse), System.currentTimeMillis() - beginTime);
        return CommonHandlerUtil.SUCCESS;
    }
}
