package com.wanmi.sbc.goods.distributionmatter.repository;

import com.wanmi.sbc.goods.distributionmatter.model.root.DistributionGoodsMatter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionGoodsMatterRepository extends JpaRepository<DistributionGoodsMatter, String>, JpaSpecificationExecutor<DistributionGoodsMatter> {

    /**
     * 批量删除商品素材
     *
     * @param ids
     * @return
     */
    @Modifying
    @Query("update DistributionGoodsMatter set del_flag = 1, del_time = now() where id in(?1)")
    int deleteByIds(List ids);

    /**
     * 根据id批量查询
     * @param ids
     * @return
     */
    @Query("from DistributionGoodsMatter where id in(?1) and delFlag = 0")
    List<DistributionGoodsMatter> queryByIds(List ids);

    @Modifying
    @Query("update DistributionGoodsMatter d set d.recommend = :#{#info.recommend},d.matter = :#{#info.matter},d.matterType =:#{#info.matterType}," +
            "d.updateTime = now() where id =:#{#info.id}")
    void update(@Param("info") DistributionGoodsMatter info);

    @Modifying
    @Query("update DistributionGoodsMatter d set d.recommend = :#{#info.recommend} where id =:#{#info.id}")
    void updataRecomendNumById(@Param("info") DistributionGoodsMatter info);

    @Query("from DistributionGoodsMatter where id =?1 and delFlag = 0")
    DistributionGoodsMatter queryById(String id);

}
