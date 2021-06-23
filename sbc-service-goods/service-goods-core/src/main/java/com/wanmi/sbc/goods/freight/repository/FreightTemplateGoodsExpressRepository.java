package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoodsExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunkun on 2018/5/4.
 */
@Repository
public interface FreightTemplateGoodsExpressRepository extends JpaRepository<FreightTemplateGoodsExpress, Long> {

    @Query
    FreightTemplateGoodsExpress findByFreightTempIdAndDelFlagAndDefaultFlag(@Param("freightTempId") Long freightTempId, @Param("deleteFlag") DeleteFlag deleteFlag, @Param("defaultFlag") DefaultFlag defaultFlag);

    List<FreightTemplateGoodsExpress> findByFreightTempIdAndDelFlag(Long freightTempId, DeleteFlag delFlag);

    @Query("from FreightTemplateGoodsExpress f where f.freightTempId in :freightTempIds and f.delFlag = 0")
    List<FreightTemplateGoodsExpress> findByFreightTempIds(@Param("freightTempIds") List<Long> freightTempIds);
}
