package com.wanmi.sbc.order.exceptionoftradepoints.repository;

import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>积分订单抵扣异常表DAO</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@Repository
public interface ExceptionOfTradePointsRepository extends JpaRepository<ExceptionOfTradePoints, String>,
        JpaSpecificationExecutor<ExceptionOfTradePoints> {

    /**
     * 单个删除积分订单抵扣异常表
     * @author caofang
     */
    @Modifying
    @Query("update ExceptionOfTradePoints set delFlag = 1 where id = ?1")
    void deleteById(String id);

    /**
     * 批量删除积分订单抵扣异常表
     * @author caofang
     */
    @Modifying
    @Query("update ExceptionOfTradePoints set delFlag = 1 where id in ?1")
    void deleteByIdList(List<String> idList);

    Optional<ExceptionOfTradePoints> findByIdAndDelFlag(String id, DeleteFlag delFlag);

    Optional<ExceptionOfTradePoints> findByTradeIdAndDelFlag(String tradeId, DeleteFlag delFlag);

}
