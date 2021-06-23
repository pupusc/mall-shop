package com.wanmi.sbc.marketing.distribution.model;

import java.math.BigDecimal;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.enums.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

/**
 * <p>分销设置实体类</p>
 *
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@Data
@Entity
@Table(name = "distribution_setting")
public class DistributionSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销设置主键Id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "setting_id")
    private String settingId;

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @Column(name = "open_flag")
    private DefaultFlag openFlag;

    /**
     * 分销员名称
     */
    @Column(name = "distributor_name")
    private String distributorName;

    /**
     * 是否开启分销小店 0：关闭，1：开启
     */
    @Column(name = "shop_open_flag")
    private DefaultFlag shopOpenFlag;

    /**
     * 小店名称
     */
    @Column(name = "shop_name")
    private String shopName;

    /**
     * 店铺分享图片
     */
    @Column(name = "shop_share_img")
    private String shopShareImg;

    /**
     * 注册限制
     */
    @Column(name = "register_limit_type")
    private RegisterLimitType registerLimitType;

    /**
     * 基础邀新奖励限制 0：不限，1：仅限有效邀新
     */
    @Column(name = "base_limit_type")
    private DistributionLimitType baseLimitType;

    /**
     * 佣金返利优先级
     */
    @Column(name = "commission_priority_type")
    private CommissionPriorityType commissionPriorityType;

    /**
     * 是否开启申请入口 0：关闭，1：开启
     */
    @Column(name = "apply_flag")
    private DefaultFlag applyFlag;

    /**
     * 申请条件 0：购买商品，1：邀请注册
     */
    @Column(name = "apply_type")
    private RecruitApplyType applyType;

    /**
     * 购买商品时招募入口海报
     */
    @Column(name = "buy_recruit_enter_img")
    private String buyRecruitEnterImg;

    /**
     * 邀请注册时招募入口海报
     */
    @Column(name="invite_recruit_enter_img")
    private String inviteRecruitEnterImg;

    /**
     * 邀请注册时招募落地页海报
     */
    @Column(name="invite_recruit_img")
    private String inviteRecruitImg;

    /**
     * 购买商品时招募落地页海报
     */
    @Column(name = "recruit_img")
    private String recruitImg;

    /**
     * 邀新入口海报
     */
    @Column(name="invite_enter_img")
    private String inviteEnterImg;

    /**
     * 招募邀新转发图片
     */
    @Column(name = "recruit_share_img")
    private String recruitShareImg;

    /**
     * 招募规则说明
     */
    @Column(name = "recruit_desc")
    private String recruitDesc;

    /**
     * 邀请人数
     */
    @Column(name = "invite_count")
    private Integer inviteCount;

    /**
     * 限制条件 0：不限，1：仅限有效邀新
     */
    @Column(name = "limit_type")
    private DistributionLimitType limitType;

    /**
     * 是否开启分销佣金 0：关闭，1：开启
     */
    @Column(name = "commission_flag")
    private DefaultFlag commissionFlag;

    /**
     * 是否开启分销商品审核 0：关闭，1：开启
     */
    @Column(name = "goods_audit_flag")
    private DefaultFlag goodsAuditFlag;

    /**
     * 是否开启邀新奖励 0：关闭，1：开启
     */
    @Column(name = "invite_flag")
    private DefaultFlag inviteFlag;

    /**
     * 邀新专题页海报
     */
    @Column(name = "invite_img")
    private String inviteImg;

    /**
     * 邀新转发图片
     */
    @Column(name = "invite_share_img")
    private String inviteShareImg;

    /**
     * 邀新奖励规则说明
     */
    @Column(name = "invite_desc")
    private String inviteDesc;

    /**
     * 邀新奖励限制 0：不限，1：仅限有效邀新
     */
    @Column(name = "reward_limit_type")
    private DistributionLimitType rewardLimitType;

    /**
     * 是否开启奖励现金 0：关闭，1：开启
     */
    @Column(name = "reward_cash_flag")
    private DefaultFlag rewardCashFlag;

    /**
     * 奖励上限类型 0：不限， 1：限人数
     */
    @Column(name = "reward_cash_type")
    private RewardCashType rewardCashType;

    /**
     * 奖励现金上限(人数)
     */
    @Column(name = "reward_cash_count")
    private Integer rewardCashCount;

    /**
     * 每位奖励金额
     */
    @Column(name = "reward_cash")
    private BigDecimal rewardCash;

    /**
     * 是否开启奖励优惠券 0：关闭，1：开启
     */
    @Column(name = "reward_coupon_flag")
    private DefaultFlag rewardCouponFlag;

    /**
     * 奖励优惠券上限(组数)
     */
    @Column(name = "reward_coupon_count")
    private Integer rewardCouponCount;

    /**
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 分销业绩规则说明
     */
    @Column(name = "performance_desc")
    private String performanceDesc;

    /**
     * 佣金提成脱钩
     */
    @Column(name = "commission_unhook_type")
    private CommissionUnhookType commissionUnhookType;

    /**
     * 分销员等级规则
     */
    @Column(name = "distributor_level_desc")
    private String distributorLevelDesc;

}