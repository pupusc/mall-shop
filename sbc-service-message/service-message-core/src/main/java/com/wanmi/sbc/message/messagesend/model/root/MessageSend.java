package com.wanmi.sbc.message.messagesend.model.root;

import com.wanmi.sbc.message.bean.enums.MessageSendType;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import com.wanmi.sbc.message.bean.enums.SendType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>站内信任务表实体类</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@Data
@Entity
@Table(name = "message_send")
public class MessageSend extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long messageId;

	/**
	 * 任务名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 消息类型 0优惠促销
	 */
	@Column(name = "message_type")
	private MessageType messageType;

	/**
	 * 消息标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 消息内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 封面图
	 */
	@Column(name = "img_url")
	private String imgUrl;

	/**
	 * 推送类型 0：全部会员、1：按会员等级、2：按标签、3：按人群、4：指定会员
	 */
	@Column(name = "send_type")
	private MessageSendType sendType;

	/**
	 * 发送时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "send_time")
	private LocalDateTime sendTime;

	/**
	 * 删除标识 0：未删除 1：删除
	 */
	@Column(name = "del_flag")
	private DeleteFlag delFlag;

	/**
	 * 消息同步标识 0：push消息、1：运营计划
	 */
	@Column(name = "push_flag")
	private PushFlag pushFlag;

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
	 * 推送时间类型 0：立即、1：定时
	 */
	@Column(name = "send_time_type")
	private SendType sendTimeType;

	/**
	 * 推送消息id
	 */
	@Column(name = "push_id")
	private String pushId;

	/**
	 * 运营计划id
	 */
	@Column(name = "plan_id")
	private Long planId;

	/**
	 * 移动端落地页参数
	 */
	@Column(name = "route_params")
	private String routeParams;

	/**
	 * PC端落地页参数
	 */
	@Column(name = "pc_route_params")
	private String pcRouteParams;
}