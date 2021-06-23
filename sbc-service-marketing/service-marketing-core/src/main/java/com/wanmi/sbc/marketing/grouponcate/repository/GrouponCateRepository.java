package com.wanmi.sbc.marketing.grouponcate.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.marketing.grouponcate.model.root.GrouponCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>拼团分类信息表DAO</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@Repository
public interface GrouponCateRepository extends JpaRepository<GrouponCate, String>,
        JpaSpecificationExecutor<GrouponCate> {
    /**
     * 查询拼团分类列表
     *
     * @return
     */
    @Query(" from GrouponCate c where c.delFlag = '0' order by c.defaultCate desc, c.cateSort asc, c.createTime desc")
    List<GrouponCate> listGrouponCate();

    /**
     * 除自己外，根据分类名称查询其他拼团分类是否存在
     *
     * @param grouponCateId   拼团分类ID
     * @param grouponCateName 拼团分类名称
     * @return
     */
    @Query("select count(1) from GrouponCate c where c.delFlag = '0' and c.grouponCateId <> ?1 and c.grouponCateName = ?2")
    Integer findByNameExceptSelf(String grouponCateId, String grouponCateName);

    /**
     * 根据拼团分类ID和删除标志查询拼团分类信息
     *
     * @param grouponCateId
     * @param delFlag
     * @return
     */
    GrouponCate findByGrouponCateIdAndDelFlag(String grouponCateId, DeleteFlag delFlag);

    /**
     * 拼团分类排序
     *
     * @param storeCateId 拼团分类Id
     * @param cateSort    拼团分类顺序
     */
    @Modifying
    @Query(" update GrouponCate c set c.cateSort = ?2 where c.grouponCateId = ?1 ")
    void updateCateSort(String storeCateId, Integer cateSort);

    /**
     * 根据拼团分类ID集合批量查询拼团分类信息
     * @param grouponCateIds
     * @return
     */
    List<GrouponCate> findByGrouponCateIdIn(List<String> grouponCateIds);
}
