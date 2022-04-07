package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @auther ruilinxin
 * @create 2018/03/20 10:04
 */
@Repository
public interface GoodsPropDetailRelRepository extends JpaRepository<GoodsPropDetailRel, Long>, JpaSpecificationExecutor<GoodsPropDetailRel> {

    @Modifying
    @Query(value = "delete from goods_prop_detail_rel where goods_id=:goodsId", nativeQuery = true)
    void deletePropsForGoods(String goodsId);

    @Query(value = " from GoodsPropDetailRel a where a.goodsId= ?1 and delFlag = 0")
    List<GoodsPropDetailRel> queryByGoodsId(String goodsId);

    @Query(value = "select r.* from goods_prop as gp join goods_prop_detail_rel as r on r.prop_id=gp.prop_id join goods_info as g on r.goods_id=g.goods_id where gp.prop_name='定价' and g.goods_info_id=?1", nativeQuery = true)
    GoodsPropDetailRel findPriceByGoodsId(String goodsInfoId);

    @Query
    List<GoodsPropDetailRel> findAllByPropId(Long propId);

    @Query
    List<GoodsPropDetailRel> findAllByDetailId(Long detailId);

    @Modifying
    @Query("update GoodsPropDetailRel w set w.detailId = ?1 ,w.updateTime = now() where w.goodsId = ?2 and w.propId = ?3 and w.delFlag = 0")
    int updateByGoodsIdAndPropId(Long detailId, String goodsId, Long propId);

    /**
     * 根据多个SpuID查询
     *
     * @param goodsIds 多SpuID
     * @return
     */
    @Query(" from GoodsPropDetailRel w where w.delFlag = 0 and w.goodsId in ?1")
    List<GoodsPropDetailRel> findByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个SpuID查询
     *
     * @param goodsIds 多SpuID
     * @return
     */
    @Query(value =
            "SELECT t1.rel_id AS relId, t1.prop_id AS propId, t1.goods_id AS goodsid, t1.prop_value AS propvalue, t2.prop_name AS propName, t2.prop_type AS propType " +
            "FROM goods_prop_detail_rel t1 LEFT JOIN goods_prop t2 ON t2.del_flag=0 AND t2.prop_id = t1.prop_id " +
            "WHERE t1.del_flag=0 AND t1.goods_id IN (?1)", nativeQuery = true)
    List<Map<String, Object>> selectByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品ID编号进行删除
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update GoodsPropDetailRel w set w.delFlag = 1 ,w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个SpuID查询
     * @param goodsIds
     * @return
     */
//    @Query(value = "SELECT p.prop_id, IFNULL(r.detail_id, 0) AS detail_id, g.goods_id FROM goods_prop p JOIN goods g ON p.cate_id = g.cate_id LEFT JOIN goods_prop_detail_rel r ON p.prop_id = r.prop_id AND g.goods_id = r.goods_id WHERE g.goods_id in ?1", nativeQuery = true)
    @Query(value = "SELECT p.prop_id, IFNULL(r.detail_id, 0) AS detail_id, g.goods_id, r.prop_value FROM goods_prop p join t_goods_prop_cate_rel gcr on gcr.prop_id = p.prop_id JOIN goods g ON g.cate_id = gcr.cate_id LEFT JOIN goods_prop_detail_rel r ON p.prop_id = r.prop_id AND g.goods_id = r.goods_id WHERE p.del_flag=0 and g.goods_id in ?1", nativeQuery = true)
    List<Object> findRefByGoodIds(List<String> goodsIds);

    /**
     * 根据商品id和isbn查询属性
     * @param goodsId
     * @return
     */
    @Query(value = "select * from goods_prop_detail_rel where prop_id=?1 and goods_id=?2", nativeQuery = true)
    GoodsPropDetailRel findByGoodsIdAndIsbn(Long propId, String goodsId);

    /**
     * 根据商品ID编号进行删除
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update GoodsPropDetailRel w set w.delFlag = 1 ,w.updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);
}
