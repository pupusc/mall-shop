package com.wanmi.sbc.erp.provider.impl;

import com.sbc.wanmi.erp.bean.dto.ERPTradeItemDTO;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.sbc.wanmi.erp.bean.enums.InterceptType;
import com.sbc.wanmi.erp.bean.enums.RefundPhaseType;
import com.sbc.wanmi.erp.bean.vo.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.erp.api.constant.ErpErrorCode;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.*;
import com.wanmi.sbc.erp.api.response.*;
import com.wanmi.sbc.erp.entity.*;
import com.wanmi.sbc.erp.request.*;
import com.wanmi.sbc.erp.response.*;
import com.wanmi.sbc.erp.service.GuanyierpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 获取商品库存
     * @param erpSynGoodsStockRequest
     * @return
     */
    @Override
    public BaseResponse<SyncGoodsInfoResponse> syncGoodsStock(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest) {
        ERPGoodsStockQueryRequest goodsStockQueryRequest =
                ERPGoodsStockQueryRequest.builder().itemCode(erpSynGoodsStockRequest.getSpuCode()).itemSkuCode(erpSynGoodsStockRequest.getSkuCode()).build();
        Optional<List<ERPGoodsInfoVO>> optionalGoodsStock = guanyierpService.getERPGoodsStock(goodsStockQueryRequest);
        if (optionalGoodsStock.isPresent()){
            SyncGoodsInfoResponse erpSyncGoodsStockResponse =
                    SyncGoodsInfoResponse.builder().erpGoodsInfoVOList(optionalGoodsStock.get()).build();
            return BaseResponse.success(erpSyncGoodsStockResponse);
        }
        return BaseResponse.success(SyncGoodsInfoResponse.builder().build());
    }

    /**
     * 获取商品库存
     */
    @Override
    public BaseResponse<List<ERPGoodsInfoVO>> getUpdatedStock(String startTime, String erpGoodInfoNo) {
        return BaseResponse.success(guanyierpService.getUpdatedStock(startTime, erpGoodInfoNo));
    }

    /**
     * 同步ERP商品信息
     * @param erpSynGoodsStockRequest
     * @return
     */
    @Override
    public BaseResponse<SyncGoodsInfoResponse> syncGoodsInfo(@RequestBody @Valid SynGoodsInfoRequest erpSynGoodsStockRequest) {
        ERPGoodsQueryRequest erpGoodsQueryRequest = ERPGoodsQueryRequest.builder().code(erpSynGoodsStockRequest.getSpuCode()).build();
        Optional<ERPGoodsQueryResponse> optionalErpGoodsInfo = guanyierpService.getERPGoodsInfo(erpGoodsQueryRequest);
        List<ERPGoodsInfoVO> erpGoodsInfoVOList = new ArrayList<>();
        if (optionalErpGoodsInfo.isPresent()){
            ERPGoodsStockQueryRequest goodsStockQueryRequest =
                    ERPGoodsStockQueryRequest.builder()
                            .itemCode(erpSynGoodsStockRequest.getSpuCode())
                            .build();
            Optional<List<ERPGoodsInfoVO>> optionalGoodsStock = guanyierpService.getERPGoodsStock(goodsStockQueryRequest);
            if (optionalGoodsStock.isPresent()){
                List<ERPGoodsInfoVO> erpGoodsInfoStockList = optionalGoodsStock.get();
                ERPGoods erpGoods=  optionalErpGoodsInfo.get().getItems().get(0);
                optionalErpGoodsInfo.get().getItems().get(0).getSkus().stream().forEach(erpGoodsInfo -> {
                    ERPGoodsInfoVO erpGoodsInfoVO = ERPGoodsInfoVO.builder()
                            .itemSkuName(erpGoodsInfo.getName())
                            .skuCode(erpGoodsInfo.getCode())
                            .costPrice(erpGoodsInfo.getCostPrice())
                            .itemCode(erpGoods.getCode())
                            .itemName(erpGoods.getName())
                            .build();
                    //填充库存数据（采用erp可销售数据,取出最大困的值）
                    Optional<ERPGoodsInfoVO> goodsInfoVOOptional = erpGoodsInfoStockList.stream()
                            .filter(erpGoodsInfoStock -> !ObjectUtils.isEmpty(erpGoodsInfoStock)
                                    && erpGoodsInfoStock.getSkuCode().equals(erpGoodsInfo.getCode()))
                            .max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty));
                    if (goodsInfoVOOptional.isPresent()){
                        erpGoodsInfoVO.setSalableQty(goodsInfoVOOptional.get().getSalableQty());
                        //库存数量使用erp的可销售数量
                        erpGoodsInfoVO.setQty(goodsInfoVOOptional.get().getSalableQty());
                    }
                    erpGoodsInfoVOList.add(erpGoodsInfoVO);
                });
            }
        }
        return BaseResponse.success(SyncGoodsInfoResponse.builder().erpGoodsInfoVOList(erpGoodsInfoVOList).build());
    }

    /**
     * 获取ERP商品信息,不带库存
     */
    @Override
    public BaseResponse<List<ERPGoodsInfoVO>> getErpGoodsInfoWithoutStock(String erpGoodsNum) {
        ERPGoodsQueryRequest erpGoodsQueryRequest = ERPGoodsQueryRequest.builder().code(erpGoodsNum).build();
        Optional<ERPGoodsQueryResponse> optionalErpGoodsInfo = guanyierpService.getERPGoodsInfo(erpGoodsQueryRequest);
        List<ERPGoodsInfoVO> erpGoodsInfoVOList = new ArrayList<>();
        if(optionalErpGoodsInfo.isPresent()) {
            ERPGoods erpGoods=  optionalErpGoodsInfo.get().getItems().get(0);
            erpGoods.getSkus().stream().forEach(erpGoodsInfo -> {
                ERPGoodsInfoVO erpGoodsInfoVO = ERPGoodsInfoVO.builder()
                        .itemSkuName(erpGoodsInfo.getName()).skuCode(erpGoodsInfo.getCode())
                        .costPrice(erpGoodsInfo.getCostPrice()).stockStatusCode(erpGoodsInfo.getStockStatusCode())
                        .itemCode(erpGoods.getCode()).itemName(erpGoods.getName())
                        .build();
                erpGoodsInfoVOList.add(erpGoodsInfoVO);
            });
        }
        return BaseResponse.success(erpGoodsInfoVOList);
    }

    /**
     * 推送订单到ERP
     * @param request
     * @return
     */
    @Override
    public BaseResponse autoPushTrade(@RequestBody @Valid PushTradeRequest request) {
        ERPPushTradeRequest erpPushTradeRequest = KsBeanUtil.convert(request, ERPPushTradeRequest.class);
        Optional<ERPPushTradeResponse> erpPushTradeResponse = guanyierpService.pushTrade(erpPushTradeRequest);
        if (erpPushTradeResponse.isPresent()){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 推送订单到ERP--已发货
     * @param request
     * @return
     */
    @Override
    public BaseResponse autoPushTradeDelivered(@RequestBody @Valid PushTradeRequest request) {
        ERPPushTradeRequest erpPushTradeRequest = KsBeanUtil.convert(request, ERPPushTradeRequest.class);
        Optional<ERPPushTradeResponse> erpPushTradeResponse = guanyierpService.pushTradeDelivered(erpPushTradeRequest);
        if (erpPushTradeResponse.isPresent()){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 获取仓库列表
     * @param wareHouseQueryRequest
     * @return
     */
    @Override
    public BaseResponse<WareHouseListResponse> getWareHouseList(@Valid WareHouseQueryRequest wareHouseQueryRequest) {
        ERPWareHouseQueryRequest queryRequest = ERPWareHouseQueryRequest.builder().hasDelData(false).build();
        Optional<ERPWareHouseQueryResponse> optionalWareHouseQueryResponse = guanyierpService.getWarehouseList(queryRequest);
        if (optionalWareHouseQueryResponse.isPresent()){
            List<ERPWareHouse> warehouses = optionalWareHouseQueryResponse.get().getWarehouses();
            List<ERPWareHouseVO> erpWareHouseVOS = KsBeanUtil.copyListProperties(warehouses, ERPWareHouseVO.class);
            WareHouseListResponse wareHouseListResponse = WareHouseListResponse.builder().wareHouseVOList(erpWareHouseVOS).build();
            return BaseResponse.success(wareHouseListResponse);
        }
        return BaseResponse.success(WareHouseListResponse.builder().build());
    }

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
        return BaseResponse.success(DeliveryStatusResponse.builder().build());
    }

    /**
     * 订单仅退款
     * @param request
     * @return
     */
    @Override
    public BaseResponse RefundTrade(@Valid RefundTradeRequest request) {
        ERPRefundUpdateRequest refundUpdateRequest = KsBeanUtil.copyPropertiesThird(request,
                ERPRefundUpdateRequest.class);
        Optional<ERPRefundUpdateResponse> refundUpdateReponseOptional =
                guanyierpService.refundOrderUpdate(refundUpdateRequest);
        if (refundUpdateReponseOptional.isPresent()){
            //           log.info("--->> guanyierpController RefundTrade tid:{} oid:{} isIntercept:{}", request.getTid(), request.getOid(), request.getIsIntercept());
//            if (request.getIsIntercept() != null && Objects.equals(request.getIsIntercept(), 1)) {
//                log.info("--->> guanyierpController RefundTrade tid:{} oid:{} 当前强制退款，不需要管易云执行终止操作", request.getTid(), request.getOid());
//                return BaseResponse.SUCCESSFUL();
//            }
            //进行订单拦截
            ERPTradeInterceptRequest tradeInterceptRequest = ERPTradeInterceptRequest.builder()
                    .operateType(1)
                    .platformCode(request.getTid())
                    .tradeHoldCode(InterceptType.CANCEL_DELIVERY.getCode())
                    .tradeHoldReason(InterceptType.CANCEL_DELIVERY.getReason())
                    .build();
            Optional<ERPBaseResponse> optionalERPBaseResponse = guanyierpService.interceptTrade(tradeInterceptRequest);
            if (optionalERPBaseResponse.isPresent() && optionalERPBaseResponse.get().isSuccess()){
                return BaseResponse.SUCCESSFUL();
            }
        }
        return BaseResponse.FAILED();
    }

    /**
     * 创建退货单
     * @param requst
     * @return
     */
    @Override
    public BaseResponse createReturnOrder(@Valid ReturnTradeCreateRequst requst) {
        List<ERPReturnTradeItem> list = new ArrayList<>();
        requst.getTradeItems().forEach(returnTradeItemVO -> {
            ERPReturnTradeItem erpReturnTradeItem = ERPReturnTradeItem.builder()
                    .itemCode(returnTradeItemVO.getSpuCode())
                    .skuCode(returnTradeItemVO.getSkuCode())
                    .qty(returnTradeItemVO.getQty()).build();
            list.add(erpReturnTradeItem);
        });
        List<ERPTradePayment> erpTradePayments = KsBeanUtil.copyListProperties(requst.getRefundDetail(), ERPTradePayment.class);
        ERPReturnTradeCreateRequest erpReturnTradeCreateRequest = ERPReturnTradeCreateRequest.builder()
                .typeCode(requst.getTypeCode())
                .returnType(requst.getReturnType())
                .tradePlatformCode(requst.getTradeNo())
                .vipCode(requst.getBuyerMobile())
                .shopCode(requst.getShopCode())
                .itemDetail(list)
                .refundDetail(erpTradePayments)
                .refundPhase(RefundPhaseType.RECEIVED.getCode())
                .receiverName(requst.getReceiveName())
                .receiverMobile(requst.getReceiverMobile())
                .receiverProvince(requst.getReceiverProvince())
                .receiverCity(requst.getReceiverCity())
                .receiverDistrict(requst.getReceiverDistrict())
                .receiverAddress(requst.getReceiverAddress())
                .expressName(Objects.nonNull(requst.getExpressName())  ? requst.getExpressName(): null)
                .expressNum(Objects.nonNull(requst.getExpressNum()) ? requst.getExpressNum() : null)
                .build();
        Optional<ERPBaseResponse> optionalERPBaseResponse = guanyierpService.createReturnTrade(erpReturnTradeCreateRequest);
        if (optionalERPBaseResponse.isPresent() && optionalERPBaseResponse.get().isSuccess()){
            return BaseResponse.SUCCESSFUL();
        }
        return  BaseResponse.FAILED();
    }

    /**
     * 退货单查询
     * @param request
     * @return
     */
    @Override
    public BaseResponse<ReturnTradeResponse> getReturnTradeStatus(@Valid ReturnTradeQueryRequest request) {
        ERPReturnTradeQueryRequest erpReturnTradeQueryRequest =
                ERPReturnTradeQueryRequest.builder().platformCode(request.getTradeNo()).build();
        Optional<ERPReturnTradeResponse> tradeResponseOptional = guanyierpService.getReturnTrade(erpReturnTradeQueryRequest);
        if (tradeResponseOptional.isPresent() && tradeResponseOptional.get().isSuccess()){
            Map<String, Integer> returnTradeStatusMap =
                    tradeResponseOptional.get().getTradeReturns().stream().collect(Collectors.toMap(ERPReturnTrade::getPlatformCode, ERPReturnTrade::getReceive));
            ReturnTradeResponse returnTradeResponse = ReturnTradeResponse.builder().receiveStatusMap(returnTradeStatusMap).build();
            return BaseResponse.success(returnTradeResponse);
        }
        return BaseResponse.FAILED();
    }

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
    public DeliveryStatusResponse packageDeliveryData(Optional<ERPDeliveryQueryResponse> optionalDeliveryQueryResponse){
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
