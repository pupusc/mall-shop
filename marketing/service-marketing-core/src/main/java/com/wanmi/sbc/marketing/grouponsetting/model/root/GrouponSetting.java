package com.wanmi.sbc.marketing.grouponsetting.model.root;


import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>拼团活动信息表实体类</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@Data
@Entity
@Table(name = "groupon_setting")
public class GrouponSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 * 拼团商品审核
	 */
	@Column(name = "goods_audit_flag")
	private DefaultFlag goodsAuditFlag;

	/**
	 * 广告
	 */
	@Column(name = "advert")
	private String advert;

	/**
	 * 拼团规则
	 */
	@Column(name = "rule")
	private String rule;

}