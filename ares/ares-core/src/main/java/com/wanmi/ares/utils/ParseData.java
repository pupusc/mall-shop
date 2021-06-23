package com.wanmi.ares.utils;

import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.enums.StatisticsWeekType;
import com.wanmi.ares.report.trade.model.reponse.TradeReponse;
import com.wanmi.ares.report.trade.model.root.TradeBase;
import com.wanmi.ares.report.trade.model.root.TradeReport;
import com.wanmi.ares.request.flow.FlowDataListRequest;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.view.trade.TradeView;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-09-03 14:52
 */
public class ParseData {

    /**
     * 查询日期参数
     * @param request
     * @return
     */
    public static FlowDataListRequest parse(FlowRequest request){
        FlowDataListRequest flowDataListRequest = new FlowDataListRequest();
        BeanUtils.copyProperties(request, flowDataListRequest);
        StatisticsDataType flowDataType;
        if (request.getSelectType().equals("0")) {
            flowDataType = StatisticsDataType.TODAY;
        } else if (request.getSelectType().equals("1")) {
            flowDataType = StatisticsDataType.YESTERDAY;
        } else if (request.getSelectType().equals("2")) {
            flowDataType = StatisticsDataType.SEVEN;
        } else if (request.getSelectType().equals("3")) {
            flowDataType = StatisticsDataType.THIRTY;
            if (request.isWeek()) {
                flowDataType = StatisticsDataType.THIRTY_WEEk;
                flowDataListRequest.setStatisticsWeekType(StatisticsWeekType.THIRTY_WEEk);
            }
        } else {
            flowDataType = StatisticsDataType.MONTH;
            flowDataListRequest.setMonth(request.getSelectType());
            if (request.isWeek()) {
                flowDataType = StatisticsDataType.MONTH_WEEk;
                flowDataListRequest.setStatisticsWeekType(StatisticsWeekType.MONTH_WEEk);
            }
        }
        flowDataListRequest.setStatisticsDataType(flowDataType);
        return flowDataListRequest;
    }

    public static TradeView parseData(TradeBase tradeBase){
        if(tradeBase == null){
            return null;
        }
        TradeView tradeView = new TradeView();
        tradeView.setDate(DateUtil.formatLocalDate(tradeBase.getDate(),DateUtil.FMT_DATE_1));
        tradeView.setOrderCount(tradeBase.getOrderNum() != null? tradeBase.getOrderNum():0);
        tradeView.setOrderNum(tradeBase.getOrderUserNum() != null? tradeBase.getOrderUserNum():0);
        tradeView.setOrderAmt(tradeBase.getOrderMoney() != null ?tradeBase.getOrderMoney().doubleValue():0);
        tradeView.setPayOrderCount(tradeBase.getPayNum() != null ? tradeBase.getPayNum():0);
        tradeView.setPayOrderNum(tradeBase.getPayUserNum() != null? tradeBase.getPayUserNum():0);
        tradeView.setPayOrderAmt(tradeBase.getPayMoney() != null ?tradeBase.getPayMoney().doubleValue():0);

        tradeView.setOrderConversionRate(tradeBase.getOrderConversion() != null ?tradeBase.getOrderConversion().doubleValue():0);
        tradeView.setPayOrderConversionRate(tradeBase.getPayConversion() != null ?tradeBase.getPayConversion().doubleValue():0);
        tradeView.setWholeStoreConversionRate(tradeBase.getAllConversion() != null ?tradeBase.getAllConversion().doubleValue():0);
        tradeView.setCustomerUnitPrice(tradeBase.getUserPerPrice() != null ?tradeBase.getUserPerPrice().doubleValue():0);
        tradeView.setEveryUnitPrice(tradeBase.getOrderPerPrice() != null ?tradeBase.getOrderPerPrice().doubleValue():0);

        tradeView.setReturnOrderCount(tradeBase.getRefundNum() != null?tradeBase.getRefundNum():0);
        tradeView.setReturnOrderNum(tradeBase.getRefundUserNum() != null?tradeBase.getRefundUserNum():0);
        tradeView.setReturnOrderAmt(tradeBase.getRefundMoney() != null ?tradeBase.getRefundMoney().doubleValue():0);

        tradeView.setTotalUv(tradeBase.getUv() != null?tradeBase.getUv():0);

        return tradeView;
    }

