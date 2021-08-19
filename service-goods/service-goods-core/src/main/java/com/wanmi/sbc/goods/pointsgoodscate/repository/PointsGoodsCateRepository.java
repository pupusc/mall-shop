package com.wanmi.sbc.goods.pointsgoodscate.repository;

import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>积分商品分类表DAO</p>
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@Repository
public interface PointsGoodsCateRepository extends JpaRepository<PointsGoodsCate, Integer>,
        JpaSpecificationExecutor<PointsGoodsCate> {

    /**
     * 单个删除积分商品分类表
     * @author yang
     */
    @Modifying
    @Query("update PointsGoodsCate set delFlag = 1 where cateId = ?1")
    int modifyDelFlagById(Integer cateId);

    /**
     * 批量删除积分商品分类表
     * @author yang
     */
    @Modifying
    @Query("update PointsGoodsCate set delFlag = 1 where cateId in ?1")
    int deleteByIdList(List<Integer> cateIdList);

}
