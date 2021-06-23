package com.wanmi.sbc.goods.goodsrestrictedcustomerrela.repository;

import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>限售配置会员关系表DAO</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@Repository
public interface GoodsRestrictedCustomerRelaRepository extends JpaRepository<GoodsRestrictedCustomerRela, Long>,
        JpaSpecificationExecutor<GoodsRestrictedCustomerRela> {

    /**
     * 批量删除会员限售
     * @param restrictedSaleIds
     * @return
     */
    @Modifying
    @Query(value = "delete from GoodsRestrictedCustomerRela g where g.restrictedSaleId in ?1")
    int deleteAllByRestrictedSaleIds(List<Long> restrictedSaleIds);

}
