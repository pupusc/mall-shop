package com.wanmi.sbc.customer.email.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.email.model.root.CustomerEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户邮箱配置数据源
 */
@Repository
public interface CustomerEmailRepository extends JpaRepository<CustomerEmail, Long>,
        JpaSpecificationExecutor<CustomerEmail> {
    /**
     * 根据客户id查询客户邮箱
     *
     * @param customerId
     * @param deleteFlag
     * @return
     */
    List<CustomerEmail> findCustomerEmailsByCustomerIdAndDelFlagOrderByCreateTime(String customerId, DeleteFlag
            deleteFlag);

    /**
     * 根据邮箱id查询客户邮箱
     *
     * @param customerEmailId
     * @param deleteFlag
     * @return
     */
    CustomerEmail findCustomerEmailByCustomerEmailIdAndDelFlag(String customerEmailId, DeleteFlag deleteFlag);

    /**
     * 查询除自身外的名称是否存在(判重)
     *
     * @param customerEmailId 邮箱id
     * @param customerId      客户id
     * @param emailAddress    邮箱地址
     * @return
     */
    @Query("from CustomerEmail c where c.delFlag = '0' and c.customerEmailId <> ?1 and c.customerId = ?2 " +
            "and c.emailAddress = ?3 ")
    CustomerEmail findByEmailAddressNotSelf(String customerEmailId, String customerId, String emailAddress);

    /**
     * 根据客户邮箱ID删除财务邮箱
     *
     * @param customerEmailId
     * @param delPerson
     * @param delTime
     */
    @Query("update CustomerEmail c set c.delFlag = '1', c.delPerson = ?2, c.delTime = ?3 where c.customerEmailId = ?1")
    @Modifying
    void deleteCustomerEmailByCustomerEmailId(String customerEmailId, String delPerson, LocalDateTime delTime);
}
