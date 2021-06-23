package com.wanmi.sbc.goods.goodstobeevaluate.repository;

import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * <p>订单商品待评价DAO</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@Repository
public interface GoodsTobeEvaluateRepository extends JpaRepository<GoodsTobeEvaluate, String>,
        JpaSpecificationExecutor<GoodsTobeEvaluate> {

    @Modifying
    int deleteByOrderNoAndGoodsInfoId(String oId, String skuId);
	
}
