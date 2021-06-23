package com.wanmi.sbc.goods.standardspec.repository;

import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
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
public interface StandardSpecDetailRepository extends JpaRepository<StandardSpecDetail, Long>{

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return
     */
    @Query("from StandardSpecDetail w where w.delFlag = '0' and w.goodsId = ?1")
    List<StandardSpecDetail> findByGoodsId(String goodsId);

    /**
     * 根据商品ID查询
     * @param goodsIds 多个商品ID
     * @return
     */
    @Query("from StandardSpecDetail w where w.delFlag = '0' and w.goodsId in ?1")
    List<StandardSpecDetail> findByGoodsIds(List<String> goodsIds);


    /**
     * 根据商品ID批量查询删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("update StandardSpecDetail w set w.delFlag = '1' , updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品ID批量查询删除
     * @param goodsIds 商品ID
     * @return
     */
    @Modifying
    @Query("update StandardSpecDetail w set w.delFlag = '1' , updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);
}
