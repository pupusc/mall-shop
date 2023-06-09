package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soybean.common.enums.PayAccountEnums;
import com.soybean.mall.wx.mini.order.bean.request.WxDealAftersaleRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.callback.service.CallBackCommonService;
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
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByConditionResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.dto.RefundOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.RefundChannel;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.ChannelItemByGatewayRequest;
import com.wanmi.sbc.pay.api.request.ChannelItemSaveRequest;
import com.wanmi.sbc.pay.api.request.PayTradeRecordRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.response.PayChannelItemListResponse;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 5:21 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class ReturnOrderPayCallbackHandler implements CallbackHandler{


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

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private CallBackCommonService callBackCommonService;

    @Override
    public boolean support(String eventType) {
        return "aftersale_refund_success".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("ReturnOrderPayCallbackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();
        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常 param:{}", paramMap);
            return CommonHandlerUtil.FAIL;
        }
//        if (!(returnOrderObj instanceof Map)) {
//            log.error("回调参数异常 returnOrderObj is not map");
//            return;
//        }
        Map<String, Object> returnOrderMap = (Map<String, Object>) returnOrderObj;
//        String returnOrderId = returnOrderMap.get("out_aftersale_id").toString();
        String aftersaleId = returnOrderMap.get("aftersale_id").toString(); //退单流水

        log.info("ReturnOrderPayCallbackHandler handle aftersale_id: {}", aftersaleId);

        //根据视频号的售后id获取 微信 售后详细信息
        WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
        wxDealAftersaleRequest.setAftersaleId(Long.valueOf(aftersaleId));
        BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
        WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();
        if (context.getAfterSalesOrder() == null) {
            log.error("ReturnOrderPayCallbackHandler handler aftersaleId:{} 获取守候单为空售后订单", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }


        ReturnOrderByConditionRequest returnOrderByConditionRequest = new ReturnOrderByConditionRequest();
        returnOrderByConditionRequest.setAftersaleId(aftersaleId);
        BaseResponse<ReturnOrderByConditionResponse> returnOrderByConditionResponseBaseResponse = returnOrderQueryProvider.listByCondition(returnOrderByConditionRequest);
        List<ReturnOrderVO> returnOrderList = returnOrderByConditionResponseBaseResponse.getContext().getReturnOrderList();
        ReturnOrderVO returnOrderVO = callBackCommonService.getValidReturnOrderVo(returnOrderList);
        if (returnOrderVO == null) {
            log.error("ReturnOrderPayCallbackHandler handler aftersaleId:{} 获取退单为空,完成售后退款", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }

        log.info("ReturnOrderPayCallbackHandler handler aftersaleId:{} 返回的退单为：{}", aftersaleId, JSON.toJSONString(returnOrderVO));



        Long channelId = 16L;
        ChannelItemByGatewayRequest channelItemByGatewayRequest = new ChannelItemByGatewayRequest();
        channelItemByGatewayRequest.setGatewayName(PayGatewayEnum.WECHAT);
        PayChannelItemListResponse payChannelItemListResponse =
                payQueryProvider.listChannelItemByGatewayName(channelItemByGatewayRequest).getContext();
        List<PayChannelItemVO> payChannelItemVOList =
                payChannelItemListResponse.getPayChannelItemVOList();
        ChannelItemSaveRequest channelItemSaveRequest = new ChannelItemSaveRequest();
        channelItemSaveRequest.setCode("wx_app");
        for (PayChannelItemVO payChannelItemParam : payChannelItemVOList) {
            if (channelItemSaveRequest.getCode().equals(payChannelItemParam.getCode())) {
                //更新支付项
                channelId = payChannelItemParam.getId();
            }
        }

        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(returnOrderVO.getTid());
        BaseResponse<TradeGetByIdResponse> tradeGetByIdResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeGetByIdResponse tradeGetById = tradeGetByIdResponse.getContext();
        TradeVO tradeVO = tradeGetById.getTradeVO();

        RefundOrderByReturnCodeResponse refundOrder =
                refundOrderQueryProvider.getByReturnOrderCode(new RefundOrderByReturnOrderCodeRequest(returnOrderVO.getId())).getContext();

        Operator operator = Operator.builder().ip("127.0.0.0").adminId("-1").name("UNIONB2B")
                .platform(Platform.THIRD).build();

        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
        payTradeRecordRequest.setTradeNo(aftersaleId);
        payTradeRecordRequest.setBusinessId(returnOrderVO.getId());
        payTradeRecordRequest.setResult_code(WXPayConstants.SUCCESS);
        payTradeRecordRequest.setChannelItemId(channelId);
        payTradeRecordRequest.setAppId(PayAccountEnums.WX_MINI_PROGRAM_VIDEO.getCode());
        payTradeRecordRequest.setApplyPrice(returnOrderVO.getReturnPrice().getApplyPrice().divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        payTradeRecordRequest.setPracticalPrice(tradeVO.getTradePrice().getTotalPrice().divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        payTradeRecordRequest.setTradeType(TradeType.REFUND);
        payProvider.wxPayCallBack(payTradeRecordRequest);
        BaseResponse baseResponse = returnOrderProvider.onlineRefund(
                ReturnOrderOnlineRefundRequest.builder().operator(operator)
                        .returnOrder(KsBeanUtil.convert(returnOrderVO, ReturnOrderDTO.class))
                        .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());

        log.info("ReturnOrderCallbackHandler  orderId:{} aftersaleId:{} returnOrderId:{} handle result:{} --> end cost: {} ms",
                returnOrderVO.getTid(), aftersaleId, returnOrderVO.getId(), JSON.toJSONString(baseResponse),
                System.currentTimeMillis() - beginTime);
        return CommonHandlerUtil.SUCCESS;
    }
}
