package com.wanmi.sbc.crm.customertag.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>会员标签DAO</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@Repository
public interface CustomerTagRepository extends JpaRepository<CustomerTag, Long>,
        JpaSpecificationExecutor<CustomerTag> {

    /**
     * 单个删除会员标签
     * @author zhanglingke
     */
    @Modifying
    @Query("update CustomerTag set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除会员标签
     * @author zhanglingke
     */
    @Modifying
    @Query("update CustomerTag set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    /**
     * 按名称查询
     * @param name
     * @param deleteFlag
     * @return
     */
    Optional<CustomerTag> findByNameAndDelFlag(String name, DeleteFlag deleteFlag);

    /**
     * 查询数量
     * @param ids
     * @param delFlag
     * @return
     */
    @Query("select  count(c.id) from CustomerTag c where c.delFlag=:delFlag and  c.id in (:ids)")
    int findCountByIdListAndDelFlag(@Param("ids")List<Long> ids,@Param("delFlag")DeleteFlag delFlag);

}
