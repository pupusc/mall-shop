package com.wanmi.sbc.goods.images;

import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品图片数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsImageRepository extends JpaRepository<GoodsImage, Long>, JpaSpecificationExecutor<GoodsImage> {

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return 商品图片信息
     */
    @Query("from GoodsImage w where w.delFlag = '0' and w.goodsId = ?1 order by w.sort asc")
    List<GoodsImage> findByGoodsId(String goodsId);

    /**
     * 根据商品ID批量查询
     * @param goodsIds 商品ID
     * @return 商品图片信息
     */
    @Query("from GoodsImage w where w.delFlag = '0' and w.goodsId in ?1")
    List<GoodsImage> findByGoodsIds(List<String> goodsIds);


    /**
     * 根据商品ID批量查询删除
     * @param goodsId 商品ID
     * @return
     */
    @Modifying
    @Query("update GoodsImage w set w.delFlag = '1' , updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据采集spu图片列表信息
     * @return
     */
    @Query(value = "select * from goods_image where update_time >=?1 and update_time < ?2 and image_id > ?3 order by image_id asc limit ?4", nativeQuery = true)
    List<GoodsImage> collectSpuIdImageByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId, Integer pageSize);

}
