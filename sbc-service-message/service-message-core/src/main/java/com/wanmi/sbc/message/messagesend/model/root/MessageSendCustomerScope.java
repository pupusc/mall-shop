package com.wanmi.sbc.message.messagesend.model.root;


import lombok.Data;
import javax.persistence.*;

/**
 * <p>站内信消息会员关联表实体类</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:16:04
 */
@Data
@Entity
@Table(name = "message_send_customer_scope")
public class MessageSendCustomerScope {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long scopeId;

	/**
	 * 消息id
	 */
	@Column(name = "message_id")
	private Long messageId;

	/**
	 * 关联的等级、人群、标签id
	 */
	@Column(name = "join_id")
	private String joinId;

}