package com.wanmi.sbc.customer.fdpaidcast.repository;

import com.wanmi.sbc.customer.fdpaidcast.model.root.FdPaidCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>樊登付费类型 映射商城付费类型DAO</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@Repository
public interface FdPaidCastRepository extends JpaRepository<FdPaidCast, Long>,
        JpaSpecificationExecutor<FdPaidCast> {

    /**
     * 单个删除樊登付费类型 映射商城付费类型
     * @author tzx
     */
    @Modifying
    @Query("update FdPaidCast set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除樊登付费类型 映射商城付费类型
     * @author tzx
     */
    @Modifying
    @Query("update FdPaidCast set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<FdPaidCast> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    Optional<FdPaidCast> findByFdPayTypeAndDelFlag(Integer fdPayType, DeleteFlag delFlag);

}
