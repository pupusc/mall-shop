package com.wanmi.sbc.goods.storecate.repository;

import com.wanmi.sbc.goods.storecate.entity.StoreCateBase;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 店铺分类数据源
 * Created by bail on 2017/11/13.
 */
@Repository
public interface StoreCateRepository extends JpaRepository<StoreCate, Long>, JpaSpecificationExecutor<StoreCate>{

    /**
     * 获取所有可用的最子分类
     * @return 子分类结果
     */
    @Query("from StoreCate where delFlag = '0' and storeId = :storeId and storeCateId not in (select cateParentId from StoreCate where storeId = :storeId)")
    List<StoreCate> queryLeaf(@Param("storeId") Long storeId);

    /**
     * 查询该分类下排序最大的子分类
     * @param cateParentId
     * @return
     */
    StoreCate findTop1ByCateParentIdOrderBySortDesc(Long cateParentId);
    /**
     * 店铺分类排序
     *
     * @param storeCateId  店铺分类Id
     * @param sort         店铺分类顺序
     */
    @Query(" update StoreCate c set c.sort = ?2 where c.storeCateId = ?1 ")
    @Modifying
    void updateCateSort(Long storeCateId, Integer sort);

    /**
     * 根据店铺分类名称查询与之相同的记录数
     * @param storeCate
     * @return
     */
    @Query("select count(s) from StoreCate s where s.cateName=:#{#storeCate.cateName} and s.storeId=:#{#storeCate.storeId} " +
            "and s.delFlag=:#{#storeCate.delFlag} and s.cateGrade=:#{#storeCate.cateGrade} and s.cateParentId=:#{#storeCate.cateParentId} ")
    Long countStoreCateByConditions(@Param("storeCate")StoreCate storeCate);

    /**
     * 根据ID集合查询店铺分类
     * @return 子分类结果
     */
    @Query("select new com.wanmi.sbc.goods.storecate.entity.StoreCateBase(g.storeCateId , g.cateName,g.cateParentId ) from StoreCate g where g.storeCateId in ?1 and g.delFlag = 0 ")
    List<StoreCateBase> findAllByStoreCateIdIn(List<Long> storeCateIds);
}
