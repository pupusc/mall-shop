package com.wanmi.sbc.customer.storecustomer.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.constant.CompanyInfoErrorCode;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerVO;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.repository.CompanyInfoRepository;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.repository.CustomerDetailRepository;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import com.wanmi.sbc.customer.storecustomer.repository.StoreCustomerRepository;
import com.wanmi.sbc.customer.storecustomer.request.StoreCustomerRequest;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 店铺-会员关联Service
 * Created by bail on 2017/11/15.
 */
@Service
public class StoreCustomerService {
    @Autowired
    private CustomerAresService customerAresService;

    @Autowired
    private StoreCustomerRepository storeCustomerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private CustomerDetailRepository customerDetailRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CustomerService customerService;

    /**
     * 根据店铺标识查询店铺的会员列表
     *
     * @return
     */
//    public List<StoreCustomerResponse> findCustomerList(Long storeId) {
//        if (Objects.isNull(storeId)) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        }
//        List<Object> resultList = storeCustomerRepository.findInfoByStoreId(storeId);
//        return resultList.stream().map(StoreCustomerResponse::convertFromNativeSQLResult).collect(Collectors.toList());
//    }
    public List<StoreCustomerVO> findCustomerList(Long storeId) {
        if (Objects.isNull(storeId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Store store = storeService.findOne(storeId);
        List<Object> resultList;
        // 自营店铺
        if (store.getCompanyType().equals(BoolFlag.NO)) {
            resultList = storeCustomerRepository.findBossAll();
        } else { // 非自营店铺
            resultList = storeCustomerRepository.findInfoByStoreId(storeId);
        }
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    /**
     * 根据店铺标识查询店铺的会员列表，不区分会员禁用状态
     *
     * @return List<StoreCustomerResponse>
     */
//    public List<StoreCustomerResponse> findAllCustomerList(Long storeId) {
//        if (Objects.isNull(storeId)) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        }
//        List<Object> resultList = storeCustomerRepository.findAllByStoreId(storeId);
//        return resultList.stream().map(StoreCustomerResponse::convertFromNativeSQLResult).collect(Collectors.toList());
//    }
    public List<StoreCustomerVO> findAllCustomerList(Long storeId) {
        if (Objects.isNull(storeId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<Object> resultList = storeCustomerRepository.findAllByStoreId(storeId);
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    public List<StoreCustomerVO> findCustomerListByIds(Long storeId, List<String> customerIds) {
        if (Objects.isNull(storeId) || customerIds == null || customerIds.isEmpty()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<Object> resultList = storeCustomerRepository.findAllByStoreIdAndCustomerIds(storeId,customerIds);
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    public List<StoreCustomerVO> findBossAllCustomerList() {
        List<Object> resultList = storeCustomerRepository.findBossAll();
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    public List<StoreCustomerVO> findBossAllCustomerListByName(String customerName) {
        List<Object> resultList = storeCustomerRepository.findBossByCustomerName(customerName);
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    /**
     * 根据店铺标识,账户账号匹配查询店铺的会员列表前几条
     *
     * @return
     */
//    public List<StoreCustomerResponse> findCustomerList(Long storeId, String customerAccount, Integer pageSize,
//                                                        String employeeId) {
//        if (Objects.isNull(storeId) || Objects.isNull(customerAccount) || Objects.isNull(pageSize)) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        }
//        List<Object> resultList = storeCustomerRepository.findInfoByStoreId(storeId, customerAccount, pageSize,
//                employeeId);
//        return resultList.stream().map(StoreCustomerResponse::convertFromNativeSQLResult).collect(Collectors.toList());
//    }
    public List<StoreCustomerVO> findCustomerList(Long storeId, String customerAccount, Integer pageSize,
                                                  List<String> employeeIds) {
        if (Objects.isNull(storeId) || Objects.isNull(customerAccount) || Objects.isNull(pageSize)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<Object> resultList = CollectionUtils.isNotEmpty(employeeIds) ? storeCustomerRepository.findInfoByStoreId(storeId, customerAccount, pageSize,
                employeeIds) : storeCustomerRepository.findInfoByStoreId(storeId, customerAccount, pageSize);
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    public List<StoreCustomerVO> findBossCustomerList(String customerAccount, Integer pageSize,
                                                      List<String> employeeIds) {
        if (Objects.isNull(customerAccount) || Objects.isNull(pageSize)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<Object> resultList = CollectionUtils.isNotEmpty(employeeIds) ? storeCustomerRepository.findBoss(customerAccount, pageSize, employeeIds) : storeCustomerRepository.findBoss(customerAccount, pageSize);
        return resultList.stream().map(StoreCustomerVO::convertFromNativeSQLResult).collect(Collectors.toList());
    }

    /**
     * 修改平台客户，只能修改等级
     *
     * @param customerId
     * @param storeCustomerRela
     * @param employeeId
     * @return
     */
    @Transactional
    public StoreCustomerRela updateByCustomerId(String customerId, StoreCustomerRela storeCustomerRela, String
            employeeId) {
        Customer customer = customerRepository.findById(storeCustomerRela.getCustomerId()).orElse(null);
        if (Objects.nonNull(customer) && customer.getCheckState() == CheckState.CHECKED && customer.getDelFlag() ==
                DeleteFlag.NO) {
            StoreCustomerRela related = this.findCustomerRelatedForAll(customerId, storeCustomerRela.getCompanyInfoId
                    ());
            if (Objects.nonNull(related)) {
                related.setStoreLevelId(storeCustomerRela.getStoreLevelId());

                if (employeeId != null && !"".equals(employeeId)) {
                    //修改业务员信息
                    CustomerDetail customerDetail = customer.getCustomerDetail();
                    customerDetail.setEmployeeId(employeeId);
                    customerDetailRepository.save(customerDetail);
                    //等级和人员关系表也插入employeeId
                    related.setEmployeeId(employeeId);
                }

                //ares埋点-会员-修改会员在某店铺的所属等级
                customerAresService.dispatchFunction(AresFunctionType.EDIT_STORE_CUSTOMER_RELA, related);
                return storeCustomerRepository.save(related);
            } else {
                throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_NOT_EXIST_IN_COMPANY);
            }
        } else {
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_STATUS_ERROR);
        }
    }

    /**
     * 添加平台客户
     *
     * @param storeCustomerRela
     * @return
     */
    @Transactional
    public StoreCustomerRela addPlatformRelated(StoreCustomerRela storeCustomerRela) {
        Customer customer = customerRepository.findById(storeCustomerRela.getCustomerId()).orElse(null);
        if (Objects.nonNull(customer) && customer.getCheckState() == CheckState.CHECKED && customer.getDelFlag() ==
                DeleteFlag.NO) {
            StoreCustomerRela related = this.findCustomerRelatedForAll(storeCustomerRela.getCustomerId(),
                    storeCustomerRela.getCompanyInfoId());
            if (Objects.isNull(related)) {
                StoreCustomerRela savedRela = storeCustomerRepository.save(storeCustomerRela);

                //ares埋点-会员-添加某店铺的会员
                customerAresService.dispatchFunction(AresFunctionType.ADD_STORE_CUSTOMER_RELA, storeCustomerRela);
                return savedRela;
            } else {
                throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_EXIST_IN_COMPANY);
            }
        } else {
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_STATUS_ERROR);
        }
    }


    @Transactional
    public void deletePlatformRelated(StoreCustomerRela storeCustomerRela) {
        StoreCustomerRela related = this.findCustomerRelatedForPlatform(storeCustomerRela.getCustomerId(),
                storeCustomerRela.getCompanyInfoId());
        storeCustomerRepository.delete(related);
        //ares埋点-会员-删除某店铺的某个会员
//        customerAresService.dispatchFunction("delStoreCustomerRela", related.getId());
        customerAresService.dispatchFunction(AresFunctionType.DEL_STORE_CUSTOMER_RELA, related.getId());
    }


    /**
     * 通过客户Id获取商家记录
     *
     * @param customerId
     * @return
     */
    public CompanyInfo getCompanyInfoByCustomerId(String customerId) {
        StoreCustomerRela storeCustomerRela = this.findCustomer(customerId);
        if (Objects.nonNull(storeCustomerRela)) {
            CompanyInfo companyInfo = companyInfoRepository.findByCompanyInfoIdAndDelFlag(storeCustomerRela
                    .getCompanyInfoId(), DeleteFlag.NO);
            if (Objects.nonNull(companyInfo)) {
                return companyInfo;
            } else {
                throw new SbcRuntimeException(CompanyInfoErrorCode.NOT_EXIST);
            }
        } else {
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_NOT_EXIST_IN_COMPANY);
        }
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @param customerId
     * @return
     */
    public CompanyInfo getCompanyInfoBelongByCustomerId(String customerId) {
        StoreCustomerRela storeCustomerRela = this.findCustomerBelong(customerId);
        if (Objects.nonNull(storeCustomerRela)) {
            CompanyInfo companyInfo = companyInfoRepository.findByCompanyInfoIdAndDelFlag(storeCustomerRela
                    .getCompanyInfoId(), DeleteFlag.NO);
            if (Objects.nonNull(companyInfo)) {
                return companyInfo;
            } else {
                throw new SbcRuntimeException(CompanyInfoErrorCode.NOT_EXIST);
            }
        } else {
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_NOT_EXIST_IN_COMPANY);
        }
    }

    /**
     * 获取客户-商家的关联记录(平台客户)
     *
     * @param customerId
     * @param companyInfoId
     * @return
     */
    public StoreCustomerRela findCustomerRelatedForPlatform(String customerId, Long companyInfoId) {
        return this.findCustomerRelated(customerId, companyInfoId, true);
    }

    /**
     * 获取客户-商家的关联记录(全部客户)
     *
     * @param customerId
     * @param companyInfoId
     * @return
     */
    public StoreCustomerRela findCustomerRelatedForAll(String customerId, Long companyInfoId) {
        return this.findCustomerRelated(customerId, companyInfoId, false);
    }

    /**
     * 私有方法 -- 获取客户-商家的关联记录
     * 因拆分接口，改为公有方法
     *
     * @param customerId
     * @param companyInfoId
     * @return
     */
    public StoreCustomerRela findCustomerRelated(String customerId, Long companyInfoId, boolean queryPlatform) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCustomerId(customerId);
        storeCustomerRequest.setCompanyInfoId(companyInfoId);
        if (queryPlatform) {
            storeCustomerRequest.setCustomerType(CustomerType.PLATFORM);
        }
        return storeCustomerRepository.findOne(storeCustomerRequest.getWhereCriteria()).orElse(null);
    }


    /**
     * 获取和某个商家有关联关系的记录
     *
     * @param companyInfoId
     * @return
     */
    public List<StoreCustomerRela> findRelatedCustomerByCompanyId(Long companyInfoId) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCompanyInfoId(companyInfoId);
        return storeCustomerRepository.findAll(storeCustomerRequest.getWhereCriteria());
    }

    /**
     * 获取用户在商户店铺下的等级信息
     *
     * @param request
     * @return
     */
    public List<StoreCustomerRela> list(StoreCustomerRequest request) {
        return storeCustomerRepository.findAll(request.getWhereCriteria());
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @param customerId
     * @return
     */
    public StoreCustomerRela findCustomerBelong(String customerId) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCustomerId(customerId);
        storeCustomerRequest.setCustomerType(CustomerType.SUPPLIER);
        return storeCustomerRepository.findOne(storeCustomerRequest.getWhereCriteria()).orElse(null);
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     *
     * @param customerId
     * @return
     */
    public StoreCustomerRela findCustomerBelongTwo(String customerId, Long companyInfoId) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCustomerId(customerId);
        storeCustomerRequest.setCompanyInfoId(companyInfoId);
        return storeCustomerRepository.findOne(storeCustomerRequest.getWhereCriteria()).orElse(null);
    }

    /**
     * 获取客户-商家客户，包括平台关联和自己发展的客户
     *
     * @param customerId
     * @return
     */
    public StoreCustomerRela findCustomer(String customerId) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCustomerId(customerId);
        return storeCustomerRepository.findOne(storeCustomerRequest.getWhereCriteria()).orElse(null);
    }

    /**
     * 更新店铺会员关系表
     *
     * @param rela
     */
    public void updateStoreCustomerRela(StoreCustomerRela rela) {
        storeCustomerRepository.save(rela);
    }

    public List<String> findCustomerIdByStoreId(Long storeId, List<Long> storeLevelIds, PageRequest pageRequest) {
        if (CollectionUtils.isEmpty(storeLevelIds)) {
            return storeCustomerRepository.findCustomerIdByStoreId(storeId, pageRequest);
        }
        return storeCustomerRepository.findCustomerIdByStoreIdAndStoreLevelIdsIn(storeId, storeLevelIds, pageRequest);
    }

    /**
     * 根据会员ID查询店铺-会员关系
     *
     * @param customerIds
     * @return
     */
    public List<StoreCustomerRela> findByCompanyInfoIdAndCustomerIdIn(Long companyInfoId, List<String> customerIds) {
        return storeCustomerRepository.findByCompanyInfoIdAndCustomerIdIn(companyInfoId, customerIds);
    }

    /**
     * 根据会员ID查询店铺-会员关系
     *
     * @param customerIds
     * @return
     */
    public List<StoreCustomerRela> findByCustomerIdIn(List<String> customerIds) {
        return storeCustomerRepository.findByCustomerIdIn(customerIds);
    }
}
