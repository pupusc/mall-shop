package com.wanmi.sbc.customer.provider.impl.department;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.department.DepartmentQueryProvider;
import com.wanmi.sbc.customer.api.request.department.*;
import com.wanmi.sbc.customer.api.response.department.*;
import com.wanmi.sbc.customer.bean.vo.DepartmentTreeVO;
import com.wanmi.sbc.customer.bean.vo.DepartmentVO;
import com.wanmi.sbc.customer.department.model.root.Department;
import com.wanmi.sbc.customer.department.service.DepartmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>部门管理查询服务接口实现</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@RestController
@Validated
public class DepartmentQueryController implements DepartmentQueryProvider {
	@Autowired
	private DepartmentService departmentService;

	@Override
	public BaseResponse<DepartmentPageResponse> page(@RequestBody @Valid DepartmentPageRequest departmentPageReq) {
		DepartmentQueryRequest queryReq = KsBeanUtil.convert(departmentPageReq, DepartmentQueryRequest.class);
		Page<Department> departmentPage = departmentService.page(queryReq);
		Page<DepartmentVO> newPage = departmentPage.map(entity -> departmentService.wrapperVo(entity));
		MicroServicePage<DepartmentVO> microPage = new MicroServicePage<>(newPage, departmentPageReq.getPageable());
		DepartmentPageResponse finalRes = new DepartmentPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<DepartmentListResponse> listDepartmentTree(@RequestBody @Valid DepartmentListRequest departmentListReq) {
		DepartmentQueryRequest queryReq = KsBeanUtil.convert(departmentListReq, DepartmentQueryRequest.class);
		List<Department> departmentList = departmentService.list(queryReq);
		if (CollectionUtils.isEmpty(departmentList)){
			return BaseResponse.success(new DepartmentListResponse(Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST, departmentListReq.getIsMaster()));
		}
		List<DepartmentVO> departmentVOS = KsBeanUtil.convert(departmentList,DepartmentVO.class);
		List<DepartmentTreeVO> departmentTreeVOS = KsBeanUtil.convert(departmentList,DepartmentTreeVO.class);
		departmentTreeVOS = departmentService.listToTree(departmentTreeVOS);
        List<String> manageDepartmentIdList = Collections.EMPTY_LIST;
		if (StringUtils.isNotBlank(departmentListReq.getManageDepartmentIds())) {
            manageDepartmentIdList = departmentService.findByManageDepartmentIds(departmentListReq.getManageDepartmentIds()).stream().collect(Collectors.toList());
        }
		return BaseResponse.success(new DepartmentListResponse(departmentVOS,departmentTreeVOS,manageDepartmentIdList,departmentListReq.getIsMaster()));
	}

	@Override
	public BaseResponse<DepartmentListByManageDepartmentIdsResponse> ListByManageDepartmentIds(@Valid DepartmentListByManageDepartmentIdsRequest request) {
		List<String> list = departmentService.findByManageDepartmentIds(request.getManageDepartmentIds()).stream().collect(Collectors.toList());
		return BaseResponse.success(new DepartmentListByManageDepartmentIdsResponse(list));
	}

	@Override
	public BaseResponse<DepartmentListAllByManageDepartmentIdsResponse> ListAllByManageDepartmentIds(@Valid DepartmentListAllByManageDepartmentIdsRequest request) {
		List<String> list = departmentService.findAllByManageDepartmentIds(request.getManageDepartmentIds());
		return BaseResponse.success(new DepartmentListAllByManageDepartmentIdsResponse(list));
	}

	@Override
	public BaseResponse<DepartmentListByBelongToDepartmentIdsResponse> listByBelongToDepartmentIds(@Valid DepartmentListByBelongToDepartmentIdsRequest request) {
		List<String> list = departmentService.findByBelongToDepartmentIds(request.getBelongToDepartmentIds());
		return BaseResponse.success(new DepartmentListByBelongToDepartmentIdsResponse(list));
	}

	@Override
	public BaseResponse<DepartmentExcelExportTemplateResponse> exportTemplate() {
		String file = departmentService.exportTemplate();
		return BaseResponse.success(new DepartmentExcelExportTemplateResponse(file));
	}

	@Override
	public BaseResponse<DepartmentListResponse> listDepartment(@RequestBody @Valid DepartmentListRequest departmentListReq) {
		DepartmentQueryRequest queryReq = KsBeanUtil.convert(departmentListReq, DepartmentQueryRequest.class);
		List<Department> departmentList = departmentService.list(queryReq);
		if (CollectionUtils.isEmpty(departmentList)){
			return BaseResponse.success(new DepartmentListResponse(Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST,departmentListReq.getIsMaster()));
		}
		List<DepartmentVO> departmentVOS = KsBeanUtil.convert(departmentList,DepartmentVO.class);
		DepartmentListResponse response = DepartmentListResponse.builder().departmentVOS(departmentVOS).build();
		return BaseResponse.success(response);
	}

}

