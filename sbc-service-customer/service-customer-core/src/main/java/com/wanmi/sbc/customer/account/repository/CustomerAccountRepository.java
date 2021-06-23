package com.wanmi.sbc.customer.account.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.account.model.root.CustomerAccount;
import com.wanmi.sbc.customer.account.request.CustomerAccountSaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 客户银行账户信息数据源
 * Created by CHENLI on 2017/4/18.
 */
@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, String> {

    /**
     * 查询客户的银行账户信息
     *
     * @param customerId
     * @return
     */
    @Query("from CustomerAccount l where l.delFlag = 0 and l.customerId = ?1 order by l.createTime desc")
    List<CustomerAccount> findCustomerAccountList(String customerId);

    /**
     * 根据银行账号查询银行账户
     *
     * @param customerAccountNo
     * @param deleteFlag
     * @return
     */
    Optional<CustomerAccount> findByCustomerAccountNoAndDelFlag(String customerAccountNo, DeleteFlag deleteFlag);

    /**
     * 根据ID查询银行账户
     *
     * @param customerAccountId
     * @param deleteFlag
     * @return
     */
    Optional<CustomerAccount> findByCustomerAccountIdAndDelFlag(String customerAccountId, DeleteFlag deleteFlag);

    /**
     * 统计该会员有几条银行账号信息
     *
     * @param customerId
     * @return
     */
    @Query("select count(1) from CustomerAccount l where l.delFlag = 0 and l.customerId = ?1")
    Integer countCustomerAccount(String customerId);

    /**
     * boss端删除会员银行账号信息
     */
    @Modifying
    @Query("update CustomerAccount l set l.delFlag = 1, l.deletePerson=:deletePerson, l.deleteTime=:deleteTime " +
            "where l.delFlag = 0 and l.customerAccountId = :customerAccountId")
    int deleteCustomerAccount(@Param("customerAccountId") String customerAccountId,
                              @Param("deletePerson") String deletePerson,
                              @Param("deleteTime") LocalDateTime deleteTime);

    /**
     * 客户端删除会员银行账号信息
     */
    @Modifying
    @Query("update CustomerAccount l set l.delFlag = 1, l.deletePerson=:customerId, l.deleteTime=:deleteTime " +
            "where l.delFlag = 0 and l.customerAccountId = :customerAccountId and l.customerId = :customerId")
    int deleteCustomerAccountById(@Param("customerAccountId") String customerAccountId,
                                  @Param("customerId") String customerId,
                                  @Param("deleteTime") LocalDateTime deleteTime);

    /**
     * 客户端修改会员银行账户
     *
     * @param account
     * @return
     */
    @Modifying
    @Query("update CustomerAccount l set l.customerAccountName = :#{#account.customerAccountName}," +
            "l.customerAccountNo = :#{#account.customerAccountNo}, l.customerBankName = :#{#account" +
            ".customerBankName}," +
            "l.updatePerson = :#{#customerId}, l.updateTime = :#{#updateTime}" +
            " where l.customerAccountId = :#{#account.customerAccountId} and l.customerId = :#{#customerId}")
    int updateCustomerAccount(@Param("account") CustomerAccountSaveRequest account,
                              @Param("customerId") String customerId,
                              @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员id批量删除银行账号
     *
     * @param customerIds customerIds
     * @return rows
     */
    @Query("update CustomerAccount c set c.delFlag = 1 where c.delFlag = 0 and c.customerId in :customerIds")
    @Modifying
    int deleteCustomerAccountByCustomerIds(@Param("customerIds") List<String> customerIds);
}
