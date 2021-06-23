package com.wanmi.sbc.goods.livegoods.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.livegoods.model.root.LiveGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>直播商品DAO</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@Repository
public interface LiveGoodsRepository extends JpaRepository<LiveGoods, Long>,
        JpaSpecificationExecutor<LiveGoods> {



    /**
     * 单个删除直播商品
     * @author zwb
     */
    @Modifying
    @Query("update LiveGoods set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除直播商品
     * @author zwb
     */
    @Modifying
    @Query("update LiveGoods set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> goodsIdList);


    Optional<LiveGoods> findByGoodsIdAndDelFlag(Long id,DeleteFlag delFlag);

    Optional<LiveGoods> findByGoodsIdAndStoreIdAndDelFlag(Long id, Long storeId, DeleteFlag delFlag);


    /**
     * 单个修改直播商品
     * @author zwb
     */
    @Modifying
    @Query("update LiveGoods set auditStatus = ?1 where goodsId in ?2")
    int updateByGoodsIdList(Integer auditStatus ,List<Long> goodsIdList);

    /**
     * 单个修改直播商品goodsId
     * @author zwb
     */
    @Modifying
    @Query("update LiveGoods set goodsId = ?1,auditStatus=?2 where id = ?3")
    int updateGoodsIdAndAuditStatusById(Long goodsIdNew,Integer auditStatus, Long id);

    Optional<LiveGoods> findByGoodsId(Long goodsId);

    /**
     * 单个修改直播商品goodsId
     * @author zwb
     */
    @Query("SELECT  l from LiveGoods l where l.goodsId in ?1 and l.delFlag= ?2")
    List<LiveGoods> findByGoodsIdList(List<Long> goodsIdList, DeleteFlag no);
    /**
     * 修改状态
     * @author zwb
     */
    @Modifying
    @Query("update LiveGoods set auditStatus = ?1,auditReason=?2 where id = ?3")
    void updateAuditStatusById(Integer auditStatus, String auditReason, Long id);
}
