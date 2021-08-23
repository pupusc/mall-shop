package com.wanmi.ares.report.goods.model.reponse;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Es 提取返回全局商品uv浏览量聚合结果
 *
 * @author daiyitian
 * @version 1.0
 * @since 17/10/12 17:29
 */
@Data
public class GoodsUvAggsResponse implements Serializable{

    /**
     * uv量
     */
    private Long uvCount = 0L;

    private Set<String> uvCustomers;

    /**
     * 转换 聚合结果（全局）
     * @param response
     * @return
     */
//    private GoodsUvAggsResponse setUvCount(SearchResponse response) {
//        if (Objects.nonNull(response.getAggregations())) {
//            Aggregation uvGroup = response.getAggregations().get("uvCount");
//            if (uvGroup != null && uvGroup instanceof Cardinality) {
//                uvCount = ((Cardinality)uvGroup).getValue();
//            }
//            Terms userGroup = response.getAggregations().get("userGroup");
//            if (userGroup != null) {
//                uvCustomers = userGroup.getBuckets().parallelStream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toSet());
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
//    public static GoodsUvAggsResponse buildUvCount(SearchResponse searchResponse) {
//        return new GoodsUvAggsResponse().setUvCount(searchResponse);
//    }
}
