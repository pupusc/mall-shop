package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>第三方地址映射表分页VO</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAddressPageVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 第三方省份id
	 */
	@ApiModelProperty(value = "第三方省份id")
	private String provId;

	/**
	 * 第三方省份名称
	 */
	@ApiModelProperty(value = "第三方省份名称")
	private String provName;


	/**
	 * 平台省份id
	 */
	@ApiModelProperty(value = "平台省份id")
	private String platformProvId;

	/**
	 * 第三方城市id
	 */
	@ApiModelProperty(value = "第三方城市id")
	private String cityId;

	/**
	 * 第三方城市名称
	 */
	@ApiModelProperty(value = "第三方城市名称")
	private String cityName;

	/**
	 * 平台城市id
	 */
	@ApiModelProperty(value = "平台城市id")
	private String platformCityId;

	/**
	 * 第三方区县id
	 */
	@ApiModelProperty(value = "第三方区县id")
	private String districtId;

	/**
	 * 第三方区县名称
	 */
	@ApiModelProperty(value = "第三方区县名称")
	private String districtName;

	/**
	 * 平台区县id
	 */
	@ApiModelProperty(value = "平台区县id")
	private String platformDistrictId;

	/**
	 * 第三方街道id
	 */
	@ApiModelProperty(value = "第三方街道id")
	private String streetId;

	/**
	 * 第三方街道名称
	 */
	@ApiModelProperty(value = "第三方街道名称")
	private String streetName;

	/**
	 * 平台街道id
	 */
	@ApiModelProperty(value = "平台街道id")
	private String platformStreetId;

	/**
	 * 平台地址
	 */
	@ApiModelProperty(value = "平台地址")
	private String platformAddress;

    /**
     * 第三方平台类型
     */
    @ApiModelProperty(value = "第三方平台类型")
	private ThirdPlatformType thirdPlatformType;
}