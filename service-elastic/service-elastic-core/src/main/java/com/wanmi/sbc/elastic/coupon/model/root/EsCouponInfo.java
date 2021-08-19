package com.wanmi.sbc.elastic.coupon.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券ES文档
 */
@Document(indexName = EsConstants.DOC_COUPON_INFO_TYPE, type = EsConstants.DOC_COUPON_INFO_TYPE)
@Data
public class EsCouponInfo implements Serializable {

    private static final long serialVersionUID = 8270069437627689630L;

    /**
     * 优惠券主键Id
     */
    @Id
    private String couponId;

    /**
     * 优惠券名称
     */
    @Field(type = FieldType.Keyword)
    private String couponName;

    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    @Field(type = FieldType.Integer)
    private RangeDayType rangeDayType;

    /**
     * 优惠券开始时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 优惠券结束时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 有效天数
     */
    @Field(index = false , type = FieldType.Integer)
    private Integer effectiveDays;

    /**
     * 购满多少钱
     */
    @Field(index = false, type = FieldType.Double)
    private BigDecimal fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    @Field(index = false,type = FieldType.Integer)
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    @Field(index = false, type = FieldType.Double)
    private BigDecimal denomination;

    /**
     * 商户id
     */
    @Field(type = FieldType.Long)
    private Long storeId;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @Field(type = FieldType.Integer)
    private DefaultFlag platformFlag;

    /**
     * 营销范围类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @Field(type = FieldType.Integer)
    private ScopeType scopeType;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @Field(type = FieldType.Integer)
    private CouponType couponType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Field(type = FieldType.Integer)
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


    /**
     *  关联分类id集合
     */
    @Field(type = FieldType.Keyword)
    private List<String> cateIds;

    /**
     * 促销范围Ids
     */
    @Field(type = FieldType.Keyword)
    private List<String> scopeIds = new ArrayList<>();


}
