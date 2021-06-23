package com.wanmi.sbc.customer.distribution.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>分销员实体类</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@Data
@Entity
@Table(name = "distribution_customer")
public class DistributionCustomer implements Serializable {

    private static final long serialVersionUID = -4493242521160954457L;
    /**
     * 分销员标识UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "distribution_id")
    private String distributionId;

    /**
     * 会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 会员名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 会员登录账号|手机号
     */
    @Column(name = "customer_account")
    private String customerAccount;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人(后台新增分销员)
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 是否删除标志 0:否，1:是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 是否禁止分销 0:启用  1:禁用
     */
    @Column(name = "forbidden_flag")
    @Enumerated
    private DefaultFlag forbiddenFlag = DefaultFlag.NO;

    /**
     * 禁用原因
     */
    @Column(name = "forbidden_reason")
    private String forbiddenReason;

    /**
     * 是否有分销员资格0:否，1:是
     */
    @Column(name = "distributor_flag")
    @Enumerated
    private DefaultFlag distributorFlag = DefaultFlag.NO;

    /**
     * 邀新人数
     */
    @Column(name = "invite_count")
    private Integer inviteCount = 0;

    /**
     * 有效邀新人数
     */
    @Column(name = "invite_available_count")
    private Integer inviteAvailableCount = 0;

    /**
     * 邀新奖金(元)
     */
    @Column(name = "reward_cash")
    private BigDecimal rewardCash = BigDecimal.ZERO;

    /**
     * 未入账邀新奖金(元)
     */
    @Column(name = "reward_cash_not_recorded")
    private BigDecimal rewardCashNotRecorded = BigDecimal.ZERO;

    /**
     * 分销订单(笔)
     */
    @Column(name = "distribution_trade_count")
    private Integer distributionTradeCount = 0;

    /**
     * 销售额(元)
     */
    @Column(name = "sales")
    private BigDecimal sales = BigDecimal.ZERO;

    /**
     * 分销佣金(元)
     */
    @Column(name = "commission")
    private BigDecimal commission = BigDecimal.ZERO;

    /**
     * 佣金总额(元)
     */
    @Column(name = "commission_total")
    private BigDecimal commissionTotal = BigDecimal.ZERO;

    /**
     * 未入账分销佣金(元)
     */
    @Column(name = "commission_not_recorded")
    private BigDecimal commissionNotRecorded = BigDecimal.ZERO;

    /**
     * 分销员等级ID
     */
    @Column(name = "distributor_level_id")
    private String distributorLevelId;

    /**
     * 邀请码
     */
    @Column(name = "invite_code")
    private String inviteCode;

    /**
     * 邀请人会员ID集合，后期可扩展N级
     */
    @Column(name = "invite_customer_ids")
    private String inviteCustomerIds;
}