package com.wanmi.sbc.message.smssignfileinfo.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>短信签名文件信息实体类</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@Data
@Entity
@Table(name = "sms_sign_file_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignFileInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 短信签名id
	 */
	@Column(name = "sms_sign_id")
	private Long smsSignId;

	/**
	 * 文件路径
	 */
	@Column(name = "file_url")
	private String fileUrl;

	/**
	 * 文件名称
	 */
	@Column(name = "file_name")
	private String fileName;

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

}