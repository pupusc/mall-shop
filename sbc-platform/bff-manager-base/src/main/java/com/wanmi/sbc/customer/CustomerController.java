package com.wanmi.sbc.customer;

import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.customer.api.provider.account.CustomerAccountQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelSaveProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.CustomerEditRequest;
import com.wanmi.sbc.customer.api.request.account.CustomerAccountListRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryRequest;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.detail.CustomerStateBatchModifyRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdsRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelWithDefaultByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerQueryByEmployeeRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.api.response.account.CustomerAccountListResponse;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetForSupplierResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.validator.CustomerValidator;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerCheckStateModifyRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerStateBatchModifyRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

/**
 * 会员
 * Created by CHENLI on 2017/4/19.
 */
@Api(tags = "CustomerController", description = "会员 Api")
@RestController
public class CustomerController {

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerDetailProvider customerDetailProvider;

    @Autowired
    private CustomerAccountQueryProvider customerAccountQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private CustomerValidator customerValidator;

    @Autowired
    private StoreCustomerQueryProvider storeCustomerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private EsCustomerDetailProvider esCustomerDetailProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private PaidCardCustomerRelSaveProvider paidCardCustomerRelSaveProvider;

    @InitBinder
    public void initBinder(DataBinder binder) {
        if (binder.getTarget() instanceof CustomerEditRequest) {
            binder.setValidator(customerValidator);
        }
    }


    /**
     * 多条件查询会员详细信息
     *
     * @param request
     * @return 会员详细信息
     */
    @ApiOperation(value = "多条件查询会员详细信息")
    @RequestMapping(value = "/customer/customerDetails", method = RequestMethod.POST)
    public List<CustomerDetailVO> findCustomerDetailList(@RequestBody CustomerDetailListByConditionRequest request) {
        return customerQueryProvider.listCustomerDetailByCondition(request).getContext().getDetailResponseList();
    }

    /**
     * 多条件查询会员信息
     *
     * @param request
     * @return 会员信息
     */
    @ApiOperation(value = "多条件查询会员信息")
    @RequestMapping(value = "/customerList", method = RequestMethod.POST)
    public List<CustomerVO> findCustomerList(@RequestBody CustomerListByConditionRequest request) {
        return customerQueryProvider.listCustomerByCondition(request).getContext().getCustomerVOList();
    }

