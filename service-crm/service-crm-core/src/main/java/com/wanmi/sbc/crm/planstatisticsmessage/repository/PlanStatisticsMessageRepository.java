package com.wanmi.sbc.crm.planstatisticsmessage.repository;

import com.wanmi.sbc.crm.planstatisticsmessage.model.root.PlanStatisticsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据DAO</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@Repository
public interface PlanStatisticsMessageRepository extends JpaRepository<PlanStatisticsMessage, Long>,
        JpaSpecificationExecutor<PlanStatisticsMessage> {

}
