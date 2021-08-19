package com.wanmi.sbc.customer.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>员工与部门关联VO</p>
 * @author wanggang
 * @date 2020-02-26 19:33:10
 */
@ApiModel
@Data
public class EmployeeDepartmentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 员工id
	 */
	@ApiModelProperty(value = "员工id")
	private String employeeId;

	/**
	 * 部门id
	 */
	@ApiModelProperty(value = "部门id")
	private Long departmentId;

	/**
	 * 是否主管，0：否，1：是
	 */
	@ApiModelProperty(value = "是否主管，0：否，1：是")
	private Integer isLeader;

}