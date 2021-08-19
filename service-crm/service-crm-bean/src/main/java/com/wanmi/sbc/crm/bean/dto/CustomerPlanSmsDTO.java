package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>运营计划与短信关联参数</p>
 * @author dyt
 * @date 2020-01-10 11:12:50
 */
@ApiModel
@Data
public class CustomerPlanSmsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * 签名id
     */
    @ApiModelProperty(value = "签名id")
    private Long signId;

    /**
     * 签名名称
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
}