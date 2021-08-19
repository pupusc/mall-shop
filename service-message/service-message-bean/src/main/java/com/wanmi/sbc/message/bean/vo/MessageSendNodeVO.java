package com.wanmi.sbc.message.bean.vo;

import com.wanmi.sbc.message.bean.enums.NoticeType;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表VO</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@Data
public class MessageSendNodeVO implements Serializable {
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
	 * 启用状态：0未启用 1启用
	 */
	@ApiModelProperty(value = "启用状态：0未启用 1启用")
	private SwitchFlag status;

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

	/**
	 * 跳转路由
	 */
	@ApiModelProperty(value = "跳转路由")
	private String routeName;

	/**
	 * 节点类型
	 */
	@ApiModelProperty(value = "节点类型")
	private NoticeType nodeType;

	/**
	 * 节点code
	 */
	@ApiModelProperty(value = "节点code")
	private String nodeCode;

}