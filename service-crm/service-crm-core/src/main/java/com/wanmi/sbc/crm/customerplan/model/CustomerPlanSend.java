package com.wanmi.sbc.crm.customerplan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * customer_plan_send
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPlanSend implements Serializable {
    private Long id;

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 积分发送标识（0-不需要发送，1-需要发送（未发送），2-已发送）
     */
    private Integer pointFlag;

    /**
     * 优惠券发送标识（0-不需要发送，1-需要发送（未发送），2-已发送）
     */
    private Integer couponFlag;

    /**
     * 短信发送标识（0-不需要发送，1-需要发送（未发送），2-已发送）
     */
    private Integer smsFlag;

    /**
     * app通知发送标识（0-不需要发送，1-需要发送（未发送），2-已发送）
     */
    private Integer appPushFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 统计时间
     */
    private String statDate;

    /**
     * 权益包剩余个数
     */
    private Integer giftPackageTotal;


    private List<Long> idList;

    private Integer groupType;

    private Long groupId;

    /**
     * 统计是否去重  0--容许重复 1--不容许重复
     */
    @Transient
    private int isRepeat;

    /**
     * 是否只统计优惠券发放 0--只统计优惠券 1--统计优惠券和积分
     */
    @Transient
    private int isOnlyCoupon;


}