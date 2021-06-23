package com.wanmi.sbc.message.smssign.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.bean.enums.InvolveThirdInterest;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SignSource;
import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * <p>短信签名实体类</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@Data
@Entity
@Table(name = "sms_sign")
public class SmsSign implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 签名名称
	 */
	@Column(name = "sms_sign_name")
	private String smsSignName;

	/**
	 * 签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称
	 */
	@Column(name = "sign_source")
	@Enumerated
	private SignSource signSource;

	/**
	 * 短信签名申请说明
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 是否涉及第三方利益：0：否，1：是
	 */
	@Column(name = "involve_third_interest")
	@Enumerated
	private InvolveThirdInterest involveThirdInterest;

	/**
	 * 审核状态：0:待审核，1:审核通过，2:审核未通过
	 */
	@Column(name = "review_status")
	@Enumerated
	private ReviewStatus reviewStatus;

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
	 * 删除标识，0：未删除，1：已删除
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
	 * 短信签名文件信息
	 */
	@Transient
	private List<SmsSignFileInfo> smsSignFileInfoList;

}