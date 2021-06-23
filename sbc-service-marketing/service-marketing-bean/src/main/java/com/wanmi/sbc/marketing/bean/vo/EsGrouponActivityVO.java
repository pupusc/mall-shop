package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author houshuai
 * @date 2020/12/12 18:25
 * @description <p> </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EsGrouponActivityVO implements Serializable {

    private static final long serialVersionUID = -5105981525104704326L;
    /**
     * 活动ID
     */
    private String grouponActivityId;

    /**
     * 拼团人数
     */
    private Integer grouponNum;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 拼团分类ID
     */
    private String grouponCateId;

    /**
     * 是否自动成团
     */
    private boolean autoGroupon;

    /**
     * 是否包邮
     */
    private boolean freeDelivery;

    /**
     * spu编号
     */
    private String goodsId;

    /**
     * spu编码
     */
    private String goodsNo;

    /**
     * spu商品名称
     */
    private String goodsName;

    /**
     * 店铺ID
     */
    private String storeId;

    /**
     * 是否精选
     */
    private boolean sticky;

    /**
     * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
     */
    private AuditStatus auditStatus;

    /**
     * 审核不通过原因
     */
    private String auditFailReason;

    /**
     * 已成团人数
     */
    private Integer alreadyGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 待成团人数
     */
    private Integer waitGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 团失败人数
     */
    private Integer failGrouponNum = NumberUtils.INTEGER_ZERO;

    /**
     * 是否删除，0：否，1：是
     */
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
