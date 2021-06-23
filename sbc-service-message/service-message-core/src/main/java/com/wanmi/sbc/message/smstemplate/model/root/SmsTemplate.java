package com.wanmi.sbc.message.smstemplate.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SmsType;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>短信模板实体类</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@Data
@Entity
@Table(name = "sms_template")
public class SmsTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 模板名称
	 */
	@Column(name = "template_name")
	private String templateName;

	/**
	 * 模板内容
	 */
	@Column(name = "template_content")
	private String templateContent;

	/**
	 * 短信模板申请说明
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 短信类型。其中： 0：验证码。 1：短信通知。 2：推广短信。 短信类型,0：验证码,1：短信通知,2：推广短信,3：国际/港澳台消息。
	 */
	@Column(name = "template_type")
	@Enumerated
	private SmsType templateType;

	/**
	 * 模板状态，0：待审核，1：审核通过，2：审核未通过
	 */
	@Column(name = "review_status")
	@Enumerated
	private ReviewStatus reviewStatus;

	/**
	 * 模板code,模板审核通过返回的模板code，发送短信时使用
	 */
	@Column(name = "template_code")
	private String templateCode;

	/**
	 * 审核原因
	 */
	@Column(name = "review_reason")
	private String reviewReason;

	/**
	 * 短信配置id
	 */
	@Column(name = "sms_setting_id")
	private Long smsSettingId;

	/**
	 * 删除标识位，0：未删除，1：已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

    /**
     * 业务类型，发送短信时使用
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 用途
     */
    @Column(name = "purpose")
    private String purpose;

    /**
     * 签名id
     */
    @Column(name = "sign_id")
    private Long signId;

	/**
	 * 开关标识, 0:关,1:开
	 */
	@Column(name = "open_flag")
	private Boolean openFlag;
}