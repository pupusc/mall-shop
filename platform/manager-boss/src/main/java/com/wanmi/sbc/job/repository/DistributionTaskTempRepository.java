package com.wanmi.sbc.job.repository;


import com.wanmi.sbc.job.model.entity.DistributionTaskTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hht on 2017/12/4.
 */
@Repository
public interface DistributionTaskTempRepository extends JpaRepository<DistributionTaskTemp, String>, JpaSpecificationExecutor<DistributionTaskTemp> {

    /**
     * 定时任务搜索数据，查询已经过了退货期内的并且退单中数量为0的数据
     * @return
     */
    @Query("from DistributionTaskTemp d where d.returnOrderNum = 0 and d.orderDisableTime < now() ")
    Page<DistributionTaskTemp> queryForTask(Pageable pageable);

    @Modifying
    @Query("update DistributionTaskTemp set returnOrderNum = returnOrderNum + 1  where orderDisableTime >= now() and orderId = ?1 ")
    int addReturnOrderNum(String orderId);

    @Modifying
    @Query("update DistributionTaskTemp set returnOrderNum = returnOrderNum - 1  where returnOrderNum > 0 and orderId = ?1")
    int minusReturnOrderNum(String orderId);

    int deleteByIdIn(List<String> ids);

    /**
     * 根据订单id查询临时表信息
     * @param orderId
     * @return
     */
    List<DistributionTaskTemp> findByOrderId(String orderId);

}
