package com.wanmi.sbc.setting.storeresource.repository;

import com.wanmi.sbc.setting.storeresource.model.root.StoreResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>店铺资源库DAO</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@Repository
public interface StoreResourceRepository extends JpaRepository<StoreResource, Long>,
        JpaSpecificationExecutor<StoreResource> {

    /**
     * 单个删除店铺资源库
     * @author lq
     */
    @Modifying
    @Query("update StoreResource set delFlag = 1 where resourceId = ?1")
    void deleteById(Long resourceId);

    /**
     * 批量删除店铺资源库
     * @author lq
     */
    @Modifying
    @Query("update StoreResource set delFlag = 1 ,updateTime = now() where resourceId in ?1  and storeId = ?2")
    int deleteByIdList(List<Long> resourceIdList, Long storeId);

    /**
     * 根据多个ID编号更新分类
     *
     * @param cateId   分类ID
     * @param resourceIds 素材ID
     */
    @Modifying
    @Query("update StoreResource w set w.cateId = ?1 ,w.updateTime = now() where w.resourceId in ?2 and w.storeId = ?3")
    void updateCateByIds(Long cateId, List<Long> resourceIds, Long storeId);

    /**
     * 根据多个分类编号更新分类
     *
     * @param cateId   分类ID
     * @param cateIds 分类ID
     */
    @Modifying
    @Query("update StoreResource w set w.cateId = ?1 ,w.updateTime = now() where w.cateId in ?2 and w.storeId = ?3")
    void updateCateByCateIds(Long cateId, List<Long> cateIds, Long storeId);

    StoreResource findByResourceIdAndStoreId(Long resourceID,  Long storeId);


}
