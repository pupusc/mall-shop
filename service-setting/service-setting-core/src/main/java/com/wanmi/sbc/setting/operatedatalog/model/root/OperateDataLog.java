package com.wanmi.sbc.setting.operatedatalog.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>系统日志实体类</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@Data
@Entity
@Table(name = "operate_data_log")
public class OperateDataLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 操作内容
	 */
	@Column(name = "operate_content")
	private String operateContent;

	/**
	 * 操作标识
	 */
	@Column(name = "operate_id")
	private String operateId;

	/**
	 * 操作前数据
	 */
	@Column(name = "operate_before_data")
	private String operateBeforeData;

	/**
	 * 操作后数据
	 */
	@Column(name = "operate_after_data")
	private String operateAfterData;

	/**
	 * 操作人账号
	 */
	@Column(name = "operate_account")
	private String operateAccount;

	/**
	 * 操作人名称
	 */
	@Column(name = "operate_name")
	private String operateName;

	/**
	 * 操作时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "operate_time")
	private LocalDateTime operateTime;

	/**
	 * 删除标记
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}