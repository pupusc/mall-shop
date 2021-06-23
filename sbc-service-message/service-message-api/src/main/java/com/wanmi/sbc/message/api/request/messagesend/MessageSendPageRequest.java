package com.wanmi.sbc.message.api.request.messagesend;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信任务表分页查询请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "任务名称")
	private String name;

}