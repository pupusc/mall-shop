package com.wanmi.sbc.customer.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "${application.customer.name}")
public interface EmployeeProvider {

    /**
     * 保存员工信息
     *
     * @param employeeSaveRequest {@link EmployeeAddRequest}
     * @return 新增结果 {@link EmployeeAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/add")
    BaseResponse<EmployeeAddResponse> add(@RequestBody @Valid EmployeeAddRequest employeeSaveRequest);


    /**
     * 修改员工信息
     *
     * @param employeeModifyRequest {@link EmployeeModifyRequest}
     * @return 修改结果 {@link EmployeeModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify")
    BaseResponse<EmployeeModifyResponse> modify(@RequestBody @Valid EmployeeModifyRequest employeeModifyRequest);

    /**
     * 修改员工所有信息
     *
     * @param employeeModifyAllRequest {@link EmployeeModifyAllRequest}
     * @return 修改结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-all-by-id")
    BaseResponse modifyAllById(@RequestBody @Valid EmployeeModifyAllRequest employeeModifyAllRequest);

    /**
     * 修改员工姓名
     *
     * @param employeeNameModifyByIdRequest {@link EmployeeNameModifyByIdRequest}
     * @return 修改员工姓名结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-name-by-id")
    BaseResponse modifyNameById(@RequestBody @Valid EmployeeNameModifyByIdRequest employeeNameModifyByIdRequest);

    /**
     * 修改员工手机号
     *
     * @param employeeMobileModifyByIdRequest {@link EmployeeMobileModifyByIdRequest}
     * @return 修改员工手机号结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-mobile-by-id")
    BaseResponse modifyMobileById(@RequestBody @Valid EmployeeMobileModifyByIdRequest employeeMobileModifyByIdRequest);

    /**
     * 修改员工登录时间
     *
     * @param employeeLoginTimeModifyByIdRequest {@link EmployeeLoginTimeModifyByIdRequest}
     * @return 修改员工登录时间结果 {@link EmployeeLoginTimeModifyResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-login-time-by-id")
    BaseResponse<EmployeeLoginTimeModifyResponse> modifyLoginTimeById(@RequestBody @Valid
                                                                              EmployeeLoginTimeModifyByIdRequest employeeLoginTimeModifyByIdRequest);

    /**
     * 解锁员工
     *
     * @param employeeUnlockByIdRequest {@link EmployeeUnlockByIdRequest}
     * @return 解锁员工结果 {@link EmployeeUnlockResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/unlock-by-id")
    BaseResponse<EmployeeUnlockResponse> unlockById(@RequestBody @Valid EmployeeUnlockByIdRequest employeeUnlockByIdRequest);

    /**
     * 批量启用员工
     *
     * @param employeeBatchEnableByIdsRequest {@link EmployeeBatchEnableByIdsRequest}
     * @return 批量启用员工结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/batch-enable-by-ids")
    BaseResponse batchEnableByIds(@RequestBody @Valid EmployeeBatchEnableByIdsRequest employeeBatchEnableByIdsRequest);


    /**
     * 禁用员工
     *
     * @param employeeDisableByIdRequest {@link EmployeeDisableByIdRequest}
     * @return 禁用员工结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/disable-by-id")
    BaseResponse disableById(@RequestBody @Valid EmployeeDisableByIdRequest employeeDisableByIdRequest);

    /**
     * 账号启用/禁用
     *
     * @param employeeDisableOrEnableByCompanyIdRequest {@link EmployeeDisableOrEnableByCompanyIdRequest}
     * @return 账号启用/禁用结果 {@link EmployeeDisableOrEnableByCompanyIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/disable-or-enable-by-company-id")
    BaseResponse<EmployeeDisableOrEnableByCompanyIdResponse> disableOrEnableByCompanyId(@RequestBody @Valid
                                                                                                EmployeeDisableOrEnableByCompanyIdRequest employeeDisableOrEnableByCompanyIdRequest);

    /**
     * 批量禁用员工
     *
     * @param employeeBatchDisableByIdsRequest {@link EmployeeBatchDisableByIdsRequest}
     * @return 批量禁用员工结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/batch-disable-by-ids")
    BaseResponse batchDisableByIds(@RequestBody @Valid EmployeeBatchDisableByIdsRequest employeeBatchDisableByIdsRequest);

    /**
     * 批量删除员工
     *
     * @param employeeBatchDeleteByIdsRequest {@link EmployeeBatchDeleteByIdsRequest}
     * @return 批量删除员工结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/batch-delete-by-ids")
    BaseResponse batchDeleteByIds(@RequestBody @Valid EmployeeBatchDeleteByIdsRequest employeeBatchDeleteByIdsRequest);

    /**
     * 修改员工登录错误次数
     *
     * @param employeeLoginErrorCountModifyByIdRequest {@link EmployeeLoginErrorCountModifyByIdRequest}
     * @return 修改员工登录错误次数结果 {@link EmployeeLoginErrorCountModifyByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-login-error-count-by-id")
    BaseResponse<EmployeeLoginErrorCountModifyByIdResponse> modifyLoginErrorCountById(
            @RequestBody @Valid EmployeeLoginErrorCountModifyByIdRequest employeeLoginErrorCountModifyByIdRequest);

    /**
     * 修改员工登录锁定时间
     *
     * @param employeeLoginLockTimeModifyByIdRequest {@link EmployeeLoginLockTimeModifyByIdRequest}
     * @return 修改员工登录锁定时间结果 {@link EmployeeLoginLockTimeModifyByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-login-lock-time-by-id")
    BaseResponse<EmployeeLoginLockTimeModifyByIdResponse> modifyLoginLockTimeById(@RequestBody @Valid
                                                                                          EmployeeLoginLockTimeModifyByIdRequest employeeLoginLockTimeModifyByIdRequest);

    /**
     * 修改员工密码
     *
     * @param employeePasswordModifyByIdRequest {@link EmployeePasswordModifyByIdRequest}
     * @return 修改员工密码结果 {@link EmployeePasswordModifyByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/modify-password-by-id")
    BaseResponse<EmployeePasswordModifyByIdResponse> modifyPasswordById(@RequestBody @Valid
                                                                                EmployeePasswordModifyByIdRequest employeePasswordModifyByIdRequest);

    /**
     * 注册
     *
     * @param employeeRegisterRequest {@link EmployeeRegisterRequest}
     * @return 注册结果 {@link EmployeeRegisterResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/register")
    BaseResponse<EmployeeRegisterResponse> register(@RequestBody @Valid EmployeeRegisterRequest employeeRegisterRequest);

    /**
     * 注册
     *
     * @param employeeRegisterRequest {@link EmployeeRegisterRequest}
     * @return 注册结果 {@link StoreInformationResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/register-get-store-info")
    BaseResponse<StoreInformationResponse> registerGetStoreInfo(@RequestBody @Valid EmployeeRegisterRequest employeeRegisterRequest);


    /**
     * 发送验证码
     *
     * @param employeeSmsRequest {@link EmployeeSmsRequest}
     * @return 验证码发送结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/sms")
    BaseResponse sms(@RequestBody @Valid EmployeeSmsRequest employeeSmsRequest);

    /**
     * 批量离职员工
     *
     * @param employeeBatchDimissionByIdsRequest {@link EmployeeBatchDimissionByIdsRequest}
     * @return 批量离职员工结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/batch-dimission-by-ids")
    BaseResponse batchDimissionByIds(@RequestBody @Valid EmployeeBatchDimissionByIdsRequest employeeBatchDimissionByIdsRequest);

    /**
     * 批量设为业务员
     *
     * @param request {@link EmployeeListByIdsRequest}
     * @return 批量设为业务员结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/batch-set-employee-by-ids")
    BaseResponse<Integer> batchSetEmployeeByIds(@RequestBody @Valid EmployeeListByIdsRequest request);

    /**
     * 批量调整部门
     *
     * @param request {@link EmployeeListByIdsRequest}
     * @return 批量调整部门结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/change-department")
    BaseResponse changeDepartment(@RequestBody @Valid EmployeeChangeDepartmentRequest request);

    /**
     * 业务员交接
     *
     * @param request {@link EmployeeHandoverRequest}
     * @return 业务员交接结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/handover-employee")
    BaseResponse<Integer> handoverEmployee(@RequestBody @Valid EmployeeHandoverRequest request);

    /**
     * 会员账户激活
     *
     * @param request {@link EmployeeHandoverRequest}
     * @return 会员账户激活结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/activate-account")
    BaseResponse<Integer> activateAccount(@RequestBody @Valid EmployeeActivateAccountRequest request);

    /**
     * 下载员工导入模版
     *
     * @param
     * @return 下载员工导入模版结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/export-template")
    BaseResponse<EmployeeExcelExportTemplateResponse> exportTemplate();

    /**
     * 批量导入
     *
     * @param
     * @return 批量导入结果 {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/employee/import-employee")
    BaseResponse<List<EmployeeImportResponse>> importEmployee(@RequestBody @Valid EmployeeImportRequest request);
}
