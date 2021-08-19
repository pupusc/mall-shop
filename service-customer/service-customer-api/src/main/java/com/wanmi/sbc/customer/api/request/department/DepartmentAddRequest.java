package com.wanmi.sbc.customer.api.request.department;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>部门管理新增参数</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称")
	@NotBlank
	@Length(max=50)
	private String departmentName;

	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID")
	private Long companyInfoId;

	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer departmentGrade;

	/**
	 * 主管员工ID
	 */
	@ApiModelProperty(value = "主管员工ID")
	@Length(max=32)
	private String employeeId;

	/**
	 * 员工名称
	 */
	@ApiModelProperty(value = "员工名称")
	@Length(max=45)
	private String employeeName;

	/**
	 * 父部门id（上一级）
	 */
	@ApiModelProperty(value = "父部门id（上一级）")
	private String parentDepartmentId;



	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}