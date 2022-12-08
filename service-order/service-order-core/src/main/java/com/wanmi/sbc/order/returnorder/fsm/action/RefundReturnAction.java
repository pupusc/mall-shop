package com.wanmi.sbc.order.returnorder.fsm.action;
import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.dszt.TransferService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.FeiShuUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.order.api.enums.ThirdInvokeCategoryEnum;
import com.wanmi.sbc.order.api.enums.ThirdInvokePublishStatusEnum;
import com.wanmi.sbc.order.bean.enums.HandleStatus;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengKnowledgeRefundRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRefundRequest;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateListRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import com.wanmi.sbc.order.exceptionoftradepoints.service.ExceptionOfTradePointsService;
import com.wanmi.sbc.order.returnorder.fsm.ReturnAction;
import com.wanmi.sbc.order.returnorder.fsm.ReturnStateContext;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.returnorder.fsm.params.ReturnStateRequest;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnEventLog;
import com.wanmi.sbc.order.third.ThirdInvokeService;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 退款操作
 * Created by jinwei on 22/4/2017.
 */
@Component
@Slf4j
public class RefundReturnAction extends ReturnAction {
//    @Autowired
//    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private GoodsTobeEvaluateSaveProvider goodsTobeEvaluateSaveProvider;

    @Autowired
    private GoodsTobeEvaluateQueryProvider goodsTobeEvaluateQueryProvider;

    @Autowired
    private StoreTobeEvaluateSaveProvider storeTobeEvaluateSaveProvider;

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;

    @Autowired
    private ExceptionOfTradePointsService exceptionOfTradePointsService;

    @Autowired
    private ExternalProvider externalProvider;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private ShopCenterSaleAfterProvider shopCenterSaleAfterProvider;

    @Autowired
    private TransferService transferService;

    @Autowired
    private ThirdInvokeService thirdInvokeService;

