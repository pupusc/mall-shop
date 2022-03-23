package com.wanmi.sbc.customer.service;

import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.CustomerDetailQueryRequest;
import com.wanmi.sbc.customer.account.repository.CustomerAccountRepository;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerAddResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailPageForSupplierResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailPageResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetForSupplierResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeAccountResponse;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.dto.CustomerDetailFromEsDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.service.CompanyInfoService;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetailInitEs;
import com.wanmi.sbc.customer.detail.repository.CustomerDetailRepository;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerService;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.employee.service.EmployeeService;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import com.wanmi.sbc.customer.enterpriseinfo.service.EnterpriseInfoService;
import com.wanmi.sbc.customer.growthvalue.service.GrowthValueIncreaseService;
import com.wanmi.sbc.customer.invoice.model.entity.CustomerInvoiceQueryRequest;
import com.wanmi.sbc.customer.invoice.repository.CustomerInvoiceRepository;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.paidcardcustomerrel.service.PaidCardCustomerRelService;
import com.wanmi.sbc.customer.points.service.CustomerPointsDetailService;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.customer.sms.SmsSendUtil;
import com.wanmi.sbc.customer.storecustomer.repository.StoreCustomerRepository;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.storecustomer.service.StoreCustomerService;
import com.wanmi.sbc.customer.storelevel.model.entity.StoreLevelQueryRequest;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import com.wanmi.sbc.customer.storelevel.repository.StoreLevelRepository;
import com.wanmi.sbc.customer.storelevel.service.StoreLevelService;
import com.wanmi.sbc.customer.util.QueryConditionsUtil;
import com.wanmi.sbc.customer.util.SafeLevelUtil;
import com.wanmi.sbc.customer.util.mapper.CustomerDetailMapper;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 会员服务接口
 * Created by CHENLI on 2017/4/19.
 */
@Service
public class CustomerService {
    @Autowired
    CustomerAresService customerAresService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDetailRepository customerDetailRepository;

    @Autowired
    private CustomerLevelService customerLevelService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private StoreCustomerRepository storeCustomerRepository;

    @Autowired
    private StoreLevelRepository storeLevelRepository;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private StoreLevelService storeLevelService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private DistributionCustomerService distributionCustomerService;

    @Autowired
    private GrowthValueIncreaseService growthValueIncreaseService;

    @Autowired
    private CustomerPointsDetailService customerPointsDetailService;

    @Autowired
    private EnterpriseInfoService enterpriseInfoService;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Autowired
    private StoreCustomerService storeCustomerService;

    @Autowired
    private PaidCardCustomerRelService paidCardCustomerRelService;

    /**
     * 条件查询会员带分页
     *
     * @param request
     * @return
     */
    public CustomerDetailPageResponse page(CustomerDetailPageRequest request) {
        request.setDelFlag(DeleteFlag.NO.toValue());
        Page<CustomerDetail> customerDetailPage = customerDetailRepository.findAll(QueryConditionsUtil
                .getWhereCriteria(request), request.getPageRequest());
        return pageHelper(customerDetailPage, request.getPageNum(), null);
    }

    /**
     * 分页查询企业会员列表
     *
     * @param request
     * @return
     */
    public CustomerDetailPageResponse pageForEnterpriseCustomer(EnterpriseCustomerPageRequest request) {
        StringBuilder sql = new StringBuilder();
        StringBuilder wheresql = new StringBuilder("");
        StringBuilder countsql = new StringBuilder("select count(1)\n" +
                "from customer\n" +
                "left join customer_detail detail on customer.customer_id = detail.customer_id\n" +
                "where customer.customer_id in (select customer_id from enterprise_info where del_flag = 0) and customer.del_flag = 0\n" +
                "and customer.enterprise_check_state in (1,2,3) ");
        sql.append("select detail.* \n" +
                "from customer\n" +
                "left join customer_detail detail on customer.customer_id = detail.customer_id\n" +
                "where customer.customer_id in (select customer_id from enterprise_info where del_flag = 0) and customer.del_flag = 0\n" +
                "and customer.enterprise_check_state in (1,2,3) ");

        if (StringUtils.isNotBlank(request.getCustomerName())) {
            wheresql.append(" and detail.customer_name like concat('%', :customerName,'%')");
        }
        if (StringUtils.isNotBlank(request.getCustomerAccount())) {
            wheresql.append(" and customer.customer_account like concat('%', :customerAccount,'%')");
        }
        if (StringUtils.isNotBlank(request.getEmployeeId())) {
            wheresql.append(" and detail.employee_id = :employeeId");
        }
        if (StringUtils.isNotBlank(request.getEnterpriseName())) {
            wheresql.append(" and enterprise.enterprise_name like concat('%', :enterpriseName,'%')");
        }
        if (request.getProvinceId() != null) {
            wheresql.append(" and detail.province_id = :provinceId");
        }
        if (request.getCityId() != null) {
            wheresql.append(" and detail.city_id = :cityId");
        }
        if (request.getAreaId() != null) {
            wheresql.append(" and detail.area_id = :areaId");
        }
        if (Objects.nonNull(request.getCustomerStatus())) {
            wheresql.append(" and detail.customer_status = :customerStatus");
        }
        if (request.getCustomerLevelId() != null) {
            wheresql.append(" and customer.customer_level_id = :customerLevelId");
        }
        if (request.getBusinessNatureType() != null) {
            wheresql.append(" and enterprise.business_nature_type = :businessNatureType");
        }
        if (Objects.nonNull(request.getEnterpriseCheckState())) {
            wheresql.append(" and customer.enterprise_check_state = :enterpriseCheckState");
        }
        wheresql.append(" order by detail.create_time desc");
        Query query = entityManager.createNativeQuery(sql.append(wheresql).toString());
        //组装查询参数
        this.wrapperQueryParam(query, request);
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).addEntity("detail", CustomerDetail.class);
        List<CustomerDetail> customerDetailList = query.getResultList();
        long count = 0;
        if (CollectionUtils.isNotEmpty(customerDetailList)) {
            Query countQuery = entityManager.createNativeQuery(countsql.append(wheresql).toString());
            //组装查询参数
            this.wrapperQueryParam(countQuery, request);
            count = Long.parseLong(countQuery.getSingleResult().toString());
        }
        Page<CustomerDetail> customerDetailPage = new PageImpl<>(customerDetailList, request.getPageable(), count);

        return pageHelper(customerDetailPage, request.getPageNum(), null);
    }

    /**
     * 条件查询会员带分页(S2b)
     *
     * @param request
     * @return
     */
    public CustomerDetailPageForSupplierResponse pageForS2bSupplier(CustomerDetailPageForSupplierRequest request) {
        Page<Customer> customerPage = customerRepository.findAll(QueryConditionsUtil.getWhereCriteria(request),
                request.getPageRequest());
        Page<CustomerDetail> customerDetailPage = customerPage.map(Customer::getCustomerDetail);
        CustomerDetailPageResponse customerDetailPageResponse = pageHelper(customerDetailPage, request.getPageNum(),
                request.getCompanyInfoId());

        CustomerDetailPageForSupplierResponse response = new CustomerDetailPageForSupplierResponse();
        response.setCurrentPage(customerDetailPageResponse.getCurrentPage());
        response.setDetailResponseList(customerDetailPageResponse.getDetailResponseList());
        response.setTotal(customerDetailPageResponse.getTotal());

        return response;
    }


    /**
     * @param customerDetailPage
     * @param pageNum
     * @param companyInfoId      存在，则是s2b-商家端
     * @return
     */
