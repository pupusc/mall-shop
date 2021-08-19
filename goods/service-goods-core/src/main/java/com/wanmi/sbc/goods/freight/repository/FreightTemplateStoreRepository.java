package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunkun on 2018/5/3.
 */
@Repository
public interface FreightTemplateStoreRepository extends JpaRepository<FreightTemplateStore, Long>, JpaSpecificationExecutor<FreightTemplateStore> {


    /**
     * 根据id查询店铺运费模板
     *
     * @param freightTempId
     * @return
     */
    @Query("from FreightTemplateStore f where f.freightTempId = :freightTempId and f.delFlag = 0")
    FreightTemplateStore findByIdAndDefaultFlag(@Param("freightTempId") Long freightTempId);

    @Query("from FreightTemplateStore f where f.storeId = :storeId and f.delFlag = :delFlag")
    List<FreightTemplateStore> findByAll(@Param("storeId") Long storeId, @Param("delFlag") DeleteFlag delFlag);

    /**
     * 根据模板名称查询店铺运费模板
     *
     * @param storeId
     * @param freightTempName
     * @return
     */
    @Query("from FreightTemplateStore f where f.storeId = :storeId and f.freightTempName = :freightTempName and f.delFlag = 0")
    FreightTemplateStore findByFreightTemplateName(@Param("storeId") Long storeId, @Param("freightTempName") String freightTempName);
}
