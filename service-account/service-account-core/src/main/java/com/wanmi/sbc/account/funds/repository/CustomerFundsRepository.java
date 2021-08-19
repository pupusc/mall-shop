package com.wanmi.sbc.account.funds.repository;

import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsIdBase;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsStatistics;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员资金-数据库交互层
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:31
 * @version: 1.0
 */
@Repository
public interface CustomerFundsRepository extends JpaRepository<CustomerFunds, String>, JpaSpecificationExecutor<CustomerFunds> {

    /**
     * 根据用户ID查询会员资金
     *
     * @param customerId
     * @return
     */
    CustomerFunds findByCustomerId(String customerId);


    /**
     * 根据用户IDlist查询会员资金列表
     *
     * @param customerIdList
     * @return
     */
    List<CustomerFunds> findByCustomerIdIn(List<String> customerIdList);

    /**
     * 根据会员ID更新会员账号
     *
     * @param customerId      会员ID
     * @param customerAccount 会员账号
     * @param updateTime      更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.customerAccount = :customerAccount,cf.updateTime = :updateTime where cf.customerId = :customerId")
    int updateCustomerAccountByCustomerId(@Param("customerId") String customerId, @Param("customerAccount") String customerAccount, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新已提现金额
     *
     * @param customerId            会员ID
     * @param alreadyDrawCashAmount 已提现金额
     * @param updateTime            更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.alreadyDrawAmount = :alreadyDrawCashAmount,cf.updateTime = :updateTime where cf.customerId = " +
            ":customerId")
    int updateAlreadyDrawCashAmountByCustomerId(@Param("customerId") String customerId,
                                                @Param("alreadyDrawCashAmount") BigDecimal alreadyDrawCashAmount,
                                                @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新账户余额
     *
     * @param customerId     会员ID
     * @param accountBalance 账户余额
     * @param updateTime     更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.accountBalance = :accountBalance,cf.updateTime = :updateTime where cf.customerId " +
            "= " +
            ":customerId")
    int updateAccountBalanceByCustomerId(@Param("customerId") String customerId,
                                         @Param("accountBalance") BigDecimal accountBalance,
                                         @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新冻结金额
     *
     * @param customerId     会员ID
     * @param blockedBalance 冻结金额
     * @param updateTime     更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.blockedBalance = :blockedBalance,cf.updateTime = :updateTime where cf.customerId " +
            "= " +
            ":customerId")
    int updateBlockedBalanceByCustomerId(@Param("customerId") String customerId,
                                         @Param("blockedBalance") BigDecimal blockedBalance,
                                         @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新会员名称
     *
     * @param customerId   会员ID
     * @param customerName 会员名称
     * @param updateTime   更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.customerName = :customerName ,cf.updateTime = :updateTime where cf.customerId = :customerId")
    int updateCustomerNameByCustomerId(@Param("customerId") String customerId, @Param("customerName") String customerName, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新会员名称、会员账号
     *
     * @param customerId      会员ID
     * @param customerName    会员名称
     * @param customerAccount 会员账号
     * @param updateTime      更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.customerName = :customerName ,cf.customerAccount = :customerAccount ,cf.updateTime = :updateTime where cf.customerId = :customerId")
    int updateCustomerNameAndAccountByCustomerId(@Param("customerId") String customerId, @Param("customerName") String customerName, @Param("customerAccount") String customerAccount, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据会员ID更新是否分销员
     *
     * @param customerId  会员ID
     * @param distributor 是否分销员
     * @param updateTime  更新时间
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.distributor = :distributor ,cf.updateTime = :updateTime where cf.customerId = :customerId")
    int updateIsDistributorByCustomerId(@Param("customerId") String customerId, @Param("distributor") Integer distributor, @Param("updateTime") LocalDateTime updateTime);


    /**
     * 根据会员资金ID和提现金额更新冻结余额、可提现余额（用户提交提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.blockedBalance = cf.blockedBalance + :withdrawAmount ,cf.withdrawAmount =  cf.withdrawAmount - :withdrawAmount,updateTime = now() where cf.customerFundsId = :customerFundsId")
    int submitWithdrawCashApply(@Param("customerFundsId") String customerFundsId, @Param("withdrawAmount") BigDecimal withdrawAmount);

    /**
     * 根据会员资金ID和提现金额更新账户余额、冻结余额（同意用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.accountBalance =  cf.accountBalance - :withdrawAmount ,cf.blockedBalance = cf.blockedBalance - :withdrawAmount,updateTime = now() where cf.customerFundsId = :customerFundsId")
    int agreeWithdrawCashApply(@Param("customerFundsId") String customerFundsId, @Param("withdrawAmount") BigDecimal withdrawAmount);


    /**
     * 根据会员资金ID和提现金额更新冻结余额、可提现余额（驳回用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.blockedBalance = cf.blockedBalance - :withdrawAmount ,cf.withdrawAmount =  cf.withdrawAmount + :withdrawAmount ,updateTime = now()  where cf.customerFundsId = :customerFundsId")
    int rejectWithdrawCashApply(@Param("customerFundsId") String customerFundsId, @Param("withdrawAmount") BigDecimal withdrawAmount);


    /**
     * 增加用户账户余额
     */
    @Modifying
    @Query("update CustomerFunds f set f.accountBalance = f.accountBalance + :amount, f.withdrawAmount = f.withdrawAmount + :amount ,f.income = f.income +1 " +
            ",f.amountReceived = f.amountReceived + :amount where f.customerId = :customerId ")
    int addAmount(@Param("customerId") String customerId, @Param("amount") BigDecimal amount);

