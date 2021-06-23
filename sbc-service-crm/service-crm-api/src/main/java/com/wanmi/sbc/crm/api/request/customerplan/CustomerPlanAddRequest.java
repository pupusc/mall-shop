package com.wanmi.sbc.crm.api.request.customerplan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import com.wanmi.sbc.crm.bean.dto.CustomerPlanAppPushDTO;
import com.wanmi.sbc.crm.bean.dto.CustomerPlanCouponDTO;
import com.wanmi.sbc.crm.bean.dto.CustomerPlanSmsDTO;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * <p> 人群运营计划新增参数</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanAddRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 计划名称
	 */
	@ApiModelProperty(value = "计划名称")
	@NotBlank
	@Length(max=20)
	private String planName;

	/**
	 * 触发条件标志 0:否1:是
	 */
	@ApiModelProperty(value = "触发条件标志 0:否1:是")
	@NotNull
	private Boolean triggerFlag;

	/**
	 * 触发条件
	 */
	@ApiModelProperty(value = "触发条件")
	private List<TriggerCondition> triggerConditions;

	/**
	 * 计划开始时间
	 */
	@ApiModelProperty(value = "计划开始时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDate;

	/**
	 * 计划结束时间
	 */
	@ApiModelProperty(value = "计划结束时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDate;

	/**
	 * 目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）
	 */
	@ApiModelProperty(value = "目标人群类型（0-全部，1-会员等级，2-会员人群，3-自定义）")
	@NotNull
	private Integer receiveType;

	/**
	 * 目标人群值
	 */
	@ApiModelProperty(value = "目标人群值")
	private String receiveValue;

	/**
	 * 是否送积分 0:否1:是
	 */
	@ApiModelProperty(value = "是否送积分 0:否1:是")
	@NotNull
	private Boolean pointFlag;

	/**
	 * 赠送积分值
	 */
	@ApiModelProperty(value = "赠送积分值")
	@Max(9999999999L)
    @Min(1L)
	private Integer points;

	/**
	 * 是否送优惠券 0:否1:是
	 */
	@ApiModelProperty(value = "是否送优惠券 0:否1:是")
	@NotNull
	private Boolean couponFlag;

	/**
	 * 是否每人限发次数 0:否1:是
	 */
	@ApiModelProperty(value = "是否每人限发次数 0:否1:是")
	@NotNull
	private Boolean customerLimitFlag;

	/**
	 * 每人限发次数值
	 */
	@ApiModelProperty(value = "每人限发次数值")
	@Max(9999999999L)
    @Min(1L)
	private Integer customerLimit;

	/**
	 * 权益礼包总数
	 */
	@ApiModelProperty(value = "权益礼包总数")
	@NotNull
	@Max(9999999999L)
    @Min(1L)
	private Integer giftPackageTotal;

	/**
	 * 短信标识 0:否1:是
	 */
	@ApiModelProperty(value = "短信标识 0:否1:是")
	@NotNull
	private Boolean smsFlag;

	/**
	 * 站内信标识 0:否1:是
	 */
	@ApiModelProperty(value = "站内信标识 0:否1:是")
	@NotNull
	private Boolean appPushFlag;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id", hidden = true)
    private String activityId;

    /**
     * 总抵扣
     */
    @ApiModelProperty(value = "总抵扣")
    private BigDecimal couponDiscount;

    /**
     * 优惠券列表
     */
    @ApiModelProperty(value = "优惠券列表")
    @Size(max = 10)
    private List<CustomerPlanCouponDTO> planCouponList;

    /**
     * 短信信息
     */
    @ApiModelProperty(value = "短信信息")
    private CustomerPlanSmsDTO planSms;

    /**
     * App通知信息
     */
    @ApiModelProperty(value = "App通知信息")
    private CustomerPlanAppPushDTO planAppPush;

    @Override
    public void checkParam() {
        if(triggerFlag && CollectionUtils.isEmpty(triggerConditions)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if(pointFlag && Objects.isNull(points)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if(customerLimitFlag && Objects.isNull(customerLimit)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if(couponFlag && (CollectionUtils.isEmpty(planCouponList) || Objects.isNull(couponDiscount))){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if(smsFlag && (Objects.isNull(planSms) || Objects.isNull(planSms.getSignId()) || Objects.isNull(planSms.getTemplateCode()))){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if(appPushFlag && (Objects.isNull(planAppPush)
                || StringUtils.isBlank(planAppPush.getName())
                || StringUtils.isBlank(planAppPush.getNoticeTitle())
                || StringUtils.isBlank(planAppPush.getNoticeContext()))){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}