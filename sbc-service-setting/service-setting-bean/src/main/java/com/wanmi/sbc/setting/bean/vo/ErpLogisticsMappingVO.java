package com.wanmi.sbc.setting.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>erp系统物流编码映射VO</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@ApiModel
@Data
public class ErpLogisticsMappingVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 基本设置ID
	 */
	@ApiModelProperty(value = "基本设置ID")
	private Integer id;

	/**
	 * 物流公司名称
	 */
	@ApiModelProperty(value = "物流公司名称")
	private String nameLogisticsCompany;

	/**
	 * erp系统物流编码
	 */
	@ApiModelProperty(value = "erp系统物流编码")
	private String erpLogisticsCode;

	/**
	 * 快递100物流编码
	 */
	@ApiModelProperty(value = "快递100物流编码")
	private String wmLogisticsCode;

}