package com.wanmi.sbc.goods.storecate.repository;

import com.wanmi.sbc.goods.storecate.model.pk.StoreCateGoodsRelaPK;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品-店铺分类关联数据源
 * Created by bail on 2017/11/13.
 */
@Repository
public interface StoreCateGoodsRelaRepository extends JpaRepository<StoreCateGoodsRela, StoreCateGoodsRelaPK>, JpaSpecificationExecutor<StoreCateGoodsRela>{

    /**
     * 根据多个店铺分类ID批量更新商品的店铺分类关联表
     * (只有商品只有一个分类时才更新)
     * @param newStoreCateId 新的店铺分类ID
     * @param storeCateIds 多个店铺分类ID
     */
    @Modifying
    @Query(value = "UPDATE store_cate_goods_rela s JOIN (SELECT c.goods_id FROM store_cate_goods_rela c GROUP BY c.goods_id HAVING COUNT(1) = 1 ) x ON s.goods_id = x.goods_id SET s.store_cate_id = ?1 WHERE s.store_cate_id IN ?2", nativeQuery = true)
    void updateGoodsStoreCate(Long newStoreCateId, List<Long> storeCateIds);

    /**
     * 根据店铺分类ID删除与商品关联表
     * (只有商品有多个分类时才删除,防止商品没有任何一个分类)
     * @param storeCateId 店铺分类ID
     */
    @Modifying
    @Query(value = "DELETE s FROM store_cate_goods_rela s JOIN (SELECT c.goods_id FROM store_cate_goods_rela c GROUP BY c.goods_id HAVING COUNT(1) > 1 ) x ON s.goods_id = x.goods_id WHERE s.store_cate_id = ?1", nativeQuery = true)
    void deleteGoodsStoreCate(Long storeCateId);

    /**
     * 根据商品ID批量删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("delete from StoreCateGoodsRela where goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品ID列表批量删除
     * @param goodsIds 商品ID列表
     */
    @Modifying
    @Query("delete from StoreCateGoodsRela where goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return
     */
    @Query("from StoreCateGoodsRela where goodsId in ?1")
    List<StoreCateGoodsRela> selectByGoodsId(List<String> goodsId);

    /**
     * 根据店铺分类idList查询
     * @param storeCateIds 店铺分类idList
     * @return
     */
    @Query("from StoreCateGoodsRela where storeCateId in ?1")
    List<StoreCateGoodsRela> selectByStoreCateIds(List<Long> storeCateIds);
}
