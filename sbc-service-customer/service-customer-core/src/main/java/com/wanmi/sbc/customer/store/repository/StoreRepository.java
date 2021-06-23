package com.wanmi.sbc.customer.store.repository;

import com.wanmi.sbc.common.enums.CompanySourceType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.customer.store.model.entity.StoreName;
import com.wanmi.sbc.customer.store.model.root.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 店铺信息数据源
 * Created by CHENLI on 2017/11/2.
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {

    /**
     * 根据id查询店铺
     *
     * @param storeId
     * @param deleteFlag
     * @return
     */
    Store findByStoreIdAndDelFlag(Long storeId, DeleteFlag deleteFlag);

    /**
     * 根据店铺Id和商家Id查询店铺信息
     *
     * @param storeId
     * @param companyInfoId
     * @param deleteFlag
     * @return
     */
    @Query("from Store s where s.storeId = :storeId and s.companyInfo.companyInfoId = :companyInfoId " +
            "and s.delFlag = :deleteFlag")
    Optional<Store> findByStoreIdAndCompanyInfoIdAndDelFlag(@Param("storeId") Long storeId,
                                                            @Param("companyInfoId") Long companyInfoId,
                                                            @Param("deleteFlag") DeleteFlag deleteFlag);

    /**
     * 根据id查询店铺
     *
     * @param companyInfoId
     * @param deleteFlag
     * @return
     */
    @Query("from Store s where s.companyInfo.companyInfoId = :companyInfoId and s.delFlag = :deleteFlag")
    Store findStoreByCompanyInfoId(@Param("companyInfoId") Long companyInfoId, @Param("deleteFlag") DeleteFlag
            deleteFlag);

    /**
     * 根据店铺名称查询店铺
     *
     * @param storeName
     * @param deleteFlag
     * @return
     */
    Optional<Store> findByStoreNameAndDelFlag(String storeName, DeleteFlag deleteFlag);


    @Query("from Store s where s.delFlag = :deleteFlag and s.storeId in (:ids)")
    List<Store> queryListByIds(@Param("deleteFlag") DeleteFlag deleteFlag, @Param("ids") List<Long> ids);

    @Modifying
    @Query("update Store s set s.smallProgramCode = null ")
    void clearStoreProgramCode();

    /**
     * 根据店铺ID集合查询已过期的店铺ID集合
     *
     * @param ids
     * @return
     */
    @Query("select s.storeId from Store s where s.contractEndDate < now() and s.storeId in (:ids)")
    List<Long> findExpiredByStoreIds(@Param("ids") List<Long> ids);

    /**
     * 根据店铺id列表查询店铺名称
     */
    @Query("select new com.wanmi.sbc.customer.store.model.entity.StoreName(s.storeId, s.storeName) from Store s where" +
            " s.storeId in (?1)")
    List<StoreName> listStoreNameByStoreIds(List<Long> storeIds);

    @Query(value = "from Store s where s.companySourceType = :type")
    Store getStoreBycompanySourceType(@Param("type") CompanySourceType type);

    /**
     * 根据店铺id列表查询店铺名称
     */
    @Query("select new com.wanmi.sbc.customer.store.model.entity.StoreName(s.storeId,s.storeName, s.companyType) from Store s where" +
            " s.storeId in (?1)")
    List<StoreName> listCompanyTypeByStoreIds(List<Long> storeIds);


    @Query(value = "select s.company_info_id companyInfoId,s.store_id storeId,s.store_name storeName,s.supplier_name supplierName, " +
            "s.company_type companyType," +
            "s.audit_state auditState,s.audit_reason auditReason,s.store_state storeState," +
            "s.store_closed_reason storeClosedReason,s.store_type storeType," +
            "s.del_flag storeDelFlag," +
            "s.contract_start_date contractStartDate,s.contract_end_date contractEndDate" +
            " from store s " +
            " where s.del_flag = 0  " +
            " limit :pageNum, :pageSize"
            , nativeQuery = true)
    List<Object> queryStoreInfo(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);



    @Query(value = "select c.company_info_id companyInfoId, c.company_code companyCode, " +
            "e.account_name accountName," +
            "e.account_state accountState,e.account_disable_reason accountDisableReason," +
            "e.is_master_account isMasterAccount," +
            "c.del_flag companyInfoDelFlag, e.del_flag employeeDelFlag,c.remit_affirm remitAffirm," +
            "c.apply_enter_time applyEnterTime" +
            " from company_info c " +
            " left join employee e on c.company_info_id = e.company_info_id" +
            " AND ( e.is_master_account = 1 AND e.del_flag = 0 ) " +
            " where  c.del_flag = 0 and e.del_flag = 0 " +
            " and c.company_info_id in (:companyInfoIds) "
            , nativeQuery = true)
    List<Object> queryCompanyAccount(@Param("companyInfoIds") List<Long> companyInfoIds);





}
