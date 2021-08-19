package com.wanmi.sbc.message.messagesendnode.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>站内信通知节点表实体类</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@Data
@Entity
@Table(name = "message_send_node")
public class MessageSendNode extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	/**
	 * 节点名称
	 */
	@Column(name = "node_name")
	private String nodeName;

	/**
	 * 节点标题
	 */
	@Column(name = "node_title")
	private String nodeTitle;

	/**
	 * 内容
	 */
	@Column(name = "node_content")
	private String nodeContent;

	/**
	 * 启用状态：0未启用 1启用
	 */
	@Column(name = "status")
	private SwitchFlag status;

	/**
	 * 删除标识 0未删除 1删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 发送数
	 */
	@Column(name = "send_sum")
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@Column(name = "open_sum")
	private Integer openSum;


	/**
	 * 跳转路由
	 */
	@Column(name = "route_name")
	private String routeName;

	/**
	 * 节点类型
	 */
	@Column(name = "node_type")
	private NodeType nodeType;

	/**
	 * 节点code
	 */
	@Column(name = "node_code")
	private String nodeCode;
}