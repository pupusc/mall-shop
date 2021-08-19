package com.wanmi.sbc.crm.api.request.customerplan;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>根据活动id单个查询 人群运营计划请求参数</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanByActivityIdRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动标识
	 */
	@ApiModelProperty(value = "活动标识")
	@NotBlank
	private String activityId;
}