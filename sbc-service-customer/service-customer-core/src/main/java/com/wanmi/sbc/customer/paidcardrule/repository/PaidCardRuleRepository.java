package com.wanmi.sbc.customer.paidcardrule.repository;

import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <p>付费会员DAO</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@Repository
public interface PaidCardRuleRepository extends JpaRepository<PaidCardRule, String>,
        JpaSpecificationExecutor<PaidCardRule> {

    Integer countByErpSkuCodeIn(List<String> skuCodeList);

    @Query(" select count (r.id) from PaidCardRule r where r.paidCardId <> ?1 and r.erpSkuCode in ?2")
    Integer countByErpSkuCode4Modify(String id, List<String> skuCodeList);

    @Modifying
    void deleteByPaidCardId(String id);
}
