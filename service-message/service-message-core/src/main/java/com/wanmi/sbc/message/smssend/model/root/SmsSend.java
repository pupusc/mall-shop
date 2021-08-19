package com.wanmi.sbc.message.smssend.model.root;

import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.enums.ResendType;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.bean.enums.SendType;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>短信发送实体类</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@Data
@Entity
//@DynamicUpdate
@Table(name = "sms_send")
public class SmsSend implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "sms_setting_id")
	private Long smsSettingId;

	/**
	 * 短信内容
	 */
	@Column(name = "context")
	private String context;

	/**
	 * 模板code
	 */
	@Column(name = "template_code")
	private String templateCode;

	/**
	 * 签名id
	 */
	@Column(name = "sign_id")
	private Long signId;

	/**
	 * 接收人描述
	 */
	@Column(name = "receive_context")
	private String receiveContext;

	/**
	 * 接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）
	 */
	@Column(name = "receive_type")
	private ReceiveType receiveType;

	/**
	 * 接收人明细
	 */
	@Column(name = "receive_value")
	private String receiveValue;

	/**
	 * 手工添加的号码
	 */
	@Column(name = "manual_add")
	private String manualAdd;

	/**
	 * 状态（0-未开始，1-进行中，2-已结束，3-任务失败）
	 */
	@Column(name = "status")
	private SendStatus status;

	/**
	 * 任务执行信息
	 */
	@Column(name = "message")
	private String message;

	/**
	 * 发送类型（0-立即发送，1-定时发送）
	 */
	@Column(name = "send_type")
	private SendType sendType;

	/**
	 * 发送时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "send_time")
	private LocalDateTime sendTime;

	/**
	 * 预计发送条数
	 */
	@Column(name = "row_count")
	private Integer rowCount;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 重发类型（0-不可重发，1-可重发）
	 */
	@Column(name = "resend_type")
	private ResendType resendType;

	/**
	 * 发送明细条数
	 */
	@Column(name = "send_detail_count")
	private Integer sendDetailCount;

	@Transient
	private String signName;
}