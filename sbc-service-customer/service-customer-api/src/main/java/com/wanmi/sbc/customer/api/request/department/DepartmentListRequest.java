package com.wanmi.sbc.customer.api.request.department;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>部门管理列表查询请求参数</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID")
	private Long companyInfoId;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer departmentSort;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
	private Integer delFlag;

	/**
	 * 归属部门/管理部门集合
	 */
	private List<String> departmentIsolationIdList;

	/**
	 * 管理部门id列表(数据隔离)
	 */
	@ApiModelProperty(value = "管理部门列表")
	private List<String> manageDepartmentIdList;

	/**
	 * 所属部门id列表(数据隔离)
	 */
	@ApiModelProperty(value = "所属部门列表")
	private List<String> belongToDepartmentIdList;

	/**
	 * 是否有归属部门
	 */
	private Boolean belongToDepartment = Boolean.TRUE;

	private String employeeId;

	/**
	 * 是否是主账号 0:否，1：是
	 */
	private Integer isMaster = 0;

	/**
	 * 管理部门集合
	 */
	private String manageDepartmentIds;


}