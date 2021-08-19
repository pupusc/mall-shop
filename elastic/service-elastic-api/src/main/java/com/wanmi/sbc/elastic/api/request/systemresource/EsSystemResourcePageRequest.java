package com.wanmi.sbc.elastic.api.request.systemresource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSystemResourcePageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-素材资源IDList
     */
    @ApiModelProperty(value = "批量查询-素材资源IDList")
    private List<Long> resourceIdList;

    /**
     * 素材资源ID
     */
    @ApiModelProperty(value = "素材资源ID")
    private Long resourceId;

    /**
     * 资源类型(0:图片,1:视频)
     */
    @ApiModelProperty(value = "资源类型(0:图片,1:视频)")
    private ResourceType resourceType;

    /**
     * 素材分类ID
     */
    @ApiModelProperty(value = "素材分类ID")
    private Long cateId;

    /**
     * 素材KEY
     */
    @ApiModelProperty(value = "素材KEY")
    private String resourceKey;

    /**
     * 素材名称
     */
    @ApiModelProperty(value = "素材名称")
    private String resourceName;

    /**
     * 素材地址
     */
    @ApiModelProperty(value = "素材地址")
    private String artworkUrl;

    /**
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 搜索条件:更新时间开始
     */
    @ApiModelProperty(value = "搜索条件:更新时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:更新时间截止
     */
    @ApiModelProperty(value = "搜索条件:更新时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 删除标识,0:未删除1:已删除
     */
    @ApiModelProperty(value = "删除标识,0:未删除1:已删除")
    private DeleteFlag delFlag;

    /**
     * oss服务器类型，对应system_config的config_type
     */
    @ApiModelProperty(value = "oss服务器类型，对应system_config的config_type")
    private String serverType;


    @ApiModelProperty(value = "批量查询-素材分类id")
    private List<Long> cateIds;
    /**
     *  0：待审核，1：审核通过，2：审核不通过
     */
    @ApiModelProperty(value = "0：待审核，1：审核通过，2：审核不通过")
    private AuditStatus auditStatus;
    public NativeSearchQuery esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        // 批量查询-素材资源IDList
        if (CollectionUtils.isNotEmpty(this.getResourceIdList())) {
            bool.must(QueryBuilders.termsQuery("distributionId", this.getResourceIdList()));
        }

        // 素材资源ID
        if (this.getResourceId() != null) {
            bool.must(QueryBuilders.termQuery("resourceId", this.getResourceId()));
        }

        // 资源类型(0:图片,1:视频)
        if (this.getResourceType() != null) {
            bool.must(QueryBuilders.termQuery("resourceType", this.getResourceType().toValue()));
        }

        // 素材分类ID
        if (this.getCateId() != null) {
            bool.must(QueryBuilders.termQuery("cateId", this.getCateId()));
        }

        // 模糊查询 - 素材KEY
        if (StringUtils.isNotEmpty(this.getResourceKey())) {
            bool.must(QueryBuilders.wildcardQuery("resourceKey", "*" + this.getResourceKey() + "*"));
        }

        // 模糊查询 - 素材名称
        if (StringUtils.isNotEmpty(this.getResourceName())) {
            bool.must(QueryBuilders.wildcardQuery("resourceName", "*" + this.getResourceName() + "*"));
        }

        // 模糊查询 - 素材地址
        if (StringUtils.isNotEmpty(this.getArtworkUrl())) {
            bool.must(QueryBuilders.wildcardQuery("artworkUrl", "*" + this.getArtworkUrl() + "*"));
        }

        // 大于或等于 搜索条件:创建时间开始
        if (this.getCreateTimeBegin() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").gte(DateUtil.format(this.getCreateTimeBegin(), DateUtil.FMT_TIME_4)));
        }
        // 小于或等于 搜索条件:创建时间截止
        if (this.getCreateTimeEnd() != null) {
            bool.must(QueryBuilders.rangeQuery("createTime").lte(DateUtil.format(this.getCreateTimeBegin(), DateUtil.FMT_TIME_4)));
        }

        // 大于或等于 搜索条件:更新时间开始
        if (this.getUpdateTimeBegin() != null) {
            bool.must(QueryBuilders.rangeQuery("updateTime").gte(DateUtil.format(this.getUpdateTimeBegin(), DateUtil.FMT_TIME_4)));
        }

        // 小于或等于 搜索条件:更新时间截止
        if (this.getUpdateTimeEnd() != null) {
            bool.must(QueryBuilders.rangeQuery("updateTime").lte(DateUtil.format(this.getUpdateTimeBegin(), DateUtil.FMT_TIME_4)));
        }

        // 删除标识,0:未删除1:已删除
        if (this.getDelFlag() != null) {
            bool.must(QueryBuilders.termQuery("delFlag", this.getDelFlag().toValue()));
        }
        // 0：待审核，1：审核通过，2：审核不通过
        if (this.getAuditStatus() != null) {
            bool.must(QueryBuilders.termQuery("auditStatus", this.getAuditStatus().toValue()));
        }

        // 模糊查询 - oss服务器类型，对应system_config的config_type
        if (StringUtils.isNotEmpty(this.getServerType())) {
            bool.must(QueryBuilders.wildcardQuery("serverType", "*" + this.getServerType() + "*"));
        }


        //批量分类编号
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(this.getCateIds())) {
            bool.must(QueryBuilders.termsQuery("cateId", this.getCateIds()));
        }

        FieldSortBuilder createTime = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        FieldSortBuilder resourceId = SortBuilders.fieldSort("_id").order(SortOrder.ASC);
        return new NativeSearchQueryBuilder()
                .withSort(createTime)
                .withSort(resourceId)
                .withQuery(bool)
                .withPageable(this.getPageable())
                .build();

    }

}