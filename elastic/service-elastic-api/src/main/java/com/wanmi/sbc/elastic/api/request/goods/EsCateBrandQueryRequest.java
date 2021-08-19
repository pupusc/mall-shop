package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * 分类品牌查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsCateBrandQueryRequest extends BaseQueryRequest {

    /**
     * 批量商品分类ID
     */
    @ApiModelProperty(value = "批量商品分类ID")
    private List<Long> goodsCateIds;

    /**
     * 批量商品品牌ID
     */
    //todo
    @ApiModelProperty(value = "批量商品品牌ID")
    private List<Long> goodsBrandIds;


    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //批量商品分类ID
        if(CollectionUtils.isNotEmpty(goodsCateIds)){
            boolQueryBuilder.must(termsQuery("goodsCate.cateId",goodsCateIds));
        }
        //批量商品品牌ID
        if(CollectionUtils.isNotEmpty(goodsBrandIds)){
            boolQueryBuilder.must(termsQuery("goodsBrand.brandId",goodsBrandIds));
        }
        return boolQueryBuilder;
    }
}
