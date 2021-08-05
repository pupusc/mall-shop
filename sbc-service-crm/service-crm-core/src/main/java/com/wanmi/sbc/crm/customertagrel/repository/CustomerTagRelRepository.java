package com.wanmi.sbc.crm.customertagrel.repository;

import com.wanmi.sbc.crm.customertagrel.model.root.CustomerTagRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>会员标签关联DAO</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@Repository
public interface CustomerTagRelRepository extends JpaRepository<CustomerTagRel, Long>,
        JpaSpecificationExecutor<CustomerTagRel> {

    void deleteByTagId(Long tagId);

    void deleteByCustomerId(String customerId);
}
