package com.wanmi.sbc.goods.standardspec.repository;

import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SKU规格值关联数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface StandardSkuSpecDetailRelRepository extends JpaRepository<StandardSkuSpecDetailRel, Long>, JpaSpecificationExecutor<StandardSkuSpecDetailRel>{


    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return
     */
    @Query("from StandardSkuSpecDetailRel w where w.delFlag = '0' and w.goodsId = ?1")
    List<StandardSkuSpecDetailRel> findByGoodsId(String goodsId);

    /**
     * 根据spuid 和skuid查询
     * @param goodsId
     * @param goodsInfoId
     * @return
     */
    List<StandardSkuSpecDetailRel> findByGoodsIdAndGoodsInfoId(String goodsId, String goodsInfoId);

    /**
     * 根据多个商品ID查询
     * @param goodsIds 多商品ID
     * @return
     */
    @Query("from StandardSkuSpecDetailRel w where w.delFlag = '0' and w.goodsId in ?1")
    List<StandardSkuSpecDetailRel> findByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品ID查询
     * @param goodsIds 多商品ID
     * @return
     */
    @Query("from StandardSkuSpecDetailRel w where w.goodsId in ?1")
    List<StandardSkuSpecDetailRel> findAllByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个SkuID查询
     * @param goodsInfoIds 多SkuID
     * @return
     */
    @Query("from StandardSkuSpecDetailRel w where w.delFlag = '0' and w.goodsInfoId in ?1")
    List<StandardSkuSpecDetailRel> findByGoodsInfoIds(List<String> goodsInfoIds);

    /**
     * 根据多个SkuID查询
     * @param goodsInfoIds 多SkuID
     * @return
     */
    @Query("from StandardSkuSpecDetailRel w where w.goodsInfoId in ?1")
    List<StandardSkuSpecDetailRel> findByAllGoodsInfoIds(List<String> goodsInfoIds);

    /**
     * 根据商品ID和SKU多个编号删除
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardSkuSpecDetailRel w set w.delFlag = '1' , w.updateTime = now() where w.goodsInfoId in ?1 and w.goodsId = ?2")
    void deleteByGoodsInfoIds(List<String> goodsInfoIds, String goodsId);

    @Modifying
    @Query("update StandardSkuSpecDetailRel w set w.delFlag = '1' , w.updateTime = now() where w.goodsInfoId in ?1")
    void deleteByGoodsInfoIds(List<String> goodsInfoIds);

    /**
     * 根据商品ID和规格值编号更新名称
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardSkuSpecDetailRel w set w.detailName = ?1 , w.updateTime = now() where w.specDetailId = ?2 and w.goodsId = ?3")
    void updateNameBySpecDetail(String detailName, Long specDetailId, String goodsId);

    /**
     * 根据商品ID批量查询删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("update StandardSkuSpecDetailRel w set w.delFlag = '1' , updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据商品ID批量查询删除
     * @param goodsIds 商品ID
     * @return
     */
    @Modifying
    @Query("update StandardSkuSpecDetailRel w set w.delFlag = '1' , updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);
}
