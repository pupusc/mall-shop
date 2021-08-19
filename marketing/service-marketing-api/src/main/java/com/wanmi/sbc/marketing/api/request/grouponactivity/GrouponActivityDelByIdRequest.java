package com.wanmi.sbc.marketing.api.request.grouponactivity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除拼团活动信息表请求参数</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityDelByIdRequest  {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动ID
	 */
	@NotNull
	private String grouponActivityId;
}