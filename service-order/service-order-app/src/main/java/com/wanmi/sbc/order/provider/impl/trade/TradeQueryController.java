package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.OrderType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.NotSupportDeliveryException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewListResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.enums.OrderTagEnum;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.LatestDeliverDateRequest;
import com.wanmi.sbc.order.api.request.trade.LogisticsRepeatRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeVerifyAfterProcessingRequest;
import com.wanmi.sbc.order.api.request.trade.ShippingCalendarRequest;
import com.wanmi.sbc.order.api.request.trade.TradeAccountRecordRequest;
import com.wanmi.sbc.order.api.request.trade.TradeByPayOrderIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCountCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDeliveryCheckRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdListRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetPayOrderByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetRemedyByTidRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListAllRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListByParentIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListExportRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageForSettlementRequest;
import com.wanmi.sbc.order.api.request.trade.TradeParamsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeQueryFirstCompleteRequest;
import com.wanmi.sbc.order.api.request.trade.TradeQueryPurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradeSendEmailToFinanceRequest;
import com.wanmi.sbc.order.api.request.trade.TradeVerifyAfterProcessingRequest;
import com.wanmi.sbc.order.api.request.trade.TradeWrapperBackendCommitRequest;
import com.wanmi.sbc.order.api.response.trade.LatestDeliverDateResponse;
import com.wanmi.sbc.order.api.response.trade.LogisticNoRepeatResponse;
import com.wanmi.sbc.order.api.response.trade.ShippingCalendarResponse;
import com.wanmi.sbc.order.api.response.trade.TradeAccountRecordResponse;
import com.wanmi.sbc.order.api.response.trade.TradeByPayOrderIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeCountCriteriaResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdsResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetFreightResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetPayOrderByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetRemedyByTidResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListAllResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListByParentIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListExportResponse;
import com.wanmi.sbc.order.api.response.trade.TradePageCriteriaResponse;
import com.wanmi.sbc.order.api.response.trade.TradePageForSettlementResponse;
import com.wanmi.sbc.order.api.response.trade.TradeQueryFirstCompleteResponse;
import com.wanmi.sbc.order.api.response.trade.TradeQueryPurchaseInfoResponse;
import com.wanmi.sbc.order.api.response.trade.TradeVerifyAfterProcessingResponse;
import com.wanmi.sbc.order.api.response.trade.TradeWrapperBackendCommitResponse;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import com.wanmi.sbc.order.bean.vo.PointsTradeVO;
import com.wanmi.sbc.order.bean.vo.ProviderTradeVO;
import com.wanmi.sbc.order.bean.vo.ShippingCalendarVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeRemedyDetailsVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.entity.DeliverCalendar;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.mapper.TradeMapper;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.model.root.TradeConfirmItem;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.reponse.TradeFreightResponse;
import com.wanmi.sbc.order.trade.reponse.TradeRemedyDetails;
import com.wanmi.sbc.order.trade.request.ProviderTradeQueryRequest;
import com.wanmi.sbc.order.trade.request.TradeCreateRequest;
import com.wanmi.sbc.order.trade.request.TradeDeliverRequest;
import com.wanmi.sbc.order.trade.request.TradeParams;
import com.wanmi.sbc.order.trade.request.TradeQueryRequest;
import com.wanmi.sbc.order.trade.service.CycleBuyDeliverTimeService;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeOptimizeService;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 10:04
 */
@Slf4j
@Validated
@RestController
public class TradeQueryController implements TradeQueryProvider {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private ProviderTradeService providerTradeService;
    @Autowired
    private LinkedMallTradeService thirdPlatformTradeService;

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private TradeOptimizeService tradeOptimizeService;

    @Autowired
    private CycleBuyDeliverTimeService cycleBuyDeliverTimeService;

    /**
     * @param tradePageRequest 分页参数 {@link TradePageRequest}
     * @return
     */
//    @Override
//    public BaseResponse<TradePageResponse> page(@RequestBody @Valid TradePageRequest tradePageRequest) {
//        Page<Trade> page = tradeService.page(tradePageRequest.getQueryBuilder(), tradePageRequest.getRequest());
//        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);
//        return BaseResponse.success(TradePageResponse.builder().tradePage(tradeVOS).build());
//    }