    /**
     * 查询单条会员信息
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询单条会员信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<CustomerGetForSupplierResponse> findById(@PathVariable String customerId) {
        CustomerGetByIdResponse customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        CustomerGetForSupplierResponse customerResponse = new CustomerGetForSupplierResponse();
        BeanUtils.copyProperties(customer, customerResponse);
        if (customerResponse.getCustomerType() == CustomerType.SUPPLIER) {
            CompanyInfoQueryRequest request = new CompanyInfoQueryRequest();
            request.setCustomerId(customerId);

            CompanyInfoGetResponse companyInfo =
                    storeCustomerQueryProvider.getCompanyInfoBelongByCustomerId(request).getContext();
            customerResponse.setSupplierName(companyInfo.getSupplierName());
        }
        CustomerLevelVO customerLevel = customerLevelQueryProvider.getCustomerLevelWithDefaultById(
                CustomerLevelWithDefaultByIdRequest.builder().customerLevelId(customer.getCustomerLevelId()).build())
                .getContext();
        customerResponse.setCustomerLevelName(customerLevel.getCustomerLevelName());
        //查询企业信息
        if(commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)){
            BaseResponse<EnterpriseInfoByCustomerIdResponse> enterpriseInfo = enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                    .customerId(customerId)
                    .build());
            if(Objects.nonNull(enterpriseInfo.getContext())){
                customerResponse.setEnterpriseInfo(enterpriseInfo.getContext().getEnterpriseInfoVO());
            }
        }
        //填充付费会员信息
        BaseResponse<List<PaidCardCustomerRelVO>> response =
                this.paidCardCustomerRelQueryProvider
                        .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder().customerId(customerId)
                                //.delFlag(DeleteFlag.NO)
                                .build());
        List<PaidCardCustomerRelVO> paidCardCustomerRelList = response.getContext();
        if(CollectionUtils.isNotEmpty(paidCardCustomerRelList)){
            customerResponse.setPaidCardCustomerRelList(paidCardCustomerRelList);
        }

        return ResponseEntity.ok(customerResponse);
    }

    /**
     * 查询客户是否被删除了
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询客户是否被删除了", notes = "true: 删除, false: 未删除")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/customer/customerDelFlag/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> findCustomerDelFlag(@PathVariable String customerId) {
        return ResponseEntity.ok(customerQueryProvider.getCustomerDelFlag(new CustomerDelFlagGetRequest(customerId))
                .getContext().getDelFlag());
    }

    /**
     * 审核客户状态
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "审核客户状态")
    @RequestMapping(value = "/customer/customerState", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> updateCheckState(@RequestBody CustomerCheckStateModifyRequest request) {
        if (null == request.getCheckState() || StringUtils.isEmpty(request.getCustomerId())) {
            throw new SbcRuntimeException("K-000009");
        }
        customerProvider.modifyCustomerCheckState(request);

        //获取会员
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (request.getCustomerId())).getContext();
        if (nonNull(customer)) {
            if (request.getCheckState() == 1) {
                operateLogMQUtil.convertAndSend("客户", "审核客户", "审核客户：" + customer.getCustomerAccount());
            } else {
                operateLogMQUtil.convertAndSend("客户", "驳回客户", "驳回客户：" + customer.getCustomerAccount());
            }
        }
        EsCustomerCheckStateModifyRequest modifyRequest =  EsCustomerCheckStateModifyRequest.builder()
                .checkState(request.getCheckState()).customerId(request.getCustomerId()).rejectReason(request.getRejectReason()).build();
        esCustomerDetailProvider.modifyCustomerCheckState(modifyRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 批量删除会员
     *
     * @param customerQueryRequest
     * @return
     */
    @ApiOperation(value = "批量删除会员")
    @RequestMapping(value = "/customer", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> delete(@RequestBody CustomersDeleteRequest customerQueryRequest) {
        if (CollectionUtils.isEmpty(customerQueryRequest.getCustomerIds())) {
            throw new SbcRuntimeException("K-000009");
        }
        customerProvider.deleteCustomers(customerQueryRequest);
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 批量启用/禁用会员详情
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "批量启用/禁用会员详情")
    @RequestMapping(value = "/customer/detailState", method = RequestMethod.POST)
    public BaseResponse updateCustomerState(@RequestBody CustomerStateBatchModifyRequest queryRequest) {
        if (null == queryRequest.getCustomerStatus() || CollectionUtils.isEmpty(queryRequest.getCustomerIds())) {
            throw new SbcRuntimeException("K-000009");
        }

        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        //操作日志记录
        if (CustomerStatus.DISABLE.equals(queryRequest.getCustomerStatus())) {
            //获取会员
            CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                    (queryRequest.getCustomerIds().get(0))).getContext();
            if (nonNull(customer)) {
                String opContext = "禁用客户：";
                if (nonNull(claims)) {
                    String platform = Objects.toString(claims.get("platform"), "");
                    if (Platform.SUPPLIER.toValue().equals(platform)) {
                        opContext += "禁用客户：客户账号";
                    }
                }

                operateLogMQUtil.convertAndSend("客户", "禁用客户",
                        opContext + customer.getCustomerAccount());
            }
        } else {
            operateLogMQUtil.convertAndSend("客户", "批量启用客户", "批量启用客户");
        }

        customerDetailProvider.modifyCustomerStateByCustomerId(
                CustomerStateBatchModifyRequest.builder()
                        .customerIds(queryRequest.getCustomerIds())
                        .customerStatus(queryRequest.getCustomerStatus())
                        .forbidReason(queryRequest.getForbidReason()).build());

        esCustomerDetailProvider.modifyCustomerStateByCustomerId(EsCustomerStateBatchModifyRequest.builder()
                .customerIds(queryRequest.getCustomerIds())
                .customerStatus(queryRequest.getCustomerStatus())
                .forbidReason(queryRequest.getForbidReason()).build());
        return BaseResponse.SUCCESSFUL();

    }

