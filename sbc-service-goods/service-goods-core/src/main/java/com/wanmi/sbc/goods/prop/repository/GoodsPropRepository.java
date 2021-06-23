package com.wanmi.sbc.goods.prop.repository;


import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoodsPropRepository extends JpaRepository<GoodsProp, Long>, JpaSpecificationExecutor<GoodsProp> {

    @Query
    List<GoodsProp> findAllByCateIdAndDelFlagOrderBySortAsc(Long cateId, DeleteFlag sort);

    @Query
    GoodsProp findByPropId(Long propId);


    @Query
    List<GoodsProp> findAllByCateIdAndIndexFlagAndDelFlagOrderBySortAsc(Long cateId, DefaultFlag indexFlag, DeleteFlag sort);

    @Modifying
    @Query(value = "update GoodsProp a set a.cateId = :cateId, " +
            "a.propName = :propName, " +
            "a.indexFlag = :indexFlag, " +
            "a.updateTime = :updateTime " +
            "where a.propId = :propId ")
    int editGoodsProp(@Param("cateId") Long cateId,
                      @Param("propName") String propName,
                      @Param("indexFlag") DefaultFlag indexFlag,
                      @Param("updateTime") LocalDateTime updateTime,
                      @Param("propId") Long propId);

    @Query("from GoodsProp g where g.cateId in(:cateIds) and g.delFlag = '0'")
    List<GoodsProp> findAllByCateIsAndAndDelFlag(@Param("cateIds")List<Long> cateIds);
}
