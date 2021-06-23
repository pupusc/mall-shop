package com.wanmi.ares.report.trade.model.reponse;

import com.wanmi.ares.report.trade.model.root.TradeReport;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by sunkun on 2017/10/28.
 */
@Data
public class TradeAggsResponse implements Serializable {

    private TradeReport tradeReport;

//    private TradeAggsResponse countTradeReport(SearchResponse searchResponse) {
//        List<String> list = new ArrayList<>();
//        TradeReport tradeReport = new TradeReport();
//        ((Terms) (searchResponse).getAggregations().getAsMap().get("orderType")).getBuckets().stream().forEach(info -> {
//            Long num = (long) ((Cardinality) (info.getAggregations().getAsMap().get("num"))).getValue();
//            BigDecimal bigDecimal = new BigDecimal(((InternalSum) (info.getAggregations().getAsMap().get("realAmtSum"))).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
//            Long count = info.getDocCount();
//            switch (info.getKeyAsString()) {
//                case "CREATE":
//                    tradeReport.setOrderCount(count);
//                    tradeReport.setOrderNum(num);
//                    bigDecimal = new BigDecimal(((InternalSum) (info.getAggregations().getAsMap().get("orderAmtSum"))).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                    tradeReport.setOrderAmt(bigDecimal);
//                    break;
//                case "PAY":
//                    tradeReport.setPayOrderCount(count);
//                    tradeReport.setPayOrderNum(num);
//                    tradeReport.setPayOrderAmt(bigDecimal);
//                    break;
//                case "RETURN":
//                    tradeReport.setReturnOrderCount(count);
//                    tradeReport.setReturnOrderNum(num);
//                    tradeReport.setReturnOrderAmt(bigDecimal);
//            }
//
////            System.err.println(info.getKeyAsString());
////            System.err.println(((InternalSum) (info.getAggregations().getAsMap().get("orderAmtSum"))).getValue());
////            System.err.println(info.getDocCount());
//        });
//        this.setTradeReport(tradeReport);
//        return this;
//    }
//
//
//    public static TradeAggsResponse build(SearchResponse searchResponse) {
//
//        return new TradeAggsResponse().countTradeReport(searchResponse);
//    }
}
