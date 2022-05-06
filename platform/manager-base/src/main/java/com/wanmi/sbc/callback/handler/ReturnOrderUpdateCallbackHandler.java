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
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.payorder.PayOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.payorder.FindPayOrderRequest;
import com.wanmi.sbc.order.api.request.returnorder.RejectRefund2DeliveredRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAuditRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderCancelRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderDeliverRequest;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderReceiveRequest;
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
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnLogisticsVO;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
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
            return CommonHandlerUtil.FAIL;
        }

        WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder = context.getAfterSalesOrder();
//        if (AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()) != AfterSalesStateEnum.AFTER_SALES_STATE_TWO) {
//            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 非创建售后状态，return", aftersaleId);
//            return CommonHandlerUtil.FAIL;
//        }

        //根据视频号获取退单的详细信息
        ReturnOrderByConditionRequest returnOrderByConditionRequest = new ReturnOrderByConditionRequest();
        returnOrderByConditionRequest.setAftersaleId(aftersaleId);
        BaseResponse<ReturnOrderByConditionResponse> returnOrderByConditionResponseBaseResponse = returnOrderQueryProvider.listByCondition(returnOrderByConditionRequest);
        List<ReturnOrderVO> returnOrderList = returnOrderByConditionResponseBaseResponse.getContext().getReturnOrderList();

        if (CollectionUtils.isEmpty(returnOrderList)) {
            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 获取退单为空,不能修改售后订单", aftersaleId);
            return CommonHandlerUtil.FAIL;
        }
        ReturnOrderVO returnOrderVO = callBackCommonService.getValidReturnOrderVo(returnOrderList);
        if (returnOrderVO == null) {
            returnOrderVO = returnOrderList.get(0);
        }


        log.info("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 返回的退单为：{} 微信售后单为: {}", aftersaleId, JSON.toJSONString(returnOrderVO), JSON.toJSONString(afterSalesOrder));

        //附件
        if (!CollectionUtils.isEmpty(afterSalesOrder.getMediaList())) {
            returnOrderVO.setImages(callBackCommonService.appendix(afterSalesOrder.getMediaList()));
        }
        returnOrderVO.setReturnReason(callBackCommonService.wxReturnReason2ReturnReasonType(afterSalesOrder));
        returnOrderVO.setDescription(callBackCommonService.wxReturnReasonType2ReturnReasonStr(afterSalesOrder));


        Operator operator = callBackCommonService.packOperator(returnOrderVO);

        BaseResponse baseResponse = null;

        try {
            //修改售后订单
//            ReturnOrderRemedyRequest ReturnOrderRemedyRequest = new ReturnOrderRemedyRequest();
//            ReturnOrderRemedyRequest.setNewReturnOrder(KsBeanUtil.convert(returnOrderVO, ReturnOrderDTO.class));
//            ReturnOrderRemedyRequest.setOperator(operator);
//            baseResponse = returnOrderProvider.remedy(ReturnOrderRemedyRequest);
//            更新物流信息

            WxDetailAfterSaleResponse.ReturnInfo returnInfo = afterSalesOrder.getReturnInfo();
            if (returnInfo != null) {
                ReturnOrderDeliverRequest returnOrderDeliverRequest = new ReturnOrderDeliverRequest();
                returnOrderDeliverRequest.setRid(returnOrderVO.getId());
                returnOrderDeliverRequest.setOperator(operator);
                ReturnLogisticsDTO returnLogisticsDTO = new ReturnLogisticsDTO();
                if (StringUtils.isNotBlank(returnInfo.getDeliveryName())) {
                    returnLogisticsDTO.setCompany(returnInfo.getDeliveryName());
                }
                if (StringUtils.isNotBlank(returnInfo.getDeliveryId())) {
                    returnLogisticsDTO.setCode(returnInfo.getDeliveryId());
                }
                if (StringUtils.isNotBlank(returnInfo.getWaybillId())) {
                    returnLogisticsDTO.setNo(returnInfo.getWaybillId());
                }
                if (returnInfo.getOrderReturnTime() != null) {
                    Instant instant = Instant.ofEpochMilli(returnInfo.getOrderReturnTime());
                    returnLogisticsDTO.setCreateTime(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }
                returnOrderDeliverRequest.setLogistics(returnLogisticsDTO);
                baseResponse = returnOrderProvider.updateReturnLogistics(returnOrderDeliverRequest);
            }
        } catch (Exception ex) {
            log.error("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 修改提示内容信息异常", aftersaleId, ex);
        }

        if (Objects.equals(ReturnType.RETURN, returnOrderVO.getReturnType())
                && Objects.equals(AfterSalesStateEnum.AFTER_SALES_STATE_TWO, AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()))
                && Objects.equals(AfterSalesStateEnum.AFTER_SALES_STATE_FOUR, AfterSalesStateEnum.getByCode(afterSalesOrder.getStatus()))) {

            if (returnOrderVO.getReturnFlowState() == ReturnFlowState.REJECT_RECEIVE) {
                RejectRefund2DeliveredRequest request = new RejectRefund2DeliveredRequest();
                request.setRid(returnOrderVO.getId());
                request.setReason("用户主动申请退货退款-退货");
                request.setOperator(callBackCommonService.packOperator(returnOrderVO));
                baseResponse = returnOrderProvider.rejectReceive2Delivered(request);
//                this.package2DirectStatus(returnOrderVO, afterSalesOrder, operator);

            } else if (returnOrderVO.getReturnFlowState() == ReturnFlowState.REJECT_REFUND) {
                RejectRefund2DeliveredRequest request = new RejectRefund2DeliveredRequest();
                request.setRid(returnOrderVO.getId());
                request.setReason("用户主动申请退货退款-退款");
                request.setOperator(callBackCommonService.packOperator(returnOrderVO));
                baseResponse = returnOrderProvider.rejectRefund2Audit(request);

//                String returnOrderId = this.package2DirectStatus(returnOrderVO, afterSalesOrder, operator);
//
//                //              5、同意退货、要带id
//                ReturnOrderReceiveRequest returnOrderReceiveRequest = new ReturnOrderReceiveRequest();
//                returnOrderReceiveRequest.setOperator(operator);
//                returnOrderReceiveRequest.setRid(returnOrderId);
//                BaseResponse receive = returnOrderProvider.receive(returnOrderReceiveRequest);
//                log.info("ReturnOrderUpdateCallbackHandler handler 4、同意退货 aftersaleId:{} returnOrderId:{} 返回结果为:{}"
//                        , afterSalesOrder.getAftersaleId(), returnOrderId, JSON.toJSONString(receive));
            }
        }

        log.info("ReturnOrderUpdateCallbackHandler  orderId:{} aftersaleId:{} returnOrderId:{} handle result:{} --> end cost: {} ms",
                returnOrderVO.getTid(), aftersaleId, returnOrderVO.getId(), JSON.toJSONString(baseResponse), System.currentTimeMillis() - beginTime);
        return CommonHandlerUtil.SUCCESS;
    }


    /**
     * 到指定的状态
     * @param returnOrderVO
     * @param afterSalesOrder
     * @param operator
     */
