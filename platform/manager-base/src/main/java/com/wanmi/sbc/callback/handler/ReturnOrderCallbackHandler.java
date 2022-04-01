package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOnlineRefundRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnCodeResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.dto.RefundOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.RefundChannel;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.PayTradeRecordRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 5:21 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class ReturnOrderCallbackHandler implements CallbackHandler{


    @Autowired
    private PayProvider payProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;

    @Override
    public boolean support(String eventType) {
        return "aftersale_refund_success".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {
        log.info("ReturnOrderCallbackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();
        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常");
            return;
        }
//        if (!(returnOrderObj instanceof Map)) {
//            log.error("回调参数异常 returnOrderObj is not map");
//            return;
//        }
        Map<String, Object> returnOrderMap = (Map<String, Object>) returnOrderObj;
        String returnOrderId = returnOrderMap.get("out_aftersale_id").toString();
        String returnOrderTradeNo = returnOrderMap.get("aftersale_id").toString(); //退单流水

        log.info("ReturnOrderCallbackHandler handle out_aftersale_id: {}, aftersale_id: {}", returnOrderObj, returnOrderTradeNo);
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder()
                    .rid(returnOrderId).build()).getContext();

        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(returnOrder.getTid());
        BaseResponse<TradeGetByIdResponse> tradeGetByIdResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeGetByIdResponse tradeGetById = tradeGetByIdResponse.getContext();
        TradeVO tradeVO = tradeGetById.getTradeVO();

        RefundOrderByReturnCodeResponse refundOrder =
                refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(returnOrder.getId())).getContext();

        Operator operator = Operator.builder().ip(HttpUtil.getIpAddr()).adminId("-1").name("UNIONB2B")
                .platform(Platform.THIRD).build();

        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
        payTradeRecordRequest.setTradeNo(returnOrderTradeNo);
        payTradeRecordRequest.setBusinessId(returnOrderId);
        payTradeRecordRequest.setResult_code(WXPayConstants.SUCCESS);

        payTradeRecordRequest.setApplyPrice(returnOrder.getReturnPrice().getApplyPrice().divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        payTradeRecordRequest.setPracticalPrice(tradeVO.getTradePrice().getTotalPrice().divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        payProvider.wxPayCallBack(payTradeRecordRequest);
        returnOrderProvider.onlineRefund(
                ReturnOrderOnlineRefundRequest.builder().operator(operator)
                        .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                        .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());

        log.info("ReturnOrderCallbackHandler handle --> end cost: {} ms", System.currentTimeMillis() - beginTime);
//
//        //获取退单
//        ReturnOrderByIdRequest request = new ReturnOrderByIdRequest();
//        BaseResponse<ReturnOrderByIdResponse> returnOrderByIdResponseBaseResponse = returnOrderQueryProvider.getById(request);
//        ReturnOrderByIdResponse context = returnOrderByIdResponseBaseResponse.getContext();
//        if (context == null) {
//            return;
//        }
//
//        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
//        payTradeRecordRequest.setTradeNo(returnOrderTradeNo);
//        payTradeRecordRequest.setBusinessId(returnOrderIdObj);
//        payTradeRecordRequest.setResult_code(WXPayConstants.SUCCESS);
//        BigDecimal applyPrice = context.getReturnPrice().getApplyPrice().
//                divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
//        payTradeRecordRequest.setApplyPrice(applyPrice);
//        payTradeRecordRequest.setPracticalPrice(applyPrice);
//        payProvider.wxPayCallBack(payTradeRecordRequest);
//
//        Operator operator =
//                Operator.builder().adminId("1").name("system").account("system").ip("127.0.0.1").platform(Platform.PLATFORM).build();
//
//        returnOrderProvider.onlineRefund(
//                ReturnOrderOnlineRefundRequest.builder().operator(operator)
//                        .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
//                        .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());
    }
}
