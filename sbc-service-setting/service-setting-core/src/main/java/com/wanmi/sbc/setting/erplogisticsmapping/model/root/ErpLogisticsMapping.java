package com.wanmi.sbc.setting.erplogisticsmapping.model.root;


import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>erp系统物流编码映射实体类</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@Data
@Entity
@Table(name = "erp_logistics_mapping")
public class ErpLogisticsMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 基本设置ID
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	/**
	 * 物流公司名称
	 */
	@Column(name = "name_logistics_company")
	private String nameLogisticsCompany;

	/**
	 * erp系统物流编码
	 */
	@Column(name = "erp_logistics_code")
	private String erpLogisticsCode;

	/**
	 * 快递100物流编码
	 */
	@Column(name = "wm_logistics_code")
	private String wmLogisticsCode;

}