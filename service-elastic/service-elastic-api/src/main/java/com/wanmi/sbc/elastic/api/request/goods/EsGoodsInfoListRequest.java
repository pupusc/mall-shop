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
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGoodsInfoListRequest {

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;

    /**
     * spu编号
     */
    @ApiModelProperty(value = "spu编号")
    private String goodsId;


    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_INFO_TYPE);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String queryName = "goodsInfo";

        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            boolQueryBuilder.must(termsQuery(queryName.concat(".goodsInfoId"), goodsInfoIds));
        }

        if (Objects.nonNull(goodsId)) {
            boolQueryBuilder.must(termsQuery(queryName.concat(".goodsId"), goodsId));
        }

        builder.withQuery(boolQueryBuilder);
        return builder.build();
    }
}
