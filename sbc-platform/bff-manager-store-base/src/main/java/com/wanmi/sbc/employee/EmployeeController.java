package com.wanmi.sbc.employee;

import com.wanmi.sbc.aop.DepartmentIsolation;
import com.wanmi.sbc.base.verifycode.VerifyCodeService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.EmployeeResponse;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.*;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.request.EmployeeExcelImportRequest;
import com.wanmi.sbc.customer.service.EmployeeExcelService;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeProvider;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeQueryProvider;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeActivateAccountRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeBatchDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeSaveRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeePageRequest;
import com.wanmi.sbc.setting.bean.enums.VerifyType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.wanmi.sbc.util.sms.SmsSendUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @Author: songhanlin
 * @Date: Created In 下午5:31 2017/11/2
 * @Description: 商家Controller
 */
@Api(tags = "EmployeeController", description = "商家服务 API")
@RestController("supplierEmployeeController")
@RequestMapping("/customer")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EmployeeExcelService employeeExcelService;

    @Autowired
    private EsEmployeeQueryProvider esEmployeeQueryProvider;


    @Autowired
    private EsEmployeeProvider esEmployeeProvider;
    /**
     * 查询员工是否存在
     *
     * @param phone phone
     * @return boolean
     */
    @ApiOperation(value = "查询员工是否存在")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号", required = true)
    @RequestMapping(value = "/{phone}/valid", method = RequestMethod.GET)
    public BaseResponse<Boolean> findEmployeeExist(@PathVariable String phone, HttpServletRequest request) {

        if (!ValidateUtil.isPhone(phone)) {
            logger.error("手机号码:{}格式错误", phone);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Claims empInfo = (Claims) request.getAttribute("claims");
        if (isNull(empInfo)) {
            AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                    AccountType.s2bSupplier : AccountType.s2bProvider;
            EmployeeByMobileResponse response = employeeQueryProvider.getByMobile(
                    EmployeeByMobileRequest.builder().mobile(phone).accountType(accountType).build()
            ).getContext();
            if (response == null || response.getEmployeeId() == null) {
                throw new SbcRuntimeException(EmployeeErrorCode.NOT_EXIST);
            }
            return BaseResponse.SUCCESSFUL();
        } else {
            String employeeId = empInfo.get("employeeId").toString();

            EmployeeByIdResponse response = employeeQueryProvider.getById(
                    EmployeeByIdRequest.builder().employeeId(employeeId).build()
            ).getContext();
            if (response == null || response.getEmployeeId() == null || !phone.equals(response.getEmployeeMobile())) {
                throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR);
            }
            return BaseResponse.success(true);
        }
    }

    /**
     * 查询商家员工是否存在
     *
     * @param phone phone
     * @return boolean
     */
    @ApiOperation(value = "查询商家员工是否存在")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号", required = true)
    @RequestMapping(value = "/{phone}/supplier", method = RequestMethod.GET)
    public BaseResponse findEmployeeExist(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @PathVariable(
            "phone") String phone) {
        commonValidEmployee(phone, AccountType.s2bSupplier);
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "查询供应商商员工是否存在")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号", required = true)
    @RequestMapping(value = "/{phone}/provider", method = RequestMethod.GET)
    public BaseResponse findProviderEmployeeExist(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @PathVariable(
            "phone") String phone) {
        commonValidEmployee(phone,AccountType.s2bProvider);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 统一校验
     * @param phone
     * @param type
     */
    public void commonValidEmployee(String phone,AccountType type){
        EmployeeByMobileResponse employee = employeeQueryProvider.getByMobile(
                EmployeeByMobileRequest.builder().mobile(phone).accountType(type).build()
        ).getContext();

        if (employee == null || employee.getEmployeeId() == null) {
            throw new SbcRuntimeException(EmployeeErrorCode.MOBILE_NOT_EXIST);
        }
        //被禁用
        if (AccountState.DISABLE.equals(employee.getAccountState())) {
            throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_DISABLED,
                    new Object[]{"，原因为：" + employee.getAccountDisableReason() + "，如有疑问请联系平台"});
        }
    }


    /**
     * 查询所有员工
     *
     * @return <List<EmployeeResponse>>
     */
    @ApiOperation(value = "查询所有员工")
    @RequestMapping(value = "/employee/allEmployees")
    public ResponseEntity<List<EmployeeResponse>> findAllEmployees() {
        return ResponseEntity.ok(
                employeeQueryProvider.listByCompanyId(
                        EmployeeListByCompanyIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
                ).getContext().getEmployeeList()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(employee -> {
                            EmployeeResponse employeeResponse = new EmployeeResponse();
                            employeeResponse.setEmployeeId(employee.getEmployeeId());
                            employeeResponse.setEmployeeName(StringUtils.isEmpty(employee.getEmployeeName()) ? employee
                                    .getEmployeeMobile() : employee.getEmployeeName());
                            employeeResponse.setEmployeeMobile(employee.getEmployeeMobile());
                            return employeeResponse;
                        }).collect(Collectors.toList()));
    }

    /**
     * 查询平台所有员工
     *
     * @return <List<EmployeeResponse>>
     */
    @ApiOperation(value = "查询平台所有员工")
    @RequestMapping(value = "/employee/allBossEmployees")
    public ResponseEntity<List<EmployeeResponse>> findAllBossEmployees() {
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
     * 分页查询员工
     *
     * @param employeeRequest employeeRequest
     * @return 会员信息
     */
    @DepartmentIsolation(isIncluedeParentDepartment = false)
    @ApiOperation(value = "分页查询员工")
    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> findEmployees(@RequestBody EmployeePageRequest employeeRequest) {
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;
        employeeRequest.setAccountType(accountType);
        employeeRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        employeeRequest.putSort("isMasterAccount", SortType.DESC.toValue());
        employeeRequest.putSort("manageDepartmentIds", SortType.DESC.toValue());
        employeeRequest.putSort("createTime", SortType.DESC.toValue());
        return ResponseEntity.ok(BaseResponse.success(employeeQueryProvider.page(employeeRequest).getContext()
                .getEmployeePageVOPage()));
    }


    /**
     * ES分页查询员工
     *
     * @param employeeRequest employeeRequest
     * @return 会员信息
     */
    @DepartmentIsolation(isIncluedeParentDepartment = false)
    @ApiOperation(value = "分页查询员工")
    @RequestMapping(value = "/es/employees", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> findEsEmployees(@RequestBody EsEmployeePageRequest employeeRequest) {
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;
        employeeRequest.setAccountType(accountType);
        employeeRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        employeeRequest.putSort("isMasterAccount", SortType.DESC.toValue());
        employeeRequest.putSort("manageDepartmentIds", SortType.DESC.toValue());
        employeeRequest.putSort("createTime", SortType.DESC.toValue());
        return ResponseEntity.ok(BaseResponse.success(esEmployeeQueryProvider.page(employeeRequest).getContext()
                .getEmployeePageVOPage()));
    }

    /**
     * 新增员工
     *
     * @param employeeSaveRequest employeeSaveRequest
     * @return employee
     */
    @ApiOperation(value = "新增员工")
    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveEmployee(@Valid @RequestBody EmployeeAddRequest employeeSaveRequest) {
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;
        employeeSaveRequest.setAccountType(accountType);
        employeeSaveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        EmployeeAddResponse employeeAddResponse = employeeProvider.add(employeeSaveRequest).getContext();
        EsEmployeeSaveRequest esEmployeeSaveRequest = KsBeanUtil.convert(employeeAddResponse, EsEmployeeSaveRequest.class);
        esEmployeeProvider.save(esEmployeeSaveRequest);
        if (StringUtils.isNotEmpty(employeeSaveRequest.getEmployeeName()) && CollectionUtils.isEmpty(employeeSaveRequest.getRoleIdList())
                && nonNull(employeeSaveRequest.getRoleName())) {
            operateLogMQUtil.convertAndSend("设置", "新增角色",
                    "新增角色： " + employeeSaveRequest.getRoleName());
        }
        operateLogMQUtil.convertAndSend("设置", "新增员工",
                "新增员工：员工账户名 " + employeeSaveRequest.getEmployeeName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }


    /**
     * 批量删除
     *
     * @param employeeRequest employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/employee", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteEmployees(@RequestBody EmployeeRequest employeeRequest) {
        List<String> employeeIds = employeeRequest.getEmployeeIds();
        if (1 == CollectionUtils.size(employeeIds)) {
            EmployeeByIdResponse employeeByIdResponse = employeeQueryProvider.getById(
                    EmployeeByIdRequest.builder().employeeId(employeeIds.get(0)).build()).getContext();
            operateLogMQUtil.convertAndSend("设置", "删除员工",
                    "删除员工：员工账户名" + employeeByIdResponse.getAccountName());
        }
        if (CollectionUtils.size(employeeIds) > 1) {
            operateLogMQUtil.convertAndSend("设置", "批量删除", "批量删除");
        }
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;
        employeeProvider.batchDeleteByIds(
                EmployeeBatchDeleteByIdsRequest.builder()
                        .employeeIds(employeeRequest.getEmployeeIds())
                        .accountType(accountType)
                        .build()
        );
        esEmployeeProvider.batchDeleteByIds(EsEmployeeBatchDeleteByIdsRequest.builder()
                .employeeIds(employeeRequest.getEmployeeIds())
                .accountType(accountType)
                .build());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 发送验证码
     *
     * @param phone phone
     * @return BaseResponse
     */
    @ApiOperation(value = "发送验证码")
    @RequestMapping(value = "/send/sms", method = RequestMethod.POST)
    public BaseResponse sendSms(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @RequestParam("phone")
                                        String phone, @RequestParam("uuid") String uuid) {

        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(commonUtil.getOperatorId()).build()
        ).getContext();
        if (employee != null && !phone.equals(employee.getEmployeeMobile())) {
            AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                    AccountType.s2bSupplier : AccountType.s2bProvider;
            EmployeeMobileExistsResponse existsResponse = employeeQueryProvider.mobileIsExists(
                    EmployeeMobileExistsRequest.builder()
                            .mobile(phone)
                            .accountType(accountType).build()
            ).getContext();
            if (existsResponse.isExists()) {
                throw new SbcRuntimeException("K-040005");
            }
        }

        String regexp = "\\d{11}";
        Pattern p = Pattern.compile(regexp);
        if (p.matcher(phone).find() && verifyCodeService.validateCaptchaCertificate(uuid)) {
            if (verifyCodeService.validSmsCertificate(phone, VerifyType.EMPLOYEE_CHANGE_PHONE)) {
                String verifyCode = verifyCodeService.generateSmsVerifyCode(phone, VerifyType.EMPLOYEE_CHANGE_PHONE, 5,
                        TimeUnit.MINUTES);
                smsSendUtil.send(SmsTemplate.CHANGE_PHONE, new String[]{phone}, verifyCode);
            } else {
                throw new SbcRuntimeException(CommonErrorCode.SEND_FAILURE);
            }
        } else {
            throw new SbcRuntimeException("K-000013");
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询业务员绑定的手机号
     *
     * @return
     */
    @ApiOperation(value = "查询业务员绑定的手机号")
    @RequestMapping(value = "/employeesMobile", method = RequestMethod.GET)
    public BaseResponse<EmployeeResponse> findEmployeesMobile() {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(commonUtil.getOperatorId()).build()
        ).getContext();
        if (employee != null) {
            BeanUtils.copyProperties(employee, employeeResponse);
        }
        if (StringUtils.isNotBlank(employeeResponse.getEmployeeMobile())) {
            return BaseResponse.success(employeeResponse);
        }
        return BaseResponse.FAILED();
    }

    /**
     * 根据业务员名称模糊查询
     *
     * @param  employeeByNameRequest
     * @return
     */
    @DepartmentIsolation(isIncluedeParentDepartment = false)
    @ApiOperation(value = "根据业务员名称模糊查询")
    @RequestMapping(value = "/employee/name", method = RequestMethod.POST)
    public BaseResponse<EmployeeByNameResponse> findBossEmployeeByName(@RequestBody EmployeeByNameRequest employeeByNameRequest) {

        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;

        EmployeePageRequest request = new EmployeePageRequest();
        request.setIsEmployeeSearch(Boolean.TRUE);
        request.setEmployeeName(employeeByNameRequest.getName());
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.setAccountType(accountType);
        request.setIsEmployee(0);
        request.setPageNum(0);
        request.setPageSize(5);
        request.putSort("createTime", SortType.DESC.toValue());
        EmployeeByNameResponse response = employeeQueryProvider.pageForEmployee(request).getContext();
        return BaseResponse.success(response);
    }

    /**
     * 会员账号激活
     *
     * @param idsRequest
     * @return 会员信息
     */
    @ApiOperation(value = "会员账号激活")
    @RequestMapping(value = "/activateAccount", method = RequestMethod.POST)
    public BaseResponse findEmployees(@RequestBody EmployeeListByIdsRequest idsRequest) {
        EmployeeActivateAccountRequest request = new EmployeeActivateAccountRequest();
        request.setEmployeeIds(idsRequest.getEmployeeIds());
        request.setCustomerType(CustomerType.SUPPLIER);
        request.setOperator(commonUtil.getOperatorId());
        request.setS2bSupplier(Boolean.TRUE);
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.setStoreId(commonUtil.getStoreId());
        Integer num = employeeProvider.activateAccount(request).getContext();
        esEmployeeProvider.activateAccount(KsBeanUtil.convert(request, EsEmployeeActivateAccountRequest.class));
        operateLogMQUtil.convertAndSend("会员", "账户激活", "账户激活数：" + num);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 确认导入员工
     *
     * @param ext 文件格式 {@link String}
     * @return
     */
    @ApiOperation(value = "确认导入员工")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext", value = "文件名后缀", required = true)
    @RequestMapping(value = "/employee/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> implGoods(@PathVariable String ext) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;

        EmployeeExcelImportRequest request = new EmployeeExcelImportRequest();
        request.setExt(ext);
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.setUserId(commonUtil.getOperatorId());
        request.setAccountType(accountType);
        employeeExcelService.importEmployee(request);
        //操作日志记录
        operateLogMQUtil.convertAndSend("员工", "批量导入", "批量导入");
        return BaseResponse.success(Boolean.TRUE);
    }

    /**
     * 统计部门人数
     *
     * @param
     * @return EmployeeNumResponse
     */
    @ApiOperation(value = "统计部门人数")
    @RequestMapping(value = "/employee/countNum", method = RequestMethod.GET)
    public BaseResponse<EmployeeNumResponse> countEmployeeNum() {
        AccountType accountType = Platform.SUPPLIER.equals(commonUtil.getOperator().getPlatform()) ?
                AccountType.s2bSupplier : AccountType.s2bProvider;
        EmployeeNumRequest request = EmployeeNumRequest.builder()
                .companyInfoId(commonUtil.getCompanyInfoId())
                .accountType(accountType).build();
        return employeeQueryProvider.countEmployeeNum(request);
    }
}
