package com.wanmi.sbc.goods.groupongoodsinfo.repository;

import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.groupongoodsinfo.model.root.GrouponGoodsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>拼团活动商品信息表DAO</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@Repository
public interface GrouponGoodsInfoRepository extends JpaRepository<GrouponGoodsInfo, String>,
        JpaSpecificationExecutor<GrouponGoodsInfo> {

	/**
	 * 根据拼团活动ID、SPU编号查询拼团价格最小的拼团SKU信息
	 * @param grouponActivityId
	 * @param goodsId
	 * @return
	 */
	GrouponGoodsInfo findTop1ByGrouponActivityIdAndGoodsIdOrderByGrouponPriceAsc(String grouponActivityId, String goodsId);

	/**
	 *  根据商品ids查询包含的活动ids
	 *
	 * @param spuIds 商品ids
	 * @return
	 */
	@Query("select distinct g.grouponActivityId from GrouponGoodsInfo g where g.goodsId in ?1")
	List<String> listActivityIdsBySpuIds(List<String> spuIds);


	@Modifying
	@Query
	Integer deleteByGrouponActivityId(String grouponActivityId);

	/**
	 * 根据活动ID、SKU编号更新已成团人数
	 * @param grouponActivityId
	 * @param goodsInfoId
	 * @param alreadyGrouponNum
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query("update GrouponGoodsInfo g set g.alreadyGrouponNum = g.alreadyGrouponNum + :alreadyGrouponNum," +
			"g.updateTime = :updateTime where g.grouponActivityId = :grouponActivityId and g.goodsInfoId = :goodsInfoId")
	int updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(@Param("grouponActivityId") String grouponActivityId,
																 @Param("goodsInfoId") String goodsInfoId,
												   @Param("alreadyGrouponNum") Integer alreadyGrouponNum,
												   @Param("updateTime") LocalDateTime updateTime);

	/**
	 *  根据活动ID、SKU编号更新商品销售量、订单量、交易额
	 * @param grouponActivityId
	 * @param goodsInfoId
	 * @param goodsSalesNum
	 * @param orderSalesNum
	 * @param tradeAmount
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query("update GrouponGoodsInfo g set g.goodsSalesNum = g.goodsSalesNum + :goodsSalesNum," +
			"g.orderSalesNum = g.orderSalesNum + :orderSalesNum,g.tradeAmount = g.tradeAmount + :tradeAmount," +
			"g.updateTime = :updateTime  where g.grouponActivityId = :grouponActivityId and g.goodsInfoId = :goodsInfoId")
	int updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(@Param("grouponActivityId") String grouponActivityId,
																 @Param("goodsInfoId") String goodsInfoId,
																 @Param("goodsSalesNum") Integer goodsSalesNum,
																 @Param("orderSalesNum") Integer orderSalesNum,
																 @Param("tradeAmount") BigDecimal tradeAmount,
																 @Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据活动ID、SKU编号更新成团后退单数量、成团后退单金额
	 * @param grouponActivityId
	 * @param goodsInfoId
	 * @param refundNum
	 * @param refundAmount
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query("update GrouponGoodsInfo g set g.refundNum = g.refundNum + :refundNum," +
			"g.refundAmount = g.refundAmount + :refundAmount," +
			"g.updateTime = :updateTime  where g.grouponActivityId = :grouponActivityId and g.goodsInfoId = :goodsInfoId")
	int updateReturnOrderStatisticNumByGrouponActivityIdAndGoodsInfoId(@Param("grouponActivityId") String grouponActivityId,
															@Param("goodsInfoId") String goodsInfoId,
															@Param("refundNum") Integer refundNum,
															@Param("refundAmount") BigDecimal refundAmount,
															@Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据活动ID批量更新审核状态
	 * @param grouponActivityIds
	 * @param auditStatus
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query("update GrouponGoodsInfo g set g.auditStatus = :auditStatus,g.updateTime = :updateTime where g.grouponActivityId in :grouponActivityIds")
	int updateAuditStatusByGrouponActivityIds(@Param("grouponActivityIds") List<String> grouponActivityIds,
																 @Param("auditStatus") AuditStatus auditStatus,
																 @Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据活动ID批量更新是否精选
	 * @param grouponActivityIds
	 * @param sticky
	 * @param updateTime
	 * @return
	 */
	@Modifying
	@Query("update GrouponGoodsInfo g set g.sticky = :sticky,g.updateTime = :updateTime where g.grouponActivityId in :grouponActivityIds")
	int updateStickyByGrouponActivityIds(@Param("grouponActivityIds") List<String> grouponActivityIds,
											  @Param("sticky") Boolean sticky,
											  @Param("updateTime") LocalDateTime updateTime);

	/**
	 * 根据活动ID、SKU编号查询拼团商品信息
	 * @param grouponActivityId
	 * @param goodsInfoId
	 * @return
	 */
	GrouponGoodsInfo findByGrouponActivityIdAndGoodsInfoId(String grouponActivityId, String goodsInfoId);

	/**
	 * 根据商品ids，查询正在进行的活动(时间段)
	 *
	 * @param goodsInfoIds  商品ids
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	@Query("from GrouponGoodsInfo g where g.goodsInfoId in ?1 and not (g.startTime > ?3 or g.endTime < ?2)")
	List<GrouponGoodsInfo> listActivitying(List<String> goodsInfoIds, LocalDateTime startTime, LocalDateTime endTime);

}
