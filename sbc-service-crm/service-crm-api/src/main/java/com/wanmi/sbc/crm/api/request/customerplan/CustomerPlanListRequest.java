package com.wanmi.sbc.crm.api.request.customerplan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.crm.bean.enums.PlanStatus;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p> 人群运营计划分页查询请求参数</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanListRequest implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 计划id
     */
    @ApiModelProperty(value = "计划id")
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
	private Integer triggerFlag;

	/**
	 * 触发条件
	 */
	@ApiModelProperty(value = "触发条件")
	private TriggerCondition triggerCondition;

	/**
	 * 搜索条件:计划开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:计划开始时间开始")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDate;

	/**
	 * 搜索条件:计划结束时间结束
	 */
	@ApiModelProperty(value = "搜索条件:计划结束时间结束")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDate;

	/**
	 * 目标人群值
	 */
	@ApiModelProperty(value = "目标人群值")
	private String receiveValue;


	/**
	 * 删除标志位，0:未删除，1:已删除
	 */
	@ApiModelProperty(value = "删除标志位，0:未删除，1:已删除", hidden = true)
	private DeleteFlag delFlag;


	/**
	 * 0:未开始,1:进行中,2:暂停中,3;已结束
	 */
	@ApiModelProperty(value = "状态 0:未开始,1:进行中,2:暂停中,3;已结束")
    public PlanStatus planStatus;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
	private String activityId;

    /**
     * 模板code
     */
    @ApiModelProperty(value = "模板code")
    private String templateCode;

    /**
     * 签名id
     */
    @ApiModelProperty(value = "签名id")
    private Long signId;

    /**
     * 非结束状态
     */
    @ApiModelProperty(value = "非结束状态")
    private Boolean notEndStatus;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String couponId;

}