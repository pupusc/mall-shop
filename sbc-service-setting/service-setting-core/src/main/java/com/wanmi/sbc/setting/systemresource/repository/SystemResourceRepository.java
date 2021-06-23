package com.wanmi.sbc.setting.systemresource.repository;

import com.wanmi.sbc.setting.systemresource.model.root.SystemResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>平台素材资源DAO</p>
 *
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@Repository
public interface SystemResourceRepository extends JpaRepository<SystemResource, Long>,
        JpaSpecificationExecutor<SystemResource> {

    /**
     * 单个删除平台素材资源
     *
     * @author lq
     */
    @Modifying
    @Query("update SystemResource set delFlag = 1 where resourceId = ?1")
    void deleteById(Long resourceId);

    /**
     * 批量删除平台素材资源
     *
     * @author lq
     */
    @Modifying
    @Query("update SystemResource set delFlag = 1 ,updateTime = now() where resourceId in ?1")
    int deleteByIdList(List<Long> resourceIdList);

    /**
     * 根据多个ID编号更新分类
     *
     * @param cateId      分类ID
     * @param resourceIds 素材资源ID
     */
    @Modifying
    @Query("update SystemResource w set w.cateId = ?1 ,w.updateTime = now() where w.resourceId in ?2 ")
    void updateCateByIds(Long cateId, List<Long> resourceIds);

    /**
     * 根据多个分类编号更新分类
     *
     * @param cateId  分类ID
     * @param cateIds 分类ID
     */
    @Modifying
    @Query("update SystemResource w set w.cateId = ?1 ,w.updateTime = now() where w.cateId in ?2")
    void updateCateByCateIds(Long cateId, List<Long> cateIds);

    /**
     * 根据资源id获取
     *
     * @param resourceId
     * @return
     */
    SystemResource findByResourceId(Long resourceId);

}
