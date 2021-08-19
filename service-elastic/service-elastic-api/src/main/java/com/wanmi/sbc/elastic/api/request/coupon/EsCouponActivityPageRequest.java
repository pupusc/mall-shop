package com.wanmi.sbc.elastic.api.request.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.elastic.api.request.base.EsBaseQueryRequest;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.MarketingStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class EsCouponActivityPageRequest extends EsBaseQueryRequest {

    private static final long serialVersionUID = 4243718077145628609L;


    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     *优惠券活动名称
     */
    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;


    /**
     * 优惠券活动类型
     */
    @ApiModelProperty(value = "优惠券活动类型")
    private CouponActivityType couponActivityType;

    /**
     *开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     *结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     *目标客户 -1:全部客户 0:全部等级 other:其他等级 ,
     */
    @ApiModelProperty(value = "关联的客户等级", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    private String joinLevel;

    @ApiModelProperty(value = "查询类型")
    private MarketingStatus queryTab;

    /**
     * 优惠券活动列表查询条件封装
     * @return
     */
    public BoolQueryBuilder getBoolQueryBuilder() {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        if (Objects.nonNull(storeId)){
            bool.must(termQuery("storeId", storeId));
            bool.must(termQuery("platformFlag", 0));
        }else{
            bool.must(termQuery("platformFlag", 1));
        }

        if (StringUtils.isNotEmpty(activityName)) {
            bool.must(QueryBuilders.wildcardQuery("activityName",  StringUtil.ES_LIKE_CHAR.concat(XssUtils.replaceEsLikeWildcard(activityName.trim())).concat(StringUtil.ES_LIKE_CHAR)));
        }

        //活动类型筛选
        if (CouponActivityType.ALL_COUPONS.equals(couponActivityType)
                || CouponActivityType.SPECIFY_COUPON.equals(couponActivityType)
                || CouponActivityType.STORE_COUPONS.equals(couponActivityType)
                || CouponActivityType.REGISTERED_COUPON.equals(couponActivityType)
                || CouponActivityType.RIGHTS_COUPON.equals(couponActivityType)
                || CouponActivityType.DISTRIBUTE_COUPON.equals(couponActivityType)
                || CouponActivityType.POINTS_COUPON.equals(couponActivityType)
                || CouponActivityType.ENTERPRISE_REGISTERED_COUPON.equals(couponActivityType)) {
            bool.must(termQuery("couponActivityType", couponActivityType.toValue()));
        }


        if (Objects.nonNull(startTime)) {
            bool.must(QueryBuilders.rangeQuery("startTime").gte(DateUtil.format(startTime, DateUtil.FMT_TIME_4)));
        }

        if (Objects.nonNull(endTime)) {
            bool.must(QueryBuilders.rangeQuery("endTime").lte(DateUtil.format(endTime, DateUtil.FMT_TIME_4)));
        }

        if (Objects.nonNull(queryTab)) {
            BoolQueryBuilder b1 = QueryBuilders.boolQuery();
            BoolQueryBuilder b2 = QueryBuilders.boolQuery();
            BoolQueryBuilder b3 = QueryBuilders.boolQuery();
            BoolQueryBuilder b4 = QueryBuilders.boolQuery();
            BoolQueryBuilder b5 = QueryBuilders.boolQuery();
            switch (queryTab) {
                case STARTED://进行中
                    b1.must(QueryBuilders.rangeQuery("startTime").lt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b2.must(QueryBuilders.rangeQuery("endTime").gte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b3.must(b1).must(b2);

                    b4.must(QueryBuilders.termQuery("couponActivityType", CouponActivityType.RIGHTS_COUPON.toValue()));

                    b5.should(b3).should(b4);

                    bool.must(b5);

                    bool.must(QueryBuilders.termQuery("pauseFlag",0));

                    bool.mustNot(QueryBuilders.termQuery("couponActivityType",1));

                    break;
                case PAUSED://暂停中

                    b1.must(QueryBuilders.rangeQuery("startTime").lte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b2.must(QueryBuilders.rangeQuery("endTime").gte(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));

                    b3.must(b1).must(b2);

                    b4.must(QueryBuilders.termQuery("couponActivityType", CouponActivityType.RIGHTS_COUPON.toValue()));

                    b5.should(b3).should(b4);

                    bool.must(b5);

                    bool.must(QueryBuilders.termQuery("pauseFlag",1));

                    break;
                case NOT_START://未开始
                    bool.must(QueryBuilders.rangeQuery("startTime").gt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
                    break;
                case ENDED://已结束
                    bool.must(QueryBuilders.rangeQuery("endTime").lt(DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4)));
                    break;
                default:
                    break;
            }
        }

        if (StringUtils.isNotBlank(joinLevel)) {
            bool.must(QueryBuilders.termsQuery("joinLevels",joinLevel));
        }

        return bool;

    }

    public SearchQuery getSearchQuery() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_COUPON_ACTIVITY);
        builder.withQuery(getBoolQueryBuilder());
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        return builder.build();
    }

}
