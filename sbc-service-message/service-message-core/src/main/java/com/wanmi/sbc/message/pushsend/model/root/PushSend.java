package com.wanmi.sbc.message.pushsend.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>会员推送信息实体类</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@Data
@Entity
@Table(name = "push_send")
public class PushSend extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 友盟安卓任务ID
	 */
	@Column(name = "android_task_id")
	private String androidTaskId;

	/**
	 * 友盟iOS任务ID
	 */
	@Column(name = "ios_task_id")
	private String iosTaskId;

	/**
	 * 消息名称
	 */
	@Column(name = "msg_name")
	private String msgName;

	/**
	 * 消息标题
	 */
	@Column(name = "msg_title")
	private String msgTitle;

	/**
	 * 消息内容
	 */
	@Column(name = "msg_context")
	private String msgContext;

	/**
	 * 消息封面图片
	 */
	@Column(name = "msg_img")
	private String msgImg;

	/**
	 * 点击消息跳转的页面路由
	 */
	@Column(name = "msg_router")
	private String msgRouter;

	/**
	 * 消息接受人 0:全部会员 1:按会员等级 2:按标签 3:按人群 4:指定会员
	 */
	@Column(name = "msg_recipient")
	private Integer msgRecipient;

	/**
	 * 等级、标签、人群ID。逗号分割
	 */
	@Column(name = "msg_recipient_detail")
	private String msgRecipientDetail;

	/**
	 * 推送时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "push_time")
	private LocalDateTime pushTime;

	/**
	 * 预计发送人数
	 */
	@Column(name = "expected_send_count")
	private Integer expectedSendCount;

	/**
	 * 删除标志 0:未删除 1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "android_task_id", insertable = false, updatable = false)
	@NotFound(action= NotFoundAction.IGNORE)
	@JsonBackReference
	private PushDetail androidPushDetail;

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "ios_task_id", insertable = false, updatable = false)
	@NotFound(action= NotFoundAction.IGNORE)
	@JsonBackReference
	private PushDetail iosPushDetail;

	@Column(name = "push_flag")
	@Enumerated
	private PushFlag pushFlag;

	/**
	 * 运营计划ID
	 */
	@Column(name = "plan_id")
	private Long planId;

}