package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 等级公共类 包含店铺等级和平台等级
 * @author yinxianzhi
 * @date 2019-03-07
 */
@Data
public class CommonLevelVO implements Serializable {

	private static final long serialVersionUID = 1433711138538574464L;

	/**
	 * 等级ID
	 */
	private Long levelId;

	/**
	 * 等级折扣率
	 */
	private BigDecimal levelDiscount;

	/**
	 * 等级类型 0、平台 1、店铺
	 */
	private BoolFlag levelType;

	/**
	 * 等级名称
	 */
	private String levelName;

}