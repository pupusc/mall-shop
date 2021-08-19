package com.wanmi.sbc.goods.goodssharerecord.repository;

import com.wanmi.sbc.goods.goodssharerecord.model.root.GoodsShareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>商品分享DAO</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@Repository
public interface GoodsShareRecordRepository extends JpaRepository<GoodsShareRecord, Long>,
        JpaSpecificationExecutor<GoodsShareRecord> {

}
