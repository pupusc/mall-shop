package com.wanmi.sbc.message.pushsendnode.model.root;

import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.NodeType;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>会员推送通知节点实体类</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@Data
@Entity
@Table(name = "push_send_node")
public class PushSendNode extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
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
	 * 节点类型
	 */
	@Column(name = "node_type")
	private Integer nodeType;

	/**
	 * 节点code
	 */
	@Column(name = "node_code")
	private String nodeCode;

	/**
	 * 节点标题
	 */
	@Column(name = "node_title")
	private String nodeTitle;

	/**
	 * 通知内容
	 */
	@Column(name = "node_context")
	private String nodeContext;

	/**
	 * 预计发送
	 */
	@Column(name = "expected_send_count")
	private Long expectedSendCount;

	/**
	 * 实际发送总数
	 */
	@Column(name = "actually_send_count")
	private Long actuallySendCount;

	/**
	 * 打开总数
	 */
	@Column(name = "open_count")
	private Long openCount;

	/**
	 * 状态 0:未启用 1:启用
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 删除标志 0:未删除 1:删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}