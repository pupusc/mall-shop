package com.wanmi.sbc.goods.cyclebuy.repository;

import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>周期购活动DAO</p>
 * @author weiwenhao
 * @date 2021-01-21 20:01:50
 */
@Repository
public interface CycleBuyRepository extends JpaRepository<CycleBuy, Long>,
        JpaSpecificationExecutor<CycleBuy> {

    /**
     * 单个删除周期购活动
     * @author weiwenhao
     */
    @Modifying
    @Query("update CycleBuy set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除周期购活动
     * @author weiwenhao
     */
    @Modifying
    @Query("update CycleBuy set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    /**
     * 根据goodsId查询
     * @param goodsId
     * @return
     */
    @Query("from CycleBuy where delFlag = 0 and goodsId = ?1")
    CycleBuy findByGoodsId(String goodsId);

}