//    private String package2DirectStatus(ReturnOrderVO returnOrderVO, WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder, Operator operator) {
//        //此处售后 拒绝收获或者拒绝退款进行流转到 创建初始化
//
////            1、用户申请、要带id
//        String returnOrderId = this.createReturnOrder(returnOrderVO, afterSalesOrder);
//        log.info("ReturnOrderUpdateCallbackHandler handler 1、创建售后订单完成 aftersaleId:{} returnOrderId:{} 返回结果为:{}"
//                , afterSalesOrder.getAftersaleId(), returnOrderId, returnOrderId);
//
////            2、用户审核、要带id
//        ReturnOrderAuditRequest request = new ReturnOrderAuditRequest();
//        request.setOperator(operator);
//        request.setRid(returnOrderId);
//        BaseResponse audit = returnOrderProvider.audit(request);
//        log.info("ReturnOrderUpdateCallbackHandler handler 2、走审核流程 aftersaleId:{} returnOrderId:{} 返回结果为:{}"
//                , afterSalesOrder.getAftersaleId(), returnOrderId, JSON.toJSONString(audit));
//
////            3、填写物流、要带id
//        ReturnOrderDeliverRequest returnOrderDeliverRequest = new ReturnOrderDeliverRequest();
//        returnOrderDeliverRequest.setRid(returnOrderId);
//        ReturnLogisticsVO returnLogistics = returnOrderVO.getReturnLogistics();
//        if (returnLogistics != null) {
//            returnOrderDeliverRequest.setLogistics(callBackCommonService.packReturnLogistics(returnLogistics));
//        }
//        returnOrderDeliverRequest.setOperator(operator);
//        BaseResponse deliver = returnOrderProvider.deliver(returnOrderDeliverRequest);
//        log.info("ReturnOrderUpdateCallbackHandler handler 3、填写物流 aftersaleId:{} returnOrderId:{} 返回结果为:{}"
//                , afterSalesOrder.getAftersaleId(), returnOrderId, JSON.toJSONString(deliver));
//        return returnOrderId;
//    }


    /**
     * 退单转化售后订单
     * @param returnOrderVO
     * @param afterSalesOrder
     * @return
     */
