package com.wanmi.sbc.setting.storeresourcecate.repository;

import com.wanmi.sbc.setting.storeresourcecate.model.root.StoreResourceCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>店铺资源资源分类表DAO</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@Repository
public interface StoreResourceCateRepository extends JpaRepository<StoreResourceCate, Long>,
        JpaSpecificationExecutor<StoreResourceCate> {

    /**
     * 单个删除店铺资源资源分类表
     * @author lq
     */
    @Modifying
    @Query("update StoreResourceCate set delFlag = 1 where cateId = ?1")
    void deleteById(Long cateId);

    /**
     * 批量删除店铺资源资源分类表
     * @author lq
     */
    @Modifying
    @Query("update StoreResourceCate set delFlag = 1 where cateId in ?1")
    int deleteByIdList(List<Long> cateIdList);

    /**
     * 查询
     * @param cateId 素材分类id
     * @param storeId 店铺id
     * @return
     */
    StoreResourceCate findByCateIdAndStoreId(Long cateId, Long storeId);


}
