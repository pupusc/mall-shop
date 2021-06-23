package com.wanmi.sbc.account.company.repository;

import com.wanmi.sbc.account.company.model.root.CompanyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商户账号Repository
 * Created by sunkun on 2017/11/30.
 */
@Repository
public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, Long> {
    /**
     * 删除商户账号
     *
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Query("update CompanyAccount off set off.deleteFlag = 1, off.deleteTime = :deleteTime where off.accountId = " +
            ":offlineAccountId")
    @Modifying
    int removeOfflineAccountById(@Param("offlineAccountId") Long offlineAccountId,
                                 @Param("deleteTime") LocalDateTime deleteTime);

    /**
     * 查询所有有效线下账户
     *
     * @return 线下账户列表
     */
    @Query("from CompanyAccount off where off.deleteFlag = 0 and off.bankStatus = 0 order by off.createTime desc")
    List<CompanyAccount> findValidAccounts();

    /**
     * 查询所有线下账户
     *
     * @return 线下账户列表
     */
    @Query("from CompanyAccount off where off.deleteFlag = 0 order by off.createTime desc")
    List<CompanyAccount> findManagerAccounts();

    /**
     * 根据公司信息Id和默认标识查询线下账户
     *
     * @param companyInfoId 公司信息Id
     * @param defaultFlag   默认标识
     * @return 线下账户列表
     */
    @Query(value = "from CompanyAccount off where off.companyInfoId = :companyInfoId and off.deleteFlag = :defaultFlag")
    List<CompanyAccount> findOfflineAccounts(@Param("companyInfoId") Long companyInfoId, @Param("defaultFlag")
            Integer defaultFlag);

    /**
     * 修改账户状态
     *
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Modifying
    @Query("update CompanyAccount off set off.bankStatus = :accountStatus where off.deleteFlag = 0 and off.accountId " +
            "= :offlineAccountId")
    int modifyAccountStatus(@Param("offlineAccountId") Long offlineAccountId,
                            @Param("accountStatus") Integer accountStatus);

    /**
     * 根据银行账号和删除标识统计线下账户信息
     *
     * @param bankNo        银行账号
     * @param compantInfoId 商家id
     * @param bankCode      银行编码
     * @param deleteFlag    删除标识
     * @return 线下账户总数
     */
    List<CompanyAccount> getByCompanyInfoIdAndBankNoAndBankCodeAndDeleteFlag(Long compantInfoId, String bankNo, String bankCode,
                                                              Integer deleteFlag);

    /**
     * 根据删除标识统计线下账户总数
     *
     * @param deleteFlag 删除标识
     * @return 线下账户总数
     */
    int countByDeleteFlag(Integer deleteFlag);

    /**
     * 统计商家账户信息
     *
     * @param companyInfoId 公司信息Id
     * @param deleteFlag    删除标识
     * @return 线下账户总数
     */
    int countByCompanyInfoIdAndDeleteFlag(Long companyInfoId, Integer deleteFlag);
}
