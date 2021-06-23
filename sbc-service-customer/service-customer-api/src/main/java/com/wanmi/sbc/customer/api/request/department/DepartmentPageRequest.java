package com.wanmi.sbc.customer.api.request.department;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>部门管理分页查询请求参数</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> departmentIdList;

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
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 父部门id集合（多级）
	 */
	@ApiModelProperty(value = "父部门id集合（多级）")
	private String parentDepartmentIds;

}