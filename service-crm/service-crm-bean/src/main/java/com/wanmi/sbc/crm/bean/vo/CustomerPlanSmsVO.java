package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>运营计划与短信关联VO</p>
 * @author dyt
 * @date 2020-01-10 11:12:50
 */
@ApiModel
@Data
public class CustomerPlanSmsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@ApiModelProperty(value = "标识")
	private Long id;

	/**
	 * 签名id
	 */
	@ApiModelProperty(value = "签名id")
	private Long signId;

    /**
     * 签名名称s
     */
    @ApiModelProperty(value = "签名名称")
    private String signName;

	/**
	 * 模板id
	 */
	@ApiModelProperty(value = "模板id")
	private String templateCode;

	/**
	 * 模板内容
	 */
	@ApiModelProperty(value = "模板内容")
	private String templateContent;

	/**
	 * 计划id
	 */
	@ApiModelProperty(value = "计划id")
	private Long planId;

}