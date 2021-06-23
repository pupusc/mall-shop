package com.wanmi.sbc.employee;

import com.wanmi.sbc.aop.DepartmentIsolation;
import com.wanmi.sbc.base.verifycode.VerifyCodeService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.EmployeeResponse;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.request.store.StoreByCompanyInfoIdRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoByIdResponse;
import com.wanmi.sbc.customer.api.response.employee.*;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.bean.vo.EmployeeDisableOrEnableByCompanyIdVO;
import com.wanmi.sbc.customer.bean.vo.EmployeePageVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.customer.request.EmployeeExcelImportRequest;
import com.wanmi.sbc.customer.service.EmployeeExcelService;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeProvider;
import com.wanmi.sbc.elastic.api.provider.employee.EsEmployeeQueryProvider;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationProvider;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeBatchDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeePageRequest;
import com.wanmi.sbc.elastic.api.request.employee.EsEmployeeSaveRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.StoreInfoStateModifyRequest;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSwitchRequest;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsVO;
import com.wanmi.sbc.setting.bean.enums.VerifyType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import com.wanmi.sbc.util.sms.SmsSendUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * @Author: songhanlin
 * @Date: Created In 下午5:26 2017/11/3
 * @Description: 商家账号/员工 Controller
 */
@Api(description = "平台员工API", tags = "EmployeeController")
@RestController("bossEmployeeController")
@RequestMapping("/customer")
public class EmployeeController {

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private PointsGoodsSaveProvider pointsGoodsSaveProvider;

    @Autowired
    private EmployeeExcelService employeeExcelService;

    @Autowired
    private EsStoreInformationProvider esStoreInformationProvider;

    @Autowired
    private EsEmployeeQueryProvider esEmployeeQueryProvider;

    @Autowired
    private EsEmployeeProvider esEmployeeProvider;

