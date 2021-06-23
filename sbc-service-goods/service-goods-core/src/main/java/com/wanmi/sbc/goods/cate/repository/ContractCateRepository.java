package com.wanmi.sbc.goods.cate.repository;

import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 签约分类数据源
 * Created by sunkun on 2017/10/30.
 */
@Repository
public interface ContractCateRepository extends JpaRepository<ContractCate, Long>, JpaSpecificationExecutor<ContractCate> {

    @Modifying
    @Query("delete from ContractCate c where c.goodsCate.cateId in (:ids) and c.storeId = :storeId")
    int deleteByIdsAndStoreId(@Param("ids") List<Long> ids, @Param("storeId") Long storeId);

    @Query("update ContractCate c set c.cateRate = :cateRate where c.goodsCate.cateId in (:cateIds)")
    @Modifying
    void updateCateRate(@Param("cateRate") BigDecimal cateRate, @Param("cateIds") List<Long> cateIds);

    @Query("from ContractCate c where c.goodsCate.cateId = :cateId and c.storeId = :storeId")
    ContractCate queryByCateIdAndStoreId(@Param("cateId") Long cateId, @Param("storeId") Long storeId);

    /**
     * 根据分类Ids查询签约分类数量
     * @param ids
     * @return
     */
    @Query("select count(c.contractCateId) from ContractCate c where c.goodsCate.cateId in (:ids) ")
    int findCountByIds(@Param("ids") List<Long> ids);

    /**
     * 根据Ids删除签约分类
     * @param ids
     * @return
     */
    @Modifying
    @Query("delete from ContractCate c where c.goodsCate.cateId in (:ids) ")
    int deleteByIds(@Param("ids") List<Long> ids);

    @Query("from ContractCate where storeId in ?1")
    List<ContractCate> findByStoreIds(List<Long> storeIds);
}
