package com.wanmi.sbc.setting.storeexpresscompanyrela.model.root;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * <p>店铺快递公司关联表实体类</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@Data
@Entity
@Table(name = "store_express_company_rela")
public class StoreExpressCompanyRela implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键UUID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 主键ID,自增
	 */
	@Column(name = "express_company_id")
	private Long expressCompanyId;

	@JsonManagedReference
	@JoinColumn(name = "express_company_id", insertable = false, updatable = false)
	@OneToOne
	private ExpressCompany expressCompany;

	/**
	 * 店铺标识
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 商家标识
	 */
	@Column(name = "company_info_id")
	private Integer companyInfoId;
}