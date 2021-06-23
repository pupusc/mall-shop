package com.wanmi.sbc.setting.api.request.thirdaddress;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>第三方地址映射表分页查询请求参数</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAddressPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 省份名称
	 */
	@ApiModelProperty(value = "省份名称")
	private String provName;

	/**
	 * 城市名称
	 */
	@ApiModelProperty(value = "城市名称")
	private String cityName;

	/**
	 * 区县名称
	 */
	@ApiModelProperty(value = "区县名称")
	private String districtName;

	/**
	 * 街道名称
	 */
	@ApiModelProperty(value = "街道名称")
	private String streetName;

	/**
	 * 第三方标志 0:likedMall 1:京东
	 */
	@ApiModelProperty(value = "第三方标志 0:likedMall 1:京东")
	private ThirdPlatformType thirdFlag;

	/**
	 * 映射
	 */
	@ApiModelProperty(value = "映射状态")
	private Boolean mappingFlag;

}