//    private CustomerQueryResponse pageHelper(Page<CustomerDetail> customerDetailPage, int pageNum, Long companyInfoId) {
//        CustomerQueryResponse customerQueryResponse = new CustomerQueryResponse();
//
//        customerQueryResponse.setCurrentPage(pageNum);
//        if (CollectionUtils.isEmpty(customerDetailPage.getContent())) {
//            customerQueryResponse.setDetailResponseList(Collections.emptyList());
//            customerQueryResponse.setTotal(0L);
//            return customerQueryResponse;
//        }
//
//        //先把属性值复制到CustomerDetailResponse对象里
//        List<CustomerDetailResponse> customerDetailResponses = customerDetailPage.getContent().stream().map
//                (customerDetail -> {
//            CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();
//            Customer customer = customerDetail.getCustomer();
//            BeanUtils.copyProperties(customerDetail, customerDetailResponse);
//            customerDetailResponse.setCustomerId(customer.getCustomerId());
//            customerDetailResponse.setCustomerAccount(customer.getCustomerAccount());
//            customerDetailResponse.setCheckState(customer.getCheckState());
//            //如果companyInfoId不为空，则是s2b-supplier端的请求
//            if (companyInfoId != null) {
//                //商家和客户不管是从属关系还是关联关系都是一对一的
//                Optional<StoreCustomerRela> optionalStoreCustomerRela = customer.getStoreCustomerRelaListByAll()
//                        .stream().filter(v -> v.getCompanyInfoId() == companyInfoId).findFirst();
//                if (optionalStoreCustomerRela.isPresent()) {
//                    customerDetailResponse.setCustomerLevelId(optionalStoreCustomerRela.get().getCustomerLevelId());
//                    customerDetailResponse.setMyCustomer(optionalStoreCustomerRela.get().getCustomerType() ==
//                            CustomerType.SUPPLIER);
//                } else {
//                    //数据正常不会走到这。。。
//                    customerDetailResponse.setCustomerLevelId(null);
//                    customerDetailResponse.setMyCustomer(false);
//                }
//            } else {
//                customerDetailResponse.setCustomerLevelId(customer.getCustomerLevelId());
//                customerDetailResponse.setCustomerType(customer.getCustomerType());
//            }
//            return customerDetailResponse;
//        }).collect(Collectors.toList());
//
//        //客户等级
//        List<Long> levelIds = customerDetailResponses.stream().filter(v -> v.getCustomerLevelId() != null).map(v -> v
//                .getCustomerLevelId()).collect(toList());
//        if (Objects.nonNull(levelIds) && !levelIds.isEmpty()) {
//            List<CustomerLevel> customerLevels = customerLevelService.findByCustomerLevelIds(levelIds);
//            IteratorUtils.zip(customerDetailResponses, customerLevels,
//                    (collect1, levels1) -> Objects.nonNull(collect1.getCustomerLevelId()) &&
//                            collect1.getCustomerLevelId().equals(levels1.getCustomerLevelId()),
//                    (collect2, levels2) -> {
//                        collect2.setCustomerLevelName(levels2.getCustomerLevelName());
//                    }
//            );
//        }
//
//        //遍历得到Employee对象，获取相应属性
//        List<String> employeeIds;
//        if (companyInfoId != null) {//商家列表 -- 商家的平台客户不需要显示业务员，只有所属客户才显示业务员
//            employeeIds = customerDetailResponses.stream().filter(v -> v.isMyCustomer() && v.getEmployeeId() != null)
//                    .map(v -> v.getEmployeeId()).collect(toList());
//        } else {//平台 -- 商家客户不显示业务员，平台客户才显示业务员
//            employeeIds = customerDetailResponses.stream().filter(v -> v.getCustomerType() == CustomerType.PLATFORM
//                    && v.getEmployeeId() != null).map(v -> v.getEmployeeId()).collect(toList());
//        }
//        if (Objects.nonNull(employeeIds) && !employeeIds.isEmpty()) {
//            List<Employee> employeeList = employeeService.findByEmployeeIds(employeeIds);
//            IteratorUtils.zip(customerDetailResponses, employeeList,
//                    (collect1, employee1) -> !StringUtils.isEmpty(collect1.getEmployeeId()) && collect1.getEmployeeId
//                            ().equals(employee1.getEmployeeId()),
//                    (collect2, employee2) -> {
//                        collect2.setEmployeeName(StringUtils.isEmpty(employee2.getEmployeeName()) ?
//                                employee2.getEmployeeMobile() : employee2.getEmployeeName());
//                    }
//            );
//        }
//
//        customerQueryResponse.setTotal(customerDetailPage.getTotalElements());
//        customerQueryResponse.setDetailResponseList(customerDetailResponses);
//
//        return customerQueryResponse;
//    }
    private CustomerDetailPageResponse pageHelper(Page<CustomerDetail> customerDetailPage, int pageNum, Long companyInfoId) {
        CustomerDetailPageResponse customerQueryResponse = new CustomerDetailPageResponse();

        customerQueryResponse.setCurrentPage(pageNum);
        if (CollectionUtils.isEmpty(customerDetailPage.getContent())) {
            customerQueryResponse.setDetailResponseList(Collections.emptyList());
            customerQueryResponse.setTotal(0L);
            return customerQueryResponse;
        }

        //先把属性值复制到CustomerDetailResponse对象里
        List<CustomerDetailForPageVO> customerDetailResponses = customerDetailPage.getContent().stream().map
                (customerDetail -> {
                    CustomerDetailForPageVO customerDetailResponse = new CustomerDetailForPageVO();
                    Customer customer = customerDetail.getCustomer();
                    BeanUtils.copyProperties(customerDetail, customerDetailResponse);
                    customerDetailResponse.setCustomerId(customer.getCustomerId());
                    customerDetailResponse.setCustomerAccount(customer.getCustomerAccount());
                    customerDetailResponse.setCheckState(customer.getCheckState());
                    customerDetailResponse.setGrowthValue(customer.getGrowthValue());
                    customerDetailResponse.setPointsAvailable(customer.getPointsAvailable());
                    customerDetailResponse.setPointsUsed(customer.getPointsUsed());
                    customerDetailResponse.setEnterpriseCheckState(customer.getEnterpriseCheckState());
                    customerDetailResponse.setEnterpriseCheckReason(customer.getEnterpriseCheckReason());
                    //如果companyInfoId不为空，则是s2b-supplier端的请求
                    if (companyInfoId != null) {
                        //商家和客户不管是从属关系还是关联关系都是一对一的
                        Optional<StoreCustomerRela> optionalStoreCustomerRela = customer.getStoreCustomerRelaListByAll()
                                .stream().filter(v -> v.getCompanyInfoId().equals(companyInfoId)).findFirst();
                        if (optionalStoreCustomerRela.isPresent()) {
                            //非自营店铺存储的是店铺等级Id
                            customerDetailResponse.setCustomerLevelId(optionalStoreCustomerRela.get().getStoreLevelId());
                            customerDetailResponse.setMyCustomer(optionalStoreCustomerRela.get().getCustomerType() ==
                                    CustomerType.SUPPLIER);
                        } else {
                            //自营店铺存储的是平台等级Id
                            customerDetailResponse.setCustomerLevelId(customer.getCustomerLevelId());
                            customerDetailResponse.setCustomerType(customer.getCustomerType());
                        }
                    } else {
                        customerDetailResponse.setCustomerLevelId(customer.getCustomerLevelId());
                        customerDetailResponse.setCustomerType(customer.getCustomerType());
                    }
                    return customerDetailResponse;
                }).collect(Collectors.toList());

        //客户等级
        List<Long> levelIds = customerDetailResponses.stream().filter(v -> v.getCustomerLevelId() != null).map(v -> v
                .getCustomerLevelId()).collect(toList());
        if (Objects.nonNull(levelIds) && !levelIds.isEmpty()) {
            List<CustomerLevel> customerLevels = customerLevelService.findByCustomerLevelIds(levelIds);
            IteratorUtils.zip(customerDetailResponses, customerLevels,
                    (collect1, levels1) -> Objects.nonNull(collect1.getCustomerLevelId()) &&
                            collect1.getCustomerLevelId().equals(levels1.getCustomerLevelId()),
                    (collect2, levels2) -> {
                        collect2.setCustomerLevelName(levels2.getCustomerLevelName());
                    }
            );

            //如果companyInfoId不为空，则是s2b-supplier端的请求
            if (companyInfoId != null) {
                //判断是否是非自营店铺
                CompanyInfo companyInfo = companyInfoService.findOne(companyInfoId);
                if (companyInfo.getCompanyType().equals(BoolFlag.YES)) {
                    List<StoreLevel> storeLevels = storeLevelRepository.findAll(StoreLevelQueryRequest.builder()
                            .storeLevelIds(levelIds)
                            .build().getWhereCriteria()
                    );
                    IteratorUtils.zip(customerDetailResponses, storeLevels,
                            (collect1, levels1) -> Objects.nonNull(collect1.getCustomerLevelId()) &&
                                    collect1.getCustomerLevelId().equals(levels1.getStoreLevelId()),
                            (collect2, levels2) -> {
                                collect2.setCustomerLevelName(levels2.getLevelName());
                            }
                    );
                }
            }
        }

        //遍历得到Employee对象，获取相应属性
        List<String> employeeIds;
        if (companyInfoId != null) {//商家列表 -- 商家的平台客户不需要显示业务员，只有所属客户才显示业务员
            employeeIds = customerDetailResponses.stream().filter(v -> v.isMyCustomer() && v.getEmployeeId() != null)
                    .map(v -> v.getEmployeeId()).collect(toList());
        } else {//平台 -- 商家客户不显示业务员，平台客户才显示业务员
            employeeIds = customerDetailResponses.stream().filter(v -> v.getCustomerType() == CustomerType.PLATFORM
                    && v.getEmployeeId() != null).map(v -> v.getEmployeeId()).collect(toList());
        }
        if (Objects.nonNull(employeeIds) && !employeeIds.isEmpty()) {
            Optional<List<Employee>> employeeListOptional = employeeService.findByEmployeeIds(employeeIds);
            IteratorUtils.zip(customerDetailResponses, employeeListOptional.get(),
                    (collect1, employee1) -> !StringUtils.isEmpty(collect1.getEmployeeId()) && collect1.getEmployeeId
                            ().equals(employee1.getEmployeeId()),
                    (collect2, employee2) -> {
                        collect2.setEmployeeName(StringUtils.isEmpty(employee2.getEmployeeName()) ?
                                employee2.getEmployeeMobile() : employee2.getEmployeeName());
                    }
            );
        }

        customerQueryResponse.setTotal(customerDetailPage.getTotalElements());
        customerQueryResponse.setDetailResponseList(customerDetailResponses);

        return customerQueryResponse;
    }

    /**
     * 多条件查询会员详细信息
     *
     * @param
     * @return
     */
    public Optional<List<CustomerDetail>> findDetailByCondition(CustomerDetailListByConditionRequest request) {
        request.setDelFlag(DeleteFlag.NO.toValue());
        return Optional.ofNullable(customerDetailRepository.findAll(QueryConditionsUtil.getWhereCriteria(request)));
    }

    /**
     * 代客下单 autocomplete
     *
     * @param customerDetailQueryRequest
     * @returnaccount/orderInvoice
     */
    public List<CustomerDetail> findDetailForOrder(CustomerDetailListForOrderRequest customerDetailQueryRequest) {
        customerDetailQueryRequest.setPageNum(0);
        customerDetailQueryRequest.setPageSize(5);

        CustomerDetailListByPageRequest request = new CustomerDetailListByPageRequest();

        KsBeanUtil.copyPropertiesThird(customerDetailQueryRequest, request);

        return this.findDetailByPage(request);
    }

    /**
     * 分页获取客户列表
     *
     * @param customerDetailQueryRequest
     * @return
     */
