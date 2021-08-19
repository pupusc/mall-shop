package com.wanmi.sbc.setting.api.request.platformaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * <p>平台地址信息校验是否需要完善参数</p>
 * @author yhy
 * @date 2020-03-30 14:39:57
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAddressVerifyRequest extends BaseRequest {

	private static final long serialVersionUID = 8834926958552326629L;
	/**
	 * 省级id
	 */
	@ApiModelProperty(value = "省级id")
	@NotBlank
	private String provinceId;

	/**
	 * 城市id
	 */
	@ApiModelProperty(value = "城市id")
	@NotBlank
	private String cityId;

	/**
	 * 区县id
	 */
	@ApiModelProperty(value = "区县id")
	@NotBlank
	private String areaId;

	/**
	 * 街道id
	 */
	@ApiModelProperty(value = "街道id")
	private String streetId;

}