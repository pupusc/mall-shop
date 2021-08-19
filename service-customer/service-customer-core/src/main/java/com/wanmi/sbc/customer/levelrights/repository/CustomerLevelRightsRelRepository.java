package com.wanmi.sbc.customer.levelrights.repository;

import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>会员等级权益关联表DAO</p>
 */
@Repository
public interface CustomerLevelRightsRelRepository extends JpaRepository<CustomerLevelRightsRel, Integer>,
        JpaSpecificationExecutor<CustomerLevelRightsRel> {
    /**
     * 根据权益id查询
     *
     * @param rightsId 权益ID
     * @return
     */
    @Query("from CustomerLevelRightsRel c where c.rightsId = ?1")
    List<CustomerLevelRightsRel> findByRightsId(Integer rightsId);

    /**
     * 根据用户等级编号查询
     *
     * @return
     */
    List<CustomerLevelRightsRel> findByCustomerLevelId(Long customerLevelId);
}
