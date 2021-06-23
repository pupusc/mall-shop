package com.wanmi.sbc.goods.prop.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GoodsPropDetailRepository extends JpaRepository<GoodsPropDetail, Long>, JpaSpecificationExecutor<GoodsPropDetail> {

    @Query
    List<GoodsPropDetail> findAllByPropIdAndDelFlagOrderBySortAsc(Long propId, DeleteFlag sort);

    @Query
    List<GoodsPropDetail> findAllByPropId(Long propId);

    @Modifying
    @Query(value = "update GoodsPropDetail a set a.propId = :propId, " +
            "a.detailName = :detailName, " +
            "a.updateTime = :updateTime, " +
            "a.delFlag = :delFlag, " +
            "a.sort = :sort " +
            "where a.detailId = :detailId")
    int editPropDetail(@Param("propId") Long propId,
                       @Param("detailName") String detailName,
                       @Param("updateTime") LocalDateTime updateTime,
                       @Param("delFlag") DeleteFlag delFlag,
                       @Param("sort") Integer sort,
                       @Param("detailId") Long detailId);
}
