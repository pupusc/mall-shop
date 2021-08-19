package com.wanmi.sbc.customer.livecompany.model.root;

import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>直播商家实体类</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@Data
@Entity
@Table(name = "live_company")
public class LiveCompany extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 提交审核时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "submit_time")
	private LocalDateTime submitTime;

	/**
	 * 直播状态 0未开通，1待审核，2已开通，3审核未通过，4禁用中
	 */
	@Column(name = "live_broadcast_status")
	private Integer liveBroadcastStatus;

	/**
	 * 直播审核原因
	 */
	@Column(name = "audit_reason")
	private String auditReason;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 删除人
	 */
	@Column(name = "delete_person")
	private String deletePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 公司信息ID
	 */
	@Column(name = "company_info_id")
	private Long companyInfoId;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

}