    /**
     * 根据customerId查询会员账号
     *
     * @param customerId customerId
     * @return 会员账号
     */
    @ApiOperation(value = "根据customerId查询会员账号")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/customerAccounts/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<List<CustomerAccountVO>> findCustomerAccountById(@PathVariable("customerId") String customerId) {
        CustomerAccountListRequest customerAccountListRequest = new CustomerAccountListRequest();
        customerAccountListRequest.setCustomerId(customerId);
        BaseResponse<CustomerAccountListResponse> customerAccountListResponseBaseResponse =
                customerAccountQueryProvider.listByCustomerId(customerAccountListRequest);
        CustomerAccountListResponse customerAccountListResponse = customerAccountListResponseBaseResponse.getContext();
        if (Objects.nonNull(customerAccountListResponse)) {
            return ResponseEntity.ok(customerAccountListResponse.getCustomerAccountVOList());
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


    /**
     * 查询所有的有效的会员的id和accoutName，给前端autocomplete
     *
     * @return
     */
    @ApiOperation(value = "查询所有的有效的会员的id和accoutName，给前端autocomplete",
            notes = "customerId: 会员Id, customerAccount: 账号, customerName: 会员名称, customerLevelId: 等级Id")
    @EmployeeCheck
    @RequestMapping(value = "/customer/customerAccount/list", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> findAllCustomers(@RequestBody StoreCustomerQueryByEmployeeRequest queryRequest) {
        if (StringUtils.isBlank(queryRequest.getCustomerAccount())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        return ResponseEntity.ok(getCustomerAccount(queryRequest.getCustomerAccount(), commonUtil.getOperatorId()));
    }

    /**
     * 查询所有的有效的会员的id和accoutName，给前端autocomplete
     *
     * @return
     */
    @ApiOperation(value = "查询所有的有效的会员的id和accoutName，给前端autocomplete",
            notes = "customerId: 会员Id, customerAccount: 账号, customerName: 会员名称, customerLevelId: 等级Id")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccount", value = "账号",
            required = true)
    @RequestMapping(value = "/customer/list/{customerAccount}", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> findAllCustomers(@PathVariable String customerAccount) {
        return ResponseEntity.ok(getCustomerAccount(customerAccount, null));
    }

    /**
     * 代客下单-搜索客户
     *
     * @param customerAccount 账号
     * @param employeeId      业务员
     * @return
     */
    private List<Map<String, Object>> getCustomerAccount(String customerAccount, String employeeId) {
        List<Map<String, Object>> collect = new ArrayList<>();
        //查询所有的合法的会员账户
        //非自营店铺查询店铺会员 自营店铺查询平台所有会员
        List<StoreCustomerVO> customerByCondition;
        //已审核
        StoreCustomerQueryByEmployeeRequest request = new StoreCustomerQueryByEmployeeRequest();
        request.setCustomerAccount(customerAccount);
        request.setEmployeeId(employeeId);
        request.setPageSize(5);
        if (commonUtil.getCompanyType().equals(BoolFlag.YES)) {
            request.setStoreId(commonUtil.getStoreId());

            customerByCondition = storeCustomerQueryProvider.listCustomer(request).getContext().getStoreCustomerVOList();
        } else {
            customerByCondition = storeCustomerQueryProvider.listBossCustomer(request).getContext().getStoreCustomerVOList();
        }

        if (CollectionUtils.isEmpty(customerByCondition)) {
            return collect;
        }

        collect = customerByCondition.stream()
                .map(v -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerId", v.getCustomerId());
                    map.put("customerAccount", v.getCustomerAccount());
                    map.put("customerName", v.getCustomerName());
                    map.put("customerLevelId", v.getCustomerLevelId());
                    return map;
                }).collect(toList());

        List<Long> levelIds = collect.stream().filter(v -> v.get("customerLevelId") != null).map(v -> (Long) v.get(
                "customerLevelId")).collect(toList());
        if (levelIds != null && !levelIds.isEmpty()) {
            List<CustomerLevelVO> customerLevels = customerLevelQueryProvider.listCustomerLevelByIds(
                    CustomerLevelByIdsRequest.builder().customerLevelIds(levelIds).build()).getContext().getCustomerLevelVOList();
            IteratorUtils.zip(collect, customerLevels
                    , (collect1, levels1) -> collect1.get("customerLevelId") != null && collect1.get("customerLevelId"
                    ).equals(levels1.getCustomerLevelId())
                    , (collect2, levels2) -> {
                        if (levels2.getCustomerLevelName() != null) {
                            collect2.put("customerLevelName", levels2.getCustomerLevelName());
                        }
                    }
            );

            if (commonUtil.getCompanyType().equals(BoolFlag.YES)) {
                List<StoreLevelVO> storeLevels = storeLevelQueryProvider.list(StoreLevelListRequest.builder()
                        .storeLevelIdList(levelIds)
                        .build()).getContext().getStoreLevelVOList();

                IteratorUtils.zip(collect, storeLevels,
                        (collect1, levels1) -> Objects.nonNull(collect1.get("customerLevelId")) &&
                                collect1.get("customerLevelId").equals(levels1.getStoreLevelId()),
                        (collect2, levels2) -> {
                            collect2.put("customerLevelName", levels2.getLevelName());
                        }
                );
            }
        }
        return collect;
    }

    /**
     * 根据客户ID查询相关信息，编辑代客下单用
     * add Transactional的意思是为了hibernate懒加载，后期重构要放到service
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "根据客户ID查询相关信息，编辑代客下单用",
            notes = "customerId: 用户Id, customerAccount: 账号, customerName: 会员名称, customerLevelName: 等级名称")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/customer/single/{customerId}", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<Map<String, String>> findCustomerById(@PathVariable("customerId") String customerId) {
        Map<String, String> resultMap = new HashMap<>();
        CustomerGetByIdResponse customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        String companyInfoId = commonUtil.getOperator().getAdminId();
        if (null != customer && nonNull(companyInfoId)) {
            resultMap.put("customerId", customer.getCustomerId());
            resultMap.put("customerAccount", customer.getCustomerAccount());
            resultMap.put("customerName", customer.getCustomerDetail().getCustomerName());
            List<Long> levelIds = customer.getStoreCustomerRelaListByAll()
                    .parallelStream()
                    .filter(storeCustomerRela -> companyInfoId.equals(storeCustomerRela.getCompanyInfoId().toString()))
                    .map(StoreCustomerRelaVO::getStoreLevelId)
                    .collect(toList());
            // 非自营店铺
            if (CollectionUtils.isNotEmpty(levelIds)) {
                StoreLevelVO storeLevelVO = storeLevelQueryProvider.getById(StoreLevelByIdRequest.builder().storeLevelId(levelIds.get(0)).build()).getContext().getStoreLevelVO();
                resultMap.put("customerLevelName", storeLevelVO.getLevelName());
            } else {// 自营店铺
                CustomerLevelVO customerLevel = customerLevelQueryProvider.getCustomerLevelWithDefaultById(
                        CustomerLevelWithDefaultByIdRequest.builder().customerLevelId(customer.getCustomerLevelId()).build()).getContext();
                resultMap.put("customerLevelName", customerLevel.getCustomerLevelName());
            }
        }
        return ResponseEntity.ok(resultMap);
    }


    /**
     * 查询所有的有效的会员的id和accoutName，给前端autocomplete
     *
     * @return
     */
    @ApiOperation(value = "查询所有的有效的会员的id和accoutName，给前端autocomplete",
            notes = "customerId: 用户Id, customerAccount: 账号, customerName: 会员名称")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员ID", required = true)
    @RequestMapping(value = "/customer/all/{customerAccount}", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> autoCustomerInfo(@PathVariable String customerAccount) {
        List<Map<String, Object>> collect = new ArrayList<>();
        //查询所有的合法的会员账户
        CustomerDetailListForOrderRequest cqr = new CustomerDetailListForOrderRequest();
        //已审核
        cqr.setCheckState(CheckState.CHECKED.toValue());
        cqr.setCustomerStatus(CustomerStatus.ENABLE);
        cqr.setCustomerAccount(customerAccount);
//        List<CustomerDetail> customerByCondition = customerService.findDetailForOrder(cqr);
        List<CustomerDetailVO> customerByCondition = customerQueryProvider.listCustomerDetailForOrder(cqr).getContext()
                .getDetailResponseList();

        if (CollectionUtils.isEmpty(customerByCondition)) {
            return ResponseEntity.ok(collect);
        }

        collect = customerByCondition.stream()
                .map(v -> {
                    CustomerGetByIdRequest request = new CustomerGetByIdRequest();
                    request.setCustomerId(v.getCustomerId());
                    String account = customerQueryProvider.getCustomerById(request).getContext().getCustomerAccount();
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerId", v.getCustomerId());
                    map.put("customerAccount", account);
                    map.put("customerName", v.getCustomerName());
                    return map;
                }).collect(toList());

        return ResponseEntity.ok(collect);
    }


    /**
     * BOSS删除付费会员
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "BOSS删除付费会员")
    @DeleteMapping(value = "/customer/deleteCustomerPaidCard/{paidCardRelId}")
    public BaseResponse deleteCustomerPaidCard(@PathVariable String paidCardRelId) {
        CustomerDeletePaidCardRequest request = new CustomerDeletePaidCardRequest();
        request.setPaidCardRelId(paidCardRelId);
        BaseResponse response =  this.paidCardCustomerRelSaveProvider.deleteCustomerPaidCard(request);

        return response;

    }


}
