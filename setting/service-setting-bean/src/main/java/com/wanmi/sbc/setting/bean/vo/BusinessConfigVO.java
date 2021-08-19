package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>招商页设置VO</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@Data
public class BusinessConfigVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 招商页设置主键
	 */
	@ApiModelProperty(value = "招商页设置主键")
	private Integer businessConfigId;

	/**
	 * 招商页banner
	 */
	@ApiModelProperty(value = "招商页banner")
	private String businessBanner;

	/**
	 * 招商页自定义
	 */
	@ApiModelProperty(value = "招商页自定义")
	private String businessCustom;

	/**
	 * 招商页注册协议
	 */
	@ApiModelProperty(value = "招商页注册协议")
	private String businessRegister;

	/**
	 * 招商页入驻协议
	 */
	@ApiModelProperty(value = "招商页入驻协议")
	private String businessEnter;

	/**
	 * 招商页banner
	 */
	@ApiModelProperty(value = "商家banner")
	private String supplierBanner;

	/**
	 * 招商页自定义
	 */
	@ApiModelProperty(value = "商家自定义")
	private String supplierCustom;

	/**
	 * 招商页注册协议
	 */
	@ApiModelProperty(value = "商家注册协议")
	private String supplierRegister;

	/**
	 * 招商页入驻协议
	 */
	@ApiModelProperty(value = "商家入驻协议")
	private String supplierEnter;

}