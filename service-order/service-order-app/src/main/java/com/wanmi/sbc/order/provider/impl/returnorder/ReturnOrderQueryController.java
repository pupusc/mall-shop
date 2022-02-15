package com.wanmi.sbc.order.provider.impl.returnorder;

import com.aliyuncs.linkedmall.model.v20180116.QueryRefundApplicationDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.linkedmall.api.provider.order.LinkedMallOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.returnorder.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.order.SbcOrderQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.returnorder.SbcQueryRefundApplicationDetailRequest;
import com.wanmi.sbc.linkedmall.api.response.order.SbcOrderQueryResponse;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.*;
import com.wanmi.sbc.order.api.response.returnorder.*;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.vo.*;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.request.ReturnQueryRequest;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeDeliver;
import com.wanmi.sbc.order.trade.model.entity.value.TradeCycleBuyInfo;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>退单服务查询接口</p>
 *
 * @Author: daiyitian
 * @Description: 退单服务查询接口
 * @Date: 2018-12-03 15:40
 */
@Validated
@RestController
public class ReturnOrderQueryController implements ReturnOrderQueryProvider {

    @Autowired
    private ReturnOrderService returnOrderService;

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private LinkedMallTradeService thirdPlatformTradeService;

    @Autowired
    private LinkedMallOrderQueryProvider linkedMallOrderQueryProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @Autowired
    private TradeService tradeService;

    /**
     * 根据userId获取退单快照
     *
     * @param request 根据userId获取退单快照请求结构 {@link ReturnOrderTransferByUserIdRequest}
     * @return 退单快照 {@link ReturnOrderTransferByUserIdResponse}
     */
    @Override
    public BaseResponse<ReturnOrderTransferByUserIdResponse> getTransferByUserId(@RequestBody @Valid
                                                                                         ReturnOrderTransferByUserIdRequest
                                                                                         request) {
        ReturnOrder returnOrder = returnOrderService.findTransfer(request.getUserId());
        if (Objects.nonNull(returnOrder)) {
            return BaseResponse.success(KsBeanUtil.convert(returnOrder, ReturnOrderTransferByUserIdResponse.class));
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据动态条件统计退单
     *
     * @param request 根据动态条件统计退单请求结构 {@link ReturnCountByConditionRequest}
     * @return 退单数 {@link ReturnCountByConditionResponse}
     */
    @Override
    public BaseResponse<ReturnCountByConditionResponse> countByCondition(@RequestBody @Valid
                                                                                 ReturnCountByConditionRequest
                                                                                     request) {
        Long count = returnOrderService.countNum(KsBeanUtil.convert(request, ReturnQueryRequest.class));
        return BaseResponse.success(ReturnCountByConditionResponse.builder().count(count).build());
    }

    /**
     * 根据动态条件查询退单分页列表
     *
     * @param request 根据动态条件查询退单分页列表请求结构 {@link ReturnOrderPageRequest}
     * @return 退单分页列表 {@link ReturnOrderPageResponse}
     */
    @Override
    public BaseResponse<ReturnOrderPageResponse> page(@RequestBody @Valid ReturnOrderPageRequest request) {
        Page<ReturnOrder> orderPage = returnOrderService.page(KsBeanUtil.convert(request, ReturnQueryRequest.class));

        //查询关联的订单信息
        List<String> tids=orderPage.getContent().stream().map(ReturnOrder::getTid).distinct().collect(Collectors.toList());
        List<Trade>  tradeList= tradeService.details(tids);

        MicroServicePage<ReturnOrderVO> returnOrderVOS = KsBeanUtil.convertPage(orderPage, ReturnOrderVO.class);
        List<ReturnOrderVO> list=returnOrderVOS.getContent();
        list.forEach(returnOrderVO -> {
            tradeList.forEach(trade -> {
                if (Objects.equals(returnOrderVO.getTid(),trade.getId()) && trade.getCycleBuyFlag()) {
                    TradeCycleBuyInfo tradeCycleBuyInfo = trade.getTradeCycleBuyInfo();
                    TradeCycleBuyInfoVO tradeCycleBuyInfoVO= KsBeanUtil.convert(tradeCycleBuyInfo,TradeCycleBuyInfoVO.class);
                    List<DeliverCalendarVO> deliverCalendarVOList=tradeCycleBuyInfoVO.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.SHIPPED).collect(Collectors.toList());
                    //已发期数
                    tradeCycleBuyInfoVO.setAlreadyCycleNum(deliverCalendarVOList.size());
                    returnOrderVO.setTradeCycleBuyInfo(tradeCycleBuyInfoVO);
                    returnOrderVO.setCycleBuyFlag(Boolean.TRUE);
                }
            });
        });

        for(int i=0;i<list.size();i++){
            if (list.get(i).getPtid()!=null){
                ProviderTrade providerTrade=providerTradeService.findbyId(list.get(i).getPtid());
                list.get(i).setTradeVO(KsBeanUtil.convert(providerTrade,TradeVO.class));
            }
        };
        returnOrderVOS.setContent(list);
        return BaseResponse.success(ReturnOrderPageResponse.builder()
                .returnOrderPage(returnOrderVOS).build());
    }

    /**
     * 根据动态条件查询退单列表
     *
     * @param request 根据动态条件查询退单列表请求结构 {@link ReturnOrderByConditionRequest}
     * @return 退单列表 {@link ReturnOrderByConditionResponse}
     */
    @Override
    public BaseResponse<ReturnOrderByConditionResponse> listByCondition(@RequestBody @Valid
                                                                                    ReturnOrderByConditionRequest
                                                                                request) {
        List<ReturnOrder> orderList = returnOrderService.findByCondition(KsBeanUtil.convert(request,
                ReturnQueryRequest.class));
        List<ReturnOrderVO> orderVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderVOList = KsBeanUtil.convert(orderList, ReturnOrderVO.class);
        }
        return BaseResponse.success(ReturnOrderByConditionResponse.builder().returnOrderList(orderVOList).build());
    }

