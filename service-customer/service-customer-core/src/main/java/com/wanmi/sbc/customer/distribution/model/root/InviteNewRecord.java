package com.wanmi.sbc.customer.distribution.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.enums.RewardFlag;
import com.wanmi.sbc.customer.bean.enums.FailReasonFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by feitingting on 2019/2/21.
 */
@Data
@Entity
@Table(name = "invite_new_record")
public class InviteNewRecord implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "record_id")
    private String recordId;

    /**
     * 受邀人ID
     */
    @ApiModelProperty(value = "受邀人ID")
    @Column(name = "invited_customer_id")
    private String  invitedNewCustomerId;

    /**
     * 邀请人ID
     */
    @Column(name = "request_customer_id")
    private String  requestCustomerId;


    /**
     * 是否有效邀新 0：否 1：是
     */
    @Column(name="available_distribution")
    @Enumerated
    private InvalidFlag availableDistribution = InvalidFlag.NO;

    /**
     * 是否是分销员 0: 否  1：是
     */
    @ApiModelProperty(value = "是否是分销员", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    @Column(name="distributor")
    private Integer distributor = 0;

    /**
     * 注册时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name="register_time")
    private LocalDateTime registerTime;

    /**
     * 首次下单时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name="first_order_time")
    private LocalDateTime firstOrderTime;

    /**
     * 订单编号
     */
    @Column(name="order_code")
    private String orderCode;

    /**
     * 订单完成时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name="order_complete_time")
    private LocalDateTime orderFinishTime;

    /**
     * 奖励是否入账 0:否 1:是
     */
    @Column(name="reward_recorded")
    @Enumerated
    private InvalidFlag rewardRecorded = InvalidFlag.NO;

    /**
     * 奖励入账时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name="reward_cash_recorded_time")
    private LocalDateTime rewardRecordedTime;

    /**
     * 奖励金额
     */
    @Column(name="reward_cash")
    private BigDecimal rewardCash = BigDecimal.ZERO;

    /**
     * 后台配置的奖励优惠券id，多个以逗号分隔
     */
    @Column(name="setting_coupons")
    private String settingCoupons;

    /**
     * 后台配置的奖励金额
     */
    @Column(name="setting_amount")
    private BigDecimal settingAmount = BigDecimal.ZERO;

    /**
     * 未入账原因(0:非有效邀新，1：奖励达到上限，2：奖励未开启)
     */
    @Column(name="fail_reason_flag")
    private FailReasonFlag failReasonFlag;

    /**
     * 入账类型（0：现金 1：优惠券）
     */
    @Column(name="reward_flag")
    @Enumerated
    private RewardFlag rewardFlag ;

    /**
     * 入账优惠券
     */
    @Column(name="reward_coupon")
    private String  rewardCoupon ;

    /**
     * 优惠券组集合信息（id：count）
     */
    @Column(name="setting_coupon_ids_counts")
    private String settingCouponIdsCounts;


    /**
     * 终端
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;


}
