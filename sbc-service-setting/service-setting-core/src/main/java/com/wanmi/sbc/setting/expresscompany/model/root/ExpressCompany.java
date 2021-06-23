package com.wanmi.sbc.setting.expresscompany.model.root;
import com.wanmi.sbc.common.enums.DeleteFlag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>物流公司实体类</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@Data
@Entity
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "express_company")
public class ExpressCompany implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID,自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "express_company_id")
	private Long expressCompanyId;

	/**
	 * 物流公司名称
	 */
	@Column(name = "express_name")
	private String expressName;

	/**
	 * 物流公司代码
	 */
	@Column(name = "express_code")
	private String expressCode;

	/**
	 * 是否是常用物流公司 0：否 1：是
	 */
	@Column(name = "is_checked")
	private Integer isChecked;

	/**
	 * 是否是用户新增 0：否 1：是
	 */
	@Column(name = "is_add")
	private Integer isAdd;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@ApiModelProperty(value = "删除标志 默认0：未删除 1：删除")
	private DeleteFlag delFlag;
}