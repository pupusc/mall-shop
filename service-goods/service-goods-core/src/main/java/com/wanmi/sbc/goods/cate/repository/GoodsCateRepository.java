package com.wanmi.sbc.goods.cate.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.cate.entity.GoodsCateBase;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品分类数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface GoodsCateRepository extends JpaRepository<GoodsCate, Long>, JpaSpecificationExecutor<GoodsCate> {

    /**
     * 获取所有可用的最子分类
     *
     * @return 子分类结果
     */
    @Query("from GoodsCate where delFlag = '0' and cateGrade = 3")
    List<GoodsCate> queryLeaf();

    /**
     * 限制范围
     * 获取所有可用的最子分类
     *
     * @return 子分类结果
     */
    @Query("from GoodsCate where delFlag = '0' and cateId in (:ids) and cateGrade = 3")
    List<GoodsCate> queryLeaf(@Param("ids") List<Long> ids);


    /**
     * 根据catid 批量查询
     *
     * @return
     */
    @Query("from GoodsCate g where g.delFlag = :deleteFlag and g.cateId in (:ids)")
    List<GoodsCate> queryCates(@Param("ids") List<Long> ids, @Param("deleteFlag") DeleteFlag deleteFlag);

    /**
     * 商品分类排序
     *
     * @param cateId  商品分类Id
     * @param sort         商品分类顺序
     */
    @Query(" update GoodsCate c set c.sort = ?2 where c.cateId = ?1 ")
    @Modifying
    void updateCateSort(Long cateId, Integer sort);

    /**
     * 同步成长值购物规则为积分购物规则
     */
    @Query(" update GoodsCate c set c.growthValueRate = c.pointsRate , c.isParentGrowthValueRate = c.isParentPointsRate ")
    @Modifying
    void updateGrowthValueRuleByPoints();

    /**
     * 根据catid 批量查询
     *
     * @return
     */
    @Query("from GoodsCate g where g.delFlag = 0 and g.cateId =:parentId")
    GoodsCate queryCatesShence(@Param("parentId")Long parentId);

    /**
     * 根据catid 批量查询
     *
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.cate.entity.GoodsCateBase(g.cateId , g.catePath ) from GoodsCate g where g.cateId = ?1 and g.delFlag = 0")
    GoodsCateBase queryBaseCatesById(Long goodCateId);

    /**
     * 根据catid 批量查询
     *
     * @return
     */
    @Query("select new com.wanmi.sbc.goods.cate.entity.GoodsCateBase(g.cateId , g.cateName,g.cateParentId ) from GoodsCate g where g.cateId in ?1 and g.delFlag = 0 ")
    List<GoodsCateBase> findAllByCateIdIn(List<Long> ids);
}
