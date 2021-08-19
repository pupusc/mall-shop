package com.wanmi.sbc.setting.api.request.baseconfig;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>基本设置分页查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-基本设置IDList
	 */
	@ApiModelProperty(value = "批量查询-基本设置IDList")
	private List<Integer> baseConfigIdList;

	/**
	 * 基本设置ID
	 */
	@ApiModelProperty(value = "基本设置ID")
	private Integer baseConfigId;

	/**
	 * PC端商城网址
	 */
	@ApiModelProperty(value = "PC端商城网址")
	private String pcWebsite;

	/**
	 * 移动端商城网址
	 */
	@ApiModelProperty(value = "移动端商城网址")
	private String mobileWebsite;

	/**
	 * PC商城logo
	 */
	@ApiModelProperty(value = "PC商城logo")
	private String pcLogo;

	/**
	 * PC商城banner,最多可添加5个,多个图片间以'|'隔开
	 */
	@ApiModelProperty(value = "PC商城banner,最多可添加5个,多个图片间以'|'隔开")
	private String pcBanner;

	/**
	 * 移动商城banner,最多可添加5个,多个图片间以'|'隔开
	 */
	@ApiModelProperty(value = "移动商城banner,最多可添加5个,多个图片间以'|'隔开")
	private String mobileBanner;

	/**
	 * PC商城首页banner,最多可添加5个,多个图片间以'|'隔开
	 */
	@ApiModelProperty(value = "PC商城首页banner,最多可添加5个,多个图片间以'|'隔开")
	private String pcMainBanner;

	/**
	 * 网页ico
	 */
	@ApiModelProperty(value = "网页ico")
	private String pcIco;

	/**
	 * pc商城标题
	 */
	@ApiModelProperty(value = "pc商城标题")
	private String pcTitle;

	/**
	 * 商家后台登录网址
	 */
	@ApiModelProperty(value = "商家后台登录网址")
	private String supplierWebsite;

	/**
	 * 会员注册协议
	 */
	@ApiModelProperty(value = "会员注册协议")
	private String registerContent;

}