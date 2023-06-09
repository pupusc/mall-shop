package com.wanmi.sbc.erp.provider.impl;
//
//import com.sbc.wanmi.erp.bean.dto.ERPTradeItemDTO;
//import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
//import com.sbc.wanmi.erp.bean.enums.InterceptType;
//import com.sbc.wanmi.erp.bean.enums.RefundPhaseType;
//import com.sbc.wanmi.erp.bean.vo.*;
//import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.common.exception.SbcRuntimeException;
//import com.wanmi.sbc.common.util.DateUtil;
//import com.wanmi.sbc.common.util.KsBeanUtil;
//import com.wanmi.sbc.erp.api.constant.ErpErrorCode;
//import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
//import com.wanmi.sbc.erp.api.request.*;
//import com.wanmi.sbc.erp.api.response.*;
//import com.wanmi.sbc.erp.entity.*;
//import com.wanmi.sbc.erp.request.*;
//import com.wanmi.sbc.erp.response.*;
//import com.wanmi.sbc.erp.service.GuanyierpService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.ObjectUtils;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.stream.Collectors;
//

import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.sbc.wanmi.erp.bean.vo.DeliveryInfoVO;
import com.sbc.wanmi.erp.bean.vo.DeliveryItemVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.erp.api.request.HistoryDeliveryInfoRequest;
import com.wanmi.sbc.erp.api.request.TradeQueryRequest;
import com.wanmi.sbc.erp.api.response.DeliveryStatusResponse;
import com.wanmi.sbc.erp.api.response.QueryTradeResponse;
import com.wanmi.sbc.erp.entity.ERPDelivery;
import com.wanmi.sbc.erp.entity.ERPDeliveryItems;
import com.wanmi.sbc.erp.entity.ERPTrade;
import com.wanmi.sbc.erp.request.ERPDeliveryQueryRequest;
import com.wanmi.sbc.erp.request.ERPHistoryDeliveryInfoRequest;
import com.wanmi.sbc.erp.request.ERPTradeQueryRequest;
import com.wanmi.sbc.erp.response.ERPDeliveryQueryResponse;
import com.wanmi.sbc.erp.response.ERPTradeQueryResponse;
import com.wanmi.sbc.erp.service.GuanyierpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program: sbc-background
 * @description: 管易云ERP接口服务Controller
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 18:11
 **/
@RestController
@Validated
@Slf4j
public class GuanyierpController implements GuanyierpProvider {

