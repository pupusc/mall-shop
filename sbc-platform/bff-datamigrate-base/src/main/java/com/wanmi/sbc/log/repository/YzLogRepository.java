package com.wanmi.sbc.log.repository;


import com.wanmi.sbc.log.model.entity.YzLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by hht on 2017/12/4.
 */
@Repository
public interface YzLogRepository extends JpaRepository<YzLog, String>, JpaSpecificationExecutor<YzLog> {


}
