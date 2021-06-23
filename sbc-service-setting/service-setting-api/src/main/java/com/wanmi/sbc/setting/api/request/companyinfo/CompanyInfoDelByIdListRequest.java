package com.wanmi.sbc.setting.api.request.companyinfo;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除公司信息请求参数</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-公司信息IDList
	 */
	@ApiModelProperty(value = "批量删除-公司信息IDList")
	@NotEmpty
	private List<Long> companyInfoIdList;
}