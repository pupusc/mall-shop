package com.wanmi.sbc.goods.bookingsale.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>预售信息DAO</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@Repository
public interface BookingSaleRepository extends JpaRepository<BookingSale, Long>,
        JpaSpecificationExecutor<BookingSale> {

    /**
     * 单个删除预售信息
     *
     * @author dany
     */
    @Modifying
    @Query("update BookingSale set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除预售信息
     *
     * @author dany
     */
    @Modifying
    @Query("update BookingSale set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<BookingSale> findByIdAndStoreIdAndDelFlag(Long id, Long storeId, DeleteFlag delFlag);

    /**
     * 根据skuid列表获取正在进行中的预售活动
     *
     * @author dany
     */
    @Query("from BookingSale a inner join BookingSaleGoods b on a.id = b.bookingSaleId " +
            "and b.goodsInfoId in ?1 and a.startTime <= now() and a.endTime >= now() and a.pauseFlag = 0 and a.delFlag = 0")
    List findByGoodsInfoIdInProcess(List<String> goodsInfoIdList);


    /**
     * 根据skuid列表获取预售中的活动（全款预售起止时间内、定金预售定金支付起止时间内）
     *
     */
    @Query("from BookingSale a inner join BookingSaleGoods b on a.id = b.bookingSaleId " +
            "and b.goodsInfoId in ?1 " +
            "and (a.bookingStartTime <= now() and a.bookingEndTime >= now() " +
                "or (a.handSelStartTime <= now() and a.handSelEndTime >= now()) ) " +
            "and a.pauseFlag = 0 and a.delFlag = 0")
    List findByGoodsInfoIdInBooking(List<String> goodsInfoIdList);


    /**
     * 根据skuid获取正在进行中的预购活动
     *
     * @author dany
     */
    @Query("from BookingSale a inner join BookingSaleGoods b on a.id = b.bookingSaleId " +
            "and b.goodsInfoId = ?1 and a.startTime <= now() and a.endTime >= now() and a.pauseFlag = 0 and a.delFlag = 0")
    Object findByGoodsInfoIdInProcess(String goodsInfoId);

    Optional<BookingSale> findByIdAndDelFlag(Long id, DeleteFlag deleteFlag);


    /**
     * 根据spuid列表获取未结束的预售活动
     *
     * @author zxd
     */
    @Query("from BookingSale a inner join BookingSaleGoods b on a.id = b.bookingSaleId " +
            "and b.goodsId = ?1 and a.endTime >= now() and a.delFlag = 0")
    List getNotEndActivity(String goodsId);
}
