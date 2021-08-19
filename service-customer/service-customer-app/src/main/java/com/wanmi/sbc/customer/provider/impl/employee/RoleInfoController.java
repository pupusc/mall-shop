package com.wanmi.sbc.customer.provider.impl.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoAddRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoDeleteRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoModifyRequest;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoAddResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoModifyResponse;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.employee.model.root.RoleInfo;
import com.wanmi.sbc.customer.employee.service.EmployeeService;
import com.wanmi.sbc.customer.employee.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 14:22
 * @Version: 1.0
 */
@Validated
@RestController
public class RoleInfoController implements RoleInfoProvider {

    @Autowired
    private RoleInfoService roleInfoService;
    @Autowired
    private EmployeeService employeeService;
    /**
     * 新增角色信息
     * @param saveRequest
     * @return
     */

    @Override
    public BaseResponse<RoleInfoAddResponse> add(@RequestBody @Valid RoleInfoAddRequest saveRequest) {
        RoleInfo roleInfo = roleInfoService.saveRoleInfo(saveRequest).orElse(null);
        if (Objects.isNull(roleInfo)){
            return BaseResponse.SUCCESSFUL();
        }
        RoleInfoAddResponse roleInfoAddResponse = new RoleInfoAddResponse();
        KsBeanUtil.copyPropertiesThird(roleInfo,roleInfoAddResponse);
        return BaseResponse.success(roleInfoAddResponse);
    }

    /**
     * 修改角色信息
     * @param editRequest
     * @return
     */

    @Override
    public BaseResponse<RoleInfoModifyResponse> modify(@RequestBody @Valid RoleInfoModifyRequest editRequest) {
        RoleInfo roleInfo = roleInfoService.editRoleInfo(editRequest).orElse(null);
        if (Objects.isNull(roleInfo)){
            return BaseResponse.SUCCESSFUL();
        }
        RoleInfoModifyResponse roleInfoModifyResponse = new RoleInfoModifyResponse();
        KsBeanUtil.copyPropertiesThird(roleInfo,roleInfoModifyResponse);
        return BaseResponse.success(roleInfoModifyResponse);
    }

    @Override
    public BaseResponse delete(@Valid RoleInfoDeleteRequest request) {
        List<String> roleList  = new ArrayList<>();
        RoleInfo roleInfo =
                roleInfoService.getRoleInfoById(request.getRoleInfoId())
                        .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "角色不存在"));
        EmployeeListRequest employeeListRequest = new EmployeeListRequest();
        employeeListRequest.setCompanyInfoId(roleInfo.getCompanyInfoId());
        roleList.add(roleInfo.getRoleInfoId().toString());
        employeeListRequest.setRoleIds(roleList);
        List<Employee> employeeList = employeeService.findByCiretira(employeeListRequest);
        if (!CollectionUtils.isEmpty(employeeList)){
            throw new SbcRuntimeException("K-040032");
        }
        int result = roleInfoService.delete(request.getRoleInfoId());
        return BaseResponse.success(result);
    }
}
