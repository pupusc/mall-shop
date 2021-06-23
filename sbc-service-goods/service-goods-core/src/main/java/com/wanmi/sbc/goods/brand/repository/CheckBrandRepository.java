package com.wanmi.sbc.goods.brand.repository;

import com.wanmi.sbc.goods.brand.model.root.CheckBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 待审核品牌
 * Created by sunkun on 2017/11/1.
 */
@Repository
public interface CheckBrandRepository extends JpaRepository<CheckBrand, Long>, JpaSpecificationExecutor<CheckBrand> {

    @Query("select c from CheckBrand c where c.name = :name and c.storeId = :storeId")
    CheckBrand queryByCheckNameAndStoreId(@Param("name") String name, @Param("storeId") Long storeId);

    @Modifying
    @Query("delete from CheckBrand c where c.checkBrandId in (:ids)")
    int deleteByCheckBrandIds(@Param("ids") List<Long> ids);
}
