package com.wanmi.sbc.goods.standard.repository;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品库与商品库数据源
 *
 * @auther daiyitian
 * @create 2018/03/20 10:04
 */
@Repository
public interface StandardGoodsRelRepository extends JpaRepository<StandardGoodsRel, Long> {

    /**
     * 根据多个商品ID查询
     *
     * @param goodsIds 商品ID
     * @return
     */
    @Query("from StandardGoodsRel w where w.goodsId in ?1 and w.delFlag = '0'")
    List<StandardGoodsRel> findByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品ID统计
     *
     * @param goodsIds 商品ID
     * @return
     */
    @Query("select count(0) from StandardGoodsRel w where w.goodsId in ?1 and w.delFlag = '0'")
    Long countByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品ID查询
     *
     * @param standardIds 商品库ID
     * @return
     */
    @Query("from StandardGoodsRel w where w.standardId in ?1 and  w.storeId in ?2 and w.delFlag = '0'")
    List<StandardGoodsRel> findByStandardIds(List<String> standardIds, List<Long> storeIds);

    /**
     * 根据standardIds查询
     *
     * @param standardIds 商品库ID
     * @return
     */
    @Query("from StandardGoodsRel w where w.standardId in ?1 and w.delFlag = '0'")
    List<StandardGoodsRel> findByStandardIds(List<String> standardIds);

    /**
     * 根据多个商品库ID进行删除统计
     *
     * @param standardIds 商品库ID
     * @return
     */
    @Query("select count(0) from StandardGoodsRel w where w.standardId in ?1 and w.delFlag = '0'")
    Long countByStandardIds(List<String> standardIds);

    /**
     * 根据多个商品库ID和店铺ID进行删除统计
     *
     * @param standardIds 商品库ID
     * @param storeIds 店铺ID
     * @return
     */
    @Query("select count(0) from StandardGoodsRel w where w.standardId in ?1 and  w.storeId in ?2 and w.delFlag = '0'")
    Long countByStandardAndStoreIds(List<String> standardIds, List<Long> storeIds);

    /**
     * 根据多个商品ID进行删除
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update StandardGoodsRel w set w.delFlag = '1' where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品库ID进行删除
     *
     * @param standardIds 商品库ID
     */
    @Modifying
    @Query("update StandardGoodsRel w set w.delFlag = '1' where w.standardId in ?1")
    void deleteByStandardIds(List<String> standardIds);

    @Modifying
    @Query("update StandardGoodsRel w set w.delFlag = '0' where w.standardId = ?1")
    void updateDelFlag(String standardId);

    /**
     * 根据商品id查询关系
     * @param goodsId
     * @return
     */
    @Query("from StandardGoodsRel w where w.goodsId = ?1 and w.delFlag = '0'")
    StandardGoodsRel findByGoodsId(String goodsId);

    /**
     * 根据删除状态查找
     * @param goodsId
     * @param delFlag
     * @return
     */
    StandardGoodsRel findByGoodsIdAndDelFlag(String goodsId, DeleteFlag delFlag);

    /**
     * 根据商品id查找(删除的和未删除的)
     * @param goodsId
     * @return
     */
    StandardGoodsRel findALLByGoodsId(String goodsId);

    /**
     * 查找已被删除导入的关系
     * @param deleteFlag
     * @param goodsIds
     * @return
     */
    List<StandardGoodsRel> findByDelFlagAndGoodsIdIn(DeleteFlag deleteFlag, List<String> goodsIds);

    /**
     * 根据商品库id查找
     * @param standardIds
     * @return
     */
    List<StandardGoodsRel> findByStandardId(String standardIds);

    /**
     * 根据多个商品ID查询
     *
     * @param standardIds 商品库ID
     * @return
     */
    @Query("from StandardGoodsRel w where w.standardId in ?1 and  w.storeId in ?2 and w.needSynchronize = ?3 and w.delFlag = '0'")
    List<StandardGoodsRel> findByNeedSynStandardIds(List<String> standardIds, List<Long> storeIds, BoolFlag boolFlag);

    /**
     * 店铺ID查询
     *
     * @param storeId 店铺ID
     * @return
     */
    @Query("from StandardGoodsRel w where w.storeId = ?1 and w.needSynchronize = 1 and w.delFlag = '0'")
    List<StandardGoodsRel> findByNeedSynByStoreId(Long storeId);


    StandardGoodsRel findByStandardIdAndStoreId(String standardgoodsId, Long storeId);
    /**
     * 根据店铺id查询所有的商品库
     *
     * @param storeIds 商家id
     * @return
     */
    @Query("from StandardGoodsRel w where  w.storeId in ?1  and w.delFlag = '0'")
    List<StandardGoodsRel> findByStoreIds(List<Long> storeIds);

    @Modifying
    @Query("update StandardGoodsRel set delFlag=1 where storeId=?1 and goodsId=?2")
    void delByStoreIdAndGoodsId(Long storeId,String goodsId);
}