//    private String createReturnOrder(ReturnOrderVO returnOrderVO, WxDetailAfterSaleResponse.AfterSalesOrder afterSalesOrder) {
//
//        List<ReturnItemVO> returnItems = returnOrderVO.getReturnItems();
//        if (CollectionUtils.isEmpty(returnItems) || returnItems.size() > 1) {
//            log.info("ReturnOrderUpdateCallbackHandler handler aftersaleId:{} 订单中的商品只能为单个商品 return", afterSalesOrder.getAftersaleId());
//            return "";
//        }
//        ReturnItemVO returnItemVO = returnItems.get(0);
//
//        ReturnOrderDTO returnOrderDTO = new ReturnOrderDTO();
//        returnOrderDTO.setTid(returnOrderVO.getTid());
//        returnOrderDTO.setAftersaleId(afterSalesOrder.getAftersaleId().toString());
//        returnOrderDTO.setReturnReason(callBackCommonService.wxReturnReason2ReturnReasonType(afterSalesOrder));
//        returnOrderDTO.setDescription(callBackCommonService.wxReturnReasonType2ReturnReasonStr(afterSalesOrder));
//        //附件
//        if (!CollectionUtils.isEmpty(afterSalesOrder.getMediaList())) {
//            returnOrderDTO.setImages(callBackCommonService.appendix(afterSalesOrder.getMediaList()));
//        }
//
//
//        //物流信息
//        ReturnLogisticsVO returnLogistics = returnOrderVO.getReturnLogistics();
//        if (returnLogistics != null) {
//            returnOrderDTO.setReturnLogistics(callBackCommonService.packReturnLogistics(returnLogistics));
//        }
//
//        returnOrderDTO.setReturnWay(returnOrderVO.getReturnWay());
//        returnOrderDTO.setReturnType(returnOrderVO.getReturnType());
//        returnOrderDTO.setTerminalSource(TerminalSource.MINIPROGRAM);
//
//        ReturnPriceDTO returnPrice = new ReturnPriceDTO();
//        returnPrice.setApplyPrice(returnOrderVO.getReturnPrice().getApplyPrice());
//        returnPrice.setTotalPrice(returnOrderVO.getReturnPrice().getTotalPrice());
//        returnPrice.setApplyStatus(false);
//        returnOrderDTO.setReturnPrice(returnPrice);
//
//        CompanyDTO company = new CompanyDTO();
//        company.setCompanyInfoId(returnOrderVO.getCompany().getCompanyInfoId());
//        company.setSupplierName(returnOrderVO.getCompany().getSupplierName());
//        company.setCompanyCode(returnOrderVO.getCompany().getCompanyCode());
//        company.setAccountName(returnOrderVO.getCompany().getAccountName());
//        company.setStoreId(returnOrderVO.getCompany().getStoreId());
//        company.setStoreName(returnOrderVO.getCompany().getStoreName());
//        company.setCompanyType(returnOrderVO.getCompany().getCompanyType());
//        returnOrderDTO.setCompany(company);
//
//        returnOrderDTO.setChannelType(returnOrderVO.getChannelType());
////        returnOrderDTO.setPlatform(Platform.WX_VIDEO);
//
//
//        Operator operator = new Operator();
//        operator.setPlatform(Platform.WX_VIDEO);
//        operator.setUserId(returnOrderVO.getBuyer().getId());
//        operator.setName(returnOrderVO.getBuyer().getName());
//        operator.setStoreId(returnOrderVO.getCompany().getStoreId() == null ? "" : returnOrderVO.getCompany().getStoreId().toString());
//        operator.setIp("127.0.0.1");
//        operator.setAccount(returnOrderVO.getBuyer().getAccount());
//        operator.setCompanyInfoId(returnOrderVO.getCompany().getCompanyInfoId());
//
//
//
//        returnOrderDTO.setReturnItems(Collections.singletonList(this.packageTradeItem(returnItemVO)));
//        return returnOrderProvider.add(ReturnOrderAddRequest.builder().returnOrder(returnOrderDTO).operator(operator).build()).getContext().getReturnOrderId();
//    }



    /**
     * 打包tradeItem
     * @param returnItem
     * @return
     */
