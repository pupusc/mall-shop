package com.wanmi.sbc.customer.department.model.root;


import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>部门管理实体类</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@Data
@Entity
@Table(name = "department")
public class Department extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "department_id")
	private String departmentId;

	/**
	 * 部门名称
	 */
	@Column(name = "department_name")
	private String departmentName;

	/**
	 * 公司ID
	 */
	@Column(name = "company_info_id")
	private Long companyInfoId;

	/**
	 * 层级
	 */
	@Column(name = "department_grade")
	private Integer departmentGrade;

	/**
	 * 主管ID
	 */
	@Column(name = "employee_id")
	private String employeeId;

	/**
	 * 主管名称
	 */
	@Column(name = "employee_name")
	private String employeeName;

	/**
	 * 排序
	 */
	@Column(name = "department_sort")
	private Integer departmentSort;

	/**
	 * 父部门id（上一级）
	 */
	@Column(name = "parent_department_id")
	private String parentDepartmentId;

	/**
	 * 员工数
	 */
	@Column(name = "employee_num")
	private Integer employeeNum;

	/**
	 * 父部门id集合（多级）
	 */
	@Column(name = "parent_department_ids")
	private String parentDepartmentIds;

	/**
	 * 删除标志
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag = DeleteFlag.NO;
}