package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.PaymentOrder;
import com.wanmi.sbc.order.bean.vo.ProviderTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.root.ProviderTrade;
import com.wanmi.sbc.order.trade.request.ProviderTradeQueryRequest;
import com.wanmi.sbc.order.trade.service.ProviderTradeService;
import com.wanmi.sbc.order.trade.service.TradeCacheService;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 供应商订单处理
 * @Autho qiaokang
 * @Date：2020-03-27 09:17
 */
@Validated
@RestController
public class ProviderTradeQueryController implements ProviderTradeQueryProvider {

    @Autowired
    private ProviderTradeService providerTradeService;

    @Autowired
    private TradeCacheService tradeCacheService;

    /**
     * 分页查询供应商订单
     *
     * @param tradePageCriteriaRequest 带参分页参数
     * @return
     */
    @Override
    public BaseResponse<TradePageCriteriaResponse> providerPageCriteria(@RequestBody @Valid ProviderTradePageCriteriaRequest tradePageCriteriaRequest) {
        ProviderTradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradePageCriteriaRequest.getTradePageDTO(), ProviderTradeQueryRequest.class);
        // 查询订单支付顺序设置
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_PAYMENT_ORDER);
        Integer paymentOrder = tradeCacheService.getTradeConfigByType(ConfigType.ORDER_SETTING_PAYMENT_ORDER).getStatus();
        if(PaymentOrder.PAY_FIRST == PaymentOrder.values()[paymentOrder]) {
            tradeQueryRequest.getTradeState().setPayState(PayState.PAID);
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
        Page<ProviderTrade> page = providerTradeService.providerPage(criteria, tradeQueryRequest);
        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);
        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
    }

    /**
     * 查询供应商订单
     *
     * @param tradeGetByIdRequest 交易单id {@link TradeGetByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<TradeGetByIdResponse> providerGetById(@RequestBody @Valid TradeGetByIdRequest tradeGetByIdRequest) {
        ProviderTrade trade = providerTradeService.providerDetail(tradeGetByIdRequest.getTid());

        TradeVO tradeVO = KsBeanUtil.convert(trade, TradeVO.class);
        if (tradeVO != null && CollectionUtils.isNotEmpty(tradeVO.getTradeItems())) {
            List<ProviderTrade> providerTradeList = providerTradeService.findListByParentId(trade.getParentId());

            StringBuffer subOrderNo = new StringBuffer();
            if (CollectionUtils.isNotEmpty(providerTradeList)) {
                for (int i = 0; i < providerTradeList.size(); i++) {
                    if (i == providerTradeList.size() - 1) {
                        subOrderNo.append(providerTradeList.get(i).getId());
                    } else {
                        subOrderNo.append(providerTradeList.get(i).getId() + ",");
                    }
                }
            }

//            for (TradeItemVO item : tradeVO.getTradeItems()) {
//                item.setSubOrderNo(subOrderNo.toString());
//                Long providerId = item.getProviderId();
//                item.setProviderTradeId(providerTradeList.stream().filter(providerTrade ->
//                        providerTrade.getProvider().getStoreId().equals(providerId)
//                ).collect(Collectors.toList()).get(0).getId());
//            }

        }

        BaseResponse<TradeGetByIdResponse> baseResponse = BaseResponse.success(TradeGetByIdResponse.builder().
                tradeVO(tradeVO).build());
        return baseResponse;
    }

    @Override
    public BaseResponse<ProviderTradeGetByIdsResponse> providerGetByIds(@Valid ProviderTradeGetByIdListRequest providerTradeGetByIdListRequest) {
        List<ProviderTrade> providerTradeList = providerTradeService.details(providerTradeGetByIdListRequest.getIdList());
        List<ProviderTradeVO> providerTradeVOList = KsBeanUtil.convert(providerTradeList, ProviderTradeVO.class);
        return BaseResponse.success(new ProviderTradeGetByIdsResponse(providerTradeVOList));
    }

    /**
     * 根据父订单号查询供应商订单
     *
     * @param tradeListByParentIdRequest 父交易单id {@link TradeListByParentIdRequest}
     * @return
     */
    @Override
    public BaseResponse<TradeListByParentIdResponse> getProviderListByParentId(@RequestBody @Valid TradeListByParentIdRequest tradeListByParentIdRequest) {
        List<ProviderTrade> tradeList =
                providerTradeService.findListByParentId(tradeListByParentIdRequest.getParentTid());
        if (tradeList.isEmpty()) {
            return BaseResponse.success(TradeListByParentIdResponse.builder().tradeVOList(Collections.emptyList()).build());
        }
        // 父订单号对应的子订单的买家信息应该是相同的
        ProviderTrade trade = tradeList.get(0);

        final Buyer buyer = trade.getBuyer();
        //统一设置账号加密后的买家信息
        List<TradeVO> tradeVOList = tradeList.stream().map(i -> {
            i.setBuyer(buyer);
            return KsBeanUtil.convert(i, TradeVO.class);
        }).collect(Collectors.toList());
        return BaseResponse.success(TradeListByParentIdResponse.builder().tradeVOList(tradeVOList).build());
    }

    /**
     * 查询导出数据
     *
     * @param tradeListExportRequest 查询条件 {@link TradeListExportRequest}
     * @return
     */
    @Override
    public BaseResponse<TradeListExportResponse> providerListTradeExport(@RequestBody @Valid TradeListExportRequest tradeListExportRequest) {
        ProviderTradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(tradeListExportRequest.getTradeQueryDTO(),
                ProviderTradeQueryRequest.class);
        List<ProviderTrade> tradeList = providerTradeService.listProviderTradeExport(tradeQueryRequest);
        return BaseResponse.success(TradeListExportResponse.builder().tradeVOList(KsBeanUtil.convert(tradeList,
                TradeVO.class)).build());
    }

    /**
     * 按条件统计数量
     * @param tradeCountCriteriaRequest 带参分页参数 {@link TradeCountCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<ProviderTradeCountCriteriaResponse> countCriteria(@RequestBody @Valid ProviderTradeCountCriteriaRequest tradeCountCriteriaRequest) {
        ProviderTradeQueryRequest providerTradeQueryRequest = KsBeanUtil.convert(
                tradeCountCriteriaRequest.getTradePageDTO(), ProviderTradeQueryRequest.class);
        long count = providerTradeService.countNum(providerTradeQueryRequest.getWhereCriteria(), providerTradeQueryRequest);
        return BaseResponse.success(ProviderTradeCountCriteriaResponse.builder().count(count).build());
    }

    /**
     * 批量推送周期购订单
     * @param providerTradePushErpRequest
     * @return
     */
    @Override
    public BaseResponse batchPushCycleOrder(@RequestBody @Valid ProviderTradeErpRequest providerTradePushErpRequest) {
        providerTradeService.batchPushCycleOrder(providerTradePushErpRequest.getPageSize());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量同步发货单状态
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchSyncDeliveryStatus(@RequestBody @Valid ProviderTradeErpRequest request) {
        providerTradeService.batchSyncDeliveryStatus(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量补偿推送erp普通订单
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchPushOrder(@Valid ProviderTradeErpRequest request) {
        providerTradeService.batchPushOrder(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量重置订单扫描次数
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchResetScanCount(@RequestBody @Valid ProviderTradeErpRequest request) {
        providerTradeService.batchResetScanCount(request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 扫描未发货订单同步erp发货状态
     * @param request
     * @return
     */
    @Override
    public BaseResponse scanNotYetShippedTrade(@RequestBody @Valid ProviderTradeErpRequest request) {
        providerTradeService.scanNotYetShippedTrade(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 同步历史发货单
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchSyncHistoryOrderStatus(@Valid ProviderTradeErpRequest request) {
        providerTradeService.batchSyncHistoryOrderStatus(request.getStartTime(),request.getEndTime(),request.getPageSize(),request.getPageNum());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 同步订单到微信
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchSyncDeliveryStatusToWechat(@RequestBody @Valid ProviderTradeErpRequest request) {
        providerTradeService.batchSyncDeliveryStatus(request.getPageSize(),request.getPtid());
        return BaseResponse.SUCCESSFUL();
    }
}
