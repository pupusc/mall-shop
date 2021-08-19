package com.wanmi.sbc.customer.storesharerecord.repository;

import com.wanmi.sbc.customer.storesharerecord.model.root.StoreShareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * <p>商城分享DAO</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@Repository
public interface StoreShareRecordRepository extends JpaRepository<StoreShareRecord, Long>,
        JpaSpecificationExecutor<StoreShareRecord> {

}
