package com.wanmi.sbc.crm.api.request.customerplanconversion;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询运营计划转化效果请求参数</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanConversionByPlanIdRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	@NotNull
	private Long planId;
}