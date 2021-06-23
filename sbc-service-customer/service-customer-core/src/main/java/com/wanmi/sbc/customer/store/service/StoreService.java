package com.wanmi.sbc.customer.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.Pinyin4jUtil;
import com.wanmi.sbc.customer.api.constant.CompanyInfoErrorCode;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.constant.StoreErrorCode;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.repository.CompanyInfoRepository;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.employee.repository.EmployeeRepository;
import com.wanmi.sbc.customer.store.model.entity.StoreName;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.repository.StoreRepository;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 店铺信息服务
 * Created by CHENLI on 2017/11/2.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerAresService customerAresService;

    /**
     * 分页查询店铺
     *
     * @param queryRequest
     * @return
     */
    public Page<Store> page(StoreQueryRequest queryRequest) {
        return storeRepository.findAll(StoreWhereCriteriaBuilder.build(queryRequest), queryRequest
                .getPageRequest());
    }

    /**
     * 根据店铺标识查询店铺信息公共方法
     *
     * @param storeId
     * @return
     * @author bail
     */
    public Store queryStoreCommon(Long storeId) {
        //1.参数不正确
        if (Objects.isNull(storeId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Store store = storeRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        //2.店铺不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        //3.店铺已关店
//        if (StoreState.CLOSED.equals(store.getStoreState())) {
//            throw new SbcRuntimeException(StoreErrorCode.CLOSE);
//        }
        //4.店铺已过期(当前时间超过了截止日期)
        if (LocalDateTime.now().isAfter(store.getContractEndDate())) {
            throw new SbcRuntimeException(StoreErrorCode.OVER_DUE);
        }
        return store;
    }

    /**
     * 查询店铺基本信息(会员查看店铺首页时使用)
     *
     * @param storeId
     * @return
     * @author bail
     */
    public Store queryStoreBaseInfo(Long storeId) {
        return queryStoreCommon(storeId);
    }

    /**
     * 查询店铺档案信息(会员查看店铺档案时使用)
     *
     * @param storeId
     * @return
     * @author bail
     *//*
    public StoreDocumentResponse queryStoreDocument(Long storeId) {
        return new StoreDocumentResponse().convertFromEntity(queryStoreCommon(storeId));
    }*/

    /**
     * 查询店铺基本信息(商家后台查看自己的信息)
     *
     * @param storeId
     * @return
     * @author bail
     *//*
    public Store queryBossStoreBaseInfo(Long storeId) {
        Store store = storeRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        //2.店铺不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        return new StoreBaseInfoResponse().convertFromEntity(store);
    }*/

    /**
     * 查询店铺信息-商家信息-主账号信息
     *
     * @param storeId
     * @return
     */
    public StoreInfoResponse queryStoreInfo(Long storeId) {
        StoreInfoResponse storeInfoResponse = new StoreInfoResponse();
        Store store = storeRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        //店铺信息不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        //商家不存在
        CompanyInfo companyInfo = store.getCompanyInfo();
        if (Objects.isNull(companyInfo)) {
            throw new SbcRuntimeException(CompanyInfoErrorCode.NOT_EXIST);
        }
        //查询商家下的主账号
        Employee employee = employeeRepository.findMainEmployee(companyInfo.getCompanyInfoId(), DeleteFlag.NO);
        if (Objects.isNull(employee)) {
            throw new SbcRuntimeException(EmployeeErrorCode.NOT_EXIST);
        }
        KsBeanUtil.copyProperties(store, storeInfoResponse);
        storeInfoResponse.setAccountState(employee.getAccountState());
        storeInfoResponse.setAccountDisableReason(employee.getAccountDisableReason());
        storeInfoResponse.setAccountName(employee.getAccountName());
        storeInfoResponse.setSupplierName(companyInfo.getSupplierName());
        storeInfoResponse.setSupplierCode(companyInfo.getCompanyCode());
        storeInfoResponse.setCompanyInfoId(companyInfo.getCompanyInfoId());
        storeInfoResponse.setCompanyType(companyInfo.getCompanyType());

        return storeInfoResponse;
    }

    /**
     * 通过商家id查询店铺信息
     *
     * @param companyInfoId
     * @return
     */
    public Store queryStoreByCompanyInfoId(Long companyInfoId) {
        return storeRepository.findStoreByCompanyInfoId(companyInfoId, DeleteFlag.NO);
    }

    /**
     * 保存店铺信息
     * 保存店铺信息 并且修改商家信息
     *
     * @param saveRequest
     * @return
     */
    @Transactional
    public Store saveStore(StoreSaveRequest saveRequest) {
        //商家名称重复
        if (companyInfoRepository.findBySupplierNameAndDelFlag(saveRequest.getSupplierName(), DeleteFlag.NO)
                .isPresent()) {
            throw new SbcRuntimeException(CompanyInfoErrorCode.NAME_ALREADY_EXISTS);
        }
        //店铺名称重复
        if (storeRepository.findByStoreNameAndDelFlag(saveRequest.getStoreName(), DeleteFlag.NO).isPresent()) {
            throw new SbcRuntimeException(StoreErrorCode.NAME_ALREADY_EXISTS);
        }
        //商家不存在
        CompanyInfo companyInfo = companyInfoRepository.findByCompanyInfoIdAndDelFlag(saveRequest.getCompanyInfoId(),
                DeleteFlag.NO);
        if (Objects.isNull(companyInfo)) {
            throw new SbcRuntimeException(CompanyInfoErrorCode.NOT_EXIST);
        }

        Store store = new Store();
        KsBeanUtil.copyProperties(saveRequest, store);
        store.setDelFlag(DeleteFlag.NO);

        KsBeanUtil.copyProperties(saveRequest, companyInfo);

        companyInfo.setContactName(saveRequest.getContactPerson());
        companyInfo.setContactPhone(saveRequest.getContactMobile());
        companyInfo.setDetailAddress(saveRequest.getAddressDetail());
        companyInfoRepository.save(companyInfo);
        store.setCompanyInfo(companyInfo);
        store.setStorePinyinName(Pinyin4jUtil.converterToSpell(store.getStoreName(), ","));
        store.setSupplierPinyinName(Pinyin4jUtil.converterToSpell(store.getSupplierName(), ","));
        if(StringUtils.isNotBlank(store.getStorePinyinName())){
            store.setStorePinyinName(Arrays.stream(store.getStorePinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        if(StringUtils.isNotBlank(store.getSupplierPinyinName())){
            store.setSupplierPinyinName(Arrays.stream(store.getSupplierPinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        return storeRepository.saveAndFlush(store);
    }

    /**
     * 修改店铺logo与店招
     *
     * @param saveRequest
     * @return
     */
    @Transactional
    public void updateStoreBaseInfo(StoreModifyLogoRequest saveRequest) {
        if (saveRequest == null || saveRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Store store = storeRepository.findByStoreIdAndDelFlag(saveRequest.getStoreId(), DeleteFlag.NO);
        if (store == null) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        store.setStoreLogo(saveRequest.getStoreLogo());
        store.setStoreSign(saveRequest.getStoreSign());
        store.setStorePinyinName(Pinyin4jUtil.converterToSpell(store.getStoreName(), ","));
        store.setSupplierPinyinName(Pinyin4jUtil.converterToSpell(store.getSupplierName(), ","));
        if(StringUtils.isNotBlank(store.getStorePinyinName())){
            store.setStorePinyinName(Arrays.stream(store.getStorePinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        if(StringUtils.isNotBlank(store.getSupplierPinyinName())){
            store.setSupplierPinyinName(Arrays.stream(store.getSupplierPinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        storeRepository.save(store);
    }

    /**
     * 修改店铺结算日期
     *
     * @param request
     * @return
     */
    @Transactional
    public Store update(AccountDateModifyRequest request) {
        Store store = storeRepository.findByStoreIdAndDelFlag(request.getStoreId(), DeleteFlag.NO);
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        request.setAccountDay(StringUtils.join(request.getDays().toArray(), ','));
        KsBeanUtil.copyProperties(request, store);
        return storeRepository.save(store);
    }


    /**
     * 查询店铺信息
     *
     * @param storeId
     * @return
     */
    public Store findOne(Long storeId) {
        Store store = storeRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        return store;
    }

    /**
     * 查询店铺信息,不考虑删除状态
     *
     * @param storeId
     * @return
     */
    public Store find(Long storeId) {
        Store store =
                storeRepository.findById(storeId).orElseThrow(() -> new SbcRuntimeException(StoreErrorCode.NOT_EXIST));
        return store;
    }


    /**
     * 店铺开店/关店
     *
     * @param request
     * @return
     */
    @Transactional
    public Store closeOrOpen(StoreSwitchRequest request) {
        Store store = storeRepository.findByStoreIdAndDelFlag(request.getStoreId(), DeleteFlag.NO);
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        } else if (store.getAuditState() != CheckState.CHECKED) {
            throw new SbcRuntimeException(StoreErrorCode.REJECTED);
        }
        store.setStoreState(request.getStoreState());
        store.setStoreClosedReason(request.getStoreClosedReason());
        Store savedStore = storeRepository.save(store);

        //ares埋点-会员-编辑店铺信息
        customerAresService.dispatchFunction(AresFunctionType.EDIT_STORE, savedStore);
        return savedStore;
    }


    /**
     * 驳回/通过 审核
     *
     * @param request
     * @return
     */
    @Transactional
    @GlobalTransactional
    public Store rejectOrPass(StoreAuditRequest request) {
        Store store = storeRepository.findByStoreIdAndDelFlag(request.getStoreId(), DeleteFlag.NO);
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        } else if (!Objects.equals(store.getAuditState(), CheckState.WAIT_CHECK)
                && !Objects.equals(store.getAuditState(), CheckState.NOT_PASS)) {
            throw new SbcRuntimeException(StoreErrorCode.COMPLETED);
        }
        //操作审核成功状态
        if (Objects.equals(request.getAuditState(), CheckState.CHECKED)) {
            store.setStoreState(StoreState.OPENING);
            store.setCompanyType(Objects.equals(request.getCompanyType().toValue(), 0) ? BoolFlag.NO : BoolFlag.YES);
            store.setContractStartDate(DateUtil.parse(request.getContractStartDate(), DateUtil.FMT_TIME_1));
            store.setContractEndDate(DateUtil.parse(request.getContractEndDate(), DateUtil.FMT_TIME_1));
            store.setApplyEnterTime(LocalDateTime.now());
            store.setFreightTemplateType(DefaultFlag.NO);
            request.setAccountDay(StringUtils.join(request.getDays().toArray(), ','));
        }
        KsBeanUtil.copyProperties(request, store);
        return storeRepository.save(store);
    }


    public List<Store> findList(List<Long> ids) {
        return storeRepository.queryListByIds(DeleteFlag.NO, ids);
    }

    public List<Store> findAllList(List<Long> ids) {
        return storeRepository.findAllById(ids);
    }

    /**
     * B2B初始化,一般只有B2B调用
     *
     * @param companyInfoId
     * @return
     */
    public Store getStore(Long companyInfoId) {
        Store store = this.queryStoreByCompanyInfoId(companyInfoId);
        if (store == null) {
            CompanyInfo companyInfo = companyInfoRepository.findById(companyInfoId).orElse(null);
            store = new Store();
            store.setContractStartDate(LocalDateTime.now());
            store.setContractEndDate(store.getContractStartDate().minusYears(100));
            store.setAuditState(CheckState.CHECKED);
            store.setStoreState(StoreState.OPENING);
            store.setCompanyInfo(companyInfo);
            store.setDelFlag(DeleteFlag.NO);
            store.setStoreId(this.storeRepository.save(store).getStoreId());
        }
        return store;
    }

    /**
     * 批量校验店铺是否全部有效（审核状态|开关店|删除状态|签约失效时间）
     *
     * @param ids 店铺id集合
     * @return true|false:有效|无效,只要有一个失效，则返回false
     */
    public boolean checkStore(List<Long> ids) {
        List<Store> stores = findList(ids);
        return !CollectionUtils.isEmpty(stores)
                && stores.stream().noneMatch(
                s -> s.getDelFlag() == DeleteFlag.YES
                        || s.getAuditState() != CheckState.CHECKED
                        || s.getStoreState() == StoreState.CLOSED
                        || s.getContractEndDate().isBefore(LocalDateTime.now())
        );
    }

    /**
     * 修改签约日期和商家类型
     *
     * @param saveRequest
     * @return
     */
    @Transactional
    public Store updateStoreContract(StoreContractModifyRequest saveRequest) {
        Store store = storeRepository.findByStoreIdAndDelFlag(saveRequest.getStoreId(), DeleteFlag.NO);
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        KsBeanUtil.copyProperties(saveRequest, store);
        store.getCompanyInfo().setCompanyType(saveRequest.getCompanyType());
        Store newStore = storeRepository.save(store);

        //ares埋点-会员-编辑店铺信息
        customerAresService.dispatchFunction(AresFunctionType.EDIT_STORE, newStore);
        return newStore;
    }

    /**
     * 查询账期内的有效店铺，进行结算明细的定时任务
     * FIND_IN_SET这个函数hibernate并不支持
     *
     * @param
     * @return
     */
    public List<Store> getStoreListForSettle(ListStoreForSettleRequest request) {
//        String sql = "SELECT store_id storeId, account_day accountDay, store_name storeName FROM store " +
//                "WHERE FIND_IN_SET( ?1, account_day) AND audit_state = 1 AND store_state = 0 AND del_flag = 0";
        String sql = "SELECT store_id storeId, account_day accountDay, store_name storeName FROM store " +
                "WHERE FIND_IN_SET( ?1, account_day) AND audit_state = 1 AND del_flag = 0 AND store_type = (?2)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, request.getTargetDay());
        query.setParameter(2,request.getStoreType().toValue());
        query.unwrap(SQLQuery.class)
//                .addEntity(Store.class)
                .addScalar("storeId", StandardBasicTypes.LONG)
                .addScalar("accountDay", StandardBasicTypes.STRING)
                .addScalar("storeName", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(Store.class));
        List<Store> storeList = query.getResultList();
        return storeList;
    }

    /**
     * 模糊查询storeName,自动关联5条信息
     *
     * @param storeName
     * @return
     */
    public List<Store> queryStoreByNameForAutoComplete(String storeName) {
        StoreQueryRequest queryRequest = new StoreQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.setStoreName(storeName);
        queryRequest.setPageSize(5);
        return storeRepository.findAll(StoreWhereCriteriaBuilder.build(queryRequest), queryRequest
                .getPageRequest()).getContent();
    }

    /**
     * 模糊查询storeName,自动关联5条信息
     *
     * @param storeName
     * @return
     */
    public List<Store> queryStoreByNameAndStoreTypeForAutoComplete(String storeName , StoreType storeType) {
        StoreQueryRequest queryRequest = new StoreQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.setStoreName(storeName);
        queryRequest.setStoreType(storeType);
        queryRequest.setPageSize(5);
        return storeRepository.findAll(StoreWhereCriteriaBuilder.build(queryRequest), queryRequest
                .getPageRequest()).getContent();
    }

    /**
     * 模糊查询storeName
     *
     * @param storeName
     * @return
     */
    public List<Store> queryStoreByName(String storeName) {
        StoreQueryRequest queryRequest = new StoreQueryRequest();
        queryRequest.setStoreName(storeName);
        return storeRepository.findAll(StoreWhereCriteriaBuilder.build(queryRequest));
    }

    /**
     * 查询店铺主页信息(店铺主页用)
     *
     * @param storeId
     * @return
     *//*
    public StoreHomeInfoResponse queryStoreHomeInfo(Long storeId) {
        return new StoreHomeInfoResponse().convertFromEntity(findOne(storeId));
    }*/


    /**
     * 不分页查询所有店铺
     *
     * @param queryRequest
     * @return
     */
    public List<Store> list(StoreQueryRequest queryRequest) {
        return storeRepository.findAll(StoreWhereCriteriaBuilder.build(queryRequest));
    }

    public StoreVO wraper2VoFromStore(Store store) {
        StoreVO storeVO = new StoreVO();
        KsBeanUtil.copyPropertiesThird(store, storeVO);
        if (store.getCompanyInfo() != null) {
            CompanyInfoVO companyInfoVO = new CompanyInfoVO();
            storeVO.setCompanyInfo(companyInfoVO);
            KsBeanUtil.copyPropertiesThird(store.getCompanyInfo(), companyInfoVO);
        }
        return storeVO;
    }

    @Transactional
    public void modifyAuditState(StoreAuditStateModifyRequest request) {
        Store store = this.findOne(request.getStoreId());

        if (store.getApplyEnterTime() != null) {
            store.setApplyEnterTime(request.getApplyEnterTime());
        } else {
            store.setAuditReason(request.getAuditReason());
        }
        store.setAuditState(request.getAuditState());
        storeRepository.save(store);
    }

    /**
     * 根据店铺名称获取未删除店铺信息
     *
     * @param storeName
     * @return
     */
    public Store getByStoreNameAndDelFlag(String storeName) {
        return storeRepository.findByStoreNameAndDelFlag(storeName, DeleteFlag.NO).orElse(new Store());
    }


    /**
     * 修改店铺信息
     *
     * @param request
     */
    @GlobalTransactional
    @Transactional
    public Store modifyStoreBaseInfo(StoreModifyRequest request) {
        if (request == null || request.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Store store = storeRepository.findByStoreIdAndDelFlag(request.getStoreId(), DeleteFlag.NO);
        if (store == null) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }

        KsBeanUtil.copyPropertiesThird(request, store);
        store.setStorePinyinName(Pinyin4jUtil.converterToSpell(store.getStoreName(), ","));
        store.setSupplierPinyinName(Pinyin4jUtil.converterToSpell(store.getSupplierName(), ","));
        if(StringUtils.isNotBlank(store.getStorePinyinName())){
            store.setStorePinyinName(Arrays.stream(store.getStorePinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        if(StringUtils.isNotBlank(store.getSupplierPinyinName())){
            store.setSupplierPinyinName(Arrays.stream(store.getSupplierPinyinName().split(",")).limit(3).collect(Collectors.joining(",")));
        }
        return storeRepository.save(store);
    }


    /**
     * 更新店铺码
     *
     * @param request
     */
    @Transactional
    public void updateStoreSmallProgram(StoreInfoSmallProgramRequest request) {
        Store store = this.findOne(request.getStoreId());
        //店铺不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        //更新店铺码字段
        store.setSmallProgramCode(request.getCodeUrl());
        storeRepository.save(store);
    }

    /**
     * 清空店铺码
     */
    @Transactional
    public void clearStoreProgramCode() {
        storeRepository.clearStoreProgramCode();
    }

    /**
     * 根据店铺ID集合查询已过期的店铺ID集合
     *
     * @param ids
     * @return
     */
    public List<Long> findExpiredByStoreIds(List<Long> ids) {
        return storeRepository.findExpiredByStoreIds(ids);
    }

    /**
     * 获取店铺总数量
     *
     * @param queryRequest
     * @return
     */
    public Long countStoreNum(StoreQueryRequest queryRequest) {
        return storeRepository.count(StoreWhereCriteriaBuilder.build(queryRequest));
    }

    /**
     * 根据店铺id列表查询店铺名称
     *
     * @param storeIds
     */
    public List<StoreName> listStoreNameByStoreIds(List<Long> storeIds) {
        return storeRepository.listStoreNameByStoreIds(storeIds);
    }

    /**
     * 查询店铺信息
     *
     * @param storeId
     * @param companyInfoId
     * @return
     */
    public Store findByStoreIdAndCompanyInfoIdAndDelFlag(Long storeId, Long companyInfoId) {
        return storeRepository.findByStoreIdAndCompanyInfoIdAndDelFlag(storeId, companyInfoId, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(StoreErrorCode.NOT_EXIST));
    }

    public StoreVO getStoreBycompanySourceType(StoreBycompanySourceType request) {
        Store store = storeRepository.getStoreBycompanySourceType(request.getCompanySourceType());
        StoreVO storeVO = KsBeanUtil.copyPropertiesThird(store, StoreVO.class);
        storeVO.setCompanyInfo(KsBeanUtil.copyPropertiesThird(store.getCompanyInfo(),CompanyInfoVO.class));
        return storeVO;
    }

    public List<StoreName> listCompanyTypeByStoreIds(List<Long> storeIds){
        return storeRepository.listCompanyTypeByStoreIds(storeIds);
    }



    /**
     * 查询店铺信息分页 用于批量刷入es
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<EsStoreInfoVo> queryEsStoreInfoByPage(Integer pageNum, Integer pageSize) {
//        List<Object> resultList = storeRepository.queryEsStoreInfoByPage(pageNum * pageSize, pageSize);
//        List<EsStoreInfoVo> esStoreIns = resultList.stream()
//                .map(o -> new EsStoreInfoVo().convertFromNativeSQLResult(o))
//                .collect(Collectors.toList());

        List<EsStoreVo> stores = this.queryCompanyInfoIds(pageNum,pageSize);
        List<EsStoreCompanyInfoVo> esStoreCompanyInfoVos = queryCompanyInfoIds(stores.stream().map(EsStoreVo::getCompanyInfoId).collect(Collectors.toList()));

        Map<Long, EsStoreVo> esStoreVoMap = stores.stream()
                .collect(Collectors.toMap(EsStoreVo::getCompanyInfoId, Function.identity(), (key1, key2) -> key2));

        List<EsStoreInfoVo> lists = new ArrayList<>();
        esStoreCompanyInfoVos.forEach(esStoreCompanyInfoVo -> {
            EsStoreInfoVo esStoreInfoVo = new EsStoreInfoVo();
            if(esStoreVoMap.containsKey(esStoreCompanyInfoVo.getCompanyInfoId())){
                EsStoreVo esStoreVo= esStoreVoMap.get(esStoreCompanyInfoVo.getCompanyInfoId());
                KsBeanUtil.copyPropertiesThird(esStoreCompanyInfoVo,esStoreInfoVo);
                KsBeanUtil.copyPropertiesThird(esStoreVo,esStoreInfoVo);
                lists.add(esStoreInfoVo);
            }
        });
        return lists;
    }



    public List<EsStoreVo> queryCompanyInfoIds(Integer pageNum, Integer pageSize) {
        List<Object> resultList = storeRepository.queryStoreInfo(pageNum * pageSize, pageSize);
        List<EsStoreVo> esStores = resultList.stream()
                .map(o -> new EsStoreVo().convertFromNativeSQLResult(o))
                .collect(Collectors.toList());

        return esStores;
    }


    public List<EsStoreCompanyInfoVo> queryCompanyInfoIds(List<Long> CompanyInfoIds) {
        List<Object> resultList = storeRepository.queryCompanyAccount(CompanyInfoIds);
        List<EsStoreCompanyInfoVo> esStores = resultList.stream()
                .map(o -> new EsStoreCompanyInfoVo().convertFromNativeSQLResult(o))
                .collect(Collectors.toList());
        return esStores;
    }


    /**
     * 自定义字段的列表查询
     * @param request 参数
     * @param cols 列名
     * @return 列表
     */
    public List<Store> listCols(StoreQueryRequest request, List<String> cols) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Store> rt = cq.from(Store.class);
        cq.multiselect(cols.stream().map(c -> rt.get(c).alias(c)).collect(Collectors.toList()));
        Specification<Store> spec = StoreWhereCriteriaBuilder.build(request);
        Predicate predicate = spec.toPredicate(rt, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        Sort sort = request.getSort();
        if (sort.isSorted()) {
            cq.orderBy(QueryUtils.toOrders(sort, rt, cb));
        }
        cq.orderBy(QueryUtils.toOrders(request.getSort(), rt, cb));
        return this.converter(entityManager.createQuery(cq).getResultList(), cols);
    }


    /**
     * 查询对象转换
     * @param result
     * @return
     */
    private List<Store> converter(List<Tuple> result, List<String> cols) {
        return result.stream().map(item -> {
            Store store = new Store();
            store.setStoreId(toLong(item, "storeId", cols));
            store.setStoreName(toString(item,"storeName", cols));
            store.setDelFlag(toDeleteFlag(item,"delFlag", cols));
            store.setStoreState(toStoreState(item,"storeState", cols));
            store.setContractStartDate(toDate(item,"contractStartDate", cols));
            store.setContractEndDate(toDate(item,"contractEndDate", cols));
            return store;
        }).collect(Collectors.toList());
    }

    private String toString(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        return tuple.get(name, String.class);
    }

    private DeleteFlag toDeleteFlag(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        return tuple.get(name, DeleteFlag.class);
    }

    private StoreState toStoreState(Tuple tuple, String name, List<String> cols) {
        if (!cols.contains(name)) {
            return null;
        }
        return tuple.get(name, StoreState.class);
    }

    private Long toLong(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        return tuple.get(name, Long.class);
    }

    private LocalDateTime toDate(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        return tuple.get(name, LocalDateTime.class);
    }

    /**
     * 批量店铺开店/关店
     *
     * @param request
     * @return
     */
    @Transactional
    public void closeOrOpenBatch(StoreSwitchBatchRequest request) {
        List<Store> storeList = storeRepository.queryListByIds(DeleteFlag.NO, request.getStoreIdList());
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(storeList)) {
            storeList = storeList.stream().map(store -> {
                store.setStoreState(request.getStoreState());
                store.setStoreClosedReason(request.getStoreClosedReason());
                return store;
            }).collect(Collectors.toList());
        }
        List<Store> savedStore = storeRepository.saveAll(storeList);
        //ares埋点-会员-编辑店铺信息
        for (Store store : storeList) {
            customerAresService.dispatchFunction(AresFunctionType.EDIT_STORE, store);
        }
    }
}
