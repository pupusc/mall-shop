package com.wanmi.sbc.message.api.request.appmessage;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信消息会员关联表通用查询请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:16:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendCustomerScopeQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<Long> scopeIdList;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long scopeId;

	/**
	 * 消息id
	 */
	@ApiModelProperty(value = "消息id")
	private Long messageId;

	/**
	 * 关联的等级、人群、标签id
	 */
	@ApiModelProperty(value = "关联的等级、人群、标签id")
	private String joinId;

	/**
	 * 任务ids
	 */
	@ApiModelProperty(value = "任务ids")
	private List<Long> messageIds;

}