    /**
     * @param tradePageCriteriaRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<TradePageCriteriaResponse> pageCriteria(@RequestBody @Valid TradePageCriteriaRequest tradePageCriteriaRequest) {
        TradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradePageCriteriaRequest.getTradePageDTO(), TradeQueryRequest.class);
        if(Objects.nonNull(tradeQueryRequest.getQueryOrderType())){
            switch (tradeQueryRequest.getQueryOrderType()){
                case GROUPON:tradeQueryRequest.setGrouponFlag(Boolean.TRUE);break;
                case FLASH_SALE:tradeQueryRequest.setFlashSaleFlag(Boolean.TRUE);break;
                case BOOKING_SALE:tradeQueryRequest.setBookingSaleFlag(Boolean.TRUE);break;
            }
        }

        //查看渠道待处理就显示待发货和部分发货
        if (Boolean.TRUE.equals(tradeQueryRequest.getThirdPlatformToDo())) {
            tradeQueryRequest.setFlowStates(Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART));
            tradeQueryRequest.setReturnHasFlag(false);//没有退单
        }

        Criteria criteria;
        if (tradePageCriteriaRequest.isReturn()) {
            criteria = tradeQueryRequest.getCanReturnCriteria();
        } else {
            criteria = tradeQueryRequest.getWhereCriteria();
        }
        Page<Trade> page = tradeService.page(criteria, tradeQueryRequest);
        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);
        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
    }

    /**
     * @param tradePageCriteriaRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<TradePageCriteriaResponse> supplierPageCriteria(@RequestBody @Valid TradePageCriteriaRequest tradePageCriteriaRequest) {
        //根据providerTradeId模糊查询ProviderTrade,获取tradeId
        if(StringUtils.isNotBlank(tradePageCriteriaRequest.getTradePageDTO().getProviderTradeId())
                || StringUtils.isNotBlank(tradePageCriteriaRequest.getTradePageDTO().getProviderName())){
            List<ProviderTrade> providerTrades = providerTradeService.queryAll(ProviderTradeQueryRequest.builder()
                    .providerName(tradePageCriteriaRequest.getTradePageDTO().getProviderName())
                    .id(tradePageCriteriaRequest.getTradePageDTO().getProviderTradeId()).build());
            if(CollectionUtils.isNotEmpty(providerTrades)){
                List<String> tradeIds = providerTrades.stream().map(ProviderTrade::getParentId).collect(Collectors.toList());
                String[] ids = tradeIds.toArray(new String[tradeIds.size()]);
                tradePageCriteriaRequest.getTradePageDTO().setIds(ids);
            }else {
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(new MicroServicePage<TradeVO>()).build());
            }
        }
        tradePageCriteriaRequest.getTradePageDTO().setProviderTradeId(null);
        tradePageCriteriaRequest.getTradePageDTO().setProviderName(null);
        TradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradePageCriteriaRequest.getTradePageDTO(), TradeQueryRequest.class);
        Criteria criteria;
        TradeQueryDTO dto = tradePageCriteriaRequest.getTradePageDTO();
        if (Objects.nonNull(dto.getQueryOrderType())){
            switch (dto.getQueryOrderType()){
                case GROUPON:
                    tradeQueryRequest.setGrouponFlag(true);
                    break;
                case FLASH_SALE:
                    tradeQueryRequest.setFlashSaleFlag(true);
                    break;
                case BOOKING_SALE:
                    tradeQueryRequest.setBookingSaleFlag(true);
                    break;
                case VIRTUAL_COUPON_ORDER:
                    tradeQueryRequest.setVirtualCouponId(Long.valueOf(DefaultFlag.NO.toValue()));
                    break;
                case VIRTUAL_GOODS_ORDER:
                    tradeQueryRequest.setGoodsType(GoodsType.VIRTUAL_GOODS);
                    break;
                case BUY_POINTS_ORDER:
                    tradeQueryRequest.setOrderType(OrderType.NORMAL_ORDER);
                    break;
                case CYCLE_BUY_ORDER:  //周期购订单查询
                    tradeQueryRequest.setCycleBuyFlag(true);
                    break;
            }
        }

        //查看渠道待处理就显示待发货和部分发货
        if (Boolean.TRUE.equals(tradeQueryRequest.getThirdPlatformToDo())) {
            tradeQueryRequest.setFlowStates(Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART));
            tradeQueryRequest.setReturnHasFlag(false);//没有退单
        }

        if (tradePageCriteriaRequest.isReturn()) {
            criteria = tradeQueryRequest.getCanReturnCriteria();
        } else {
            criteria = tradeQueryRequest.getWhereCriteria();
        }
        Page<Trade> page = tradeService.page(criteria, tradeQueryRequest);
        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);

        if (CollectionUtils.isNotEmpty(tradeVOS.getContent())) {
            tradeVOS.getContent().forEach(item -> item.setGiftFlag(CollectionUtils.isNotEmpty(item.getTags()) ? item.getTags().contains(OrderTagEnum.GIFT.getCode()) : false));
        }

        List<TradeVO> tradeVOList=tradeVOS.getContent();
        // 未支付不显示卡券信息
        tradeVOList.stream()
                .filter(t-> PayState.NOT_PAID == t.getTradeState().getPayState())
                .forEach(tradeVO->{
                    tradeVO.getTradeItems().stream()
                            .forEach(t-> t.setVirtualCoupons(Collections.EMPTY_LIST) );
                    tradeVO.getGifts().stream()
                            .forEach(t-> t.setVirtualCoupons(Collections.EMPTY_LIST) );
                }  );
        if(CollectionUtils.isNotEmpty(tradeVOS.getContent())){
            List<String> providerTradeIds = tradeVOS.getContent().stream().map(TradeVO::getId).collect(Collectors.toList());
            //查询所有的子订单(providerTrade表)
            List<ProviderTrade> providerTradeList = providerTradeService.queryAll(ProviderTradeQueryRequest.builder().parentIds(providerTradeIds).build());
            if(CollectionUtils.isNotEmpty(providerTradeList)){
                List<TradeVO> result=new ArrayList<>();
                //查询主订单编号列表
                tradeVOList.forEach(vo->{
                    List<TradeVO> items=new ArrayList<>();
                    for (ProviderTrade item: providerTradeList){
                        if (vo.getId().equals(item.getParentId())){
                            TradeVO tradeVO = KsBeanUtil.convert(item, TradeVO.class);
                            items.add(tradeVO);
                        }
                    }

                    vo.setTradeVOList(items);
                });

                for (TradeVO tradeVO : tradeVOList) {
                    Boolean isContainsTrade = false;
                    if(CollectionUtils.isNotEmpty(tradeVO.getTradeVOList())){
                        List<Long> storeList = tradeVO.getTradeVOList().stream().map(TradeVO::getStoreId).collect(Collectors.toList());
                        isContainsTrade = storeList.contains(null);
                    }
                    tradeVO.setIsContainsTrade(isContainsTrade);
                }
                tradeVOS.setContent(tradeVOList);
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
            }else {
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
            }
        }

        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
    }

    /**
     * @param tradePageCriteriaRequest Boss端带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<TradePageCriteriaResponse> pageBossCriteria(@RequestBody @Valid TradePageCriteriaRequest tradePageCriteriaRequest){
        //根据providerTradeId模糊查询ProviderTrade,获取tradeId
        if(StringUtils.isNotBlank(tradePageCriteriaRequest.getTradePageDTO().getProviderTradeId())
                || StringUtils.isNotBlank(tradePageCriteriaRequest.getTradePageDTO().getProviderName())
                || StringUtils.isNotEmpty(tradePageCriteriaRequest.getTradePageDTO().getProviderCode())){
            List<ProviderTrade> providerTrades = providerTradeService.queryAll(ProviderTradeQueryRequest.builder()
                    .providerName(tradePageCriteriaRequest.getTradePageDTO().getProviderName())
                    .providerCode(tradePageCriteriaRequest.getTradePageDTO().getProviderCode())
                    .id(tradePageCriteriaRequest.getTradePageDTO().getProviderTradeId()).build());
            if(CollectionUtils.isNotEmpty(providerTrades)){
                List<String> tradeIds = providerTrades.stream().map(ProviderTrade::getParentId).collect(Collectors.toList());
                String[] ids = tradeIds.toArray(new String[tradeIds.size()]);
                tradePageCriteriaRequest.getTradePageDTO().setIds(ids);
                tradePageCriteriaRequest.getTradePageDTO().setId(null);
            }else {
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(new MicroServicePage<TradeVO>()).build());
            }
        }
        tradePageCriteriaRequest.getTradePageDTO().setProviderTradeId(null);
        tradePageCriteriaRequest.getTradePageDTO().setProviderName(null);
        tradePageCriteriaRequest.getTradePageDTO().setProviderCode(null);
        TradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradePageCriteriaRequest.getTradePageDTO(), TradeQueryRequest.class);
        Criteria criteria;

        TradeQueryDTO dto = tradePageCriteriaRequest.getTradePageDTO();
        if (Objects.nonNull(dto.getQueryOrderType())){
            switch (dto.getQueryOrderType()){
                case GROUPON:
                    tradeQueryRequest.setGrouponFlag(true);
                    break;
                case FLASH_SALE:
                    tradeQueryRequest.setFlashSaleFlag(true);
                    break;
                case BOOKING_SALE:
                    tradeQueryRequest.setBookingSaleFlag(true);
                    break;
                case BUY_POINTS_ORDER:
                    tradeQueryRequest.setOrderType(OrderType.NORMAL_ORDER);
                    break;
                case VIRTUAL_COUPON_ORDER:
                    tradeQueryRequest.setVirtualCouponId(Long.valueOf(DefaultFlag.NO.toValue()));
                    break;
                case VIRTUAL_GOODS_ORDER:
                    tradeQueryRequest.setGoodsType(GoodsType.VIRTUAL_GOODS);
                    break;
                case CYCLE_BUY_ORDER:
                    tradeQueryRequest.setCycleBuyFlag(true);
                    break;
            }
        }

        //查看渠道待处理就显示待发货和部分发货
        if (Boolean.TRUE.equals(tradeQueryRequest.getThirdPlatformToDo())) {
            tradeQueryRequest.setFlowStates(Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART));
            tradeQueryRequest.setReturnHasFlag(false);//没有退单
        }

        if (tradePageCriteriaRequest.isReturn()) {
            criteria = tradeQueryRequest.getCanReturnCriteria();
        } else {
            criteria = tradeQueryRequest.getWhereCriteria();
        }
        Page<Trade> page = tradeService.page(criteria, tradeQueryRequest);
        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);
        List<TradeVO> tradeVOList=tradeVOS.getContent();

        if(CollectionUtils.isNotEmpty(tradeVOS.getContent())){
            List<String> providerTradeIds = tradeVOS.getContent().stream().map(TradeVO::getId).collect(Collectors.toList());
            //查询所有的子订单(providerTrade表)
            List<ProviderTrade> providerTradeList = providerTradeService.queryAll(ProviderTradeQueryRequest.builder().parentIds(providerTradeIds).build());
            if(CollectionUtils.isNotEmpty(providerTradeList)){
                List<TradeVO> result=new ArrayList<>();
                //查询主订单编号列表
                tradeVOList.forEach(vo->{
                    List<TradeVO> items=new ArrayList<>();
                    for (ProviderTrade item: providerTradeList){
                        if (vo.getId().equals(item.getParentId())){
                            TradeVO tradeVO = KsBeanUtil.convert(item, TradeVO.class);
                            items.add(tradeVO);
                        }
                    }

                    vo.setTradeVOList(items);
                });

                for (TradeVO tradeVO : tradeVOList) {
                    Boolean isContainsTrade = false;
                    if(CollectionUtils.isNotEmpty(tradeVO.getTradeVOList())){
                        List<Long> storeList = tradeVO.getTradeVOList().stream().map(TradeVO::getStoreId).collect(Collectors.toList());
                        isContainsTrade = storeList.contains(null);
                    }
                    tradeVO.setIsContainsTrade(isContainsTrade);
                }
                tradeVOS.setContent(tradeVOList);
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
            }else {
                return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
            }
        }
        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
    }

    /**
     * @param tradeCountCriteriaRequest 带参分页参数 {@link TradeCountCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<TradeCountCriteriaResponse> countCriteria(@RequestBody @Valid TradeCountCriteriaRequest tradeCountCriteriaRequest) {
        TradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradeCountCriteriaRequest.getTradePageDTO(), TradeQueryRequest.class);
        long count = tradeService.countNum(tradeQueryRequest.getWhereCriteria(), tradeQueryRequest);
        return BaseResponse.success(TradeCountCriteriaResponse.builder().count(count).build());
    }

    /**
     * 调用校验与封装单个订单信息
     *
     * @param tradeWrapperBackendCommitRequest 包装信息参数 {@link TradeWrapperBackendCommitRequest}
     * @return 订单信息 {@link TradeWrapperBackendCommitResponse}
     */
    @Override
    public BaseResponse<TradeWrapperBackendCommitResponse> wrapperBackendCommit(@RequestBody @Valid TradeWrapperBackendCommitRequest tradeWrapperBackendCommitRequest) {
        Trade trade = tradeService.wrapperBackendCommitTrade(tradeWrapperBackendCommitRequest.getOperator(),
                KsBeanUtil.convert(tradeWrapperBackendCommitRequest.getCompanyInfo(), CompanyInfoVO.class),
                KsBeanUtil.convert(tradeWrapperBackendCommitRequest.getStoreInfo(), StoreInfoResponse.class),
                KsBeanUtil.convert(tradeWrapperBackendCommitRequest.getTradeCreate(), TradeCreateRequest.class));
        return BaseResponse.success(TradeWrapperBackendCommitResponse.builder()
                .tradeVO(KsBeanUtil.convert(trade, TradeVO.class)).build());
    }

