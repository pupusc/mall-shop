package com.wanmi.sbc.order.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购单数据源
 * 支持一个人多个购物车
 * Created by sunkun on 2017/11/27.
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {


    /**
     * 根据多个ID编号\分销员id进行删除
     *
     * @param goodsInfoIds 商品ID
     * @param customerId   客户ID
     */
    @Modifying
    @Query("delete from Purchase p where p.goodsInfoId in ?1 and p.customerId = ?2 and p.inviteeId=?3")
    void deleteByGoodsInfoids(List<String> goodsInfoIds, String customerId, String inviteeId);

    /**
     * 根据客户id\分销员id采购单sku数量
     *
     * @param customerId
     * @return
     */
    Integer countByCustomerIdAndInviteeId(String customerId, String inviteeId);

    /**
     * 根据客户id\分销员id\公司信息id查询采购单sku数量
     * @param customerId
     * @param inviteeId
     * @param companyInfoId
     * @return
     */
    Integer countByCustomerIdAndInviteeIdAndCompanyInfoId(String customerId, String inviteeId,Long companyInfoId);

    /**
     * 根据用户id\分销员id查询采购单
     *
     * @param customerId
     * @return
     */
    List<Purchase> queryPurchaseByCustomerIdAndInviteeId(String customerId, String inviteeId);

    /**
     * 根据sku编号和用户编号\分销员id查询采购单列表
     *
     * @param goodsInfoIds
     * @param customerId
     * @return
     */
    @Query("select p from Purchase p where p.goodsInfoId in (:goodsInfoIds) and p.customerId = :customerId and p.inviteeId=:inviteeId")
    List<Purchase> queryPurchaseByGoodsIdsAndCustomerId(@Param("goodsInfoIds") List<String> goodsInfoIds, @Param
            ("customerId") String customerId, @Param("inviteeId") String inviteeId);

    /**
     * 获取采购单客户商品种类
     *
     * @param cuostomerId
     * @return
     */
    @Query("SELECT sum(p.goodsNum) from Purchase p where p.customerId=?1 and p.inviteeId=?2")
    Long queryGoodsNum(String cuostomerId, String inviteeId);

    /**
     * 获取采购单客户商品种类
     *
     * @param customerId
     * @return
     */
    @Query("SELECT sum(p.goodsNum) from Purchase p where p.customerId=?1 and p.inviteeId=?2 and p.companyInfoId=?3")
    Long queryGoodsNumByCompanyInfoId(String customerId, String inviteeId,Long companyInfoId);

    @Modifying
    @Query("update Purchase set updateTime = :dateTime where customerId = :customerId and goodsInfoId = :goodsInfoId")
    int updateTime(@Param("dateTime") LocalDateTime dateTime, @Param("customerId") String customerId,
                   @Param("goodsInfoId") String goodsInfoId);
}
