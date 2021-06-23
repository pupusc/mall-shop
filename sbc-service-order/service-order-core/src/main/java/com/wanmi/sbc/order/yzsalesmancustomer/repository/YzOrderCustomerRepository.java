package com.wanmi.sbc.order.yzsalesmancustomer.repository;

import com.wanmi.sbc.order.yzsalesmancustomer.model.root.YzOrderCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface YzOrderCustomerRepository extends JpaRepository<YzOrderCustomer, Long>,
        JpaSpecificationExecutor<YzOrderCustomer>{
}
