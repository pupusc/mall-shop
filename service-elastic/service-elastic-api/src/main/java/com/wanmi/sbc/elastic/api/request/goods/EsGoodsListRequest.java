package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGoodsListRequest {

    /**
     * 批量spu编号
     */
    @ApiModelProperty(value = "批量spu编号")
    @NotEmpty
    private List<String> goodsIds;


    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_TYPE);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (CollectionUtils.isNotEmpty(goodsIds)) {
            boolQueryBuilder.must(idsQuery().addIds(goodsIds.toArray(new String[]{})));
        }

        builder.withQuery(boolQueryBuilder);
        return builder.build();
    }
}
