package com.wanmi.sbc.crm.api.request.planstatisticsmessage;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询运营计划效果统计站内信收到人/次统计数据请求参数</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStatisticsMessageByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	@NotNull
	private Long planId;

}