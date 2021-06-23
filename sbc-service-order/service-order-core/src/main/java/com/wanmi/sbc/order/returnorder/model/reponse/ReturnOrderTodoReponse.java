//package com.wanmi.sbc.order.returnorder.model.reponse;
//
//import lombok.Data;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//
///**
// * Created by sunkun on 2017/9/18.
// */
//@Data
//public class ReturnOrderTodoReponse {
//
//    /**
//     * 待审核
//     */
//    private long waitAudit;
//
//    /**
//     * 待填写物流
//     */
//    private long waitFillLogistics;
//
//    /**
//     * 待收货
//     */
//    private long waitReceiving;
//
//    /**
//     * 待退款
//     */
//    private long waitRefund = 0;
//
//    private ReturnOrderTodoReponse countReturnOrderTodoReponse(SearchResponse searchResponse) {
//        ((Terms) (searchResponse).getAggregations().getAsMap().get("returnType")).getBuckets().stream().forEach(info -> {
//            Long count = info.getDocCount();
//            switch (info.getKey().toString()) {
//                case "init":
//                    this.waitAudit = count;
//                    break;
//                case "audit":
//                    this.waitFillLogistics = count;
//                    this.waitRefund += count;
//                    break;
//                case "delivered":
//                    this.waitReceiving = count;
//                    break;
//                case "received":
//                    this.waitRefund += count;
//            }
//        });
//        return this;
//    }
//
//    public static ReturnOrderTodoReponse build(SearchResponse searchResponse) {
//
//        return new ReturnOrderTodoReponse().countReturnOrderTodoReponse(searchResponse);
//    }
//}
