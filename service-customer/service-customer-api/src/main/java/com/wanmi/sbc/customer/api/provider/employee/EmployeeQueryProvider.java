package com.wanmi.sbc.customer.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@FeignClient(value = "${application.customer.name}", contextId = "EmployeeQueryProvider")
public interface EmployeeQueryProvider {

    /**
     * 账户名是否存在
     *
     * @param employeeAccountNameExistsRequest {@link EmployeeAccountNameExistsRequest}
     * @return 账户是否存在 {@link EmployeeAccountNameExistsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/account-name-is-exists")
    BaseResponse<EmployeeAccountNameExistsResponse> accountNameIsExists(@RequestBody @Valid
                                                                                EmployeeAccountNameExistsRequest
                                                                                employeeAccountNameExistsRequest);

    /**
     * 手机号是否存在
     *
     * @param employeeMobileExistsRequest {@link EmployeeMobileExistsRequest}
     * @return 手机是否存在 {@link EmployeeMobileExistsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/mobile-is-exists")
    BaseResponse<EmployeeMobileExistsResponse> mobileIsExists(@RequestBody @Valid EmployeeMobileExistsRequest
                                                                      employeeMobileExistsRequest);

    /**
     * 根据员工姓名判断员工是否存在
     *
     * @param employeeNameExistsRequest {@link EmployeeNameExistsRequest}
     * @return 员工是否存在 {@link EmployeeNameExistsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/name-is-exists")
    BaseResponse<EmployeeNameExistsResponse> nameIsExists(@RequestBody @Valid EmployeeNameExistsRequest
                                                                  employeeNameExistsRequest);

    /**
     * 根据手机号查询
     *
     * @param employeeByMobileRequest {@link EmployeeByMobileRequest}
     * @return 员工是否存在 {@link EmployeeByMobileResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-by-mobile")
    BaseResponse<EmployeeByMobileResponse> getByMobile(@RequestBody @Valid
                                                               EmployeeByMobileRequest employeeByMobileRequest);

    /**
     * 分页查询
     *
     * @param employeePageRequest {@link EmployeePageRequest}
     * @return 员工列表信息（带分页）{@link EmployeePageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/page")
    BaseResponse<EmployeePageResponse> page(@RequestBody @Valid EmployeePageRequest employeePageRequest);

    /**
     * 分页查询List
     *
     * @param employeePageRequest {@link EmployeePageRequest}
     * @return 员工列表信息（带分页）{@link EmployeeListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/pageList")
    BaseResponse<EmployeePageListResponse> pageList(@RequestBody @Valid EmployeePageRequest employeePageRequest);
    /**
     * 查询列表
     *
     * @param employeeListRequest {@link EmployeeListRequest}
     * @return 员工列表信息（不带分页） {@link EmployeeListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list")
    BaseResponse<EmployeeListResponse> list(@RequestBody @Valid EmployeeListRequest employeeListRequest);

    /**
     * 根据编号查询
     *
     * @param employeeOptionalByIdRequest {@link EmployeeOptionalByIdRequest}
     * @return 员工信息 {@link EmployeeOptionalByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-optional-by-id")
    BaseResponse<EmployeeOptionalByIdResponse> getOptionalById(@RequestBody @Valid EmployeeOptionalByIdRequest
                                                                       employeeOptionalByIdRequest);


    /**
     * 根据编号查询
     *
     * @param employeeByIdRequest {@link EmployeeByIdRequest}
     * @return 员工信息 {@link EmployeeByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-by-id")
    BaseResponse<EmployeeByIdResponse> getById(@RequestBody @Valid EmployeeByIdRequest employeeByIdRequest);

    /**
     * 根据账户类型查询
     *
     * @param employeeListByAccountTypeRequest {@link EmployeeListByAccountTypeRequest}
     * @return 员工信息 {@link EmployeeListByAccountTypeResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list-by-account-type")
    BaseResponse<EmployeeListByAccountTypeResponse> listByAccountType(@RequestBody @Valid
                                                                              EmployeeListByAccountTypeRequest
                                                                              employeeListByAccountTypeRequest);

    /**
     * 根据商家编号查询
     *
     * @param employeeListByCompanyIdRequest {@link EmployeeListByCompanyIdRequest}
     * @return 员工信息 {@link EmployeeListByCompanyIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list-by-company-id")
    BaseResponse<EmployeeListByCompanyIdResponse> listByCompanyId(@RequestBody @Valid EmployeeListByCompanyIdRequest
                                                                          employeeListByCompanyIdRequest);


    /**
     * 根据编号批量查询
     *
     * @param employeeListByIdsRequest {@link EmployeeListByIdsRequest}
     * @return 员工列表信息 {@link EmployeeListByIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list-by-ids")
    BaseResponse<EmployeeListByIdsResponse> listByIds(@RequestBody @Valid EmployeeListByIdsRequest
                                                              employeeListByIdsRequest);

    /**
     * 根据账号名称查询
     *
     * @param employeeByAccountNameRequest {@link EmployeeByAccountNameRequest}
     * @return 员工信息 {@link EmployeeByAccountNameResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-by-account-name")
    BaseResponse<EmployeeByAccountNameResponse> getByAccountName(
            @RequestBody @Valid EmployeeByAccountNameRequest employeeByAccountNameRequest);

    /**
     * 根据账号编号查询
     *
     * @param employeeAccountByIdRequest {@link EmployeeAccountByIdRequest}
     * @return 员工信息 {@link EmployeeAccountByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/account/get-by-id")
    BaseResponse<EmployeeAccountByIdResponse> getAccountById(@RequestBody @Valid EmployeeAccountByIdRequest
                                                                     employeeAccountByIdRequest);

    /**
     * 根据商家编号查询
     *
     * @param employeeByCompanyIdRequest {@link EmployeeByCompanyIdRequest}
     * @return 员工信息 {@link EmployeeByCompanyIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-by-company-id")
    BaseResponse<EmployeeByCompanyIdResponse> getByCompanyId(@RequestBody @Valid EmployeeByCompanyIdRequest
                                                                     employeeByCompanyIdRequest);

    /**
     * 手机号是否可以发送验证码
     *
     * @param employeeMobileSmsRequest {@link EmployeeMobileSmsRequest}
     * @return 短信是否可以发送 {@link EmployeeMobileSmsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/mobile-is-sms")
    BaseResponse<EmployeeMobileSmsResponse> mobileIsSms(@RequestBody @Valid EmployeeMobileSmsRequest
                                                                employeeMobileSmsRequest);

    /**
     * 根据员工编号获取角色编号
     *
     * @param roleIdByEmployeeIdRequest {@link RoleIdByEmployeeIdRequest}
     * @return 角色编号 {@link RoleIdByEmployeeIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/get-role-id-by-employee-id")
    BaseResponse<RoleIdByEmployeeIdResponse> getRoleIdByEmployeeId(@RequestBody @Valid RoleIdByEmployeeIdRequest
                                                                           roleIdByEmployeeIdRequest);

    /**
     * 分页查询业务员
     *
     * @param employeePageRequest {@link EmployeePageRequest}
     * @return 角色编号 {@link RoleIdByEmployeeIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/page-for-employee")
    BaseResponse<EmployeeByNameResponse> pageForEmployee(@RequestBody @Valid EmployeePageRequest
                                                                           employeePageRequest);

    /**
     * 根据管理部门查询员工ID列表
     *
     * @param request {@link EmployeeListByManageDepartmentIdsRequest}
     * @return 员工列表信息（不带分页） {@link EmployeeListByManageDepartmentIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list-by-manage-department-ids")
    BaseResponse<EmployeeListByManageDepartmentIdsResponse> listByManageDepartmentIds(@RequestBody @Valid EmployeeListByManageDepartmentIdsRequest request);

    /**
     * 统计部门人数
     *
     * @param request {@link EmployeeNumRequest}
     * @return 总数 {@link EmployeeNumResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/count-employee-num")
    BaseResponse<EmployeeNumResponse> countEmployeeNum(@RequestBody @Valid EmployeeNumRequest request);

    /**
     * 工号是否存在
     *
     * @param request {@link EmployeeJobNoExistsRequest}
     * @return 是否存在 {@link EmployeeJobNoExistsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/job-no-exists")
    BaseResponse<EmployeeJobNoExistsResponse> jobNoIsExist(@RequestBody @Valid EmployeeJobNoExistsRequest request);

    /**
     * 根据交接人ID查询员工ID集合
     *
     * @param request {@link EmployeeListByHeirEmployeeIdRequest}
     * @return 员工ID集合 {@link EmployeeListByHeirEmployeeIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/list-by-heir-employee-id")
    BaseResponse<EmployeeListByHeirEmployeeIdResponse> listByHeirEmployeeId(@RequestBody @Valid EmployeeListByHeirEmployeeIdRequest request);
}
