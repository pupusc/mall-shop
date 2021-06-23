package com.wanmi.sbc.elastic.coupon.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Joiner;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 11:15 AM 2018/9/12
 * @Description: 优惠券活动表
 */
@Document(indexName = EsConstants.DOC_COUPON_ACTIVITY, type = EsConstants.DOC_COUPON_ACTIVITY)
@Data
public class EsCouponActivity {

    /**
     * 优惠券活动id
     */
    @Id
    private String activityId;

    /**
     * 优惠券活动名称
     */
    @Field(type = FieldType.Keyword)
    private String activityName;

    /**
     * 开始时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券， 4权益赠券
     */
    @Field(type = FieldType.Integer)
    private CouponActivityType couponActivityType;

    /**
     * 领取类型，0 每人限领次数不限，1 每人限领N次
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag receiveType;

    /**
     * 是否暂停 ，1 暂停
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag pauseFlag;

    /**
     * 优惠券被使用后可再次领取的次数，每次仅限领取1张
     */
    @Field(type = FieldType.Integer)
    private Integer receiveCount;

    /**
     * 生效终端，逗号分隔 0全部,1.PC,2.移动端,3.APP
     */
    @Field(type = FieldType.Keyword)
    private String terminals;

    /**
     * 商户id
     */
    @Field(type = FieldType.Long)
    private Long storeId;

    /**
     * 是否平台 0店铺 1平台
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag platformFlag;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Field(type = FieldType.Integer)
    private DeleteFlag delFlag;

    /**
     * 关联的客户等级  -2指定客户 -1:全部客户 0:全部等级 other:其他等级 ,
     */
    @Field(type = FieldType.Keyword)
    private List<String> joinLevels;


    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag joinLevelType;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Transient
    private String joinLevel;

    public String getJoinLevel() {
        return CollectionUtils.isNotEmpty(joinLevels) ? Joiner.on(",").skipNulls().join(joinLevels) : "";
    }

}
