package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.info.model.entity.GoodsInfoLiveGoods;
import com.wanmi.sbc.goods.info.model.entity.GoodsInfoParams;
import com.wanmi.sbc.goods.info.model.entity.GoodsMarketingPrice;
import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SKU数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsInfoRepository extends JpaRepository<GoodsInfo, String>, JpaSpecificationExecutor<GoodsInfo> {

    /**
     * 根据spuIdList查询sku
     *
     * @param goodsIdList
     */
    List<GoodsInfo> findByGoodsIdIn(List<String> goodsIdList);

    Optional<GoodsInfo> findByGoodsInfoIdAndStoreIdAndDelFlag(String goodsInfoId, Long storeId, DeleteFlag deleteFlag);

    /**
     * 根据spuIdList查询sku(不包含已删除的)
     *
     * @param goodsIdList
     */
    @Query("from GoodsInfo w where w.delFlag = '0' and w.goodsId in ?1")
    List<GoodsInfo> findByGoodsIds(List<String> goodsIdList);

    /**
     * 根据skuIdList查询sku(包含已删除的)
     *
     * @param goodsInfoIdList
     */
    @Query("from GoodsInfo w where w.goodsInfoId in ?1")
    List<GoodsInfo> findByGoodsInfoIds(List<String> goodsInfoIdList);

    /**
     * 根据多个商品ID编号进行删除
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.delFlag = '1' ,w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据商品ID编号进行删除
     *
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.delFlag = '1' ,w.updateTime = now() where w.goodsId = ?1")
    void updateDeleteFlagByGoodsId(String goodsId);

    /**
     * 根据多个商品ID编号进行删除
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.delFlag = '1' ,w.updateTime = now() where w.providerGoodsInfoId in ?1")
    void deleteByProviderGoodsInfoId(List<String> goodsIds);

    /**
     * 根据多个商品skuId进行删除
     *
     * @param goodsInfoIds 商品skuId列表
     */
    @Modifying
    @Query("update GoodsInfo w set w.delFlag = '1' ,w.updateTime = now() where w.goodsInfoId in ?1")
    void deleteByGoodsInfoIds(List<String> goodsInfoIds);

    /**
     * 根据多个商品ID编号更新上下架状态
     *
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.addedFlag = ?1, w.updateTime = now(), w.addedTime = now() where w.goodsId in ?2")
    void updateAddedFlagByGoodsIds(Integer addedFlag, List<String> goodsIds);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.addedTimingFlag = ?1, w.updateTime = now() where w.goodsId in ?2")
    void updateAddedTimingFlagByGoodsIds(Boolean addedTimingFlag, List<String> goodsIds);

    /**
     * 根据多个商品skuId更新上下架状态
     *
     * @param addedFlag    上下架状态
     * @param goodsInfoIds 商品skuId列表
     */
    @Modifying
    @Query("update GoodsInfo w set w.addedFlag = ?1, w.updateTime = now(), w.addedTime = now() where w.goodsInfoId in ?2")
    void updateAddedFlagByGoodsInfoIds(Integer addedFlag, List<String> goodsInfoIds);

    /**
     * 根据商品SKU编号加库存
     *
     * @param stock       库存数
     * @param goodsInfoId 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.stock = w.stock + ?1, w.updateTime = now() where w.goodsInfoId = ?2")
    int addStockById(Long stock, String goodsInfoId);

    /**
     * 根据商品SKU编号减库存
     *
     * @param stock       库存数
     * @param goodsInfoId 商品ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.stock = w.stock - ?1, w.updateTime = now() where w.goodsInfoId = ?2 and w.stock  >= ?1")
    int subStockById(Long stock, String goodsInfoId);


    /**
     * 根据商品SKU编号加库存
     *
     * @param stock       库存数
     * @param goodsInfoId 商品ID
     */
    @Transactional
    @Modifying
    @Query("update GoodsInfo w set w.stock = ?1 where w.goodsInfoId = ?2")
    int resetStockById(Long stock, String goodsInfoId);

    /**
     * 根据多个Sku编号更新审核状态
     *
     * @param auditStatus 审核状态
     * @param goodsIds    多个商品
     */
    @Modifying
    @Query("update GoodsInfo w set w.auditStatus = ?1  where w.goodsId in ?2")
    void updateAuditDetail(CheckStatus auditStatus, List<String> goodsIds);


    @Modifying
    @Query("update GoodsInfo g set g.smallProgramCode = ?2 where g.goodsInfoId = ?1 ")
    void updateSkuSmallProgram(String goodsInfoId, String codeUrl);

    @Modifying
    @Query("update GoodsInfo g set g.smallProgramCode = null ")
    void clearSkuSmallProgramCode();

    /**
     * 根据品牌id 批量把sku品牌置为null
     *
     * @param brandId
     */
    @Modifying
    @Query("update GoodsInfo g set g.brandId = null where g.brandId = :brandId")
    void updateSKUBrandByBrandId(@Param("brandId") Long brandId);

    /**
     * 根据店铺id及品牌id列表 批量把sku品牌置为null
     *
     * @param storeId
     * @param brandIds
     */
    @Modifying
    @Query("update GoodsInfo g set g.brandId = null where g.storeId = :storeId and g.brandId in (:brandIds)")
    void updateBrandByStoreIdAndBrandIds(@Param("storeId") Long storeId, @Param("brandIds") List<Long> brandIds);

    /**
     * 根据多个分类ID编号更新sku关联分类
     *
     * @param newCateId 分类ID
     * @param cateIds   多个分类ID
     */
    @Modifying
    @Query("update GoodsInfo w set w.cateId = ?1, w.updateTime = now() where w.cateId in ?2")
    void updateSKUCateByCateIds(Long newCateId, List<Long> cateIds);

    /**
     * 分销商品审核通过(单个)
     *
     * @param goodsInfoId
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set w.distributionGoodsAudit = 2, w.updateTime = now() where w.goodsInfoId = ?1")
    int checkDistributionGoods(String goodsInfoId);

    /**
     * 批量审核分销商品
     *
     * @param goodsInfoIds
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set w.distributionGoodsAudit = 2, w.updateTime = now() where w.goodsInfoId in ?1")
    int batchCheckDistributionGoods(List<String> goodsInfoIds);

    /**
     * 驳回或禁止分销商品
     *
     * @param goodsInfoId
     * @param distributionGoodsAudit
     * @param distributionGoodsAuditReason
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set w.distributionGoodsAudit = ?2, w.distributionGoodsAuditReason = ?3, w.updateTime =" +
            " now() where w.goodsInfoId = ?1")
    int refuseCheckDistributionGoods(String goodsInfoId, DistributionGoodsAudit distributionGoodsAudit,
                                     String distributionGoodsAuditReason);

    /**
     * 删除分销商品
     *
     * @param goodsInfoId
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set w.distributionGoodsAudit = 0, w.updateTime = now() where w.goodsInfoId = ?1")
    int delDistributionGoods(String goodsInfoId);

    /**
     * 编辑分销商品，修改佣金和状态
     *
     * @param goodsInfoId
     * @param distributionCommission
     * @param distributionGoodsAudit
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set distributionCommission = ?2, w.distributionGoodsAudit = ?3, w.updateTime = now() " +
            "where w.goodsInfoId = ?1")
    int modifyDistributionGoods(String goodsInfoId, BigDecimal distributionCommission, DistributionGoodsAudit distributionGoodsAudit);

    /**
     * 编辑分销商品，修改佣金比例和状态
     *
     * @param goodsInfoId
     * @param
     * @return
     */
    @Modifying
    @Query("update GoodsInfo w set commissionRate = ?2,distributionCommission = ?3, w.distributionGoodsAudit = ?4, w" +
            ".updateTime = now() where w.goodsInfoId = ?1")
    int modifyCommissionDistributionGoods(String goodsInfoId, BigDecimal commissionRate, BigDecimal
            distributionCommission, DistributionGoodsAudit distributionGoodsAudit);

    /*
     * @Description: 商品ID<spu> 修改商品审核状态
     * @Author: Bob
     * @Date: 2019-03-11 16:28
     */
    @Modifying
    @Query("update GoodsInfo w set w.distributionGoodsAudit = :distributionGoodsAudit, w.updateTime = now() where w.goodsId = :goodsId")
    int modifyDistributeState(@Param("goodsId") String goodsId, @Param("distributionGoodsAudit") DistributionGoodsAudit distributionGoodsAudit);


    /**
     * 添加分销商品前，验证所添加的sku是否符合条件
     * 条件：商品是否有效状态（商品已审核通过且未删除和上架状态）以及是否是零售商品
     *
     * @param goodsInfoIds
     * @return
     */
    @Query(value = "select info.goods_info_id " +
            "from goods_info info " +
            "left join goods g on info.goods_id = g.goods_id " +
            "where info.goods_info_id in (:goodsInfoIds) " +
            "and (info.distribution_goods_audit != 0 " +
            "or info.added_flag = 0 " +
            "or info.del_flag = 1 " +
            "or g.sale_type = 0 " +
            "or info.audit_status != 1)", nativeQuery = true)
    List<Object> getInvalidGoodsInfoByGoodsInfoIds(@Param("goodsInfoIds") List<String> goodsInfoIds);


    /**
     * 添加企业购商品前，验证所添加的sku是否符合条件
     * 条件：商品是否有效状态（商品已审核通过且未删除和上架状态) 以及是否是零售商品
     *
     * @param goodsInfoIds
     * @return
     */
    @Query(value = "select info.goods_info_id " +
            "from goods_info info " +
            "left join goods g on info.goods_id = g.goods_id " +
            "where info.goods_info_id in (:goodsInfoIds) " +
            "and info.goods_type !=3 " +
            "and (info.enterprise_goods_audit > 0 "+
            "or info.added_flag = 0 " +
            "or info.del_flag = 1 " +
            "or info.audit_status != 1)", nativeQuery = true)
    List<Object> getInvalidEnterpriseByGoodsInfoIds(@Param("goodsInfoIds") List<String> goodsInfoIds);


    /**
     * 根据单品ids，查询商品名称、市场价
     *
     * @param goodsInfoIds 单品ids
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.goods.info.model.entity.GoodsInfoParams(g.goodsInfoId, g.goodsInfoNo, g.goodsInfoName,g.marketPrice) " +
            " from GoodsInfo g where g.goodsInfoId in (?1)")
    List<GoodsInfoParams> findGoodsInfoParamsByIds(List<String> goodsInfoIds);


    /**
     * 查询必须实时的商品字段
     *
     * @param goodsInfoIds
     * @return
     */
    @Query(value = "select info.goods_info_id,info.del_flag,info.added_flag,info.vendibility,info.audit_status,info.market_price,info.supply_price,info.buy_point from goods_info info where info.goods_info_id in (:goodsInfoIds)", nativeQuery = true)
    List<Object> findGoodsInfoPartColsByIds(@Param("goodsInfoIds") List<String> goodsInfoIds);

    /**
     * 修改商品的企业价格,并更新企业商品审核的状态
     *
     * @param goodsInfoId
     * @param enterPrisePrice
     * @param enterPriseAuditState
     * @return
     */
    @Modifying
    @Query(value = "update GoodsInfo gi set gi.enterPrisePrice = :enterPrisePrice ,gi.enterPriseAuditState = :enterPriseAuditState, " +
            "gi.updateTime = now() where gi.goodsInfoId = :goodsInfoId and gi.delFlag = 0")
    int updateGoodsInfoEnterPrisePrice(@Param("goodsInfoId") String goodsInfoId,
                                       @Param("enterPrisePrice") BigDecimal enterPrisePrice,
                                       @Param("enterPriseAuditState") EnterpriseAuditState enterPriseAuditState
    );

    @Modifying
    @Query(value = "update GoodsInfo gi set gi.enterPrisePrice = :enterPrisePrice ,gi.enterPriseAuditState = :enterPriseAuditState,gi.enterprisePriceType=:enterprisePriceType, " +
            "gi.updateTime = now() where gi.goodsInfoId = :goodsInfoId and gi.delFlag = 0")
    int addGoodsInfoEnterPrisePrice(@Param("goodsInfoId") String goodsInfoId,
                                       @Param("enterPrisePrice") BigDecimal enterPrisePrice,
                                       @Param("enterPriseAuditState") EnterpriseAuditState enterPriseAuditState,
                                       @Param("enterprisePriceType") Integer enterprisePriceType
    );

    /**
     * 批量审核企业购商品 - 审核通过
     *
     * @param goodsInfoIds
     * @param enterPriseAuditState
     * @return
     */
    @Modifying
    @Query(value = "update GoodsInfo gi set gi.enterPriseAuditState = :enterPriseAuditState,gi.updateTime = now() where gi.goodsInfoId in :goodsInfoIds " +
            "and gi.delFlag = 0")
    int batchAuditEnterprise(@Param("goodsInfoIds") List<String> goodsInfoIds,
                             @Param("enterPriseAuditState") EnterpriseAuditState enterPriseAuditState);

    /**
     * 批量审核企业购商品 - 被驳回
     *
     * @param goodsInfoIds
     * @param enterPriseAuditState
     * @return
     */
    @Modifying
    @Query(value = "update GoodsInfo gi set gi.enterPriseAuditState = :enterPriseAuditState,gi.enterPriseGoodsAuditReason = :enterPriseGoodsAuditReason, " +
            "gi.updateTime = now() where gi.goodsInfoId in :goodsInfoIds and gi.delFlag = 0")
    int batchRejectAuditEnterprise(@Param("goodsInfoIds") List<String> goodsInfoIds,
                                   @Param("enterPriseAuditState") EnterpriseAuditState enterPriseAuditState,
                                   @Param("enterPriseGoodsAuditReason") String enterPriseGoodsAuditReason);

    /**
     * 根据供应商商品详情找到商品详情
     *
     * @param goodsInfoId
     */
    @Query
    List<GoodsInfo> findByProviderGoodsInfoId(String goodsInfoId);

    /**
     * 根据供应商商品infoId找到
     *
     * @param delInfoIds
     * @return
     */
    List<GoodsInfo> findByProviderGoodsInfoIdIn(List<String> delInfoIds);

    @Modifying
    @Query(value = "UPDATE goods_info SET cate_id=:cateId WHERE goods_source=:source AND third_cate_id=:thirdCateId", nativeQuery = true)
    void updateThirdCateMap(@Param("source") int source, @Param("thirdCateId") long thirdCateId, @Param("cateId") long cateId);

    @Modifying
    @Query("UPDATE GoodsInfo SET cateId=?4 WHERE goodsSource=?1 and thirdPlatformType=?2 AND thirdCateId=?3")
    void updateStoreThirdCateMap(Integer goodsSource, ThirdPlatformType thirdPlatformType, Long thirdCateId, Long cateId);

    List<GoodsInfo> findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag deleteFlag, Integer goodsSource, String thirdPlatformSpuId);

    List<GoodsInfo> findByDelFlagAndGoodsSourceAndThirdPlatformTypeAndThirdPlatformSpuId(DeleteFlag deleteFlag, Integer goodsSource, ThirdPlatformType thirdPlatformType, String thirdPlatformSpuId);

    @Modifying
    @Query("update GoodsInfo set supplyPrice=?1 ,addedFlag=?2 ,updateTime = now() where goodsSource=2 and thirdPlatformSpuId=?3 and thirdPlatformSkuId=?4")
    void updateLinkedMallGoodsInfo(BigDecimal supplyPrice, Integer addedFlag, String thirdPlatformSpuId, String thirdPlatformSkuId);

    @Modifying
    @Query("update GoodsInfo set supplyPrice=?1 ,vendibility=?2 ,updateTime = now() where thirdPlatformType=0 and thirdPlatformSpuId=?3 and thirdPlatformSkuId=?4")
    void updateStoreLinkedMallGoodsInfo(BigDecimal supplyPrice, Integer vendibility, String thirdPlatformSpuId, String thirdPlatformSkuId);

    /**
     * 根据sku编号、是否删除查询商品信息
     *
     * @param goodsInfoId
     * @param deleteFlag
     * @return
     */
    GoodsInfo findByGoodsInfoIdAndDelFlag(String goodsInfoId, DeleteFlag deleteFlag);

    @Modifying
    @Query("update GoodsInfo set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2 and thirdPlatformSkuId =?3")
    void delByGoodsSourceAndThirdPlatformSpuIdAndThirdPlatformSkuId(Integer goodsSource, String thirdPlatformSpuId, String thirdPlatformSkuId);

    @Modifying
    @Query("update GoodsInfo set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2")
    void delByGoodsSourceAndThirdPlatformSpuId(Integer goodsSource, String thirdPlatformSpuId);

    /**
     * 禁售三方sku
     *
     * @param thirdPlatformType
     * @param thirdPlatformSpuId
     */
    @Modifying
    @Query("update GoodsInfo set vendibility=0,updateTime=now() where goodsSource=?1 and thirdPlatformType=?2 and thirdPlatformSpuId=?3 and thirdPlatformSkuId =?4")
    void vendibilityByGoodsSourceAndThirdPlatformTypeAndThirdPlatformSpuIdAndThirdPlatformSkuId(Integer goodsSource, ThirdPlatformType thirdPlatformType, String thirdPlatformSpuId, String hirdPlatformSkuId);

    @Modifying
    @Query("update GoodsInfo set vendibility=0,updateTime=now() where goodsSource=?1 and thirdPlatformType=?2 and thirdPlatformSpuId=?3 ")
    void vendibilityByGoodsSourceAndThirdPlatformTypeAndThirdPlatformSpuId(Integer goodsSource, ThirdPlatformType thirdPlatformType, String thirdPlatformSpuId);

    @Modifying
    @Query("update GoodsInfo set vendibility=?1 where goodsSource=1 and thirdPlatformType=?2")
    void vendibilityLinkedmallGoodsInfos(Integer vendibility, ThirdPlatformType thirdPlatformType);

    @Modifying
    @Query("update GoodsInfo set vendibility=?1,updateTime=now()  where goodsInfoId in ?2")
    void vendibilityGoodsInfos(Integer vendibility, List<String> goodsInfoIds);

    @Modifying
    @Query("update GoodsInfo set vendibility=?1 where providerGoodsInfoId in ?2")
    void updateGoodsInfoVendibility(Integer vendibility, List<String> goodsInfoIds);


    @Query("select goodsInfoId from GoodsInfo where goodsId in ?1")
    List<String> findGoodsInfoIdByGoodsId(List<String> goodsIds);

    /**
     * 更新供应商店铺状态
     *
     * @param providerStatus
     * @param storeIds
     */
    @Modifying
    @Query("update GoodsInfo  set providerStatus = ?1 where providerId in ?2")
    void updateProviderStatus(Integer providerStatus, List<Long> storeIds);


    /**
     * @param
     * @return
     * @discription 查询storeId
     * @author yangzhen
     * @date 2020/9/2 20:35
     */
    @Query(value = "select storeId from GoodsInfo where goodsInfoId=?1 and delFlag=0")
    Long queryStoreId(String skuId);

    /**
     * 根据单品ids，SPU、库存、规格值
     *
     * @param goodsInfoIds 单品ids
     * @return
     */
    @Query(value = "select new com.wanmi.sbc.goods.info.model.entity.GoodsInfoLiveGoods(g.goodsInfoId, g.goodsId, g.stock) " +
            " from GoodsInfo g where g.goodsInfoId in ?1 and g.delFlag = 0 ")
    List<GoodsInfoLiveGoods> findGoodsInfoLiveGoodsByIds(List<String> goodsInfoIds);

    @Query(value = "SELECT new com.wanmi.sbc.goods.info.model.entity.GoodsMarketingPrice(g.goodsInfoId,g.goodsInfoNo," +
            "g.goodsInfoName,g.goodsId,g.saleType,g.marketPrice,g.supplyPrice) FROM GoodsInfo g " +
            "WHERE g.goodsInfoNo in ?1 and g.delFlag = 0 and g.storeId = ?2")
    List<GoodsMarketingPrice> marketingPriceByNos(List<String> goodsInfoNos, Long storeId);

    @Modifying
    @Query("update GoodsInfo g set g.supplyPrice = ?2 where g.providerGoodsInfoId = ?1")
    void updateSupplyPriceByProviderGoodsInfoId(String goodsInfoId, BigDecimal supplyPrice);

    @Modifying
    @Query("update GoodsInfo set saleType = ?2 where goodsId = ?1")
    void updateSaleTypeByGoodsId(String goodsId, Integer saleType);

    @Query("select goodsInfoId from GoodsInfo where goodsId = ?1 and delFlag = 0 and goodsInfoId not in ?2")
    List<String> findOtherGoodsInfoByGoodsInfoIds(String goodsId, List<String> goodsInfoIds);

    @Query("select distinct erpGoodsInfoNo from GoodsInfo  where erpGoodsInfoNo in ?1 and delFlag = 0")
    List<String> findExistsErpGoodsInfoNo(List<String> erpGoodsInfoNos);


    @Query(value = "select new com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo(g.goodsInfoId, g.goodsId, g.erpGoodsNo,g.costPrice,g.costPriceSyncFlag,g.goodsInfoName,g.goodsInfoNo,g.marketPrice,g.stockSyncFlag,g.stock) " +
            " from GoodsInfo g where g.erpGoodsNo = ?1 and g.delFlag = 0 ")
    GoodsStockInfo findGoodsInfoId(String erpGoodsNo);

    @Query(value = "select new com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo(g.goodsInfoId, g.goodsId, g.erpGoodsNo,g.costPrice,g.costPriceSyncFlag,g.goodsInfoName,g.goodsInfoNo,g.marketPrice,g.stockSyncFlag,g.stock) " +
            " from GoodsInfo g where g.erpGoodsNo in ?1 and g.delFlag = 0 ")
    List<GoodsStockInfo> findGoodsInfoByGoodsNos(List<String> erpGoodsNos);


    @Modifying
    @Query(value = "update GoodsInfo gi set gi.costPrice = :costPrice ,gi.promotionStartTime = :promotionStartTime, gi.promotionEndTime =:promotionEndTime," +
            "gi.updateTime = now() where gi.goodsInfoId = :goodsInfoId")
    int updateGoodsPriceById(@Param("goodsInfoId") String goodsInfoId,
                             @Param("costPrice") BigDecimal costPrice,
                             @Param("promotionStartTime") LocalDateTime promotionStartTime,
                             @Param("promotionEndTime") LocalDateTime promotionEndTime
    );

    @Modifying
    @Transactional
    @Query(value = "update GoodsInfo gi set gi.costPrice = :costPrice where gi.goodsInfoId = :goodsInfoId")
    int updateCostPriceById(@Param("goodsInfoId") String goodsInfoId,@Param("costPrice") BigDecimal costPrice);

    /**
     * 更新库存和成本价
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "update GoodsInfo gi set gi.costPrice=?1, gi.stock=?2 where gi.goodsInfoId=?3")
    int updateCostPriceAndStockById(BigDecimal costPrice, Long stock, String goodsInfoId);
}
