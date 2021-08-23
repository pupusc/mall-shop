package com.wanmi.ares.report.goods.model.reponse;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Es 商品查询 返回聚合结果
 *
 * @author daiyitian
 * @version 1.0
 * @since 17/10/28 17:29
 */
@Data
public class GoodsSkuUvPoolAggsResponse implements Serializable{

    /**
     * sku聚合结果数据
     */
    private Map<String, Long> skuUvMap = new HashMap<>();

    /**
     * 过滤通
     */
    private Set<String> skuIds;

    /**
     * 转换 聚合结果
     * @param response
     * @return
     */
//    private GoodsSkuUvPoolAggsResponse addSkuAggResults(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            Nested skuNested = response.getAggregations().get("skus");
//            if (skuNested != null) {
//                addSkuAggs(skuNested);
//            }
//        }
//        return this;
//    }

    private GoodsSkuUvPoolAggsResponse setSkuIds(Set<String> skuIds) {
        this.skuIds = skuIds;
        return this;
    }

    /**
     * Sku分组块聚合转List
     * @param skuNested
     * @return
     */
//    private void addSkuAggs(Aggregation skuNested){
//        //Sku分组
//        ((Terms)((Nested)skuNested).getAggregations().getAsMap().get("sku_group")).getBuckets().stream().filter(skuGr -> skuIds.contains(skuGr.getKeyAsString())).forEach(skuGr -> {
//            String skuId = skuGr.getKeyAsString();
//            //SKU的类型分组
//            Aggregation skuTypeAgg =  skuGr.getAggregations().getAsMap().get("sku_type_group");
//            if(skuTypeAgg != null && skuTypeAgg instanceof Terms){
//                ((Terms)skuTypeAgg).getBuckets().stream().forEach(sumGr -> {
//                    Long customerCount = sumGr.getDocCount();
//                    Map<String, Aggregation> sumAggMap = sumGr.getAggregations().getAsMap();
//                    if(sumAggMap.get("customer_reverse") instanceof ReverseNested){
//                        ReverseNested uv_order = ((ReverseNested)sumAggMap.get("customer_reverse"));
//                        Long t_customerCount = BigDecimal.valueOf(((Cardinality)(uv_order.getAggregations().get("distinct_customer"))).getValue()).longValue();
//                        if(t_customerCount < customerCount){
//                            customerCount = t_customerCount;
//                        }
//                    }
//                    String orderType = sumGr.getKeyAsString();
//                    if(DataSourceType.CREATE.toValue().equalsIgnoreCase(orderType)){
//                        skuUvMap.put("order_".concat(skuId), customerCount);
//                    }else if(DataSourceType.PAY.toValue().equalsIgnoreCase(orderType)){
//                        skuUvMap.put("pay_".concat(skuId), customerCount);
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsSkuUvPoolAggsResponse build(SearchResponse searchResponse, Set<String> skuIds) {
//        return new GoodsSkuUvPoolAggsResponse().setSkuIds(skuIds).addSkuAggResults(searchResponse);
//    }
//
//    /**
//     * 商品分组块
//     * @return 分组对象
//     * TODO 后期去掉precisionThreshold，采用hash插件算法
//     */
//    public static AbstractAggregationBuilder getAggregation(){
//        return  AggregationBuilders.nested("skus").path("skus")
//                //按SKU分组
//                .subAggregation(AggregationBuilders.terms("sku_group").field("skus.id").size(0)
//                        //按类型分组,如下单/付款/退单
//                        .subAggregation(AggregationBuilders.terms("sku_type_group").field("skus.type").size(0)
//                                .subAggregation(AggregationBuilders.reverseNested("customer_reverse").subAggregation(
//                                        AggregationBuilders.cardinality("distinct_customer").field("murCustomerId.hash")
//                                ))
//                                //统计销量笔数
//                                .subAggregation(AggregationBuilders.reverseNested("data_reverse")
//                                        .subAggregation(AggregationBuilders.cardinality("distinct_order").field("orderNo.hash"))
//                                )
//                        )
//                );
//    }
}
