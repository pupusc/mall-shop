package com.wanmi.ares.report.goods.model.reponse;

import com.wanmi.ares.report.goods.model.root.SkuReport;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Es 商品查询 返回聚合结果
 *
 * @author daiyitian
 * @version 1.0
 * @since 17/10/28 17:29
 */
@Data
public class GoodsSkuPoolAggsResponse implements Serializable{

    /**
     * sku聚合结果数据
     */
    private List<SkuReport> skuReports;

    /**
     * 转换 聚合结果
     * @param response
     * @return
     */
//    private GoodsSkuPoolAggsResponse addAggResults(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            this.skuReports = new ArrayList<>();
//            ((Terms)(response.getAggregations().get("company_group"))).getBuckets().forEach(company -> {
//                Nested skuNested = company.getAggregations().get("skus");
//                if (skuNested != null) {
//                    addAggs(skuNested, company.getKeyAsString());
//                }
//            });
//        }
//        return this;
//    }
//
//    /**
//     * Sku分组块聚合转List
//     * @param skuNested
//     * @return
//     */
//    private void addAggs(Aggregation skuNested, String companyId){
//        //Sku分组
//        ((Terms)((Nested)skuNested).getAggregations().get("sku_group")).getBuckets().forEach(skuGr -> {
//            SkuReport skuReport = new SkuReport();
//            skuReport.setCompanyId(companyId);
//            skuReport.setId(skuGr.getKeyAsString());
//            //SKU的类型分组
//            Aggregation skuTypeAgg =  skuGr.getAggregations().get("sku_type_group");
//            if(skuTypeAgg != null && skuTypeAgg instanceof Terms){
//                ((Terms)skuTypeAgg).getBuckets().forEach(sumGr -> {
//
//                    BigDecimal skuPriceSum = BigDecimal.ZERO;
//                    Long skuNumSum = 0L;
//                    Long customerCount = sumGr.getDocCount();
//                    Long orderCount = sumGr.getDocCount();
//
//                    Map<String, Aggregation> sumAggMap = sumGr.getAggregations().getAsMap();
//                    if(sumAggMap.get("sku_price_sum") instanceof Sum){
//                        skuPriceSum = BigDecimal.valueOf(((Sum)sumAggMap.get("sku_price_sum")).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                    }
//                    if(sumAggMap.get("sku_num_sum") instanceof Sum){
//                        skuNumSum = BigDecimal.valueOf(((Sum)sumAggMap.get("sku_num_sum")).getValue()).longValue();
//                    }
//                    if(sumAggMap.get("customer_reverse") instanceof ReverseNested){
//                        ReverseNested uv_order = ((ReverseNested)sumAggMap.get("customer_reverse"));
//                        Long t_customerCount = BigDecimal.valueOf(((Cardinality)(uv_order.getAggregations().get("distinct_customer"))).getValue()).longValue();
//                        if(t_customerCount < customerCount){
//                            customerCount = t_customerCount;
//                        }
//                    }
//
//                    if(sumAggMap.get("data_reverse") instanceof ReverseNested){
//                        ReverseNested reverse = ((ReverseNested)sumAggMap.get("data_reverse"));
//                        Long t_orderCount = BigDecimal.valueOf(((Cardinality)(reverse.getAggregations().get("distinct_order"))).getValue()).longValue();
//                        if(t_orderCount < orderCount){
//                            orderCount = t_orderCount;
//                        }
//                    }
//
//                    String orderType = sumGr.getKeyAsString();
//                    if(DataSourceType.CREATE.toValue().equalsIgnoreCase(orderType)){
//                        skuReport.setOrderNum(skuNumSum);
//                        skuReport.setOrderCount(orderCount);
//                        skuReport.setOrderAmt(skuPriceSum);
//                        skuReport.setCustomerCount(customerCount);
//                    }else if(DataSourceType.PAY.toValue().equalsIgnoreCase(orderType)){
//                        skuReport.setPayNum(skuNumSum);
//                        skuReport.setPayCount(orderCount);
//                        skuReport.setPayAmt(skuPriceSum);
//                        skuReport.setPayCustomerCount(customerCount);
//                    }else if(DataSourceType.RETURN.toValue().equalsIgnoreCase(orderType)){
//                        skuReport.setReturnOrderNum(skuNumSum);
//                        skuReport.setReturnOrderCount(orderCount);
//                        skuReport.setReturnOrderAmt(skuPriceSum);
//                    }
//                });
//            }
//            this.skuReports.add(skuReport);
//        });
//    }
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsSkuPoolAggsResponse build(SearchResponse searchResponse) {
//        return new GoodsSkuPoolAggsResponse().addAggResults(searchResponse);
//    }
//
//
//    /**
//     * 聚合请求
//     * @return 分组对象
//     */
//    public static AbstractAggregationBuilder getAggregation(){
//        //按供应商分组
//        return  AggregationBuilders.terms("company_group").field("companyId").size(0)
//                .subAggregation(AggregationBuilders.nested("skus").path("skus")
//                        //按SKU分组
//                        .subAggregation(AggregationBuilders.terms("sku_group").field("skus.id").size(0)
//                                //按类型分组,如下单/付款/退单
//                                .subAggregation(AggregationBuilders.terms("sku_type_group").field("skus.type").size(0)
//                                        //统计SKU件数
//                                        .subAggregation(AggregationBuilders.sum("sku_num_sum").field("skus.num")).size(0)
//                                        //统计销售数
//                                        .subAggregation(AggregationBuilders.sum("sku_price_sum").field("skus.price")).size(0)
//                                        .subAggregation(AggregationBuilders.reverseNested("customer_reverse").subAggregation(
//                                                AggregationBuilders.cardinality("distinct_customer").field("murCustomerId.hash")
//                                        ))
//                                        //统计销量笔数
//                                        .subAggregation(AggregationBuilders.reverseNested("data_reverse")
//                                                .subAggregation(AggregationBuilders.cardinality("distinct_order").field("orderNo.hash"))
//                                        )
//                                )
//                        )
//                );
//    }
}