    @Autowired
    private GuanyierpService guanyierpService;
//
//    /**
//     * 获取商品库存
//     * @param erpSynGoodsStockRequest
//     * @return
//     */
//    @Override
//    public BaseResponse<SyncGoodsInfoResponse> syncGoodsStock(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest) {
//        ERPGoodsStockQueryRequest goodsStockQueryRequest =
//                ERPGoodsStockQueryRequest.builder().itemCode(erpSynGoodsStockRequest.getSpuCode()).itemSkuCode(erpSynGoodsStockRequest.getSkuCode()).build();
//        Optional<List<ERPGoodsInfoVO>> optionalGoodsStock = guanyierpService.getERPGoodsStock(goodsStockQueryRequest);
//        if (optionalGoodsStock.isPresent()){
//            SyncGoodsInfoResponse erpSyncGoodsStockResponse =
//                    SyncGoodsInfoResponse.builder().erpGoodsInfoVOList(optionalGoodsStock.get()).build();
//            return BaseResponse.success(erpSyncGoodsStockResponse);
//        }
//        return BaseResponse.success(SyncGoodsInfoResponse.builder().build());
//    }
//
//    /**
//     * 获取商品库存
//     * @param pageNum
//     * @param pageSize
//     * @param erpGoodNo
//     * @return
//     */
//    @Override
//    public BaseResponse<ErpStockVo> listWareHoseStock(Integer pageNum, Integer pageSize, String erpGoodNo){
//        return BaseResponse.success(guanyierpService.listWareHoseStock(pageNum, pageSize, erpGoodNo));
//    }
//
////    /**
////     * 获取商品库存
////     */
////    @Override
////    public BaseResponse<ErpStockVo> getUpdatedStock(String startTime, String erpGoodInfoNo, String pageNum, String pageSize) {
////        return BaseResponse.success(guanyierpService.getUpdatedStock(startTime, erpGoodInfoNo, pageNum, pageSize));
////    }
//
//    /**
//     * 同步ERP商品信息
//     * @param erpSynGoodsStockRequest
//     * @return
//     */
//    @Override
//    public BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest) {
//        ERPGoodsQueryRequest erpGoodsQueryRequest = ERPGoodsQueryRequest.builder().code(erpSynGoodsStockRequest.getSpuCode()).build();
//        Optional<ERPGoodsQueryResponse> optionalErpGoodsInfo = guanyierpService.getERPGoodsInfo(erpGoodsQueryRequest);
//        List<ERPGoodsInfoVO> erpGoodsInfoVOList = new ArrayList<>();
//        if (optionalErpGoodsInfo.isPresent()){
//            ERPGoodsStockQueryRequest goodsStockQueryRequest =
//                    ERPGoodsStockQueryRequest.builder()
//                            .itemCode(erpSynGoodsStockRequest.getSpuCode())
//                            .build();
//            Optional<List<ERPGoodsInfoVO>> optionalGoodsStock = guanyierpService.getERPGoodsStock(goodsStockQueryRequest);
//            if (optionalGoodsStock.isPresent()){
//                List<ERPGoodsInfoVO> erpGoodsInfoStockList = optionalGoodsStock.get();
//                ERPGoods erpGoods=  optionalErpGoodsInfo.get().getItems().get(0);
//                optionalErpGoodsInfo.get().getItems().get(0).getSkus().stream().forEach(erpGoodsInfo -> {
//                    ERPGoodsInfoVO erpGoodsInfoVO = ERPGoodsInfoVO.builder()
//                            .itemSkuName(erpGoodsInfo.getName())
//                            .skuCode(erpGoodsInfo.getCode())
//                            .costPrice(erpGoodsInfo.getCostPrice())
//                            .itemCode(erpGoods.getCode())
//                            .itemName(erpGoods.getName())
//                            .build();
//                    //填充库存数据（采用erp可销售数据,取出最大困的值）
//                    Optional<ERPGoodsInfoVO> goodsInfoVOOptional = erpGoodsInfoStockList.stream()
//                            .filter(erpGoodsInfoStock -> !ObjectUtils.isEmpty(erpGoodsInfoStock)
//                                    && erpGoodsInfoStock.getSkuCode().equals(erpGoodsInfo.getCode()))
//                            .max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty));
//                    if (goodsInfoVOOptional.isPresent()){
//                        erpGoodsInfoVO.setSalableQty(goodsInfoVOOptional.get().getSalableQty());
//                        //库存数量使用erp的可销售数量
//                        erpGoodsInfoVO.setQty(goodsInfoVOOptional.get().getSalableQty());
//                    }
//                    erpGoodsInfoVOList.add(erpGoodsInfoVO);
//                });
//            }
//        }
//        return BaseResponse.success(SyncGoodsInfoResponse.builder().erpGoodsInfoVOList(erpGoodsInfoVOList).build());
//    }
//
//
//
//    /**
//     * 获取ERP商品信息,不带库存
//     */
//    @Override
//    public BaseResponse<List<ERPGoodsInfoVO>> getErpGoodsInfoWithoutStock(String erpGoodsNum) {
//        ERPGoodsQueryRequest erpGoodsQueryRequest = ERPGoodsQueryRequest.builder().code(erpGoodsNum).build();
//        Optional<ERPGoodsQueryResponse> optionalErpGoodsInfo = guanyierpService.getERPGoodsInfo(erpGoodsQueryRequest);
//        List<ERPGoodsInfoVO> erpGoodsInfoVOList = new ArrayList<>();
//        if(optionalErpGoodsInfo.isPresent()) {
//            ERPGoods erpGoods=  optionalErpGoodsInfo.get().getItems().get(0);
//            erpGoods.getSkus().stream().forEach(erpGoodsInfo -> {
//                ERPGoodsInfoVO erpGoodsInfoVO = new ERPGoodsInfoVO();
//                erpGoodsInfoVO.setItemSkuName(erpGoodsInfo.getName());
//                erpGoodsInfoVO.setSkuCode(erpGoodsInfo.getCode());
//                erpGoodsInfoVO.setCostPrice(erpGoodsInfo.getCostPrice());
//                erpGoodsInfoVO.setStockStatusCode(erpGoodsInfo.getStockStatusCode());
//                erpGoodsInfoVO.setItemCode(erpGoods.getCode());
//                erpGoodsInfoVO.setItemName(erpGoods.getName());
//                if (StringUtils.isNotBlank(erpGoods.getStockStatusCode())) {
//                    erpGoodsInfoVO.setStockStatusCode(erpGoods.getStockStatusCode());
//                }
//
//                //成本价
//                if (erpGoods.getCostPrice() != null && erpGoods.getCostPrice().compareTo(new BigDecimal("0")) > 0) {
//                    erpGoodsInfoVO.setCostPrice(erpGoods.getCostPrice());
//                }
//                erpGoodsInfoVOList.add(erpGoodsInfoVO);
//            });
//        }
//        return BaseResponse.success(erpGoodsInfoVOList);
//    }
//
//    /**
//     * 推送订单到ERP
//     * @param request
//     * @return
//     */
//    @Override
//    public BaseResponse autoPushTrade(@RequestBody @Valid PushTradeRequest request) {
//        ERPPushTradeRequest erpPushTradeRequest = KsBeanUtil.convert(request, ERPPushTradeRequest.class);
//        Optional<ERPPushTradeResponse> erpPushTradeResponse = guanyierpService.pushTrade(erpPushTradeRequest);
//        if (erpPushTradeResponse.isPresent()){
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
//    }
//
//    /**
//     * 推送订单到ERP--已发货
//     * @param request
//     * @return
//     */
//    @Override
//    public BaseResponse autoPushTradeDelivered(@RequestBody @Valid PushTradeRequest request) {
//        ERPPushTradeRequest erpPushTradeRequest = KsBeanUtil.convert(request, ERPPushTradeRequest.class);
//        Optional<ERPPushTradeResponse> erpPushTradeResponse = guanyierpService.pushTradeDelivered(erpPushTradeRequest);
//        if (erpPushTradeResponse.isPresent()){
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
//    }
//
//    /**
//     * 获取仓库列表
//     * @param wareHouseQueryRequest
//     * @return
//     */
//    @Override
//    public BaseResponse<WareHouseListResponse> getWareHouseList(@Valid WareHouseQueryRequest wareHouseQueryRequest) {
//        ERPWareHouseQueryRequest queryRequest = ERPWareHouseQueryRequest.builder().hasDelData(false).build();
//        Optional<ERPWareHouseQueryResponse> optionalWareHouseQueryResponse = guanyierpService.getWarehouseList(queryRequest);
//        if (optionalWareHouseQueryResponse.isPresent()){
//            List<ERPWareHouse> warehouses = optionalWareHouseQueryResponse.get().getWarehouses();
//            List<ERPWareHouseVO> erpWareHouseVOS = KsBeanUtil.copyListProperties(warehouses, ERPWareHouseVO.class);
//            WareHouseListResponse wareHouseListResponse = WareHouseListResponse.builder().wareHouseVOList(erpWareHouseVOS).build();
//            return BaseResponse.success(wareHouseListResponse);
//        }
//        return BaseResponse.success(WareHouseListResponse.builder().build());
//    }
//
    /**
     * 获取订单对应的发货单
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DeliveryStatusResponse> getDeliveryStatus(@Valid DeliveryQueryRequest request) {
        ERPDeliveryQueryRequest deliveryQueryRequest = ERPDeliveryQueryRequest.builder()
                .outerCode(request.getTid())
                .delivery(request.getDelivery())
                .build();
        Optional<ERPDeliveryQueryResponse> optionalDeliveryQueryResponse =
                guanyierpService.getERPDeliveryInfo(deliveryQueryRequest);
        if (optionalDeliveryQueryResponse.isPresent() && CollectionUtils.isNotEmpty(optionalDeliveryQueryResponse.get().getDeliverys())){
            // 组装erp返回的发货单信息
            DeliveryStatusResponse deliveryStatusResponse = this.packageDeliveryData(optionalDeliveryQueryResponse);
            return BaseResponse.success(deliveryStatusResponse);
        }
        return BaseResponse.success(DeliveryStatusResponse.builder().deliveryInfoVOList(new ArrayList<>()).build());
    }
//
//
//    /**
//     * 退款商品中止发货
//     * @param request
//     * @return
//     */
//    public BaseResponse refundTradeItem(@Valid RefundTradeRequest request) {
//        ERPRefundUpdateRequest refundUpdateRequest = new ERPRefundUpdateRequest();
//        refundUpdateRequest.setTid(request.getTid());
//        refundUpdateRequest.setOid(request.getOid());
//        refundUpdateRequest.setRefundState(1);
//        Optional<ERPRefundUpdateResponse> refundUpdateReponseOptional = guanyierpService.refundOrderUpdate(refundUpdateRequest);
//        if (refundUpdateReponseOptional.isPresent()){
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
//    }
//
//    /**
//     * 订单拦截
//     * @param request
//     * @return
//     */
//    public BaseResponse refundTradeOrder(@Valid RefundTradeRequest request) {
//        //进行订单拦截
//        ERPTradeInterceptRequest tradeInterceptRequest = new ERPTradeInterceptRequest();
//        tradeInterceptRequest.setOperateType(1);
//        tradeInterceptRequest.setPlatformCode(request.getTid());
//        tradeInterceptRequest.setTradeHoldCode(InterceptType.CANCEL_DELIVERY.getCode());
//        tradeInterceptRequest.setTradeHoldReason(InterceptType.CANCEL_DELIVERY.getReason());
//        Optional<ERPBaseResponse> optionalERPBaseResponse = guanyierpService.interceptTrade(tradeInterceptRequest);
//        if (optionalERPBaseResponse.isPresent() && optionalERPBaseResponse.get().isSuccess()){
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
//    }
//    /**
//     * 订单仅退款
//     * @param request
//     * @return
//     */
//    @Override
//    public BaseResponse RefundTrade(@Valid RefundTradeRequest request) {
//        ERPRefundUpdateRequest refundUpdateRequest = KsBeanUtil.copyPropertiesThird(request,
//                ERPRefundUpdateRequest.class);
//        Optional<ERPRefundUpdateResponse> refundUpdateReponseOptional =
//                guanyierpService.refundOrderUpdate(refundUpdateRequest);
//        if (refundUpdateReponseOptional.isPresent()){
//            //           log.info("--->> guanyierpController RefundTrade tid:{} oid:{} isIntercept:{}", request.getTid(), request.getOid(), request.getIsIntercept());
////            if (request.getIsIntercept() != null && Objects.equals(request.getIsIntercept(), 1)) {
////                log.info("--->> guanyierpController RefundTrade tid:{} oid:{} 当前强制退款，不需要管易云执行终止操作", request.getTid(), request.getOid());
////                return BaseResponse.SUCCESSFUL();
////            }
//
////            //进行订单拦截
////            ERPTradeInterceptRequest tradeInterceptRequest = ERPTradeInterceptRequest.builder()
////                    .operateType(1)
////                    .platformCode(request.getTid())
////                    .tradeHoldCode(InterceptType.CANCEL_DELIVERY.getCode())
////                    .tradeHoldReason(InterceptType.CANCEL_DELIVERY.getReason())
////                    .build();
////            Optional<ERPBaseResponse> optionalERPBaseResponse = guanyierpService.interceptTrade(tradeInterceptRequest);
////            if (optionalERPBaseResponse.isPresent() && optionalERPBaseResponse.get().isSuccess()){
////                return BaseResponse.SUCCESSFUL();
////            }
//            return BaseResponse.SUCCESSFUL();
//        }
//        return BaseResponse.FAILED();
//    }
//
//    /**
//     * 创建退货单
//     * @param requst
//     * @return
//     */
//    @Override
//    public BaseResponse createReturnOrder(@Valid ReturnTradeCreateRequst requst) {
//        List<ERPReturnTradeItem> list = new ArrayList<>();
//        requst.getTradeItems().forEach(returnTradeItemVO -> {
//            ERPReturnTradeItem erpReturnTradeItem = ERPReturnTradeItem.builder()
//                    .itemCode(returnTradeItemVO.getSpuCode())
//                    .skuCode(returnTradeItemVO.getSkuCode())
//                    .qty(returnTradeItemVO.getQty()).build();
//            list.add(erpReturnTradeItem);
//        });
//        List<ERPTradePayment> erpTradePayments = KsBeanUtil.copyListProperties(requst.getRefundDetail(), ERPTradePayment.class);
//        ERPReturnTradeCreateRequest erpReturnTradeCreateRequest = ERPReturnTradeCreateRequest.builder()
//                .typeCode(requst.getTypeCode())
//                .returnType(requst.getReturnType())
//                .tradePlatformCode(requst.getTradeNo())
//                .vipCode(requst.getBuyerMobile())
//                .shopCode(requst.getShopCode())
//                .itemDetail(list)
//                .refundDetail(erpTradePayments)
//                .refundPhase(RefundPhaseType.RECEIVED.getCode())
//                .receiverName(requst.getReceiveName())
//                .receiverMobile(requst.getReceiverMobile())
//                .receiverProvince(requst.getReceiverProvince())
//                .receiverCity(requst.getReceiverCity())
//                .receiverDistrict(requst.getReceiverDistrict())
//                .receiverAddress(requst.getReceiverAddress())
//                .expressName(Objects.nonNull(requst.getExpressName())  ? requst.getExpressName(): null)
//                .expressNum(Objects.nonNull(requst.getExpressNum()) ? requst.getExpressNum() : null)
////                .note("duanlsh这个是一个退货备注")
//                .build();
//        Optional<ERPBaseResponse> optionalERPBaseResponse = guanyierpService.createReturnTrade(erpReturnTradeCreateRequest);
//        if (optionalERPBaseResponse.isPresent() && optionalERPBaseResponse.get().isSuccess()){
//            return BaseResponse.SUCCESSFUL();
//        }
//        return  BaseResponse.FAILED();
//    }
//
//    /**
//     * 退货单查询
//     * @param request
//     * @return
//     */
//    @Override
//    public BaseResponse<ReturnTradeResponse> getReturnTradeStatus(@Valid ReturnTradeQueryRequest request) {
//        ERPReturnTradeQueryRequest erpReturnTradeQueryRequest =
//                ERPReturnTradeQueryRequest.builder().platformCode(request.getTradeNo()).build();
//        Optional<ERPReturnTradeResponse> tradeResponseOptional = guanyierpService.getReturnTrade(erpReturnTradeQueryRequest);
//        if (tradeResponseOptional.isPresent() && tradeResponseOptional.get().isSuccess()){
//            Map<String, Integer> returnTradeStatusMap =
//                    tradeResponseOptional.get().getTradeReturns().stream().collect(Collectors.toMap(ERPReturnTrade::getPlatformCode, ERPReturnTrade::getReceive));
//            ReturnTradeResponse returnTradeResponse = ReturnTradeResponse.builder().receiveStatusMap(returnTradeStatusMap).build();
//            return BaseResponse.success(returnTradeResponse);
//        }
//        return BaseResponse.FAILED();
//    }
//
    /**
     * 获取历史订单对应的发货单
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DeliveryStatusResponse> getHistoryDeliveryStatus(@Valid HistoryDeliveryInfoRequest request) {
        ERPHistoryDeliveryInfoRequest historyDeliveryInfoRequest = ERPHistoryDeliveryInfoRequest.builder()
                .startDeliveryDate(request.getStartDeliveryDate())
                .endDeliveryDate(request.getEndDeliveryDate())
                .pageSize(request.getPageSize())
                .pageNum(request.getPageNum())
                .build();
        Optional<ERPDeliveryQueryResponse> optionalDeliveryQueryResponse =
                guanyierpService.getERPHistoryDeliveryInfo(historyDeliveryInfoRequest);
        if (optionalDeliveryQueryResponse.isPresent() && CollectionUtils.isNotEmpty(optionalDeliveryQueryResponse.get().getDeliverys())){
            // 组装erp返回的发货单信息
            DeliveryStatusResponse deliveryStatusResponse = this.packageDeliveryData(optionalDeliveryQueryResponse);
            return BaseResponse.success(deliveryStatusResponse);
        }
        return BaseResponse.success(DeliveryStatusResponse.builder().build());
    }

    /**
     * 查询ERP订单
     * @param request
     * @return
     */
    @Override
    public BaseResponse<QueryTradeResponse> getTradeInfo(@Valid TradeQueryRequest request) {
        ERPTradeQueryRequest erpTradeQueryRequest = ERPTradeQueryRequest.builder().platformCode(request.getTid()).build();
        Optional<ERPTradeQueryResponse> optional = guanyierpService.getErpTradeInfo(erpTradeQueryRequest,request.getFlag());
        if (optional.isPresent() && CollectionUtils.isNotEmpty(optional.get().getOrders())){
            ERPTrade erpTrade = optional.get().getOrders().get(0);
            QueryTradeResponse response = QueryTradeResponse.builder().platformCode(erpTrade.getPlatformCode()).build();
            //将erp返回的发货状态转成商城的发货状态枚举值
            switch(erpTrade.getDeliveryState()){
                case 0:
                    response.setDeliveryState(DeliveryStatus.UN_DELIVERY);
                    break;
                case 1:
                    response.setDeliveryState(DeliveryStatus.PART_DELIVERY);
                    break;
                case 2:
                    response.setDeliveryState(DeliveryStatus.DELIVERY_COMPLETE);
                default:
                    break;
            }
            return BaseResponse.success(response);
        }else{
            return BaseResponse.success(QueryTradeResponse.builder().build());
        }
    }

