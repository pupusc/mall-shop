package com.wanmi.sbc.goods.brand.repository;

import com.wanmi.sbc.goods.brand.entity.ContractBrandBase;
import com.wanmi.sbc.goods.brand.model.root.ContractBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 签约品牌数据源
 * Created by sunkun on 2017/10/31.
 */
@Repository
public interface ContractBrandRepository extends JpaRepository<ContractBrand, Long>, JpaSpecificationExecutor<ContractBrand> {

    /**
     * 根据主键列表及店铺id删除
     * @param ids
     * @param storeId
     * @return
     */
    @Modifying
    @Query("delete from ContractBrand b where b.contractBrandId in :ids and b.storeId = :storeId")
    int deleteByIdsAndStoreId(@Param("ids") List<Long> ids, @Param("storeId") Long storeId);

    /**
     * 查询店铺下所有自定义品牌
     * @param storeId
     * @return
     */
    @Query("from ContractBrand c where c.storeId = :storeId  and c.checkBrand.checkBrandId is not null")
    List<ContractBrand> findBrandByCheckId(@Param("storeId") Long storeId);

    /**
     * 根据平台品牌id 删除所有商家签约品牌
     * @param brandId
     */
    @Query("delete from ContractBrand c where c.goodsBrand.brandId = :brandId")
    @Modifying
    void deleteByGoodsBrandId(@Param("brandId") Long brandId);

    /**
     * 根据店铺id及主键列表查询
     * @param streId
     * @param contractBrandIds
     * @return
     */
    @Query("select c from ContractBrand c where c.storeId = :storeId and c.contractBrandId in (:contractBrandIds)")
    List<ContractBrand> findBrandByStoreIdAndIds(@Param("storeId") Long streId, @Param("contractBrandIds") List<Long> contractBrandIds);

    /**
     * 根据店铺id及品牌ID列表查询
     * @param storeId
     * @param brandIds
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.brand.entity.ContractBrandBase(c.contractBrandId,c.brandId) from ContractBrand c where c.storeId = :storeId and c.brandId in (:brandIds)")
    List<ContractBrandBase> findAllByStoreIdAndBrandIdIn(@Param("storeId") Long storeId, @Param("brandIds") List<Long> brandIds);
}
