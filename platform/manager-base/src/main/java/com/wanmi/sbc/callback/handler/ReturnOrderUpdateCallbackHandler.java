package com.wanmi.sbc.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.soybean.mall.wx.mini.enums.AfterSalesReasonEnum;
import com.soybean.mall.wx.mini.enums.AfterSalesStateEnum;
import com.soybean.mall.wx.mini.order.bean.request.WxDealAftersaleRequest;
import com.soybean.mall.wx.mini.order.bean.response.WxDetailAfterSaleResponse;
import com.soybean.mall.wx.mini.order.controller.WxOrderApiController;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.callback.service.CallBackCommonService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderCancelRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderRemedyRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.payorder.FindPayOrderResponse;
import com.wanmi.sbc.order.api.response.returnorder.ReturnOrderByConditionResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnLogisticsDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnPriceDTO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class ReturnOrderUpdateCallbackHandler implements CallbackHandler {

    @Autowired
    private WxOrderApiController wxOrderApiController;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;

    @Autowired
    private ReturnOrderProvider returnOrderProvider;

    @Autowired
    private CallBackCommonService callBackCommonService;

    @Override
    public boolean support(String eventType) {
        return "aftersale_update_order".equals(eventType);
    }

    @Override
    public String handle(Map<String, Object> paramMap) {
        log.info("ReturnOrderUpdateCallbackHandler handle --> begin");
        long beginTime = System.currentTimeMillis();
        Object returnOrderObj = paramMap.get("aftersale_info");
        if (returnOrderObj == null) {
            log.error("回调参数异常 param:{}", paramMap);
            return "fail";
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
         *      List<WxDetailAfterSaleResponse.MediaListInfo> mediaList = new ArrayList<>();
         *      WxDetailAfterSaleResponse.MediaListInfo mediaListInfo = new WxDetailAfterSaleResponse.MediaListInfo();
         *      mediaListInfo.setType(1);
         *      mediaListInfo.setUrl("https://store.mp.video.tencent-cloud.com/160/20304/snscosdownload/SH/reserved/6xykWLEnztLbbeOaJhzSoLdv6eLTdIlCPJNBGRFauL4TPyWk8k1IDSX32r0DQ2wE9szEIg41DGXIYKTQJyLANA?token=x5Y29zUxcibBnw6ckn64avUb5INaoz0SINpKmsU5PqgxdPEibq5kFVaGewLZzdYH0F&idx=1&expire=1650818887");
         *      mediaListInfo.setThumbUrl("https://store.mp.video.tencent-cloud.com/160/20304/snscosdownload/SH/reserved/6xykWLEnztLbbeOaJhzSoLdv6eLTdIlCPJNBGRFauL4TPyWk8k1IDSX32r0DQ2wE9szEIg41DGXIYKTQJyLANA?token=x5Y29zUxcibBnw6ckn64avUb5INaoz0SINpKmsU5PqgxdPEibq5kFVaGewLZzdYH0F&idx=1&expire=1650818887");
         *      mediaList.add(mediaListInfo);
         *      rr.setMediaList(mediaList);
         *
         *      rr.setRefundReasonType(2);
         *      rr.setRefundReason("111252345");
         *      context.setAfterSalesOrder(rr);
         */


        if (context.getAfterSalesOrder() == null) {
            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 内容为空,不能修改售后订单", aftersaleId);
            return "fail";
        }

        WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
        if (AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()) != AfterSalesStateEnum.AFTER_SALES_STATE_TWO) {
            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 非创建售后状态，return", aftersaleId);
            return "fail";
        }

        //根据视频号获取退单的详细信息
        ReturnOrderByConditionRequest returnOrderByConditionRequest = new ReturnOrderByConditionRequest();
        returnOrderByConditionRequest.setAftersaleId(aftersaleId);
        BaseResponse<ReturnOrderByConditionResponse> returnOrderByConditionResponseBaseResponse = returnOrderQueryProvider.listByCondition(returnOrderByConditionRequest);
        List<ReturnOrderVO> returnOrderList = returnOrderByConditionResponseBaseResponse.getContext().getReturnOrderList();
        returnOrderList = returnOrderList.stream().filter(returnOrderVO -> returnOrderVO.getReturnFlowState() == ReturnFlowState.INIT).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(returnOrderList)) {
            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 获取退单为空,不能修改售后订单", aftersaleId);
            return "fail";
        }

        ReturnOrderVO returnOrderVO = returnOrderList.get(0);
        log.info("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 返回的退单为：{}", aftersaleId, JSON.toJSONString(returnOrderVO));

        //附件
        if (!CollectionUtils.isEmpty(afterSalesOrder.getMediaList())) {
            returnOrderVO.setImages(callBackCommonService.appendix(afterSalesOrder.getMediaList()));
        }
        returnOrderVO.setReturnReason(callBackCommonService.wxReturnReason2ReturnReasonType(afterSalesOrder));
        returnOrderVO.setDescription(callBackCommonService.wxReturnReasonType2ReturnReasonStr(afterSalesOrder));


        Operator operator = callBackCommonService.packOperator(returnOrderVO);


        //修改售后订单
        ReturnOrderRemedyRequest ReturnOrderRemedyRequest = new ReturnOrderRemedyRequest();
        ReturnOrderRemedyRequest.setNewReturnOrder(KsBeanUtil.convert(returnOrderVO, ReturnOrderDTO.class));
        ReturnOrderRemedyRequest.setOperator(operator);
        BaseResponse remedy = returnOrderProvider.remedy(ReturnOrderRemedyRequest);
        log.info("ReturnOrderUpdateCallbackHandler  orderId:{} aftersaleId:{} returnOrderId:{} handle result:{} --> end cost: {} ms",
                returnOrderVO.getTid(), aftersaleId, returnOrderVO.getId(), JSON.toJSONString(remedy), System.currentTimeMillis() - beginTime);
        return "success";
    }

}
