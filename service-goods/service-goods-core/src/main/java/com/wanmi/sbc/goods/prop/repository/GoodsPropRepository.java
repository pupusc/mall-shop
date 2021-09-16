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

    @Query("from GoodsProp as gp left join GoodsPropCateRel as gcr on gcr.propId=gp.propId where gcr.cateId=:cateId and gp.delFlag=0 order by gp.sort asc")
    List<GoodsProp> findAllByCateIdAndDelFlag(Long cateId);

    @Query(value = "select * from goods_prop as gp join t_goods_prop_cate_rel as gcr on gcr.prop_id=gp.prop_id where gp.del_flag=0 order by gp.sort limit 999", nativeQuery = true)
    List<GoodsProp> findAllNew();

    List<GoodsProp> findAllByPropIdIn(List<Long> propIds);

    @Query
    GoodsProp findByPropId(Long propId);

    GoodsProp findByPropName(String propName);

    @Query
    List<GoodsProp> findAllByCateIdAndIndexFlagAndDelFlagOrderBySortAsc(Long cateId, DefaultFlag indexFlag, DeleteFlag sort);

    @Modifying
    @Query(value = "update GoodsProp a set a.cateId = :cateId, " +
            "a.propName = :propName, " +
            "a.indexFlag = :indexFlag, " +
            "a.updateTime = :updateTime, " +
            "a.propType = :propType " +
            "where a.propId = :propId ")
    int editGoodsProp(@Param("cateId") Long cateId,
                      @Param("propName") String propName,
                      @Param("indexFlag") DefaultFlag indexFlag,
                      @Param("updateTime") LocalDateTime updateTime,
                      @Param("propType") Integer propType,
                      @Param("propId") Long propId);

    @Query("from GoodsProp g where g.cateId in(:cateIds) and g.delFlag = '0'")
    List<GoodsProp> findAllByCateIsAndAndDelFlag(@Param("cateIds")List<Long> cateIds);
}
