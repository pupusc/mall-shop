package com.wanmi.sbc.goods.bookingsalegoods.repository;

import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>预售商品信息DAO</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@Repository
public interface BookingSaleGoodsRepository extends JpaRepository<BookingSaleGoods, Long>,
        JpaSpecificationExecutor<BookingSaleGoods> {

    List<BookingSaleGoods> findByBookingSaleIdAndStoreId(Long id, Long storeId);

    Optional<BookingSaleGoods> findByIdAndStoreId(Long id, Long storeId);

    void deleteByIdIn(List<Long> idList);

    @Modifying
    @Query("update BookingSaleGoods a set a.canBookingCount = a.canBookingCount - :stock, updateTime = now() where a.bookingSaleId = :bookingSaleId and a.goodsInfoId = :goodsInfoId")
    int subCanBookingCount(@Param("bookingSaleId") Long bookingSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("stock") Integer stock);

    @Modifying
    @Query("update BookingSaleGoods a set a.canBookingCount = a.canBookingCount + :stock, updateTime = now() where a.bookingSaleId = :bookingSaleId and a.goodsInfoId = :goodsInfoId")
    int addCanBookingCount(@Param("bookingSaleId") Long bookingSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("stock") Integer stock);


    @Query("from BookingSale a inner join BookingSaleGoods b on a.id = b.bookingSaleId " +
            "and b.goodsInfoId in :goodsInfoIds and ((a.startTime <= :startTime and a.endTime >= :startTime) or " +
            " (a.startTime <= :endTime and a.endTime >= :endTime)) " +
            " and a.delFlag = 0")
    List existBookingActivity(@Param("goodsInfoIds") List<String> goodsInfoIds,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    @Modifying
    @Query("update BookingSaleGoods a set a.payCount = a.payCount + :count, updateTime = now() where a.bookingSaleId = :bookingSaleId and a.goodsInfoId = :goodsInfoId")
    int addBookingPayCount(@Param("bookingSaleId") Long bookingSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("count") Integer count);

    @Modifying
    @Query("update BookingSaleGoods a set a.handSelCount = a.handSelCount + :count, updateTime = now() where a.bookingSaleId = :bookingSaleId and a.goodsInfoId = :goodsInfoId")
    int addBookinghandSelCount(@Param("bookingSaleId") Long bookingSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("count") Integer count);

    @Modifying
    @Query("update BookingSaleGoods a set a.tailCount = a.tailCount + :count, updateTime = now() where a.bookingSaleId = :bookingSaleId and a.goodsInfoId = :goodsInfoId")
    int addBookingTailCount(@Param("bookingSaleId") Long bookingSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("count") Integer count);
}
