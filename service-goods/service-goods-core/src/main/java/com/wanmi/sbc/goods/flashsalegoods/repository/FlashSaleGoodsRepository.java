package com.wanmi.sbc.goods.flashsalegoods.repository;

import com.wanmi.sbc.goods.flashsalegoods.model.root.FlashSaleGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>抢购商品表DAO</p>
 *
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@Repository
public interface FlashSaleGoodsRepository extends JpaRepository<FlashSaleGoods, Long>,
        JpaSpecificationExecutor<FlashSaleGoods> {

    /**
     * 单个删除抢购商品表
     *
     * @author bob
     */
    @Modifying
    @Query("update FlashSaleGoods set delFlag = 1 where id = ?1")
    int modifyDelFlagById(Long id);

    /**
     * 批量删除抢购商品表
     *
     * @author bob
     */
    @Modifying
    @Query("update FlashSaleGoods set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    @Query("from FlashSaleGoods a where a.delFlag = 0 and a.goodsInfoId in (:ids) and a.activityFullTime >= :startDate " +
            "and a.activityFullTime <= :EndDate")
    List<FlashSaleGoods> queryBySkuIdsAndDate(@Param("ids") List<String> ids, @Param("startDate") LocalDateTime startDate
            , @Param("EndDate") LocalDateTime EndDate);

    /**
     * 批量删除未开始的抢购商品表
     *
     * @author bob
     */
    @Modifying
    @Query("update FlashSaleGoods set delFlag = 1 where activityTime in ?1 and activityFullTime > now()")
    int deleteByTimeList(List<String> activityTimeList);

    /**
     * 商品是否在指定时间内
     *
     * @author bob
     */
    @Query("from FlashSaleGoods where delFlag = 0 and goodsId = :goodsId and activityFullTime <= :begin and " +
            "activityFullTime > :end")
    List<FlashSaleGoods> queryByGoodsIdAndActivityFullTime(@Param("goodsId") String goodsId,
                                                           @Param("begin") LocalDateTime begin,
                                                           @Param("end") LocalDateTime end);

    /**
     * 根据活动时间分组查询
     *
     * @author yxz
     */
    @Query(value = "SELECT s.activity_date,s.activity_time,s.activity_full_time," +
            "count(distinct s.goods_info_id) as goodsNum," +
            "count(distinct s.store_id) as storeNum " +
            "from flash_sale_goods s where if(?1 is NULL,1=1,s.activity_full_time >= ?1) and s.activity_full_time <= ?2 " +
            "and s.del_flag = 0 and if(?3 is null,1=1,s.store_id = ?3) " +
            "group by s.activity_full_time ",
            countQuery = "SELECT count(1) " +
                    "from flash_sale_goods s where if(?1 is NULL,1=1,s.activity_full_time >= ?1) and s.del_flag = 0 " +
                    "and s.activity_full_time <= ?2 and if(?3 is null,1=1,s.store_id = ?3) group by s.activity_full_time",
            nativeQuery = true)
    Page<Object> queryGroupByFullTime(String startTime, String endTime, Long storeId, Pageable pageable);

    /**
     * 根据活动时间分组查询
     *
     * @author yxz
     */
    @Query(value = "SELECT s.activity_date,s.activity_time,s.activity_full_time," +
            "count(distinct s.goods_info_id) as goodsNum," +
            "count(distinct s.store_id) as storeNum " +
            "from flash_sale_goods s  left join goods_info g on s.goods_info_id = g. goods_info_id where if(?1 is " +
            "NULL,1=1,s" +
            ".activity_full_time >= ?1)" +
            " and s" +
            ".activity_full_time <=" +
            " ?2 " +
            "and s.del_flag = 0 and if(?3 is null,1=1,s.store_id = ?3) and g.added_flag = 1 and g.del_flag = 0 and g" +
            ".audit_status = 1 and (g.vendibility = 1 or g.vendibility is null) and (g.provider_status = 1 or g" +
            ".provider_status is null)" +
            "group by s.activity_full_time", nativeQuery = true)
    List<Object> queryGroupByFullTime(String startTime, String endTime, Long storeId);

    /**
     * 根据秒杀抢购商品ID减库存
     *
     * @param stock 库存数
     * @param id    秒杀抢购商品ID
     */
    @Modifying
    @Query("update FlashSaleGoods f set f.stock = f.stock - ?1, f.updateTime = now() where f.id = ?2 and f.stock  >= ?1")
    int subStockById(Integer stock, Long id);

    /**
     * 根据秒杀抢购商品ID加库存
     *
     * @param stock 库存数
     * @param id    秒杀抢购商品ID
     */
    @Modifying
    @Query("update FlashSaleGoods f set f.stock = f.stock + ?1, f.updateTime = now() where f.id = ?2")
    int addStockById(Integer stock, Long id);

    /**
     * @return int
     * @Author lvzhenwei
     * @Description 增加抢购商品销量
     * @Date 9:41 2019/6/22
     * @Param [salesVolume, id]
     **/
    @Modifying
    @Query("update FlashSaleGoods f set f.salesVolume = f.salesVolume + ?1, f.updateTime = now() where f.id = ?2")
    int plusSalesVolumeById(Long salesVolume, Long id);

    /**
     * @return int
     * @Author lvzhenwei
     * @Description 减去抢购商品销量
     * @Date 9:41 2019/6/22
     * @Param [salesVolume, id]
     **/
    @Modifying
    @Query("update FlashSaleGoods f set f.salesVolume = f.salesVolume - ?1, f.updateTime = now() where f.id = ?2")
    int subSalesVolumeById(Long salesVolume, Long id);

    /**
     * @param activityTime
     * @return
     */
    @Query(value = "select activity_time, goods_id " +
            "from flash_sale_goods " +
            "where del_flag = 0 and activity_time = :activityTime and " +
            "date_format(adddate(activity_full_time, interval 2 hour), '%Y-%m-%d %H:%i:%S') > " +
            "date_format(now(), '%Y-%m-%d %H:%i:%S') limit 0, 1", nativeQuery = true)
    Object queryByActivityTime(@Param("activityTime") String activityTime);

    @Query(value = "SELECT COUNT(DISTINCT store_id) FROM flash_sale_goods WHERE del_flag = 0", nativeQuery = true)
    long storeCount();

}
