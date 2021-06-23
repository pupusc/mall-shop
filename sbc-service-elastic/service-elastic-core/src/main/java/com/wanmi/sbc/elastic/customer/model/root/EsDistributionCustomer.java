package com.wanmi.sbc.elastic.customer.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Convert;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = EsConstants.DISTRIBUTION_CUSTOMER, type = EsConstants.DISTRIBUTION_CUSTOMER)
public class EsDistributionCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 分销员标识UUID
     */
    @Id
    private String distributionId;

    /**
     * 会员ID
     */

    @Field(type = FieldType.Keyword)
    private String customerId;

    /**
     * 会员名称
     */

    @Field(type = FieldType.Keyword)
    private String customerName;

    /**
     * 会员登录账号|手机号
     */

    @Field(type = FieldType.Keyword)
    private String customerAccount;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
    private LocalDateTime createTime;

    /**
     * 创建人(后台新增分销员)
     */
    @Field(type = FieldType.Keyword)
    private String createPerson;

    /**
     * 是否删除标志 0:否，1:是
     */
    @Enumerated
    @Field(type = FieldType.Keyword)
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 是否禁止分销 0:启用  1:禁用
     */
    @Enumerated
    @Field(type = FieldType.Keyword)
    private DefaultFlag forbiddenFlag = DefaultFlag.NO;

    /**
     * 禁用原因
     */
    @Field(type = FieldType.Keyword)
    private String forbiddenReason;

    /**
     * 是否有分销员资格0:否，1:是
     */
    @Enumerated
    @Field(type = FieldType.Keyword)
    private DefaultFlag distributorFlag = DefaultFlag.NO;

    /**
     * 邀新人数
     */
    @Field(type = FieldType.Keyword)
    private Integer inviteCount = 0;


    /**
     * 有效邀新人数
     */
    @Field(type = FieldType.Keyword)
    private Integer inviteAvailableCount = 0;

    /**
     * 邀新奖金(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal rewardCash = BigDecimal.ZERO;

    /**
     * 未入账邀新奖金(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal rewardCashNotRecorded = BigDecimal.ZERO;

    /**
     * 分销订单(笔)
     */
    @Field(type = FieldType.Keyword)
    private Integer distributionTradeCount = 0;

    /**
     * 销售额(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal sales = BigDecimal.ZERO;

    /**
     * 分销佣金(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal commission = BigDecimal.ZERO;

    /**
     * 佣金总额(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal commissionTotal = BigDecimal.ZERO;

    /**
     * 未入账分销佣金(元)
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal commissionNotRecorded = BigDecimal.ZERO;

    /**
     * 分销员等级ID
     */
    @Field(type = FieldType.Keyword)
    private String distributorLevelId;

    /**
     * 邀请码
     */
    @Field(type = FieldType.Keyword)
    private String inviteCode;

    /**
     * 邀请人会员ID集合，后期可扩展N级
     */
    @Field(type = FieldType.Keyword)
    private String inviteCustomerIds;
}