package com.wanmi.sbc.returnorder.service;

import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.wanmi.sbc.client.CancelOrderRequest;
import com.wanmi.sbc.client.CancelOrderResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.vo.ReturnItemVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/1/6 9:44 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BoKuService extends AbstractCRMService{


    @Override
    public BaseResponse interceptorErpDeliverStatus(ReturnOrderVO returnOrderVO, Boolean flag){
//        ReturnOrderVO returnOrderVO = super.getReturnOrderVo(returnOrderId);
        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = TradeGetByIdRequest.builder().tid(returnOrderVO.getTid()).build();
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

        List<TradeVO> providerTradeVoList = tradeVO.getTradeVOList().stream().filter(p -> p.getId().equals(returnOrderVO.getPtid())).collect(Collectors.toList());
        for (TradeVO providerTradeParam : providerTradeVoList) {
            BaseResponse<DeliveryStatusResponse> deliveryStatusResponse = bizSupplierClient.getDeliveryStatus(providerTradeParam.getId());
            //表示调用第三方接口异常或者 订单没有推送过去。
            if (deliveryStatusResponse == null || deliveryStatusResponse.getContext() == null) {
                log.error("BoKuService bizSupplierClient getDeliveryStatus tradeId:{} pid: {} result content is null 获取订单信息的时候异常", tradeVO.getId(), providerTradeParam.getId());
                throw new SbcRuntimeException(null, "K-050143", "获取博库订单信息失败,请重试");
            }

            //博库已经取消，本地没有取消
            if (deliveryStatusResponse.getContext() != null
                    && CollectionUtils.isNotEmpty(deliveryStatusResponse.getContext().getDeliveryInfoVOList())
                    && DeliveryStatus.CANCELED.equals(deliveryStatusResponse.getContext().getDeliveryInfoVOList().get(0).getDeliveryStatus())) {
                log.info("BoKuService bizSupplierClient tradeId:{} pid: {} 博库申请退款 博库第三方不存在该商品，直接取消该商品；", tradeVO.getId(), providerTradeParam.getId());
                return BaseResponse.SUCCESSFUL();
            }
            //如果博库已经发货
            //博库已经取消，本地没有取消
            Map<String, TradeItemVO> skuId2TradeItemMap =
                    providerTradeParam.getTradeItems().stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity(), (k1, k2) -> k2));

            if (deliveryStatusResponse.getContext() != null
                    && CollectionUtils.isNotEmpty(deliveryStatusResponse.getContext().getDeliveryInfoVOList())
                    && DeliveryStatus.UN_DELIVERY.equals(deliveryStatusResponse.getContext().getDeliveryInfoVOList().get(0).getDeliveryStatus())) {

                for (ReturnItemVO returnItemParam : returnOrderVO.getReturnItems()) {
                    TradeItemVO tradeItemVO = skuId2TradeItemMap.get(returnItemParam.getSkuId());
                    if (tradeItemVO == null) {
                        throw new SbcRuntimeException("K-000009");
                    }
                    CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
                    cancelOrderRequest.setOrderId(providerTradeParam.getDeliveryOrderId());
                    cancelOrderRequest.setPid(providerTradeParam.getId());
                    cancelOrderRequest.setType(1); //单品
                    cancelOrderRequest.setErpGoodsInfoNo(tradeItemVO.getErpSkuNo());
                    BaseResponse<CancelOrderResponse> response = bizSupplierClient.cancelOrder(cancelOrderRequest);
                    if (response == null || response.getContext() == null) {
                        throw new SbcRuntimeException(null, "K-050143", "取消博库售后订单失败");
                    }
                    if(!Objects.equals(response.getContext().getStatus(),1)){
                        throw new SbcRuntimeException(null, "K-050143", "订单取消失败 " + (StringUtils.isNotEmpty(response.getContext().getErrorMsg()) ? response.getContext().getErrorMsg() : ""));
                    }
                }
            } else {
                //此处表示博库已经发货，则只有商品航全部发货才可以取消退单
                for (ReturnItemVO returnItemParam : returnOrderVO.getReturnItems()) {
                    TradeItemVO tradeItemVO = skuId2TradeItemMap.get(returnItemParam.getSkuId());
                    if (tradeItemVO == null) {
                        throw new SbcRuntimeException("K-000009");
                    }
                    if (!DeliverStatus.SHIPPED.equals(tradeItemVO.getDeliverStatus())) {
                        throw new SbcRuntimeException(null, "K-050511", "退单中包含已经发货的商品行，请同步订单后重新发起售后");
                    }
                }
            }
        }