//    public List<CustomerDetail> findDetailByPage(CustomerDetailQueryRequest customerDetailQueryRequest) {
//        customerDetailQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        Page<CustomerDetail> customerDetailPage = customerDetailRepository.findAll(customerDetailQueryRequest
//                .getWhereCriteria(), customerDetailQueryRequest.getPageRequest());
//        return customerDetailPage.getContent();
//    }
    public List<CustomerDetail> findDetailByPage(CustomerDetailListByPageRequest customerDetailQueryRequest) {
        customerDetailQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        Page<CustomerDetail> customerDetailPage = customerDetailRepository.findAll(QueryConditionsUtil
                .getWhereCriteria(customerDetailQueryRequest), customerDetailQueryRequest.getPageRequest());
        return customerDetailPage.getContent();
    }

    /**
     * 根据业务员id获取客户列表
     *
     * @param
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    public List<String> findDetaileByEmployeeId(CustomerDetailQueryRequest customerDetailQueryRequest) {
//        customerDetailQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        List<String> customerDetails1 = customerDetailRepository.queryAllCustomerIdByEmployeeId
//                (customerDetailQueryRequest.getEmployeeId(), DeleteFlag.NO);
//        return customerDetails1;
//    }
    public List<String> findDetaileByEmployeeId(CustomerIdListRequest request) {
        List<String> customerDetails1 = customerDetailRepository.queryAllCustomerIdByEmployeeId
                (request.getEmployeeId(), DeleteFlag.NO);
        return customerDetails1;
    }

    /**
     * 根据业务员id获取客户列表
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<String> queryAllCustomerId() {
        return customerDetailRepository.queryAllCustomerId(DeleteFlag.NO);
    }

    /**
     * 模糊查询会员信息
     *
     * @param queryRequest
     * @return
     */
