package com.wanmi.ares.report.goods.model.reponse;

import com.wanmi.ares.report.goods.model.root.GoodsReport;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Es 商品分类聚合结果
 *
 * @author daiyitian
 * @version 1.0
 * @since 17/10/28 17:29
 */
@Data
public class GoodsStoreCatePoolAggsResponse implements Serializable{

    /**
     * 商品分类聚合结果数据
     */
    private List<GoodsReport> goodsCateReportList;

    /**
     * 叶子分类ID
     */
    private Set<String> leafCates;

    /**
     * 转换 聚合结果
     * @param response
     * @return
     */
//    private GoodsStoreCatePoolAggsResponse addAggResults(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            this.goodsCateReportList = new ArrayList<>();
//            this.leafCates = new HashSet<>();
//            Nested skuNested = response.getAggregations().get("skus");
//            if (skuNested != null) {
//                //存储叶子分类ID
//                ((Terms)(skuNested).getAggregations().get("leaf_cate_group")).getBuckets().forEach(skuGr -> {
//                    leafCates.add(skuGr.getKeyAsString());
//                });
//                addGoodsAggs(skuNested, Constants.bossId);
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsStoreCatePoolAggsResponse build(SearchResponse searchResponse) {
//        return new GoodsStoreCatePoolAggsResponse().addAggResults(searchResponse);
//    }
//
//    /**
//     * 全局分类分组块
//     * @return 分组对象
//     */
//    public static AbstractAggregationBuilder getAggregation(){
//        return AggregationBuilders.nested("skus").path("skus")
//                //按商品所属分类分组
//                .subAggregation(AggregationBuilders.terms("cate_group").field("skus.storeCateIds").size(0)
//                        //按类型分组,如下单/付款/退单
//                        .subAggregation(AggregationBuilders.terms("sku_type_group").field("skus.type").size(0)
//                                //统计SKU件数
//                                .subAggregation(AggregationBuilders.sum("sku_num_sum").field("skus.num")).size(0)
//                                //统计销售数
//                                .subAggregation(AggregationBuilders.sum("sku_price_sum").field("skus.price")).size(0)
//                                //统计销量笔数
//                                .subAggregation(AggregationBuilders.reverseNested("data_reverse")
//                                        .subAggregation(AggregationBuilders.cardinality("distinct_order").field("orderNo.hash"))
//                                )
//                        )
//                )
//                //按商品当前分类分组
//                .subAggregation(AggregationBuilders.terms("leaf_cate_group").field("skus.leafStoreCateIds").size(0));
//    }
//
//    /**
//     * 转换 聚合结果
//     * @param response
//     * @return
//     */
//    private GoodsStoreCatePoolAggsResponse addAggResultsByComp(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            this.goodsCateReportList = new ArrayList<>();
//            this.leafCates = new HashSet<>();
//            ((Terms)(response.getAggregations().get("company_group"))).getBuckets().forEach(company -> {
//                Nested skuNested = company.getAggregations().get("skus");
//                if (skuNested != null) {
//                    //存储叶子分类ID
//                    ((Terms) (skuNested).getAggregations().get("leaf_cate_group")).getBuckets().forEach(skuGr -> {
//                        leafCates.add(skuGr.getKeyAsString());
//                    });
//                    addGoodsAggs(skuNested, company.getKeyAsString());
//                }
//            });
//        }
//        return this;
//    }
//
//
//    /**
//     * 根据ES查询返回结果 构建 EsDataResponse实例
//     * @param searchResponse
//     * @return
//     */
//    public static GoodsStoreCatePoolAggsResponse buildByComp(SearchResponse searchResponse) {
//        return new GoodsStoreCatePoolAggsResponse().addAggResultsByComp(searchResponse);
//    }
//
//    /**
//     * 全局分类分组块
//     * @return 分组对象
//     */
//    public static AbstractAggregationBuilder getAggregationByComp(){
//        return AggregationBuilders.terms("company_group").field("companyId").size(0)
//                    .subAggregation(getAggregation());
//    }
//
//    /**
//     * Cate和Brand分组块聚合转List
//     * @param skuNested
//     * @return
//     */
//    private void addGoodsAggs(Aggregation skuNested, String companyId){
//        //Sku分组
//        ((Terms)((Nested)skuNested).getAggregations().get("cate_group")).getBuckets().forEach(skuGr -> {
//            GoodsReport goodsReport = new GoodsReport();
//            goodsReport.setId(skuGr.getKeyAsString());
//            goodsReport.setCompanyId(companyId);
//            //SKU的类型分组
//            Aggregation skuTypeAgg =  skuGr.getAggregations().get("sku_type_group");
//            if(skuTypeAgg != null && skuTypeAgg instanceof Terms){
//                ((Terms)skuTypeAgg).getBuckets().forEach(sumGr -> {
//                    BigDecimal skuPriceSum = BigDecimal.ZERO;
//                    Long skuNumSum = 0L;
//                    Long orderCount = 0L;
//
//                    Map<String, Aggregation> sumAggMap = sumGr.getAggregations().getAsMap();
//                    if(sumAggMap.get("sku_price_sum") instanceof Sum){
//                        skuPriceSum = BigDecimal.valueOf(((Sum)sumAggMap.get("sku_price_sum")).getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
//                    }
//                    if(sumAggMap.get("sku_num_sum") instanceof Sum){
//                        skuNumSum = BigDecimal.valueOf(((Sum)sumAggMap.get("sku_num_sum")).getValue()).longValue();
//                    }
//
//                    if(sumAggMap.get("data_reverse") instanceof ReverseNested){
//                        ReverseNested reverse = ((ReverseNested)sumAggMap.get("data_reverse"));
//                        orderCount = BigDecimal.valueOf(((Cardinality)(reverse.getAggregations().get("distinct_order"))).getValue()).longValue();
//                    }
//
//                    String orderType = sumGr.getKeyAsString();
//                    if(DataSourceType.CREATE.toValue().equalsIgnoreCase(orderType)){
//                        goodsReport.setOrderNum(skuNumSum);
//                        goodsReport.setOrderCount(orderCount);
//                        goodsReport.setOrderAmt(skuPriceSum);
//                    }else if(DataSourceType.PAY.toValue().equalsIgnoreCase(orderType)){
//                        goodsReport.setPayNum(skuNumSum);
//                        goodsReport.setPayCount(orderCount);
//                        goodsReport.setPayAmt(skuPriceSum);
//                    }else if(DataSourceType.RETURN.toValue().equalsIgnoreCase(orderType)){
//                        goodsReport.setReturnOrderNum(skuNumSum);
//                        goodsReport.setReturnOrderCount(orderCount);
//                        goodsReport.setReturnOrderAmt(skuPriceSum);
//                    }
//                });
//            }
//            if(CollectionUtils.isNotEmpty(leafCates) && leafCates.contains(goodsReport.getId())){
//                goodsReport.setIsLeaf(EsConstants.yes);
//            }
//            this.goodsCateReportList.add(goodsReport);
//        });
//    }
}
