package com.wanmi.sbc.goods.freight.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sunkun on 2018/5/2.
 */
@Repository
public interface FreightTemplateGoodsRepository extends JpaRepository<FreightTemplateGoods, Long> {

    @Query(value = "from FreightTemplateGoods a where a.storeId = ?1 and defaultFlag = 1 and delFlag = 0")
    FreightTemplateGoods queryByDefault(Long storeId);

    /**
     * 获取店铺下所有单品运费模板
     *
     * @param storeId
     * @param delFlag
     * @return
     */
    @Query("from FreightTemplateGoods f where f.storeId = :storeId and f.delFlag = :delFlag order by f.defaultFlag desc,f.createTime desc")
    List<FreightTemplateGoods> queryAll(@Param("storeId") Long storeId, @Param("delFlag") DeleteFlag delFlag);

    /**
     * 获取单品运费模板
     *
     * @param freightTempId
     * @return
     */
    @Query("from FreightTemplateGoods f where f.freightTempId = :freightTempId and f.delFlag = 0")
    FreightTemplateGoods queryById(@Param("freightTempId") Long freightTempId);

    /**
     * 查询店铺下未删除的单品运费模板总数
     *
     * @param storeId
     * @param delFlag
     * @return
     */
    int countByStoreIdAndDelFlag(Long storeId, DeleteFlag delFlag);

    /**
     * 根据单品名称查询
     *
     * @param storeId
     * @param freightTempName
     * @return
     */
    @Query("from FreightTemplateGoods f where f.storeId = :storeId and f.freightTempName = :freightTempName and f.delFlag = 0")
    FreightTemplateGoods queryByFreighttemplateName(@Param("storeId") Long storeId, @Param("freightTempName") String freightTempName);

    @Query("from FreightTemplateGoods f where f.freightTempId in :freightTempIds")
    List<FreightTemplateGoods> queryByFreightTempIds(@Param("freightTempIds") List<Long> freightTempIds);
}
