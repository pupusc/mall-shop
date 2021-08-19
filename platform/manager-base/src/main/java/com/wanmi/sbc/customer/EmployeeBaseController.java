package com.wanmi.sbc.customer;

import com.wanmi.sbc.base.verifycode.VerifyCodeService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.*;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.customer.request.EmployeeExcelImportRequest;
import com.wanmi.sbc.customer.service.EmployeeExcelService;
import com.wanmi.sbc.customer.validator.EmployeeEditValidator;
import com.wanmi.sbc.department.request.DepartmentExcelImportRequest;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeProvider;
import com.wanmi.sbc.elastic.api.request.employee.*;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.bean.enums.VerifyType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


/**
 * 员工bff
 * Created by zhangjin on 2017/4/18.
 */
@Api(tags = "EmployeeBaseController", description = "员工 Api")
@RestController
@RequestMapping("/customer")
public class EmployeeBaseController {

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;

    @Autowired
    private EmployeeEditValidator employeeEditValidator;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private RoleMenuQueryProvider roleMenuQueryProvider;

    @Autowired
    private EmployeeExcelService employeeExcelService;

    @Autowired
    private EsEmployeeProvider esEmployeeProvider;

    @InitBinder
    public void initBinder(DataBinder binder) {
        if (binder.getTarget() instanceof EmployeeModifyRequest) {
            binder.setValidator(employeeEditValidator);
        }
    }

    /**
     * //     * 新增员工
     * //     *
     * //     * @param employeeSaveRequest employeeSaveRequest
     * //     * @return employee
     * //
     */
    @ApiOperation(value = "新增员工")
    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    public BaseResponse<EmployeeModifyResponse> update(@RequestBody EmployeeModifyRequest employeeSaveRequest) {

        employeeSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        String opContext = "编辑员工：" + employeeSaveRequest.getEmployeeName();
        if (nonNull(claims)) {
            String platform = Objects.toString(claims.get("platform"), "");
            if (Platform.SUPPLIER.toValue().equals(platform)) {
                opContext = "编辑员工：员工账户名 " + employeeSaveRequest.getEmployeeName();
            }
        }
        operateLogMQUtil.convertAndSend("设置", "编辑员工", opContext);
        BaseResponse<EmployeeModifyResponse> modifyEmployee = employeeProvider.modify(employeeSaveRequest);
        EmployeeModifyResponse employeeModifyResponse = modifyEmployee.getContext();
        EsEmployeeSaveRequest esEmployeeSaveRequest = KsBeanUtil.convert(employeeModifyResponse, EsEmployeeSaveRequest.class);
        esEmployeeProvider.save(esEmployeeSaveRequest);
        return modifyEmployee;
    }

