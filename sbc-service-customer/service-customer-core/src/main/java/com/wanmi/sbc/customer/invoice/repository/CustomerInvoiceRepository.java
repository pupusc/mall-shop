package com.wanmi.sbc.customer.invoice.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户增专信息数据层
 * Created by CHENLI on 2017/4/21.
 */
@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long>,
        JpaSpecificationExecutor<CustomerInvoice> {

    /**
     * 根据会员ID查询客户的增专票信息
     *
     * @param customerId
     * @return
     */
    @Query(value = "from CustomerInvoice e where e.delFlag = 0 and e.customerId = :customerId and e.checkState = 1")
    Optional<CustomerInvoice> findByCustomerId(@Param("customerId") String customerId);

    /**
     * 根据会员ID查询客户的增专票信息
     *
     * @param customerId
     * @return
     */
    Optional<CustomerInvoice> findByCustomerIdAndDelFlag(String customerId, DeleteFlag delFlag);

    /**
     * 根据会员ID查询客户的增专票信息
     *
     * @param customerId
     * @return
     */
    Optional<CustomerInvoice> findByCustomerIdAndCheckState(String customerId, CheckState checkState);

    /**
     * 根据增专票ID查询客户的增专票信息
     *
     * @param customerInvoiceId
     * @param deleteFlag
     * @return
     */
    Optional<CustomerInvoice> findByCustomerInvoiceIdAndDelFlag(Long customerInvoiceId, DeleteFlag deleteFlag);

    /**
     * 根据纳税人识别号查询客户的增专票信息
     *
     * @param taxpayerNumber
     * @param deleteFlag
     * @return
     */
    Optional<CustomerInvoice> findByTaxpayerNumberAndDelFlag(String taxpayerNumber, DeleteFlag deleteFlag);

    /**
     * 批量删除 增专票信息
     *
     * @param ids
     */
    @Query("update CustomerInvoice e set e.delFlag = 1 where e.delFlag = 0 and e.customerInvoiceId in :ids")
    @Modifying
    void deleteCustomerInvoicesByIds(@Param("ids") List<Long> ids);

    /**
     * 批量删除 根据会员id
     *
     * @param customerIds customerIds
     * @return rows
     */
    @Query("update CustomerInvoice c set c.delFlag = 1 where c.delFlag = 0 and c.customerId in :customerIds")
    @Modifying
    int deleteCustomerInvoiceByCustomerIds(@Param("customerIds") List<String> customerIds);

    /**
     * 单条 / 批量审核 增专票信息
     *
     * @param ids
     */
    @Query("update CustomerInvoice e set e.checkState = :checkState where e.customerInvoiceId in :ids")
    @Modifying
    void checkCustomerInvoice(@Param("checkState") CheckState checkState, @Param("ids") List<Long> ids);

    /**
     * 批量作废 增专票信息
     *
     * @param ids
     */
    @Query("update CustomerInvoice e set e.invalidFlag = :invalidFlag , e.checkState=2 where e.customerInvoiceId in " +
            ":ids")
    @Modifying
    void invalidCustomerInvoice(@Param("invalidFlag") InvalidFlag invalidFlag, @Param("ids") List<Long> ids);

    /**
     * 驳回增专票信息
     *
     * @param rejectReason      rejectReason
     * @param customerInvoiceId customerInvoiceId
     * @return rows
     */
    @Modifying
    @Query("update CustomerInvoice e set e.rejectReason = :rejectReason" +
            ", e.checkState = :checkState where e.customerInvoiceId = :customerInvoiceId")
    int rejectInvoice(@Param("rejectReason") String rejectReason,
                      @Param("customerInvoiceId") Long customerInvoiceId,
                      @Param("checkState") CheckState checkState
    );
}
