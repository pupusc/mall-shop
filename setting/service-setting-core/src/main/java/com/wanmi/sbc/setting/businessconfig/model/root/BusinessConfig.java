package com.wanmi.sbc.setting.businessconfig.model.root;


import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>招商页设置实体类</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@Data
@Entity
@Table(name = "business_config")
public class BusinessConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 招商页设置主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "business_config_id")
	private Integer businessConfigId;

	/**
	 * 招商页banner
	 */
	@Column(name = "business_banner")
	private String businessBanner;

	/**
	 * 招商页自定义
	 */
	@Column(name = "business_custom")
	private String businessCustom;

	/**
	 * 招商页注册协议
	 */
	@Column(name = "business_register")
	private String businessRegister;

	/**
	 * 招商页入驻协议
	 */
	@Column(name = "business_enter")
	private String businessEnter;

	/**
	 * 商家banner
	 */
	@Column(name = "supplier_banner")
	private String supplierBanner;

	/**
	 * 商家自定义
	 */
	@Column(name = "supplier_custom")
	private String supplierCustom;

	/**
	 * 商家注册协议
	 */
	@Column(name = "supplier_register")
	private String supplierRegister;

	/**
	 * 商家入驻协议
	 */
	@Column(name = "supplier_enter")
	private String supplierEnter;
}