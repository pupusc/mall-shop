package com.wanmi.sbc.goods.standard.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品库SPU数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface StandardGoodsRepository extends JpaRepository<StandardGoods, String>, JpaSpecificationExecutor<StandardGoods>{

    /**
     * 根据多个商品ID编号进行删除
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update StandardGoods w set w.delFlag = '1', w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据商品ID编号进行删除
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardGoods w set w.delFlag = '1', w.updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);



    /**
     * 根据多个分类ID编号更新分类
     * @param newCateId 分类ID
     * @param cateIds 多个分类ID
     */
    @Modifying
    @Query("update StandardGoods w set w.cateId = ?1, w.updateTime = now() where w.cateId in ?2")
    void updateCateByCateIds(Long newCateId, List<Long> cateIds);

    /**
     * 根据品牌id 批量把spu品牌置为null
     * @param brandId
     */
    @Modifying
    @Query("update StandardGoods g set g.brandId = null where g.brandId = :brandId")
    void updateBrandByBrandId(@Param("brandId") Long brandId);


    /**
     * 根据类别id查询SPU
     * @param cateId
     * @return
     */
    @Query
    List<StandardGoods> findAllByCateId(Long cateId);

    /**
     * 商品库id 查找
     * @param standardGoodsIds
     * @return
     */
    @Query
    List<StandardGoods> findByGoodsIdIn(List<String> standardGoodsIds);

    @Modifying
    @Query("update StandardGoods g set g.delFlag = '0' , g.updateTime = now() where g.delFlag = '1' and g.goodsId = ?1 ")
    void updateDelFlag(String standardId);

    @Modifying
    @Query("update StandardGoods g set g.addedFlag = ?2 ,g.goodsNo = ?3, g.updateTime = now() where g.goodsId = ?1 ")
    void updateAddedFlagAndGoodsNo(String standardId ,Integer addedFlag ,String goodsNo);

    @Modifying
    @Query("update StandardGoods g set g.addedFlag = ?2 , g.updateTime = now() where g.goodsId = ?1 ")
    void updateAddedFlag(String standardId ,Integer addedFlag );

    @Modifying
    @Query("update StandardGoods set addedFlag = ?1 , updateTime = now() where goodsSource=?2 and thirdPlatformSpuId=?3 ")
    void updateAddedFlagByGoodsSourceAndThirdPlatformSpuId(Integer addedFlag,Integer goodsSource,String thirdPlatformSpuId );

    @Modifying
    @Query("update StandardGoods g set g.delFlag = '1' , g.deleteReason=?2 , g.updateTime = now() where g.delFlag = '0' and g.goodsId in ?1 ")
    void updateDelFlagAddReason(List<String> goodsIds,String reason);


    @Query("from StandardGoods g where g.delFlag = '0' and g.goodsId in ?1")
    List<StandardGoods> findByGoodsIds(List<String> goodsIds);

    /**
     * 单个查找
     * @param goodsId
     * @param no
     * @return
     */
    StandardGoods findByGoodsIdAndDelFlag(String goodsId, DeleteFlag no);

    @Modifying
    @Query(value = "UPDATE standard_goods SET cate_id=:cateId WHERE goods_source=:source AND third_cate_id=:thirdCateId",nativeQuery = true)
    void updateThirdCateMap(@Param("source") int source, @Param("thirdCateId") long thirdCateId, @Param("cateId") long cateId);

    StandardGoods findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag deleteFlag, Integer goodsSource,String thirdPlatformSpuId);

    /**
     * 查询所有三方渠道商品
     * @param deleteFlag
     * @param goodsSource
     * @return
     */
    List<StandardGoods> findByDelFlagAndGoodsSource(DeleteFlag deleteFlag, Integer goodsSource);

    @Modifying
    @Query("update StandardGoods set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2")
    void delByGoodsSourceAndThirdPlatformSpuId(Integer goodsSource,String thirdPlatformSpuId);

}