    /**
     * 查询会员
     *
     * @param employeeId employeeId
     * @return ResponseEntity<Employee>
     */
    @ApiOperation(value = "查询会员")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "employeeId", value = "员工Id", required = true)
    @RequestMapping(value = "/employee/info/{employeeId}")
    public ResponseEntity<EmployeeByIdResponse> findByEmployeeId(@PathVariable("employeeId") String employeeId) {
        return ResponseEntity.ok(Optional.ofNullable(employeeQueryProvider.getById(EmployeeByIdRequest.builder()
                .employeeId(employeeId).build()).getContext()).orElseThrow(() ->
                new SbcRuntimeException("K-040001")));
    }

    /**
     * 启用员工
     *
     * @param employeeRequest employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "启用员工")
    @RequestMapping(value = "/employee/enable", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> enableEmployees(@RequestBody EmployeeRequest employeeRequest) {

        List<String> employeeIds = employeeRequest.getEmployeeIds();

        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");

        //操作日志记录
        if (1 == CollectionUtils.size(employeeIds)) {
            EmployeeByIdResponse employee = employeeQueryProvider.getById(EmployeeByIdRequest.builder()
                    .employeeId(employeeIds.get(0)).build()).getContext();
            if (nonNull(employee)) {
                String opContext = "启用员工：" + employee.getAccountName();
                if (nonNull(claims)) {
                    String platform = Objects.toString(claims.get("platform"), "");
                    if (Platform.SUPPLIER.toValue().equals(platform)) {
                        opContext = "启用员工：员工账户名 " + employee.getAccountName();
                    }
                }
                operateLogMQUtil.convertAndSend("设置", "启用员工", opContext);
            }
        } else {
            operateLogMQUtil.convertAndSend("设置", "批量启用", "批量启用");
        }
        employeeProvider.batchEnableByIds(EmployeeBatchEnableByIdsRequest.builder()
                .employeeIds(employeeRequest.getEmployeeIds()).build());

        return ResponseEntity.ok(esEmployeeProvider.
                batchEnableByIds(EsEmployeeBatchEnableByIdsRequest.builder().employeeIds(employeeRequest.getEmployeeIds())
                        .build()));
    }

    /**
     * 禁用员工
     *
     * @param employeeRequest employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "禁用员工")
    @RequestMapping(value = "/employee/disable", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> disableEmployees(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeDisableByIdRequest request = new EmployeeDisableByIdRequest();
        KsBeanUtil.copyPropertiesThird(employeeRequest, request);

        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");

        EmployeeByIdResponse employee = employeeQueryProvider.getById(EmployeeByIdRequest.builder()
                .employeeId(employeeRequest.getEmployeeId()).build()).getContext();
        if (nonNull(employee)) {
            String opContext = "禁用员工：" + employee.getAccountName();
            if (nonNull(claims)) {
                String platform = Objects.toString(claims.get("platform"), "");
                if (Platform.SUPPLIER.toValue().equals(platform)) {
                    opContext = "禁用员工：员工账户名 " + employee.getAccountName();
                }
            }
            operateLogMQUtil.convertAndSend("设置", "禁用员工", opContext);
        }
        employeeProvider.disableById(request);
        EsEmployeeDisableByIdRequest esEmployeeDisableByIdRequest = new EsEmployeeDisableByIdRequest();
        KsBeanUtil.copyPropertiesThird(request, esEmployeeDisableByIdRequest);
        return ResponseEntity.ok(esEmployeeProvider.disableById(esEmployeeDisableByIdRequest));
    }

    /**
     * 批量禁用员工
     *
     * @param employeeRequest employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "批量禁用员工")
    @RequestMapping(value = "/employee/batch/disable", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> batchDisableEmployees(@RequestBody EmployeeRequest employeeRequest) {
        EmployeeBatchDisableByIdsRequest idsRequest = new EmployeeBatchDisableByIdsRequest();
        KsBeanUtil.copyPropertiesThird(employeeRequest, idsRequest);
        operateLogMQUtil.convertAndSend("设置", "批量禁用", "批量禁用");
        employeeProvider.batchDisableByIds(idsRequest);
        EsEmployeeBatchDisableByIdsRequest request = new EsEmployeeBatchDisableByIdsRequest();
        KsBeanUtil.copyPropertiesThird(idsRequest, request);
        return ResponseEntity.ok(esEmployeeProvider.batchDisableByIds(request));
    }

    /**
     * 查询所有角色
     *
     * @return ResponseEntity<List < RoleInfo>>
     */
    @ApiOperation(value = "查询所有角色")
    @RequestMapping(value = "/employee/roles", method = RequestMethod.GET)
    public ResponseEntity<List<RoleInfoVO>> findAllRoles() {
        RoleInfoListRequest roleInfoListRequest = new RoleInfoListRequest();
        roleInfoListRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        BaseResponse<RoleInfoListResponse> roleInfoListResponseBaseResponse =
                roleInfoQueryProvider.listByCompanyInfoId(roleInfoListRequest);
        RoleInfoListResponse roleInfoListResponse = roleInfoListResponseBaseResponse.getContext();
        if (Objects.nonNull(roleInfoListResponse)) {
            return ResponseEntity.ok(roleInfoListResponse.getRoleInfoVOList());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


    /**
     * 查询员工
     *
     * @return ResponseEntity<EmployeeAccountResponse>
     */
    @ApiOperation(value = "查询员工")
    @RequestMapping(value = "/employee/info", method = RequestMethod.GET)
    public ResponseEntity<EmployeeAccountByIdResponse> findEmployee() {
        return ResponseEntity.ok(employeeQueryProvider.getAccountById(
                EmployeeAccountByIdRequest.builder().employeeId(commonUtil.getOperatorId()).build()
        ).getContext());
    }


    /**
     * 根据员工名称修改员工
     *
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "根据员工名称修改员工")
    @RequestMapping(value = "/employeeName", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> findEmployee(@RequestBody EmployeeModifyRequest employeeSaveRequest) {
        operateLogMQUtil.convertAndSend("账户管理", "账号管理", "修改员工姓名");
        return ResponseEntity.ok(employeeProvider.modifyNameById(
                EmployeeNameModifyByIdRequest.builder()
                        .employeeId(commonUtil.getOperatorId())
                        .employeeName(employeeSaveRequest.getEmployeeName())
                        .build()
        ));
    }


    /**
     * 校验手机验证码
     *
     * @param phone phone
     * @param code  code
     * @return BaseResponse
     */
    @ApiOperation(value = "校验手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "phone", value = "电话", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "code", value = "验证码", required = true)
    })
    @RequestMapping(value = "/sms/valid", method = RequestMethod.POST)
    public BaseResponse validSms(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @RequestParam("phone") String phone, String code) {
        return verifyCodeService.validateSmsVerifyCode(phone, code, VerifyType.EMPLOYEE_CHANGE_PHONE, 1,
                TimeUnit.DAYS) ?
                BaseResponse.SUCCESSFUL() : BaseResponse.FAILED();
    }

    /**
     * 绑定手机号
     *
     * @param phone phone
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "绑定手机号")
    @ApiImplicitParam(paramType = "query", dataType = "String", name = "phone", value = "电话", required = true)
    @RequestMapping(value = "/send/bind", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> bindUserPhone(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @RequestParam("phone") String phone, String code) {
        if(verifyCodeService.validateSmsVerifyCode(phone, code, VerifyType.EMPLOYEE_CHANGE_PHONE, 1, TimeUnit.DAYS)) {
            operateLogMQUtil.convertAndSend("账户管理", "账号管理", "修改绑定手机");
            if (verifyCodeService.validatePhoneCertificate(phone, VerifyType.EMPLOYEE_CHANGE_PHONE)) {
                return ResponseEntity.ok(employeeProvider.modifyMobileById(
                        EmployeeMobileModifyByIdRequest.builder()
                                .employeeId(commonUtil.getOperatorId())
                                .mobile(phone).build()
                ));
            } else {
                throw new SbcRuntimeException("K-040012");
            }
        } else {
            throw new SbcRuntimeException("K-000010");
        }
    }

    /**
     * 查询boss所有员工，用于商家端调用显示企业会员列表
     *
     * @return <List<EmployeeResponse>>
     */
    @ApiOperation(value = "查询boos所有员工")
    @RequestMapping(value = "/boss/allEmployees")
    public ResponseEntity<List<EmployeeResponse>> findAllEmployees() {
        return ResponseEntity.ok(
                employeeQueryProvider.listByAccountType(
                        EmployeeListByAccountTypeRequest.builder().accountType(AccountType.s2bBoss).build()
                ).getContext().getEmployeeList().stream().filter(Objects::nonNull).map(employee -> {
                    EmployeeResponse employeeResponse = new EmployeeResponse();
                    employeeResponse.setEmployeeId(employee.getEmployeeId());
                    employeeResponse.setEmployeeName(employee.getEmployeeName());
                    return employeeResponse;
                }).collect(Collectors.toList()));
    }

    /**
     * 查询员工自己的信息
     *
     * @return 员工信息
     */
    @ApiOperation(value = "查询员工自己的信息")
    @RequestMapping(value = "/employee/myself")
    public BaseResponse<EmployeeOptionalByIdResponse> findMyself() {
        return employeeQueryProvider.getOptionalById(EmployeeOptionalByIdRequest.builder()
                .employeeId(commonUtil.getOperatorId()).build());
    }
    /**
     * 批量离职员工
     *
     * @param request employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "批量离职员工")
    @RequestMapping(value = "/employee/batch/dimission", method = RequestMethod.POST)
    public BaseResponse batchDimissionEmployees(@RequestBody EmployeeBatchDimissionByIdsRequest request) {
        employeeProvider.batchDimissionByIds(request);
        esEmployeeProvider.batchDimissionByIds(KsBeanUtil.convert(request,EsEmployeeBatchDimissionByIdsRequest.class));
        operateLogMQUtil.convertAndSend("设置", "批量离职", "批量离职");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量设为业务员
     *
     * @param request employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "批量设为业务员")
    @RequestMapping(value = "/employee/batch/setEmployee", method = RequestMethod.POST)
    public BaseResponse batchSetEmployee(@RequestBody EmployeeListByIdsRequest request) {
        Integer num = employeeProvider.batchSetEmployeeByIds(request).getContext();
        esEmployeeProvider.batchSetEmployeeByIds(KsBeanUtil.convert(request,EsEmployeeListByIdsRequest.class));
        operateLogMQUtil.convertAndSend("设置", "批量设置业务员", "批量设置业务员数" + num);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 调整部门
     *
     * @param request employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "调整部门")
    @RequestMapping(value = "/employee/adjustDepartment", method = RequestMethod.POST)
    public BaseResponse adjustDepartment(@RequestBody EmployeeChangeDepartmentRequest request) {
        employeeProvider.changeDepartment(request);
        esEmployeeProvider.changeDepartment(KsBeanUtil.convert(request, EsEmployeeChangeDepartmentRequest.class));
        operateLogMQUtil.convertAndSend("设置", "批量调整部门", "批量调整部门");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 业务员交接
     *
     * @param request employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "业务员交接")
    @RequestMapping(value = "/employee/handoverEmployee", method = RequestMethod.POST)
    public BaseResponse employeeHandover(@RequestBody EmployeeHandoverRequest request) {
        Integer num = employeeProvider.handoverEmployee(request).getContext();
        esEmployeeProvider.handoverEmployee(KsBeanUtil.convert(request,EsEmployeeHandoverRequest.class));
        operateLogMQUtil.convertAndSend("设置", "业务员交接", "业务员交接数：" + num);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 下载员工导入模版
     *
     */
    @ApiOperation(value = "下载员工导入模版")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "加密", required = true)
    @RequestMapping(value = "/employee/excel/template/{encrypted}", method = RequestMethod.GET)
    public void downloadTemplate(@PathVariable String encrypted) {
        String file = employeeProvider.exportTemplate().getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("员工导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @RequestMapping(value = "/employee/excel/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        employeeExcelService.downErrExcel(commonUtil.getOperatorId(), ext);
    }

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/employee/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(employeeExcelService.upload(uploadFile, commonUtil.getOperatorId()));
    }

}
