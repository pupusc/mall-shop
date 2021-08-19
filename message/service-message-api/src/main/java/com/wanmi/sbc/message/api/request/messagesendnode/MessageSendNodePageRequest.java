package com.wanmi.sbc.message.api.request.messagesendnode;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表分页查询请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodePageRequest extends BaseQueryRequest {

	private static final long serialVersionUID = -3743558099636066587L;
	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称")
	private String nodeName;
	/**
	 * 节点代码
	 */
	@ApiModelProperty(value = "节点代码")
	private String nodeCode;
}