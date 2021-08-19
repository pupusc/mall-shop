package com.wanmi.sbc.message.pushdetail.repository;

import com.wanmi.sbc.message.bean.enums.PushStatus;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>推送详情DAO</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@Repository
public interface PushDetailRepository extends JpaRepository<PushDetail, String>,
        JpaSpecificationExecutor<PushDetail> {

    @Query("from PushDetail where nodeId is not null and sendStatus not in :status and createTime > :dateTime and " +
            "openSum is null ")
    List<PushDetail> getPushDetails(@Param("status") List<PushStatus> pushStatuses,
                                                        @Param("dateTime") LocalDateTime dateTime, Pageable pageable);
}
