package com.wanmi.sbc.goods.cyclebuy.repository;

import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuyGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>周期购活动DAO</p>
 * @author weiwenhao
 * @date 2021-01-21 19:42:48
 */
@Repository
public interface CycleBuyGiftRepository extends JpaRepository<CycleBuyGift, Long>,
        JpaSpecificationExecutor<CycleBuyGift> {

    /**
     * 根据周期购活动id查询赠品列表
     * @param cycleBuyId
     * @return
     */
    List<CycleBuyGift> findByCycleBuyId(Long cycleBuyId);


}
