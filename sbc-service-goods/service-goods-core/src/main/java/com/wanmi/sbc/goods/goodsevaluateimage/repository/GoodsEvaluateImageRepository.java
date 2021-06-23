package com.wanmi.sbc.goods.goodsevaluateimage.repository;

import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>商品评价图片DAO</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Repository
public interface GoodsEvaluateImageRepository extends JpaRepository<GoodsEvaluateImage, String>,
        JpaSpecificationExecutor<GoodsEvaluateImage> {
    /**
     * 根据评论id 删除图片
     * @param
     */
    @Modifying
    @Query("delete from GoodsEvaluateImage g where g.evaluateId = ?1")
    void deleteByEvaluateId(String evaluateId);

    @Modifying
    @Query("update GoodsEvaluateImage g set g.isShow = :isShow where g.evaluateId = :evaluateId")
    int updateIsShowByGoodsId(@Param("isShow") int isShow, @Param("evaluateId") String evaluateId);
	
}
