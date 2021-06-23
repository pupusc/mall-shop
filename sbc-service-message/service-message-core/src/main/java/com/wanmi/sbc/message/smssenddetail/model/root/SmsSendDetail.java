package com.wanmi.sbc.message.smssenddetail.model.root;

import com.wanmi.sbc.message.bean.enums.SendDetailStatus;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>短信发送实体类</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@Data
@Entity
@Table(name = "sms_send_detail")
public class SmsSendDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 发送任务id
	 */
	@Column(name = "send_id")
	private Long sendId;

	/**
	 * 接收短信的号码
	 */
	@Column(name = "phone_numbers")
	private String phoneNumbers;

	/**
	 * 回执id
	 */
	@Column(name = "biz_id")
	private String bizId;

	/**
	 * 状态（0-失败，1-成功）
	 */
	@Column(name = "status")
	private SendDetailStatus status;

	/**
	 * 请求状态码。
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 任务执行信息
	 */
	@Column(name = "message")
	private String message;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * sendTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "send_time")
	private LocalDateTime sendTime;

	@Transient
	private String signName;

	@Transient
	private String templateCode;

    /**
     * 模板内容JSON
     */
    @Transient
	private String templateParam;

    /**
     * 业务类型  参照com.wanmi.sbc.customer.bean.enums.SmsTemplate
     */
	@Transient
	private String businessType;

	@Transient
	private Long signId;

}