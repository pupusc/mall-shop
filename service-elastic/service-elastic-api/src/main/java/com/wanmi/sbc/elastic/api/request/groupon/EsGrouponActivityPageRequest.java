package com.wanmi.sbc.elastic.api.request.groupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.bean.enums.GrouponTabTypeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author houshuai
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGrouponActivityPageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 8805638492638567858L;
    /**
     * 批量查询-活动IDList
     */
    @ApiModelProperty(value = "批量查询-活动IDList")
    private List<String> grouponActivityIdList;

    /**
     * 活动ID
     */
    @ApiModelProperty(value = "是否包邮，0：否，1：是")
    private String grouponActivityId;

    /**
     * 拼团人数
     */
    @ApiModelProperty(value = "拼团人数")
    private Integer grouponNum;

    /**
     * 搜索条件:开始时间开始
     */
    @ApiModelProperty(value = "搜索条件:开始时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTimeBegin;
    /**
     * 搜索条件:开始时间截止
     */
    @ApiModelProperty(value = "搜索条件:开始时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTimeEnd;

    /**
     * 搜索条件:结束时间开始
     */
    @ApiModelProperty(value = "搜索条件:结束时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTimeBegin;
    /**
     * 搜索条件:结束时间截止
     */
    @ApiModelProperty(value = "搜索条件:结束时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTimeEnd;


    /**
     * 搜索条件:开始时间开始
     */
    @ApiModelProperty(value = "搜索条件:开始时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    /**
     * 搜索条件:开始时间截止
     */
    @ApiModelProperty(value = "搜索条件:开始时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;
    /**
     * 拼团分类ID
     */
    @ApiModelProperty(value = "拼团分类ID")
    private String grouponCateId;

    /**
     * 是否自动成团
     */
    @ApiModelProperty(value = "是否自动成团")
    private Boolean autoGroupon;

    /**
     * 是否包邮
     */
    @ApiModelProperty(value = "是否包邮")
    private Boolean freeDelivery;

    /**
     * spu编号
     */
    @ApiModelProperty(value = "spu编号")
    private String goodsId;

    /**
     * spu编码
     */
    @ApiModelProperty(value = "spu编码")
    private String goodsNo;

    /**
     * spu商品名称
     */
    @ApiModelProperty(value = "spu商品名称")
    private String goodsName;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private String storeId;

    /**
     * 是否精选
     */
    @ApiModelProperty(value = "是否精选")
    private Boolean sticky;

    /**
     * 是否审核通过，0：待审核，1：审核通过，2：审核不通过
     */
    @ApiModelProperty(value = "是否审核通过")
    private AuditStatus auditStatus;


    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag = DeleteFlag.NO;


    /**
     *  页面tab类型，0: 即将开始, 1: 进行中, 2: 已结束，3：待审核，4：审核失败
     */
    @ApiModelProperty(value = " 页面tab类型")
    private GrouponTabTypeStatus tabType;


    /**
     * 批量查询-spu
     */
    private List<String> spuIdList;

    public BoolQueryBuilder esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        // 批量查询-id 主键List
        // 批量查询-活动IDList
        if (CollectionUtils.isNotEmpty(this.getGrouponActivityIdList())) {
            bool.must(QueryBuilders.termsQuery("grouponActivityId", this.getGrouponActivityIdList()));
        }

        // 活动ID
        if (StringUtils.isNotEmpty(this.getGrouponActivityId())) {
            bool.must(QueryBuilders.termQuery("grouponActivityId", this.getGrouponActivityId()));
        }

        // 拼团人数
        if (this.getGrouponNum() != null) {
            bool.must(QueryBuilders.termQuery("grouponNum", this.getGrouponNum()));
        }

        // 大于或等于 搜索条件:开始时间开始
        if (this.getStartTimeBegin() != null) {
            bool.must(QueryBuilders.rangeQuery("startTime").gte(DateUtil.format(this.getStartTimeBegin(), DateUtil.FMT_TIME_4)));
        }
        // 小于或等于 搜索条件:开始时间截止
        if (this.getStartTimeEnd() != null) {
            bool.must(QueryBuilders.rangeQuery("startTime").lte(DateUtil.format(this.getStartTimeEnd(), DateUtil.FMT_TIME_4)));
        }

        // 大于或等于 搜索条件:结束时间开始
        if (this.getEndTimeBegin() != null) {
            bool.must(QueryBuilders.rangeQuery("endTime").gte(DateUtil.format(this.getEndTimeBegin(), DateUtil.FMT_TIME_4)));
        }
        // 小于或等于 搜索条件:结束时间截止
        if (this.getEndTimeEnd() != null) {
            bool.must(QueryBuilders.rangeQuery("endTime").lte(DateUtil.format(this.getEndTimeEnd(), DateUtil.FMT_TIME_4)));
        }

        // 模糊查询 - 拼团分类ID
        if (StringUtils.isNotEmpty(this.getGrouponCateId())) {
            bool.must(QueryBuilders.wildcardQuery("grouponCateId", "*" + this.getGrouponCateId() + "*"));
        }

        // 是否自动成团，0：否，1：是
        if (Objects.nonNull(this.getAutoGroupon())) {
            bool.must(QueryBuilders.termQuery("autoGroupon", this.getAutoGroupon()));
        }

        // 是否包邮，0：否，1：是
        if (Objects.nonNull(this.getFreeDelivery())) {
            bool.must(QueryBuilders.termQuery("freeDelivery", this.getFreeDelivery()));
        }

        // 模糊查询 - spu编号
        if (StringUtils.isNotEmpty(this.getGoodsId())) {
            bool.must(QueryBuilders.wildcardQuery("goodsId", "*" + this.getGoodsId() + "*"));
        }

        //模糊查询 - spu编码
        if (StringUtils.isNotEmpty(this.getGoodsNo())) {
            bool.must(QueryBuilders.wildcardQuery("goodsNo", "*" + this.getGoodsNo() + "*"));
        }

        // 模糊查询 - spu商品名称
        if (StringUtils.isNotEmpty(this.getGoodsName())) {
            bool.must(QueryBuilders.wildcardQuery("goodsName", "*" + this.getGoodsName() + "*"));
        }

        // 模糊查询 - 店铺ID
        if (StringUtils.isNotEmpty(this.getStoreId())) {
            bool.must(QueryBuilders.termQuery("storeId", this.getStoreId()));
        }

        // 是否精选，0：否，1：是
        if (Objects.nonNull(this.getSticky())) {
            bool.must(QueryBuilders.termQuery("sticky", this.getSticky()));
        }

        // 是否审核通过，0：待审核，1：审核通过，2：审核不通过
        if (Objects.nonNull(this.getAuditStatus())) {
            bool.must(QueryBuilders.termQuery("auditStatus", this.getAuditStatus().toValue()));
        }


        // 是否删除，0：否，1：是
        if (Objects.nonNull(this.getDelFlag())) {
            bool.must(QueryBuilders.termQuery("delFlag", this.getDelFlag().toValue()));
        }

        if (Objects.nonNull(this.getTabType())) {
            switch (this.getTabType()) {
                //进行中
                case STARTED:
                    bool.must(QueryBuilders.rangeQuery("startTime").to(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
                    bool.must(QueryBuilders.rangeQuery("endTime").from(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.CHECKED.toValue()));
                    break;
                //未生效
                case NOT_START:
                    bool.must(QueryBuilders.rangeQuery("startTime").from(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4),Boolean.FALSE));
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.CHECKED.toValue()));
                    break;
                //已结束
                case ENDED:
                    bool.must(QueryBuilders.rangeQuery("endTime").to(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4),Boolean.FALSE));
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.CHECKED.toValue()));
                    break;
                //待审核
                case WAIT_CHECK:
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.WAIT_CHECK.toValue()));
                    break;
                //审核失败
                case NOT_PASS:
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.NOT_PASS.toValue()));
                    break;
                case WILL_AND_ALREADY_START:
                    bool.must(QueryBuilders.rangeQuery("endTime").from(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
                    bool.must(QueryBuilders.termQuery("auditStatus", AuditStatus.CHECKED.toValue()));
                default:
                    break;
            }
        }

        // 批量查询-spuIDList
        if (CollectionUtils.isNotEmpty(this.getSpuIdList())) {
            bool.must(QueryBuilders.termsQuery("goodsId", this.getSpuIdList()));
        }
        return bool;
    }


}