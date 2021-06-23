package com.wanmi.sbc.customer.levelrights.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>会员等级权益表实体类</p>
 */
@Data
@Entity
@Table(name = "customer_level_rights")
public class CustomerLevelRights implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rights_id")
	private Integer rightsId;

	/**
	 * 权益名称
	 */
	@Column(name = "rights_name")
	private String rightsName;

	/**
	 * 权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分
	 */
	@Column(name = "rights_type")
	@Enumerated
	private LevelRightsType rightsType;

	/**
	 * logo地址
	 */
	@Column(name = "rights_logo")
	private String rightsLogo;

	/**
	 * 权益介绍
	 */
	@Column(name = "rights_description")
	private String rightsDescription;

	/**
	 * 权益规则(JSON)
	 */
	@Column(name = "rights_rule")
	private String rightsRule;

	/**
	 * 活动Id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 是否开启 0:关闭 1:开启
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 删除标识 0:未删除1:已删除
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
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "del_time")
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	@Column(name = "del_person")
	private String delPerson;

}