package com.wanmi.sbc.goods.standard.repository;

import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @auther ruilinxin
 * @create 2018/03/20 10:04
 */
@Repository
public interface StandardPropDetailRelRepository extends JpaRepository<StandardPropDetailRel, Long>, JpaSpecificationExecutor<StandardPropDetailRel> {

    @Query(value = " from StandardPropDetailRel a where a.goodsId= ?1 and delFlag = 0 ")
    List<StandardPropDetailRel> queryByGoodsId(String goodsId);

    @Query
    List<StandardPropDetailRel> findAllByPropId(Long propId);

    @Query
    List<StandardPropDetailRel> findAllByDetailId(Long detailId);

    @Modifying
    @Query("update StandardPropDetailRel w set w.detailId = ?1 ,w.updateTime = now() where w.goodsId = ?2 and w.propId = ?3 and w.delFlag = 0")
    int updateByGoodsIdAndPropId(Long detailId, String goodsId, Long propId);

    /**
     * 根据多个SpuID查询
     *
     * @param goodsIds 多SpuID
     * @return
     */
    @Query(" from StandardPropDetailRel w where w.delFlag = 0 and w.goodsId in ?1")
    List<StandardPropDetailRel> findByGoodsIds(List<String> goodsIds);

    /**
     * 根据多个商品ID编号进行删除
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update StandardPropDetailRel w set w.delFlag = 1 ,w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据商品ID编号进行删除
     *
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardPropDetailRel w set w.delFlag = 1 ,w.updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);
}
