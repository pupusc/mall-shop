package com.wanmi.sbc.message.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>短信用途VO</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@Data
public class SmsPurposeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * businessType 参照com.wanmi.sbc.customer.bean.enums.SmsTemplate
	 */
	@ApiModelProperty(value = "业务类型编码")
	private String businessType;

	/**
	 * 用途
	 */
	@ApiModelProperty(value = "用途")
	private String purpose;
}