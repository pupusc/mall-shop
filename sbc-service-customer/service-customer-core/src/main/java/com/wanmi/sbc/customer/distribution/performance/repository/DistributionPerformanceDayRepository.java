package com.wanmi.sbc.customer.distribution.performance.repository;

import com.wanmi.sbc.customer.distribution.performance.model.entity.PerformanceTotal;
import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceDay;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>分销业绩日统计Repository</p>
 * Created by of628-wenzhi on 2019-04-17-16:16.
 */
@Repository
public interface DistributionPerformanceDayRepository extends JpaRepository<DistributionPerformanceDay, String>,
        JpaSpecificationExecutor<DistributionPerformanceDay> {


    /**
     * 按分区查询是否存在
     */
    String QUERY_BY_PARTITION_SQL = "SELECT COUNT(1) FROM information_schema.partitions WHERE  TABLE_NAME = " +
            "'distribution_performance_day' AND PARTITION_NAME = :partitionName ";
    /**
     * 按分区清理数据本地sql
     */
    String CLEAR_BY_PARTITION_SQL = "ALTER TABLE distribution_performance_day TRUNCATE PARTITION :partitionName";

    /**
     * 查询日报表(限制最多31条)
     *
     * @param distributionId 分销员id
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @param sort           排序规则
     * @return
     */
    List<DistributionPerformanceDay> findTop31ByDistributionIdAndTargetDateBetween(String distributionId,
                                                                                   LocalDate startDate,
                                                                                   LocalDate endDate, Sort sort);

    /**
     * 根据分销员id和目标日期获取当天业绩
     *
     * @param distributionId
     * @param targetDate
     * @return
     */
    DistributionPerformanceDay findFirstByDistributionIdAndTargetDate(String distributionId, LocalDate targetDate);


    /**
     * 分批获取去重后的分销员id
     *
     * @param pageable
     * @return
     */
    @Query("select distinct d.distributionId from DistributionPerformanceDay d where d.targetDate between :startDate " +
            "and :endDate")
    List<String> findDistinctDistributionIds(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             Pageable pageable);


    /**
     * @param distributionIds
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT new com.wanmi.sbc.customer.distribution.performance.model.entity.PerformanceTotal(        " +
            "      " +
            "sum(d.saleAmount) AS saleAmount," +
            "sum(d.commission) AS commission," +
            "d.customerId AS customerId," +
            "d.distributionId AS distributionId) " +
            "FROM " +
            "DistributionPerformanceDay d " +
            "WHERE " +
            "targetDate BETWEEN :startDate " +
            "AND :endDate " +
            "AND distributionId IN :ids " +
            "GROUP BY " +
            "d.distributionId," +
            "d.customerId")
    List<PerformanceTotal> findTotalAmountByDistributionIds(@Param("ids") List<String> distributionIds,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

}
