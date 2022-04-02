package com.wanmi.sbc.goods.info.repository;


import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.info.model.root.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods, String>, JpaSpecificationExecutor<Goods>{

    /**
     * 根据多个商品ID编号进行删除
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.delFlag = '1', w.updateTime = now() where w.goodsId in ?1")
    void deleteByGoodsIds(List<String> goodsIds);



    /**
     * 根据多个商品ID编号进行删除供应商商品
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.delFlag = '1',w.deleteReason=?2 ,w.updateTime = now() where w.goodsId in ?1")
    void deleteProviderByGoodsIds(List<String> goodsIds,String deleteReason);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedFlag = ?1, w.updateTime = now(),w.addFalseReason=?3, w.addedTime = now() where w.goodsId in ?2")
    void updateAddedFlagByGoodsIds(Integer addedFlag, List<String> goodsIds,String unAddFlagReason);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedFlag = ?1, w.deleteReason=?4 , w.updateTime = now(),w.addFalseReason=?3, w.addedTime = now() where w.goodsId in ?2")
    void updateAddedFlagByGoodsIdsAddDeleteReason(Integer addedFlag, List<String> goodsIds,String unAddFlagReason,String reason);



    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedFlag = ?1, w.updateTime = now(), w.addFalseReason=?3,w.addedTime = now() where w.providerGoodsId in ?2")
    void updateAddedFlagByPrividerGoodsIds(Integer addedFlag, List<String> goodsIds,String unAddFlagReason);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsId 商品ID
     */
    @Modifying
    @Query("update Goods w set  w.updateTime = now(), w.addFalseReason=null where w.providerGoodsId = ?1")
    void updateAddedFlagReasonByPrividerGoodsIds(String goodsId);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedFlag = ?1, w.updateTime = now(), w.addedTime = now() where w.goodsId in ?2")
    void updateAddedFlagByGoodsIds(Integer addedFlag, List<String> goodsIds);


    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedTimingFlag = ?1, w.updateTime = now() where w.goodsId in ?2")
    void updateAddedTimingFlagByGoodsIds(Boolean addedTimingFlag, List<String> goodsIds);

    /**
     * 根据多个商品ID编号更新上下架状态
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.addedFlag = ?1, w.updateTime = now(),w.addedTime = now() where w.providerGoodsId in ?2")
    void updateAddedFlagByPrividerGoodsIds(Integer addedFlag, List<String> goodsIds);


    @Modifying
    @Query("update Goods set addedFlag=?1,updateTime = now (),addedTime = now() where goodsSource=?2 and thirdPlatformSpuId=?3")
    void updateAddedFlagByGoodsSourceAndThirdPlatformSpuId(Integer addedFlag,Integer goodsSource,String thirdPlatformSpuId);

    @Modifying
    @Query("update Goods set vendibility=?1 where goodsSource=?2 and thirdPlatformType=?3 and thirdPlatformSpuId=?4")
    void vendibilityByGoodsSourceAndThirdPlatformTypeAndThirdPlatformSpuId(Integer vendibility, Integer goodsSource,ThirdPlatformType thirdPlatformType,String thirdPlatformSpuId);


    /**
     * 根据多个分类ID编号更新分类
     * @param newCateId 分类ID
     * @param cateIds 多个分类ID
     */
    @Modifying
    @Query("update Goods w set w.cateId = ?1, w.updateTime = now() where w.cateId in ?2")
    void updateCateByCateIds(Long newCateId, List<Long> cateIds);

    /**
     * 根据多个商品ID编号更新审核状态
     * @param auditStatus 审核状态
     * @param auditReason 审核原因
     * @param goodsIds 多个商品
     */
    @Modifying
    @Query("update Goods w set w.auditStatus = ?1,w.deleteReason=null,w.addFalseReason=null, w.auditReason = ?2, w.submitTime = now()  where w.goodsId in ?3")
    void updateAuditDetail(CheckStatus auditStatus, String auditReason, List<String> goodsIds);

    /**
     * 根据商家id 批量更新商家名称
     * @param supplierName
     * @param companyInfoId
     */
    @Modifying
    @Query("update Goods g set g.supplierName = :supplierName where g.companyInfoId = :companyInfoId")
    void updateSupplierName(@Param("supplierName") String supplierName, @Param("companyInfoId") Long companyInfoId);

    /**
     * 根据品牌id 批量把spu品牌置为null
     * @param brandId
     */
    @Modifying
    @Query("update Goods g set g.brandId = null where g.brandId = :brandId")
    void updateBrandByBrandId(@Param("brandId") Long brandId);


    /**
     * 根据店铺id及品牌id列表 批量把spu品牌置为null
     * @param storeId
     * @param brandIds
     */
    @Modifying
    @Query("update Goods g set g.brandId = null where g.storeId = :storeId and g.brandId in (:brandIds)")
    void updateBrandByStoreIdAndBrandIds(@Param("storeId") Long storeId, @Param("brandIds") List<Long> brandIds);

    /**
     * 根据类别id查询SPU
     * @param cateId
     * @return
     */
    @Query
    List<Goods> findAllByCateId(Long cateId);

    /**
     * 根据多个商品ID编号编辑运费模板
     * @param goodsIds 商品ID
     */
    @Modifying
    @Query("update Goods w set w.freightTempId = ?1, w.updateTime = now() where w.goodsId in ?2")
    void updateFreightTempIdByGoodsIds(Long freightTempId, List<String> goodsIds);

    /**
     * 修改商品运费模板为默认运费模板
     * @param oldFreightTempId
     * @param freightTempId
     */
    @Modifying
    @Query("update Goods g set g.freightTempId = :freightTempId where g.freightTempId = :oldFreightTempId")
    void updateFreightTempId(@Param("oldFreightTempId") Long oldFreightTempId, @Param("freightTempId") Long freightTempId);

    /**
     * @Author lvzhenwei
     * @Description 更新商品评论数
     * @Date 14:31 2019/4/11
     * @Param [goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsEvaluateNum = g.goodsEvaluateNum+1, g.updateTime = now() where g.goodsId = ?1")
    void updateGoodsEvaluateNum(@Param("goodsId") String goodsId);

    /**
     * @Author lvzhenwei
     * @Description 更新商品收藏量
     * @Date 14:43 2019/4/11
     * @Param [goodsCollectNum, GoodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsCollectNum = g.goodsCollectNum + ?1, g.updateTime = now()  where g.goodsId = ?2")
    void updateGoodsCollectNum(@Param("goodsCollectNum") Long goodsCollectNum,@Param("goodsId") String GoodsId);

    /**
     * @Author lvzhenwei
     * @Description 更新商品销量
     * @Date 14:43 2019/4/11
     * @Param [goodsSalesNum, goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsSalesNum = g.goodsSalesNum + ?1, g.updateTime = now()  where g.goodsId = ?2")
    void updateGoodsSalesNum(@Param("goodsSalesNum") Long goodsSalesNum,@Param("goodsId") String goodsId);

    /**
     * @Author lvzhenwei
     * @Description 更新商品好评数量
     * @Date 14:50 2019/4/11
     * @Param [goodsPositiveFeedback, goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsFavorableCommentNum = g.goodsFavorableCommentNum + ?1, g.updateTime = now()  where g.goodsId = ?2")
    void updateGoodsFavorableCommentNum(@Param("goodsPositiveFeedback") Long goodsFavorableCommentNum,@Param("goodsId") String goodsId);

    /**
     * @Description 更新商品注水销量
     * @Date 14:43 2019/4/11
     * @Param [shamSalesNum, goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.shamSalesNum = :shamSalesNum, g.updateTime = now()  where g.goodsId = :goodsId")
    void updateShamGoodsSalesNum(@Param("shamSalesNum") Long shamSalesNum,@Param("goodsId") String goodsId);

    /**
     * @Description 更新商品排序号
     * @Date 14:43 2019/4/11
     * @Param [shamSalesNum, goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.sortNo = :sortNo, g.updateTime = now()  where g.goodsId = :goodsId")
    void updateSortNo(@Param("sortNo") Long sortNo,@Param("goodsId") String goodsId);
//    /**
//     * 根据供应商商品id查询关联商品
//     * @param goodsIds
//     * @return
//     */
//    @Query
//    List<Goods> findAllByProviderGoodsIdIn(List<String> goodsIds);

    /**
     * 根据供应商商品id查询关联商品
     * @param providerGoodsId
     * @return
     */
    @Query
    List<Goods> findAllByProviderGoodsId(String providerGoodsId);

    /**
     * 根据商品id查询商品信息
     * @param goodsIds
     * @return
     */
    @Query
    List<Goods> findAllByGoodsIdIn(List<String> goodsIds);

    /**
     * 根据商品SPU编号减库存
     * @param stock 库存数
     */
    @Modifying
    @Query("update Goods w set w.stock = w.stock - ?1 where w.goodsId = ?2 and w.stock  >= ?1")
    void subStockById(Long stock, String goodsId);

    /**
     * 更新代销商品的可售性
     * @param stock
     * @param providerGoodsIds
     */
    @Modifying
    @Query("update Goods set stock=?1 where providerGoodsId in ?2")
    void updateStockByProviderGoodsIds(Long stock, List<String> providerGoodsIds);

    @Modifying
    @Transactional
    @Query("update Goods set stock=?1 where goodsId = ?2")
    void updateStockByGoodsId(Long stock, String goodsId);

    @Modifying
    @Query(value = "UPDATE goods SET cate_id=:cateId WHERE goods_source=:source AND third_cate_id=:thirdCateId",nativeQuery = true)
    void updateThirdCateMap(@Param("source") int source, @Param("thirdCateId") long thirdCateId, @Param("cateId") long cateId);

//    @Modifying
//    @Query("UPDATE Goods SET cateId=?4 WHERE goodsSource=?1 and thirdPlatformType=?2 AND thirdCateId=?3")
//    void updateStoreThirdCateMap( Integer goodsSource,ThirdPlatformType thirdPlatformType, Long thirdCateId, Long cateId);
//
//    Goods findByThirdPlatformSpuIdAndGoodsSource(String thirdPlatformSpuId, Integer goodsSource);

    @Modifying
    @Query("update Goods set delFlag=1,updateTime=now() where goodsSource=?1 and thirdPlatformSpuId=?2")
    void delByGoodsSourceAndThirdPlatformSpuId(Integer goodsSource,String thirdPlatformSpuId);

    Goods findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag deleteFlag, Integer goodsSource, String thirdPlatformSpuId);

    @Modifying
    @Query("update Goods set vendibility=?1 where goodsSource=1 and thirdPlatformType=?2")
    void vendibilityLinkedmallGoods(Integer vendibility,ThirdPlatformType thirdPlatformType);

    /**
     * 更新代销商品的可售性
     * @param vendibility
     * @param goodsIds
     */
    @Modifying
    @Query("update Goods set vendibility=?1 where providerGoodsId in ?2")
    void updateGoodsVendibility(Integer vendibility, List<String> goodsIds);

    /**
     * 更新供应商店铺状态
     * @param providerStatus
     * @param storeIds
     */
    @Modifying
    @Query("update Goods  set providerStatus = ?1 where providerId in ?2")
    void updateProviderStatus(Integer providerStatus, List<Long> storeIds);


    /**
    * @discription 根据goodsid 查询图文信息
    * @author yangzhen
    * @date 2020/9/3 11:21
    * @param goodsId
    * @return
    */
    @Query("select goodsDetail  from Goods  where goodsId = ?1")
    String getGoodsDetail( String goodsId);

    /**
     * @Author lvzhenwei
     * @Description 增加商品评论数
     * @Date 14:31 2019/4/11
     * @Param [goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsEvaluateNum = g.goodsEvaluateNum + 1, g.updateTime = now() where g.goodsId = ?1 ")
    void increaseGoodsEvaluateNum(@Param("goodsId") String goodsId);

    /**
     * @Author lvzhenwei
     * @Description 减少商品评论数
     * @Date 14:31 2019/4/11
     * @Param [goodsId]
     * @return void
     **/
    @Modifying
    @Query("update Goods g set g.goodsEvaluateNum = g.goodsEvaluateNum - 1, g.updateTime = now() where g.goodsId = ?1 and g.goodsEvaluateNum > 0 ")
    void decreaseGoodsEvaluateNum(@Param("goodsId") String goodsId);

    /**
     * 根据goodsId查询评论总数
     * @param goodsId
     * @return
     */
    @Query("select g.goodsEvaluateNum from  Goods g where g.goodsId = ?1")
    Long getGoodsEvaluateNumByGoodsId(String goodsId);

    /**
     * 查询商品
     * @param storeId
     * @param deleteFlag
     * @return
     */
    List<Goods> findByGoodsIdInAndStoreIdAndDelFlag(List<String> goodsIds, Long storeId, DeleteFlag deleteFlag);


    /**
     * 根据商品SPU编号减库存
     * @param stock 库存数
     */
    @Transactional
    @Modifying
    @Query("update Goods w set w.stock = ?1 where w.goodsId = ?2")
    void resetGoodsStockById(Long stock, String goodsId);

    /**
     * 查询erp编码
     * @param erpGoodsNos
     * @return
     */
    @Query("select distinct erpGoodsNo from Goods where erpGoodsNo = ?1 and delFlag= 0")
    List<String> findExistsErpGoodsNo(String erpGoodsNos);

    /**
     * 根据spu编号查询
     * @param goodsId
     * @return
     */
    Goods findByGoodsIdAndDelFlag(String goodsId, DeleteFlag delFlag);

    /**
     * 根据goodsNumber找到goodsId
     * @return
     */
    @Query(value = "select goods_id,goods_no from goods where goods_no in ?1 and del_flag=0", nativeQuery = true)
    List<Object[]> findIdByNumber(List<String> numbers);

//    /**
//     * 查找所有书籍
//     */
//    @Query(value = "select g.* from goods_cate as c join goods as g on g.cate_id=c.cate_id where c.book_flag = 1", nativeQuery = true)
//    List<Goods> findBooks();

    @Modifying
    @Query("update Goods w set w.costPrice = ?2 where w.goodsId = ?1")
    void resetGoodsPriceById(String goodsId, BigDecimal costPrice);


    /**
     * 根据最大id
     * @param providerId
     * @return
     */
    @Query(value = "select tmp_id, erp_goods_no from goods where del_flag = 0 and added_flag = 1 and provider_id=?1  and tmp_id > ?2 order by tmp_id asc limit ?3", nativeQuery = true)
    List<Map<String, Object>> listByMaxAutoId(String providerId, Long tmpId, Integer pageSize);
}