//    public List<Customer> findCustomerByCondition(CustomerQueryRequest queryRequest) {
//        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        return customerRepository.findAll(queryRequest.getWhereCriteria());
//    }
    public Optional<List<Customer>> findCustomerByCondition(CustomerListByConditionRequest queryRequest) {
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        return Optional.ofNullable(customerRepository.findAll(QueryConditionsUtil.getWhereCriteria(queryRequest)));
    }


    /**
     * 查询单条会员信息
     *
     * @param customerId
     * @return
     */
    public Customer findById(String customerId) {
        return customerRepository.findByCustomerIdAndDelFlag(customerId, DeleteFlag.NO);
    }

    /**
     * 根据会员ID查询会员的可用积分
     * @param customerId
     * @return
     */
    @MasterRouteOnly
    public Long getPointsAvailable(String customerId) {
        Long pointsAvailable = customerRepository.findPointsAvailableByCustomerId(customerId);
        return Objects.isNull(pointsAvailable) ? NumberUtils.LONG_ZERO : pointsAvailable;
    }

    /**
     * 批量查询会员信息
     *
     * @param idList
     * @return
     */
    public List<Customer> findByCustomerIdIn(Collection<String> idList) {
        return customerRepository.findByCustomerIdIn(idList);
    }

    /**
     * 查询
     *
     * @param customerId
     * @return
     */
    public Boolean findCustomerDelFlag(String customerId) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if (!optional.isPresent()) {
            return Boolean.TRUE;
        }
        return optional.get().getDelFlag().toValue() == 1;
    }

    /**
     * 查询是否有客户获取了成长值
     *
     * @return
     */
    public Boolean hasObtainedGrowthValue() {
        List<Customer> customers = customerRepository.findHasGrowthValueCustomer();
        if (CollectionUtils.isEmpty(customers)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 审核客户状态
     *
     * @param request
     * @return
     */
    @Transactional
//    public int updateCheckState(CustomerQueryRequest customerQueryRequest) {
//        //审核状态 0：待审核 1：已审核 2：审核未通过
//        CheckState checkState = customerQueryRequest.getCheckState() == 1 ? CheckState.CHECKED : CheckState.NOT_PASS;
//        int n = customerRepository.checkCustomerState(checkState, customerQueryRequest.getCustomerId());
//        customerDetailRepository.updateRejectReason(customerQueryRequest.getRejectReason(), customerQueryRequest
//                .getCustomerId());
//        //ares埋点-会员-后台修改客户审核状态
//        customerAresService.dispatchFunction("editCustomerCheckState", checkState, customerQueryRequest.getCustomerId
//                ());
//        return n;
//    }
    public int updateCheckState(CustomerCheckStateModifyRequest request) {
        //审核状态 0：待审核 1：已审核 2：审核未通过
        CheckState checkState = request.getCheckState() == 1 ? CheckState.CHECKED : CheckState.NOT_PASS;
        int n = customerRepository.checkCustomerState(checkState, request.getCustomerId(), LocalDateTime.now());
        customerDetailRepository.updateRejectReason(request.getRejectReason(), request
                .getCustomerId());
        //ares埋点-会员-后台修改客户审核状态
        customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_CHECK_STATE, checkState, request
                .getCustomerId
                        ());
        Customer customer = this.findById(request.getCustomerId());
        // 用户审核通过并且会员已完善个人信息，增加成长值/积分
        if (request.getCheckState() == 1 &&
                customer.getCustomerDetail().getAreaId() != null && customer.getCustomerDetail().getCustomerAddress() != null) {
            addGrowValueAndPoint(customer);
        }
        return n;
    }

    /**
     * 审核企业会员
     *
     * @param request
     * @return
     */
    @Transactional
    public int checkEnterpriseCustomer(CustomerEnterpriseCheckStateModifyRequest request) {
        CheckState checkState = EnterpriseCheckState.CHECKED.equals(request.getEnterpriseCheckState()) ?
                CheckState.CHECKED : CheckState.NOT_PASS;
        int n = customerRepository.checkEnterpriseCustomer(request.getEnterpriseCheckState(), checkState,
                request.getEnterpriseCheckReason(), request.getCustomerId(), LocalDateTime.now());

        //ares埋点-会员-后台修改客户审核状态
        customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_CHECK_STATE, request.getEnterpriseCheckState(), request
                .getCustomerId
                        ());
        Customer customer = this.findById(request.getCustomerId());
        // 用户审核通过并且会员已完善个人信息，增加成长值/积分
        if (EnterpriseCheckState.CHECKED.equals(request.getEnterpriseCheckState()) &&
                customer.getCustomerDetail().getAreaId() != null && customer.getCustomerDetail().getCustomerAddress() != null) {
            addGrowValueAndPoint(customer);
        }
        return n;
    }

    /**
     * 会员审核成功后增加成长值，积分
     *
     * @param customer
     */
    @Transactional
    public void addGrowValueAndPoint(Customer customer) {
        // 增加成长值
        CustomerGrowthValueAddRequest growthValueAddRequest = CustomerGrowthValueAddRequest.builder()
                .customerId(customer.getCustomerId())
                .type(OperateType.GROWTH)
                .serviceType(GrowthValueServiceType.PERFECTINFO)
                .build();
        growthValueIncreaseService.increaseGrowthValue(growthValueAddRequest);
        // 增加积分
        CustomerPointsDetailAddRequest pointsDetailAddRequest = CustomerPointsDetailAddRequest.builder()
                .customerId(customer.getCustomerId())
                .type(OperateType.GROWTH)
                .serviceType(PointsServiceType.PERFECTINFO)
                .build();
        customerPointsDetailService.increasePoints(pointsDetailAddRequest, ConfigType.POINTS_BASIC_RULE_COMPLETE_INFORMATION);
    }

    /**
     * boss批量删除会员
     * 删除会员
     * 删除会员详情表
     *
     * @param customerIds
     * @return
     */
    @Transactional
    public int delete(List<String> customerIds) {
        customerDetailRepository.deleteByCustomerId(customerIds);
        customerInvoiceRepository.deleteCustomerInvoiceByCustomerIds(customerIds);
        customerAccountRepository.deleteCustomerAccountByCustomerIds(customerIds);
        int n = customerRepository.deleteByCustomerId(customerIds);

        //ares埋点-会员-后台单个或批量删除会员
        customerAresService.dispatchFunction(AresFunctionType.DEL_CUSTOMER, customerIds);
        return n;
    }


    /**
     * 检验账户是否存在
     *
     * @param
     * @return
     */
    public Customer findByCustomerAccountAndDelFlag(String customerAccount) {
        return customerRepository.findByCustomerAccountAndDelFlag(customerAccount, DeleteFlag.NO);
    }

    /**
     * 通过Fandengid 查询用户
     *
     * @param
     * @return
     */
    public Customer findByFanDengUserNoAndDelFlag(String fanDengId) {
        return customerRepository.findByFanDengUserNoAndDelFlag(fanDengId, DeleteFlag.NO);
    }

    /**
     * 通过Fandengid 查询用户
     *
     * @param
     * @return
     */
    @Transactional
    public Integer updateFanDengId(String customerId,String fanDengId,String loginIp) {
        return customerRepository.updateLoginTimeAndFanDengUserNo(customerId,LocalDateTime.now(),loginIp,fanDengId);
    }

    @Transactional
    public Integer updateOpenIdAndUnionId(String customerId, String openId, String unionId) {
        return customerRepository.updateOpenIdAndUnionId(customerId, openId, unionId);
    }

    /**
     * 检查账户是否禁用
     *
     * @param customerAccount
     * @return
     */
    public CustomerDetail findDisableCustomer(String customerAccount) {
        CustomerDetailQueryRequest request = new CustomerDetailQueryRequest();
        request.setCustomerAccount(customerAccount);
        request.setCustomerStatus(CustomerStatus.DISABLE);
        return customerDetailRepository.findOne(request.getWhereCriteria()).orElse(null);
    }

    /**
     * 非c端-新增客户共通
     *
     * @param
     * @param
     * @param
     * @return
     */
    @Transactional
    public CustomerAddResponse saveCustomerAll(CustomerAddRequest request) {
        Customer customer = this.getCustomer(request);
        customer.setCreatePerson(request.getOperator());
        if (Objects.isNull(request.getCustomerLevelId()) || request.isS2bSupplier()) {
            //级别为null 或者 是商家新增会员时 给一个默认的平台会员等级
            customer.setCustomerLevelId(customerLevelService.getDefaultLevel().getCustomerLevelId());
        }
        CustomerDetail customerDetail = this.getCustomerDetail(request);
        customerDetail.setCreatePerson(request.getOperator());
        Customer customerInit = customerRepository.save(customer);

        customerInit.setSafeLevel(SafeLevelUtil.getSafeLevel(customerInit.getCustomerPassword()));
        String saltVal = SecurityUtil.getNewPsw(); //生成盐值
        String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customerInit.getCustomerId()), customerInit
                .getCustomerPassword(), saltVal); //生成加密后的登录密码
        //发送短信
        if (request.isEnterpriseCustomer()) {
            sendEnterpriseMsg(customer.getCustomerAccount(), customer.getCustomerPassword());
        } else {
            sendMsg(customer.getCustomerAccount(), customer.getCustomerPassword());
        }
        customerInit.setCustomerSaltVal(saltVal);
        customerInit.setCustomerPassword(encryptPwd);
        customer = customerRepository.save(customerInit);
        customerDetail.setCustomerId(customer.getCustomerId());
        customerDetail = customerDetailRepository.save(customerDetail);

        StoreCustomerRela storeCustomerRela = null;
        //S2b的商家端-业务处理
        if (request.isS2bSupplier()) {
            //设置的等级信息放在客户-商家关系表中
            storeCustomerRela = new StoreCustomerRela();
            BeanUtils.copyProperties(request, storeCustomerRela);
            storeCustomerRela.setCustomerId(customer.getCustomerId());
            storeCustomerRela.setStoreLevelId(request.getCustomerLevelId());
            storeCustomerRepository.save(storeCustomerRela);
        }

        CustomerAddResponse response = new CustomerAddResponse();
        KsBeanUtil.copyPropertiesThird(request, response);
        //企业信息新增
        EnterpriseInfo enterpriseInfo = null;
        if (request.isEnterpriseCustomer()) {
            enterpriseInfo = new EnterpriseInfo();
            KsBeanUtil.copyProperties(request.getEnterpriseInfo(), enterpriseInfo);
            enterpriseInfo.setCustomerId(customer.getCustomerId());
            enterpriseInfo.setCreatePerson(request.getOperator());
            enterpriseInfo = enterpriseInfoService.add(enterpriseInfo);
            response.setEnterpriseInfo(KsBeanUtil.convert(enterpriseInfo, EnterpriseInfoVO.class));
        }
        response.setCustomerId(customer.getCustomerId());

        CustomerDetailToEsVO customerDetailToEsVO = customerDetailMapper.customerDetailToCustomerDetailToEsVO(customerDetail);
        customerDetailToEsVO.setCustomerAccount(customer.getCustomerAccount());
        customerDetailToEsVO.setCustomerLevelId(customer.getCustomerLevelId());
        customerDetailToEsVO.setCheckState(customer.getCheckState());
        customerDetailToEsVO.setStoreCustomerRela(Objects.isNull(storeCustomerRela) ? null : KsBeanUtil.convert(storeCustomerRela, StoreCustomerRelaVO.class));
        customerDetailToEsVO.setEnterpriseInfo(response.getEnterpriseInfo());
        customerDetailToEsVO.setEnterpriseCheckState(customer.getEnterpriseCheckState());
        customerDetailToEsVO.setEnterpriseCheckReason(customer.getEnterpriseCheckReason());
        response.setCustomerDetailToEsVO(customerDetailToEsVO);
        //初始化会员资金信息
        producerService.initCustomerFunds(customerDetail.getCustomerId(), customerDetail.getCustomerName(), customer.getCustomerAccount());
        //ares埋点-会员-后台添加会员
        customerAresService.dispatchFunction(AresFunctionType.ADD_CUSTOMER, customer, customerDetail);
        return response;
    }




    /**
     * @Author yangzhen
     * @Description //boss 新增分销员
     * @Date 9:53 2020/12/23
     * @Param [request]
     * @return com.wanmi.sbc.customer.api.response.customer.CustomerAddResponse
     **/
    @Transactional
    public CustomerAddResponse bossSaveCustomer(CustomerAddRequest request) {
        Customer customer = this.getCustomer(request);
        customer.setCreatePerson(request.getOperator());
        if (Objects.isNull(request.getCustomerLevelId()) || request.isS2bSupplier()) {
            //级别为null 或者 是商家新增会员时 给一个默认的平台会员等级
            customer.setCustomerLevelId(customerLevelService.getDefaultLevel().getCustomerLevelId());
        }
        CustomerDetail customerDetail = this.getCustomerDetail(request);
        customerDetail.setCreatePerson(request.getOperator());
        Customer customerInit = customerRepository.save(customer);

        customerInit.setSafeLevel(SafeLevelUtil.getSafeLevel(customerInit.getCustomerPassword()));
        String saltVal = SecurityUtil.getNewPsw(); //生成盐值
        String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customerInit.getCustomerId()), customerInit
                .getCustomerPassword(), saltVal); //生成加密后的登录密码
        //发送短信
        if (request.isEnterpriseCustomer()) {
            sendEnterpriseMsg(customer.getCustomerAccount(), customer.getCustomerPassword());
        } else {
            sendMsg(customer.getCustomerAccount(), customer.getCustomerPassword());
        }
        customerInit.setCustomerSaltVal(saltVal);
        customerInit.setCustomerPassword(encryptPwd);
        customer = customerRepository.save(customerInit);
        customerDetail.setCustomerId(customer.getCustomerId());
        customerDetail = customerDetailRepository.save(customerDetail);

        StoreCustomerRela storeCustomerRela = null;
        //S2b的商家端-业务处理
        if (request.isS2bSupplier()) {
            //设置的等级信息放在客户-商家关系表中
            storeCustomerRela = new StoreCustomerRela();
            BeanUtils.copyProperties(request, storeCustomerRela);
            storeCustomerRela.setCustomerId(customer.getCustomerId());
            storeCustomerRela.setStoreLevelId(request.getCustomerLevelId());
            storeCustomerRepository.save(storeCustomerRela);
        }

        CustomerAddResponse response = new CustomerAddResponse();
        KsBeanUtil.copyPropertiesThird(request, response);
        //企业信息新增
        EnterpriseInfo enterpriseInfo = null;
        if (request.isEnterpriseCustomer()) {
            enterpriseInfo = new EnterpriseInfo();
            KsBeanUtil.copyProperties(request.getEnterpriseInfo(), enterpriseInfo);
            enterpriseInfo.setCustomerId(customer.getCustomerId());
            enterpriseInfo.setCreatePerson(request.getOperator());
            enterpriseInfo = enterpriseInfoService.add(enterpriseInfo);
            response.setEnterpriseInfo(KsBeanUtil.convert(enterpriseInfo, EnterpriseInfoVO.class));
        }
        response.setCustomerId(customer.getCustomerId());

        CustomerDetailToEsVO customerDetailToEsVO = customerDetailMapper.customerDetailToCustomerDetailToEsVO(customerDetail);
        customerDetailToEsVO.setCustomerAccount(customer.getCustomerAccount());
        customerDetailToEsVO.setCustomerLevelId(customer.getCustomerLevelId());
        customerDetailToEsVO.setCheckState(customer.getCheckState());
        customerDetailToEsVO.setStoreCustomerRela(Objects.isNull(storeCustomerRela) ? null : KsBeanUtil.convert(storeCustomerRela, StoreCustomerRelaVO.class));
        customerDetailToEsVO.setEnterpriseInfo(response.getEnterpriseInfo());
        customerDetailToEsVO.setEnterpriseCheckState(customer.getEnterpriseCheckState());
        customerDetailToEsVO.setEnterpriseCheckReason(customer.getEnterpriseCheckReason());
        response.setCustomerDetailToEsVO(customerDetailToEsVO);
        //ares埋点-会员-后台添加会员
        customerAresService.dispatchFunction(AresFunctionType.ADD_CUSTOMER, customer, customerDetail);
        return response;
    }


    /**
     * Boss端修改会员
     * 修改会员表，修改会员详细信息
     *
     * @param
     */
    @Transactional(rollbackFor = Exception.class)
    public CustomerDetailToEsVO updateCustomerAll(CustomerModifyRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        if (Objects.isNull(customer)) {
            throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
        }
        CustomerDetail customerDetail = customerDetailRepository.findById(request.getCustomerDetailId()).orElse(null);
        if (Objects.isNull(customerDetail)) {
            throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
        }
        KsBeanUtil.copyProperties(request, customer);
        KsBeanUtil.copyProperties(request, customerDetail);

        customer.setUpdateTime(LocalDateTime.now());
        customer.setUpdatePerson(request.getOperator());

        customerDetail.setUpdateTime(LocalDateTime.now());
        customerDetail.setUpdatePerson(request.getOperator());


        //重置客户账号相关
        if (request.isPassReset() && StringUtils.isNotEmpty(request
                .getCustomerAccountForReset())) {

            Customer ifExists = this.findByCustomerAccountAndDelFlag(request.getCustomerAccountForReset());
            if (ifExists != null) {
                throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_ACCOUNT_EXISTS_FOR_RESET);
            }

            customer.setCustomerAccount(request.getCustomerAccountForReset());
            customer.setCustomerPassword(this.getRandomPwd());
            customer.setSafeLevel(SafeLevelUtil.getSafeLevel(customer.getCustomerPassword()));
            String saltVal = SecurityUtil.getNewPsw(); //生成盐值
            String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), customer
                    .getCustomerPassword(), saltVal); //生成加密后的登录密码
            sendMsg(customer.getCustomerAccount(), customer.getCustomerPassword());
            customer.setCustomerSaltVal(saltVal);
            customer.setCustomerPassword(encryptPwd);
        }

        customerRepository.save(customer);
        customerDetailRepository.save(customerDetail);
        //企业信息新增
        EnterpriseInfo enterpriseInfo = null;
        if (!Objects.equals(EnterpriseCheckState.INIT, customer.getEnterpriseCheckState())) {
            enterpriseInfo = enterpriseInfoService.getOne(request.getEnterpriseInfo().getEnterpriseId());
            if (Objects.nonNull(enterpriseInfo)) {
                KsBeanUtil.copyProperties(request.getEnterpriseInfo(), enterpriseInfo);
                enterpriseInfo.setUpdatePerson(request.getOperator());
                enterpriseInfo = enterpriseInfoService.modify(enterpriseInfo);
            }
        }



        //修改会员名称，同时修改会员资金-会员名称、会员账号字段
        distributionCustomerService.updateCustomerNameAndAccountByCustomerId(customer.getCustomerId(), customer.getCustomerAccount(), customerDetail.getCustomerName());
        producerService.modifyCustomerNameAndAccountWithCustomerFunds(customer.getCustomerId(), customerDetail.getCustomerName(), customer.getCustomerAccount());
        //修改会员名称,同时修改会员提现管理-会员名称、会员账号字段
        producerService.modifyCustomerNameWithCustomerDrawCash(customer.getCustomerId(), customerDetail.getCustomerName());
        //修改会员账号，同时修改分销员-会员名称字段
        distributionCustomerService.updateCustomerNameByCustomerId(customer.getCustomerId(),customerDetail.getCustomerName());
        //ares埋点-会员-后台修改会员
        customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER, customer, customerDetail);

        CustomerDetailToEsVO customerDetailToEsVO = customerDetailMapper.customerDetailToCustomerDetailToEsVO(customerDetail);
        customerDetailToEsVO.setCustomerAccount(customer.getCustomerAccount());
        customerDetailToEsVO.setCustomerLevelId(customer.getCustomerLevelId());
        customerDetailToEsVO.setEnterpriseInfo(KsBeanUtil.convert(enterpriseInfo,EnterpriseInfoVO.class));
        return customerDetailToEsVO;
    }

    /**
     * 根据编号获取会员
     *
     * @param customerId 会员ID
     * @return
     */
    public Customer findInfoById(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    /**
     * 修改绑定手机号
     *
     * @param
     * @param
     * @return
     */
    @Transactional
    public int updateCustomerAccount(CustomerAccountModifyRequest request) {
        int result = customerRepository.updateCustomerAccount(request.getCustomerId(), request.getCustomerAccount());
        if (Constants.yes == result) {
            //修改会员账号，同时修改会员资金-会员账号字段
            producerService.modifyCustomerAccountWithCustomerFunds(request.getCustomerId(), request.getCustomerAccount());
            //修改会员账号，同时修改分销员-会员账号字段
            distributionCustomerService.updateCustomerAccountByCustomerId(request.getCustomerId(), request.getCustomerAccount());
            //修改会员账号，同事修改会员提现管理-会员账号字段
            producerService.modifyCustomerAccountWithCustomerDrawCash(request.getCustomerId(), request.getCustomerAccount());
            //ares埋点-会员-会员修改绑定手机号
            customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_PHONE, request.getCustomerAccount(),
                    request.getCustomerId());

            return Constants.yes;
        }
        return Constants.no;
    }

    @Transactional
    public void updateCustomerSalesMan(String employeeId, AccountType accountType) {
        //如果是商家类型
        if (accountType.equals(AccountType.s2bSupplier) || accountType.equals(AccountType.s2bProvider)) {
            Employee employee = employeeService.findEmployeeById(employeeId).orElseThrow(() -> new
                    SbcRuntimeException(EmployeeErrorCode.NOT_EXIST));
            Optional<Employee> mainEmployeeOptional = employeeService.findByComanyId(employee.getCompanyInfo().getCompanyInfoId());
            customerRepository.updateCustomerByEmployeeId(employeeId, mainEmployeeOptional.get().getEmployeeId());
        } else {
            employeeService.findByAccountName("system", accountType).ifPresent(employee -> customerRepository
                    .updateCustomerByEmployeeId(employeeId, employee.getEmployeeId()));
        }
    }

    /**
     * 根据审核状态统计用户
     *
     * @param request
     * @return
     */
    public long countCustomerByState(CustomerCountByStateRequest request) {
        return customerRepository.count(QueryConditionsUtil.getWhereCriteria(request));
    }

    /**
     * 根据审核状态统计增票资质
     *
     * @return
     */
    public long countInvoiceByState(CheckState checkState) {
        CustomerInvoiceQueryRequest customerInvoiceQueryRequest = new CustomerInvoiceQueryRequest();
        customerInvoiceQueryRequest.setCheckState(checkState);
        return customerInvoiceRepository.count(customerInvoiceQueryRequest.getWhereCriteria());
    }

    /**
     * 获取新增的客户对象
     *
     * @param editRequest
     * @return
     */
    private Customer getCustomer(CustomerAddRequest editRequest) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(editRequest, customer);
        //BOSS端新增的客户，随机生成6位数字的密码
        customer.setCustomerPassword(this.getRandomPwd());
        customer.setGrowthValue(0L);
        customer.setPointsAvailable(0L);
        customer.setPointsUsed(0L);
        customer.setDelFlag(DeleteFlag.NO);
        customer.setCheckState(CheckState.CHECKED);
        customer.setCreateTime(LocalDateTime.now());
        customer.setCheckTime(LocalDateTime.now());
        customer.setEnterpriseCheckState(editRequest.isEnterpriseCustomer() ? EnterpriseCheckState.CHECKED : EnterpriseCheckState.INIT);
        return customer;
    }

    /**
     * 获取新增的客户详细信息对象
     *
     * @param editRequest
     * @return
     */
    private CustomerDetail getCustomerDetail(CustomerAddRequest editRequest) {
        CustomerDetail customerDetail = new CustomerDetail();
        BeanUtils.copyProperties(editRequest, customerDetail);
        customerDetail.setDelFlag(DeleteFlag.NO);
        customerDetail.setCustomerStatus(CustomerStatus.ENABLE);
        customerDetail.setCreateTime(LocalDateTime.now());
        customerDetail.setIsDistributor(DefaultFlag.NO);

        return customerDetail;
    }

    /**
     * 发送密码短信给客户
     *
     * @param mobile
     * @param password
     */
    private void sendMsg(String mobile, String password) {
        smsSendUtil.send(SmsTemplate.CUSTOMER_PASSWORD, new String[]{mobile}, mobile, password);
    }

    /**
     * 发送密码短信给企业用户
     *
     * @param mobile
     * @param password
     */
    private void sendEnterpriseMsg(String mobile, String password) {
        smsSendUtil.send(SmsTemplate.ENTERPRISE_CUSTOMER_PASSWORD, new String[]{mobile}, mobile, password);
    }

    /**
     * 生成6位数密码
     *
     * @return
     */
    private String getRandomPwd() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 按照客户名称模糊匹配，当前商家未关联的平台客户
     *
     * @param companyInfoId
     * @param customerAccount
     * @return
     */
    public List<Map<String, Object>> getCustomerNotRelated(Long companyInfoId, String customerAccount) {
        String sql = "SELECT d.customer_id customerId, c.customer_account customerAccount," +
                " d.customer_name customerName, c.customer_level_id customerLevelId \n" +
                "FROM customer_detail d\n" +
                "  LEFT JOIN customer c\n" +
                "    ON d.customer_id = c.customer_id\n" +
                "WHERE\n" +
                "  c.customer_account LIKE :customerAccount \n" +
                "  AND d.del_flag = 0\n" +
                "  AND c.check_state = 1\n" +
                "LIMIT 5";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("customerAccount", "%" + customerAccount + "%");
//        query.setParameter("companyInfoId", companyInfoId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> customerList = query.getResultList();
        return customerList;
    }


    /**
     * 获取客户详情信息，包括业务员名称，等级，等级扣率
     *
     * @param customerId
     * @param companyInfoId
     * @param storeId
     * @return
     */
    @Transactional
    public CustomerGetForSupplierResponse getCustomerResponseForSupplier(String customerId, Long companyInfoId, Long storeId) {
        Customer customer = this.findById(customerId);
        CustomerGetForSupplierResponse customerResponse = KsBeanUtil.convert(customer, CustomerGetForSupplierResponse.class);
        CompanyInfo companyInfo = companyInfoService.findOne(companyInfoId);
        if (companyInfo != null) {
            customerResponse.setSupplierName(companyInfo.getSupplierName());
        }
        if (!StringUtils.isEmpty(customerResponse.getCustomerDetail().getEmployeeId())) {
            Optional<EmployeeAccountResponse> employeeAccountResponseOptional = employeeService.findByEmployeeId(customerResponse.getCustomerDetail().getEmployeeId());
            employeeAccountResponseOptional.ifPresent(response -> {
                customerResponse.setEmployeeName(response.getEmployeeName());
            });
        }
        // 非自营店铺
        if (BoolFlag.YES.equals(companyInfo.getCompanyType())) {
            List<StoreCustomerRela> relaList = customer.getStoreCustomerRelaListByAll();
            if (relaList != null && !relaList.isEmpty()) {
                relaList = relaList.stream().filter(item -> item.getStoreId().longValue() == storeId.longValue()).collect(Collectors.toList());
                if (relaList != null && !relaList.isEmpty()) {
                    customerResponse.setCustomerLevelId(relaList.get(0).getStoreLevelId());
                    customerResponse.setMyCustomer(relaList.get(0).getCustomerType() ==
                            CustomerType.SUPPLIER);
                    customerResponse.setStoreCustomerRelaId(relaList.get(0).getId());
                    StoreLevel storeLevel = storeLevelService.getById(relaList.get(0).getStoreLevelId());
                    customerResponse.setCustomerLevelName(storeLevel.getLevelName());
                    customerResponse.setCustomerLevelDiscount(storeLevel.getDiscountRate());
                }
            }
        } else {
            // 自营店铺
            CustomerLevel customerLevel = customerLevelService.findById(customer.getCustomerLevelId()).orElse(null);
            if (customerLevel != null) {
                customerResponse.setCustomerLevelId(customerLevel.getCustomerLevelId());
                customerResponse.setMyCustomer(false);
                customerResponse.setCustomerLevelName(customerLevel.getCustomerLevelName());
                customerResponse.setCustomerLevelDiscount(customerLevel.getCustomerLevelDiscount());
            }
        }
        return customerResponse;
    }

    @Transactional
    public void updateCustomerToDistributor(CustomerToDistributorModifyRequest request) {
        CustomerDetail customerDetail = customerDetailRepository.findByCustomerId(request.getCustomerId());
        customerDetail.setCustomerId(request.getCustomerId());
        customerDetail.setIsDistributor(request.getIsDistributor());
        customerDetailRepository.save(customerDetail);
        producerService.updateCustomerToDistributor(request.getCustomerId(),request.getIsDistributor());
    }

    /**
     * 根据会员ID、是否删除查询会员基础信息
     *
     * @param customerId
     * @param deleteFlag
     * @return
     */
    public CustomerBase getBaseCustomerByCustomerIdAndDeleteFlag(String customerId, DeleteFlag deleteFlag) {
        CustomerBase customerBase = getByCustomerIdAndDeleteFlag(customerId, deleteFlag);
        String customerName = customerDetailRepository.getCustomerNameByCustomerId(customerId, deleteFlag);
        customerBase.setCustomerName(customerName);
        return customerBase;
    }

    /**
     * 根据会员ID、是否删除查询会员基础信息
     *
     * @param customerId
     * @param deleteFlag
     * @return
     */
    public CustomerBase getByCustomerIdAndDeleteFlag(String customerId, DeleteFlag deleteFlag) {
        CustomerBase customerBase = customerRepository.getBaseCustomerByCustomerId(customerId, deleteFlag);
        return customerBase;
    }

    /**
     * 判断达到该成长值的用户
     *
     * @param growthValue
     * @return
     */
    public List<String> findByGrowthValue(Long growthValue) {
        return customerRepository.findByGrowthValue(growthValue);
    }

    public List<CustomerBase> findCustomerLevelIdByCustomerIds(List<String> customerIds) {
        return customerRepository.findCustomerLevelIdByCustomerIds(customerIds);
    }

    public List<String> findCustomerIdByPageable(List<Long> customerLevelIds, PageRequest pageRequest) {
        if (CollectionUtils.isEmpty(customerLevelIds)) {
            return customerRepository.findCustomerIdByPageable(pageRequest);
        }
        return customerRepository.findCustomerIdByCustomerLevelIds(customerLevelIds, pageRequest);
    }


    public List<CustomerBase> findCustomerByCustomerIds(List<String> customerIds) {
        return customerRepository.getBaseCustomerByCustomerIds(customerIds);
    }

    /**
     * 查询账号是否已注册
     * @param phones
     * @return
     */
    public List<String> getCustomersByPhones(List<String> phones){
        return customerRepository.getCustomerByPhones(phones);
    }

    /**
     * 填充省市区
     * @param details
     */
    public void fillArea(List<CustomerDetailForPageVO> details){
        if(CollectionUtils.isNotEmpty(details)){
            List<String> addrIds = new ArrayList<>();
            details.forEach(detail -> {
                addrIds.add(Objects.toString(detail.getProvinceId()));
                addrIds.add(Objects.toString(detail.getCityId()));
                addrIds.add(Objects.toString(detail.getAreaId()));
                addrIds.add(Objects.toString(detail.getStreetId()));
            });
            List<PlatformAddressVO> voList = platformAddressQueryProvider.list(PlatformAddressListRequest.builder().addrIdList(addrIds).build()).getContext().getPlatformAddressVOList();
            if(CollectionUtils.isNotEmpty(voList)){
                Map<String, String> addrMap = voList.stream().collect(Collectors.toMap(PlatformAddressVO::getAddrId, PlatformAddressVO::getAddrName));
                details.forEach(detail -> {
                    detail.setProvinceName(addrMap.get(Objects.toString(detail.getProvinceId())));
                    detail.setCityName(addrMap.get(Objects.toString(detail.getCityId())));
                    detail.setAreaName(addrMap.get(Objects.toString(detail.getAreaId())));
                    detail.setStreetName(addrMap.get(Objects.toString(detail.getStreetId())));
                });
            }
        }
    }

    /**
     * 组装查询参数
     *
     * @param query
     * @param request
     */
    private void wrapperQueryParam(Query query, EnterpriseCustomerPageRequest request) {
        if (StringUtils.isNotBlank(request.getCustomerName())) {
            query.setParameter("customerName", request.getCustomerName());
        }
        if (StringUtils.isNotBlank(request.getCustomerAccount())) {
            query.setParameter("customerAccount", request.getCustomerAccount());
        }
        if (StringUtils.isNotBlank(request.getEmployeeId())) {
            query.setParameter("employeeId", request.getEmployeeId());
        }
        if (StringUtils.isNotBlank(request.getEnterpriseName())) {
            query.setParameter("enterpriseName", request.getEnterpriseName());
        }
        if (request.getProvinceId() != null) {
            query.setParameter("provinceId", request.getProvinceId());
        }
        if (request.getCityId() != null) {
            query.setParameter("cityId", request.getCityId());
        }
        if (request.getAreaId() != null) {
            query.setParameter("areaId", request.getAreaId());
        }
        if (Objects.nonNull(request.getCustomerStatus())) {
            query.setParameter("customerStatus", request.getCustomerStatus().toValue());
        }
        if (request.getCustomerLevelId() != null) {
            query.setParameter("customerLevelId", request.getCustomerLevelId());
        }
        if (request.getBusinessNatureType() != null) {
            query.setParameter("businessNatureType", request.getBusinessNatureType().intValue());
        }
        if (Objects.nonNull(request.getEnterpriseCheckState())) {
            query.setParameter("enterpriseCheckState", request.getEnterpriseCheckState().toValue());
        }
    }

    /**
     * 根据会员ID查询会员成长值
     * @param customerIds
     * @return
     */
    public List<CustomerBase> findGrowthValueByCustomerIds(List<String> customerIds){
        return customerRepository.findGrowthValueByCustomerIds(customerIds);
    }

    /**
     * 包装会员ES分页信息
     * @param request
     * @return
     */
    public List<CustomerDetailFromEsVO> listByCustomerIds(CustomerDetailListByCustomerIdsRequest request){
        List<CustomerDetailFromEsDTO> dtoList = request.getDtoList();

        Long companyInfoId = request.getCompanyInfoId();
        List<CustomerDetailFromEsVO> result = KsBeanUtil.convert(dtoList,CustomerDetailFromEsVO.class);
        List<String> customerIds = dtoList.stream().map(CustomerDetailFromEsDTO::getCustomerId).collect(Collectors.toList());

        Map<String, StoreCustomerRela> customerRelaMap = new HashMap<>();
        if (Objects.nonNull(companyInfoId)) {
            List<StoreCustomerRela> storeCustomerRelaList =  storeCustomerService.findByCompanyInfoIdAndCustomerIdIn(companyInfoId,customerIds);
            customerRelaMap = storeCustomerRelaList.stream().collect(Collectors.toMap(StoreCustomerRela::getCustomerId,Function.identity()));
        }
        Map<String, StoreCustomerRela> storeCustomerRelaMap =  customerRelaMap;

        List<CustomerBase> customerBaseList = this.findGrowthValueByCustomerIds(customerIds);
        Map<String, CustomerBase> growthValuesMap = customerBaseList.stream().collect(Collectors.toMap(CustomerBase::getCustomerId,Function.identity()));

        result.stream().forEach(c -> {
            CustomerBase customerBase = growthValuesMap.get(c.getCustomerId());
            if(Objects.nonNull(customerBase)){
                if(Objects.nonNull(customerBase.getGrowthValue())){
                    c.setGrowthValue(customerBase.getGrowthValue());
                } else {
                    c.setGrowthValue(0L);
                }

                if (Objects.nonNull(companyInfoId)) {
                    //商家和客户不管是从属关系还是关联关系都是一对一的
                    StoreCustomerRela storeCustomerRela = storeCustomerRelaMap.get(c.getCustomerId());
                    if (Objects.nonNull(storeCustomerRela)) {
                        //非自营店铺存储的是店铺等级Id
                        c.setCustomerLevelId(storeCustomerRela.getStoreLevelId());
                        c.setMyCustomer(storeCustomerRela.getCustomerType() == CustomerType.SUPPLIER);
                    } else {
                        //自营店铺存储的是平台等级Id
                        c.setCustomerType(customerBase.getCustomerType());
                    }
                } else {
                    c.setCustomerType(customerBase.getCustomerType());
                }
            }
        });


        List<Long> levelIds = result.stream().filter(v -> Objects.nonNull(v.getCustomerLevelId())).map(v -> v
                .getCustomerLevelId()).collect(toList());
        if (CollectionUtils.isNotEmpty(levelIds)) {
            if (Objects.nonNull(companyInfoId)){
                CompanyInfo companyInfo = companyInfoService.findOne(companyInfoId);
                if (companyInfo.getCompanyType().equals(BoolFlag.YES)) {
                    List<StoreLevel> storeLevels = storeLevelRepository.findAll(StoreLevelQueryRequest.builder()
                            .storeLevelIds(levelIds)
                            .build().getWhereCriteria()
                    );
                    Map<Long, String>  levelNamesMap = storeLevels.stream().collect(Collectors.toMap(StoreLevel::getStoreLevelId,StoreLevel::getLevelName));
                    result.stream().forEach(c ->
                        c.setCustomerLevelName(levelNamesMap.get(c.getCustomerLevelId()))
                    );
                }
            }else{
                List<CustomerLevel> customerLevels = customerLevelService.findByCustomerLevelIds(levelIds);
                Map<Long, String>  levelNamesMap = customerLevels.stream().collect(Collectors.toMap(CustomerLevel::getCustomerLevelId,CustomerLevel::getCustomerLevelName));
                result.stream().forEach(c ->
                    c.setCustomerLevelName(levelNamesMap.get(c.getCustomerLevelId()))
                );
            }

        }

        //遍历得到Employee对象，获取相应属性
        List<String> employeeIds;
        if (Objects.nonNull(companyInfoId)) {//商家列表 -- 商家的平台客户不需要显示业务员，只有所属客户才显示业务员
            employeeIds = result.stream().filter(v -> v.getMyCustomer() && v.getEmployeeId() != null)
                    .map(v -> v.getEmployeeId()).collect(toList());
        } else {//平台 -- 商家客户不显示业务员，平台客户才显示业务员
            employeeIds = result.stream().filter(v -> v.getCustomerType() == CustomerType.PLATFORM
                    && v.getEmployeeId() != null).map(v -> v.getEmployeeId()).collect(toList());
        }
        if (CollectionUtils.isNotEmpty(employeeIds)) {
            List<Employee> employeeList = employeeService.findByEmployeeIds(employeeIds).orElse(null);
            if (CollectionUtils.isNotEmpty(employeeList)){
                Map<String, Employee>  employeeNamesMap = employeeList.stream().collect(Collectors.toMap(Employee::getEmployeeId, Function.identity()));
                result.stream().forEach(c -> {
                    Employee employee =  employeeNamesMap.get(c.getEmployeeId());
                    if (Objects.nonNull(employee)){
                        c.setEmployeeName(StringUtils.isEmpty(employee.getEmployeeName()) ? employee.getEmployeeMobile() : employee.getEmployeeName());
                    }
                });
            }
        }


        return result;

    }

    /**
     * 分页查询会员信息
     * @param request
     * @return
     */
    public List<CustomerDetailInitEsVO> listByPage(CustomerDetailInitEsRequest request){

        List<CustomerDetailInitEs> customerDetailInitEsList = null;

        if (CollectionUtils.isNotEmpty(request.getCustomerIds())) {
            customerDetailInitEsList = customerDetailRepository.page(request.getCustomerIds());
        }else  {
            customerDetailInitEsList = customerDetailRepository.page(request.getPageRequest());
        }

        if (CollectionUtils.isEmpty(customerDetailInitEsList)){
            return null;
        }
        List<String> customerIds = customerDetailInitEsList.stream().map(CustomerDetailInitEs::getCustomerId).collect(Collectors.toList());
        List<StoreCustomerRela> storeCustomerRelaList =  storeCustomerService.findByCustomerIdIn(customerIds);
        List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoService.listByCustomerIds(customerIds);
        Map<String, EnterpriseInfoVO> enterpriseInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(enterpriseInfoList)){
            List<EnterpriseInfoVO> enterpriseInfoVOS = customerDetailMapper.enterpriseInfosToEnterpriseInfoVO(enterpriseInfoList);
            enterpriseInfoMap = enterpriseInfoVOS.stream().collect(Collectors.toMap(EnterpriseInfoVO::getCustomerId,Function.identity()));
        }
        Map<String, EnterpriseInfoVO> enterpriseInfoFinalMap = enterpriseInfoMap;
        Map<String, List<StoreCustomerRela>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeCustomerRelaList)){
            map = storeCustomerRelaList.stream().collect(Collectors.groupingBy(StoreCustomerRela::getCustomerId));
        }
        Map<String, List<StoreCustomerRela>> resultMap = map;
        List<CustomerBase> customerBaseList = this.findCustomerBaseByCustomerIds(customerIds);
        Map<String, CustomerBase> customerBaseMap = customerBaseList.stream().collect(Collectors.toMap(CustomerBase::getCustomerId,Function.identity()));
        List<CustomerDetailInitEsVO> result = customerDetailMapper.customerDetailInitEsToCustomerDetailInitEsVO(customerDetailInitEsList);
        //付费会员信息
        Map<String, List<PaidCardCustomerRelVO>>  paidMap = new HashMap<>();
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOS = paidCardCustomerRelService.listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder().customerIdList(customerIds).delFlag(DeleteFlag.NO).endTimeBegin(LocalDateTime.now()).build());
        if(CollectionUtils.isNotEmpty(paidCardCustomerRelVOS)) {
            paidMap = paidCardCustomerRelVOS.stream().collect(Collectors.groupingBy(PaidCardCustomerRelVO::getCustomerId));
        }
        Map<String, List<PaidCardCustomerRelVO>> finalPaidMap = paidMap;
        result.stream().forEach(c -> {
            String customerId = c.getCustomerId();
            CustomerBase customerBase = customerBaseMap.get(customerId);
            if (Objects.nonNull(customerBase)){
                c.setCustomerAccount(customerBase.getCustomerAccount());
                c.setCustomerLevelId( customerBase.getCustomerLevelId() );
                c.setCheckState(customerBase.getCheckState());
                c.setEnterpriseCheckState(customerBase.getEnterpriseCheckState());
                c.setEnterpriseCheckReason(customerBase.getEnterpriseCheckReason());
                if (MapUtils.isNotEmpty(resultMap)){
                    List<StoreCustomerRela> relaList = resultMap.get(customerId);
                    if (CollectionUtils.isNotEmpty(relaList)){
                        c.setEsStoreCustomerRelaList(customerDetailMapper.storeCustomerRelasToStoreCustomerRelaVO(relaList));
                    }
                }
                if (MapUtils.isNotEmpty(enterpriseInfoFinalMap)){
                    c.setEnterpriseInfo(enterpriseInfoFinalMap.get(customerId));
                }
            }

            List<PaidCardCustomerRelVO> relVOS = finalPaidMap.get(customerId);
            if(CollectionUtils.isNotEmpty(relVOS)) {
                List<PaidCardVO> paidCardVOList = relVOS.stream().map(PaidCardCustomerRelVO::getPaidCardVO).collect(toList());
                List<PaidCardSimpleVO> simpleVOS = paidCardVOList.stream().map(v -> {
                    PaidCardSimpleVO simpleVO = new PaidCardSimpleVO();
                    simpleVO.setPaidCardId(v.getId());
                    simpleVO.setPaidCardName(v.getName());
                    return simpleVO;
                }).collect(toList());
                c.setEsPaidCardList(simpleVOS);
            }
        });
        return result;
    }


    /**
     * ES初始化付费会员信息分页查询会员信息
     * @param request
     * @return
     */
    public List<CustomerDetailInitEsVO> listByPaidMembersPage(CustomerDetailInitEsRequest request){
        List<CustomerDetailInitEs> customerDetailInitEsList = customerDetailRepository.page(request.getCustomerIds());
        if (CollectionUtils.isEmpty(customerDetailInitEsList)){
            return null;
        }
        List<String> customerIds = customerDetailInitEsList.stream().map(CustomerDetailInitEs::getCustomerId).collect(Collectors.toList());
        List<StoreCustomerRela> storeCustomerRelaList =  storeCustomerService.findByCustomerIdIn(customerIds);
        List<EnterpriseInfo> enterpriseInfoList = enterpriseInfoService.listByCustomerIds(customerIds);
        Map<String, EnterpriseInfoVO> enterpriseInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(enterpriseInfoList)){
            List<EnterpriseInfoVO> enterpriseInfoVOS = customerDetailMapper.enterpriseInfosToEnterpriseInfoVO(enterpriseInfoList);
            enterpriseInfoMap = enterpriseInfoVOS.stream().collect(Collectors.toMap(EnterpriseInfoVO::getCustomerId,Function.identity()));
        }
        Map<String, EnterpriseInfoVO> enterpriseInfoFinalMap = enterpriseInfoMap;
        Map<String, List<StoreCustomerRela>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeCustomerRelaList)){
            map = storeCustomerRelaList.stream().collect(Collectors.groupingBy(StoreCustomerRela::getCustomerId));
        }
        Map<String, List<StoreCustomerRela>> resultMap = map;
        List<CustomerBase> customerBaseList = this.findCustomerBaseByCustomerIds(customerIds);
        Map<String, CustomerBase> customerBaseMap = customerBaseList.stream().collect(Collectors.toMap(CustomerBase::getCustomerId,Function.identity()));
        List<CustomerDetailInitEsVO> result = customerDetailMapper.customerDetailInitEsToCustomerDetailInitEsVO(customerDetailInitEsList);
        //付费会员信息
        Map<String, List<PaidCardCustomerRelVO>>  paidMap = new HashMap<>();
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOS = paidCardCustomerRelService.listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder().customerIdList(customerIds).delFlag(DeleteFlag.NO).endTimeBegin(LocalDateTime.now()).build());
        if(CollectionUtils.isNotEmpty(paidCardCustomerRelVOS)) {
            paidMap = paidCardCustomerRelVOS.stream().collect(Collectors.groupingBy(PaidCardCustomerRelVO::getCustomerId));
        }
        Map<String, List<PaidCardCustomerRelVO>> finalPaidMap = paidMap;
        result.stream().forEach(c -> {
            String customerId = c.getCustomerId();
            CustomerBase customerBase = customerBaseMap.get(customerId);
            if (Objects.nonNull(customerBase)){
                c.setCustomerAccount(customerBase.getCustomerAccount());
                c.setCustomerLevelId( customerBase.getCustomerLevelId() );
                c.setCheckState(customerBase.getCheckState());
                c.setEnterpriseCheckState(customerBase.getEnterpriseCheckState());
                c.setEnterpriseCheckReason(customerBase.getEnterpriseCheckReason());
                if (MapUtils.isNotEmpty(resultMap)){
                    List<StoreCustomerRela> relaList = resultMap.get(customerId);
                    if (CollectionUtils.isNotEmpty(relaList)){
                        c.setEsStoreCustomerRelaList(customerDetailMapper.storeCustomerRelasToStoreCustomerRelaVO(relaList));
                    }
                }
                if (MapUtils.isNotEmpty(enterpriseInfoFinalMap)){
                    c.setEnterpriseInfo(enterpriseInfoFinalMap.get(customerId));
                }
            }

            List<PaidCardCustomerRelVO> relVOS = finalPaidMap.get(customerId);
            if(CollectionUtils.isNotEmpty(relVOS)) {
                List<PaidCardVO> paidCardVOList = relVOS.stream().map(PaidCardCustomerRelVO::getPaidCardVO).collect(toList());
                List<PaidCardSimpleVO> simpleVOS = paidCardVOList.stream().map(v -> {
                    PaidCardSimpleVO simpleVO = new PaidCardSimpleVO();
                    simpleVO.setPaidCardId(v.getId());
                    simpleVO.setPaidCardName(v.getName());
                    return simpleVO;
                }).collect(toList());
                c.setEsPaidCardList(simpleVOS);
            }
        });
        return result;
    }



    /**
     * 根据会员ID查询会员账号、审核状态、企业会员状态、驳回原因
     *
     * @param customerIds
     * @return
     */
    public List<CustomerBase> findCustomerBaseByCustomerIds(List<String> customerIds){
        return customerRepository.findCustomerBaseByCustomerIds(customerIds);
    }

    public List<Long> findYzUidNotExist(List<Long> ids) {
        List<Long> existIds = customerRepository.findByYzUids(ids);
        List<Long> notExistIds = new ArrayList<>();
        ids.forEach(id -> {
            if(!existIds.contains(id)) {
                notExistIds.add(id);
            }
        });
        return notExistIds;
    }


    public Customer findCustomerByYzUid(Long yzUids){
        return customerRepository.findByYzUid(yzUids);
    }

    public List<Customer> listHasOpenId(){
        Page<Customer> customerPage = customerRepository.findAll(QueryConditionsUtil.getWhereCriteria(request),
                request.getPageRequest());
    }
}
