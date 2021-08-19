package com.wanmi.sbc.account.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenli on 2017/12/12.
 */
@Repository
public interface InvoiceProjectSwitchRepository extends JpaRepository<InvoiceProjectSwitch, Long> {
    /**
     * 根据商家id查询商家的开票类型
     * @param companyInfoId
     * @return
     */
    InvoiceProjectSwitch findByCompanyInfoId(Long companyInfoId);

    /**
     * 根据商家id集合查询商家支持开票类型
     * @param companyInfoIds
     * @return
     */
    @Query("from InvoiceProjectSwitch c where c.companyInfoId in (:companyInfoIds)")
    List<InvoiceProjectSwitch> findSupportInvoice(@Param("companyInfoIds") List<Long> companyInfoIds);
}
