package com.wanmi.sbc.goods.virtualcoupon.repository;

import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.util.Optional;
import java.util.List;

/**
 * <p>卡券DAO</p>
 *
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@Repository
public interface VirtualCouponRepository extends JpaRepository<VirtualCoupon, Long>,
        JpaSpecificationExecutor<VirtualCoupon> {

    /**
     * 单个删除卡券
     *
     * @author 梁善
     */
    @Modifying
    @Query("update VirtualCoupon set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除卡券
     *
     * @author 梁善
     */
    @Modifying
    @Query("update VirtualCoupon set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<VirtualCoupon> findByIdAndStoreIdAndDelFlag(Long id, Long storeId, DeleteFlag delFlag);

    long countAllByNameAndDelFlag(String name, DeleteFlag deleteFlag);
}
