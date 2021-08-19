package com.wanmi.sbc.customer.distribution.repository;

import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRanking;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase;
//import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * <p>用户分销排行榜DAO</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@Repository
public interface DistributionCustomerRankingRepository extends JpaRepository<DistributionCustomerRanking, String>,
        JpaSpecificationExecutor<DistributionCustomerRanking> {
    /**
     * 删除当天数据
     * @param deleteDate
     * @return
     */
    @Modifying
    @Transactional
    @Query("delete from DistributionCustomerRanking d where d.targetDate =:deleteDate")
    int deleteByDate(@Param("deleteDate") LocalDate deleteDate);

    /**
     * 删除目标日期之前的数据
     * @param deleteDate
     * @return
     */
    @Modifying
    @Transactional
    @Query("delete from DistributionCustomerRanking d where d.targetDate < :deleteDate")
    int deleteFromDate(@Param("deleteDate") LocalDate deleteDate);

    /**
     * 统计最近七天邀新人数
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase(c.requestCustomerId, count(DISTINCT c.invitedNewCustomerId) as num ) " +
            " FROM InviteNewRecord c " +
            " where date(c.registerTime) >= :startDate and  date(c.registerTime) <=  :endDate " +
            " group by c.requestCustomerId " +
            " order by num desc ")
    List<DistributionCustomerRankingBase> countWeekInviteCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);



    /**
     * 统计最近七天有效邀新人数
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase(c.requestCustomerId, count(DISTINCT c.invitedNewCustomerId) as num ) " +
            " FROM InviteNewRecord c " +
            " where date(c.registerTime) >= :startDate and  date(c.registerTime)  <= :endDate " +
            " and c.availableDistribution = 1" +
            " group by c.requestCustomerId " +
            " order by num DESC")
    List<DistributionCustomerRankingBase> countWeekInviteAvailableCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);


    /**
     * 统计最近七天销售额
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase(c.customerId, sum(c.saleAmount) as saleAmount ) " +
            " FROM DistributionPerformanceDay c " +
            " where target_date >= :startDate and target_date <= :endDate " +
            " group by c.customerId " +
            " order by saleAmount desc ")
    List<DistributionCustomerRankingBase> countWeekSaleAmount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    /**
     * 统计最近七天预估收益
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase(c.customerId, sum(c.commission) as commission ) " +
            " FROM DistributionPerformanceDay c " +
            " where target_date >= :startDate and  target_date <= :endDate " +
            " group by c.customerId " +
            " order by commission desc ")
    List<DistributionCustomerRankingBase> countWeekCommission(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);



}
