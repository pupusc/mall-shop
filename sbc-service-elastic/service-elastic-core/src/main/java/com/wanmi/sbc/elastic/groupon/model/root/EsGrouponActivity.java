package com.wanmi.sbc.elastic.groupon.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表实体类</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@Data
@Document(indexName = EsConstants.GROUPON_ACTIVITY, type = EsConstants.GROUPON_ACTIVITY)
public class EsGrouponActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @Id
    private String grouponActivityId;

    /**
     * 拼团人数
     */
    @Field(type = FieldType.Keyword)
    private Integer grouponNum;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime endTime;

    /**
     * 拼团分类ID
     */
    @Field(type = FieldType.Keyword)
    private String grouponCateId;

    /**
     * 是否自动成团
     */
    @Field(type = FieldType.Keyword)
    private boolean autoGroupon;

    /**
     * 是否包邮
     */
    @Field(type = FieldType.Keyword)
    private boolean freeDelivery;

    /**
     * spu编号
     */
    @Field(type = FieldType.Keyword)
    private String goodsId;

    /**
     * spu编码
     */
    @Field(type = FieldType.Keyword)
    private String goodsNo;

    /**
     * spu商品名称
     */
    @Field(type = FieldType.Keyword)
    private String goodsName;

    /**
     * 店铺ID
     */
    @Field(type = FieldType.Keyword)
    private String storeId;

    /**
     * 是否精选
     */
    @Field(type = FieldType.Keyword)
    private boolean sticky;

    /**
     * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
     */
    @Enumerated
    @Field(type = FieldType.Keyword)
    private AuditStatus auditStatus;

    /**
     * 审核不通过原因
     */
    @Field(type = FieldType.Keyword)
    private String auditFailReason;

    /**
     * 已成团人数
     */
    @Field(type = FieldType.Keyword)
    private Integer alreadyGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 待成团人数
     */
    @Field(type = FieldType.Keyword)
    private Integer waitGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 团失败人数
     */
    @Field(type = FieldType.Keyword)
    private Integer failGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 是否删除，0：否，1：是
     */
    @Field(type = FieldType.Keyword)
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime updateTime;

}