    public static TradeReport parseDataReport(TradeBase tradeBase){
        if(tradeBase == null){
            return null;
        }
        TradeReport tradeReport = new TradeReport();
        tradeReport.setId(tradeBase.getCompanyId().toString());
        tradeReport.setDate(tradeBase.getDate());
        tradeReport.setOrderCount(tradeBase.getOrderNum() != null? tradeBase.getOrderNum():0L);
        tradeReport.setOrderNum(tradeBase.getOrderUserNum() != null? tradeBase.getOrderUserNum():0);
        tradeReport.setOrderAmt(tradeBase.getOrderMoney() != null ?tradeBase.getOrderMoney(): BigDecimal.ZERO);
        tradeReport.setPayOrderCount(tradeBase.getPayNum() != null ? tradeBase.getPayNum():0);
        tradeReport.setPayOrderNum(tradeBase.getPayUserNum() != null? tradeBase.getPayUserNum():0);
        tradeReport.setPayOrderAmt(tradeBase.getPayMoney() != null ?tradeBase.getPayMoney():BigDecimal.ZERO);

        tradeReport.setOrderConversionRate(tradeBase.getOrderConversion() != null ?tradeBase.getOrderConversion():BigDecimal.ZERO);
        tradeReport.setPayOrderConversionRate(tradeBase.getPayConversion() != null ?tradeBase.getPayConversion():BigDecimal.ZERO);
        tradeReport.setWholeStoreConversionRate(tradeBase.getAllConversion() != null ?tradeBase.getAllConversion():BigDecimal.ZERO);
        tradeReport.setCustomerUnitPrice(tradeBase.getUserPerPrice() != null ?tradeBase.getUserPerPrice():BigDecimal.ZERO);
        tradeReport.setEveryUnitPrice(tradeBase.getOrderPerPrice() != null ?tradeBase.getOrderPerPrice():BigDecimal.ZERO);

        tradeReport.setReturnOrderCount(tradeBase.getRefundNum() != null?tradeBase.getRefundNum():0L);
        tradeReport.setReturnOrderNum(tradeBase.getRefundUserNum() != null?tradeBase.getRefundUserNum():0L);
        tradeReport.setReturnOrderAmt(tradeBase.getRefundMoney() != null ?tradeBase.getRefundMoney():BigDecimal.ZERO);
        return tradeReport;
    }

    public static TradeReponse parseDataResponse(TradeBase tradeBase){
        if(tradeBase == null){
            return null;
        }
        TradeReponse tradeReponse = new TradeReponse();
        tradeReponse.setId(tradeBase.getCompanyId().toString());
        tradeReponse.setOrderCount(tradeBase.getOrderNum() != null? tradeBase.getOrderNum():0L);
        tradeReponse.setOrderNum(tradeBase.getOrderUserNum() != null? tradeBase.getOrderUserNum():0);
        tradeReponse.setOrderAmt(tradeBase.getOrderMoney() != null ?tradeBase.getOrderMoney(): BigDecimal.ZERO);
        tradeReponse.setPayOrderCount(tradeBase.getPayNum() != null ? tradeBase.getPayNum():0);
        tradeReponse.setPayOrderNum(tradeBase.getPayUserNum() != null? tradeBase.getPayUserNum():0);
        tradeReponse.setPayOrderAmt(tradeBase.getPayMoney() != null ?tradeBase.getPayMoney():BigDecimal.ZERO);

        tradeReponse.setOrderConversionRate(tradeBase.getOrderConversion() != null ?tradeBase.getOrderConversion():BigDecimal.ZERO);
        tradeReponse.setPayOrderConversionRate(tradeBase.getPayConversion() != null ?tradeBase.getPayConversion():BigDecimal.ZERO);
        tradeReponse.setWholeStoreConversionRate(tradeBase.getAllConversion() != null ?tradeBase.getAllConversion():BigDecimal.ZERO);
        tradeReponse.setCustomerUnitPrice(tradeBase.getUserPerPrice() != null ?tradeBase.getUserPerPrice():BigDecimal.ZERO);
        tradeReponse.setEveryUnitPrice(tradeBase.getOrderPerPrice() != null ?tradeBase.getOrderPerPrice():BigDecimal.ZERO);

        tradeReponse.setReturnOrderCount(tradeBase.getRefundNum() != null?tradeBase.getRefundNum():0L);
        tradeReponse.setReturnOrderNum(tradeBase.getRefundUserNum() != null?tradeBase.getRefundUserNum():0L);
        tradeReponse.setReturnOrderAmt(tradeBase.getRefundMoney() != null ?tradeBase.getRefundMoney():BigDecimal.ZERO);
        return tradeReponse;
    }

}
