package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>部门管理VO</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@Data
public class DepartmentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String departmentId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称")
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
	private String employeeId;

	/**
	 * 员工名称
	 */
	@ApiModelProperty(value = "员工名称")
	private String employeeName;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer departmentSort;

	/**
	 * 父部门id（上一级）
	 */
	@ApiModelProperty(value = "父部门id（上一级）")
	private String parentDepartmentId;

	/**
	 * 员工数
	 */
	@ApiModelProperty(value = "员工数")
	private Integer employeeNum;

	/**
	 * 父部门id集合（多级）
	 */
	@ApiModelProperty(value = "父部门id集合（多级）")
	private String parentDepartmentIds;

	/**
	 * 删除标志 0未删除 1已删除
	 */
	@ApiModelProperty(value = "删除标志")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	private String createPerson;


	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	private String updatePerson;

}