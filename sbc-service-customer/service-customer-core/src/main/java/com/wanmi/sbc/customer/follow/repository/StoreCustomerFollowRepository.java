package com.wanmi.sbc.customer.follow.repository;

import com.wanmi.sbc.customer.follow.model.root.StoreCustomerFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品客户收藏数据源
 * Created by daiyitian on 2017/05/17.
 */
@Repository
public interface StoreCustomerFollowRepository extends JpaRepository<StoreCustomerFollow, Long>,
        JpaSpecificationExecutor<StoreCustomerFollow> {

    /**
     * 查询关注的店铺
     * 按收藏时间倒序
     *
     * @param customerId 客户ID
     * @param pageable   分类数据
     * @return
     */
    @Query(value = "select s.store_id,s.store_name,s.company_type,s.store_logo,s.store_sign,s.contact_person,s" +
            ".contact_mobile,s.contact_email,s.del_flag,s.store_state,s.contract_start_date,s.contract_end_date,s" +
            ".city_id,s.supplier_name,s.province_id, s.area_id from store_customer_follow t left join store s on s" +
            ".company_info_id = t.company_info_id and s.store_id = t.store_id  " +
            "where t.customer_id = ?1 and s.audit_state =  1 and s.del_flag = 0 " +
            "order by t.follow_time desc ",
            countQuery = "select count(1) from store_customer_follow t left join store s on s.company_info_id = t" +
                    ".company_info_id and s.store_id = t.store_id  " +
                    "where t.customer_id = ?1 and s.audit_state =  1 and s.del_flag = 0", nativeQuery = true)
    Page<Object> pageFollow(String customerId, Pageable pageable);

    /**
     * 查询关注的店铺
     * 按收藏时间倒序
     *
     * @param customerId 客户ID
     * @param pageable   分类数据
     * @return
     */
    @Query(value = "select s.store_id,s.store_name,s.company_type,s.store_logo,s.store_sign,s.contact_person,s" +
            ".contact_mobile,s.contact_email,s.del_flag,s.store_state,s.contract_start_date,s.contract_end_date,s" +
            ".city_id,s.supplier_name,s.province_id, s.area_id from store_customer_follow t left join store s on s" +
            ".company_info_id = t.company_info_id and s.store_id = t.store_id  " +
            "where t.customer_id = ?1 and t.store_id = ?2 and s.audit_state =  1 and s.del_flag = 0 " +
            "order by t.follow_time desc ",
            countQuery = "select count(1) from store_customer_follow t left join store s on s.company_info_id = t" +
                    ".company_info_id and s.store_id = t.store_id  " +
                    "where t.customer_id = ?1 and t.store_id = ?2 and s.audit_state =  1 and s.del_flag = 0", nativeQuery = true)
    Page<Object> pageFollowAndStoreId(String customerId, Long storeId, Pageable pageable);

    /**
     * 根据多个ID编号进行删除
     *
     * @param storeIds   店铺ID
     * @param customerId 客户ID
     */
    @Modifying
    @Query("delete from StoreCustomerFollow where storeId in ?1 and customerId = ?2")
    void deleteByFollowIds(List<Long> storeIds, String customerId);

    /**
     * 根据店铺ID和客户ID查询
     *
     * @param storeId    店铺ID
     * @param customerId 客户ID
     */
    @Modifying
    @Query("from StoreCustomerFollow where storeId in ?1 and customerId = ?2")
    List<StoreCustomerFollow> findByStoreId(List<Long> storeId, String customerId);

    /**
     * 统计店铺关注数量
     *
     * @param customerId 客户ID
     * @return 店铺关注数量
     */
    @Query(value = "select count(1) from store_customer_follow t left join store s on s.company_info_id = t" +
            ".company_info_id and s.store_id = t.store_id  " +
            "where t.customer_id = ?1 and s.audit_state =  1 and s.del_flag = 0", nativeQuery = true)
    Long queryStoreFollowNum(String customerId);
    
    /**
     * @Description:
     * @param:  storeId 店铺ID
     * @Author: Bob
     * @Date: 2019-04-03 10:22
     */
    long countByStoreId(long storeId);

}
