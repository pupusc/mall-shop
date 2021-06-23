package com.wanmi.sbc.goods.customergoodsevaluatepraise.repository;

import com.wanmi.sbc.goods.customergoodsevaluatepraise.model.root.CustomerGoodsEvaluatePraise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * <p>会员商品评价点赞关联表DAO</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@Repository
public interface CustomerGoodsEvaluatePraiseRepository extends JpaRepository<CustomerGoodsEvaluatePraise, String>,
        JpaSpecificationExecutor<CustomerGoodsEvaluatePraise> {

    int deleteCustomerGoodsEvaluatePraiseByCustomerIdAndGoodsEvaluateId(String customerId, String goodsEvaluateId);
}
