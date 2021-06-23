package com.wanmi.sbc.customer.customersignrecord.repository;

import com.wanmi.sbc.customer.customersignrecord.model.root.CustomerSignRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>用户签到记录DAO</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@Repository
public interface CustomerSignRecordRepository extends JpaRepository<CustomerSignRecord, String>,
        JpaSpecificationExecutor<CustomerSignRecord> {

    /**
     * 单个删除用户签到记录
     * @author wangtao
     */
    @Modifying
    @Query("update CustomerSignRecord set delFlag = 1 where signRecordId = ?1")
    void deleteById(String signRecordId);

    /**
     * 批量删除用户签到记录
     * @author wangtao
     */
    @Modifying
    @Query("update CustomerSignRecord set delFlag = 1 where signRecordId in ?1")
    int deleteByIdList(List<String> signRecordIdList);

    @Query("from CustomerSignRecord where customerId = ?1 " +
            " and date_format(sign_record, '%Y-%m') = date_format(now(),'%Y-%m')")
    List<CustomerSignRecord> listByMonth(String customerId);

    @Query("from CustomerSignRecord where customerId = ?1 and to_days(now()) - to_days(sign_record) = ?2")
    CustomerSignRecord getRecordByDays(String customerId, Long days);
}
