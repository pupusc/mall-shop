package com.wanmi.sbc.setting.systemresourcecate.repository;

import com.wanmi.sbc.setting.systemresourcecate.model.root.SystemResourceCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>平台素材资源分类DAO</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@Repository
public interface SystemResourceCateRepository extends JpaRepository<SystemResourceCate, Long>,
        JpaSpecificationExecutor<SystemResourceCate> {

    /**
     * 单个删除平台素材资源分类
     * @author lq
     */
    @Modifying
    @Query("update SystemResourceCate set delFlag = 1 where cateId = ?1")
    void deleteById(Long cateId);

    /**
     * 批量删除平台素材资源分类
     * @author lq
     */
    @Modifying
    @Query("update SystemResourceCate set delFlag = 1 where cateId in ?1")
    int deleteByIdList(List<Long> cateIdList);

    /**
     * 根据cateId查询素材分类
     * @param cateId
     * @return
     */
    SystemResourceCate findByCateId(Long cateId);

}
