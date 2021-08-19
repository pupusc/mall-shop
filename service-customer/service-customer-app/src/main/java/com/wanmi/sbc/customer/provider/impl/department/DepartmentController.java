package com.wanmi.sbc.customer.provider.impl.department;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.department.DepartmentProvider;
import com.wanmi.sbc.customer.api.request.department.*;
import com.wanmi.sbc.customer.api.response.department.DepartmentAddResponse;
import com.wanmi.sbc.customer.api.response.department.DepartmentModifyResponse;
import com.wanmi.sbc.customer.bean.dto.DepartmentSortDTO;
import com.wanmi.sbc.customer.department.model.root.Department;
import com.wanmi.sbc.customer.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>部门管理保存服务接口实现</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@RestController
@Validated
public class DepartmentController implements DepartmentProvider {
	@Autowired
	private DepartmentService departmentService;

	@Override
	public BaseResponse<DepartmentAddResponse> add(@RequestBody @Valid DepartmentAddRequest departmentAddRequest) {
		Department department = KsBeanUtil.convert(departmentAddRequest, Department.class);
		return BaseResponse.success(new DepartmentAddResponse(
				departmentService.wrapperVo(departmentService.add(department))));
	}

	@Override
	public BaseResponse addfromImport(@Valid DepartmentImportRequest request) {
		if (departmentService.countByCompanyInfoId(request.getList().get(0).getCompanyInfoId()) > 0 ){
			return BaseResponse.SUCCESSFUL();
		}
		List<Department> list = KsBeanUtil.convert(request.getList(), Department.class);
		departmentService.add(list);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse<DepartmentModifyResponse> modifyDepartmentName(@RequestBody @Valid DepartmentModifyRequest departmentModifyRequest) {
		Department department = KsBeanUtil.convert(departmentModifyRequest, Department.class);
		return BaseResponse.success(new DepartmentModifyResponse(
				departmentService.wrapperVo(departmentService.modifyDepartmentName(department))));
	}

	@Override
	public BaseResponse sort(@Valid DepartmentSortRequest sortRequest) {
		List<DepartmentSortDTO> list = sortRequest.getList();
		final Integer sourceIndex = sortRequest.getSourceIndex();
		final Integer targetIndex = sortRequest.getTargetIndex();
		swap2(list,sourceIndex,targetIndex);
		departmentService.add(convertDepartment(list));
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid DepartmentDelByIdRequest departmentDelByIdRequest) {
		departmentService.deleteById(departmentDelByIdRequest.getDepartmentId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modifyLeader(@Valid DepartmentModifyLeaderRequest request) {
		departmentService.modifyLeader(request.getOldEmployeeId(),request.getNewEmployeeId(),request.getDepartmentId());
		return BaseResponse.SUCCESSFUL();
	}

	private List<Department>  convertDepartment(List<DepartmentSortDTO> list){
		List<Department> departmentList = new ArrayList<>(list.size());
		Department department;
		Integer departmentSort = 1;
		for (DepartmentSortDTO departmentSortDTO : list){
			department = KsBeanUtil.convert(departmentSortDTO,Department.class);
			department.setDepartmentSort(departmentSort);
			departmentList.add(department);
			departmentSort++;
		}
		return departmentList;
	}

	/**
	 * 调换集合中两个指定位置的元素, 若两个元素位置中间还有其他元素，需要实现中间元素的前移或后移的操作。
	 * @param list 集合
	 * @param oldPosition 需要调换的元素
	 * @param newPosition 被调换的元素
	 * @param <T>
	 */
	private static <T> void swap2(List<T> list, int oldPosition, int newPosition){
		if(null == list){
			throw new IllegalStateException("The list can not be empty...");
		}

		// 向前移动，前面的元素需要向后移动
		if(oldPosition < newPosition){
			for(int i = oldPosition; i < newPosition; i++){
				Collections.swap(list, i, i + 1);
			}
		}

		// 向后移动，后面的元素需要向前移动
		if(oldPosition > newPosition){
			for(int i = oldPosition; i > newPosition; i--){
				Collections.swap(list, i, i - 1);
			}
		}
	}

}

