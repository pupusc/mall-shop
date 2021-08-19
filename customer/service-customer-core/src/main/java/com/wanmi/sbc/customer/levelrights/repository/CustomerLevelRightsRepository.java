package com.wanmi.sbc.customer.levelrights.repository;

import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>会员等级权益表DAO</p>
 */
@Repository
public interface CustomerLevelRightsRepository extends JpaRepository<CustomerLevelRights, Integer>,
        JpaSpecificationExecutor<CustomerLevelRights> {

    /**
     * 查询权益列表
     *
     * @return
     */
    @Query("from CustomerLevelRights c where c.delFlag = '0' and c.rightsId = ?1")
    CustomerLevelRights findByRightsId(Integer rightsId);

    /**
     * 查询权益列表
     *
     * @return
     */
    @Query("from CustomerLevelRights c where c.delFlag = '0' order by c.sort,c.createTime")
    List<CustomerLevelRights> findAllList();

    /**
     * 查询除自身外的名称是否存在(判重)
     *
     * @param rightsId   权益id
     * @param rightsName 权益名称
     * @return
     */
    @Query("from CustomerLevelRights c where c.delFlag = '0' and c.rightsId <> ?1  " +
            "and c.rightsName = ?2 ")
    List<CustomerLevelRights> findByRightsNameNotSelf(Integer rightsId, String rightsName);

    /**
     * 删除权益
     */
    @Modifying
    @Query("update CustomerLevelRights c set c.delFlag = 1 where c.rightsId = ?1")
    void deleteRightsById(Integer rightsId);

}
