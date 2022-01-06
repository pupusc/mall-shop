package com.wanmi.sbc.returnorder.service;

import com.aliyuncs.linkedmall.model.v20180116.CancelOrderRequest;
import com.aliyuncs.linkedmall.model.v20180116.CancelOrderResponse;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.erp.api.request.RefundTradeRequest;
import com.wanmi.sbc.erp.api.request.TradeQueryRequest;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.erp.api.response.QueryTradeResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.provider.refund.RefundOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeCycleBuyInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/1/6 9:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class GuanYiYunService extends AbstractCRMService {


    public BaseResponse checkErpDeliverStatus(@PathVariable String rid, @PathVariable Boolean flag){
        //获取退单信息
        ReturnOrderVO returnOrder = returnOrderQueryProvider.getById(ReturnOrderByIdRequest.builder().rid(rid).build()).getContext();
        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = TradeGetByIdRequest.builder().tid(returnOrder.getTid()).build();
        BaseResponse<TradeGetByIdResponse> tradeResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeVO tradeVO = tradeResponse.getContext().getTradeVO();
        if (CollectionUtils.isEmpty(tradeResponse.getContext().getTradeVO().getTradeVOList())){
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        //判断订单中是否包含虚拟商品和电子卡券，防止用户刷商品
        if (flag){
            List<TradeItemVO> tradeItemVOList = tradeVO.getGifts().stream().filter(tradeItemVO ->
                    tradeItemVO.getGoodsType().equals(GoodsType.VIRTUAL_GOODS) || tradeItemVO.getGoodsType()
                            .equals(GoodsType.VIRTUAL_COUPON)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tradeItemVOList)){
                throw new SbcRuntimeException("K-050319");
            }
        }

        if (tradeVO.getCycleBuyFlag()) {
            List<DeliverCalendarVO> deliverCalendar=tradeVO.getTradeCycleBuyInfo().getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deliverCalendar)) {
                deliverCalendar.forEach(deliverCalendarVO -> {
                    //查询providerId查询订单的发货记录
                    DeliveryQueryRequest deliveryQueryRequest = DeliveryQueryRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).build();
                    BaseResponse<DeliveryStatusResponse> response = guanyierpProvider.getDeliveryStatus(deliveryQueryRequest);
                    //已发货并且没有确认收货的订单无法退款
                    if(!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                            && !CollectionUtils.isEmpty(response.getContext().getDeliveryInfoVOList())){
                        response.getContext().getDeliveryInfoVOList().stream().forEach(deliveryInfoVO -> {
                            if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                                throw new SbcRuntimeException("K-050106");
                            }
                        });
                    }
                });
            }
            //已经发货的订单
        } else if(Objects.equals(returnOrder.getProviderId(), defaultProviderId)){
            //已发货并且没有确认收货的订单无法退款
            DeliveryQueryRequest deliveryQueryRequest = DeliveryQueryRequest.builder().tid(returnOrder.getPtid()).build();
            BaseResponse<DeliveryStatusResponse> response = guanyierpProvider.getDeliveryStatus(deliveryQueryRequest);
            if(!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                    && !tradeVO.getTradeState().getFlowState().equals(FlowState.COMPLETED)
                    && !CollectionUtils.isEmpty(response.getContext().getDeliveryInfoVOList())){
                response.getContext().getDeliveryInfoVOList().stream().forEach(deliveryInfoVO -> {
                    if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                        throw new SbcRuntimeException("K-050106");
                    }
                });
            }
        }


        //通知erp系统停止发货,走系统退款逻辑,只退此审核单的
        if (tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED) || tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.PART_SHIPPED)){
            tradeResponse.getContext().getTradeVO().getTradeVOList().stream().filter(p->p.getId().equals(returnOrder.getPtid())).forEach(providerTradeVO -> {
                if(!Objects.equals(providerTradeVO.getTradeItems().get(0).getProviderId(),defaultProviderId)){
                    BaseResponse<CancelOrderResponse> response = bizSupplierClient.cancelOrder(CancelOrderRequest.builder().orderId(providerTradeVO.getDeliveryOrderId()).pid(providerTradeVO.getId()).build());
                    if(response == null || response.getContext() == null || !Objects.equals(response.getContext().getStatus(),1)){
                        throw new SbcRuntimeException("K-050143", new Object[]{response !=null && response.getContext() != null && StringUtils.isNotEmpty(response.getContext().getErrorMsg())?response.getContext().getErrorMsg():"订单取消失败"});
                    }
                    return;
                }
                //拦截主商品
                providerTradeVO.getTradeItems().forEach(tradeItemVO -> {
                    RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTradeVO.getId()).oid(tradeItemVO.getOid()).build();
                    guanyierpProvider.RefundTrade(refundTradeRequest);
                });
                //拦截赠品
                if (!CollectionUtils.isEmpty(providerTradeVO.getGifts())){
                    providerTradeVO.getGifts().forEach(giftVO -> {
                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder()
                                .tid(providerTradeVO.getId())
                                .oid(giftVO.getOid()).build();
                        guanyierpProvider.RefundTrade(refundTradeRequest);
                    });
                }
            });
        }

        //拦截周期购订单
        tradeResponse.getContext().getTradeVO().getTradeVOList().forEach(providerTradeVO -> {
            if (providerTradeVO.getCycleBuyFlag()) {
                TradeCycleBuyInfoVO tradeCycleBuyInfo= providerTradeVO.getTradeCycleBuyInfo();
                List<DeliverCalendarVO> deliverCalendar=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(deliverCalendar)) {
                    String  oid=providerTradeVO.getTradeItems().get(0).getOid();
                    deliverCalendar.forEach(deliverCalendarVO -> {
                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(oid).build();
                        guanyierpProvider.RefundTrade(refundTradeRequest);
                        //获取订单期数，判断是否是第一期，周期购订单只有第一期才有赠品
                        String cyclNum= deliverCalendarVO.getErpTradeCode().substring(deliverCalendarVO.getErpTradeCode().length()-1);
                        if (CollectionUtils.isNotEmpty(providerTradeVO.getGifts()) && Objects.equals(cyclNum,"1")) {
                            providerTradeVO.getGifts().forEach(giftVO -> {
                                RefundTradeRequest refundRequest = RefundTradeRequest.builder().tid(deliverCalendarVO.getErpTradeCode()).oid(giftVO.getOid()).build();
                                guanyierpProvider.RefundTrade(refundRequest);
                            });
                        }
                    });
                }
            }
        });

        if (returnOrder.getReturnType().equals(ReturnType.RETURN)) {
            if(!Objects.equals(defaultProviderId,returnOrder.getProviderId())){
                log.info("博库退货退款请走博库平台");
                return BaseResponse.SUCCESSFUL();
            }
            log.info("=============组合商品退货退款拦截未发货的商品：{}==================",tradeVO.getId());
            List<ReturnItemVO> returnGoods = returnOrder.getReturnItems();
            List<ReturnItemVO> returnGifts = returnOrder.getReturnGifts();
            List<ReturnItemVO> totalReturnItemList =
                    Stream.of(returnGoods, returnGifts).flatMap(Collection::stream).collect(Collectors.toList());
            List<String> returnItemSkuIds = totalReturnItemList.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            List<TradeVO> providerTrades =tradeVO.getTradeVOList();
            providerTrades.forEach(providerTrade -> {
                List<TradeItemVO>  tradeItems= providerTrade.getTradeItems();
                List<TradeItemVO>  gifts= providerTrade.getGifts();
                List<TradeItemVO> totalTradeItemList =
                        Stream.of(gifts, tradeItems).flatMap(Collection::stream).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(totalTradeItemList)) {
                    totalTradeItemList.forEach(tradeItem -> {
                        if (Objects.nonNull(tradeItem.getCombinedCommodity()) && tradeItem.getCombinedCommodity() && returnItemSkuIds.contains(tradeItem.getSkuId())) {
                            RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTrade.getId()).oid(tradeItem.getOid()).build();
                            //如果是组合商品,查询ERP订单发货状态，ERP订单已发货就不需要走拦截
                            TradeQueryRequest tradeQueryRequest = TradeQueryRequest.builder().tid(providerTrade.getId()).flag(0).build();
                            BaseResponse<QueryTradeResponse> tradeInfoResponse= guanyierpProvider.getTradeInfo(tradeQueryRequest);
                            //默认查询七天内的订单,如果没有加过就再查询历史订单
                            if (StringUtils.isBlank(tradeInfoResponse.getContext().getPlatformCode())){
                                tradeQueryRequest.setFlag(1);
                                BaseResponse<QueryTradeResponse> historyTradeResponse = guanyierpProvider.getTradeInfo(tradeQueryRequest);
                                if (StringUtils.isNoneBlank(historyTradeResponse.getContext().getPlatformCode())) {
                                    if (DeliverStatus.NOT_YET_SHIPPED.equals(historyTradeResponse.getContext().getDeliveryState())
                                            || DeliverStatus.PART_SHIPPED.equals(historyTradeResponse.getContext().getDeliveryState())) {
                                        guanyierpProvider.RefundTrade(refundTradeRequest);
                                        log.info("=============组合商品退货退款拦截未发货的商品：{}==================", refundTradeRequest);
                                    }
                                }
                            }else {
                                if (DeliverStatus.NOT_YET_SHIPPED.equals(tradeInfoResponse.getContext().getDeliveryState())
                                        || DeliverStatus.PART_SHIPPED.equals(tradeInfoResponse.getContext().getDeliveryState())){
                                    guanyierpProvider.RefundTrade(refundTradeRequest);
                                    log.info("=============组合商品退货退款拦截未发货的商品：{}==================",refundTradeRequest);
                                }
                            }
                        }
                    });
                }
            });
        }
        return BaseResponse.SUCCESSFUL();
    }
}
