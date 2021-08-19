package com.wanmi.ares.report.goods.model.reponse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Es 商品返回全局商品总数、商品上下架总数聚合结果
 *
 * @author daiyitian
 * @version 1.0
 * @since 17/10/12 17:29
 */
@Data
public class GoodsTotalAggsResponse implements Serializable{

    /**
     * sku聚合结果数据
     */
    private List<GoodsTotalResponse> goodsTotals;


    /**
     * 转换 聚合结果（商户分组）
     * @param response
     * @return
     */
//    private GoodsTotalAggsResponse addCompanyAggResults(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            this.goodsTotals = new ArrayList<>();
//
//            Aggregation companyGroup = response.getAggregations().get("company_group");
//            //商户分组
//            if (companyGroup != null && companyGroup instanceof Terms) {
//                if (CollectionUtils.isNotEmpty(((Terms) companyGroup).getBuckets())) {
//                    ((Terms) companyGroup).getBuckets().forEach(bucket -> {
//                        GoodsTotalResponse total = new GoodsTotalResponse();
//                        total.setCompanyId(bucket.getKeyAsString());
//                        total.setTotal(bucket.getDocCount());
//                        if (bucket.getAggregations() != null) {
//                            Map<String, Aggregation> groupMap = bucket.getAggregations().getAsMap();
//                            if (MapUtils.isNotEmpty(groupMap)) {
//                                Aggregation addedGroup = groupMap.get("added_group");
//                                if (addedGroup != null && addedGroup instanceof InternalDateRange) {
//                                    ((InternalDateRange)addedGroup).getBuckets().forEach(addedBucket -> {
//                                        Terms addedFlagGroup = addedBucket.getAggregations().get("added_flag_group");
//                                        if(addedFlagGroup != null){
//                                            Terms.Bucket bucket1 = addedFlagGroup.getBucketByKey("1");
//                                            if(bucket1 != null) {
//                                                total.setAddedTotal(bucket1.getDocCount());
//                                            }
//                                        }
//                                    });
//                                }
//
//                                Aggregation checkedGroup = groupMap.get("checked_group");
//                                if (checkedGroup != null && checkedGroup instanceof InternalDateRange) {
//                                    ((InternalDateRange)checkedGroup).getBuckets().forEach(checkedBucket -> {
//                                        Terms addedFlagGroup = checkedBucket.getAggregations().get("checked_status_group");
//                                        if(addedFlagGroup != null){
//                                            Terms.Bucket bucket1 = addedFlagGroup.getBucketByKey("1");
//                                            if(bucket1 != null) {
//                                                total.setCheckedTotal(bucket1.getDocCount());
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                        this.goodsTotals.add(total);
//                    });
//                }
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 转换 聚合结果（全局）
//     * @param response
//     * @return
//     */
//    private GoodsTotalAggsResponse addAllAggResults(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            this.goodsTotals = new ArrayList<>();
//            GoodsTotalResponse total = new GoodsTotalResponse();
//            total.setTotal(response.getHits().getTotalHits());
//            Aggregation addedGroup = response.getAggregations().get("added_group");
//            if (addedGroup != null && addedGroup instanceof InternalDateRange) {
//                ((InternalDateRange)addedGroup).getBuckets().forEach(addedBucket -> {
//                    Terms addedFlagGroup = addedBucket.getAggregations().get("added_flag_group");
//                    if(addedFlagGroup != null){
//                        Terms.Bucket bucket1 = addedFlagGroup.getBucketByKey("1");
//                        if(bucket1 != null) {
//                            total.setAddedTotal(bucket1.getDocCount());
//                        }
//                    }
//                });
//            }
//
//            Aggregation checkedGroup = response.getAggregations().get("checked_group");
//            if (checkedGroup != null && checkedGroup instanceof InternalDateRange) {
//                ((InternalDateRange)checkedGroup).getBuckets().forEach(checkedBucket -> {
//                    Terms checkStatusGroup = checkedBucket.getAggregations().get("checked_status_group");
//                    if(checkStatusGroup != null){
//                        Terms.Bucket bucket1 = checkStatusGroup.getBucketByKey("1");
//                        if(bucket1 != null) {
//                            total.setCheckedTotal(bucket1.getDocCount());
//                        }
//                    }
//                });
//            }
//            this.goodsTotals.add(total);
//
//        }
//        return this;
//    }
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsTotalAggsResponse buildByCompanyGroup(SearchResponse searchResponse) {
//        return new GoodsTotalAggsResponse().addCompanyAggResults(searchResponse);
//    }
//
//    /**
//     * 按商户分组请求聚合参数
//     * @param addedEndTime
//     * @return
//     */
//    public static AbstractAggregationBuilder getAggregationByCompanyGroup(String addedEndTime){
//        return
//                AggregationBuilders.terms("company_group").field("companyId").size(0)
//                    .subAggregation(AggregationBuilders.dateRange("added_group").field("addedTime").format(DateUtil.FMT_TIME_1).addRange(null, addedEndTime.concat(" 23:59:59"))
//                        .subAggregation(AggregationBuilders.terms("added_flag_group").field("addedFlag").size(0)))
//                    .subAggregation(AggregationBuilders.dateRange("checked_group").field("submitTime").format(DateUtil.FMT_TIME_1).addRange(null, addedEndTime.concat(" 23:59:59"))
//                        .subAggregation(AggregationBuilders.terms("checked_status_group").field("auditStatus").size(0)));
//    }
//
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsTotalAggsResponse buildByAll(SearchResponse searchResponse) {
//        return new GoodsTotalAggsResponse().addAllAggResults(searchResponse);
//    }
//
//    /**
//     * 全局请求聚合参数
//     * @param addedEndTime
//     * @return
//     */
//    public static List<AbstractAggregationBuilder> getAggregationByAll(String addedEndTime){
//        return Arrays.asList(
//                    AggregationBuilders.dateRange("added_group").field("addedTime").format(DateUtil.FMT_TIME_1).addRange(null, addedEndTime.concat(" 23:59:59"))
//                        .subAggregation(AggregationBuilders.terms("added_flag_group").field("addedFlag").size(0)),
//                    AggregationBuilders.dateRange("checked_group").field("submitTime").format(DateUtil.FMT_TIME_1).addRange(null, addedEndTime.concat(" 23:59:59"))
//                        .subAggregation(AggregationBuilders.terms("checked_status_group").field("auditStatus").size(0))
//                );
//    }
}
