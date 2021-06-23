package com.wanmi.sbc.goods.customer.repository;

import com.wanmi.sbc.goods.customer.model.root.GoodsCustomerNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品客户全局购买数数据源
 * Created by daiyitian on 2017/05/17.
 */
@Repository
public interface GoodsCustomerNumRepository extends JpaRepository<GoodsCustomerNum, Long>, JpaSpecificationExecutor<GoodsCustomerNum> {


    /**
     * 根据会员编号和SKU编号查询
     * @param customerId 会员编号
     * @param goodsInfoId SKU编号
     * @return
     */
    @Query("from GoodsCustomerNum w where w.customerId = ?1 and w.goodsInfoId = ?2")
    List<GoodsCustomerNum> findByGoodsInfoId(String customerId, String goodsInfoId);

    /**
     * 根据商品ID查询
     * @param customerId 会员编号
     * @param goodsInfoIds 多个SKU编号
     * @return
     */
    @Query("from GoodsCustomerNum w where w.customerId = ?1 and w.goodsInfoId in ?2")
    List<GoodsCustomerNum> findByGoodsInfoIds(String customerId, List<String> goodsInfoIds);
}
