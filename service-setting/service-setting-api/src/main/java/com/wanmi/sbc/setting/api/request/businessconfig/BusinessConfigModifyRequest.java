package com.wanmi.sbc.setting.api.request.businessconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;

/**
 * <p>招商页设置修改参数</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 招商页设置主键
	 */
	@ApiModelProperty(value = "招商页设置主键")
	@Max(9999999999L)
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
	 * 商家招商页banner
	 */
	@ApiModelProperty(value = "商家招商页banner")
	private String supplierBanner;

	/**
	 * 商家招商页自定义
	 */
	@ApiModelProperty(value = "商家招商页自定义")
	private String supplierCustom;

	/**
	 * 商家招商页注册协议
	 */
	@ApiModelProperty(value = "商家招商页注册协议")
	private String supplierRegister;

	/**
	 * 商家招商页入驻协议
	 */
	@ApiModelProperty(value = "商家招商页入驻协议")
	private String supplierEnter;
}