//    private ReturnItemDTO packageTradeItem(ReturnItemVO returnItem) {
//        ReturnItemDTO returnItemDTO = new ReturnItemDTO();
//        returnItemDTO.setApplyRealPrice(returnItem.getApplyRealPrice());
//        returnItemDTO.setApplyKnowledge(returnItem.getApplyKnowledge());
//        returnItemDTO.setApplyPoint(returnItem.getApplyPoint() == null ? 0L : returnItem.getApplyPoint());
//        returnItemDTO.setBuyPoint(returnItem.getBuyPoint());
//        returnItemDTO.setCanReturnNum(returnItem.getCanReturnNum());
//        returnItemDTO.setNum(returnItem.getNum().intValue());
//        returnItemDTO.setSkuId(returnItem.getSkuId());
//        returnItemDTO.setSkuName(returnItem.getSkuName());
//        returnItemDTO.setSkuNo(returnItem.getSkuNo());
//        returnItemDTO.setSpecDetails(returnItem.getSpecDetails());
//        returnItemDTO.setPrice(returnItem.getPrice());
//        returnItemDTO.setSplitPrice(returnItem.getSplitPrice());
//        returnItemDTO.setSupplyPrice(returnItem.getSupplyPrice());
////            returnItemDTO.setProviderPrice(tradeItem.getPro);
////            returnItemDTO.setOrderSplitPrice(tradeItem.getord);
//        returnItemDTO.setNum(returnItem.getNum());
//        returnItemDTO.setPic(returnItem.getPic());
//        returnItemDTO.setUnit(returnItem.getUnit());
//        returnItemDTO.setGoodsType(returnItem.getGoodsType());
//
//
////            returnItemDTO.setThirdPlatformSpuId(tradeItem.getThirdPlatformSpuId());
////            returnItemDTO.setThirdPlatformSkuId(tradeItem.getThirdPlatformSkuId());
//        returnItemDTO.setGoodsSource(returnItem.getGoodsSource());
////            returnItemDTO.setThirdPlatformType(tradeItem.getThirdPlatformType());
////            returnItemDTO.setThirdPlatformSubOrderId(tradeItem.getThirdPlatformSubOrderId());
//        returnItemDTO.setProviderId(returnItem.getProviderId());
////            returnItemDTO.setSplitPoint(tradeItem.get);
//        return returnItemDTO;
//    }
}
