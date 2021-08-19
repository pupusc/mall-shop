package com.wanmi.sbc.customer.paidcardbuyrecord.repository;

import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>付费会员DAO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@Repository
public interface PaidCardBuyRecordRepository extends JpaRepository<PaidCardBuyRecord, String>,
        JpaSpecificationExecutor<PaidCardBuyRecord> {

}
