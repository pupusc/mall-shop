package com.wanmi.sbc.message.api.request.messagesendnode;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表新增参数</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

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
	 * 启用状态：0未启用 1启用
	 */
	@ApiModelProperty(value = "启用状态：0未启用 1启用")
	@NotNull
	private Integer status;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;

	/**
	 * 删除标识 0未删除 1删除
	 */
	@ApiModelProperty(value = "删除标识 0未删除 1删除", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 发送数
	 */
	@ApiModelProperty(value = "发送数")
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@ApiModelProperty(value = "打开数")
	private Integer openSum;

}