    /**
     * 根据会员资金ID和提现金额更新支出金额、支出数（同意用户提现申请）
     *
     * @param customerFundsId 会员资金ID
     * @param withdrawAmount  提现金额
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.amountPaid =  cf.amountPaid + :withdrawAmount ,cf.expenditure = cf.expenditure + 1,updateTime = now() where cf.customerFundsId = :customerFundsId")
    int agreeAmountPaidAndExpenditure(@Param("customerFundsId") String customerFundsId, @Param("withdrawAmount") BigDecimal withdrawAmount);

    /**
     * 统计余额总额、可冻结金额总额、可提现金额总额
     *
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.account.funds.model.root.CustomerFundsStatistics(sum(f.accountBalance),sum(f.blockedBalance),sum(f.withdrawAmount)) FROM CustomerFunds f ")
    CustomerFundsStatistics statistics();

    /**
     * 使用余额，更新余额，可提现金额，支出金额，支出数
     *
     * @param customerFundsId
     * @param expenseAmount
     * @return
     */
    @Modifying
    @Query("update CustomerFunds cf set cf.accountBalance = cf.accountBalance - :expenseAmount, cf.withdrawAmount = cf.withdrawAmount - :expenseAmount" +
            ", cf.amountPaid =  cf.amountPaid + :expenseAmount ,cf.expenditure = cf.expenditure + 1, cf.updateTime = now() " +
            "where cf.customerFundsId = :customerFundsId and cf.withdrawAmount >= :expenseAmount")
    int balancePay(@Param("customerFundsId") String customerFundsId, @Param("expenseAmount") BigDecimal expenseAmount);


    /**
     * 根据会员ID集合查询会员资金列表唯一主键
     *
     * @param customerIds
     * @return
     */
    @Query("select  new com.wanmi.sbc.account.funds.model.root.CustomerFundsIdBase(f.customerId,f.customerFundsId)  from CustomerFunds f where f.customerId in :customerIds ")
    List<CustomerFundsIdBase> getCustomerFundsId(@Param("customerIds") List<String> customerIds);



    /**
     * 批量查询会员资金id
     *
     * @param pageable
     * @return
     */
    @Query(" select customerFundsId  from CustomerFunds  ")
    List<String> listByPage(Pageable pageable);

}