    @Override
    protected void evaluateInternal(ReturnOrder returnOrder, ReturnStateRequest request, ReturnStateContext rsc) {
        Operator operator = rsc.findOperator();
        returnOrder.setReturnFlowState(ReturnFlowState.COMPLETED);
        returnOrder.getReturnPrice().setActualReturnPrice(rsc.findRequestData());
        ReturnEventLog eventLog = ReturnEventLog.builder()
                .operator(operator)
                .eventType(ReturnEvent.REFUND.getDesc())
                .eventTime(LocalDateTime.now())
                .eventDetail(String.format("退单[%s]已退款，退单完成,操作人:%s", returnOrder.getId(), operator.getName()))
                .build();
        returnOrder.appendReturnEventLog(eventLog);
        returnOrder.setFinishTime(LocalDateTime.now());
        returnOrderService.updateReturnOrder(returnOrder);
        super.operationLogMq.convertAndSend(operator, ReturnEvent.REFUND.getDesc(), eventLog.getEventDetail());



        //此处为退换积分和知豆，如果没有返还用户会找过来,后续添加日志表
        Long points = Objects.nonNull(returnOrder.getReturnPoints()) ? returnOrder.getReturnPoints().getApplyPoints() : 0L;
        ExceptionOfTradePoints exceptionOfTradePoints = new ExceptionOfTradePoints();
        exceptionOfTradePoints.setTradeId(returnOrder.getTid());
        exceptionOfTradePoints.setPoints(points);
        exceptionOfTradePoints.setHandleStatus(HandleStatus.PENDING);
        exceptionOfTradePoints.setErrorTime(3); //超过3次不再处理
        exceptionOfTradePoints.setDelFlag(DeleteFlag.NO);
        exceptionOfTradePoints.setErrorCode("init");
        exceptionOfTradePoints.setCreateTime(LocalDateTime.now());

        if (points > 0) {
    /*        customerPointsDetailSaveProvider.returnPoints(CustomerPointsDetailAddRequest.builder()
                    .customerId(returnOrder.getBuyer().getId())
                    .type(OperateType.GROWTH)
                    .serviceType(PointsServiceType.RETURN_ORDER_BACK)
                    .points(points)
                    .content(JSONObject.toJSONString(Collections.singletonMap("returnOrderNo", returnOrder.getId())))
                    .build());*/
            log.info("RefundReturnAction log point begin returnOrderId:{}", returnOrder.getId());
            exceptionOfTradePoints.setType(3); //不影响原来的类型内容,只是做记录
            ExceptionOfTradePoints exceptionOfTradePointsModel = exceptionOfTradePointsService.add(exceptionOfTradePoints);
            try {

                FanDengPointRefundRequest refundRequest = FanDengPointRefundRequest.builder()
                        .point(points).userNo(returnOrder.getFanDengUserNo()).sourceId(returnOrder.getId()).sourceType(1)
                        .desc("退单返还(退单号:"+returnOrder.getId()+")").build();
                externalProvider.pointRefund(refundRequest);

                exceptionOfTradePointsModel.setHandleStatus(HandleStatus.SUCCESSFULLY_PROCESSED);
                exceptionOfTradePointsService.modify(exceptionOfTradePointsModel);
                log.info("RefundReturnAction log point end returnOrderId:{}", returnOrder.getId());
            } catch (Exception ex) {
                String errorMsg = "RefundReturnAction 退单号:" + returnOrder.getId() +" 订单号:" + returnOrder.getTid() + " 退还积分执行异常";
                log.error(errorMsg, ex);
            }
        }
        Long knowledge = Objects.nonNull(returnOrder.getReturnKnowledge()) ? returnOrder.getReturnKnowledge().getApplyKnowledge() : null;
        if (knowledge != null && knowledge > 0) {
            try {
                log.info("RefundReturnAction log knowledge begin returnOrderId:{}", returnOrder.getId());
                exceptionOfTradePoints.setType(4); //不影响原来的类型内容,只是做记录
                ExceptionOfTradePoints exceptionOfTradePointsModel = exceptionOfTradePointsService.add(exceptionOfTradePoints);

                Trade trade = tradeRepository.findById(returnOrder.getTid()).get();
                FanDengKnowledgeRefundRequest refundRequest = FanDengKnowledgeRefundRequest.builder()
                        .beans(knowledge).userNo(returnOrder.getFanDengUserNo()).sourceId(returnOrder.getId())
                        .deductCode(trade.getDeductCode())
                        .desc("退单返还(退单号:"+returnOrder.getId()+")").build();
                externalProvider.knowledgeRefund(refundRequest);

                exceptionOfTradePointsModel.setHandleStatus(HandleStatus.SUCCESSFULLY_PROCESSED);
                exceptionOfTradePointsService.modify(exceptionOfTradePointsModel);
                log.info("RefundReturnAction log knowledge end returnOrderId:{}", returnOrder.getId());
            } catch (Exception ex) {
                String errorMsg = "RefundReturnAction 退单号:" + returnOrder.getId() +" 订单号:" + returnOrder.getTid() + " 退还知豆执行异常";
                log.error(errorMsg, ex);
            }
        }
        delEvaluate(returnOrder);

        String pushMsg = "";
       try {
           //创建售后订单
           ThirdInvokeDTO thirdInvokeDTO = thirdInvokeService.add(returnOrder.getId(), ThirdInvokeCategoryEnum.INVOKE_RETURN_ORDER);
           if (Objects.equals(thirdInvokeDTO.getPushStatus(), ThirdInvokePublishStatusEnum.SUCCESS.getCode())) {
               log.info("ProviderTradeService singlePushOrder businessId:{} 已经推送成功，重复提送", thirdInvokeDTO.getBusinessId());
               return;
           }

           try {
               //调用推送接口
               SaleAfterCreateNewReq saleAfterCreateNewReq = transferService.changeSaleAfterCreateReq(returnOrder);
               if (saleAfterCreateNewReq == null) {
                   pushMsg = "售后单:" + returnOrder.getId() + " 推送电商中台异常";
                   throw new SbcRuntimeException("999999", String.format("商城售后单%s转化电商中台对象为null", returnOrder.getId()));
               }
               long beginTime = System.currentTimeMillis();
               log.info("RefundReturnAction createSaleAfter param {}", JSON.toJSONString(saleAfterCreateNewReq));
               BaseResponse<Long> saleAfter = shopCenterSaleAfterProvider.createSaleAfter(saleAfterCreateNewReq);
               log.info("RefundReturnAction createSaleAfter result {} cost: {}s", JSON.toJSONString(saleAfter), (System.currentTimeMillis() - beginTime)/100);

               if (Objects.equals(saleAfter.getCode(), CommonErrorCode.SUCCESSFUL)) {
                   thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.SUCCESS, "SUCCESS");
               } else {
                   pushMsg = "售后单:" + returnOrder.getId() + " 推送电商中台异常";
                   thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.FAIL, saleAfter.getMessage());
               }
           } catch (Exception ex) {
               pushMsg = "售后单:" + returnOrder.getId() + " 推送电商中台异常";
               log.error("ProviderTradeService singlePushOrder " + thirdInvokeDTO.getBusinessId() + " 推送失败 error:{} ", ex);
               thirdInvokeService.update(thirdInvokeDTO.getId(), "999999", ThirdInvokePublishStatusEnum.FAIL, "调用失败");
           }
       } catch (Exception ex) {
           log.error("RefundReturnAction evaluateInternal error", ex);
       }

        if (StringUtils.isNotBlank(pushMsg)) {
            FeiShuUtil.sendFeiShuMessageDefault(pushMsg);
        }
    }

    /**
     * @Author lvzhenwei
     * @Description 订单退货完成删除对应的订单以及商品待评价数据
     * @Date 11:28 2019/4/11
     * @Param [returnOrder]
     **/
    private void delEvaluate(ReturnOrder returnOrder) {
        String tid = returnOrder.getTid();
        returnOrder.getReturnItems().forEach(returnItem -> {
            //判断退单商品是否全部退完，如果已经退完，则删除对应的商品待评论数据
            List<String> goodsTobeEvaluateIds;
            if (returnItem.getCanReturnNum() == returnItem.getNum()) {
                GoodsTobeEvaluateListRequest goodsTobeEvaluateListReq = new GoodsTobeEvaluateListRequest();
                goodsTobeEvaluateListReq.setOrderNo(tid);
                goodsTobeEvaluateListReq.setGoodsInfoId(returnItem.getSkuId());
                goodsTobeEvaluateIds = goodsTobeEvaluateQueryProvider.list(goodsTobeEvaluateListReq).getContext().getGoodsTobeEvaluateVOList()
                        .stream().map(GoodsTobeEvaluateVO::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(goodsTobeEvaluateIds)) {
                    GoodsTobeEvaluateDelByIdListRequest goodsTobeEvaluateDelByIdListRequest = new GoodsTobeEvaluateDelByIdListRequest();
                    goodsTobeEvaluateDelByIdListRequest.setIdList(goodsTobeEvaluateIds);
                    goodsTobeEvaluateSaveProvider.deleteByIdList(goodsTobeEvaluateDelByIdListRequest);
                }
            }
        });
        //如果订单全部退完，则删除对应订单店铺服务待评价数据
        if (returnOrderService.isReturnFull(returnOrder)) {
            StoreTobeEvaluateListRequest storeTobeEvaluateListReq = new StoreTobeEvaluateListRequest();
            storeTobeEvaluateListReq.setOrderNo(tid);
            List<String> storeTobeEvaluateIds;
            storeTobeEvaluateIds = storeTobeEvaluateQueryProvider.list(storeTobeEvaluateListReq).getContext().getStoreTobeEvaluateVOList()
                    .stream().map(StoreTobeEvaluateVO::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(storeTobeEvaluateIds)) {
                StoreTobeEvaluateDelByIdListRequest storeTobeEvaluateDelByIdListRequest = new StoreTobeEvaluateDelByIdListRequest();
                storeTobeEvaluateDelByIdListRequest.setIdList(storeTobeEvaluateIds);
                storeTobeEvaluateSaveProvider.deleteByIdList(storeTobeEvaluateDelByIdListRequest);
            }
        }
    }
}
