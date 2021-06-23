package com.wanmi.sbc.order.follow.repository;

import com.wanmi.sbc.order.follow.model.root.GoodsCustomerFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品客户收藏数据源
 * Created by daiyitian on 2017/05/17.
 */
@Repository
public interface GoodsCustomerFollowRepository extends JpaRepository<GoodsCustomerFollow, Long>, JpaSpecificationExecutor<GoodsCustomerFollow> {

    /**
     * 根据多个ID编号进行删除
     *
     * @param followIds  收藏ID
     * @param customerId 客户ID
     */
    @Modifying
    @Query("delete from GoodsCustomerFollow where followId in ?1 and customerId = ?2")
    void deleteByFollowIds(List<Long> followIds, String customerId);
}
