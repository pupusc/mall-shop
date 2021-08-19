package com.wanmi.sbc.setting.onlineservice.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>onlineService实体类</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@Data
@Entity
@Table(name = "online_service")
public class OnlineService implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "online_service_id")
	private Integer onlineServiceId;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 在线客服是否启用 0 不启用， 1 启用
	 */
	@Column(name = "server_status")
	private DefaultFlag serverStatus;

	/**
	 * 客服标题
	 */
	@Column(name = "service_title")
	private String serviceTitle;

	/**
	 * 生效终端pc 0 不生效 1 生效
	 */
	@Column(name = "effective_pc")
	private DefaultFlag effectivePc;

	/**
	 * 生效终端App 0 不生效 1 生效
	 */
	@Column(name = "effective_app")
	private DefaultFlag effectiveApp;

	/**
	 * 生效终端移动版 0 不生效 1 生效
	 */
	@Column(name = "effective_mobile")
	private DefaultFlag effectiveMobile;

	/**
	 * 删除标志 默认0：未删除 1：删除
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
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@Column(name = "operate_person")
	private String operatePerson;

}