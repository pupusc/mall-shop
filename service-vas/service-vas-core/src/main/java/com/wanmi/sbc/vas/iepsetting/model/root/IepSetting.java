package com.wanmi.sbc.vas.iepsetting.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

/**
 * <p>企业购设置实体类</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@Data
@Entity
@Table(name = "iep_setting")
public class IepSetting extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 *  id 
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 *  企业会员名称 
	 */
	@Column(name = "enterprise_customer_name")
	private String enterpriseCustomerName;

	/**
	 *  企业价名称 
	 */
	@Column(name = "enterprise_price_name")
	private String enterprisePriceName;

	/**
	 *  企业会员logo 
	 */
	@Column(name = "enterprise_customer_logo")
	private String enterpriseCustomerLogo;

	/**
	 *  企业会员审核 0: 不需要审核 1: 需要审核 
	 */
	@Column(name = "enterprise_customer_audit_flag")
	@Enumerated
	private DefaultFlag enterpriseCustomerAuditFlag;

	/**
	 *  企业商品审核 0: 不需要审核 1: 需要审核 
	 */
	@Column(name = "enterprise_goods_audit_flag")
	@Enumerated
	private DefaultFlag enterpriseGoodsAuditFlag;

	/**
	 *  企业会员注册协议 
	 */
	@Column(name = "enterprise_customer_register_content")
	private String enterpriseCustomerRegisterContent;

	/**
	 *  是否删除标志 0：否，1：是 
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}