    /**
     * 根据id查询退单
     *
     * @param request 根据id查询退单请求结构 {@link ReturnOrderByIdRequest}
     * @return 退单信息 {@link ReturnOrderByIdResponse}
     */
    @Override
    public BaseResponse<ReturnOrderByIdResponse> getById(@RequestBody @Valid ReturnOrderByIdRequest
                                                                 request) {
        ReturnOrder returnOrder=returnOrderService.findById(request.getRid());
        ProviderTrade providerTrade=providerTradeService.findbyId(returnOrder.getPtid());
        returnOrder.setTradeVO(KsBeanUtil.convert(providerTrade,TradeVO.class));
        // 非linkedmall退单 或者 已填充linkedmall商家留言，直接返回
        if (!ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()) ||
                (ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()) &&
                        StringUtils.isNotBlank(returnOrder.getThirdSellerAgreeMsg()))) {
            return BaseResponse.success(KsBeanUtil.convert(returnOrder, ReturnOrderByIdResponse.class));
        }

        // 当前linkedmall退单中，未填充商品/赠品对应 第三方渠道订单号 的商品总数
        long count = Stream.concat(returnOrder.getReturnItems().stream(),returnOrder.getReturnGifts().stream())
                .filter(returnItem -> StringUtils.isBlank(returnItem.getThirdPlatformSubOrderId()) &&
                        ThirdPlatformType.LINKED_MALL.equals(returnItem.getThirdPlatformType())).count();
        // 获取当前退单中商品/赠品对应 第三方渠道订单号 集合
        List<String> subLmOrderIdList = new ArrayList<>();
        // 更新标记
        AtomicReference<Boolean> updateFlag = new AtomicReference<>(Boolean.FALSE);

        // 查询linkedmall 退单 对应订单详情，填充商品/赠品 对应 第三方渠道订单号
        if(ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()) &&
                StringUtils.isNotBlank(returnOrder.getThirdPlatformOrderId()) && count > 0) {
            SbcOrderQueryResponse response = linkedMallOrderQueryProvider.queryOrderDetailByLmOrderId(SbcOrderQueryRequest.builder()
                    .lmOrderId(returnOrder.getThirdPlatformOrderId())
                    .bizUid(returnOrder.getBuyer().getId()).build()).getContext();
            //只有当有LM商品和申请原因时，才可以查LM退单详情
            if (Objects.nonNull(response) && Objects.nonNull(response.getLmOrderListItem())) {
                // 获取当前退单中商品对应的 第三方渠道订单号 集合
                if (CollectionUtils.isNotEmpty(returnOrder.getReturnItems())) {
                    returnOrder.getReturnItems().forEach(returnItem -> {
                        response.getLmOrderListItem().getSubOrderList().forEach(subOrderListItem -> {
                            if (String.valueOf(subOrderListItem.getItemId()).equals(returnItem.getThirdPlatformSpuId()) &&
                                    String.valueOf(subOrderListItem.getSkuId()).equals(returnItem.getThirdPlatformSkuId())) {
                                subLmOrderIdList.add(String.valueOf(subOrderListItem.getLmOrderId()));
                                // 填充渠道商品订单号
                                if (StringUtils.isBlank(returnItem.getThirdPlatformSubOrderId())) {
                                    returnItem.setThirdPlatformSubOrderId(String.valueOf(subOrderListItem.getLmOrderId()));
                                    updateFlag.set(Boolean.TRUE);
                                }
                            }
                        });
                    });
                }
                // 获取当前退单中赠品对应的 第三方渠道订单号 集合
                if (CollectionUtils.isNotEmpty(returnOrder.getReturnGifts())) {
                    returnOrder.getReturnGifts().forEach(returnItem -> {
                        response.getLmOrderListItem().getSubOrderList().forEach(subOrderListItem -> {
                            if (String.valueOf(subOrderListItem.getItemId()).equals(returnItem.getThirdPlatformSpuId()) &&
                                    String.valueOf(subOrderListItem.getSkuId()).equals(returnItem.getThirdPlatformSkuId())) {
                                subLmOrderIdList.add(String.valueOf(subOrderListItem.getLmOrderId()));
                                // 填充渠道商品订单号
                                if (StringUtils.isBlank(returnItem.getThirdPlatformSubOrderId())) {
                                    returnItem.setThirdPlatformSubOrderId(String.valueOf(subOrderListItem.getLmOrderId()));
                                    updateFlag.set(Boolean.TRUE);
                                }
                            }
                        });
                    });
                }
            }
        } else if(ThirdPlatformType.LINKED_MALL.equals(returnOrder.getThirdPlatformType()) && count == 0) {
            subLmOrderIdList.addAll(Stream.concat(returnOrder.getReturnItems().stream(),returnOrder.getReturnGifts().stream())
                    .filter(returnItem -> ThirdPlatformType.LINKED_MALL.equals(returnItem.getThirdPlatformType()))
                    .map(ReturnItem::getThirdPlatformSubOrderId).collect(Collectors.toList()));
        }

        // 查询linkedmall 退单 的 linkedmall商家留言信息
        if(CollectionUtils.isNotEmpty(subLmOrderIdList) && !ReturnFlowState.INIT.equals(returnOrder.getReturnFlowState()) &&
                Objects.isNull(returnOrder.getThirdSellerAgreeMsg()) && Objects.nonNull(returnOrder.getThirdReasonId())) {
            // 当前退单对应所有linkedmall子单号
            List<String> subLmOrderIds = subLmOrderIdList.stream().distinct().collect(Collectors.toList());
            // 获取当前退单商品对应 所有 商家留言信息 去重后的集合
            List<String> lmSellAgreeMsgs = subLmOrderIds.stream().map(s -> {
                QueryRefundApplicationDetailResponse.RefundApplicationDetail context =
                        linkedMallReturnOrderQueryProvider.queryRefundApplicationDetail(SbcQueryRefundApplicationDetailRequest.builder()
                                .bizUid(returnOrder.getBuyer().getId()).subLmOrderId(s).build()).getContext().getDetail();
                if (Objects.nonNull(context) && StringUtils.isNotBlank(context.getSellerAgreeMsg())) {
                    return context.getSellerAgreeMsg();
                }
                return null;
            }).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            // 如果商家留言信息 只有一个，设置当前退单的退货地址并更新到 数据库中
            if (CollectionUtils.isNotEmpty(lmSellAgreeMsgs) && lmSellAgreeMsgs.size() == 1) {
                returnOrder.setThirdSellerAgreeMsg(lmSellAgreeMsgs.get(0));
                updateFlag.set(Boolean.TRUE);
            }
        }
        if(updateFlag.get()) {
            // 更新退单信息
            returnOrderService.updateReturnOrder(returnOrder);
        }
        return BaseResponse.success(KsBeanUtil.convert(returnOrder, ReturnOrderByIdResponse.class));
    }

    /**
     * 查询所有退货方式
     *
     * @return 退货方式列表 {@link ReturnWayListResponse}
     */
    @Override
    public BaseResponse<ReturnWayListResponse> listReturnWay() {
        return BaseResponse.success(ReturnWayListResponse.builder().returnWayList(returnOrderService.findReturnWay())
                .build());
    }

    /**
     * 查询所有退货原因
     *
     * @return 退货原因列表 {@link ReturnReasonListResponse}
     */
    @Override
    public BaseResponse<ReturnReasonListResponse> listReturnReason(Integer replace) {
        return BaseResponse.success(ReturnReasonListResponse.builder()
                .returnReasonList(returnOrderService.findReturnReason(replace)).build());
    }

    /**
     * 查询可退金额
     *
     * @param request 查询可退金额请求结构 {@link ReturnOrderQueryRefundPriceRequest}
     * @return 可退金额 {@link ReturnOrderQueryRefundPriceResponse}
     */
    @Override
    public BaseResponse<ReturnOrderQueryRefundPriceResponse> queryRefundPrice(@RequestBody @Valid
                                                                                      ReturnOrderQueryRefundPriceRequest
                                                                                      request) {
        return BaseResponse.success(ReturnOrderQueryRefundPriceResponse.builder()
                .refundPrice(returnOrderService.queryRefundPrice(request.getRid())).build());
    }

    /**
     * 根据订单id查询含可退商品的交易信息
     *
     * @param request 根据订单id查询可退商品数请求结构 {@link CanReturnItemNumByTidRequest}
     * @return 含可退商品的交易信息 {@link CanReturnItemNumByTidResponse}
     */
    @Override
    public BaseResponse<CanReturnItemNumByTidResponse> queryCanReturnItemNumByTid(@RequestBody @Valid
                                                                                          CanReturnItemNumByTidRequest
                                                                                          request) {
        return BaseResponse.success(KsBeanUtil.convert(returnOrderService.queryCanReturnItemNumByTid(request.getTid(), request.getReplace()),
                CanReturnItemNumByTidResponse.class));
    }

    /**
     * 根据退单id查询含可退商品的退单信息
     *
     * @param request 根据退单id查询可退商品数请求结构 {@link CanReturnItemNumByIdRequest}
     * @return 含可退商品的退单信息 {@link CanReturnItemNumByIdResponse}
     */
    @Override
    public BaseResponse<CanReturnItemNumByIdResponse> queryCanReturnItemNumById(@RequestBody @Valid
                                                                                        CanReturnItemNumByIdRequest
                                                                                        request) {
        return BaseResponse.success(KsBeanUtil.convert(returnOrderService.queryCanReturnItemNumById(request.getRid()),
                CanReturnItemNumByIdResponse.class));
    }

    /**
     * 根据订单id查询退单列表(不包含已作废状态以及拒绝收货的退货单与拒绝退款的退款单)
     *
     * @param request 查询退单列表请求结构 {@link ReturnOrderNotVoidByTidRequest}
     * @return 退单列表 {@link ReturnOrderNotVoidByTidResponse}
     */
    @Override
    public BaseResponse<ReturnOrderNotVoidByTidResponse> listNotVoidByTid(@RequestBody @Valid
                                                                                  ReturnOrderNotVoidByTidRequest
                                                                                  request) {
        List<ReturnOrder> orders = returnOrderService.findReturnsNotVoid(request.getTid());
        return BaseResponse.success(ReturnOrderNotVoidByTidResponse.builder()
                .returnOrderList(KsBeanUtil.convert(orders, ReturnOrderVO.class)).build());
    }

    /**
     * 根据订单id查询所有退单
     *
     * @param request 根据订单id查询请求结构 {@link ReturnOrderListByTidRequest}
     * @return 退单列表 {@link ReturnOrderListByTidResponse}
     */
    @Override
    public BaseResponse<ReturnOrderListByTidResponse> listByTid(@RequestBody @Valid ReturnOrderListByTidRequest
                                                                        request) {
        List<ReturnOrder> orderList = returnOrderService.findReturnByTid(request.getTid());
        List<ReturnOrderVO> orderVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderVOList = KsBeanUtil.convert(orderList, ReturnOrderVO.class);
        }
        return BaseResponse.success(ReturnOrderListByTidResponse.builder().returnOrderList(orderVOList).build());
    }

    /**
     * 根据结束时间统计退单
     *
     * @param request 根据结束时间统计退单请求结构 {@link ReturnOrderCountByEndDateRequest}
     * @return 退单数 {@link ReturnOrderCountByEndDateResponse}
     */
    @Override
    public BaseResponse<ReturnOrderCountByEndDateResponse> countByEndDate(@RequestBody @Valid
                                                                                  ReturnOrderCountByEndDateRequest
                                                                                  request) {
        return BaseResponse.success(ReturnOrderCountByEndDateResponse.builder()
                .count(returnOrderService.countReturnOrderByEndDate(request.getEndDate(), request.getReturnFlowState()))
                .build());
    }

    /**
     * 根据结束时间查询退单列表
     *
     * @param request 根据结束时间查询退单列表请求结构 {@link ReturnOrderListByEndDateRequest}
     * @return 退单列表 {@link ReturnOrderListByEndDateResponse}
     */
    @Override
    public BaseResponse<ReturnOrderListByEndDateResponse> listByEndDate(@RequestBody @Valid
                                                                                ReturnOrderListByEndDateRequest
                                                                                request) {
        List<ReturnOrder> orderList = returnOrderService.queryReturnOrderByEndDate(request.getEndDate(),
                request.getStart(), request.getEnd(), request.getReturnFlowState());
        List<ReturnOrderVO> orderVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderVOList = KsBeanUtil.convert(orderList, ReturnOrderVO.class);
        }
        return BaseResponse.success(ReturnOrderListByEndDateResponse.builder().returnOrderList(orderVOList).build());
    }

    /**
     * 根据状态统计退单
     *
     * @param request 根据状态统计退单请求结构 {@link ReturnOrderCountByFlowStateRequest}
     * @return 退单数 {@link ReturnOrderCountByFlowStateResponse}
     */
//    @Override
//    public BaseResponse<ReturnOrderCountByFlowStateResponse> countByFlowState(@RequestBody @Valid
//                                                                                      ReturnOrderCountByFlowStateRequest
//                                                                                      request) {
//        ReturnOrderTodoReponse todoReponse = returnOrderService.countReturnOrderByFlowState(
//                KsBeanUtil.convert(request, ReturnQueryRequest.class));
//        return BaseResponse.success(KsBeanUtil.convert(todoReponse, ReturnOrderCountByFlowStateResponse.class));
//    }
}