    /**
     * 分页查询员工
     *
     * @param pageRequest
     * @return 会员信息
     */
    @DepartmentIsolation(isIncluedeParentDepartment = false)
    @ApiOperation(value = "分页查询员工")
    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<EmployeePageVO>> findEmployees(@RequestBody EmployeePageRequest pageRequest) {
        pageRequest.setAccountType(AccountType.s2bBoss);
        pageRequest.putSort("isMasterAccount", SortType.DESC.toValue());
        pageRequest.putSort("isLeader", SortType.DESC.toValue());
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        return BaseResponse.success(employeeQueryProvider.page(pageRequest).getContext()
                .getEmployeePageVOPage());
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
        employeeRequest.setAccountType(AccountType.s2bBoss);
        employeeRequest.putSort("isMasterAccount", SortType.DESC.toValue());
        employeeRequest.putSort("isLeader", SortType.DESC.toValue());
        employeeRequest.putSort("createTime", SortType.DESC.toValue());
        return ResponseEntity.ok(BaseResponse.success(esEmployeeQueryProvider.page(employeeRequest).getContext()
                .getEmployeePageVOPage()));
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
     * 新增员工
     *
     * @param employeeSaveRequest employeeSaveRequest
     * @return employee
     */
    @ApiOperation(value = "新增员工")
    @RequestMapping(value = "/employee", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveEmployee(@Valid @RequestBody EmployeeAddRequest employeeSaveRequest) {
        employeeSaveRequest.setAccountType(AccountType.s2bBoss);

        if (StringUtils.isNotEmpty(employeeSaveRequest.getEmployeeName()) && CollectionUtils.isEmpty(employeeSaveRequest.getRoleIdList())
                && nonNull(employeeSaveRequest.getRoleName())) {
            operateLogMQUtil.convertAndSend("设置", "新增角色", "新增角色：" + employeeSaveRequest.getRoleName());
        }

        operateLogMQUtil.convertAndSend("设置", "新增员工",
                "新增员工：" + employeeSaveRequest.getEmployeeName());
        EmployeeAddResponse employeeAddResponse = employeeProvider.add(employeeSaveRequest).getContext();
        EsEmployeeSaveRequest esEmployeeSaveRequest = KsBeanUtil.convert(employeeAddResponse, EsEmployeeSaveRequest.class);
        return ResponseEntity.ok(esEmployeeProvider.save(esEmployeeSaveRequest));
    }


    /**
     * 查询s2b平台员工是否存在
     *
     * @param phone phone
     * @return boolean
     */
    @ApiOperation(value = "查询平台员工是否存在")
    @ApiImplicitParam(paramType = "path", dataType = "String",
            name = "phone", value = "手机号", required = true)
    @RequestMapping(value = "/{phone}/exist", method = RequestMethod.GET)
    public BaseResponse findBossEmployeeExist(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @PathVariable("phone") String phone) {
        EmployeeByMobileResponse employee =
                Optional.ofNullable(
                        employeeQueryProvider.getByMobile(
                                EmployeeByMobileRequest.builder().accountType(AccountType.s2bBoss).mobile(phone).build()
                        ).getContext()).orElseThrow(() -> new SbcRuntimeException(EmployeeErrorCode.MOBILE_NOT_EXIST));
        //被禁用
        if (AccountState.DISABLE.equals(employee.getAccountState())) {
            throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_DISABLED,
                    new Object[]{""});
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 启用/禁用 账号
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "启用/禁用员工账号")
    @RequestMapping(value = "/switch", method = RequestMethod.PUT)
    public BaseResponse<List<EmployeeDisableOrEnableByCompanyIdVO>> switchEmp(@Valid @RequestBody
                                                                                      EmployeeDisableOrEnableByCompanyIdRequest request) {
        EmployeeDisableOrEnableByCompanyIdResponse response =
                employeeProvider.disableOrEnableByCompanyId(request).getContext();



        // 停用该店铺关联的积分商品
        StoreVO storeVO = storeQueryProvider.getStoreByCompanyInfoId(StoreByCompanyInfoIdRequest.builder()
                .companyInfoId(request.getCompanyInfoId())
                .build()).getContext().getStoreVO();
        List<PointsGoodsVO> pointsGoodsVOList = pointsGoodsQueryProvider.getByStoreId(PointsGoodsByStoreIdRequest.builder()
                .storeId(storeVO.getStoreId())
                .build()).getContext().getPointsGoodsVOList();
        pointsGoodsVOList.forEach(pointsGoodsVO -> pointsGoodsSaveProvider.modifyStatus(PointsGoodsSwitchRequest.builder()
                .pointsGoodsId(pointsGoodsVO.getPointsGoodsId())
                .status(EnableStatus.DISABLE)
                .build()));
        //操作日志记录
        CompanyInfoByIdRequest companyInfoByIdRequest = new CompanyInfoByIdRequest();
        companyInfoByIdRequest.setCompanyInfoId(request.getCompanyInfoId());
        CompanyInfoByIdResponse companyInfoByIdResponse = companyInfoQueryProvider.getCompanyInfoById(companyInfoByIdRequest).getContext();

        //更新es店铺信息
        esStoreInformationProvider.modifyStoreState(StoreInfoStateModifyRequest
                .builder()
                .storeId(storeVO.getStoreId())
                .accountState(request.getAccountState().toValue())
                .accountDisableReason(request.getAccountDisableReason()).build());

        if (request.getAccountState() == AccountState.ENABLE) {
            operateLogMQUtil.convertAndSend("商家", "启用商家",
                    "启用商家：商家编号" + companyInfoByIdResponse.getCompanyCode());
        } else {
            operateLogMQUtil.convertAndSend("商家", "禁用商家",
                    "禁用商家：商家编号" + companyInfoByIdResponse.getCompanyCode());
        }
        return BaseResponse.success(response.getEmployeeList());
    }

    /**
     * 批量删除
     *
     * @param employeeRequest employeeRequest
     * @return ResponseEntity<BaseResponse>
     */
    @ApiOperation(value = "批量删除员工")
    @RequestMapping(value = "/employee", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteEmployees(@RequestBody EmployeeRequest employeeRequest) {
        List<String> employeeIds = employeeRequest.getEmployeeIds();
        EmployeeByIdResponse employee = null;
        if (CollectionUtils.size(employeeIds) == 1) {
            employee = employeeQueryProvider.getById(
                    EmployeeByIdRequest.builder().employeeId(employeeIds.get(0)).build()).getContext();
            operateLogMQUtil.convertAndSend("设置", "删除员工",
                    "删除员工：" + employee.getAccountName());
        }
        if (CollectionUtils.size(employeeIds) > 1) {
            operateLogMQUtil.convertAndSend("设置", "批量删除", "批量删除");
        }
        employeeProvider.batchDeleteByIds(
                EmployeeBatchDeleteByIdsRequest.builder()
                        .employeeIds(employeeRequest.getEmployeeIds())
                        .accountType(AccountType.s2bBoss).build());
        return ResponseEntity.ok(esEmployeeProvider.batchDeleteByIds(EsEmployeeBatchDeleteByIdsRequest.builder()
                .employeeIds(employeeRequest.getEmployeeIds())
                .accountType(AccountType.s2bBoss).build()));
    }

    /**
     * 发送验证码
     *
     * @param phone phone
     * @return BaseResponse
     */
    @ApiOperation(value = "发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "uuid", value = "uuid", required = true)
    })
    @RequestMapping(value = "/send/sms", method = RequestMethod.POST)
    public BaseResponse sendSms(@javax.validation.constraints.Pattern(regexp = "\\d{11}") @RequestParam("phone")
                                        String phone, @RequestParam("uuid") String uuid) {

        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(commonUtil.getOperatorId()).build())
                .getContext();
        if (employee != null && !phone.equals(employee.getEmployeeMobile())) {
            EmployeeMobileExistsResponse existsResponse = employeeQueryProvider.mobileIsExists(
                    EmployeeMobileExistsRequest.builder()
                            .mobile(phone)
                            .accountType(AccountType.s2bSupplier)
                            .build())
                    .getContext();
            EmployeeMobileExistsResponse existsMallResponse = employeeQueryProvider.mobileIsExists(
                    EmployeeMobileExistsRequest.builder()
                            .mobile(phone)
                            .accountType(AccountType.s2bProvider)
                            .build())
                    .getContext();
            if (existsResponse.isExists() || existsMallResponse.isExists()) {
                throw new SbcRuntimeException("K-040005");
            }
        }

        String regexp = "\\d{11}";
        Pattern p = Pattern.compile(regexp);
        if (p.matcher(phone).find() && verifyCodeService.validateCaptchaCertificate(uuid)) {
            if (verifyCodeService.validSmsCertificate(phone, VerifyType.EMPLOYEE_CHANGE_PHONE)) {
                String verifyCode = verifyCodeService.generateSmsVerifyCode(phone, VerifyType.EMPLOYEE_CHANGE_PHONE, 5, TimeUnit.MINUTES);
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
     * 根据业务员名称模糊查询
     *
     * @param  employeeByNameRequest
     * @return boolean
     */
    @DepartmentIsolation(isIncluedeParentDepartment = false)
    @ApiOperation(value = "根据业务员名称模糊查询")
    @RequestMapping(value = "/employee/name", method = RequestMethod.POST)
    public BaseResponse<EmployeeByNameResponse> findBossEmployeeByName(@RequestBody EmployeeByNameRequest employeeByNameRequest) {

        EmployeePageRequest request = new EmployeePageRequest();
        request.setDepartmentIds(employeeByNameRequest.getDepartmentIds());
        request.setIsEmployeeSearch(Boolean.TRUE);
        request.setEmployeeName(employeeByNameRequest.getName());
        request.setAccountType(AccountType.s2bBoss);
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
        request.setCustomerType(CustomerType.PLATFORM);
        request.setOperator(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        Integer num = employeeProvider.activateAccount(request).getContext();

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

        EmployeeExcelImportRequest request = new EmployeeExcelImportRequest();
        request.setExt(ext);
        request.setCompanyInfoId(commonUtil.getCompanyInfoId());
        request.setUserId(commonUtil.getOperatorId());
        request.setAccountType(AccountType.s2bBoss);
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
        EmployeeNumRequest request = EmployeeNumRequest.builder().accountType(AccountType.s2bBoss).build();
        return employeeQueryProvider.countEmployeeNum(request);
    }

}
