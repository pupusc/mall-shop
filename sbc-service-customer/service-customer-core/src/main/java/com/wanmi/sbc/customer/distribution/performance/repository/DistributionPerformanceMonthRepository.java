package com.wanmi.sbc.customer.distribution.performance.repository;

import com.wanmi.sbc.customer.distribution.performance.model.root.DistributionPerformanceMonth;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>分销业绩月统计Repository</p>
 * Created by of628-wenzhi on 2019-04-17-16:16.
 */
@Repository
public interface DistributionPerformanceMonthRepository extends JpaRepository<DistributionPerformanceMonth, String>,
        JpaSpecificationExecutor<DistributionPerformanceMonth> {

    /**
     * 按分区查询是否存在
     */
    String QUERY_BY_PARTITION_SQL="SELECT COUNT(1) FROM information_schema.partitions WHERE  TABLE_NAME = " +
            "'distribution_performance_month' AND PARTITION_NAME = :partitionName";

    /**
     * 按分区清理数据本地sql
     */
    String CLEAR_BY_PARTITION_SQL = "ALTER TABLE distribution_performance_month TRUNCATE PARTITION :partitionName";

    /**
     * 查询月报表
     *
     * @param distributionId 分销员id
     * @param startDate     开始日期
     * @param endDate       结束日期
     * @param sort           排序规则
     * @return
     */
    List<DistributionPerformanceMonth> findByDistributionIdAndTargetDateBetween(String distributionId,
                                                                                LocalDate startDate,
                                                                                LocalDate endDate,
                                                                                Sort sort);

}
