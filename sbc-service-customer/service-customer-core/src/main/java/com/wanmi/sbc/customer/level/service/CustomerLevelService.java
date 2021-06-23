package com.wanmi.sbc.customer.level.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelEditRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerLevelNameRequest;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.dto.MarketingCustomerLevelDTO;
import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.vo.MarketingCustomerLevelVO;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.level.repository.CustomerLevelRepository;
import com.wanmi.sbc.customer.level.request.CustomerLevelQueryRequest;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRelRepository;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.store.model.entity.StoreName;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.repository.StoreRepository;
import com.wanmi.sbc.customer.store.service.StoreService;
import com.wanmi.sbc.customer.storecustomer.repository.StoreCustomerRepository;
import com.wanmi.sbc.customer.storecustomer.request.StoreCustomerRequest;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.storelevel.model.entity.StoreLevelQueryRequest;
import com.wanmi.sbc.customer.storelevel.model.root.CommonLevel;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import com.wanmi.sbc.customer.storelevel.repository.StoreLevelRepository;
import com.wanmi.sbc.customer.storelevel.service.StoreLevelService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员等级服务
 * Created by CHENLI on 2017/4/17.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class CustomerLevelService {

    @Autowired
    private CustomerLevelRepository customerLevelRepository;

    @Autowired
    private CustomerAresService customerAresService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreLevelRepository storeLevelRepository;

    @Autowired
    private StoreCustomerRepository storeCustomerRepository;

    @Autowired
    private CustomerLevelRightsRelRepository customerLevelRightsRelRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreLevelService storeLevelService;

    /**
     * 分页
     *
     * @param customerLevelQueryRequest
     * @return
     */
    public Page<CustomerLevel> page(CustomerLevelQueryRequest customerLevelQueryRequest) {
        customerLevelQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        return customerLevelRepository.findAll(customerLevelQueryRequest.getWhereCriteria(),
                customerLevelQueryRequest.getPageRequest());
    }

    /**
     * 查询有多少条客户等级信息
     *
     * @return
     */
    public long countCustomerLevel() {
        CustomerLevelQueryRequest customerLevelQueryRequest = new CustomerLevelQueryRequest();
        customerLevelQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        return customerLevelRepository.count(customerLevelQueryRequest.getWhereCriteria());
    }

    /**
     * 条件查询会员等级列表
     *
     * @return
     */
    public List<CustomerLevel> findAllLevel() {
        return customerLevelRepository.findByDelFlagOrderByCreateTimeAsc();
    }

    /**
     * 条件查询会员等级信息
     *
     * @param customerLevelQueryRequest
     * @return
     */
    public CustomerLevel findOne(CustomerLevelQueryRequest customerLevelQueryRequest) {
        customerLevelQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        return customerLevelRepository.findOne(customerLevelQueryRequest.getWhereCriteria()).orElse(null);
    }

    /**
     * 通过ID查询会员等级
     *
     * @param customerLevelId
     * @return
     */
    public Optional<CustomerLevel> findById(Long customerLevelId) {
        return Optional.ofNullable(customerLevelRepository.findBycustomerLevelId(customerLevelId));
    }

    /**
     * 添加会员等级
     *
     * @param customerLevelEditRequest
     */
    @Transactional
    public void add(CustomerLevelEditRequest customerLevelEditRequest, String employeeId) {
        CustomerLevel customerLevel = new CustomerLevel();
        BeanUtils.copyProperties(customerLevelEditRequest, customerLevel);
        customerLevel.setIsDefalt(DefaultFlag.NO);
        customerLevel.setDelFlag(DeleteFlag.NO);
        customerLevel.setCreatePerson(employeeId);
        customerLevel.setCreateTime(LocalDateTime.now());
//        if (osUtil.isS2b()) {
//            customerLevel.setCustomerLevelDiscount(BigDecimal.ONE);
//        }
        Long levelId = customerLevelRepository.save(customerLevel).getCustomerLevelId();
        // 保存会员等级与权益关联表
        if (customerLevelEditRequest.getRightsIds() != null) {
            List<CustomerLevelRightsRel> customerLevelRightsRels = customerLevelEditRequest.getRightsIds().stream()
                    .map(rightsId -> {
                        CustomerLevelRightsRel customerLevelRightsRel = new CustomerLevelRightsRel();
                        customerLevelRightsRel.setCustomerLevelId(levelId);
                        customerLevelRightsRel.setRightsId(rightsId);
                        return customerLevelRightsRel;
                    }).collect(Collectors.toList());
            customerLevelRightsRelRepository.saveAll(customerLevelRightsRels);
        }
        //ares埋点-会员-后台添加会员等级
        customerAresService.dispatchFunction(AresFunctionType.ADD_CUSTOMER_LEVEL, customerLevel);
    }

    /**
     * 编辑会员等级
     *
     * @param customerLevelEditRequest
     */
    @Transactional
    public Boolean edit(CustomerLevelEditRequest customerLevelEditRequest, String employeeId) {
        CustomerLevel customerLevel = customerLevelRepository.findById(customerLevelEditRequest.getCustomerLevelId()).orElse(null);
        if (Objects.isNull(customerLevel)){
            return Boolean.FALSE;
        }
        KsBeanUtil.copyProperties(customerLevelEditRequest, customerLevel);
        customerLevel.setUpdatePerson(employeeId);
        customerLevel.setUpdateTime(LocalDateTime.now());
        // 保存会员等级与权益关联表
        List<CustomerLevelRightsRel> byCustomerLevelId = customerLevelRightsRelRepository.findByCustomerLevelId(customerLevelEditRequest.getCustomerLevelId());
        customerLevelRightsRelRepository.deleteAll(byCustomerLevelId);
        if (customerLevelEditRequest.getRightsIds() != null) {
            List<CustomerLevelRightsRel> customerLevelRightsRels = customerLevelEditRequest.getRightsIds().stream()
                    .map(rightsId -> {
                        CustomerLevelRightsRel customerLevelRightsRel = new CustomerLevelRightsRel();
                        customerLevelRightsRel.setCustomerLevelId(customerLevelEditRequest.getCustomerLevelId());
                        customerLevelRightsRel.setRightsId(rightsId);
                        return customerLevelRightsRel;
                    }).collect(Collectors.toList());
            customerLevelRightsRelRepository.saveAll(customerLevelRightsRels);
        }
        customerLevelRepository.updateByCustomerLevelId(customerLevel);
        //ares埋点-会员-后台编辑会员等级
        customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_LEVEL, customerLevel);
        return true;
    }

    /**
     * 删除会员等级
     * 删除会员等级时，把该等级下的所有会员转到默认等级下
     *
     * @param customerLevelId
     */
    @Transactional
    public Boolean delete(Long customerLevelId) {
//        CustomerLevel defaultLevel = getDefaultLevel();
//        if (Objects.equals(customerLevelId, defaultLevel.getCustomerLevelId())) {
//            return false;
//        }

        //删除等级
        customerLevelRepository.deleteCustomerLevel(customerLevelId);

        //删除等级权益关联信息
        List<CustomerLevelRightsRel> byCustomerLevelId = customerLevelRightsRelRepository.findByCustomerLevelId(customerLevelId);
        customerLevelRightsRelRepository.deleteAll(byCustomerLevelId);

        //ares埋点-会员-后台删除会员等级
        customerAresService.dispatchFunction(AresFunctionType.DEL_CUSTOMER_LEVEL, customerLevelId);
        return true;
    }

    /**
     * 根据等级编号获取等级信息
     *
     * @param customerLevelId 等级编号
     * @return
     */
    public CustomerLevel findLevelById(Long customerLevelId) {
        if (customerLevelId == null || customerLevelId < 1) {
            return getDefaultLevel();
        }
        CustomerLevel level = customerLevelRepository.findBycustomerLevelId(customerLevelId);
        if (level == null) {
            return this.getDefaultLevel();
        }
        return level;
    }

    /**
     * 根据客户编号获取等级信息(仅B2B模式单店铺下用)
     *
     * @param customerId 客户编号
     * @return
     */
    public CommonLevel findLevelByCustomerId(String customerId) {
        Long storeId = storeRepository.findAll().get(0).getStoreId();
        return findLevelByCustomer(customerId, Collections.singletonList(storeId)).get(storeId);
    }

    /**
     * 获取默认会员等级
     */
    @Transactional
    public CustomerLevel getDefaultLevel() {
        CustomerLevelQueryRequest customerLevelQueryRequest = new CustomerLevelQueryRequest();
        customerLevelQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        customerLevelQueryRequest.setIsDefault(Constants.yes);
        List<CustomerLevel> levels = customerLevelRepository.findAll(customerLevelQueryRequest.getWhereCriteria());
        return levels.get(0);
    }

    /**
     * 批量查询会员等级
     *
     * @param customerLevelIds
     * @return
     */
    public List<CustomerLevel> findByCustomerLevelIds(List<Long> customerLevelIds) {
        return customerLevelRepository.findByCustomerLevelIds(customerLevelIds);
    }

    /**
     * 获取某会员在不同店铺下的会员等级信息
     * 自营店铺——平台等级等级
     * 非自营店铺——店铺会员等级
     *
     * @param customerId 客户ID
     * @param storeIds   店铺ID
     * @return <店铺ID,等级信息（不包含等级名称）>
     */
    public Map<Long, CommonLevel> findLevelByCustomer(String customerId, List<Long> storeIds) {
        Map<Long, CommonLevel> map = new HashMap<>();
        if (CollectionUtils.isEmpty(storeIds)) {
            return map;
        }
        //查询客户在店铺中的等级 区分自营店铺和非自营店铺
        List<Store> storeList = storeRepository.queryListByIds(DeleteFlag.NO, storeIds);
        //非自营店铺id
        List<Long> businessStoreIds = storeList.stream().filter(store -> BoolFlag.YES.equals(store.getCompanyType()))
                .map(Store::getStoreId).collect(Collectors.toList());
        //自营店铺id
        List<Long> platFormStoreIds = storeList.stream().filter(store -> BoolFlag.NO.equals(store.getCompanyType()))
                .map(Store::getStoreId).collect(Collectors.toList());

        //非自营店铺查询对应店铺等级
        if (!CollectionUtils.isEmpty(businessStoreIds)) {
            StoreCustomerRequest customerRequest = new StoreCustomerRequest();
            customerRequest.setStoreIds(businessStoreIds);
            customerRequest.setCustomerId(customerId);
            List<StoreCustomerRela> relax = storeCustomerRepository.findAll(customerRequest.getWhereCriteria());
            List<Long> storeLevelIds = relax.stream().map(StoreCustomerRela::getStoreLevelId).filter(a -> a != null
                    && a > 0)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(storeLevelIds)) {
                //填充
                map.putAll(relax.stream().collect(Collectors.toMap(
                        StoreCustomerRela::getStoreId,
                        re -> {
                            CommonLevel level = new CommonLevel();
                            level.setLevelId(re.getStoreLevelId());
                            level.setLevelDiscount(BigDecimal.ONE);
                            level.setLevelType(BoolFlag.YES);
                            return level;
                        }
                )));

                //填充店铺等级
                storeLevelRepository.findAll(StoreLevelQueryRequest.builder()
                        .storeLevelIds(storeLevelIds)
                        .build().getWhereCriteria()
                ).stream().filter(rel -> map.containsKey(rel.getStoreId()) && rel.getStoreLevelId().equals(map.get(rel
                        .getStoreId()).getLevelId())).forEach(rel -> {
                    map.get(rel.getStoreId()).setLevelDiscount(rel.getDiscountRate());
                    map.get(rel.getStoreId()).setLevelName(rel.getLevelName());
                });
            }
        }

        //自营店铺查询用户平台等级
        if (!CollectionUtils.isEmpty(platFormStoreIds)) {
            CustomerLevel customerLevel = customerLevelRepository.findByCustomerId(customerId);

            //填充
            if(customerLevel != null){
                platFormStoreIds.forEach(storeId -> {
                    CommonLevel level = new CommonLevel();
                    level.setLevelId(customerLevel.getCustomerLevelId());
                    level.setLevelDiscount(customerLevel.getCustomerLevelDiscount());
                    level.setLevelType(BoolFlag.NO);
                    level.setLevelName(customerLevel.getCustomerLevelName());

                    map.put(storeId, level);
                });
            }
        }


        return map;
    }

    /**
     * 获取等级信息（不包含等级名称）
     *
     * @param customerId 客户ID
     * @param storeId    店铺ID
     * @return 店铺类
     */
    public CommonLevel findLevelByCustomer(String customerId, Long storeId) {
        return findLevelByCustomer(customerId, Collections.singletonList(storeId)).get(storeId);
    }

    /**
     * 根据会员ID查询会员等级ID
     * @param customerIds
     * @return
     */
    public List<CustomerBase> findCustomerLevelIdByCustomerIds(List<String> customerIds){
        List<CustomerBase> customerBaseList =  customerService.findCustomerLevelIdByCustomerIds(customerIds);
        List<Long> list = customerBaseList.stream().map(CustomerBase::getCustomerLevelId).distinct().collect(Collectors.toList());
        List<CustomerLevel> customerLevelList = findByCustomerLevelIds(list);
        Map<Long,String> map = customerLevelList.stream().collect(Collectors.toMap(CustomerLevel::getCustomerLevelId,CustomerLevel::getCustomerLevelName));
        customerBaseList.stream().forEach(customerBase -> customerBase.setCustomerLevelName(map.get(customerBase.getCustomerLevelId())));
        return customerBaseList;
    }


    /**
     * 查询目标客户
     * @param request
     */
    public List<MarketingCustomerLevelVO> listCustomerLevelName(CustomerLevelListByCustomerLevelNameRequest request){
        List<MarketingCustomerLevelDTO> marketingList = request.getCustomerLevelDTOList();
        //店铺信息
        List<Long> storeIdList = marketingList.stream().map(m -> m.getStoreId()).collect(Collectors.toList());
        List<StoreName> storeNameList = storeService.listCompanyTypeByStoreIds(storeIdList);
        Map<Long, StoreName> storeVOMap = storeNameList.stream().collect(Collectors.toMap(StoreName::getStoreId, v -> v));
        //平台客户等级
        List<CustomerLevel> customerLevelVOList = this.findAllLevel();
        List<StoreLevel> storeLevelVOList =  storeLevelService.findByStoreIdOrderByCreateTimeAsc(storeIdList);
        Map<Long, List<StoreLevel>> storeLevelMap = storeLevelVOList.stream().collect(Collectors.groupingBy(StoreLevel::getStoreId));
        return marketingList.stream().map(marketingPageVO -> {
            //填充客户等级名称
            StoreName storeName = storeVOMap.get(marketingPageVO.getStoreId());
            BoolFlag companyType = storeName.getCompanyType();
            List<String> levels = Arrays.asList(marketingPageVO.getJoinLevel().split(","));
            String levelName = "";
            if(BoolFlag.NO.equals(companyType)){
                //平台
                if(CollectionUtils.isNotEmpty(customerLevelVOList) && CollectionUtils.isNotEmpty(levels)){
                    levelName = levels.stream().flatMap(level -> customerLevelVOList.stream()
                            .filter(customerLevelVO -> level.equals(customerLevelVO.getCustomerLevelId().toString()))
                            .map(v -> v.getCustomerLevelName())).collect(Collectors.joining(","));
                }
            }else{
                //商家
                if(CollectionUtils.isNotEmpty(levels)) {
                    List<StoreLevel> storeLevels =  storeLevelMap.get(marketingPageVO.getStoreId());
                    if (CollectionUtils.isNotEmpty(storeLevels)) {
                        levelName = levels.stream().flatMap(level -> storeLevels.stream()
                                .filter(storeLevelVO -> level.equals(storeLevelVO.getStoreLevelId().toString()))
                                .map(v -> v.getLevelName())).collect(Collectors.joining(","));
                    }
                }
            }
            MarketingCustomerLevelVO vo = new MarketingCustomerLevelVO();
            vo.setId(marketingPageVO.getId());
            vo.setMarketingId(marketingPageVO.getMarketingId());
            vo.setStoreId(marketingPageVO.getStoreId());
            vo.setJoinLevel(marketingPageVO.getJoinLevel());
            vo.setLevelName(levelName);
            vo.setStoreName(storeName.getStoreName());
            return vo;
        }).collect(Collectors.toList());
    }

}