    /**
     * 查询店铺订单应付的运费
     *
     * @param tradeParamsRequest 包装信息参数 {@link TradeParamsRequest}
     * @return 店铺运费信息 {@link TradeGetFreightResponse}
     */
    @Override
    public BaseResponse<TradeGetFreightResponse> getFreight(@RequestBody @Valid TradeParamsRequest tradeParamsRequest) {
        try {
            TradeFreightResponse freight = tradeService.getFreight(KsBeanUtil.convert(tradeParamsRequest, TradeParams.class));
            return BaseResponse.success(KsBeanUtil.convert(freight, TradeGetFreightResponse.class));
        }catch (NotSupportDeliveryException e) {
            return BaseResponse.success(null);
        }
    }

    /**
     * 获取订单商品详情,不包含区间价，会员级别价信息
     *
     * @param tradeGetGoodsRequest 商品skuid列表 {@link TradeGetGoodsRequest}
     * @return 商品信息列表 {@link GoodsInfoViewListResponse}
     */
    @Override
    public BaseResponse<TradeGetGoodsResponse> getGoods(@RequestBody @Valid TradeGetGoodsRequest tradeGetGoodsRequest) {
        return BaseResponse.success(tradeService.getGoodsResponse(tradeGetGoodsRequest.getSkuIds()));
    }

