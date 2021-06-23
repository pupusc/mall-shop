package com.wanmi.sbc.setting.api.request.businessconfig;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>招商页设置通用查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-招商页设置主键List
	 */
	@ApiModelProperty(value = "批量查询-招商页设置主键List")
	private List<Integer> businessConfigIdList;

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
}