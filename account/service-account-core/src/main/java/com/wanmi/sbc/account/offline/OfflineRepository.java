package com.wanmi.sbc.account.offline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 线下账号数据源
 * Created by zhangjin on 2017/3/19.
 */
@Repository
public interface OfflineRepository extends JpaRepository<OfflineAccount, Long> {

    /**
     * 删除线下账号
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Query("update OfflineAccount off set off.deleteFlag = 1, off.deleteTime = :deleteTime where off.accountId = " +
            ":offlineAccountId")
    @Modifying
    int removeOfflineAccountById(@Param("offlineAccountId") Long offlineAccountId, @Param("deleteTime") LocalDateTime deleteTime);

    /**
     * 查询所有有效线下账户
     * @return 线下账户列表
     */
    @Query("from OfflineAccount off where off.deleteFlag = 0 and off.bankStatus = 0 order by off.createTime desc")
    List<OfflineAccount> findValidAccounts();

    @Query("from OfflineAccount off where off.deleteFlag = 0 order by off.createTime desc")
    List<OfflineAccount> findManagerAccounts();

    @Query(value = "from OfflineAccount off where off.companyInfoId = :companyInfoId and off.deleteFlag = :defaultFlag")
    List<OfflineAccount> findOfflineAccounts(@Param("companyInfoId") Long companyInfoId, @Param("defaultFlag")
            Integer defaultFlag);

    /**
     * 修改账户状态
     * @param offlineAccountId offlineAccountId
     * @return rows
     */
    @Modifying
    @Query("update OfflineAccount off set off.bankStatus = :accountStatus where off.deleteFlag = 0 and off.accountId = :offlineAccountId")
    int modifyAccountStatus(@Param("offlineAccountId") Long offlineAccountId, @Param("accountStatus") Integer accountStatus);

    int countByBankNoAndDeleteFlag(String bankNo, Integer deleteFlag);

    int countByDeleteFlag(Integer deleteFlag);

    /**
     * 统计供应商账户信息
     * @param companyInfoId
     * @param deleteFlag
     * @return
     */
    int countByCompanyInfoIdAndDeleteFlag(Long companyInfoId, Integer deleteFlag);

    /**
     * 根据银行账号获取账户列表
     *
     * @param bankNo
     * @return
     */
    @Query(value = "from OfflineAccount off where off.bankNo like concat('%', :bankNo, '%') and off.deleteFlag = 0")
    List<OfflineAccount> findOfflineAccounts(@Param("bankNo") String bankNo);

    /**
     * 根据银行账号获取账户列表
     *
     * @param bankNo
     * @return
     */
    @Query(value = "from OfflineAccount off where off.bankNo like concat('%', :bankNo, '%')")
    List<OfflineAccount> findOfflineAccountsWithOutDeleteFlag(@Param("bankNo") String bankNo);
}