    /**
     * 组装erp返回的发货单信息
     * @param optionalDeliveryQueryResponse
     * @return
     */
    private DeliveryStatusResponse packageDeliveryData(Optional<ERPDeliveryQueryResponse> optionalDeliveryQueryResponse){
        List<ERPDelivery> erpDeliveryList = optionalDeliveryQueryResponse.get().getDeliverys();
        List<DeliveryInfoVO> deliveryInfoVOList =new ArrayList<>();
        erpDeliveryList.stream().forEach(erpDelivery -> {
            List<ERPDeliveryItems> erpDeliveryItems = erpDelivery.getDetails();
            List<DeliveryItemVO> list = KsBeanUtil.copyListProperties(erpDeliveryItems, DeliveryItemVO.class);
            DeliveryInfoVO deliveryInfoVO = KsBeanUtil.copyPropertiesThird(erpDelivery,
                    DeliveryInfoVO.class);
            //设置发货时间
            deliveryInfoVO.setDeliverTime(erpDelivery.getDeliveryStatusInfo().getDeliveryDate());
            deliveryInfoVO.setItemVOList(list);

            //将erp返回的发货状态转成商城的发货状态枚举值
            switch(erpDelivery.getDeliveryStatusInfo().getDelivery()){
                case -1:
                    deliveryInfoVO.setDeliveryStatus(DeliveryStatus.UN_DELIVERY);
                    break;
                case 0:
                    deliveryInfoVO.setDeliveryStatus(DeliveryStatus.UN_DELIVERY);
                    break;
                case 1:
                    deliveryInfoVO.setDeliveryStatus(DeliveryStatus.UN_DELIVERY);
                    break;
                case 2:
                    deliveryInfoVO.setDeliveryStatus(DeliveryStatus.DELIVERY_COMPLETE);
                default:
                    break;
            }
            //deliveryInfoVO.setDeliveryStatus(erpDelivery.getDeliveryStatusInfo().getDelivery());
            deliveryInfoVOList.add(deliveryInfoVO);
        });

        return DeliveryStatusResponse.builder().deliveryInfoVOList(deliveryInfoVOList).build();
    }
}
