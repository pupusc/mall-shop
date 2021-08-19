package com.wanmi.sbc.setting.operatedatalog.repository;

import com.wanmi.sbc.setting.operatedatalog.model.root.OperateDataLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>系统日志DAO</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@Repository
public interface OperateDataLogRepository extends JpaRepository<OperateDataLog, Long>,
        JpaSpecificationExecutor<OperateDataLog> {

    /**
     * 单个删除系统日志
     * @author guanfl
     */
    @Modifying
    @Query("update OperateDataLog set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除系统日志
     * @author guanfl
     */
    @Modifying
    @Query("update OperateDataLog set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    @Modifying
    @Query("update OperateDataLog set delFlag = 1 where operateId = ?1")
    void deleteByOperateId(String operateId);

    /**
     * 根据操作标识id找
     * @param operateId
     * @return
     */
    OperateDataLog getByOperateId(String operateId);
}
