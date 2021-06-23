package com.wanmi.sbc.elastic.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.elastic.api.request.base.EsBaseQueryRequest;
import com.wanmi.sbc.marketing.bean.enums.CouponStatus;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * 商品库查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCouponInfoPageRequest extends EsBaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -4485444157498437822L;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 模糊条件-优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称模糊条件查询")
    private String likeCouponName;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "营销范围类型")
    private ScopeType scopeType;


    @ApiModelProperty(value = "查询类型")
    private CouponStatus couponStatus;

    /**
     * 是否只查询有效的优惠券（优惠券活动选择有效的优惠券）
     */
    @ApiModelProperty(value = "是否只查询有效的优惠券")
    private DefaultFlag isMarketingChose;

    /**
     * 查询开始时间，精确到天
     */
    @ApiModelProperty(value = "查询开始时间，精确到天")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 查询结束时间，精确到天
     */
    @ApiModelProperty(value = "查询结束时间，精确到天")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;


    @ApiModelProperty(value = "是否已删除")
    private DeleteFlag delFlag;

    /**
     * 批量优惠券id
     */
    @ApiModelProperty(value = "优惠券id列表")
    private List<String> couponIds;

    /**
     * 优惠券列表查询条件封装
     * @return
     */
    public BoolQueryBuilder getBoolQueryBuilder() {


        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        //店铺ID
        if (Objects.nonNull(storeId)) {
            bool.must(termQuery("storeId", storeId));
        }

        if (Objects.equals(DefaultFlag.YES, isMarketingChose)) {
            BoolQueryBuilder orBq = QueryBuilders.boolQuery();

            BoolQueryBuilder b1 = QueryBuilders.boolQuery();
            b1.mustNot(QueryBuilders.rangeQuery("endTime").lt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

            BoolQueryBuilder b2 = QueryBuilders.boolQuery();
            b2.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.DAYS.toValue()));

            orBq.should(b1);
            orBq.should(b2);

            bool.must(orBq);
        }

        if (Objects.nonNull(startTime)) {

            BoolQueryBuilder orBq = QueryBuilders.boolQuery();

            BoolQueryBuilder b1 = QueryBuilders.boolQuery();
            b1.must(QueryBuilders.rangeQuery("startTime").gte(DateUtil.format(startTime, DateUtil.FMT_TIME_4)));

            BoolQueryBuilder b2 = QueryBuilders.boolQuery();
            b2.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.RANGE_DAY.toValue()));

            orBq.must(b1);
            orBq.must(b2);

            bool.must(orBq);
        }

        if (Objects.nonNull(this.getEndTime())) {
            BoolQueryBuilder orBq = QueryBuilders.boolQuery();

            BoolQueryBuilder b1 = QueryBuilders.boolQuery();
            b1.must(QueryBuilders.rangeQuery("endTime").lte(DateUtil.format(this.getEndTime(), DateUtil.FMT_TIME_4)));

            BoolQueryBuilder b2 = QueryBuilders.boolQuery();
            b2.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.RANGE_DAY.toValue()));

            orBq.must(b1);
            orBq.must(b2);

            bool.must(orBq);

        }


        //是否平台优惠券 1平台 0店铺
        if (Objects.nonNull(this.getPlatformFlag())) {
            bool.must(termQuery("platformFlag", this.getPlatformFlag().toValue()));
        }


        //使用范围
        if (Objects.nonNull(this.getScopeType())) {
            bool.must(termQuery("scopeType", this.getScopeType().toValue()));
        }

        //模糊查询名称
        if (StringUtils.isNotEmpty(this.getLikeCouponName())) {
            bool.must(QueryBuilders.wildcardQuery("couponName",  StringUtil.ES_LIKE_CHAR.concat(XssUtils.replaceEsLikeWildcard(this.getLikeCouponName().trim())).concat(StringUtil.ES_LIKE_CHAR)));
        }

        //删除标记
        if (Objects.nonNull(this.getDelFlag())) {
            bool.must(termQuery("delFlag", this.getDelFlag().toValue()));
        }

        if (Objects.nonNull(this.getCouponStatus())) {
            BoolQueryBuilder b1 = QueryBuilders.boolQuery();
            BoolQueryBuilder b2 = QueryBuilders.boolQuery();
            BoolQueryBuilder b3 = QueryBuilders.boolQuery();
            switch (this.getCouponStatus()) {
                case STARTED://进行中

                    b1.must(QueryBuilders.rangeQuery("startTime").lt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b1.must(QueryBuilders.rangeQuery("endTime").gte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b3.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.RANGE_DAY.toValue()));

                    bool.must(b1).must(b2).must(b3);
                    break;
                case NOT_START://未生效
                    b1.must(QueryBuilders.rangeQuery("startTime").gte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b2.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.RANGE_DAY.toValue()));

                    bool.must(b1).must(b2);

                    break;
                case DAYS://领取生效

                    b1.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.DAYS.toValue()));

                    bool.must(b1);
                    break;
                case ENDED://已结束
                    b1.must(QueryBuilders.rangeQuery("endTime").lt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b2.must(QueryBuilders.termQuery("rangeDayType", RangeDayType.RANGE_DAY.toValue()));

                    bool.must(b1).must(b2);

                    break;
                default:
                    break;
            }
        }

        return bool;
    }

    public SearchQuery getSearchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_COUPON_INFO_TYPE);
        builder.withQuery(getBoolQueryBuilder());
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        return builder.build();
    }

}
