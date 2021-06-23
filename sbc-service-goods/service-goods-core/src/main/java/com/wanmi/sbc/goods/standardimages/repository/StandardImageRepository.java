package com.wanmi.sbc.goods.standardimages.repository;

import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品库图片数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface StandardImageRepository extends JpaRepository<StandardImage, Long>{

    /**
     * 根据商品ID查询
     * @param goodsId 商品ID
     * @return 商品图片信息
     */
    @Query("from StandardImage w where w.delFlag = '0' and w.goodsId = ?1")
    List<StandardImage> findByGoodsId(String goodsId);

    /**
     * 根据商品ID批量查询
     * @param goodsIds 商品ID
     * @return 商品图片信息
     */
    @Query("from StandardImage w where w.delFlag = '0' and w.goodsId in ?1")
    List<StandardImage> findByGoodsIds(List<String> goodsIds);


    /**
     * 根据商品ID编号进行删除
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardImage w set w.delFlag = '1' ,w.updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

}
