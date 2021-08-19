package com.wanmi.sbc.setting.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺快递公司关联表VO</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@ApiModel
@Data
public class StoreExpressCompanyRelaVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键UUID
	 */
	@ApiModelProperty(value = "主键UUID")
	private Long id;

	/**
	 * 主键ID,自增
	 */
	@ApiModelProperty(value = "主键ID,自增")
	private Long expressCompanyId;

	/**
	 * 快递公司信息
	 */
	@ApiModelProperty(value = "快递公司信息")
	private ExpressCompanyVO expressCompany;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	/**
	 * 商家标识
	 */
	@ApiModelProperty(value = "商家标识")
	private Integer companyInfoId;
}