//
//
//
//        DeliverStatus deliverStatus = tradeVO.getTradeState().getDeliverStatus();
//
//        if (deliverStatus.equals(DeliverStatus.NOT_YET_SHIPPED) || deliverStatus.equals(DeliverStatus.PART_SHIPPED)) {
//            //这里获取的实际上是子单
//            List<TradeVO> providerTradeVoList = tradeVO.getTradeVOList().stream().filter(p -> p.getId().equals(returnOrderVO.getPtid())).collect(Collectors.toList());
//            for (TradeVO providerTradeParam : providerTradeVoList) {
//                BaseResponse<DeliveryStatusResponse> deliveryStatusResponse = bizSupplierClient.getDeliveryStatus(providerTradeParam.getId());
//                //表示调用第三方接口异常
//                if (deliveryStatusResponse == null || deliveryStatusResponse.getContext() == null) {
//                    log.error("BoKuService bizSupplierClient getDeliveryStatus tradeId:{} pid: {} result content is null 获取订单信息的时候异常", tradeVO.getId(), providerTradeParam.getId());
//                    throw new SbcRuntimeException("K-050143", new Object[]{"获取订单网络异常，请稍后重试"});
//                }
//
//                if (deliveryStatusResponse.getContext() != null && CollectionUtils.isEmpty(deliveryStatusResponse.getContext().getDeliveryInfoVOList())) {
//                    log.info("BoKuService bizSupplierClient tradeId:{} pid: {} 博库申请退款 博库第三方不存在该商品，直接取消该商品；", tradeVO.getId(), providerTradeParam.getId());
//                    return BaseResponse.SUCCESSFUL();
//                }
//
//                Map<String, TradeItemVO> skuId2TradeItemMap =
//                        providerTradeParam.getTradeItems().stream().collect(Collectors.toMap(TradeItemVO::getSkuId, Function.identity(), (k1, k2) -> k2));
//
//                List<ReturnItemVO> returnItems = returnOrderVO.getReturnItems();
//                for (ReturnItemVO returnItemParam : returnItems) {
//                    TradeItemVO tradeItemVO = skuId2TradeItemMap.get(returnItemParam.getSkuId());
//                    if (tradeItemVO == null) {
//                        throw new SbcRuntimeException("K-000009");
//                    }
//                    CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
//                    cancelOrderRequest.setOrderId(providerTradeParam.getDeliveryOrderId());
//                    cancelOrderRequest.setPid(providerTradeParam.getId());
//                    cancelOrderRequest.setType(1); //单品
//                    cancelOrderRequest.setErpGoodsInfoNo(tradeItemVO.getErpSkuNo());
//                    BaseResponse<CancelOrderResponse> response = bizSupplierClient.cancelOrder(cancelOrderRequest);
//                    if(response == null || response.getContext() == null || !Objects.equals(response.getContext().getStatus(),1)){
//                        throw new SbcRuntimeException("K-050143", new Object[]{response !=null && response.getContext() != null && StringUtils.isNotEmpty(response.getContext().getErrorMsg())?response.getContext().getErrorMsg():"订单取消失败"});
//                    }
//                }
//
//
//            }
//        }

        if (returnOrderVO.getReturnType().equals(ReturnType.RETURN)) {
            log.info("BoKuService bizSupplierClient tradeId:{} 博库退货退款请走博库平台 ", tradeVO.getId());
        }
//            if(!Objects.equals(defaultProviderId,returnOrderVO.getProviderId())){
//                log.info("博库退货退款请走博库平台");
//                return BaseResponse.SUCCESSFUL();
//            }
        log.info("************* BoKuService bizSupplierClient tradeId: {} 博库申请退款或退货退款 完成 *************", tradeVO.getId());
        return BaseResponse.SUCCESSFUL();
    }

}