    /**
     * 发货校验,检查请求发货商品数量是否符合应发货数量
     *
     * @param tradeDeliveryCheckRequest 订单号 物流信息 {@link TradeDeliveryCheckRequest}
     * @return 处理结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse deliveryCheck(@RequestBody @Valid TradeDeliveryCheckRequest tradeDeliveryCheckRequest) {
        tradeService.deliveryCheck(tradeDeliveryCheckRequest.getTid(),
                KsBeanUtil.convert(tradeDeliveryCheckRequest.getTradeDeliver(), TradeDeliverRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 通过id获取交易单信息,并将buyer.account加密
     *
     * @param tradeGetByIdRequest 交易单id {@link TradeGetByIdRequest}
     * @return 交易单信息 {@link TradeGetByIdResponse}
     */
    @Override
    public BaseResponse<TradeGetByIdResponse> getById(@RequestBody @Valid TradeGetByIdRequest tradeGetByIdRequest) {
        Trade trade = tradeService.detail(tradeGetByIdRequest.getTid());
        if(Objects.isNull(trade)){
            return BaseResponse.success(TradeGetByIdResponse.builder().tradeVO(new TradeVO()).build());
        }
        TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);
        List<ProviderTrade> providerTrades =
                providerTradeService.findListByParentId(trade.getId());
        List<TradeVO> providerTradeVOS = KsBeanUtil.convert(providerTrades, TradeVO.class);
        tradeVO.setTradeVOList(providerTradeVOS);
        //周期购订单 重新设置商品数量 购买的数量*期数
        if(tradeVO.getCycleBuyFlag()) {
            tradeVO.getTradeItems().forEach(tradeItemVO -> {
                if(Objects.nonNull(tradeItemVO.getCycleNum())) {
                    tradeItemVO.setNum(tradeItemVO.getCycleNum()*tradeItemVO.getNum());
                }
            });

            if(CollectionUtils.isNotEmpty(providerTrades)) {
                providerTradeVOS.forEach(providerTradeVO -> {
                    providerTradeVO.getTradeItems().forEach(tradeItem -> {
                        if(Objects.nonNull(tradeItem.getCycleNum())) {
                            tradeItem.setNum(tradeItem.getCycleNum()*tradeItem.getNum());
                        }
                    });
                });
            }
        }
        // 查询linkedmall订单详情
        if (Boolean.TRUE.equals(tradeGetByIdRequest.getNeedLmOrder()) && CollectionUtils.isNotEmpty(providerTrades) &&
                providerTradeVOS.stream().anyMatch(t -> ThirdPlatformType.LINKED_MALL.equals(t.getThirdPlatformType()))) {
            // 填充并保存linkedmall订单配送信息
            tradeVO = thirdPlatformTradeService.fillLinkedMallTradeDelivers(tradeVO);
        }
        if (Objects.nonNull(tradeVO) && Objects.nonNull(tradeVO.getBuyer()) && StringUtils.isNotEmpty(tradeVO.getBuyer()
                .getAccount())) {
            tradeVO.getBuyer().setAccount(ReturnOrderService.getDexAccount(trade.getBuyer().getAccount()));
        }
        return BaseResponse.success(TradeGetByIdResponse.builder().tradeVO(tradeVO).build());
    }

    /**
     * 通过id批量获取交易单信息,并将buyer.account加密
     *
     * @param tradeGetByIdsRequest 交易单id {@link TradeGetByIdRequest}
     * @return 交易单信息 {@link TradeGetByIdResponse}
     */
    @Override
    public BaseResponse<TradeGetByIdsResponse> getByIds(@Valid TradeGetByIdsRequest tradeGetByIdsRequest) {

        List<Trade> trades = tradeService.details(tradeGetByIdsRequest.getTid());
        if(CollectionUtils.isEmpty(trades)){
            return BaseResponse.success(TradeGetByIdsResponse.builder().tradeVO(Arrays.asList()).build());
        }
        List<ProviderTrade> providerTrades = providerTradeService.queryAll(
                ProviderTradeQueryRequest.builder().parentIds(trades.stream().map(Trade::getId).collect(Collectors.toList())).build());

        List<TradeVO> items = new ArrayList<>();
        trades.forEach(trade -> {
            TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);

            if (CollectionUtils.isNotEmpty(providerTrades)) {
                //将子订单填充到相应的父订单中
                List<PointsTradeVO> item = new ArrayList<>();
                for (ProviderTrade providerTrade : providerTrades) {
                    if (trade.getId().equals(providerTrade.getParentId())) {
                        PointsTradeVO pointsTradeVO = KsBeanUtil.convert(providerTrade, PointsTradeVO.class);
                        item.add(pointsTradeVO);
                    }
                }
                List<TradeVO> providerTradeVOS = KsBeanUtil.convert(item, TradeVO.class);
                tradeVO.setTradeVOList(providerTradeVOS);

                if (Objects.nonNull(tradeVO) && Objects.nonNull(tradeVO.getBuyer()) && StringUtils.isNotEmpty(tradeVO.getBuyer()
                        .getAccount())) {
                    tradeVO.getBuyer().setAccount(ReturnOrderService.getDexAccount(trade.getBuyer().getAccount()));
                }
            }
            items.add(tradeVO);
        });
        return BaseResponse.success(TradeGetByIdsResponse.builder().tradeVO(items).build());
    }

    @Override
    public BaseResponse<TradeListAllResponse> getTradeListByIds(@RequestBody @Valid TradeGetByIdListRequest tradeGetByIdListRequest) {
        List<Trade> trades = tradeService.details(tradeGetByIdListRequest.getTidList());
        List<TradeVO> tradeVOList = KsBeanUtil.convert(trades, TradeVO.class);
        if(BoolFlag.YES == tradeGetByIdListRequest.getBoolFlag()){
            List<ProviderTrade> providerTrades = providerTradeService.queryAll(
                    ProviderTradeQueryRequest.builder().parentIds(tradeVOList.stream().map(TradeVO::getId).collect(Collectors.toList())).build());
            //主单组装子单
            tradeVOList.forEach(tradeVO -> {
                if (CollectionUtils.isNotEmpty(providerTrades)) {
                    List<ProviderTradeVO> providerTradeVOList = new ArrayList<>();
                    for (ProviderTrade providerTrade : providerTrades) {
                        if (tradeVO.getId().equals(providerTrade.getParentId())) {
                            ProviderTradeVO providerTradeVO = KsBeanUtil.convert(providerTrade, ProviderTradeVO.class);
                            providerTradeVOList.add(providerTradeVO);
                        }
                    }
                    tradeVO.setProviderTradeVOList(providerTradeVOList);
                }
            });
        }
        return BaseResponse.success(TradeListAllResponse.builder().tradeVOList(tradeVOList).build());
    }

    /**
     * 通过id获取交易单信息
     *
     * @param tradeGetByIdRequest 交易单id {@link TradeGetByIdRequest}
     * @return 交易单信息 {@link TradeGetByIdResponse}
     */
    @Override
    public BaseResponse<TradeGetByIdResponse> getOrderById(@RequestBody @Valid TradeGetByIdRequest tradeGetByIdRequest) {
        Trade trade = tradeService.detail(tradeGetByIdRequest.getTid());
        BaseResponse<TradeGetByIdResponse> baseResponse = BaseResponse.success(TradeGetByIdResponse.builder().
                tradeVO(KsBeanUtil.convert(trade, TradeVO.class)).build());
        return baseResponse;
    }

    @Override
    public BaseResponse<TradeListByParentIdResponse> getListByParentId(@RequestBody @Valid TradeListByParentIdRequest tradeListByParentIdRequest) {
        List<Trade> tradeList = tradeService.detailsByParentId(tradeListByParentIdRequest.getParentTid());
        if (tradeList.isEmpty()) {
            return BaseResponse.success(TradeListByParentIdResponse.builder().tradeVOList(Collections.emptyList()).build());
        }
        //父订单号对应的子订单的买家信息应该是相同的
        Trade trade = tradeList.get(0);
        //买家银行账号加密
        if (Objects.nonNull(trade) && Objects.nonNull(trade.getBuyer()) && StringUtils.isNotEmpty(trade.getBuyer()
                .getAccount())) {
            trade.getBuyer().setAccount(ReturnOrderService.getDexAccount(trade.getBuyer().getAccount()));
        }
        final Buyer buyer = trade.getBuyer();
        //统一设置账号加密后的买家信息
        List<TradeVO> tradeVOList = tradeList.stream().map(i -> {
            i.setBuyer(buyer);
            return KsBeanUtil.convert(i, TradeVO.class);
        }).collect(Collectors.toList());
        return BaseResponse.success(TradeListByParentIdResponse.builder().
                tradeVOList(tradeVOList).build());
    }

    @Override
    public BaseResponse<TradeListByParentIdResponse> getOrderListByParentId(@RequestBody @Valid TradeListByParentIdRequest tradeListByParentIdRequest) {
        List<Trade> tradeList = tradeService.detailsByParentId(tradeListByParentIdRequest.getParentTid());
        if (tradeList.isEmpty()) {
            return BaseResponse.success(TradeListByParentIdResponse.builder().tradeVOList(Collections.emptyList()).build());
        }
        //统一设置账号加密后的买家信息
        List<TradeVO> tradeVOList = tradeList.stream().map(i -> {
            return KsBeanUtil.convert(i, TradeVO.class);
        }).collect(Collectors.toList());
        return BaseResponse.success(TradeListByParentIdResponse.builder().
                tradeVOList(tradeVOList).build());
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param tradeVerifyAfterProcessingRequest 交易单id {@link TradeVerifyAfterProcessingRequest}
     * @return 验证结果 {@link TradeVerifyAfterProcessingResponse}
     */
    @Override
    public BaseResponse<TradeVerifyAfterProcessingResponse> verifyAfterProcessing(@RequestBody @Valid TradeVerifyAfterProcessingRequest tradeVerifyAfterProcessingRequest) {
        return BaseResponse.success(TradeVerifyAfterProcessingResponse.builder().
                verifyResult(tradeService.verifyAfterProcessing(tradeVerifyAfterProcessingRequest.getTid())).build());
    }


    /**
     * 验证订单是否存在售后申请
     *
     * @param providerTradeVerifyAfterProcessingRequest 交易单id {@link TradeVerifyAfterProcessingRequest}
     * @return 验证结果 {@link TradeVerifyAfterProcessingResponse}
     */
    @Override
    public BaseResponse<TradeVerifyAfterProcessingResponse> providerVerifyAfterProcessing(@RequestBody @Valid ProviderTradeVerifyAfterProcessingRequest providerTradeVerifyAfterProcessingRequest) {
        return BaseResponse.success(TradeVerifyAfterProcessingResponse.builder().
                verifyResult(tradeService.providerVerifyAfterProcessing(providerTradeVerifyAfterProcessingRequest.getPtid())).build());
    }

    /**
     * 条件查询所有订单
     *
     * @param tradeListAllRequest 查询条件 {@link TradeListAllRequest}
     * @return 验证结果 {@link TradeListAllResponse}
     */
    @Override
    public BaseResponse<TradeListAllResponse> listAll(@RequestBody @Valid TradeListAllRequest tradeListAllRequest) {
        List<Trade> trades = tradeService.queryAll(KsBeanUtil.convert(tradeListAllRequest.getTradeQueryDTO(),
                TradeQueryRequest.class));
        return BaseResponse.success(TradeListAllResponse.builder().tradeVOList(KsBeanUtil.convert(trades,
                TradeVO.class)).build());
    }

    /**
     * 获取支付单
     *
     * @param tradeGetPayOrderByIdRequest 支付单号 {@link TradeGetPayOrderByIdRequest}
     * @return 支付单 {@link TradeGetPayOrderByIdResponse}
     */
    @Override
    public BaseResponse<TradeGetPayOrderByIdResponse> getPayOrderById(@RequestBody @Valid TradeGetPayOrderByIdRequest tradeGetPayOrderByIdRequest) {
        PayOrder payOrder = tradeService.findPayOrder(tradeGetPayOrderByIdRequest.getPayOrderId());
        return BaseResponse.success(TradeGetPayOrderByIdResponse.builder()
                .payOrder(KsBeanUtil.convert(payOrder, PayOrderVO.class)).build());
    }

    /**
     * 查询订单信息作为结算原始数据
     *
     * @param tradePageForSettlementRequest 查询分页参数 {@link TradePageForSettlementRequest}
     * @return 支付单集合 {@link TradePageForSettlementResponse}
     */
    @Override
    public BaseResponse<TradePageForSettlementResponse> pageForSettlement(@RequestBody @Valid TradePageForSettlementRequest tradePageForSettlementRequest) {
        List<Trade> tradeList = tradeService.findTradeListForSettlement(tradePageForSettlementRequest.getStoreId(),
                tradePageForSettlementRequest.getStartTime()
                , tradePageForSettlementRequest.getEndTime(), tradePageForSettlementRequest.getPageRequest());
        return BaseResponse.success(TradePageForSettlementResponse.builder().tradeVOList(KsBeanUtil.convert(tradeList
                , TradeVO.class)).build());
    }

    /**
     * 根据快照封装订单确认页信息
     *
     * @param tradeQueryPurchaseInfoRequest 交易单快照信息 {@link TradeQueryPurchaseInfoRequest}
     * @return 交易单确认项 {@link TradeQueryPurchaseInfoResponse}
     */
    @Override
    public BaseResponse<TradeQueryPurchaseInfoResponse> queryPurchaseInfo(@RequestBody @Valid TradeQueryPurchaseInfoRequest tradeQueryPurchaseInfoRequest) {
        TradeConfirmItem tradeConfirmItem = tradeService.getPurchaseInfo(
                KsBeanUtil.convert(tradeQueryPurchaseInfoRequest.getTradeItemGroupDTO(), TradeItemGroup.class),
                KsBeanUtil.convert(tradeQueryPurchaseInfoRequest.getTradeItemList(), TradeItem.class),
                KsBeanUtil.convert(tradeQueryPurchaseInfoRequest.getMarkupItemList(), TradeItem.class)
                );
        return BaseResponse.success(TradeQueryPurchaseInfoResponse.builder()
                .tradeConfirmItemVO(KsBeanUtil.convert(tradeConfirmItem, TradeConfirmItemVO.class)).build());
    }

    /**
     * 用于编辑订单前的展示信息，包含了原订单信息和最新关联的订单商品价格（计算了会员价和级别价后的商品单价）
     *
     * @param tradeGetRemedyByTidRequest 交易单id {@link TradeGetRemedyByTidRequest}
     * @return 废弃单 {@link TradeGetRemedyByTidResponse}
     */
    @Override
    public BaseResponse<TradeGetRemedyByTidResponse> getRemedyByTid(@RequestBody @Valid TradeGetRemedyByTidRequest tradeGetRemedyByTidRequest) {
        TradeRemedyDetails tradeRemedyDetails = tradeService.getTradeRemedyDetails(tradeGetRemedyByTidRequest.getTid());
        return BaseResponse.success(TradeGetRemedyByTidResponse.builder()
                .tradeRemedyDetailsVO(KsBeanUtil.convert(tradeRemedyDetails, TradeRemedyDetailsVO.class)).build());
    }

    /**
     * 查询客户首笔完成的交易号
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<TradeQueryFirstCompleteResponse> queryFirstCompleteTrade(@RequestBody @Valid TradeQueryFirstCompleteRequest request) {
        String tradeId = tradeService.queryFirstCompleteTrade(request.getCustomerId());
        return BaseResponse.success(TradeQueryFirstCompleteResponse.builder().tradeId(tradeId).build());
    }

    /**
     * 订单选择银联企业支付通知财务
     *
     * @param tradeSendEmailToFinanceRequest 邮箱信息 {@link TradeSendEmailToFinanceRequest}
     * @return 发送结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse sendEmailToFinance(@RequestBody @Valid TradeSendEmailToFinanceRequest tradeSendEmailToFinanceRequest) {
        tradeService.sendEmailToFinance(tradeSendEmailToFinanceRequest.getCustomerId(),
                tradeSendEmailToFinanceRequest.getOrderId(), tradeSendEmailToFinanceRequest.getUrl());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询导出数据
     *
     * @param tradeListExportRequest 查询条件 {@link TradeListExportRequest}
     * @return 验证结果 {@link TradeListExportResponse}
     */
    @Override
    public BaseResponse<TradeListExportResponse> listTradeExport(@RequestBody @Valid TradeListExportRequest tradeListExportRequest) {
        TradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(tradeListExportRequest.getTradeQueryDTO(),
                TradeQueryRequest.class);

        //查看渠道待处理就显示待发货和部分发货
        if (Boolean.TRUE.equals(tradeQueryRequest.getThirdPlatformToDo())) {
            tradeQueryRequest.setFlowStates(Arrays.asList(FlowState.AUDIT, FlowState.DELIVERED_PART));
            tradeQueryRequest.setReturnHasFlag(false);//没有退单
        }

        TradeQueryDTO dto = tradeListExportRequest.getTradeQueryDTO();
        if (Objects.nonNull(dto.getQueryOrderType())){
            switch (dto.getQueryOrderType()){
                case GROUPON:
                    tradeQueryRequest.setGrouponFlag(true);
                    break;
                case FLASH_SALE:
                    tradeQueryRequest.setFlashSaleFlag(true);
                    break;
                case BOOKING_SALE:
                    tradeQueryRequest.setBookingSaleFlag(true);
                    break;
                case BUY_POINTS_ORDER:
                    tradeQueryRequest.setOrderType(OrderType.NORMAL_ORDER);
                    break;
                case VIRTUAL_COUPON_ORDER:
                    tradeQueryRequest.setVirtualCouponId(Long.valueOf(DefaultFlag.NO.toValue()));
                    break;
                case VIRTUAL_GOODS_ORDER:
                    tradeQueryRequest.setGoodsType(GoodsType.VIRTUAL_GOODS);
                    break;
                case CYCLE_BUY_ORDER:
                    tradeQueryRequest.setCycleBuyFlag(true);
                    break;
            }
        }

        List<Trade> tradeList = tradeService.listTradeExport(tradeQueryRequest);
        return BaseResponse.success(TradeListExportResponse.builder().tradeVOList(KsBeanUtil.convert(tradeList,
                TradeVO.class)).build());
    }

    /**
     * 根据支付单查询
     * @param tradeByPayOrderIdRequest 父交易单id {@link TradeByPayOrderIdRequest}
     * @return
     */
    @Override
    public BaseResponse<TradeByPayOrderIdResponse> getOrderByPayOrderId(@RequestBody @Valid TradeByPayOrderIdRequest tradeByPayOrderIdRequest) {
        Trade trade = tradeService.queryAll(TradeQueryRequest.builder().payOrderId(tradeByPayOrderIdRequest.getPayOrderId()).build()).get(0);
        List<ProviderTrade> providerTrades = providerTradeService.queryAll(ProviderTradeQueryRequest.builder().parentId(trade.getId()).build());
        List<TradeVO> providerTradeVOS = KsBeanUtil.convert(providerTrades, TradeVO.class);
        if (Objects.nonNull(trade) && Objects.nonNull(trade.getBuyer()) && StringUtils.isNotEmpty(trade.getBuyer()
                .getAccount())) {
            trade.getBuyer().setAccount(ReturnOrderService.getDexAccount(trade.getBuyer().getAccount()));
        }
        BaseResponse<TradeByPayOrderIdResponse> baseResponse = BaseResponse.success(TradeByPayOrderIdResponse.builder().
                tradeVO(KsBeanUtil.convert(trade, TradeVO.class)).build());
        baseResponse.getContext().getTradeVO().setTradeVOList(providerTradeVOS);
        return baseResponse;
    }

    @Override
    public BaseResponse<TradePageCriteriaResponse> pageCriteriaOptimize(@RequestBody @Valid TradePageCriteriaRequest tradePageCriteriaRequest) {
        TradeQueryRequest tradeQueryRequest = tradeMapper.tradeDtoToTradeQueryRequest(tradePageCriteriaRequest.getTradePageDTO());
        if(Objects.nonNull(tradeQueryRequest.getQueryOrderType())){
            switch (tradeQueryRequest.getQueryOrderType()){
                case GROUPON:tradeQueryRequest.setGrouponFlag(Boolean.TRUE);break;
                case FLASH_SALE:tradeQueryRequest.setFlashSaleFlag(Boolean.TRUE);break;
                case BOOKING_SALE:tradeQueryRequest.setBookingSaleFlag(Boolean.TRUE);break;
            }
        }

        Criteria criteria;
        if (tradePageCriteriaRequest.isReturn()) {
            criteria = tradeQueryRequest.getCanReturnCriteria();
        } else {
            criteria = tradeQueryRequest.getWhereCriteria();
        }
        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeService.pageOptimize(criteria,tradeQueryRequest)).build());
    }

    @Override
    public BaseResponse<LogisticNoRepeatResponse> getRepeatLogisticNo(@Valid @RequestBody LogisticsRepeatRequest logisticsRepeatRequest) {
        List<String> repeatLogisticNoList = tradeOptimizeService.verifyLogisticNo(logisticsRepeatRequest.getLogisticsDTOList());
        return BaseResponse.success(LogisticNoRepeatResponse.builder().logisticNoList(repeatLogisticNoList).build());
    }

    @Override
    public BaseResponse<LatestDeliverDateResponse> getLatestDeliverDate(@Valid @RequestBody LatestDeliverDateRequest latestDeliverDateRequest) {
        LocalDate date = cycleBuyDeliverTimeService.getLatestDeliverTime(latestDeliverDateRequest.getDate(), latestDeliverDateRequest.getDeliveryCycle(), latestDeliverDateRequest.getRule());
        return BaseResponse.success(LatestDeliverDateResponse.builder().date(date).build());
    }


    /**
     * 周期购订单查询发货日历
     * @param shippingCalendarRequest
     * @return
     */
    @Override
    public BaseResponse<ShippingCalendarResponse> getShippingCalendar(@Valid @RequestBody  ShippingCalendarRequest shippingCalendarRequest) {
        Trade trade = tradeService.queryAll(TradeQueryRequest.builder().id(shippingCalendarRequest.getTid()).build()).get(0);

        List<DeliverCalendar> deliverCalendars=trade.getTradeCycleBuyInfo().getDeliverCalendar();

        List<DeliverCalendarVO> calendarVOS=KsBeanUtil.convert(deliverCalendars,DeliverCalendarVO.class);

        //已送期数
        int size=calendarVOS.stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.SHIPPED).collect(Collectors.toList()).size();

        //总期数
        int totalIssues=calendarVOS.stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()!= CycleDeliverStatus.POSTPONE).collect(Collectors.toList()).size();


        return BaseResponse.success(ShippingCalendarResponse.builder().shippingCalendarVO( ShippingCalendarVO.builder().deliverCalendarVOS(calendarVOS).numberPeriods(size).totalIssues(totalIssues).build()).build());
    }


    /**
     * 查询对账信息
     * @param shippingCalendarRequest
     * @return
     */
    @Override
    public BaseResponse<TradeAccountRecordResponse> getTradeAccountRecord(@Valid @RequestBody  TradeAccountRecordRequest shippingCalendarRequest) {
        return BaseResponse.success(tradeService.getTradeAccountRecord(shippingCalendarRequest));
    }

}
