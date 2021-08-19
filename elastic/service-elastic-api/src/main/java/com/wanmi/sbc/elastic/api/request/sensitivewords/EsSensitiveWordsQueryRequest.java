package com.wanmi.sbc.elastic.api.request.sensitivewords;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author houshuai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSensitiveWordsQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-敏感词id 主键List
     */
    private List<Long> sensitiveIdList;

    /**
     * 敏感词id 主键
     */
    private Long sensitiveId;

    /**
     * 敏感词内容
     */
    private String sensitiveWords;

    /**
     * 敏感词内容
     */
    private String likeSensitiveWords;

    /**
     * 是否删除
     */
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 搜索条件:创建时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 搜索条件:修改时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:修改时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 删除人
     */
    private String deleteUser;

    /**
     * 搜索条件:删除时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTimeBegin;
    /**
     * 搜索条件:删除时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTimeEnd;


    public NativeSearchQuery esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        // 批量查询-敏感词id 主键List
        if (CollectionUtils.isNotEmpty(this.getSensitiveIdList())) {
            bool.must(QueryBuilders.termsQuery("sensitiveId", this.getSensitiveIdList()));
        }

        // 敏感词id 主键
        if (this.getSensitiveId() != null) {
            bool.must(QueryBuilders.termQuery("sensitiveId", this.getSensitiveId()));
        }

        //敏感词名称查询
        if (StringUtils.isNotEmpty(this.getSensitiveWords())) {
            bool.must(QueryBuilders.termQuery("sensitiveWords", this.getSensitiveWords()));
        }

        // 模糊查询 - 敏感词内容
        if (StringUtils.isNotEmpty(this.getLikeSensitiveWords())) {
            bool.must(QueryBuilders.wildcardQuery("sensitiveWords", "*" + this.getLikeSensitiveWords() + "*"));
        }

        // 是否删除
        if (this.getDelFlag() != null) {
            bool.must(QueryBuilders.termQuery("delFlag", this.getDelFlag().toValue()));
        }

        FieldSortBuilder createTime = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        FieldSortBuilder sensitiveId = SortBuilders.fieldSort("_id").order(SortOrder.DESC);
        return new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(this.getPageable())
                .withSort(createTime)
                .withSort(sensitiveId)
                .build();
    }

}