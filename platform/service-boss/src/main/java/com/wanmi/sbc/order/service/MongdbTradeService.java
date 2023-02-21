package com.wanmi.sbc.order.service;

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
import com.wanmi.sbc.setting.api.request.tradeOrder.GoodsMonthRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.utils.DateUtil;
import com.wanmi.sbc.utils.GoodsDateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class MongdbTradeService {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;
    //获取list
    public List<GoodsMonthRequest> getList() {
        TradeQueryDTO tradeQueryRequest=new TradeQueryDTO();
//        tradeQueryRequest.setBeginTime("2023-01-01");
//        tradeQueryRequest.setEndTime("2023-02-01");

        LocalDate localDate = LocalDate.now();
        LocalDate beforeDate = localDate.minusMonths(1);
        String date = GoodsDateUtil.getDay(1);
        String beginTime = DateUtil.format(LocalDate.of(beforeDate.getYear(), beforeDate.getMonth(), 1),DateUtil.FMT_DATE_1);//前一个月
        String endTime = DateUtil.format(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1),DateUtil.FMT_DATE_1);//本月第一天
        String dateId=DateUtil.format(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1),DateUtil.FMT_MONTH_1);

        tradeQueryRequest.setBeginTime(beginTime);
        tradeQueryRequest.setEndTime(endTime);
        tradeQueryRequest.setPageNum(0);
        tradeQueryRequest.setPageSize(100);
        TradeStateDTO stateDTO=new TradeStateDTO();
        stateDTO.setPayState(PayState.PAID);
        tradeQueryRequest.setTradeState(stateDTO);
        List<TradeItemVO> itemVOList=new ArrayList();
        List<GoodsMonthRequest> requests=new ArrayList<>();
        int totalPages = supplierPage(tradeQueryRequest).getContext().getTotalPages();;
        while (tradeQueryRequest.getPageNum()<=totalPages){
            BaseResponse<MicroServicePage<TradeVO>> supplierPage =supplierPage(tradeQueryRequest);
            List<TradeVO> tradeVOList = supplierPage.getContext().getContent();
            tradeVOList.stream().filter(t->t.getTradeState().getPayState().getStateId().equals("PAID")).forEach(tradeVO -> {itemVOList.addAll(tradeVO.getTradeItems());});
            tradeQueryRequest.setPageNum(tradeQueryRequest.getPageNum()+1);
        }
        itemVOList.forEach(i->{
                if(CollectionUtils.isNotEmpty(requests)){
                    AtomicBoolean flag= new AtomicBoolean(false);
                    requests.stream().filter(r->r.getGoodsInfoId().equals(i.getSpuId())).forEach(r->{
                            r.setPayMoney(r.getPayMoney().add(i.getPrice().multiply(BigDecimal.valueOf(i.getNum()))));
                            r.setPayNum(r.getPayNum().add(BigDecimal.valueOf(i.getNum())));
                            flag.set(true);
                    });
                    if(!flag.get()){
                        GoodsMonthRequest request=new GoodsMonthRequest();
                        request.setId(dateId+"-"+i.getSpuId());
                        request.setGoodsInfoId(i.getSpuId());
                        request.setPayMoney(BigDecimal.ZERO);
                        request.setPayNum(BigDecimal.ZERO);
                        request.setCreatTM(LocalDateTime.now());
                        requests.add(request);
                    }
                }else {
                    GoodsMonthRequest request=new GoodsMonthRequest();
                    request.setId(dateId+"-"+i.getSpuId());
                    request.setGoodsInfoId(i.getSpuId());
                    request.setPayMoney(BigDecimal.ZERO);
                    request.setPayNum(BigDecimal.ZERO);
                    request.setCreatTM(LocalDateTime.now());
                    requests.add(request);
                }
        });
        return requests;
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
