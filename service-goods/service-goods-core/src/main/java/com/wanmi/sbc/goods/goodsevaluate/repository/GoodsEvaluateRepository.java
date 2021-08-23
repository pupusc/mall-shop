package com.wanmi.sbc.goods.goodsevaluate.repository;

import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluatePageRequest;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>商品评价DAO</p>
 *
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@Repository
public interface GoodsEvaluateRepository extends JpaRepository<GoodsEvaluate, String>,
        JpaSpecificationExecutor<GoodsEvaluate> {

    /**
     * @param goodsId spuID
     * @Description: 商品好评率（根据spu查询）
     * @Author: Bob
     * @Date: 2019-04-09 15:44
     */
    @Query(value = "SELECT convert(b.counts/a.counts*100,decimal(15,0)) as praise FROM" +
            " (SELECT count(*) as counts FROM goods_evaluate t WHERE t.goods_id = :goodsId  and t.del_flag = 0) as a," +
            "(SELECT count(*) as counts FROM goods_evaluate t WHERE t.evaluate_score >= 4 and t.goods_id = :goodsId  " +
            "and t.del_flag = 0) as b",
            nativeQuery = true)
    String queryPraise(@Param("goodsId") String goodsId);

    /**
     * @Author lvzhenwei
     * @Description 更新商品评价点赞数
     * @Date 17:10 2019/5/7
     * @Param [evaluateId]
     * @return void
     **/
    @Modifying
    @Query("update GoodsEvaluate set goodNum = goodNum + 1 where evaluateId = :evaluateId")
    void updateGoodsEvaluateGoodNum(@Param("evaluateId") String evaluateId);

    @Query("from GoodsEvaluate where goodsId = :#{#req.goodsId} and delFlag = :#{#req.delFlag} and isShow = " +
            ":#{#req.isShow}")
    List<GoodsEvaluate> queryTopData(@Param("req")GoodsEvaluatePageRequest req, Pageable pageable);

    @Modifying
    @Query("update GoodsEvaluate set goodNum = goodNum - 1 where evaluateId = :evaluateId")
    void subGoodsEvaluateGoodNum(@Param("evaluateId") String evaluateId);

    @EntityGraph(value = "goodsEvaluate.all",type= EntityGraph.EntityGraphType.FETCH)
    Page<GoodsEvaluate> findAll(@Nullable Specification<GoodsEvaluate> var1, Pageable var2);

    @Query("select goodsId, count(1) from GoodsEvaluate where goodsId in (:goodsIds) and delFlag = 0 and isShow = 1 group by goodsId")
    List<Object> countByGoodsIdsGroupByAndGoodsId(List<String> goodsIds);

    @Query("select goodsId, count(1) from GoodsEvaluate where goodsId in (:goodsIds) and delFlag = 0 and isShow = 1 and evaluateScore in(4,5) group by goodsId")
    List<Object> countFavorteByGoodsIdsGroupByAndGoodsId(List<String> goodsIds);

}