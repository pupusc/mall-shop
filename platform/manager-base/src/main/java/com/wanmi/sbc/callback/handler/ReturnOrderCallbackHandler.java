package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.refund.RefundOrderByReturnOrderCodeRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderOnlineRefundRequest;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByIdResponse;
import com.wanmi.sbc.order.bean.dto.RefundOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.request.PayTradeRecordRequest;
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
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Override
    public boolean support(String eventType) {
        return "aftersale_refund_success".equals(eventType);
    }

    @Override
    public void handle(Map<String, Object> paramMap) {

        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常");
            return;
        }
        JSONObject returnOrderObjJson = JSON.parseObject(returnOrderObj.toString());
        String returnOrderIdObj = returnOrderObjJson.getString("out_aftersale_id");
        String returnOrderTradeNo = returnOrderObjJson.getString("aftersale_id");


        //获取退单
        ReturnOrderByIdRequest request = new ReturnOrderByIdRequest();
        BaseResponse<ReturnOrderByIdResponse> returnOrderByIdResponseBaseResponse = returnOrderQueryProvider.getById(request);
        ReturnOrderByIdResponse context = returnOrderByIdResponseBaseResponse.getContext();
        if (context == null) {
            return;
        }

        PayTradeRecordRequest payTradeRecordRequest = new PayTradeRecordRequest();
        payTradeRecordRequest.setTradeNo(returnOrderTradeNo);
        payTradeRecordRequest.setBusinessId(returnOrderIdObj);
        payTradeRecordRequest.setResult_code(WXPayConstants.SUCCESS);
        BigDecimal applyPrice = context.getReturnPrice().getApplyPrice().
                divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
        payTradeRecordRequest.setApplyPrice(applyPrice);
        payTradeRecordRequest.setPracticalPrice(applyPrice);
        payProvider.wxPayCallBack(payTradeRecordRequest);

        returnOrderProvider.onlineRefund(
                ReturnOrderOnlineRefundRequest.builder().operator(operator)
                        .returnOrder(KsBeanUtil.convert(returnOrder, ReturnOrderDTO.class))
                        .refundOrder(KsBeanUtil.convert(refundOrder, RefundOrderDTO.class)).build());
    }
}
