package com.wanmi.sbc.setting.api.request.storeexpresscompanyrela;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;

/**
 * <p>店铺快递公司关联表新增参数</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreExpressCompanyRelaAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID,自增
	 */
	@ApiModelProperty(value = "主键ID,自增")
	@Max(9223372036854775807L)
	private Long expressCompanyId;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 商家标识
	 */
	@ApiModelProperty(value = "商家标识")
	@Max(9999999999L)
	private Integer companyInfoId;
}