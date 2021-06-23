package com.wanmi.sbc.customer.paidcardrightsrel.repository;

import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>付费会员DAO</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@Repository
public interface PaidCardRightsRelRepository extends JpaRepository<PaidCardRightsRel, String>,
        JpaSpecificationExecutor<PaidCardRightsRel> {

    void deleteByPaidCardId(String id);
}
