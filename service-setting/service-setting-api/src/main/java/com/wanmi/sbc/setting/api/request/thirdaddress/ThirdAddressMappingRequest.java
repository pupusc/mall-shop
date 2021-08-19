package com.wanmi.sbc.setting.api.request.thirdaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>第三方地址映射操作</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAddressMappingRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * 第三方平台类型
	 */
	@ApiModelProperty(value = "第三方平台类型")
	@NotNull
	private ThirdPlatformType thirdPlatformType;
}