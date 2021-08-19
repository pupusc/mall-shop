package com.wanmi.sbc.setting.api.request.expresscompany;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>物流公司新增参数</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpressCompanyAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 物流公司名称
	 */
	@ApiModelProperty(value = "物流公司名称")
	@Length(max=125)
	private String expressName;

	/**
	 * 物流公司代码
	 */
	@ApiModelProperty(value = "物流公司代码")
	@Length(max=255)
	private String expressCode;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@ApiModelProperty(value = "删除标志 默认0：未删除 1：删除")
	private DeleteFlag delFlag;
}