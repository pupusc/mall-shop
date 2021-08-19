package com.wanmi.sbc.customer.storecustomer.repository;

import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 店铺-会员关联数据源
 * Created by bail on 2017/11/15.
 */
@Repository
public interface StoreCustomerRepository extends JpaRepository<StoreCustomerRela, String>,
        JpaSpecificationExecutor<StoreCustomerRela> {

    @Query(value = "SELECT s.customer_id, c.customer_account, d.customer_name, s.store_level_id, l" +
            ".level_name FROM store_customer_rela s LEFT JOIN customer c ON s.customer_id = c.customer_id " +
            "LEFT JOIN customer_detail d ON s.customer_id = d.customer_id LEFT JOIN store_level l ON s" +
            ".store_level_id = l.store_level_id WHERE s.store_id = :storeId AND c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_status = 0", nativeQuery = true)
    List<Object> findInfoByStoreId(@Param("storeId") Long storeId);

    /**
     * 查询店铺关联的会员，不区分会员是否禁用
     *
     * @param storeId
     * @return
     */
    @Query(value = "SELECT s.customer_id, c.customer_account, d.customer_name, s.store_level_id, l" +
            ".level_name FROM store_customer_rela s LEFT JOIN customer c ON s.customer_id = c.customer_id " +
            "LEFT JOIN customer_detail d ON s.customer_id = d.customer_id LEFT JOIN store_level l ON s" +
            ".store_level_id = l.store_level_id WHERE s.store_id = :storeId AND c.del_flag = 0 AND c" +
            ".check_state = 1", nativeQuery = true)
    List<Object> findAllByStoreId(@Param("storeId") Long storeId);

    /**
     * 查询平台所有会员
     *
     * @return
     */
    @Query(value = "SELECT c.customer_id, c.customer_account, d.customer_name, c.customer_level_id, l" +
            ".customer_level_name FROM customer c LEFT JOIN customer_detail d " +
            "ON c.customer_id = d.customer_id LEFT JOIN customer_level l ON c" +
            ".customer_level_id = l.customer_level_id WHERE c.del_flag = 0 AND c" +
            ".check_state = 1 LIMIT 0, 50", nativeQuery = true)
    List<Object> findBossAll();

    /**
     * 查询平台所有会员
     *
     * @return
     */
    @Query(value = "SELECT c.customer_id, c.customer_account, d.customer_name, c.customer_level_id, l" +
            ".customer_level_name FROM customer c LEFT JOIN customer_detail d " +
            "ON c.customer_id = d.customer_id LEFT JOIN customer_level l ON c" +
            ".customer_level_id = l.customer_level_id WHERE c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_name like CONCAT('%',:customerName,'%') LIMIT 0, 50", nativeQuery = true)
    List<Object> findBossByCustomerName(@Param("customerName") String customerName);

    @Query(value = "SELECT s.customer_id, c.customer_account, d.customer_name, s.store_level_id, l" +
            ".level_name FROM store_customer_rela s LEFT JOIN customer c ON s.customer_id = c.customer_id " +
            "LEFT JOIN customer_detail d ON s.customer_id = d.customer_id LEFT JOIN store_level l ON s" +
            ".store_level_id = l.store_level_id WHERE s.store_id = :storeId AND c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_status = 0 AND c.customer_account like CONCAT('%',:customerAccount,'%') " +
            "AND  (:employeeIds is null or d.employee_id in :employeeIds) LIMIT 0, :pageSize", nativeQuery = true)
    List<Object> findInfoByStoreId(@Param("storeId") Long storeId, @Param("customerAccount") String customerAccount,
                                   @Param("pageSize") Integer pageSize, @Param("employeeIds") List<String> employeeIds);

    @Query(value = "SELECT c.customer_id, c.customer_account, d.customer_name, c.customer_level_id, l" +
            ".customer_level_name FROM customer c LEFT JOIN customer_detail d " +
            "ON c.customer_id = d.customer_id LEFT JOIN customer_level l ON c" +
            ".customer_level_id = l.customer_level_id WHERE c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_status = 0 AND c.customer_account like CONCAT('%',:customerAccount,'%') " +
            "AND  d.employee_id in :employeeIds LIMIT 0, :pageSize", nativeQuery = true)
    List<Object> findBoss(@Param("customerAccount") String customerAccount,
                          @Param("pageSize") Integer pageSize, @Param("employeeIds") List<String> employeeIds);

    @Query(value = "SELECT s.customer_id, c.customer_account, d.customer_name, s.store_level_id, l" +
            ".level_name FROM store_customer_rela s LEFT JOIN customer c ON s.customer_id = c.customer_id " +
            "LEFT JOIN customer_detail d ON s.customer_id = d.customer_id LEFT JOIN store_level l ON s" +
            ".store_level_id = l.store_level_id WHERE s.store_id = :storeId AND c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_status = 0 AND c.customer_account like CONCAT('%',:customerAccount,'%') " +
            " LIMIT 0, :pageSize", nativeQuery = true)
    List<Object> findInfoByStoreId(@Param("storeId") Long storeId, @Param("customerAccount") String customerAccount,
                                   @Param("pageSize") Integer pageSize);

    @Query(value = "SELECT c.customer_id, c.customer_account, d.customer_name, c.customer_level_id, l" +
            ".customer_level_name FROM customer c LEFT JOIN customer_detail d " +
            "ON c.customer_id = d.customer_id LEFT JOIN customer_level l ON c" +
            ".customer_level_id = l.customer_level_id WHERE c.del_flag = 0 AND c" +
            ".check_state = 1 AND d.customer_status = 0 AND c.customer_account like CONCAT('%',:customerAccount,'%') " +
            " LIMIT 0, :pageSize", nativeQuery = true)
    List<Object> findBoss(@Param("customerAccount") String customerAccount,
                          @Param("pageSize") Integer pageSize);


    @Query("select c.customerId from StoreCustomerRela c  where c.storeId = :storeId ")
    List<String> findCustomerIdByStoreId(@Param("storeId") Long storeId, Pageable pageable);


    @Query("select c.customerId from StoreCustomerRela c  where c.storeId = :storeId and c.storeLevelId in :storeLevelIds")
    List<String> findCustomerIdByStoreIdAndStoreLevelIdsIn(@Param("storeId") Long storeId, @Param("storeLevelIds") List<Long> storeLevelIds, Pageable pageable);

    /**
     * 根据会员ID查询店铺-会员关系
     *
     * @param customerIds
     * @return
     */
    List<StoreCustomerRela> findByCompanyInfoIdAndCustomerIdIn(Long companyInfoId, List<String> customerIds);

    /**
     * 根据会员ID查询店铺-会员关系
     *
     * @param customerIds
     * @return
     */
    List<StoreCustomerRela> findByCustomerIdIn(List<String> customerIds);

    @Query(value = "SELECT s.customer_id, c.customer_account, d.customer_name, s.store_level_id, l" +
            ".level_name FROM store_customer_rela s LEFT JOIN customer c ON s.customer_id = c.customer_id " +
            "LEFT JOIN customer_detail d ON s.customer_id = d.customer_id LEFT JOIN store_level l ON s" +
            ".store_level_id = l.store_level_id WHERE s.store_id = :storeId AND c.del_flag = 0 AND c" +
            ".check_state = 1 and s.customer_id in (:customerIds)", nativeQuery = true)
    List<Object> findAllByStoreIdAndCustomerIds(Long storeId, List<String> customerIds);
}
