package com.wanmi.sbc.returnorder.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.linkedmall.model.v20180116.CancelOrderRequest;
import com.aliyuncs.linkedmall.model.v20180116.CancelOrderResponse;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.sbc.wanmi.erp.bean.vo.DeliveryInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.erp.api.request.RefundTradeRequest;
import com.wanmi.sbc.erp.api.request.TradeQueryRequest;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.erp.api.response.QueryTradeResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
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
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
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


    /**
     * 拦截发货状态
     * @param
     * @param flag
     * @return
     */
    @Override
    public BaseResponse interceptorErpDeliverStatus(ReturnOrderVO returnOrderVO, Boolean flag){
//        //获取退单信息
//        ReturnOrderVO returnOrderVO = super.getReturnOrderVo(returnOrderId);
//        if (returnOrderVO == null) {
//            //退单不存在
//            throw new SbcRuntimeException("K-050003");
//        }
        TradeGetByIdResponse tradeAndProviderTrade = super.getTradeAndProviderTrade(returnOrderVO.getTid(), flag);
        TradeVO tradeVO = tradeAndProviderTrade.getTradeVO();
        //如果为周期购
        if (tradeVO.getCycleBuyFlag()) {
            //获取已经推送过去的周期购订单
            List<DeliverCalendarVO> deliverCalendar =
                    tradeVO.getTradeCycleBuyInfo().getDeliverCalendar().stream()
                            .filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus() == CycleDeliverStatus.PUSHED).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deliverCalendar)) {
                for (DeliverCalendarVO deliverCalendarVOParam : deliverCalendar) {
                    //查询providerId查询订单的发货记录
                    DeliveryQueryRequest deliveryQueryRequest = DeliveryQueryRequest.builder().tid(deliverCalendarVOParam.getErpTradeCode()).build();
                    BaseResponse<DeliveryStatusResponse> response = guanyierpProvider.getDeliveryStatus(deliveryQueryRequest);
                    //已发货并且没有确认收货的订单无法退款
                    if(!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                            && !CollectionUtils.isEmpty(response.getContext().getDeliveryInfoVOList())){
                        response.getContext().getDeliveryInfoVOList().stream().forEach(deliveryInfoVO -> {
                            if (deliveryInfoVO.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                                log.error("订单:{} 周期购管易云已经发货，不能申请退款", tradeVO.getId());
                                throw new SbcRuntimeException("K-050106");
                            }
                        });
                    }
                }
            }

        } else {
            //管易云普通订单  已发货并且没有确认收货的订单无法退款
            DeliveryQueryRequest deliveryQueryRequest = new DeliveryQueryRequest();
            deliveryQueryRequest.setTid(returnOrderVO.getPtid()); //子单
            //到管易云中查询订单发货状态
            List<DeliveryInfoVO> deliveryInfoVOList = guanyierpProvider.getDeliveryStatus(deliveryQueryRequest).getContext().getDeliveryInfoVOList();
            //如果订单 没有 作废、完成 同时管易云中存在发货订单
            if (!tradeVO.getTradeState().getFlowState().equals(FlowState.VOID)
                    && !tradeVO.getTradeState().getFlowState().equals(FlowState.COMPLETED)
                    && !CollectionUtils.isEmpty(deliveryInfoVOList)) {
                //如果是已经全部发货，则抛出异常
                for (DeliveryInfoVO deliveryInfoVOParam : deliveryInfoVOList) {
                    if (deliveryInfoVOParam.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETE)){
                        throw new SbcRuntimeException("K-050106");
                    }
                }
            }
        }

        /****************商品拦截 begin*******************/
        log.info("订单管易云拦截:{}", JSON.toJSONString(returnOrderVO));
        //获取退单的商品列表
        Map<String, ReturnItemVO> skuId2ReturnItemMap =
                returnOrderVO.getReturnItems().stream().collect(Collectors.toMap(ReturnItemVO::getSkuId, Function.identity(), (k1, k2) -> k1));


        DeliverStatus deliverStatus = tradeVO.getTradeState().getDeliverStatus();
        //如果未发货或者部分发货则处理
        if (deliverStatus.equals(DeliverStatus.NOT_YET_SHIPPED) || deliverStatus.equals(DeliverStatus.PART_SHIPPED)) {
            //这里获取的实际上是子单
            List<TradeVO> providerTradeVoList = tradeVO.getTradeVOList().stream().filter(p -> p.getId().equals(returnOrderVO.getPtid())).collect(Collectors.toList());

            for (TradeVO providerTradeParam : providerTradeVoList) {
                //普通订单 拦截主商品
                for (TradeItemVO tradeItemParam : providerTradeParam.getTradeItems()) {
                    ReturnItemVO returnItemVO = skuId2ReturnItemMap.get(tradeItemParam.getSkuId());
                    if (returnItemVO == null) {
                        continue;
                    }
                    //tid表示的是 子单id  Oid表示的是管易云上的订单号
                    RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTradeParam.getId()).oid(tradeItemParam.getOid()).build();
                    BaseResponse baseResponse = guanyierpProvider.RefundTrade(refundTradeRequest);
                    if (CommonErrorCode.SUCCESSFUL.equals(baseResponse.getCode())) {
                        //此处更新订单商品为作废状态 TODO duanlsh
                        log.info("管易云取消订单 子订单号：{} 商品:{} 拦截成功", returnOrderVO.getPtid(),tradeItemParam.getSkuId());
                    } else {
                        //取消商品失败
                        throw new SbcRuntimeException("K-050141", new Object[]{returnOrderVO.getPtid(), tradeItemParam.getSpuName()});
                    }
                }

                //普通订单 拦截赠品,取消订单，赠品全部取消
                if (!CollectionUtils.isEmpty(providerTradeParam.getGifts())){
                    providerTradeParam.getGifts().forEach(giftVO -> {
                        RefundTradeRequest refundTradeRequest = RefundTradeRequest.builder().tid(providerTradeParam.getId()).oid(giftVO.getOid()).build();
                        BaseResponse baseResponse = guanyierpProvider.RefundTrade(refundTradeRequest);
                        if (CommonErrorCode.SUCCESSFUL.equals(baseResponse.getCode())) {
                            //此处更新订单商品为作废状态 TODO duanlsh
                        }
                    });
                }
            }
        }

        /****************商品拦截 end*******************/

        //拦截周期购订单
        tradeVO.getTradeVOList().forEach(providerTradeVO -> {
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

        if (returnOrderVO.getReturnType().equals(ReturnType.RETURN)) {
            log.info("=============组合商品退货退款拦截未发货的商品：{}==================",tradeVO.getId());
            List<ReturnItemVO> returnGoods = returnOrderVO.getReturnItems();
            List<ReturnItemVO> returnGifts = returnOrderVO.getReturnGifts();
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
