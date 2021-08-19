package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoodsFree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunkun on 2018/5/4.
 */
@Repository
public interface FreightTemplateGoodsFreeRepository extends JpaRepository<FreightTemplateGoodsFree, Long> {

    List<FreightTemplateGoodsFree> findByFreightTempIdAndDelFlag(Long freightTempId, DeleteFlag delFlag);

    @Query("from FreightTemplateGoodsFree f where f.freightTempId in :freightTempIds and f.delFlag = 0")
    List<FreightTemplateGoodsFree> findByFreightTempIds(@Param("freightTempIds") List<Long> freightTempIds);
}
