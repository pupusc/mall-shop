package com.wanmi.sbc.message.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>会员推送通知节点VO</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@ApiModel
@Data
public class PushSendNodeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称")
	private String nodeName;

	/**
	 * 节点类型
	 */
	@ApiModelProperty(value = "节点类型")
	private Integer nodeType;

	/**
	 * 节点code
	 */
	@ApiModelProperty(value = "节点code")
	private String nodeCode;

	/**
	 * 节点标题
	 */
	@ApiModelProperty(value = "节点标题")
	private String nodeTitle;

	/**
	 * 通知内容
	 */
	@ApiModelProperty(value = "通知内容")
	private String nodeContext;

	@ApiModelProperty(value = "预计发送总数")
	private Long expectedSendCount;

	/**
	 * 实际发送总数
	 */
	@ApiModelProperty(value = "实际发送总数")
	private Long actuallySendCount;

	/**
	 * 打开总数
	 */
	@ApiModelProperty(value = "打开总数")
	private Long openCount;

	/**
	 * 状态 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "状态 0:未启用 1:启用")
	private Integer status;

	@ApiModelProperty(value = "发送详情")
	private PushDetailVO pushDetailVO;

}