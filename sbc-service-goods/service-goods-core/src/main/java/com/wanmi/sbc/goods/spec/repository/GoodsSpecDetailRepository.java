package com.wanmi.sbc.goods.spec.repository;

import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品规格值数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsSpecDetailRepository extends JpaRepository<GoodsSpecDetail, Long>{

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return
     */
    @Query("from GoodsSpecDetail w where w.delFlag = '0' and w.goodsId = ?1")
    List<GoodsSpecDetail> findByGoodsId(String goodsId);

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return
     */
    @Query("from GoodsSpecDetail w where w.goodsId = ?1")
    List<GoodsSpecDetail> findAllByGoodsId(String goodsId);

    /**
     * 根据商品ID查询
     * @param goodsIds 多个商品ID
     * @return
     */
    @Query("from GoodsSpecDetail w where w.delFlag = '0' and w.goodsId in ?1")
    List<GoodsSpecDetail> findByGoodsIds(List<String> goodsIds);


    /**
     * 根据商品ID批量查询删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("update GoodsSpecDetail w set w.delFlag = '1' , updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品ID批量查询删除
     * @param goodsIds 商品ID
     * @return
     */
    @Modifying
    @Query("update GoodsSpecDetail w set w.delFlag = '1' , updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据规格值ID批量删除
     * @param specDetailIds 规格值ID
     */
    @Modifying
    @Query("update GoodsSpecDetail w set w.delFlag = '1' , updateTime = now() where w.specDetailId in ?1")
    void deleteBySpecDetailIds(List<Long> specDetailIds);
}
