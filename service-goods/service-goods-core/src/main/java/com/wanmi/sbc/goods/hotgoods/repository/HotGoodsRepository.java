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
    @Query("UPDATE HotGoods set sort = CEILING(RAND()*9000+1000) where isRefresh = 1 ")
    int updateSort();

    /**
     * 获取所有数据并排序
     * @return
     */
    @Query("from HotGoods where type in (1,2) order by sort desc")
    List<HotGoods> selectAllBySort();

    /**
     * 根据类型获取所有数据并排序
     * @return
     */
    @Query("from HotGoods where type in ?1 order by sort asc")
    List<HotGoods> selectAllByTypes(List<Integer> types);
}
