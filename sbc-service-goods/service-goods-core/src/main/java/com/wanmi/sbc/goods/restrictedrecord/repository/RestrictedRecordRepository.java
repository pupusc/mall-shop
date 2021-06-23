package com.wanmi.sbc.goods.restrictedrecord.repository;

import com.wanmi.sbc.goods.restrictedrecord.model.root.RestrictedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>限售DAO</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@Repository
public interface RestrictedRecordRepository extends JpaRepository<RestrictedRecord, Long>,
        JpaSpecificationExecutor<RestrictedRecord> {


    /**
     * 根据会员的Id和goodsInfoId查询某个商品针对这个会员的限售记录
     * @param customerId
     * @param goodsInfoId
     * @return
     */
    @Query("from RestrictedRecord r where r.customerId = ?1 and r.goodsInfoId = ?2")
    RestrictedRecord findByCustomerIdAndGoodsInfoId(String customerId, String goodsInfoId);

    /**
     * 根据会员的Id和goodsInfoIds批量查询某个商品针对这个会员的限售记录
     * @param customerId
     * @return
     */
    @Query("from RestrictedRecord r where r.customerId = ?1 and r.goodsInfoId in ?2")
    List<RestrictedRecord> findByCustomerIdAndGoodsInfoIds(String customerId, List<String> goodsInfoIds);

    /**
     * 增加某个商品对某个会员的购买数量 —— 下单时调用
     * @param customerId
     * @param goodsInfoId
     * @param num
     * @return
     */
    @Modifying
    @Query("update RestrictedRecord r set r.purchaseNum = r.purchaseNum + ?3 where r.goodsInfoId = ?2 and r.customerId = ?1")
    int plusPurchaseNum(String customerId, String goodsInfoId, Long num);

    /**
     * 减少某个商品对某个会员的购买数量 —— 退单时调用
     * @param customerId
     * @param goodsInfoId
     * @param num
     * @return
     */
    @Modifying
    @Query("update RestrictedRecord r set r.purchaseNum = r.purchaseNum - ?3 where r.goodsInfoId = ?2 and r.customerId = ?1")
    int reducePurchaseNum(String customerId, String goodsInfoId, Long num);

    /**
     * 批量删除商品的限售记录 —— 修改商品的限售配置时调用
     * @param goodsInfoIds
     * @return
     */
    @Modifying
    @Query("delete from RestrictedRecord r where r.goodsInfoId in ?1")
    int deleteAllByGoodsInfoIds(List<String> goodsInfoIds);



}
