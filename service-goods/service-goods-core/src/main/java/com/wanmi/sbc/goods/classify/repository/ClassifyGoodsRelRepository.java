package com.wanmi.sbc.goods.classify.repository;


import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassifyGoodsRelRepository extends JpaRepository<ClassifyGoodsRelDTO, Integer>, JpaSpecificationExecutor<ClassifyGoodsRelDTO> {

    /**
     * 根据商品ID批量删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("delete from ClassifyGoodsRelDTO where goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据时间采集分类关系商品数据
     * @return
     */
    @Query(value = "select * from t_classify_goods_rel where update_time >=?1 and update_time < ?2 and id> ?3  order by id asc limit ?4", nativeQuery = true)
    List<ClassifyGoodsRelDTO> collectClassifySpuIdByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId, Integer pageSize);

    /**
     * 根据spu id采集数据
     * @return
     */
    @Query(value = "select * from t_classify_goods_rel where classify_id in ?1 order by update_time asc", nativeQuery = true)
    List<ClassifyGoodsRelDTO> collectClassifyByClassifyIds(List<Integer> classifyIds);

    /**
     * 根据spu id采集数据
     * @return
     */
    @Query(value = "select * from t_classify_goods_rel where del_flag = 0 and goods_id in ?1 order by update_time asc", nativeQuery = true)
    List<ClassifyGoodsRelDTO> collectClassifyBySpuIds(List<String> goodsIds);
}
