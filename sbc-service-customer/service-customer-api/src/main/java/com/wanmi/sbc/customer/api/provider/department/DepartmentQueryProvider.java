package com.wanmi.sbc.customer.api.provider.department;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.department.*;
import com.wanmi.sbc.customer.api.response.department.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>部门管理查询服务Provider</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@FeignClient(value = "${application.customer.name}", contextId = "DepartmentQueryProvider")
public interface DepartmentQueryProvider {

	/**
	 * 分页查询部门管理API
	 *
	 * @author wanggang
	 * @param departmentPageReq 分页请求参数和筛选对象 {@link DepartmentPageRequest}
	 * @return 部门管理分页列表信息 {@link DepartmentPageResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/page")
	BaseResponse<DepartmentPageResponse> page(@RequestBody @Valid DepartmentPageRequest departmentPageReq);

	/**
	 * 列表查询部门管理API
	 *
	 * @author wanggang
	 * @param departmentListReq 列表请求参数和筛选对象 {@link DepartmentListRequest}
	 * @return 部门管理的列表信息 {@link DepartmentListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/list-department-tree")
	BaseResponse<DepartmentListResponse> listDepartmentTree(@RequestBody @Valid DepartmentListRequest departmentListReq);

	/**
	 * 根据管理部门集合查询当前部门及其所有子级部门ID集合(不包含上级部门）
	 *
	 * @author wanggang
	 * @param request 列表请求参数和筛选对象 {@link DepartmentListByManageDepartmentIdsRequest}
	 * @return 部门管理的列表信息 {@link DepartmentListByManageDepartmentIdsResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/list-by-manage-department-ids")
	BaseResponse<DepartmentListByManageDepartmentIdsResponse> ListByManageDepartmentIds(@RequestBody @Valid DepartmentListByManageDepartmentIdsRequest request);


	/**
	 * 根据管理部门集合查询当前部门及其所有子级部门ID集合以及上级部门
	 *
	 * @author wanggang
	 * @param request 列表请求参数和筛选对象 {@link DepartmentListAllByManageDepartmentIdsRequest}
	 * @return 部门管理的列表信息 {@link DepartmentListAllByManageDepartmentIdsResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/list-all-by-manage-department-ids")
	BaseResponse<DepartmentListAllByManageDepartmentIdsResponse> ListAllByManageDepartmentIds(@RequestBody @Valid DepartmentListAllByManageDepartmentIdsRequest request);

	/**
	 * 根据归属部门查询部门树（只到当前当前归属部门层级）
	 *
	 * @author wanggang
	 * @param request 列表请求参数和筛选对象 {@link DepartmentListByBelongToDepartmentIdsRequest}
	 * @return 部门管理的列表信息 {@link DepartmentListByBelongToDepartmentIdsResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/list-by-belong-to-department-ids")
	BaseResponse<DepartmentListByBelongToDepartmentIdsResponse> listByBelongToDepartmentIds(@RequestBody @Valid DepartmentListByBelongToDepartmentIdsRequest request);


	/**
	 * 导出部门模板
	 * @return base64位文件流字符串 {@link DepartmentExcelExportTemplateResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/export-template")
	BaseResponse<DepartmentExcelExportTemplateResponse> exportTemplate();

	/**
	 * 列表查询部门管理API
	 *
	 * @author wanggang
	 * @param departmentListReq 列表请求参数和筛选对象 {@link DepartmentListRequest}
	 * @return 部门管理的列表信息 {@link DepartmentListResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/list-department")
	BaseResponse<DepartmentListResponse> listDepartment(@RequestBody @Valid DepartmentListRequest departmentListReq);


}

