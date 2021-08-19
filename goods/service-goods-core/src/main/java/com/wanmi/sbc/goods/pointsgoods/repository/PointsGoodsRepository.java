package com.wanmi.sbc.goods.pointsgoods.repository;

import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>积分商品表DAO</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@Repository
public interface PointsGoodsRepository extends JpaRepository<PointsGoods, String>,
        JpaSpecificationExecutor<PointsGoods> {

    /**
     * 单个删除积分商品表
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsGoods set delFlag = 1 where pointsGoodsId = ?1")
    int modifyDelFlagById(String pointsGoodsId);

    /**
     * 批量删除积分商品表
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsGoods set delFlag = 1 where pointsGoodsId in ?1")
    int deleteByIdList(List<String> pointsGoodsIdList);

    /**
     * 批量删除积分商品表
     *
     * @author yang
     */
    @Modifying
    @Query("update PointsGoods set delFlag = 1 where goodsInfoId in ?1 and endTime >= now() ")
    int deleteByGoodInfoIdList(List<String> goodsInfoIdList);

    @Modifying
    @Query("update PointsGoods set delFlag = 1 where goodsId in ?1 and endTime >= now() ")
    int deleteByGoodsIdList(List<String> goodsIdList);

    /**
     * 根据积分商品Id减商品活动数量
     *
     * @param stock         活动数量
     * @param pointsGoodsId 积分商品ID
     */
    @Modifying
    @Query("update PointsGoods set stock = stock - ?1, updateTime = now() where pointsGoodsId = ?2 and stock >= ?1")
    int subStockById(Long stock, String pointsGoodsId);

    /**
     * 根据积分商品Id库存清零并停用
     *
     * @param pointsGoodsId 积分商品ID
     */
    @Modifying
    @Query("update PointsGoods set stock = 0, status = 0, updateTime = now() where pointsGoodsId = ?1")
    int resetStockById(String pointsGoodsId);

    /**
     * 查询过期积分商品
     *
     * @return
     */
    @Query("select c from PointsGoods c where c.endTime< now() and delFlag = 0 and stock != 0")
    List<PointsGoods> queryOverdueList();

    /**
     * 根据店铺id查询积分商品
     *
     * @param storeId
     * @return
     */
    @Query("from PointsGoods c where c.goods.storeId = ?1 and delFlag = 0 and status = 1")
    List<PointsGoods> getByStoreId(Long storeId);

    /**
     * 根据积分商品Id加销量
     *
     * @param salesNum      商品销量
     * @param pointsGoodsId 积分商品ID
     */
    @Modifying
    @Query("update PointsGoods set sales = sales + ?1, updateTime = now() where pointsGoodsId = ?2")
    int updatePointsGoodsSalesNum(Long salesNum, String pointsGoodsId);
}
