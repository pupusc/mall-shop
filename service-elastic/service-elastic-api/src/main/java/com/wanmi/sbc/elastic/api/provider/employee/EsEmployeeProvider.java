package com.wanmi.sbc.elastic.api.provider.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.employee.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.elastic.name}", contextId = "EsEmployeeProvider")
public interface EsEmployeeProvider {

    /**
     * Es新增员工
     *
     * @param esEmployeeSaveRequest {@link EsEmployeeSaveRequest}
     * @return Es新增员工{@link EsEmployeePageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/add")
    BaseResponse save(@RequestBody @Valid EsEmployeeSaveRequest esEmployeeSaveRequest);


    /**
     * Es批量启用员工
     *
     * @param esEmployeeBatchEnableByIdsRequest {@link EsEmployeeBatchEnableByIdsRequest}
     * @return Es批量启用员工{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/batchEnableByIds")
    BaseResponse batchEnableByIds(@RequestBody @Valid EsEmployeeBatchEnableByIdsRequest esEmployeeBatchEnableByIdsRequest);


    /**
     * Es禁用员工
     *
     * @param esEmployeeDisableByIdRequest {@link EsEmployeeDisableByIdRequest}
     * @return Es禁用员工{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/disableById")
    BaseResponse disableById(@RequestBody @Valid EsEmployeeDisableByIdRequest esEmployeeDisableByIdRequest);


    /**
     * Es批量禁用员工
     *
     * @param esEmployeeBatchDisableByIdsRequest {@link EsEmployeeBatchDisableByIdsRequest}
     * @return Es批量禁用员工{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/batchDisableByIds")
    BaseResponse batchDisableByIds(@RequestBody @Valid EsEmployeeBatchDisableByIdsRequest esEmployeeBatchDisableByIdsRequest);



    /**
     * Es批量删除员工
     *
     * @param esEmployeeBatchDeleteByIdsRequest {@link EsEmployeeBatchDeleteByIdsRequest}
     * @return Es批量删除员工{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/batchDeleteByIds")
    BaseResponse batchDeleteByIds(@RequestBody @Valid EsEmployeeBatchDeleteByIdsRequest esEmployeeBatchDeleteByIdsRequest);


    /**
     * Es批量更改部门
     *
     * @param eSEmployeeChangeDepartmentRequest {@link EsEmployeeChangeDepartmentRequest}
     * @return Es批量更改部门{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/changeDepartment")
    BaseResponse changeDepartment(@RequestBody @Valid EsEmployeeChangeDepartmentRequest eSEmployeeChangeDepartmentRequest);


    /**
     * Es批量设为业务员
     *
     * @param esEmployeeListByIdsRequest {@link EsEmployeeListByIdsRequest}
     * @return Es批量设为业务员{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/batchSetEmployeeByIds")
    BaseResponse batchSetEmployeeByIds(@RequestBody @Valid EsEmployeeListByIdsRequest esEmployeeListByIdsRequest);


    /**
     * Es批量设为离职
     *
     * @param esEmployeeBatchDimissionByIdsRequest {@link EsEmployeeBatchDimissionByIdsRequest}
     * @return Es批量设为离职{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/batchDimissionByIds")
    BaseResponse batchDimissionByIds(@RequestBody @Valid EsEmployeeBatchDimissionByIdsRequest esEmployeeBatchDimissionByIdsRequest);

    /**
     * Es批量激活会员
     *
     * @param esEsEmployeeActivateAccountRequest {@link EsEmployeeActivateAccountRequest}
     * @return Es批量激活会员{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/activateAccount")
    BaseResponse activateAccount(@RequestBody @Valid EsEmployeeActivateAccountRequest esEsEmployeeActivateAccountRequest);


    /**
     * Es批量业务员交接
     *
     * @param esEmployeeHandoverRequest {@link EsEmployeeHandoverRequest}
     * @return Es批量业务员交接{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/handoverEmployee")
    BaseResponse handoverEmployee(@RequestBody @Valid EsEmployeeHandoverRequest esEmployeeHandoverRequest);

    /**
     * Es批量导入
     *
     * @param esEmployeeImportRequest {@link EsEmployeeImportRequest}
     * @return Es批量业务员交接{@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/esEmployee/importEmployee")
    BaseResponse importEmployee(@RequestBody @Valid EsEmployeeImportRequest esEmployeeImportRequest);

}
