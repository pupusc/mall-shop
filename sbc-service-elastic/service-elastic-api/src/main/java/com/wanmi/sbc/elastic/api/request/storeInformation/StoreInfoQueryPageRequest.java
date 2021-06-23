package com.wanmi.sbc.elastic.api.request.storeInformation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Author yangzhen
 * @Description // es店铺信息
 * @Date 18:30 2020/12/17
 * @Param
 * @return
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class StoreInfoQueryPageRequest extends BaseQueryRequest {



    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺名称关键字
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;


    /**
     * 店铺类型
     */
    @ApiModelProperty(value = "店铺类型")
    private StoreType storeType;






    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_STORE_INFORMATION_TYPE);
        QueryBuilder script = this.getWhereCriteria();
//        System.out.println("company list where===>"+script.toString());
        builder.withQuery(script);
        builder.withPageable(this.getPageable());

        return builder.build();
    }

    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //根据店铺id查询
        if(Objects.nonNull(storeId)){
            boolQueryBuilder.must(termQuery("storeId",storeId));
        }
        if(Objects.nonNull(storeType)){
            boolQueryBuilder.must(termQuery("storeType",storeType.toValue()));
        }
        //店铺名称模糊查询
        if (StringUtils.isNotBlank(storeName)) {
            boolQueryBuilder.must(wildcardQuery("storeName", ElasticCommonUtil.replaceEsLikeWildcard(storeName)));
        }

        return boolQueryBuilder;
    }

}
