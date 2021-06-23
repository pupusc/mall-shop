package com.wanmi.sbc.setting.api.request.companyinfo;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除公司信息请求参数</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDelByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 公司信息ID
	 */
	@ApiModelProperty(value = "公司信息ID")
	@NotNull
	private Long companyInfoId;
}