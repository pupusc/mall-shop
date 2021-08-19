package com.wanmi.sbc.goods.standard.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品库SKU数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface StandardSkuRepository extends JpaRepository<StandardSku, String>, JpaSpecificationExecutor<StandardSku>{

    /**
     * 根据多个商品ID编号进行删除
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update StandardSku w set w.delFlag = '1' ,w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);

    /**
     * 根据商品ID编号进行删除
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update StandardSku w set w.delFlag = '1' ,w.updateTime = now() where w.goodsId = ?1")
    void deleteByGoodsId(String goodsId);

    /**
     * 根据多个商品ID编号进行删除
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update StandardSku w set w.delFlag = '1' ,w.updateTime = now() where w.providerGoodsInfoId in ?1")
    void deleteByProviderGoodsInfoIds(List<String> goodsIds);

    /**
     * 根据商品库sku
     * @param goodsId
     * @return
     */
    @Query
    List<StandardSku> findByGoodsId(String goodsId);

    @Modifying
    @Query("update StandardSku w set w.delFlag = '0' ,w.updateTime = now() where w.delFlag = '1' and w.goodsId = ?1")
    void updateDelFlag(String standardId);

    @Modifying
    @Query("update StandardSku w set w.addedFlag = ?2 ,w.goodsInfoNo = ?3 ,w.updateTime = now() where w.goodsInfoId = ?1")
    void updateAddedFlagAndGoodsInfoNo(String goodsInfoId,Integer addedFlag ,String goodsInfoNo);

    @Modifying
    @Query("update StandardSku w set w.addedFlag = ?2 ,w.updateTime = now() where w.goodsInfoId = ?1")
    void updateAddedFlag(String goodsInfoId,Integer addedFlag );

    @Query("from StandardSku w where w.delFlag = '0' and w.goodsId in ?1")
    List<StandardSku> findByGoodsIdIn(List<String> standardGoodsIds);

    StandardSku findByDelFlagAndGoodsSourceAndThirdPlatformSpuIdAndThirdPlatformSkuId(DeleteFlag deleteFlag,Integer goodsSource, String thirdPlatformSpuId,String thirdPlatformSkuId);

    /**
     * 查询所有三方渠道商品
     * @param deleteFlag
     * @param goodsSource
     * @return
     */
    List<StandardSku> findByDelFlagAndGoodsSource(DeleteFlag deleteFlag,Integer goodsSource);

    @Modifying
    @Query("update StandardSku set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2 and thirdPlatformSkuId =?3")
    void delByGoodsSourceAndThirdPlatformSpuIdAndThirdPlatformSkuId(Integer goodsSource,String thirdPlatformSpuId,String thirdPlatformSkuId);

    @Modifying
    @Query("update StandardSku set supplyPrice=?1 ,addedFlag=?2 where goodsSource=2 and thirdPlatformSpuId=?3 and thirdPlatformSkuId=?4")
    void updateLinkedMallStandardSku(BigDecimal supplyPrice, Integer addedFlag, String thirdPlatformSpuId, String thirdPlatformSkuId);

    List<StandardSku> findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag deleteFlag, int goodsSource, String thirdPlatformSpuId);

    @Modifying
    @Query("update StandardSku set delFlag=1,updateTime=now() where goodsInfoId=?1")
    void deleteByGoodsInfoId(String goodsInfoId);

    @Modifying
    @Query("update StandardSku set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2")
    void delByGoodsSourceAndThirdPlatformSpuId(Integer goodsSource, String thirdPlatformSpuId);

    @Modifying
    @Query("update StandardSku  set supplyPrice = ?2 where providerGoodsInfoId = ?1")
    void updateSupplyPriceByProviderGoodsInfoId(String providerGoodsInfoId, BigDecimal supplyPrice);
}
