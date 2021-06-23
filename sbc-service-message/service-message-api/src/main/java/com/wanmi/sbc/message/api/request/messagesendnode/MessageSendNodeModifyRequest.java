package com.wanmi.sbc.message.api.request.messagesendnode;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表修改参数</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称")
	private String nodeName;

	/**
	 * 节点标题
	 */
	@ApiModelProperty(value = "节点标题")
	private String nodeTitle;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String nodeContent;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;


}