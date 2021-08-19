package com.wanmi.sbc.customer.api.provider.department;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.department.*;
import com.wanmi.sbc.customer.api.response.department.DepartmentAddResponse;
import com.wanmi.sbc.customer.api.response.department.DepartmentModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>部门管理保存服务Provider</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@FeignClient(value = "${application.customer.name}", contextId = "DepartmentProvider")
public interface DepartmentProvider {

	/**
	 * 新增部门管理API
	 *
	 * @author wanggang
	 * @param departmentAddRequest 部门管理新增参数结构 {@link DepartmentAddRequest}
	 * @return 新增的部门管理信息 {@link DepartmentAddResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/add")
	BaseResponse<DepartmentAddResponse> add(@RequestBody @Valid DepartmentAddRequest departmentAddRequest);

	/**
	 * 新增部门管理API(导入)
	 *
	 * @author wanggang
	 * @param request 部门管理新增参数结构 {@link DepartmentImportRequest}
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/department/add-from-import")
	BaseResponse addfromImport(@RequestBody @Valid DepartmentImportRequest request);

	/**
	 * 修改部门管理API
	 *
	 * @author wanggang
	 * @param departmentModifyRequest 部门管理修改参数结构 {@link DepartmentModifyRequest}
	 * @return 修改的部门管理信息 {@link DepartmentModifyResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/modify")
	BaseResponse<DepartmentModifyResponse> modifyDepartmentName(@RequestBody @Valid DepartmentModifyRequest departmentModifyRequest);


	/**
	 * 拖拽排序
	 * @param sortRequest
	 * @return
	 */
	@PostMapping("/customer/${application.customer.version}/department/sort")
	BaseResponse sort(@RequestBody @Valid DepartmentSortRequest sortRequest);

	/**
	 * 单个删除部门管理API
	 *
	 * @author wanggang
	 * @param departmentDelByIdRequest 单个删除参数结构 {@link DepartmentDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid DepartmentDelByIdRequest departmentDelByIdRequest);

	/**
	 * 修改主管
	 *
	 * @author wanggang
	 * @param request 批量删除参数结构 {@link DepartmentModifyLeaderRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/customer/${application.customer.version}/department/delete-by-id-list")
	BaseResponse modifyLeader(@RequestBody @Valid DepartmentModifyLeaderRequest request);

}

