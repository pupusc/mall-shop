package com.wanmi.sbc.crm.autotag.repository;

import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>自动标签DAO</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Repository
public interface AutoTagRepository extends JpaRepository<AutoTag, Long>,
        JpaSpecificationExecutor<AutoTag> {

    /**
     * 单个删除自动标签
     * @author dyt
     */
    @Modifying
    @Query("update AutoTag set delFlag = 1, updateTime = now() where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除自动标签
     * @author dyt
     */
    @Modifying
    @Query("update AutoTag set delFlag = 1, updateTime = now() where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<AutoTag> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    @Query("select count(a.id) from AutoTag a where a.id in ?1 and a.delFlag = 0")
    int countByIds(List<Long> ids);
}
