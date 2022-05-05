package com.wanmi.sbc.callback.handler;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.soybean.mall.wx.mini.enums.AfterSalesStateEnum;
import com.soybean.mall.wx.mini.order.bean.request.WxDealAftersaleRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.callback.service.CallBackCommonService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderNoRequest;
import com.wanmi.sbc.order.api.request.returnorder.RefundRejectRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderCancelRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderRejectRefundRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderTransferByUserIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.refund.RefundOrderByReturnOrderNoResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByConditionResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: {CreateTime=1650534683, aftersale_info={aftersale_id=4000000001576183, out_aftersale_id=}, Event=aftersale_user_cancel, ToUserName=gh_acd5c1ee4776, FromUserName=oj6KP5G00QNbhDzjk_ZSqw15E_9o, MsgType=event}
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/3/31 5:21 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Component
public class ReturnOrderCancelCallbackHandler implements CallbackHandler {

    @Autowired
    private PayOrderQueryProvider payOrderQueryProvider;

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private CallBackCommonService callBackCommonService;

    @Autowired
    private RefundOrderQueryProvider refundOrderQueryProvider;


    @Override
    public boolean support(String eventType) {
        return "aftersale_user_cancel".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("ReturnOrderCancelCallbackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();
        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常 param:{}", paramMap);
            return CommonHandlerUtil.FAIL;
        }

        Map<String, Object> returnOrderMap = (Map<String, Object>) returnOrderObj;

        /**
         * 测试数据
         *      String aftersaleId = "4000000001562176";
         */
        String aftersaleId = returnOrderMap.get("aftersale_id").toString(); //视频号 退单号

        //根据视频号的售后id获取 微信 售后详细信息
        WxDealAftersaleRequest wxDealAftersaleRequest = new WxDealAftersaleRequest();
        wxDealAftersaleRequest.setAftersaleId(Long.valueOf(aftersaleId));
        BaseResponse<WxDetailAfterSaleResponse> wxDetailAfterSaleResponseBaseResponse = wxOrderApiController.detailAfterSale(wxDealAftersaleRequest);
        WxDetailAfterSaleResponse context = wxDetailAfterSaleResponseBaseResponse.getContext();

        /**
         * 测试数据
         *      WxDetailAfterSaleResponse.AfterSalesOrder rr = callBackCommonService.test("O202204220216135401343", Long.valueOf(aftersaleId));
         *      context.setAfterSalesOrder(rr);
         */

        if (context.getAfterSalesOrder() == null) {
            log.error("ReturnOrderCancelCallbackHandler handler aftersaleId:{} 内容为空,不能取消售后订单", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }

        WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
        if (AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()) != AfterSalesStateEnum.AFTER_SALES_STATE_ONE) {
            log.error("ReturnOrderCancelCallbackHandler handler aftersaleId:{} 非创建售后状态，return", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }

        //根据视频号获取退单的详细信息
        ReturnOrderByConditionRequest returnOrderByConditionRequest = new ReturnOrderByConditionRequest();
        returnOrderByConditionRequest.setAftersaleId(aftersaleId);
        BaseResponse<ReturnOrderByConditionResponse> returnOrderByConditionResponseBaseResponse = returnOrderQueryProvider.listByCondition(returnOrderByConditionRequest);
        List<ReturnOrderVO> returnOrderList = returnOrderByConditionResponseBaseResponse.getContext().getReturnOrderList();
        ReturnOrderVO returnOrderVO = callBackCommonService.getValidReturnOrderVo(returnOrderList);
        if (returnOrderVO == null) {
            log.error("ReturnOrderCancelCallbackHandler handler aftersaleId:{} 获取退单为空,不能取消售后订单", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }

        log.info("ReturnOrderCancelCallbackHandler handler aftersaleId:{} 返回的退单为：{}", aftersaleId, JSON.toJSONString(returnOrderVO));

        //保证订单已经支付
        String orderId = returnOrderVO.getTid();
        BaseResponse<FindPayOrderResponse> response =
                payOrderQueryProvider.findPayOrder(FindPayOrderRequest.builder().value(orderId).build());
        FindPayOrderResponse payOrderResponse = response.getContext();
        if (Objects.isNull(payOrderResponse) || Objects.isNull(payOrderResponse.getPayOrderStatus()) || payOrderResponse.getPayOrderStatus() != PayOrderStatus.PAYED) {
            log.error("ReturnOrderCreateCallbackHandler handler orderId:{} aftersaleId: {} 未支付，无法取消售后", orderId, aftersaleId);
            return CommonHandlerUtil.FAIL;
        }

        // 查询退款单
        RefundOrderByReturnOrderNoResponse refundOrderByReturnCodeResponse = refundOrderQueryProvider.getByReturnOrderNo(new RefundOrderByReturnOrderNoRequest(returnOrderVO.getId())).getContext();


        Operator operator = callBackCommonService.packOperator(returnOrderVO);
        BaseResponse baseResponse = null;
        if (returnOrderVO.getReturnFlowState() == ReturnFlowState.INIT) {
            ReturnOrderCancelRequest returnOrderCancelRequest = new ReturnOrderCancelRequest();
            returnOrderCancelRequest.setRid(returnOrderVO.getId());
            returnOrderCancelRequest.setRemark("用户取消");
            returnOrderCancelRequest.setOperator(operator);
            returnOrderCancelRequest.setMessageSource(true);
            baseResponse = returnOrderProvider.cancel(returnOrderCancelRequest);
        } else if (returnOrderVO.getReturnFlowState() == ReturnFlowState.AUDIT && refundOrderByReturnCodeResponse != null && refundOrderByReturnCodeResponse.getRefundStatus() == RefundStatus.APPLY) {
            //表示运营取消订单
            //1、做标记、2作废
            RefundRejectRequest refundRejectRequest = new RefundRejectRequest();
            refundRejectRequest.setRid(returnOrderVO.getId());
            refundRejectRequest.setReturnReanson("用户强制取消");
            refundRejectRequest.setForceReject(1);
            baseResponse = returnOrderProvider.refundReject(refundRejectRequest);

        } else if (returnOrderVO.getReturnFlowState() == ReturnFlowState.AUDIT) {
            ReturnOrderRejectRefundRequest returnOrderRejectRefundRequest = new ReturnOrderRejectRefundRequest();
            returnOrderRejectRefundRequest.setRid(returnOrderVO.getId());
            returnOrderRejectRefundRequest.setReason("用户主动取消");
            returnOrderRejectRefundRequest.setOperator(operator);
            baseResponse = returnOrderProvider.rejectRefund(returnOrderRejectRefundRequest);
        } else {
            log.error("ReturnOrderCancelCallbackHandler  orderId:{} aftersaleId:{} returnOrderId:{} 该售后订单状态不会处理"
                , returnOrderVO.getTid(), aftersaleId, returnOrderVO.getId());
        }



        log.info("ReturnOrderCancelCallbackHandler  orderId:{} aftersaleId:{} returnOrderId:{} handle result:{} --> end cost: {} ms",
                returnOrderVO.getTid(), aftersaleId, returnOrderVO.getId(),
                JSON.toJSONString(baseResponse), System.currentTimeMillis() - beginTime);
        return CommonHandlerUtil.SUCCESS;
    }
}
