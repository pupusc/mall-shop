package com.wanmi.sbc.crm.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.crm.bean.enums.PlanStatus;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p> 人群运营计划VO</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@Data
public class CustomerPlanVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@ApiModelProperty(value = "标识")
	private Long id;

	/**
	 * 计划名称
	 */
	@ApiModelProperty(value = "计划名称")
	private String planName;

	/**
	 * 触发条件标志 0:否1:是
	 */
	@ApiModelProperty(value = "触发条件标志 0:否1:是")
	private Boolean triggerFlag;

	/**
	 * 触发条件，以逗号分隔
	 */
	@ApiModelProperty(value = "触发条件")
    private List<TriggerCondition> triggerConditions;

	/**
	 * 计划开始时间
	 */
	@ApiModelProperty(value = "计划开始时间")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDate;

	/**
	 * 计划结束时间
	 */
	@ApiModelProperty(value = "计划结束时间")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDate;

	/**
	 * 目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）
	 */
	@ApiModelProperty(value = "目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）")
	private Boolean receiveType;

	/**
	 * 目标人群值 值以type_id结构组成  type代表类型  0:系统分群,1:自定义分群  id:对应分群数据id
	 */
	@ApiModelProperty(value = "目标人群值 值以type_id结构组成  type代表类型  0:系统分群,1:自定义分群  id:对应分群数据id")
	private String receiveValue;

	/**
	 * 目标人群名称
	 */
	@ApiModelProperty(value = "目标人群名称")
	private String receiveValueName;

	/**
	 * 是否送积分 0:否1:是
	 */
	@ApiModelProperty(value = "是否送积分 0:否1:是")
	private Boolean pointFlag;

	/**
	 * 赠送积分值
	 */
	@ApiModelProperty(value = "赠送积分值")
	private Integer points;

	/**
	 * 是否送优惠券 0:否1:是
	 */
	@ApiModelProperty(value = "是否送优惠券 0:否1:是")
	private Boolean couponFlag;

	/**
	 * 是否每人限发次数 0:否1:是
	 */
	@ApiModelProperty(value = "是否每人限发次数 0:否1:是")
	private Boolean customerLimitFlag;

	/**
	 * 每人限发次数值
	 */
	@ApiModelProperty(value = "每人限发次数值")
	private Integer customerLimit;

	/**
	 * 权益礼包总数
	 */
	@ApiModelProperty(value = "权益礼包总数")
	private Integer giftPackageTotal;

	/**
	 * 已发送礼包数
	 */
	@ApiModelProperty(value = "已发送礼包数")
	private Integer giftPackageCount;

	/**
	 * 短信标识 0:否1:是
	 */
	@ApiModelProperty(value = "短信标识 0:否1:是")
	private Boolean smsFlag;

	/**
	 * 站内信标识 0:否1:是
	 */
	@ApiModelProperty(value = "站内信标识 0:否1:是")
	private Boolean appPushFlag;

	/**
	 * 删除标志位，0:未删除，1:已删除
	 */
	@ApiModelProperty(value = "删除标志位，0:未删除，1:已删除")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 是否暂停 0:开启1:暂停
	 */
	@ApiModelProperty(value = "是否暂停 0:开启1:暂停")
	private Boolean pauseFlag;

	/**
	 * 运营计划与优惠券关联
	 */
	@ApiModelProperty(value = "运营计划与优惠券关联")
	private List<CustomerPlanCouponVO> customerPlanCouponVOList;

	/**
	 * 状态 0:未开始1:进行中2:暂停中3;已结束
	 */
	private PlanStatus planStatus;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private String activityId;

    /**
     * 总抵扣
     */
    @ApiModelProperty(value = "总抵扣")
    private BigDecimal couponDiscount;

    /**
     * 状态 0:未开始1:进行中2:暂停中3;已结束
     */
    @ApiModelProperty(value = "状态 0:未开始1:进行中2:暂停中3;已结束")
    public PlanStatus getPlanStatus(){
        if(endDate != null && startDate != null){
            LocalDate date = LocalDate.now();
            if(date.isAfter(endDate)){
                return PlanStatus.END;
            }
            if(date.isBefore(startDate)){
                return PlanStatus.NO_BEGIN;
            }
            if(Boolean.FALSE.equals(pauseFlag)){
                return PlanStatus.BEGIN;
            }
            return PlanStatus.STOP;
        }
        return null;
    }

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "赠送优惠券id列表")
    private List<String> couponIds;
}