package com.wanmi.sbc.customer.api.request.department;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * <p>批量删除部门管理请求参数</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentModifyLeaderRequest extends CustomerBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "部门ID")
	@NotBlank
	private String departmentId;

	/**
	 * 主管ID
	 */
	@ApiModelProperty(value = "主管ID")
	private String oldEmployeeId;

	/**
	 * 新主管ID
	 */
	@ApiModelProperty(value = "新主管ID")
	@NotBlank
	private String newEmployeeId;
}
