package com.wanmi.sbc.crm.customerplan.model;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanCouponVO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * customer_plan
 * @author 
 */
@Data
public class CustomerPlan implements Serializable {
    /**
     * 标识
     */
    private Long id;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 触发条件标志 0:否1:是
     */
    private Boolean triggerFlag;

    /**
     * 触发条件，以逗号分隔
     */
    private String triggerCondition;

    /**
     * 计划开始时间
     */
    private Date startDate;

    /**
     * 计划结束时间
     */
    private Date endDate;

    /**
     * 目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）
     */
    private Integer receiveType;

    /**
     * 目标人群值 值以type_id结构组成  type代表类型  0:系统分群,1:自定义分群  id:对应分群数据id
     */
    private String receiveValue;

    /**
     * 是否送积分 0:否1:是
     */
    private Boolean pointFlag;

    /**
     * 赠送积分值
     */
    private Integer points;

    /**
     * 是否送优惠券 0:否1:是
     */
    private Boolean couponFlag;

    /**
     * 是否每人限发次数 0:否1:是
     */
    private Boolean customerLimitFlag;

    /**
     * 每人限发次数值
     */
    private Integer customerLimit;

    /**
     * 权益礼包总数
     */
    private Integer giftPackageTotal;

    /**
     * 已发送礼包数
     */
    private Integer giftPackageCount;

    /**
     * 短信标识 0:否1:是
     */
    private Boolean smsFlag;

    /**
     * 站内信标识 0:否1:是
     */
    private Boolean appPushFlag;

    /**
     * 删除标志位，0:未删除，1:已删除
     */
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 是否暂停 0:开启1:暂停
     */
    private Boolean pauseFlag;

    /**
     * 目标人群名称
     */
    private String receiveValueName;

    /**
     * 运营计划与优惠券关联
     */
    private List<CustomerPlanCouponVO> customerPlanCouponVOList;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 总抵扣
     */
    private BigDecimal couponDiscount;

    private static final long serialVersionUID = 1L;

}