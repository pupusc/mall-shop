package com.wanmi.sbc.crm.api.request.customerplan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.crm.bean.enums.PlanStatus;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

/**
 * <p> 人群运营计划分页查询请求参数</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

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
	private Integer triggerCondition;

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
	 * 目标人群
	 */
	@ApiModelProperty(value = "目标人群")
	private String receiveValueName;


	/**
	 * 删除标志位，0:未删除，1:已删除
	 */
	@ApiModelProperty(value = "删除标志位，0:未删除，1:已删除", hidden = true)
	private int delFlag;


	/**
	 * 0:未开始,1:进行中,2:暂停中,3;已结束
	 */
	@ApiModelProperty(value = "状态 0:未开始,1:进行中,2:暂停中,3;已结束")
    public Integer planStatus;

	@ApiModelProperty(value = "礼包是否已发放完 true:已满,false:未满")
	public Boolean giftPackageFull;

}