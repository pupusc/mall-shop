package com.wanmi.sbc.message.pushcustomerenable.repository;

import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p>会员推送通知开关DAO</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@Repository
public interface PushCustomerEnableRepository extends JpaRepository<PushCustomerEnable, String>,
        JpaSpecificationExecutor<PushCustomerEnable> {

    Optional<PushCustomerEnable> queryByCustomerId(String customerId);
}
