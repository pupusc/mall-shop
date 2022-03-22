package com.wanmi.sbc.customer.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 会员数据层
 * Created by CHENLI on 2017/4/18.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据会员ID查询会员信息
     *
     * @param customerId
     * @return
     */
    Customer findByCustomerIdAndDelFlag(String customerId, DeleteFlag deleteFlag);

    /**
     * 批量查询会员信息
     *
     * @param idList
     * @return
     */
    List<Customer> findByCustomerIdIn(Collection<String> idList);

    /**
     * 检验账户是否存在
     *
     * @param customerAccount
     * @param deleteFlag
     * @return
     */
    Customer findByCustomerAccountAndDelFlag(String customerAccount, DeleteFlag deleteFlag);

    /**
     * 通过樊登用户编号查询是否用户是否存在库里
     *
     * @param fanDengUserNo
     * @param deleteFlag
     * @return
     */
    Customer findByFanDengUserNoAndDelFlag(String fanDengUserNo, DeleteFlag deleteFlag);

    /**
     * 查询成长值大于0的客户
     *
     * @return
     */
    @Query("from Customer c where c.growthValue > 0")
    List<Customer> findHasGrowthValueCustomer();

    /**
     * 审核客户状态
     *
     * @param checkState
     * @param customerId
     * @return
     */
    @Modifying
    @Query("update Customer c set c.checkState = :checkState,c.checkTime = :checkTime where c.delFlag = 0 and c.customerId = " +
            ":customerId")
    int checkCustomerState(@Param("checkState") CheckState checkState, @Param("customerId") String customerId,@Param(
            "checkTime") LocalDateTime checkTime);

    /**
     * 审核企业会员
     *
     * @param enterpriseCheckState
     * @param customerId
     * @return
     */
    @Modifying
    @Query("update Customer c set c.enterpriseCheckState = :enterpriseCheckState, c.checkState.checkState = :checkState, " +
            "c.enterpriseCheckReason = :enterpriseCheckReason, " +
            "c.checkTime = :checkTime where c.delFlag = 0 and c.customerId = :customerId")
    int checkEnterpriseCustomer(@Param("enterpriseCheckState") EnterpriseCheckState enterpriseCheckState,
                                @Param("checkState") CheckState checkState,
                                @Param("enterpriseCheckReason") String enterpriseCheckReason,
                                @Param("customerId") String customerId,
                                @Param("checkTime") LocalDateTime checkTime);

    /**
     * 批量删除会员
     *
     * @param customerIds
     * @return
     */
    @Modifying
    @Query("update Customer c set c.delFlag = 1 where c.delFlag = 0 and c.customerId in :customerIds")
    int deleteByCustomerId(@Param("customerIds") List<String> customerIds);

    /**
     * 删除会员等级时，把该等级下的所有会员转到默认等级下
     *
     * @param defaultLevelId
     * @param customerLevelId
     * @return
     */
    @Modifying
    @Query("update Customer c set c.customerLevelId = :defaultLevelId where c.delFlag = 0 and c.customerLevelId = " +
            ":customerLevelId")
    int updateCustomerLevel(@Param("defaultLevelId") Long defaultLevelId, @Param("customerLevelId") Long
            customerLevelId);

    /**
     * 解锁
     *
     * @param customerId
     * @return
     */
    @Modifying
    @Query("update Customer e set e.loginLockTime = null, e.loginErrorCount = 0 where e.customerId = :customerId")
    int unlockCustomer(@Param("customerId") String customerId);

    /**
     * 修改登录次数
     *
     * @param customerId
     */
    @Modifying
    @Query("update Customer e set e.loginErrorCount = IFNULL(e.loginErrorCount,0) + 1 where e.customerId = :customerId")
    int updateloginErrorCount(@Param("customerId") String customerId);

    /**
     * 修改锁时间
     *
     * @param customerId customerId
     * @return
     */
    @Modifying
    @Query("update Customer e set e.loginLockTime = ?2 where e.customerId =?1")
    int updateLoginLockTime(String customerId, LocalDateTime localDateTime);

    /**
     * 修改客户登录时间
     *
     * @param customerId customerId
     * @param loginTime  loginTime
     * @param loginIp    loginIp
     * @return rows
     */
    @Modifying
    @Query("update Customer e set e.loginTime = ?2, e.loginErrorCount = 0, e.loginLockTime = null, e.loginIp = ?3 " +
            "where e.customerId =?1")
    int updateLoginTime(String customerId, LocalDateTime loginTime, String loginIp);

    /**
     * 修改客户登录时间
     *
     * @param customerId customerId
     * @param loginTime  loginTime
     * @param loginIp    loginIp
     * @return rows
     */
    @Modifying
    @Query("update Customer e set e.loginTime = ?2, e.loginErrorCount = 0, e.loginLockTime = null, e.loginIp = ?3 , e.fanDengUserNo = ?4 " +
            "where e.customerId =?1")
    int updateLoginTimeAndFanDengUserNo(String customerId, LocalDateTime loginTime, String loginIp,String fanDengUserNo);

    @Modifying
    @Query("update Customer e set e.wxMiniOpenId = ?2, e.wxMiniUnionId = ?3 where e.customerId =?1")
    int updateOpenIdAndUnionId(String customerId, String openId, String unionId);

    /**
     * 修改绑定手机号
     *
     * @param customerId
     */
    @Modifying
    @Query("update Customer e set e.customerAccount = :customerAccount where e.delFlag = 0 and e.customerId = " +
            ":customerId")
    int updateCustomerAccount(@Param("customerId") String customerId, @Param("customerAccount") String customerAccount);


    /**
     * 新的修改绑定手机号sql
     *
     * @param customerId
     */
    @Modifying
    @Query("update Customer e set e.customerAccount = ?2,e.loginTime = ?3 ,e.loginIp = ?4 where e.delFlag = 0 and e.customerId = ?1 ")
    int updateNewCustomerAccount(@Param("customerId") String customerId, @Param("customerAccount") String customerAccount,@Param("loginTime") LocalDateTime loginTime, @Param("loginIp") String loginIp);

    /**
     * 扣除会员积分
     *
     * @param customerId
     * @param points
     * @return
     */
    @Modifying
    @Query("update Customer c set c.pointsAvailable = c.pointsAvailable - :points, c.pointsUsed = c.pointsUsed + " +
            ":points where c.customerId = :customerId and c.pointsAvailable >= :points")
    int updateCustomerPoints(@Param("customerId") String customerId, @Param("points") Long points);

    /**
     * 修改会有的业务员
     *
     * @param employeeIdPre employeeIdPre
     * @param employeeId    employeeId
     * @return rows
     */
    @Modifying
    @Query("update CustomerDetail c set c.employeeId = :employeeId where c.delFlag = 0 and c.employeeId = " +
            ":employeeIdPre")
    int updateCustomerByEmployeeId(@Param("employeeIdPre") String employeeIdPre, @Param("employeeId") String
            employeeId);

    /**
     * 查询成长值达到x的会员id列表
     *
     * @param growthValue 成长值
     * @return
     */
    @Query("select c.customerId from Customer c  where c.growthValue >= :growthValue and c.delFlag = 0")
    List<String> findByGrowthValue(@Param("growthValue") Long growthValue);

    @Query(value = "select new com.wanmi.sbc.customer.model.root.CustomerBase(c.customerId,c.customerAccount) FROM " +
            "Customer c " +
            " where c.customerId = :customerId and c.delFlag = :delFlag")
    CustomerBase getBaseCustomerByCustomerId(@Param("customerId") String customerId, @Param("delFlag") DeleteFlag
            delFlag);

    /**
     * 根据会员ID查询会员等级ID
     *
     * @param customerIds
     * @return
     */
    @Query("select new com.wanmi.sbc.customer.model.root.CustomerBase(c.customerId,c.customerLevelId) FROM Customer c" +
            " where  c.customerId in :customerIds")
    List<CustomerBase> findCustomerLevelIdByCustomerIds(@Param("customerIds") List<String> customerIds);

    /**
     * 根据会员ID查询会员等级ID
     *
     * @param customerIds
     * @return
     */
    @Query("select new com.wanmi.sbc.customer.model.root.CustomerBase(c.customerId,c.customerAccount)  FROM Customer " +
            "c where  c.customerId in :customerIds")
    List<CustomerBase> getBaseCustomerByCustomerIds(@Param("customerIds") List<String> customerIds);

    @Query("select c.customerId from Customer c  where c.delFlag = 0 and c.checkState = 1 ")
    List<String> findCustomerIdByPageable(Pageable pageable);

    @Query("select c.customerId from Customer c  where c.delFlag = 0 and c.checkState = 1 and c.customerLevelId in " +
            ":customerLevelIds")
    List<String> findCustomerIdByCustomerLevelIds(@Param("customerLevelIds") List<Long> customerLevelIds, Pageable
            pageable);


    /**
     * 根据会员ID查询可用积分
     * @param customerId
     * @return
     */
    @Query("select c.pointsAvailable from Customer c  where c.delFlag = 0 and c.customerId = ?1 ")
    Long findPointsAvailableByCustomerId(String customerId);

    @Query(value = "select customer_account from customer where customer_account in (:phones) and del_flag=0",nativeQuery = true)
    List<String> getCustomerByPhones(@Param("phones") List<String> phones);

    /**
     * 根据会员ID查询会员成长值
     *
     * @param customerIds
     * @return
     */
    @Query("select new com.wanmi.sbc.customer.model.root.CustomerBase(c.customerId,c.customerAccount,c.growthValue,c.customerType) FROM Customer c" +
            " where  c.customerId in :customerIds")
    List<CustomerBase> findGrowthValueByCustomerIds(@Param("customerIds") List<String> customerIds);

    /**
     * 根据会员ID查询会员账号、审核状态、企业会员状态、驳回原因
     *
     * @param customerIds
     * @return
     */
    @Query("select new com.wanmi.sbc.customer.model.root.CustomerBase(c.customerId,c.customerAccount,c.customerLevelId,c.checkState,c.enterpriseCheckState,c.enterpriseCheckReason) " +
            " FROM Customer c" +
            " where  c.customerId in :customerIds ")
    List<CustomerBase> findCustomerBaseByCustomerIds(@Param("customerIds") List<String> customerIds);

    @Query("select yzUid from Customer where yzUid in (?1)")
    List<Long> findByYzUids(List<Long> ids);

    Customer findByYzUid(Long yzUid);
}
