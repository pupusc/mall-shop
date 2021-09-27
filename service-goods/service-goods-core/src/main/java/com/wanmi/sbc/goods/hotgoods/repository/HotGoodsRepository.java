package com.wanmi.sbc.goods.hotgoods.repository;

import com.wanmi.sbc.goods.hotgoods.model.root.HotGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>用户签到记录DAO</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@Repository
public interface HotGoodsRepository extends JpaRepository<HotGoods, String>,
        JpaSpecificationExecutor<HotGoods> {

    /**
     * 刷新排序
     * @return
     */
    @Modifying
    @Query(value = "UPDATE t_hot_goods set sort = CEILING(RAND()*9000+1000)", nativeQuery = true)
    Integer updateSort();

    /**
     * 获取所有数据并排序
     * @return
     */
    @Query(value = "select * from  t_hot_goods order by sort desc", nativeQuery = true)
    List<HotGoods> selectAllBySort();
}
