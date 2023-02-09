package com.wanmi.sbc.quartz;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.api.enums.OrderTagEnum;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.dto.TradeStateDTO;
import com.wanmi.sbc.order.bean.enums.CycleDeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.QueryOrderType;
import com.wanmi.sbc.order.bean.vo.*;
import com.wanmi.sbc.util.CommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MongdbTradeService {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;
    //获取list
    public List<TradeItemVO> getList(String orderTime, int pageSize) {
        TradeQueryDTO tradeQueryRequest=new TradeQueryDTO();
        tradeQueryRequest.setBeginTime("2023-01-01");
        tradeQueryRequest.setEndTime("2023-02-01");
        tradeQueryRequest.setPageNum(0);
        tradeQueryRequest.setPageSize(100);
        TradeStateDTO stateDTO=new TradeStateDTO();
        stateDTO.setPayState(PayState.PAID);
        tradeQueryRequest.setTradeState(stateDTO);
        BaseResponse<MicroServicePage<TradeVO>> supplierPage = supplierPage(tradeQueryRequest);
        List<TradeVO> tradeVOList = supplierPage.getContext().getContent();
        List<TradeItemVO> itemVOList=new ArrayList();
        tradeVOList.forEach(tradeVO -> {
            TradeOrderVO orderVO=new TradeOrderVO();
            orderVO.setCustomer_id(tradeVO.getBuyer().getId());
            orderVO.setCustomer_account(tradeVO.getBuyer().getAccount());
            orderVO.setSupplier_id(tradeVO.getSupplier().getSupplierId());
            orderVO.setStore_id(tradeVO.getSupplier().getStoreId());
            orderVO.setAudit_state(tradeVO.getTradeState().getAuditState().getDescription());
            orderVO.setFlow_state(tradeVO.getTradeState().getFlowState().getDescription());
            orderVO.setPay_state(tradeVO.getTradeState().getPayState().getDescription());
            orderVO.setDeliver_status(tradeVO.getTradeState().getDeliverStatus().getDescription());
            orderVO.setCreate_time(StringUtil.getCurrentAllDate());
            //
        });
        for (TradeVO tradeVO:tradeVOList){
            itemVOList.addAll(tradeVO.getTradeItems());
        }
        return itemVOList;
    }

    public BaseResponse<MicroServicePage<TradeVO>> supplierPage(TradeQueryDTO tradeQueryRequest) {
        if (QueryOrderType.GIFT_ORDER.equals(tradeQueryRequest.getQueryOrderType())) {
            tradeQueryRequest.setQueryOrderType(null);
            tradeQueryRequest.setTag(OrderTagEnum.GIFT.getCode());
        }

        Long companyInfoId = commonUtil.getCompanyInfoId();
        if (companyInfoId != null) {
            tradeQueryRequest.setSupplierId(companyInfoId);
        }

        if (!tradeQueryRequest.getIsBoss()) {
            if (Objects.nonNull(tradeQueryRequest.getTradeState()) && Objects.nonNull(tradeQueryRequest.getTradeState
                    ().getPayState())) {
                tradeQueryRequest.setNotFlowStates(Arrays.asList(FlowState.VOID, FlowState.INIT));
            }
        }
        //设定状态条件逻辑,已审核状态需筛选出已审核与部分发货
        tradeQueryRequest.makeAllAuditFlow();

        MicroServicePage<TradeVO> microServicePage = tradeQueryProvider.supplierPageCriteria(TradePageCriteriaRequest.builder().tradePageDTO(tradeQueryRequest).build()).getContext().getTradePage();

        List<TradeVO> tradeVOList = microServicePage.getContent();
        tradeVOList.forEach(tradeVO -> {
            if (tradeVO.getCycleBuyFlag()) {
                TradeCycleBuyInfoVO tradeCycleBuyInfo = tradeVO.getTradeCycleBuyInfo();
                List<DeliverCalendarVO> deliverCalendarVOList=tradeCycleBuyInfo.getDeliverCalendar().stream().filter(deliverCalendarVO -> deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.NOT_SHIPPED
                        || deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED
                        || deliverCalendarVO.getCycleDeliverStatus()== CycleDeliverStatus.PUSHED).collect(Collectors.toList());
                //下期发货信息
                if (CollectionUtils.isNotEmpty(deliverCalendarVOList)) {
                    DeliverCalendarVO deliverCalendarVO= deliverCalendarVOList.get(0);
                    //获取周几
                    Integer week=deliverCalendarVO.getDeliverDate().getDayOfWeek().getValue();
                    tradeCycleBuyInfo.setWeek(week);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    //获取下一期发货的日期
                    String localTime = df.format(deliverCalendarVO.getDeliverDate());
                    tradeCycleBuyInfo.setLocalTime(localTime);
                    //下一期期数,过滤掉只有赠品的发货记录
                    List<TradeDeliverVO> tradeDelivers=tradeVO.getTradeDelivers().stream().filter(tradeDeliverVO ->CollectionUtils.isNotEmpty(tradeDeliverVO.getShippingItems())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tradeDelivers)) {
                        TradeDeliverVO tradeDeliverVO= tradeDelivers.get(0);
                        if(Objects.nonNull(tradeDeliverVO.getCycleNum())) {
                            tradeCycleBuyInfo.setNumberPeriods(tradeDeliverVO.getCycleNum()+1);
                        }
                    }else {
                        tradeCycleBuyInfo.setNumberPeriods(1);
                    }
                }
            }
        });
        return BaseResponse.success(microServicePage);
    }
}
