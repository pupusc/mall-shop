package com.wanmi.sbc.customer.api.request.enterpriseinfo;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询企业信息表请求参数</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业Id
	 */
	@ApiModelProperty(value = "企业Id")
	@NotNull
	private String enterpriseId;

}